package com.glycin.intelli25.ui

import com.glycin.intelli25.persistence.GameSaveState
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class GameToolWindowFactory : ToolWindowFactory, DumbAware {

    private val Log = Logger.getInstance(this::class.java)

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val gameState = service<GameSaveState>()
        val gameScreenContainer = ToolWindowBaseComponent(project, toolWindow, gameState)
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(gameScreenContainer, null, false)

        toolWindow.contentManager.addContent(content)
        gameScreenContainer.showScreen()

        Log.info(
            """### 
              |### Game tool window created with state:
              |### Dialogues seen: ${gameState.dialoguesSeen}
              |### Enemies seen: ${gameState.enemiesSeen}
              |### Levels beaten: ${gameState.levelsBeaten}
              |###
              |""".trimMargin()
        )
    }
}