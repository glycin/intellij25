package com.glycin.intelli25

import com.glycin.intelli25.input.GameKeyListener
import com.glycin.intelli25.input.NoOpEditorActionHandler
import com.glycin.intelli25.managers.AttackManager
import com.glycin.intelli25.managers.CollisionsManager
import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.model.*
import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.ui.ToolWindowBaseComponent
import com.glycin.intelli25.ui.UiComponent
import com.glycin.intelli25.ui.screens.*
import com.glycin.intelli25.upgrades.BasicAttack
import com.glycin.intelli25.upgrades.UpgradeRepository
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.PNG
import com.glycin.intelli25.util.getDeltaTime
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.EDT
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.editor.actionSystem.EditorActionManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.ToolWindow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.KeyboardFocusManager
import java.awt.event.MouseWheelListener
import kotlin.math.max

private const val FPS = 120L

class Game(
    private val project: Project,
    private val editor: Editor,
    private val scope: CoroutineScope,
    private val toolWindow: ToolWindow,
    private val toolWindowBaseComponent: ToolWindowBaseComponent,
    gameStartupSettings: GameStartupSettings,
) {

    private var gameComponent: GameComponent? = null
    private var uiComponent: UiComponent? = null
    private var keyListener: GameKeyListener? = null
    private var dialogueScreenWrapper: DialogueScreenWrapper? = null
    private var mouseWheelBlocker: MouseWheelListener? = null //TODO: Workaround until I find a good way to pin the UI as the user scrolls around

    // Store original action handlers to restore them later
    private val originalActionHandlers = mutableMapOf<String, EditorActionHandler>()

    private lateinit var player: Player
    private lateinit var ggState: GameGlobalState
    private lateinit var upgradeRepository: UpgradeRepository
    private lateinit var attackManager: AttackManager
    private lateinit var collisionsManager: CollisionsManager
    private lateinit var enemyManager: EnemyManager

    init {
        scope.launch(Dispatchers.EDT) {
            disableDefaultEditorCaretActions()

            val caretModel = editor.caretModel
            caretModel.moveToOffset(0)
            caretModel.removeSecondaryCarets()

            val scrollingModel = editor.scrollingModel
            scrollingModel.scrollToCaret(ScrollType.CENTER_UP)

            delay(250) // Give the editor time to scroll up

            val maxX = scrollingModel.visibleArea.width
            val maxY = scrollingModel.visibleArea.height

            ggState = GameGlobalState(
                minX = 0,
                minY = 0,
                maxX = maxX,
                maxY = maxY,
                deltaTime = FPS.getDeltaTime(),
                gameDuration = gameStartupSettings.gameDuration,
                chosenGameLevel = gameStartupSettings.chosenGameLevel,
            )

            player = Player(
                position = Vec2((ggState.maxX / 2f) - 25, (ggState.maxY / 2f) + 25),
                ggState = ggState,
                width = 64,
                height = 64,
                onLevelUp = {
                    uiComponent?.showUpgradePopup(generateUpgradeOptions())
                },
                onDeath = {
                    scope.launch(Dispatchers.EDT) {
                        stopGame()
                        GameOverScreenWrapper(
                            screen = GameOverScreen.getGameOverScreen(ggState, player),
                            toolWindow = toolWindow,
                            project = project,
                        ).show()
                    }
                }
            )

            mouseWheelBlocker = MouseWheelListener { e -> e.consume() }.also {
                editor.contentComponent.addMouseWheelListener(it)
            }

            attackManager = AttackManager(ggState).also {
                val basicAttack = BasicAttack(ggState, player, scope).apply { activate() }
                it.addAttack(basicAttack)
                player.upgrades[basicAttack.title] = UpgradeBackpackItem(basicAttack.attackIcon, 1)
            }

            val saveState = service<GameSaveState>()
            val seenEnemies = if(saveState.enemiesSeen.isNotEmpty()) saveState.enemiesSeen.split(",").map { EnemyType.valueOf(it) }.toMutableSet() else mutableSetOf()
            enemyManager = EnemyManager(ggState, seenEnemies, player, scope)

            collisionsManager = CollisionsManager(player, enemyManager, attackManager, ggState)
            upgradeRepository = UpgradeRepository(ggState, player, enemyManager, scope)

            gameComponent = GameComponent(ggState, player, attackManager, enemyManager, scope) {
                when(ggState.chosenGameLevel) {
                    0, 1 -> levelOneBeaten()
                    2 -> levelTwoBeaten()
                    3 -> levelThreeBeaten()
                    else -> finalLevelBeaten()
                }
            }.also { gc ->
                gc.bounds = editor.contentComponent.bounds
                gc.isOpaque = false
                gc.isFocusable = true
                gc.requestFocusInWindow()
            }

            val uiComponent = UiComponent(
                project = project,
                player = player,
                ggState = ggState,
                scope = scope,
            ).also { uic ->
                uic.bounds = editor.contentComponent.bounds
                uic.isOpaque = false
            }
            this@Game.uiComponent = uiComponent

            editor.contentComponent.let { c ->
                c.add(gameComponent)
                c.add(uiComponent)
                c.setComponentZOrder(uiComponent, 0)
                c.setComponentZOrder(gameComponent, 1)

                editor.scrollingModel.addVisibleAreaListener { e ->
                    val visibleRect = e.newRectangle
                    ggState.maxX = visibleRect.width
                    ggState.maxY = visibleRect.height
                    ggState.minX = visibleRect.x
                    ggState.minY = visibleRect.y
                    uiComponent.updateBounds(visibleRect)
                    gameComponent?.bounds = visibleRect
                    uiComponent.revalidate()
                    uiComponent.repaint()
                    gameComponent?.revalidate()
                    gameComponent?.repaint()
                    collisionsManager.updateGridBounds()
                }
                c.repaint()
                c.revalidate()
            }

            keyListener = GameKeyListener(player, uiComponent, this@Game).also {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(it)
            }

            initGameConfirmationDialogListener(uiComponent)

            uiComponent.showGameUi()

            postInit()
        }
    }

    /**
     * In [GameKeyListener] we intercept the arrow key events (UP, DOWN, LEFT, RIGHT).
     * However, just that is not enough.
     * We also need to disable the default editor caret actions to prevent caret movement
     * while the game is running. This is because the arrow keys are also used for
     * navigating the editor, and we want to prevent that while the game is running.
     *
     * Just returning `true` from the `dispatchKeyEvent` method seems to not work as IntelliJ has a special handling for arrow keys
     */
    private fun disableDefaultEditorCaretActions() {
        // Override arrow key action handlers to prevent caret movement
        val editorActionManager = EditorActionManager.getInstance()

        // List of editor actions to mute while the game is running
        val editorCaretActionsToDisable: List<String> = listOf(
            // Arrow key movements
            IdeActions.ACTION_EDITOR_MOVE_CARET_UP,
            IdeActions.ACTION_EDITOR_MOVE_CARET_DOWN,
            IdeActions.ACTION_EDITOR_MOVE_CARET_LEFT,
            IdeActions.ACTION_EDITOR_MOVE_CARET_RIGHT,

            IdeActions.ACTION_EDITOR_MOVE_CARET_UP_WITH_SELECTION,
            IdeActions.ACTION_EDITOR_MOVE_CARET_DOWN_WITH_SELECTION,
            IdeActions.ACTION_EDITOR_MOVE_CARET_LEFT_WITH_SELECTION,
            IdeActions.ACTION_EDITOR_MOVE_CARET_RIGHT_WITH_SELECTION,
        )

        editorCaretActionsToDisable.forEach { actionId ->
            val originalAction: EditorActionHandler = editorActionManager.getActionHandler(actionId)
            originalActionHandlers[actionId] = originalAction

            val noOpHandler = NoOpEditorActionHandler(editor, originalAction)
            editorActionManager.setActionHandler(actionId, noOpHandler)
        }
    }

    private fun restoreOriginalEditorCaretActions() {
        val actionManager = EditorActionManager.getInstance()
        originalActionHandlers.forEach { (actionId, handler) ->
            actionManager.setActionHandler(actionId, handler)
        }
        originalActionHandlers.clear()
    }

    private fun initGameConfirmationDialogListener(uiComponent: UiComponent) {
        val listener = ShowStopGameConfirmationDialogOnEditorCloseListener(
            project,
            this,
            ggState,
            editor,
            uiComponent
        )

        // Register editor close listener to show a confirmation dialog
        val projectMessageBus = project.messageBus.connect()
        projectMessageBus.subscribe(FileEditorManagerListener.Before.FILE_EDITOR_MANAGER, listener)

        // Register a project closing listener to track when a project is being closed.
        // If an IDE is closed, it will also auto-close all projects
        val appMessageBus = ApplicationManager.getApplication().messageBus.connect()
        appMessageBus.subscribe(ProjectManager.TOPIC, listener)
    }

    private fun postInit() {
        // Rendering loop
        scope.launch(Dispatchers.Default) {
            while (ggState.gameActive) {
                editor.contentComponent.repaint()
                delay(ggState.deltaTime)
            }
        }

        var doPhysics = true
        // Gameplay loop
        scope.launch (Dispatchers.Default) {
            while(ggState.gameActive) {
                player.update()
                attackManager.update()
                enemyManager.update()

                // Check for collisions at half the rate we update the rest of the game
                if(doPhysics) {
                    collisionsManager.update()
                }
                doPhysics = !doPhysics
                delay(ggState.deltaTime)
            }
        }
    }

    private fun generateUpgradeOptions(): List<UpgradeOption> {
        return upgradeRepository.getRandomUpgrades(attackManager)
    }

    fun stopGameWithoutEditorCleanups() {
        uiComponent?.dispose()

        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyListener)

        dialogueScreenWrapper = null
        ggState.gameActive = false

        project.getService(GameService::class.java).resetGame()
    }


    fun stopGame() {
        stopGameWithoutEditorCleanups()

        cleanUpEditor()
    }

    private fun cleanUpEditor() {
        restoreOriginalEditorCaretActions()

        editor.contentComponent.remove(uiComponent)
        editor.contentComponent.remove(gameComponent)
        editor.contentComponent.revalidate()
        editor.contentComponent.repaint()

        mouseWheelBlocker?.let { mwb ->
            editor.contentComponent.removeMouseWheelListener(mwb)
        }
        mouseWheelBlocker = null
    }

    private fun levelOneBeaten() {
        stopGame()
        val saveState = service<GameSaveState>()
        saveState.levelsBeaten = max(1, saveState.levelsBeaten)
        toolWindowBaseComponent.showScreen()
        scope.launch(Dispatchers.EDT) {
            if(saveState.dialoguesSeen < 2) {
                val dialogueScreen = DialogueScreen(
                    title = "2001-2009",
                    texts = CutsceneTexts.screenTwo,
                    scope = scope,
                    backGroundImages = mapOf(0 to PNG.STORY_SCREEN_2),
                    onReadyToStart = {
                        saveState.dialoguesSeen++
                        dialogueScreenWrapper?.enableOk()
                    }
                )

                dialogueScreenWrapper = DialogueScreenWrapper(project, "Continue!", dialogueScreen)
                dialogueScreenWrapper?.let { wrapper ->
                    if(wrapper.showAndGet()){
                        showGameSurvivedScreen()
                    }
                }
            } else {
                showGameSurvivedScreen()
            }
        }
    }

    private fun levelTwoBeaten() {
        stopGame()
        val saveState = service<GameSaveState>()
        saveState.levelsBeaten = max(2, saveState.levelsBeaten)
        toolWindowBaseComponent.showScreen()
        scope.launch(Dispatchers.EDT) {
            if(saveState.dialoguesSeen < 3) {
                val dialogueScreen = DialogueScreen(
                    title = "2010-2017",
                    texts = CutsceneTexts.screenThree,
                    scope = scope,
                    backGroundImages = mapOf(0 to PNG.STORY_SCREEN_3),
                    onReadyToStart = {
                        saveState.dialoguesSeen++
                        dialogueScreenWrapper?.enableOk()
                    }
                )

                dialogueScreenWrapper = DialogueScreenWrapper(project, "Continue!", dialogueScreen)
                dialogueScreenWrapper?.let { wrapper ->
                    if(wrapper.showAndGet()){
                        showGameSurvivedScreen()
                    }
                }
            } else {
                showGameSurvivedScreen()
            }
        }
    }

    private fun levelThreeBeaten() {
        stopGame()
        val saveState = service<GameSaveState>()
        saveState.levelsBeaten = max(3, saveState.levelsBeaten)
        toolWindowBaseComponent.showScreen()

        scope.launch(Dispatchers.EDT) {
            if (saveState.dialoguesSeen < 4) {
                val dialogueScreen = DialogueScreen(
                    title = "2018-2026",
                    texts = CutsceneTexts.screenFour,
                    scope = scope,
                    backGroundImages = mapOf(0 to PNG.STORY_SCREEN_4, 4 to PNG.STORY_SCREEN_5),
                    onReadyToStart = {
                        saveState.dialoguesSeen++
                        dialogueScreenWrapper?.enableOk()
                    },
                )
                dialogueScreenWrapper = DialogueScreenWrapper(
                    project = project,
                    startButtonText = "Continue!",
                    dialogue = dialogueScreen,
                )
                dialogueScreenWrapper?.let { wrapper ->
                    if(wrapper.showAndGet()){
                        showGameSurvivedScreen()
                    }
                }
            } else {
                showGameSurvivedScreen()
            }
        }
    }

    private fun finalLevelBeaten() {
        stopGame()
    }

    private fun showGameSurvivedScreen() {
        GameOverScreenWrapper(
            screen = GameOverScreen.getSurvivedScreen(ggState, player),
            toolWindow = toolWindow,
            project = project,
        ).show()
    }
}