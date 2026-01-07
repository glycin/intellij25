package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.upgrades.AttackDef
import com.glycin.intelli25.upgrades.AttackUpgradeDef
import com.glycin.intelli25.util.GameColors
import com.intellij.ui.components.JBScrollPane
import java.awt.*
import javax.swing.*

class FeatureDetailPanel(
    private val features: List<AttackDef>,
    private val level: Int,
) : JPanel(BorderLayout()) {

    init {
        isOpaque = false

        val inner = JPanel().apply {
            isOpaque = false
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = BorderFactory.createEmptyBorder(16, 16, 16, 16)
        }

        features.forEach { feature ->
            feature.upgrades.filter { it.availableAtLevel == level}.forEachIndexed { bIndex, boost ->
                inner.add(buildBoostCard(boost))
                if (bIndex < feature.upgrades.lastIndex) {
                    inner.add(Box.createVerticalStrut(25))
                }
            }
        }

        val scrollPane = JBScrollPane(inner).apply {
            border = BorderFactory.createEmptyBorder()
            viewport.background = GameColors.black
            horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
            viewport.isOpaque = true
        }

        add(scrollPane, BorderLayout.CENTER)
    }

    private fun buildBoostCard(attackDef: AttackUpgradeDef): JComponent {
        return AtlasCardItem(
            title = attackDef.title,
            description = attackDef.effect,
            cardIcon = attackDef.image!!,
            borderColor = GameColors.jbPurple
        )
    }
}