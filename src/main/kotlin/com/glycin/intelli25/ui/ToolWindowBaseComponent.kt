package com.glycin.intelli25.ui

import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.ui.screens.MainMenuScreen
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JPanel

class ToolWindowBaseComponent(
    project: Project,
    toolWindow: ToolWindow,
    saveState: GameSaveState
) : JPanel(BorderLayout()) {
    private val mainMenu = MainMenuScreen(project, toolWindow, saveState)

    init {
        add(mainMenu)
    }

    override fun getMinimumSize(): Dimension {
        return Dimension(600, 500)
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(650, 600)
    }

    fun showScreen() {
        mainMenu.updateState()
    }
}