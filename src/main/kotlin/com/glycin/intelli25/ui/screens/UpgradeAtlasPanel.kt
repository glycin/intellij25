package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.ui.StartButton
import com.glycin.intelli25.util.GameColors
import com.intellij.ui.components.JBScrollPane
import java.awt.*
import javax.swing.*

data class UpgradeMenuItem(
    val id: String,
    val title: String
)

class UpgradeAtlasPanel() : JPanel(BorderLayout()) {

    val featureItems = List(5) { i ->
        UpgradeMenuItem("feature_$i", "Feature ${i + 1}")
    }
    val upgradeItems = List(10) { i ->
        UpgradeMenuItem("upgrade_$i", "Upgrade ${i + 1}")
    }

    private val contentPanel = JPanel(BorderLayout()).apply {
        isOpaque = false
        // Placeholder; you’ll replace this later with real content.
        add(JLabel("Select an item on the left").apply {
            horizontalAlignment = SwingConstants.CENTER
        }, BorderLayout.CENTER)
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
        upgradeItems.forEachIndexed { i, item ->
            menuPanel.add(createMenuButton(item, i))
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
        val color = when(index % 3) {
            0 -> GameColors.jbBlueLight
            1 -> GameColors.jbOrangeLight
            else -> GameColors.jbRedLight
        }
        return StartButton(
            backgroundColor = color,
            hoverColor = GameColors.jbGreen,
            textColor = GameColors.white,
            text = item.title,
        ).apply {
            alignmentX = CENTER_ALIGNMENT
            isFocusPainted = false
            maximumSize = Dimension(Int.MAX_VALUE, 40)
            addActionListener {
                showItemContent(item)
            }
        }
    }

    private fun showItemContent(item: UpgradeMenuItem) {
        contentPanel.removeAll()
        // Placeholder content – you’ll replace this later.
        val label = JLabel("<html><h3>${item.title}</h3><p>Content coming soon...</p></html>").apply {
            border = BorderFactory.createEmptyBorder(16, 16, 16, 16)
        }
        contentPanel.add(label, BorderLayout.CENTER)
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
