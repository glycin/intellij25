package com.glycin.intelli25.ui

import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.ui.screens.MainMenuScreen
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ex.ToolWindowEx
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JPanel
import javax.swing.JScrollPane

class ToolWindowBaseComponent(
    project: Project,
    toolWindow: ToolWindow,
    saveState: GameSaveState
) : JPanel(BorderLayout()) {
    private val mainMenu = MainMenuScreen(project, toolWindow, this, saveState)

    init {
        val scrollPane = JBScrollPane(mainMenu).apply {
            border = null
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        }
        add(scrollPane)
        
        // Set tool window width to fit content on the first tool window open
        // This can be important on non-default IDE Zoom levels
        setToolWindowToMatchContentWidth(toolWindow)
    }

    private fun setToolWindowToMatchContentWidth(toolWindow: ToolWindow) {
        invokeLater {
            val desiredWidth = preferredSize.width
            val currentWidth = toolWindow.component.width
            if (toolWindow is ToolWindowEx && currentWidth > 0) {
                toolWindow.stretchWidth(desiredWidth - currentWidth)
            }
        }
    }

    // NOTE: it seems this method is not invoked at the moment
    override fun getMinimumSize(): Dimension {
        return Dimension(JBUI.scale(600), JBUI.scale(500))
    }

    // NOTE: it seems this method is only invoked in the constructor manually at the moment
    override fun getPreferredSize(): Dimension {
        return Dimension(JBUI.scale(650), JBUI.scale(600))
    }

    fun showScreen() {
        mainMenu.updateState()
    }
}