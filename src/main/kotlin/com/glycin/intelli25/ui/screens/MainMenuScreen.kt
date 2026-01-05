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
import java.awt.Dimension
import javax.swing.JPanel

class MainMenuScreen(
    private val project: Project,
    private val toolWindow: ToolWindow,
    private val saveState: GameSaveState,
): JPanel() {

    lateinit var wrapper: DialogueScreenWrapper
    private val gameScreenContent: GameScreenContent

    init{
        isOpaque = false
        layout = BorderLayout()
        gameScreenContent = GameScreenContent(
            project = project,
            toolWindow = toolWindow,
            saveState = saveState,
            onStartOne = { onStartOne() },
            onStartTwo = { onStartTwo() },
            onStartThree = { onStartThree() },
        )

        add(gameScreenContent)
    }

    fun updateState() {
        gameScreenContent.refreshState()
    }

    private fun onStartOne() {
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

    private fun onStartTwo() {
        toolWindow.hide()
        startGame(
            project,
            toolWindow,
            parent as ToolWindowBaseComponent,
            GameStartupSettings.createLevelTwoSettings()
        )
    }

    private fun onStartThree() {
        toolWindow.hide()
        startGame(
            project,
            toolWindow,
            parent as ToolWindowBaseComponent,
            GameStartupSettings.createLevelThreeSettings()
        )
    }
}