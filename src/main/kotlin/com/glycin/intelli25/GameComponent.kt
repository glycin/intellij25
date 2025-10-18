package com.glycin.intelli25

import com.glycin.intelli25.managers.BulletManager
import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.util.GameGeneralState
import com.intellij.openapi.Disposable
import com.intellij.ui.JBColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionAdapter
import javax.swing.JComponent

class GameComponent(
    private val ggState: GameGeneralState,
    private val player: Player,
    private val bulletManager: BulletManager,
    private val enemyManager: EnemyManager,
    scope: CoroutineScope,
): JComponent(), Disposable {
    @Volatile private var mouseX: Int = -1
    @Volatile private var mouseY: Int = -1

    private var active = true
    init {
        scope.launch(Dispatchers.Default) {
            while (active) {
                player.update()
                repaint()
                delay(ggState.deltaTime)
            }
        }

        // TODO: Move mouse and keybaord stuff to input listener
        addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseMoved(e: MouseEvent) {
                mouseX = e.x
                mouseY = e.y
            }

            override fun mouseDragged(e: MouseEvent) {
                mouseMoved(e)
            }
        })


        addMouseListener(object: MouseListener {
            override fun mouseClicked(e: MouseEvent?) {
                bulletManager.addBullet(player.midPoint(), Vec2(mouseX.toFloat(), mouseY.toFloat()))
            }
            override fun mousePressed(e: MouseEvent?) {}
            override fun mouseReleased(e: MouseEvent?) {}
            override fun mouseEntered(e: MouseEvent?) {}
            override fun mouseExited(e: MouseEvent?) {}
        })

        enableEvents(0)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if(g is Graphics2D) {
            player.draw(g)
            bulletManager.drawBullets(g)
            enemyManager.drawEnemies(g)
            if (mouseX >= 0 && mouseY >= 0) {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
                g.color = JBColor.RED
                val d = 10
                g.fillOval(mouseX - 5, mouseY - 5, d, d)
            }
        }
    }

    override fun dispose() {
        //TODO("Not yet implemented")
    }
}