package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.upgrades.UpgradeBoostDef
import com.glycin.intelli25.util.GameColors
import com.intellij.ui.components.JBScrollPane
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

    private fun createBoostItem(boost: UpgradeBoostDef): JComponent {
        return AtlasCardItem(
            title = boost.title,
            description = boost.effect,
            cardIcon = boost.image!!,
            borderColor = GameColors.jbPurple
        )
    }
}