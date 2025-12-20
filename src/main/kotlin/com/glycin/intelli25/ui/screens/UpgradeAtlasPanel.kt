package com.glycin.intelli25.ui.screens
import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.ui.StartButton
import com.glycin.intelli25.upgrades.AttackConfig
import com.glycin.intelli25.upgrades.AttackDef
import com.glycin.intelli25.upgrades.UpgradeBoostDef
import com.glycin.intelli25.util.GameColors
import com.intellij.ui.components.JBScrollPane
import java.awt.*
import javax.swing.*

sealed class UpgradeMenuItem {
    data class FeatureItem(val feature: AttackDef) : UpgradeMenuItem()
    data class BoostItem(val boost: UpgradeBoostDef) : UpgradeMenuItem()
}

class UpgradeAtlasPanel : JPanel(BorderLayout()) {

    private val featureItems: List<UpgradeMenuItem.FeatureItem> =
        AttackConfig.ALL_FEATURES.map { UpgradeMenuItem.FeatureItem(it) }

    private val boostItems: List<UpgradeMenuItem.BoostItem> =
        UpgradeBoostDef.entries.map { UpgradeMenuItem.BoostItem(it) }

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

        menuPanel.add(groupHeader("Features"))
        featureItems.forEachIndexed { i, item ->
            menuPanel.add(createMenuButton(item, i))
        }
        menuPanel.add(Box.createVerticalStrut(12))

        menuPanel.add(groupHeader("Boosts"))
        boostItems.forEachIndexed { i, item ->
            menuPanel.add(createMenuButton(item, featureItems.size + i))
        }

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

    private fun groupHeader(text: String): JComponent =
        JLabel(text).apply {
            font = Fonts.pixelFont.deriveFont(Font.BOLD, 14f)
            alignmentX = CENTER_ALIGNMENT
        }

    private fun createMenuButton(item: UpgradeMenuItem, index: Int): JComponent {
        val color = when (index % 3) {
            0 -> GameColors.jbBlueLight
            1 -> GameColors.jbOrangeLight
            else -> GameColors.jbRedLight
        }

        val text = when (item) {
            is UpgradeMenuItem.FeatureItem -> item.feature.title
            is UpgradeMenuItem.BoostItem -> item.boost.title
        }

        return StartButton(
            backgroundColor = color,
            hoverColor = GameColors.jbGreen,
            textColor = GameColors.white,
            text = text,
        ).apply {
            alignmentX = CENTER_ALIGNMENT
            isFocusPainted = false
            maximumSize = Dimension(Int.MAX_VALUE, 40)
            addActionListener { showItemContent(item) }
        }
    }

    private fun showItemContent(item: UpgradeMenuItem) {
        contentPanel.removeAll()

        when (item) {
            is UpgradeMenuItem.FeatureItem -> {
                contentPanel.add(FeatureDetailPanel(item.feature), BorderLayout.CENTER)
            }

            is UpgradeMenuItem.BoostItem -> {
                contentPanel.add(BoostDetailPanel(item.boost), BorderLayout.CENTER)
            }
        }

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