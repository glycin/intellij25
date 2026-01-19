package com.glycin.intelli25.ui

import com.glycin.intelli25.persistence.GameSaveState
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory

class GameToolWindowFactory : ToolWindowFactory, DumbAware {

    private var currentContent: Content? = null
    private var gameScreenContainer: ToolWindowBaseComponent? = null

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val gameState = service<GameSaveState>()

        if (gameScreenContainer == null) {
            gameScreenContainer = ToolWindowBaseComponent(project, toolWindow, gameState)
        }
        val contentFactory = ContentFactory.getInstance()

        if (currentContent == null) {
            currentContent = contentFactory.createContent(gameScreenContainer, null, false)
            toolWindow.contentManager.addContent(currentContent!!)
        }

        gameScreenContainer?.showScreen()
    }
}