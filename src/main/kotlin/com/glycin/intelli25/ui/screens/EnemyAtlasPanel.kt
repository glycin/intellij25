package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.model.EnemyEntry
import com.glycin.intelli25.util.EnemyPNG
import com.glycin.intelli25.util.GameColors
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
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
            listPanel.add(Box.createVerticalStrut(15))
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
        return AtlasCardItem(
            title = enemy.name,
            description = enemy.description,
            cardIcon = if (enemy.seen) enemy.image!! else EnemyPNG.unknown!!,
            borderColor = GameColors.jbPurple
        )
    }

    override fun paintComponent(g: Graphics?) {
        if(g is Graphics2D) {
            g.color = GameColors.black
            g.fillRect(0, 0, width, height)
        }
        super.paintComponent(g)
    }
}