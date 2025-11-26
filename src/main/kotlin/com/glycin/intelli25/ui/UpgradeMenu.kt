package com.glycin.intelli25.ui

import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.util.GameColors
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.ui.JBColor
import com.intellij.util.IconUtil
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
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.SwingConstants

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
            font = Fonts.pixelFont.deriveFont(1, 26.0f)
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
                add(Box.createRigidArea(Dimension(0, 10)))
            }
        }
    }

    private fun createUpgradeCard(option: UpgradeOption): JPanel {
        return UpgradeCard(option)
    }

    inner class UpgradeCard(private val option: UpgradeOption) : JPanel() {
        private var popup: JBPopup? = null

        init {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            preferredSize = Dimension(800, 120)
            maximumSize = Dimension(Int.MAX_VALUE, 120)
            background = GameColors.black
            border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GameColors.jbRed, 2),
                JBUI.Borders.empty(10)
            )
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            isOpaque = true

            add(createIconSection())
            add(Box.createRigidArea(Dimension(10, 0)))
            add(createTextSection())

            setupMouseListeners()
        }

        private fun createIconSection(): JComponent {
            return JLabel(option.icon).apply {
                preferredSize = Dimension(48, 48)
                minimumSize = Dimension(48, 48)
                maximumSize = Dimension(48, 48)
                alignmentY = 0.5f
            }
        }

        private fun createTextSection(): JPanel {
            return JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                isOpaque = false
                alignmentY = 0.5f

                add(JLabel("${option.title}: ${option.subTitle}").apply {
                    font = Fonts.pixelFont.deriveFont(1, 16.0f)
                    foreground = GameColors.white
                })

                add(JLabel(option.description).apply {
                    font = Fonts.pixelFont.deriveFont(13.0f)
                    foreground = GameColors.white
                })

                add(JLabel(option.effect).apply {
                    font = Fonts.pixelFont.deriveFont(1, 13.0f)
                    foreground = GameColors.jbOrange
                    border = JBUI.Borders.emptyTop(4)
                })
            }
        }

        private fun setupMouseListeners() {
            addMouseListener(object : MouseAdapter() {
                override fun mouseEntered(e: MouseEvent) {
                    background = GameColors.black
                    border = BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(GameColors.jbOrange, 3),
                        JBUI.Borders.empty(10)
                    )
                    repaint()
                }

                override fun mouseExited(e: MouseEvent) {
                    background = GameColors.black
                    border = BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(GameColors.jbRed, 2),
                        JBUI.Borders.empty(10)
                    )
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
