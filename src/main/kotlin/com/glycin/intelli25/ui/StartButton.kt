package com.glycin.intelli25.ui

import com.glycin.intelli25.util.GameColors
import com.intellij.util.ui.JBUI
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JButton

class StartButton(
    private val backgroundColor: Color,
    private val hoverColor: Color,
    textColor: Color = GameColors.white,
    text: String,
    private val arc: Int = 40,
    var filled: Boolean = true,
) : JButton(text) {
    private var hover = false

    init {
        font = Fonts.pixelFont.deriveFont(16.0f)
        preferredSize = JBUI.size(320, 100)
        minimumSize = preferredSize
        maximumSize = preferredSize

        foreground = textColor
        isFocusPainted = false
        isBorderPainted = false
        isContentAreaFilled = false
        isOpaque = false
        horizontalAlignment = CENTER

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
        val g2 = g.create() as Graphics2D

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )

        g2.color = backgroundColor
        if(filled) {
            g2.fillRoundRect(0, 0, width - 1, height - 1, arc, arc)
        } else {
            g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc)
        }

        super.paintComponent(g2)

        if (hover && isEnabled) {
            g2.color = hoverColor
            g2.stroke = BasicStroke(4f)
            g2.drawRoundRect(2, 2, width - 5, height - 5, arc, arc)
        }

        g2.dispose()
    }
}