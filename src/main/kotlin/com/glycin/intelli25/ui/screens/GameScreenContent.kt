package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.ui.StartButton
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.PNG
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import java.awt.BorderLayout
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GridBagLayout
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class GameScreenContent(
    private val project: Project,
    private val onStart: () -> Unit,
    startButtonText: String,
): JPanel() {

    init {
        isOpaque = false
        layout = BorderLayout()

        val titleLabel = JLabel("IDE Survivors").apply {
            font = Fonts.pixelFont.deriveFont(24.0f)
            foreground = GameColors.white
            horizontalAlignment = SwingConstants.CENTER
            border = BorderFactory.createEmptyBorder(20, 0, 20, 0)
        }
        add(titleLabel, BorderLayout.NORTH)

        val buttonColumn = JPanel().apply {
            isOpaque = false
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = CENTER_ALIGNMENT
        }
        val startButton = StartButton(
            backgroundColor = GameColors.jbRed,
            hoverColor = GameColors.jbOrange,
            text = startButtonText,
        ).apply {
            alignmentX = CENTER_ALIGNMENT
            addActionListener {
                onStart()
            }
        }

        val howToPlayButton = StartButton(
            backgroundColor = GameColors.jbPurple,
            hoverColor = GameColors.jbOrange,
            text = "How to play",
        ).apply {
            alignmentX = CENTER_ALIGNMENT
            addActionListener {
                showHowToPlayDialog()
            }
        }

        buttonColumn.add(startButton)
        buttonColumn.add(Box.createVerticalStrut(12))
        buttonColumn.add(howToPlayButton)

        val buttonWrapper = JPanel(GridBagLayout()).apply {
            isOpaque = false
            add(buttonColumn)
        }
        add(buttonWrapper, BorderLayout.CENTER)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if(g is Graphics2D) {
            g.color = GameColors.black
            g.fillRect(0, 0, width, height)
            g.drawImage(PNG.START_BACKGROUND, (width / 2) - 512, (height / 2) - 650, 1024, 1024, null)
        }
    }

    private fun showHowToPlayDialog() {
        Messages.showInfoMessage(
            project,
            """
            How to play:
            - Move with W A S D.
            - Defeat enemies and collect the coins they drop.
            - After the top bar is filled choose your upgrade!
            - Mix and match upgrades to become overpowered!
            - Survive as long as you can!
        """.trimIndent(),
            "How To Play Runzo"
        )
    }
}