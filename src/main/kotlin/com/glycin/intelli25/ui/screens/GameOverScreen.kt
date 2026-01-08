package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeBackpackItem
import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.GameGlobalState
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.util.IconLoader
import com.intellij.util.IconUtil
import java.awt.BasicStroke
import java.awt.BorderLayout
import java.awt.Cursor
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GridBagLayout
import java.awt.Image
import java.awt.RenderingHints
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.Icon
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class GameOverScreen(
    val title: String,
    val survived: Boolean,
    private val player: Player,
    private val ggState: GameGlobalState,
    private val subText: String,
): JPanel(){

    init {
        isOpaque = false
        layout = BorderLayout()
        preferredSize = Dimension(800, 600)
        minimumSize = Dimension(600, 400)

        val titleLabel = JLabel(title).apply {
            font = Fonts.pixelFont.deriveFont(24.0f)
            foreground = GameColors.green
            horizontalAlignment = SwingConstants.CENTER
            border = BorderFactory.createEmptyBorder(20, 20, 20, 0)
        }
        add(titleLabel, BorderLayout.NORTH)

        val centerPanel = JPanel().apply {
            isOpaque = false
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
        }

        val upgradesPanel = JPanel().apply {
            isOpaque = false
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = CENTER_ALIGNMENT
        }
        upgradesPanel.add(Box.createHorizontalGlue())
        player.upgrades.forEach { (title, item) ->
            upgradesPanel.add(createUpgradeSummaryLabel(title, item))
        }
        upgradesPanel.add(Box.createHorizontalGlue())
        centerPanel.add(upgradesPanel)

        centerPanel.add(Box.createVerticalStrut(20))
        centerPanel.add(JLabel("Final Score: ${ggState.score}").apply {
            font = Fonts.pixelFont.deriveFont(20.0f)
            foreground = GameColors.white
            alignmentX = CENTER_ALIGNMENT
        })

        if (subText.isNotEmpty()) {
            centerPanel.add(Box.createVerticalStrut(15))
            val wrapped = "<html><div style='text-align: center; width: 350px;'>$subText</div></html>"
            val subTextLabel = JLabel(wrapped).apply {
                font = Fonts.pixelFont.deriveFont(16.0f)
                foreground = GameColors.red
                alignmentX = CENTER_ALIGNMENT
                preferredSize = Dimension(450, preferredSize.height)
            }
            centerPanel.add(subTextLabel)
        }

        centerPanel.add(Box.createVerticalStrut(if (survived) 40 else 150))

        val wrapperPanel = JPanel(GridBagLayout()).apply {
            isOpaque = false
            add(centerPanel)
        }

        add(wrapperPanel, BorderLayout.CENTER)

        if (survived) {
            val socialsPanel = JPanel().apply {
                isOpaque = false
                layout = FlowLayout(FlowLayout.CENTER, 20, 20)
                border = BorderFactory.createEmptyBorder(0, 0, 30, 0)
            }

            val rawText = "I survived in IDE survivors! My final score was ${ggState.score}."
            val encodedText = URLEncoder.encode(rawText, StandardCharsets.UTF_8)

            // 1. Twitter - Supports text
            socialsPanel.add(createIconSocialButton(SocialIcons.X, "Share on X") {
                "https://twitter.com/intent/tweet?text=$encodedText" // &url=$encodedUrl (optional)
            })

            // 2. Bluesky - Supports text
            socialsPanel.add(createIconSocialButton(SocialIcons.Bluesky, "Share on Bluesky") {
                "https://bsky.app/intent/compose?text=$encodedText"
            })

            // 3. LinkedIn - Supports text via 'feed' parameter (unofficial but widely used)
            socialsPanel.add(createIconSocialButton(SocialIcons.LinkedIn, "Share on LinkedIn") {
                "https://www.linkedin.com/feed/?shareActive=true&text=$encodedText"
            })

            add(socialsPanel, BorderLayout.SOUTH)
        }
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (g is Graphics2D) {
            g.color = GameColors.black
            g.fillRect(0, 0, width, height)
        }
    }

    private fun createUpgradeSummaryLabel(title: String, item: UpgradeBackpackItem): JLabel {
        return JLabel("x${item.count}").apply {
            icon = item.image?.let { ImageIcon(it.getScaledInstance(40, 40, Image.SCALE_SMOOTH)) }
            foreground = GameColors.white
            font = Fonts.pixelFont.deriveFont(12f)
            verticalTextPosition = SwingConstants.BOTTOM
            horizontalTextPosition = SwingConstants.CENTER
            iconTextGap = 2
            border = BorderFactory.createEmptyBorder(0, 8, 0, 8)
            toolTipText = title
        }
    }

    private fun createIconSocialButton(icon: Icon, toolTip: String, urlProvider: () -> String): JLabel {
        val scaledIcon = icon.scale()

        return object : JLabel(scaledIcon) {
            private var isHovered = false

            init {
                toolTipText = toolTip
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                preferredSize = Dimension(64, 64)
                isOpaque = false

                addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent?) {
                        BrowserUtil.browse(urlProvider())
                    }
                    override fun mouseEntered(e: MouseEvent?) {
                        isHovered = true
                        repaint()
                    }
                    override fun mouseExited(e: MouseEvent?) {
                        isHovered = false
                        repaint()
                    }
                })
            }

            override fun paintComponent(g: Graphics) {
                val g2 = g.create() as Graphics2D
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

                g2.color = GameColors.jbOrange
                g2.fillOval(0, 0, width - 1, height - 1)

                g2.color = if (isHovered) GameColors.jbPurple else GameColors.jbOrange
                g2.stroke = BasicStroke(2.5f)
                g2.drawOval(0, 0, width - 2, height - 2)

                val iconX = (width - scaledIcon.iconWidth) / 2
                val iconY = (height - scaledIcon.iconHeight) / 2
                scaledIcon.paintIcon(this, g2, iconX, iconY)

                g2.dispose()
            }
        }
    }

    private fun Icon.scale(): Icon {
        val targetSize = 32
        val scaleFactor = targetSize.toFloat() / this.iconWidth.toFloat()
        return IconUtil.scale(this, null, scaleFactor)
    }

    companion object {
        fun getSurvivedScreen(ggState: GameGlobalState, player: Player) = GameOverScreen(
            title = "You've Survived!",
            ggState = ggState,
            player = player,
            subText = "",
            survived = true
        )

        fun getGameOverScreen(ggState: GameGlobalState, player: Player) = GameOverScreen(
            title = "Game Over",
            ggState = ggState,
            player = player,
            subText = "Oh no, the bugs got you! Try again, and maybe see if different upgrades fit your play style better",
            survived = false
        )
    }
}

object SocialIcons {
    val X = IconLoader.getIcon("/icon/twitter.svg", GameOverScreen::class.java)
    val Bluesky = IconLoader.getIcon("/icon/bluesky.svg", GameOverScreen::class.java)
    val LinkedIn = IconLoader.getIcon("/icon/linkedin.svg", GameOverScreen::class.java)
}