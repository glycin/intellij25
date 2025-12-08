package com.glycin.intelli25

import com.glycin.intelli25.input.GameKeyListener
import com.glycin.intelli25.managers.AttackManager
import com.glycin.intelli25.managers.CollisionsManager
import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.ui.ToolWindowBaseComponent
import com.glycin.intelli25.ui.UiComponent
import com.glycin.intelli25.upgrades.BasicAttack
import com.glycin.intelli25.upgrades.UpgradeRepository
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.getDeltaTime
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.EDT
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.KeyboardFocusManager
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener

private const val FPS = 120L
private const val GAME_DURATION = 10 * 60 * 1000L // 10 Minutes

class Game(
    private val project: Project,
    private val editor: Editor,
    private val scope: CoroutineScope,
    private val toolWindow: ToolWindow,
    private val toolWindowBaseComponent: ToolWindowBaseComponent,
): Disposable {

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

            ggState = GameGlobalState(0, 0, maxX, maxY, FPS.getDeltaTime(), GAME_DURATION)
            val player = Player(Vec2((ggState.maxX / 2f) - 25, (ggState.maxY / 2f) + 25), ggState = ggState, width = 64, height = 64) {
                uiComponent?.showUpgradePopup(generateUpgradeOptions())
            }

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
                dispose()
                val saveState = service<GameSaveState>()
                saveState.levelsBeaten++
                toolWindowBaseComponent.showScreen(saveState.levelsBeaten)
                scope.launch(Dispatchers.EDT) {
                    toolWindow.show()
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

                c.addComponentListener(object : ComponentListener {
                    override fun componentResized(e: ComponentEvent?) {
                        ggState.maxX = editor.scrollingModel.visibleArea.width
                        ggState.maxY = editor.scrollingModel.visibleArea.height
                        uiComponent?.updateBounds(editor.contentComponent.bounds)
                        gameComponent?.bounds = editor.contentComponent.bounds
                    }

                    override fun componentMoved(e: ComponentEvent?) {}
                    override fun componentShown(e: ComponentEvent?) {}
                    override fun componentHidden(e: ComponentEvent?) {}
                })

                c.repaint()
                c.revalidate()
            }

            uiComponent?.showGameUi()
        }
    }

    private fun generateUpgradeOptions(): List<UpgradeOption> {
        return upgradeRepository.getRandomUpgrades(attackManager)
    }

    override fun dispose() {
        editor.contentComponent.remove(uiComponent)
        editor.contentComponent.remove(gameComponent)
        editor.contentComponent.revalidate()
        editor.contentComponent.repaint()
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyListener)
        ggState.gameActive = false
    }
}