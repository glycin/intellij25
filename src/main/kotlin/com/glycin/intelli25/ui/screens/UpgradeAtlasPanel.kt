package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.ui.StartButton
import com.glycin.intelli25.upgrades.AttackConfig
import com.glycin.intelli25.upgrades.AttackDef
import com.glycin.intelli25.upgrades.UpgradeBoostDef
import com.glycin.intelli25.util.GameColors
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBScrollPane
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
            JLabel("Select an item on the left").apply {
                font = Fonts.pixelFont.deriveFont(Font.BOLD, 20f)
                horizontalAlignment = SwingConstants.CENTER
            },
            BorderLayout.CENTER
        )
    }

    init {
        isOpaque = false
        border = BorderFactory.createEmptyBorder()

        val menuPanel = JPanel().apply {
            background = GameColors.black
            isOpaque = false
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
        }

        menuPanel.add(createAttackDefMenuItem("2001 - 2009", GameColors.jbRedLight, childhoodItems, 1))
        menuPanel.add(createAttackDefMenuItem("2010 - 2017", GameColors.jbBlueLight, teenageItems, 2))
        menuPanel.add(createAttackDefMenuItem("2018 - 2026", GameColors.jbOrangeLight, adultItems, 3))
        menuPanel.add(createBoostMenuItem(boostItems))

        val menuScroll = JBScrollPane(menuPanel).apply {
            preferredSize = Dimension(200, 400)
            minimumSize = Dimension(180, 200)
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
            horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
            border = BorderFactory.createEmptyBorder()
            viewportBorder = null
        }

        val split = JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuScroll, contentPanel).apply {
            background = GameColors.black
            resizeWeight = 0.0
            dividerLocation = 220
            isOneTouchExpandable = false
            border = BorderFactory.createEmptyBorder()
        }

        add(split, BorderLayout.CENTER)
    }

    private fun createAttackDefMenuItem(title: String, color: JBColor, items: List<AttackDef>, level: Int): JComponent {
        return StartButton(
            backgroundColor = color,
            hoverColor = GameColors.jbGreen,
            textColor = GameColors.white,
            text = title,
        ).apply {
            alignmentX = CENTER_ALIGNMENT
            isFocusPainted = false
            maximumSize = Dimension(Int.MAX_VALUE, 40)
            addActionListener { showAttackDefContent(items, level) }
        }
    }

    private fun createBoostMenuItem(items: List<UpgradeBoostDef>): JComponent {
        return StartButton(
            backgroundColor = GameColors.jbRedLight,
            hoverColor = GameColors.jbGreen,
            textColor = GameColors.white,
            text = "Universal Boosts",
        ).apply {
            alignmentX = CENTER_ALIGNMENT
            isFocusPainted = false
            maximumSize = Dimension(Int.MAX_VALUE, 40)
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