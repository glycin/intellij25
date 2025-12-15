package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.GameService
import com.glycin.intelli25.model.GameStartupSettings
import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.ui.ToolWindowBaseComponent
import com.glycin.intelli25.util.PNG
import com.glycin.intelli25.util.startGame
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import java.awt.BorderLayout
import javax.swing.JPanel

class LevelThreeScreen(
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
            onStart = { onStart() },
            startButtonText = "Start Level Three",
        )
        add(baseContent)
    }

    private fun onStart() {
        if(saveState.dialoguesSeen == 2) {
            val projectScope = project.service<GameService>().getProjectScope()
            val dialogueScreen = DialogueScreen(
                title = "Adulthood...",
                texts = CutsceneTexts.screenThree,
                scope = projectScope,
                backGroundImages = mapOf(0 to PNG.STORY_SCREEN_3),
                onReadyToStart =  {
                    toolWindow.hide()
                    saveState.dialoguesSeen++
                    wrapper.enableOk()
                }
            )

            wrapper = DialogueScreenWrapper(project, dialogueScreen)

            if (wrapper.showAndGet()) {
                toolWindow.hide()
                startGame(
                    project,
                    toolWindow,
                    parent as ToolWindowBaseComponent,
                    GameStartupSettings.createLevelThreeSettings()
                )
            }

        } else {
            toolWindow.hide()
            startGame(
                project,
                toolWindow,
                parent as ToolWindowBaseComponent,
                GameStartupSettings.createLevelThreeSettings()
            )
        }
    }
}