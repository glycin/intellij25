package com.glycin.intelli25

import com.glycin.intelli25.input.GameMouseMotionListener
import com.glycin.intelli25.managers.BulletManager
import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.util.GameGlobalState
import com.intellij.openapi.Disposable
import com.intellij.ui.JBColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.JComponent

class GameComponent(
    private val ggState: GameGlobalState,
    private val player: Player,
    private val bulletManager: BulletManager,
    private val enemyManager: EnemyManager,
    scope: CoroutineScope,
): JComponent(), Disposable {
    private var active = true
    private val gameMouseMotionListener = GameMouseMotionListener(ggState)

    init {
        scope.launch(Dispatchers.Default) {
            while (active) {
                player.update()
                repaint()
                delay(ggState.deltaTime)
            }
        }

        addMouseMotionListener(gameMouseMotionListener)
        enableEvents(0)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if(g is Graphics2D) {
            player.draw(g)
            bulletManager.drawBullets(g)
            enemyManager.drawEnemies(g)
            enemyManager.drawPickups(g)

            //TODO: Mouse debug stuff
            if (ggState.mouseX >= 0 && ggState.mouseY >= 0) {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
                g.color = JBColor.RED
                val d = 10
                g.fillOval(ggState.mouseX - 5, ggState.mouseY - 5, d, d)
            }
        }
    }

    override fun dispose() {
        removeMouseMotionListener(gameMouseMotionListener)
        active = false
    }
}