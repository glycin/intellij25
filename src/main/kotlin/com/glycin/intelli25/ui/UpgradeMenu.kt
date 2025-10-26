package com.glycin.intelli25.ui

import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.util.GameColors
import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Cursor
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.Rectangle
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.SwingConstants

class UpgradeMenu(
    private val upgrades: List<UpgradeOption>,
): JPanel() {

    init {
        layout = BorderLayout()
        background = GameColors.black
        border = JBUI.Borders.empty(20)

        add(createTitleLabel(), BorderLayout.NORTH)
        add(createOptionsPanel(), BorderLayout.CENTER)
    }

    private fun createTitleLabel(): JLabel {
        return JLabel("CHOOSE YOUR UPGRADE!").apply {
            font = Fonts.pixelFont.deriveFont(1, 26.0f)
            foreground = GameColors.white
            horizontalAlignment = SwingConstants.CENTER
            border = JBUI.Borders.empty(0, 0, 20, 0)
        }
    }

    private fun createOptionsPanel(): JPanel {
        return JPanel(GridLayout(1, 3, 20, 0)).apply {
            isOpaque = false
            upgrades.forEach { up ->
                add(createUpgradeCard(up))
            }
        }
    }

    private fun createUpgradeCard(option: UpgradeOption): JPanel {
        return UpgradeCard(option)
    }

    inner class UpgradeCard(private val option: UpgradeOption) : JPanel() {
        private var isHovered = false
        private var popup: com.intellij.openapi.ui.popup.JBPopup? = null

        init {
            layout = BorderLayout(10, 10)
            preferredSize = Dimension(200, 250)
            border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(JBColor.red, 2),
                JBUI.Borders.empty(15)
            )
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)

            add(createCardContent(), BorderLayout.CENTER)
            setupMouseListeners()
        }

        private fun createCardContent(): JPanel {
            return JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                isOpaque = false

                option.icon.also {
                    val iconLabel = JLabel(it).apply {
                        alignmentX = CENTER_ALIGNMENT
                        bounds = Rectangle(0, 0, 50, 50)
                    }
                    add(iconLabel)
                    add(Box.createRigidArea(Dimension(0, 10)))
                }

                add(JLabel(option.title).apply {
                    font = Fonts.pixelFont.deriveFont(1, 18.0f)
                    foreground = GameColors.white
                    alignmentX = CENTER_ALIGNMENT
                })

                add(Box.createRigidArea(Dimension(0, 10)))
                add(createLevelIndicator())
                add(Box.createRigidArea(Dimension(0, 15)))

                add(JTextArea(option.description).apply {
                    isEditable = false
                    lineWrap = true
                    wrapStyleWord = true
                    isOpaque = false
                    foreground = JBColor.YELLOW
                    font = Fonts.pixelFont.deriveFont(16.0f)
                    alignmentX = CENTER_ALIGNMENT
                })
            }
        }

        private fun createLevelIndicator(): JPanel {
            return JPanel(FlowLayout(FlowLayout.CENTER, 3, 0)).apply {
                isOpaque = false
                    add(JPanel().apply {
                    preferredSize = Dimension(20, 8)
                    background = GameColors.black
                    border = BorderFactory.createLineBorder(
                        GameColors.white,
                        1
                    )
                })
            }
        }

        private fun setupMouseListeners() {
            addMouseListener(object : MouseAdapter() {
                override fun mouseEntered(e: MouseEvent) {
                    isHovered = true
                    background = JBColor(Color(60, 60, 90), Color(60, 60, 90))
                    border = BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                            JBColor(Color(255, 215, 0), Color(255, 215, 0)),
                            3
                        ),
                        JBUI.Borders.empty(15)
                    )
                    repaint()
                }

                override fun mouseExited(e: MouseEvent) {
                    isHovered = false
                    background = JBColor(Color(40, 40, 60), Color(40, 40, 60))
                    border = BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                            JBColor(Color(100, 100, 120), Color(100, 100, 120)),
                            2
                        ),
                        JBUI.Borders.empty(15)
                    )
                    repaint()
                }

                override fun mouseClicked(e: MouseEvent) {
                    option.onSelect(option)
                    popup?.cancel()
                }
            })
        }

        fun setPopupReference(popup: com.intellij.openapi.ui.popup.JBPopup) {
            this.popup = popup
        }
    }
}