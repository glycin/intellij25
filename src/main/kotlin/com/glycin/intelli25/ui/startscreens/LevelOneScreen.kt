package com.glycin.intelli25.ui.startscreens

import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.ui.StartButton
import com.glycin.intelli25.util.GameColors
import java.awt.BorderLayout
import java.awt.GradientPaint
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GridBagLayout
import java.awt.RenderingHints
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class LevelOneScreen(): JPanel() {

    val startButton: JButton

    init {
        isOpaque = false
        layout = BorderLayout()

        val titleLabel = JLabel("IDE Survivors").apply {
            font = Fonts.pixelFont.deriveFont(24.0f)
            foreground = GameColors.white
            horizontalAlignment = SwingConstants.CENTER
            border = BorderFactory.createEmptyBorder(20, 0, 20, 0)
        }
        add(titleLabel, BorderLayout.NORTH)

        val buttonPanel = JPanel(GridBagLayout()).apply {
            isOpaque = false
        }
        startButton = StartButton("Start Game")
        buttonPanel.add(startButton)
        add(buttonPanel, BorderLayout.CENTER)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if(g is Graphics2D) {
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
            val gradient = GradientPaint(
                width.toFloat(), 0f, GameColors.jbRed,
                0f, height.toFloat(), GameColors.jbBlue
            )
            g.paint = gradient
            g.fillRect(0, 0, width, height)
        }
    }
}