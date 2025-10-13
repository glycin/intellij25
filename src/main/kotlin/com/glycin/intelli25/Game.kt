package com.glycin.intelli25

import com.glycin.intelli25.ideaevolved.BulletManager
import com.glycin.intelli25.ideaevolved.EvolvedComponent
import com.glycin.intelli25.ideaevolved.Player
import com.glycin.intelli25.shared.GameGeneralState
import com.glycin.intelli25.shared.Vec2
import com.glycin.intelli25.shared.getDeltaTime
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
    private var ggState: GameGeneralState? = null

    init {
        scope.launch(Dispatchers.EDT) {
            val maxX = editor.scrollingModel.visibleArea.width
            val maxY = editor.scrollingModel.visibleArea.height
            println("maxX: $maxX,  maxY: $maxY")
            ggState = GameGeneralState(0, 0, maxX, maxY, FPS.getDeltaTime())
            val bulletManager = BulletManager(scope, ggState!!)
            val player = Player(Vec2((ggState!!.maxX / 2f) - 25, (ggState!!.maxY / 2f) + 25), 50, 50)
            evolvedComponent = EvolvedComponent(ggState!!, player, bulletManager, scope).also { ec ->
                ec.bounds = editor.contentComponent.bounds
                ec.isOpaque = false
            }
            editor.contentComponent.let { c ->
                println("added")
                c.add(evolvedComponent)
                c.repaint()
                c.revalidate()
            }
        }
    }
}