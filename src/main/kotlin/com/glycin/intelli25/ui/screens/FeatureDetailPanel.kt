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

        features.forEachIndexed { fIndex, feature ->
            //inner.add(buildFeatureInfo(feature))
            //inner.add(Box.createVerticalStrut(12)) //TODO: Do we want the header here?

            feature.upgrades.filter { it.availableAtLevel == level}.forEachIndexed { bIndex, boost ->
                inner.add(buildBoostCard(boost))
                if (bIndex < feature.upgrades.lastIndex) {
                    inner.add(Box.createVerticalStrut(25))
                }
            }

            /*if (fIndex < features.lastIndex) {
                inner.add(Box.createVerticalStrut(32))
                inner.add(JSeparator(JSeparator.HORIZONTAL).apply {
                    maximumSize = Dimension(Int.MAX_VALUE, 1)
                    foreground = GameColors.white
                })
                inner.add(Box.createVerticalStrut(32))
            }*/ //TODO: No separator needed if we dont group by headers
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

    private fun buildFeatureInfo(feature: AttackDef): JComponent {
        val panel = JPanel().apply {
            isOpaque = false
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = CENTER_ALIGNMENT
        }

        feature.image?.let { img ->
            val iconLabel = JLabel(ImageIcon(img.getScaledInstance(96, 96, Image.SCALE_SMOOTH))).apply {
                alignmentX = CENTER_ALIGNMENT
                border = BorderFactory.createEmptyBorder(0, 0, 8, 0)
            }
            panel.add(iconLabel)
        }

        panel.add(JLabel(feature.title).apply {
            alignmentX = CENTER_ALIGNMENT
            font = Fonts.pixelFont.deriveFont(Font.BOLD, 18f)
            foreground = GameColors.white
        })

        panel.add(JLabel(feature.description).apply {
            alignmentX = CENTER_ALIGNMENT
            font = Fonts.pixelFont.deriveFont(Font.PLAIN, 14f)
            foreground = GameColors.white
            border = BorderFactory.createEmptyBorder(4, 0, 4, 0)
        })

        panel.add(JLabel(feature.effect).apply {
            alignmentX = CENTER_ALIGNMENT
            font = Fonts.pixelFont.deriveFont(Font.PLAIN, 14f)
            foreground = GameColors.jbGreen
        })

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
            card.add(JLabel(ImageIcon(img.getScaledInstance(48, 48, Image.SCALE_SMOOTH))).apply {
                alignmentX = CENTER_ALIGNMENT
                border = BorderFactory.createEmptyBorder(0, 0, 4, 0)
            })
        }

        card.add(JLabel(boost.title).apply {
            alignmentX = CENTER_ALIGNMENT
            font = Fonts.pixelFont.deriveFont(Font.BOLD, 14f)
            foreground = GameColors.white
        })

        card.add(JLabel(boost.effect).apply {
            alignmentX = CENTER_ALIGNMENT
            font = Fonts.pixelFont.deriveFont(Font.PLAIN, 12f)
            foreground = GameColors.jbGreen
        })

        return card
    }
}