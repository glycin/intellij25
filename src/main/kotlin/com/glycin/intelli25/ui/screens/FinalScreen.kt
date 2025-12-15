package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.model.GameStartupSettings
import com.glycin.intelli25.ui.ToolWindowBaseComponent
import com.glycin.intelli25.util.startGame
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import java.awt.BorderLayout
import javax.swing.JPanel

class FinalScreen(
    private val project: Project,
    private val toolWindow: ToolWindow,
): JPanel() {

    init{
        isOpaque = false
        layout = BorderLayout()

        val baseContent = GameScreenContent(
            project = project,
            startButtonText = "Start Game",
            onStart = { onStart() }
        )

        add(baseContent)
    }

    private fun onStart() {
        toolWindow.hide()
        startGame(
            project,
            toolWindow,
            parent as ToolWindowBaseComponent,
            GameStartupSettings.createLevelOneSettings()
        )
    }
}