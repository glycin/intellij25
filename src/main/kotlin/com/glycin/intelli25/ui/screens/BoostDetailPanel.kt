package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.upgrades.UpgradeBoostDef
import java.awt.Component
import java.awt.Font
import javax.swing.*

class BoostDetailPanel(
    boost: UpgradeBoostDef,
) : JPanel() {

    init {
        isOpaque = false
        layout = BoxLayout(this, BoxLayout.Y_AXIS)

        val imageLabel = JLabel(ImageIcon(boost.image)).apply {
            alignmentX = Component.CENTER_ALIGNMENT
        }

        val titleLabel = JLabel(boost.title).apply {
            alignmentX = Component.CENTER_ALIGNMENT
            font = Fonts.pixelFont.deriveFont(Font.BOLD, 18f)
            border = BorderFactory.createEmptyBorder(8, 0, 4, 0)
        }

        val effectLabel = JLabel(boost.effect).apply {
            alignmentX = Component.CENTER_ALIGNMENT
            font = Fonts.pixelFont.deriveFont(Font.PLAIN, 14f)
        }

        add(Box.createVerticalGlue())
        add(imageLabel)
        add(titleLabel)
        add(effectLabel)
        add(Box.createVerticalGlue())
    }
}
