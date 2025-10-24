package com.glycin.intelli25

import com.glycin.intelli25.input.GameKeyListener
import com.glycin.intelli25.managers.BulletManager
import com.glycin.intelli25.managers.CollisionsManager
import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.util.getDeltaTime
import com.glycin.intelli25.ui.UiComponent
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.EDT
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.KeyboardFocusManager

private const val FPS = 120L

class Game(
    private val project: Project,
    private val editor: Editor,
    private val scope: CoroutineScope,
): Disposable {

    private var gameComponent: GameComponent? = null
    private var uiComponent: UiComponent? = null
    private var ggState: GameGlobalState? = null
    private var keyListener: GameKeyListener? = null

    init {
        scope.launch(Dispatchers.EDT) {
            val maxX = editor.scrollingModel.visibleArea.width
            val maxY = editor.scrollingModel.visibleArea.height
            ggState = GameGlobalState(0, 0, maxX, maxY, FPS.getDeltaTime())
            val player = Player(Vec2((ggState!!.maxX / 2f) - 25, (ggState!!.maxY / 2f) + 25), width = 75, height = 75)

            keyListener = GameKeyListener(player).also {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(it)
            }

            val bulletManager = BulletManager(scope, player, ggState!!)
            val enemyManager = EnemyManager(player, ggState!!, scope)
            val collisionsManager = CollisionsManager(player, enemyManager, bulletManager, scope, ggState!!) //TODO: Create one update manager that handles all updating in the game
            gameComponent = GameComponent(ggState!!, player, bulletManager, enemyManager, scope).also { ec ->
                ec.bounds = editor.contentComponent.bounds
                ec.isOpaque = false
                ec.isFocusable = true
                ec.requestFocusInWindow()
            }

            uiComponent = UiComponent(player, ggState!!, scope).also { uic ->
                uic.bounds = editor.contentComponent.bounds
                uic.isOpaque = false
            }

            editor.contentComponent.let { c ->
                c.add(gameComponent)
                c.add(uiComponent)
                c.setComponentZOrder(uiComponent, 0)
                c.setComponentZOrder(gameComponent, 1)
                c.repaint()
                c.revalidate()
            }

            uiComponent?.showDialogBox(listOf("Test1", "Test2", "Test3", "Test4", "Test5"))
            uiComponent?.showGameUi()
        }
    }

    override fun dispose() {
        editor.contentComponent.remove(uiComponent)
        editor.contentComponent.remove(gameComponent)
        editor.contentComponent.revalidate()
        editor.contentComponent.repaint()
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyListener)
    }
}