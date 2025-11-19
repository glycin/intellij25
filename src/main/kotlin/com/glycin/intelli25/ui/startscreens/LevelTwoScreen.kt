package com.glycin.intelli25.ui.startscreens

import com.glycin.intelli25.GameService
import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.ui.PNG
import com.glycin.intelli25.ui.StartButton
import com.glycin.intelli25.ui.ToolWindowBaseComponent
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.startGame
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
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

class LevelTwoScreen(
    private val project: Project,
    private val toolWindow: ToolWindow,
    private val saveState: GameSaveState,
): JPanel() {
    init{
        isOpaque = false
        layout = BorderLayout()

        val baseContent = LevelTwoScreenContent {
            if(saveState.dialoguesSeen == 1) {
                val projectScope = project.service<GameService>().getProjectScope()
                val dialogueScreen = LevelTwoDialogueScreen(projectScope) {
                    toolWindow.hide()
                    saveState.dialoguesSeen++
                    startGame(project, toolWindow, parent as ToolWindowBaseComponent)
                }
                remove(it)
                add(dialogueScreen)
                revalidate()
                repaint()
            } else {
                toolWindow.hide()
                startGame(project, toolWindow, parent as ToolWindowBaseComponent)
            }
        }

        add(baseContent)
    }
}

private class LevelTwoScreenContent(
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
            backgroundColor = GameColors.jb2001Blue,
            hoverColor = GameColors.jbOrange,
            text = "Start Game",
        ).apply {
            addActionListener {
                onStart(this@LevelTwoScreenContent)
            }
        }
        buttonPanel.add(startButton)
        add(buttonPanel, BorderLayout.CENTER)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if(g is Graphics2D) {
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
            val gradient = GradientPaint(
                width.toFloat(), 0f, GameColors.jb2001Blue,
                0f, height.toFloat(), GameColors.jb2001Orange
            )
            g.paint = gradient
            g.fillRect(0, 0, width, height)
            g.drawImage(PNG.secondLogo, (width / 2) - 64, height - 256, 128, 128, null)
        }
    }
}