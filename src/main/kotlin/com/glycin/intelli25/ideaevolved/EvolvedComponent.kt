package com.glycin.intelli25.ideaevolved

import com.glycin.intelli25.shared.GameGeneralState
import com.glycin.intelli25.shared.Vec2
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.EDT
import com.intellij.ui.JBColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import javax.swing.JComponent

class EvolvedComponent(
    private val ggState: GameGeneralState,
    private val deltaTime: Long,
    private val scope: CoroutineScope,
): JComponent(), Disposable {
    @Volatile private var mouseX: Int = -1
    @Volatile private var mouseY: Int = -1

    private var active = true
    private val player = Player(Vec2((ggState.maxX / 2f) - 25, (ggState.maxY / 2f) + 25), 50, 50)
    init {
        scope.launch(Dispatchers.Default) {
            while (active) {
                repaint()
                delay(deltaTime)
            }
        }

        addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseMoved(e: MouseEvent) {
                mouseX = e.x
                mouseY = e.y
                repaint()
            }

            override fun mouseDragged(e: MouseEvent) {
                mouseMoved(e)
            }
        })
        enableEvents(0)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        println("drawing $this")
        if(g is Graphics2D) {
            player.draw(g)
            if (mouseX >= 0 && mouseY >= 0) {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
                g.color = JBColor.RED
                val d = 25 * 2
                g.fillOval(mouseX - 25, mouseY - 25, d, d)
            }
        }
    }

    override fun dispose() {
        //TODO("Not yet implemented")
    }
}