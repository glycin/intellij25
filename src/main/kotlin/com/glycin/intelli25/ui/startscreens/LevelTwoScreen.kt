package com.glycin.intelli25.ui.startscreens

import com.glycin.intelli25.GameService
import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.ui.StartButton
import com.glycin.intelli25.util.GameColors
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
): JPanel() {
    init{
        isOpaque = false
        layout = BorderLayout()

        val baseContent = LevelTwoScreenContent {
            val projectScope = project.service<GameService>().getProjectScope()
            val dialogueScreen = LevelTwoDialogueScreen(projectScope) {
                toolWindow.hide()
            }
            remove(it)
            add(dialogueScreen)
            revalidate()
            repaint()
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

        val titleLabel = JLabel("Runee, the IJ Survivor").apply {
            font = Fonts.pixelFont.deriveFont(24.0f)
            foreground = GameColors.white
            horizontalAlignment = SwingConstants.CENTER
            border = BorderFactory.createEmptyBorder(20, 0, 20, 0)
        }
        add(titleLabel, BorderLayout.NORTH)

        val buttonPanel = JPanel(GridBagLayout()).apply {
            isOpaque = false
        }
        val startButton = StartButton("Start Game").apply {
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
                width.toFloat(), 0f, GameColors.jbRed,
                0f, height.toFloat(), GameColors.jbBlue
            )
            g.paint = gradient
            g.fillRect(0, 0, width, height)
        }
    }
}