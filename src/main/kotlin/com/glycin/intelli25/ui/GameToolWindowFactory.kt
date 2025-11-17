package com.glycin.intelli25.ui

import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.ui.startscreens.FinalScreen
import com.glycin.intelli25.ui.startscreens.LevelOneScreen
import com.glycin.intelli25.ui.startscreens.LevelThreeScreen
import com.glycin.intelli25.ui.startscreens.LevelTwoScreen
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory

class GameToolWindowFactory : ToolWindowFactory {
    private val contentFactory = ContentFactory.getInstance()

    private var currentContent: Content? = null

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
        //TODO: Debugging remove
        gameState.levelsBeaten = 0
        gameState.dialoguesSeen = 0
        
        val contentPanel = when(gameState.levelsBeaten) {
            0 -> LevelOneScreen(project, toolWindow, gameState)
            1 -> LevelTwoScreen(project, toolWindow)
            2 -> LevelThreeScreen(project, toolWindow)
            else -> FinalScreen(project, toolWindow)
        }

        currentContent = contentFactory.createContent(contentPanel, null, false)
        toolWindow.contentManager.addContent(currentContent!!)
    }
}