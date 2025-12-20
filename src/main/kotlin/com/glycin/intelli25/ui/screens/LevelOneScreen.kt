package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.GameService
import com.glycin.intelli25.model.GameStartupSettings
import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.ui.ToolWindowBaseComponent
import com.glycin.intelli25.util.PNG
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.glycin.intelli25.util.startGame
import java.awt.BorderLayout
import javax.swing.JPanel

class LevelOneScreen(
    private val project: Project,
    private val toolWindow: ToolWindow,
    private val saveState: GameSaveState,
): JPanel() {

    lateinit var wrapper: DialogueScreenWrapper

    init{
        isOpaque = false
        layout = BorderLayout()

        val baseContent = GameScreenContent(
            project = project,
            startButtonText = "Start Game",
            toolWindow = toolWindow,
            onStart = { onStart() },
        )
        
        add(baseContent)
    }

    private fun onStart() {
        toolWindow.hide()
        if(saveState.dialoguesSeen == 0) {
            val projectScope = project.service<GameService>().getProjectScope()
            val dialogueScreen = DialogueScreen(
                title = "Origins...",
                texts = CutsceneTexts.screenOne,
                scope = projectScope,
                backGroundImages = mapOf(0 to PNG.STORY_SCREEN_1),
                onReadyToStart = {
                    saveState.dialoguesSeen++
                    wrapper.enableOk()
                }
            )

            wrapper = DialogueScreenWrapper(project, "Start!", dialogueScreen)

            if (wrapper.showAndGet()) {
                toolWindow.hide()
                startGame(
                    project,
                    toolWindow,
                    parent as ToolWindowBaseComponent,
                    GameStartupSettings.createLevelOneSettings()
                )
            }

        } else {
            startGame(
                project,
                toolWindow,
                parent as ToolWindowBaseComponent,
                GameStartupSettings.createLevelOneSettings()
            )
        }
    }
}