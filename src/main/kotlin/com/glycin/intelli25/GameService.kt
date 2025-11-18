package com.glycin.intelli25

import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.ui.ToolWindowBaseComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import kotlinx.coroutines.CoroutineScope


@Service(Service.Level.PROJECT)
class GameService(
    private val project: Project,
    private val scope: CoroutineScope,
) {

    private var game: Game? = null

    fun getProjectScope() = scope

    fun startGame(toolWindow: ToolWindow, baseComponent: ToolWindowBaseComponent) {
        println("Starting game service")
        FileEditorManager.getInstance(project).selectedTextEditor?.let { e ->
            game = Game(project, e, scope, toolWindow, baseComponent)
        }
    }

    fun stopGame() {
        println("Stopping game service")
        val saveState = service<GameSaveState>()
        game?.dispose()
        game == null
    }
}