package com.glycin.intelli25.ui

import com.glycin.intelli25.GameService
import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.ui.startscreens.LevelOneDialogueScreen
import com.glycin.intelli25.ui.startscreens.LevelOneScreen
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.ui.content.ContentFactory

class GameToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        project.messageBus.connect(toolWindow.disposable).subscribe(
            ToolWindowManagerListener.TOPIC,
            object : ToolWindowManagerListener {
                override fun stateChanged(toolWindowManager: ToolWindowManager) {
                    val tw = toolWindowManager.getToolWindow(toolWindow.id)
                    if (tw != null && tw.isVisible) {
                        updateContent(project, toolWindow)
                    }
                }
            }
        )
    }

    private fun updateContent(project: Project, toolWindow: ToolWindow) {
        val gameState = service<GameSaveState>()
        val projectScope = project.service<GameService>().getProjectScope()

        val contentPanel = when(gameState.levelsBeaten) {
            0 -> LevelOneDialogueScreen(projectScope) {
                toolWindow.contentManager.removeAllContents(true)
                toolWindow.hide()
            }
            else -> LevelOneScreen()
        }

        /*contentPanel.startButton.addActionListener {
            toolWindow.hide()
        }*/

        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(contentPanel, null, false)
        toolWindow.contentManager.addContent(content)
    }
}