package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.upgrades.AttackDef
import com.glycin.intelli25.upgrades.AttackUpgradeDef
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.PNG
import com.intellij.ui.components.JBScrollPane
import java.awt.*
import javax.swing.*

class FeatureDetailPanel(
    private val feature: AttackDef,
) : JPanel(BorderLayout()) {

    private val scaledArrow = PNG.ARROW?.getScaledInstance(32, 32, Image.SCALE_SMOOTH)

    init {
        isOpaque = false

        val inner = JPanel().apply {
            isOpaque = false
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = BorderFactory.createEmptyBorder(16, 16, 16, 16)
        }

        inner.add(buildHeaderPanel())
        inner.add(Box.createVerticalStrut(20))
        inner.add(buildArrowPanel())
        inner.add(Box.createVerticalStrut(12))
        feature.boosts.forEachIndexed { index, boost ->
            inner.add(buildBoostCard(boost))
            if (index < feature.boosts.lastIndex) {
                inner.add(Box.createVerticalStrut(12))
                inner.add(buildArrowPanel())
                inner.add(Box.createVerticalStrut(12))
            }
        }

        val scrollPane = JBScrollPane(inner).apply {
            border = BorderFactory.createEmptyBorder()
            viewport.background = GameColors.black
            horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        }

        add(scrollPane, BorderLayout.CENTER)
    }

    private fun buildHeaderPanel(): JComponent {
        val panel = JPanel().apply {
            isOpaque = false
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = CENTER_ALIGNMENT
        }

        feature.image?.let { img ->
            val iconLabel = JLabel(ImageIcon(img.getScaledInstance(128, 128, Image.SCALE_SMOOTH))).apply {
                alignmentX = CENTER_ALIGNMENT
                border = BorderFactory.createEmptyBorder(0, 0, 12, 0)
            }
            panel.add(iconLabel)
        }

        val titleLabel = JLabel(feature.title).apply {
            alignmentX = CENTER_ALIGNMENT
            font = Fonts.pixelFont.deriveFont(Font.BOLD, 18f)
            foreground = GameColors.white
            border = BorderFactory.createEmptyBorder(0, 0, 6, 0)
        }

        val descriptionLabel = JLabel(feature.description).apply {
            alignmentX = CENTER_ALIGNMENT
            font = Fonts.pixelFont.deriveFont(Font.PLAIN, 14f)
            foreground = GameColors.white
            border = BorderFactory.createEmptyBorder(0, 0, 4, 0)
        }

        val effectLabel = JLabel(feature.effect).apply {
            alignmentX = CENTER_ALIGNMENT
            font = Fonts.pixelFont.deriveFont(Font.PLAIN, 14f)
            foreground = GameColors.jbGreen
        }

        panel.add(titleLabel)
        panel.add(descriptionLabel)
        panel.add(effectLabel)

        return panel
    }

    private fun buildBoostCard(boost: AttackUpgradeDef): JComponent {
        val card = JPanel().apply {
            isOpaque = true
            background = GameColors.black
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = CENTER_ALIGNMENT
        }

        boost.image?.let { img ->
            val iconLabel = JLabel(ImageIcon(img.getScaledInstance(64, 64, Image.SCALE_SMOOTH))).apply {
                alignmentX = CENTER_ALIGNMENT
                border = BorderFactory.createEmptyBorder(0, 0, 4, 0)
            }
            card.add(iconLabel)
        }

        val titleLabel = JLabel(boost.title).apply {
            alignmentX = CENTER_ALIGNMENT
            font = Fonts.pixelFont.deriveFont(Font.BOLD, 14f)
            foreground = GameColors.white
            border = BorderFactory.createEmptyBorder(0, 0, 2, 0)
        }

        val descriptionLabel = JLabel(boost.description).apply {
            alignmentX = CENTER_ALIGNMENT
            font = Fonts.pixelFont.deriveFont(Font.PLAIN, 12f)
            foreground = GameColors.white
            border = BorderFactory.createEmptyBorder(0, 0, 2, 0)
        }

        val effectLabel = JLabel(boost.effect).apply {
            alignmentX = CENTER_ALIGNMENT
            font = Fonts.pixelFont.deriveFont(Font.PLAIN, 12f)
            foreground = GameColors.jbGreen
        }

        card.add(titleLabel)
        card.add(descriptionLabel)
        card.add(effectLabel)

        return card
    }

    private fun buildArrowPanel(): JComponent {
        return JPanel().apply {
            isOpaque = false
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = CENTER_ALIGNMENT
            preferredSize = Dimension(0, 40)

            val label = JLabel(ImageIcon(scaledArrow)).apply {
                alignmentX = CENTER_ALIGNMENT
            }

            add(Box.createVerticalGlue())
            add(label)
            add(Box.createVerticalGlue())
        }
    }
}