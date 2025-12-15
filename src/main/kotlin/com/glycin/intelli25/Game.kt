package com.glycin.intelli25

import com.glycin.intelli25.input.GameKeyListener
import com.glycin.intelli25.managers.AttackManager
import com.glycin.intelli25.managers.CollisionsManager
import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.model.GameStartupSettings
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.ui.ToolWindowBaseComponent
import com.glycin.intelli25.ui.UiComponent
import com.glycin.intelli25.ui.screens.CutsceneTexts
import com.glycin.intelli25.ui.screens.DialogueScreen
import com.glycin.intelli25.ui.screens.DialogueScreenWrapper
import com.glycin.intelli25.ui.screens.GameOverScreen
import com.glycin.intelli25.ui.screens.GameOverScreenWrapper
import com.glycin.intelli25.upgrades.BasicAttack
import com.glycin.intelli25.upgrades.UpgradeRepository
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.PNG
import com.glycin.intelli25.util.getDeltaTime
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.EDT
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.wm.ToolWindow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.KeyboardFocusManager

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

    private lateinit var ggState: GameGlobalState
    private lateinit var upgradeRepository: UpgradeRepository
    private lateinit var attackManager: AttackManager
    private lateinit var collisionsManager: CollisionsManager

    init {
        scope.launch(Dispatchers.EDT) {
            val maxX = editor.scrollingModel.visibleArea.width
            val maxY = editor.scrollingModel.visibleArea.height

            ggState = GameGlobalState(
                minX = 0,
                minY = 0,
                maxX = maxX,
                maxY = maxY,
                deltaTime = FPS.getDeltaTime(),
                gameDuration = gameStartupSettings.gameDuration,
                chosenGameLevel = gameStartupSettings.chosenGameLevel,
            )

            val player = Player(
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
                            screen = GameOverScreen.getGameOverScreen(ggState),
                            toolWindow = toolWindow,
                            project = project,
                        ).show()
                    }
                }
            )

            keyListener = GameKeyListener(player).also {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(it)
            }

            attackManager = AttackManager(ggState, scope).also {
                val basicAttack = BasicAttack(ggState, player, scope).apply { activate() }
                it.addAttack(basicAttack)
                player.upgrades[basicAttack.title] = basicAttack.attackIcon
            }
            val enemyManager = EnemyManager(ggState, player, scope)
            collisionsManager = CollisionsManager(player, enemyManager, attackManager, ggState, scope) //TODO: Create one update manager that handles all updating in the game
            upgradeRepository = UpgradeRepository(ggState, player, enemyManager, scope)

            gameComponent = GameComponent(ggState, player, attackManager, enemyManager, scope) {
                when(ggState.chosenGameLevel) {
                    0, 1, 2 -> levelOneTwoBeaten()
                    3 -> levelThreeBeaten()
                    else -> finalLevelBeaten()
                }
            }.also { gc ->
                gc.bounds = editor.contentComponent.bounds
                gc.isOpaque = false
                gc.isFocusable = true
                gc.requestFocusInWindow()
            }

            uiComponent = UiComponent(player, ggState, scope).also { uic ->
                uic.bounds = editor.contentComponent.bounds
                uic.isOpaque = false
            }

            editor.contentComponent.let { c ->
                c.add(gameComponent)
                c.add(uiComponent)
                c.setComponentZOrder(uiComponent, 0)
                c.setComponentZOrder(gameComponent, 1)

                editor.scrollingModel.addVisibleAreaListener { e ->
                    val visibleRect = e.newRectangle
                    ggState.maxX = visibleRect.width
                    ggState.maxY = visibleRect.height
                    uiComponent?.updateBounds(visibleRect)
                    gameComponent?.bounds = visibleRect
                    uiComponent?.revalidate()
                    uiComponent?.repaint()
                    gameComponent?.revalidate()
                    gameComponent?.repaint()
                }
                c.repaint()
                c.revalidate()
            }

            uiComponent?.showGameUi()
        }
    }

    private fun generateUpgradeOptions(): List<UpgradeOption> {
        return upgradeRepository.getRandomUpgrades(attackManager)
    }

    private fun stopGame() {
        editor.contentComponent.remove(uiComponent)
        editor.contentComponent.remove(gameComponent)
        editor.contentComponent.revalidate()
        editor.contentComponent.repaint()
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyListener)
        ggState.gameActive = false
        project.getService(GameService::class.java).resetGame()
    }

    private fun levelOneTwoBeaten() {
        stopGame()
        val saveState = service<GameSaveState>()
        saveState.levelsBeaten++
        toolWindowBaseComponent.showScreen(saveState.levelsBeaten)
        scope.launch(Dispatchers.EDT) {
            GameOverScreenWrapper(
                screen = GameOverScreen.getSurvivedScreen(ggState),
                toolWindow = toolWindow,
                project = project,
            ).show()
        }
    }

    private fun levelThreeBeaten() {
        stopGame()
        val saveState = service<GameSaveState>()
        saveState.levelsBeaten++
        var wrapper : DialogueScreenWrapper? = null
        scope.launch(Dispatchers.EDT) {
            val dialogueScreen = DialogueScreen(
                title = "Party time!",
                texts = CutsceneTexts.screenFour,
                scope = scope,
                backGroundImages = mapOf(0 to PNG.STORY_SCREEN_4, 8 to PNG.STORY_SCREEN_5),
                onReadyToStart = {
                    toolWindow.show()
                    wrapper?.enableOk()
                },
            )
            wrapper = DialogueScreenWrapper(
                project = project,
                dialogue = dialogueScreen,
            ).also { it.show() }
        }
    }

    private fun finalLevelBeaten() {
        stopGame()
    }
}