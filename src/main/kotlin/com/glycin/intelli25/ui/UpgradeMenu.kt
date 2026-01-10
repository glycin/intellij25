package com.glycin.intelli25.ui

import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.util.GameColors
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.util.ui.JBUI
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

class UpgradeMenu(
    private val upgrades: List<UpgradeOption>,
) : JPanel() {

    init {
        layout = BorderLayout()
        background = GameColors.black
        border = JBUI.Borders.empty(20)

        add(createTitleLabel(), BorderLayout.NORTH)
        add(createOptionsPanel(), BorderLayout.CENTER)
    }

    private fun createTitleLabel(): JLabel {
        return JLabel("CHOOSE YOUR UPGRADE!").apply {
            font = Fonts.pixelFont.deriveFont(1, 32.0f)
            foreground = GameColors.white
            horizontalAlignment = SwingConstants.CENTER
            border = JBUI.Borders.emptyBottom(20)
        }
    }

    private fun createOptionsPanel(): JPanel {
        return JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            isOpaque = false

            upgrades.forEach { up ->
                add(createUpgradeCard(up))
                add(Box.createVerticalStrut(15))
            }
        }
    }

    private fun createUpgradeCard(option: UpgradeOption): JPanel {
        return UpgradeCard(option)
    }

    inner class UpgradeCard(private val option: UpgradeOption) : JPanel(GridBagLayout()) {
        private var popup: JBPopup? = null
        private var isHovered = false

        init {
            isOpaque = false

            preferredSize = Dimension(800, 120)
            maximumSize = Dimension(Int.MAX_VALUE, 120)
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)

            border = JBUI.Borders.empty(15)

            setupComponents()
            setupMouseListeners()
        }

        private fun setupComponents() {
            val c = GridBagConstraints()

            val iconSize = 64
            val backgroundSize = 100
            val scaledIcon = option.icon.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH)
            val iconBackground = object : JPanel(GridBagLayout()) {
                override fun paintComponent(g: Graphics) {
                    val g2 = g as Graphics2D
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

                    g2.color = GameColors.black
                    g2.fillRoundRect(0, 0, width, height, 20, 20)
                }
            }.apply {
                isOpaque = false
                preferredSize = Dimension(backgroundSize, backgroundSize)
                minimumSize = Dimension(backgroundSize, backgroundSize)
                maximumSize = Dimension(backgroundSize, backgroundSize)

                add(JLabel(ImageIcon(scaledIcon)))
            }

            c.gridx = 0
            c.gridy = 0
            c.weightx = 0.0
            c.anchor = GridBagConstraints.CENTER
            c.insets = JBUI.insetsRight(20)
            add(iconBackground, c)

            val textPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                isOpaque = false

                add(JLabel(option.title.uppercase()).apply {
                    font = Fonts.jbMono.deriveFont(Font.BOLD, 24.0f)
                    foreground = option.textColor
                    alignmentX = LEFT_ALIGNMENT
                })

                add(Box.createVerticalStrut(4))

                add(JLabel("<html>${option.effect}</html>").apply {
                    font = Fonts.jbMono.deriveFont(Font.BOLD, 16.0f)
                    foreground = option.textColor
                    alignmentX = LEFT_ALIGNMENT
                })
            }

            c.gridx = 1
            c.gridy = 0
            c.weightx = 1.0
            c.fill = GridBagConstraints.HORIZONTAL
            c.anchor = GridBagConstraints.CENTER
            c.insets = JBUI.emptyInsets()
            add(textPanel, c)
        }

        override fun paintComponent(g: Graphics) {
            val g2 = g as Graphics2D
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

            g2.color = if(isHovered) GameColors.jbOrange else option.color
            if(isHovered) {
                g2.fillRoundRect(0, 0, width, height, 40, 40)
            } else {
                g2.drawRoundRect(0, 0, width, height, 40, 40)
            }

            g2.stroke = BasicStroke(10f)
            g2.drawRoundRect(1, 1, width - 3, height - 3, 40, 40)

            super.paintChildren(g)
        }

        private fun setupMouseListeners() {
            addMouseListener(object : MouseAdapter() {
                override fun mouseEntered(e: MouseEvent) {
                    isHovered = true
                    repaint()
                }

                override fun mouseExited(e: MouseEvent) {
                    isHovered = false
                    repaint()
                }

                override fun mouseClicked(e: MouseEvent) {
                    option.onSelect(option)
                    popup?.cancel()
                }
            })
        }

        fun setPopupReference(popup: JBPopup) {
            this.popup = popup
        }
    }
}