package com.glycin.intelli25

import com.glycin.intelli25.managers.BulletManager
import com.glycin.intelli25.managers.CollisionsManager
import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.managers.EvolvedComponent
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.shared.GameGeneralState
import com.glycin.intelli25.shared.Vec2
import com.glycin.intelli25.shared.getDeltaTime
import com.glycin.intelli25.ui.UiComponent
import com.intellij.openapi.application.EDT
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val FPS = 120L

class Game(
    private val project: Project,
    private val editor: Editor,
    private val scope: CoroutineScope,
) {

    private var evolvedComponent: EvolvedComponent? = null
    private var uiComponent: UiComponent? = null
    private var ggState: GameGeneralState? = null

    init {
        scope.launch(Dispatchers.EDT) {
            val maxX = editor.scrollingModel.visibleArea.width
            val maxY = editor.scrollingModel.visibleArea.height
            println("maxX: $maxX,  maxY: $maxY")
            ggState = GameGeneralState(0, 0, maxX, maxY, FPS.getDeltaTime())
            val bulletManager = BulletManager(scope, ggState!!)
            val player = Player(Vec2((ggState!!.maxX / 2f) - 25, (ggState!!.maxY / 2f) + 25), 50, 50)
            val enemyManager = EnemyManager(player, ggState!!, scope)
            val collisionsManager = CollisionsManager(player, enemyManager, bulletManager, scope, ggState!!) //TODO: Create one update manager that handles all updating in the game
            evolvedComponent = EvolvedComponent(ggState!!, player, bulletManager, enemyManager, scope).also { ec ->
                ec.bounds = editor.contentComponent.bounds
                ec.isOpaque = false
            }

            uiComponent = UiComponent(ggState!!, scope).also { uic ->
                uic.bounds = editor.contentComponent.bounds
                uic.isOpaque = false
            }

            editor.contentComponent.let { c ->
                c.add(evolvedComponent)
                c.add(uiComponent)
                c.setComponentZOrder(uiComponent, 0)
                c.setComponentZOrder(evolvedComponent, 1)
                c.repaint()
                c.revalidate()
            }

            uiComponent?.showDialogBox(listOf("Test1", "Test2", "Test3", "Test4", "Test5"))
        }
    }
}