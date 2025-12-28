package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.upgrades.UpgradeBoostDef
import com.intellij.ui.components.JBScrollPane
import java.awt.Font
import java.awt.Image
import javax.swing.*

class BoostDetailPanel(
    boosts: List<UpgradeBoostDef>
) : JPanel() {
    init {
        isOpaque = false
        layout = BoxLayout(this, BoxLayout.Y_AXIS)

        add(Box.createVerticalGlue())

        val boostsContainer = JPanel().apply {
            isOpaque = false
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
        }

        boosts.forEach { boost ->
            boostsContainer.add(createBoostItem(boost))
            boostsContainer.add(Box.createVerticalStrut(25))
        }

        val scrollPane = JBScrollPane(boostsContainer).apply {
            isOpaque = false
            border = BorderFactory.createEmptyBorder()
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
            horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
            viewport.isOpaque = false
        }

        add(scrollPane)
        add(Box.createVerticalGlue())
    }

    private fun createBoostItem(boost: UpgradeBoostDef): JPanel {
        return JPanel().apply {
            isOpaque = false
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = CENTER_ALIGNMENT

            val imageLabel = JLabel(ImageIcon(boost.image?.getScaledInstance(32, 32, Image.SCALE_SMOOTH))).apply {
                alignmentX = CENTER_ALIGNMENT
            }

            val titleLabel = JLabel(boost.title).apply {
                alignmentX = CENTER_ALIGNMENT
                font = Fonts.pixelFont.deriveFont(Font.BOLD, 18f)
                border = BorderFactory.createEmptyBorder(8, 0, 4, 0)
            }

            val effectLabel = JLabel(boost.effect).apply {
                alignmentX = CENTER_ALIGNMENT
                font = Fonts.pixelFont.deriveFont(Font.PLAIN, 14f)
            }

            add(imageLabel)
            add(titleLabel)
            add(effectLabel)
        }
    }
}