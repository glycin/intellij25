package com.glycin.intelli25.ui

import com.glycin.intelli25.util.GameColors
import com.intellij.util.ui.JBUI
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JButton

class StartButton(text: String): JButton(text) {
    private var hover = false

    init {
        font = Fonts.pixelFont.deriveFont(16.0f)
        preferredSize = JBUI.size(320, 100)
        foreground = GameColors.white
        isFocusPainted = false
        isBorderPainted = false
        isContentAreaFilled = false

        addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(e: MouseEvent?) {
                hover = true
                repaint()
            }
            override fun mouseExited(e: MouseEvent?) {
                hover = false
                repaint()
            }
        })
    }

    override fun paintComponent(g: Graphics) {
        if(g is Graphics2D) {
            g.color = GameColors.black
            g.fillRect(0, 0, width, height)
            super.paintComponent(g)

            if (hover) {
                g.color = GameColors.jbOrange
                g.stroke = java.awt.BasicStroke(4f)
                g.drawRect(2, 2, width - 4, height - 4)
            }
        }
    }
}