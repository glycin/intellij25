package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.ui.StartButton
import com.glycin.intelli25.upgrades.AttackConfig
import com.glycin.intelli25.upgrades.AttackDef
import com.glycin.intelli25.upgrades.UpgradeBoostDef
import com.glycin.intelli25.util.GameColors
import com.intellij.util.ui.JBUI
import java.awt.*
import javax.swing.*

class UpgradeAtlasPanel : JPanel(BorderLayout()) {

    private val childhoodItems : List<AttackDef> =
        AttackConfig.ALL_FEATURES.filter { it.availableAtLevel == 1 }

    private val teenageItems : List<AttackDef> =
        AttackConfig.ALL_FEATURES.filter { it.availableAtLevel <= 2 }

    private val adultItems: List<AttackDef> = AttackConfig.ALL_FEATURES

    private val boostItems: List<UpgradeBoostDef> = UpgradeBoostDef.entries

    private val contentPanel = JPanel(BorderLayout()).apply {
        isOpaque = false
        add(
            JLabel("Select a tab above").apply {
                font = Fonts.pixelFont.deriveFont(Font.BOLD, 20f)
                horizontalAlignment = SwingConstants.CENTER
                foreground = GameColors.white
            },
            BorderLayout.CENTER
        )
    }

    init {
        isOpaque = false
        border = BorderFactory.createEmptyBorder()

        val menuPanel = JPanel().apply {
            isOpaque = false
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            border = BorderFactory.createEmptyBorder(0, 0, 10, 0)
        }

        menuPanel.add(Box.createHorizontalGlue())

        menuPanel.add(createAttackDefMenuItem("2001 - 2009", childhoodItems, 1))
        menuPanel.add(Box.createHorizontalStrut(5))
        menuPanel.add(createAttackDefMenuItem("2010 - 2017", teenageItems, 2))
        menuPanel.add(Box.createHorizontalStrut(5))
        menuPanel.add(createAttackDefMenuItem("2018 - 2026", adultItems, 3))
        menuPanel.add(Box.createHorizontalStrut(5))
        menuPanel.add(createBoostMenuItem(boostItems))

        menuPanel.add(Box.createHorizontalGlue())

        add(menuPanel, BorderLayout.NORTH)
        add(contentPanel, BorderLayout.CENTER)
    }

    private fun createAttackDefMenuItem(title: String, items: List<AttackDef>, level: Int): JComponent {
        return StartButton(
            backgroundColor = GameColors.jbPurple,
            hoverColor = GameColors.jbOrange,
            textColor = GameColors.white,
            text = title,
            preferredWidth = 160,
            preferredHeight = 35,
        ).apply {
            margin = JBUI.insets(0, 10)
            isFocusPainted = false
            addActionListener { showAttackDefContent(items, level) }
        }
    }

    private fun createBoostMenuItem(items: List<UpgradeBoostDef>): JComponent {
        return StartButton(
            backgroundColor = GameColors.jbPurple,
            hoverColor = GameColors.jbOrange,
            textColor = GameColors.white,
            text = "Boosts",
            preferredWidth = 160,
            preferredHeight = 35,
        ).apply {
            margin = JBUI.insets(0, 10)
            isFocusPainted = false
            addActionListener { showBoostItemContent(items) }
        }
    }

    private fun showAttackDefContent(items: List<AttackDef>, level: Int) {
        contentPanel.removeAll()
        contentPanel.add(FeatureDetailPanel(items, level), BorderLayout.CENTER)
        contentPanel.revalidate()
        contentPanel.repaint()
    }

    private fun showBoostItemContent(items: List<UpgradeBoostDef>) {
        contentPanel.removeAll()
        contentPanel.add(BoostDetailPanel(items), BorderLayout.CENTER)
        contentPanel.revalidate()
        contentPanel.repaint()
    }

    override fun paintComponent(g: Graphics) {
        if (g is Graphics2D) {
            g.color = GameColors.black
            g.fillRect(0, 0, width, height)
        }
        super.paintComponent(g)
    }
}