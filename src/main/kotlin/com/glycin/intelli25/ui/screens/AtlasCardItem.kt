package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.util.GameColors
import com.intellij.util.ui.JBUI
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Image
import java.awt.RenderingHints
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class AtlasCardItem(
    private val title: String,
    private val description: String,
    private val cardIcon: Image,
    private val borderColor: Color,
): JPanel(GridBagLayout()) {

    init {
        isOpaque = false
        border = BorderFactory.createEmptyBorder(15, 20, 15, 20)
        maximumSize = Dimension(Int.MAX_VALUE, 120)
        preferredSize = Dimension(650, 100)

        val c = GridBagConstraints()

        val iconLabel = JLabel().apply {
            icon = ImageIcon(cardIcon.getScaledInstance(64, 64, Image.SCALE_SMOOTH))
            preferredSize = Dimension(64, 64)
            horizontalAlignment = SwingConstants.CENTER
            verticalAlignment = SwingConstants.CENTER
        }

        c.gridx = 0
        c.gridy = 0
        c.weightx = 0.0
        c.anchor = GridBagConstraints.CENTER
        c.insets = JBUI.insetsRight(20)
        add(iconLabel, c)

        val nameLabel = JLabel(title).apply {
            font = Fonts.jbMono.deriveFont(Font.BOLD, 16f)
            foreground = GameColors.white
        }

        val descLabel = JLabel("<html><body style='width: 420px;'>$description</body></html>").apply {
            font = Fonts.jbMono.deriveFont(Font.BOLD, 14f)
        }

        val textPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            isOpaque = false
            alignmentX = LEFT_ALIGNMENT
            add(nameLabel)
            add(Box.createVerticalStrut(6))
            add(descLabel)
        }

        c.gridx = 1
        c.gridy = 0
        c.weightx = 1.0
        c.fill = GridBagConstraints.HORIZONTAL
        c.anchor = GridBagConstraints.CENTER
        c.insets = JBUI.emptyInsets()
        add(textPanel, c)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        g2.color = borderColor
        g2.stroke = BasicStroke(2f)
        g2.drawRoundRect(1, 1, width - 3, height - 3, 20, 20)
    }
}