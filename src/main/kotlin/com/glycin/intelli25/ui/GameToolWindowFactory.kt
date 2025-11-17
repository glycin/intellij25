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
import javax.swing.JPanel

class GameToolWindowFactory : ToolWindowFactory {

    private var currentContent: Content? = null
    private var gameScreenContainer: ToolWindowBaseComponent? = null

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val gameState = service<GameSaveState>()
        //TODO: Testing remove
        gameState.dialoguesSeen = 0
        gameState.levelsBeaten = 2

        if (gameScreenContainer == null) {
            gameScreenContainer = ToolWindowBaseComponent(project, toolWindow, gameState)
        }
        val contentFactory = ContentFactory.getInstance()

        if (currentContent == null) {
            currentContent = contentFactory.createContent(gameScreenContainer, null, false)
            toolWindow.contentManager.addContent(currentContent!!)
        }

        gameScreenContainer?.showScreen(gameState.levelsBeaten)
    }
}