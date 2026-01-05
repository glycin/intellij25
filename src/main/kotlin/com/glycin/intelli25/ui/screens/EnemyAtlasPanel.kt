package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.model.EnemyEntry
import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.util.EnemyPNG
import com.glycin.intelli25.util.GameColors
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import javax.swing.*

class EnemyAtlasPanel(
    enemies: List<EnemyEntry>
) : JPanel(BorderLayout()) {

    init {
        isOpaque = false

        val listPanel = JPanel().apply {
            isOpaque = false
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
        }

        enemies.forEach { enemy ->
            listPanel.add(createEnemyRow(enemy))
        }

        val scrollPane = JBScrollPane(listPanel).apply {
            border = BorderFactory.createEmptyBorder()
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
            horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
            isOpaque = false
            viewport.isOpaque = false
        }

        val wrapper = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            isOpaque = false
            add(Box.createVerticalGlue())
            add(scrollPane.apply {
                preferredSize = Dimension(700, 600)
                alignmentX = CENTER_ALIGNMENT
            })
            add(Box.createVerticalGlue())
        }

        add(wrapper, BorderLayout.CENTER)
    }

    private fun createEnemyRow(enemy: EnemyEntry): JComponent {
        val panel = JPanel(BorderLayout()).apply {
            isOpaque = false
            border = BorderFactory.createEmptyBorder(8, 16, 8, 16)
            maximumSize = Dimension(Int.MAX_VALUE, 120)
        }

        val iconLabel = JLabel().apply {
            icon = if(enemy.seen) ImageIcon(enemy.image?.getScaledInstance(64, 64, Image.SCALE_SMOOTH)) else
                ImageIcon(EnemyPNG.unknown)
            preferredSize = Dimension(64, 64)
            horizontalAlignment = SwingConstants.CENTER
            verticalAlignment = SwingConstants.CENTER
        }

        val iconWrapper = JPanel(BorderLayout()).apply {
            isOpaque = false
            border = BorderFactory.createEmptyBorder(0, 0, 0, 24) // <- increase for more space
            add(iconLabel, BorderLayout.CENTER)
        }

        val nameText = if (enemy.seen) enemy.name else "???"
        val descriptionText = if (enemy.seen) enemy.description else "You have not encountered this enemy yet!"

        val nameLabel = JLabel(nameText).apply {
            font = Fonts.pixelFont.deriveFont(Font.BOLD, 16f)
        }

        val descLabel = JLabel("<html><body style='width: 420px;'>$descriptionText</body></html>").apply {
            font = Fonts.pixelFont.deriveFont(14f)
        }

        val textPanel = JPanel()
        textPanel.layout = BoxLayout(textPanel, BoxLayout.Y_AXIS)
        textPanel.isOpaque = false
        textPanel.add(nameLabel)
        textPanel.add(Box.createVerticalStrut(4))
        textPanel.add(descLabel)

        panel.add(iconWrapper , BorderLayout.WEST)
        panel.add(textPanel, BorderLayout.CENTER)

        return panel
    }

    override fun paintComponent(g: Graphics?) {
        if(g is Graphics2D) {
            g.color = GameColors.black
            g.fillRect(0, 0, width, height)
        }
        super.paintComponent(g)
    }
}