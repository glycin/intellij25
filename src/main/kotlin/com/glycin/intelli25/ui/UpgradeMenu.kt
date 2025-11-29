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
import java.awt.Font
import java.awt.GridLayout
import java.awt.Image
import java.awt.Rectangle
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.ImageIcon
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

            upgrades.forEachIndexed { index, up ->
                add(createUpgradeCard(up, index))
                add(Box.createRigidArea(Dimension(0, 10)))
            }
        }
    }

    private fun createUpgradeCard(option: UpgradeOption, index: Int): JPanel {
        return UpgradeCard(option, index)
    }

    inner class UpgradeCard(private val option: UpgradeOption, index: Int) : JPanel() {
        private var popup: JBPopup? = null

        private val cardBackground = when(index) {
            0 -> GameColors.jbBlueLight
            1 -> GameColors.jbRedLight
            else -> GameColors.jbOrangeLight
        }

        private val textColor = when(index) {
            0 -> JBColor(Color(2, 82, 163, 255), Color(2, 82, 163, 255))
            1 -> JBColor(Color(196, 0, 43, 255), Color(196, 0, 43, 255))
            else -> JBColor(Color(181, 92, 0, 255), Color(181, 92, 0, 255))
        }

        init {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            preferredSize = Dimension(800, 120)
            maximumSize = Dimension(Int.MAX_VALUE, 120)
            background = cardBackground
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
            val targetSize = 64
            val scaledIcon = option.icon.getScaledInstance(targetSize, targetSize, Image.SCALE_SMOOTH)
            return JLabel(ImageIcon(scaledIcon)).apply {
                preferredSize = Dimension(targetSize, targetSize)
                minimumSize = Dimension(targetSize, targetSize)
                maximumSize = Dimension(targetSize, targetSize)
                alignmentY = 0.5f
            }
        }

        private fun createTextSection(): JPanel {
            return JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                isOpaque = false
                alignmentY = 0.5f

                add(JLabel("${option.title}${if(option.subTitle.isNotEmpty()) " - ${option.subTitle}" else ""}").apply {
                    font = Fonts.pixelFont.deriveFont(Font.BOLD, 24.0f)
                    foreground = textColor
                })

                add(JLabel(option.effect).apply {
                    font = Fonts.pixelFont.deriveFont(Font.PLAIN, 16.0f)
                    foreground = textColor
                    border = JBUI.Borders.emptyTop(4)
                })
            }
        }

        private fun setupMouseListeners() {
            addMouseListener(object : MouseAdapter() {
                override fun mouseEntered(e: MouseEvent) {
                    background = cardBackground
                    border = BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(GameColors.jbGreen, 3),
                        JBUI.Borders.empty(10)
                    )
                    repaint()
                }

                override fun mouseExited(e: MouseEvent) {
                    background = cardBackground
                    border = BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(cardBackground, 2),
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
