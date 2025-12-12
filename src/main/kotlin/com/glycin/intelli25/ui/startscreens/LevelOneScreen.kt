package com.glycin.intelli25.ui.startscreens

import com.glycin.intelli25.GameService
import com.glycin.intelli25.model.GameStartupSettings
import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.ui.StartButton
import com.glycin.intelli25.ui.ToolWindowBaseComponent
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.PNG
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.glycin.intelli25.util.startGame
import com.intellij.openapi.ui.Messages
import java.awt.BorderLayout
import java.awt.GradientPaint
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GridBagLayout
import java.awt.RenderingHints
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class LevelOneScreen(
    private val project: Project,
    private val toolWindow: ToolWindow,
    private val saveState: GameSaveState,
): JPanel() {

    lateinit var wrapper: DialogueScreenWrapper

    init{
        isOpaque = false
        layout = BorderLayout()

        val baseContent = LevelOneScreenContent(project) {
            if(saveState.dialoguesSeen == 0) {
                val projectScope = project.service<GameService>().getProjectScope()
                val dialogueScreen = DialogueScreen(
                    title = "Origins...",
                    texts = CutsceneTexts.screenOne,
                    scope = projectScope,
                    onReadyToStart = {
                        toolWindow.hide()
                        saveState.dialoguesSeen++
                        wrapper.enableOk()
                    }
                )

                wrapper = DialogueScreenWrapper(project, dialogueScreen)

                if (wrapper.showAndGet()) {
                    toolWindow.hide()
                    startGame(
                        project,
                        toolWindow,
                        parent as ToolWindowBaseComponent,
                        GameStartupSettings.createLevelOneSettings()
                    )
                }

            } else {
                toolWindow.hide()
                startGame(
                    project,
                    toolWindow,
                    parent as ToolWindowBaseComponent,
                    GameStartupSettings.createLevelOneSettings()
                )
            }
        }

        add(baseContent)
    }
}

private class LevelOneScreenContent(
    private val project: Project,
    private val onStart: (JPanel) -> Unit,
): JPanel() {

    init {
        isOpaque = false
        layout = BorderLayout()

        val titleLabel = JLabel("Runzo, the IJ Survivor").apply {
            font = Fonts.pixelFont.deriveFont(24.0f)
            foreground = GameColors.white
            horizontalAlignment = SwingConstants.CENTER
            border = BorderFactory.createEmptyBorder(20, 0, 20, 0)
        }
        add(titleLabel, BorderLayout.NORTH)

        val buttonPanel = JPanel(GridBagLayout()).apply {
            isOpaque = false
        }
        val startButton = StartButton(
            backgroundColor = GameColors.jbRed,
            hoverColor = GameColors.jbOrange,
            text = "Start Game",
        ).apply {
            addActionListener {
                onStart(this@LevelOneScreenContent)
            }
        }
        buttonPanel.add(startButton)
        add(buttonPanel, BorderLayout.CENTER)

        val bottomPanel = JPanel(BorderLayout()).apply {
            isOpaque = false
            border = BorderFactory.createEmptyBorder(0, 10, 10, 0)
        }

        val howToPlayButton = StartButton(
            backgroundColor = GameColors.jbPurple,
            hoverColor = GameColors.jbOrange,
            text = "How to play",
        ).apply {
            addActionListener {
                showHowToPlayDialog()
            }
        }
        bottomPanel.add(howToPlayButton, BorderLayout.LINE_START)
        add(bottomPanel, BorderLayout.PAGE_END)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if(g is Graphics2D) {
            g.color = GameColors.black
            g.fillRect(0, 0, width, height)
            g.drawImage(PNG.START_BACKGROUND, (width / 2) - 512, (height / 2) - 512, 1024, 1024, null)
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