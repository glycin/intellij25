package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.PNG
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GridBagLayout
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class GameOverScreen(
    val title: String,
    val survived: Boolean,
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
            border = BorderFactory.createEmptyBorder(120, 0, 0, 0)
        }

        val scoreLabel = JLabel("Final Score: ${ggState.score}").apply {
            font = Fonts.pixelFont.deriveFont(20.0f)
            background = GameColors.black
            foreground = GameColors.white
            alignmentX = CENTER_ALIGNMENT
            border = BorderFactory.createEmptyBorder(10, 0, 10, 0)
        }
        centerPanel.add(scoreLabel)

        if (subText.isNotEmpty()) {
            val wrapped = "<html><div style='text-align: center; width: 800px;'>$subText</div></html>"
            val subTextLabel = JLabel(wrapped).apply {
                font = Fonts.pixelFont.deriveFont(18.0f)
                foreground = GameColors.red
                alignmentX = CENTER_ALIGNMENT
                border = BorderFactory.createEmptyBorder(10, 0, 10, 0)
                preferredSize = Dimension(500, preferredSize.height)
            }
            centerPanel.add(subTextLabel)
        }

        centerPanel.add(Box.createVerticalStrut(150))

        val wrapperPanel = JPanel(GridBagLayout()).apply {
            isOpaque = false
            add(centerPanel)
        }

        add(wrapperPanel, BorderLayout.CENTER)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (g is Graphics2D) {
            g.color = GameColors.black
            g.fillRect(0, 0, width, height)
            val image = if (survived) PNG.STORY_SCREEN_5 else PNG.STORY_SCREEN_2
            image?.let { img ->
                val imgWidth = img.width
                val imgHeight = img.height

                val x = (width - imgWidth) / 2
                val y = height / 2 - imgHeight / 2
                g.drawImage(image, x, y, imgWidth, imgWidth, null)
            }
        }
    }

    companion object {
        fun getSurvivedScreen(ggState: GameGlobalState) = GameOverScreen(
            title = "You Survived!",
            ggState = ggState,
            subText = "",
            survived = true
        )

        fun getGameOverScreen(ggState: GameGlobalState) = GameOverScreen(
            title = "Game Over",
            ggState = ggState,
            subText = "Oh no, the bugs got you! Try again, and maybe some different upgrades fit your play style better?",
            survived = false
        )
    }
}