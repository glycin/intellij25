package com.glycin.intelli25

import com.glycin.intelli25.ui.ToolWindowBaseComponent
import com.intellij.openapi.components.Service
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
        if(game != null) {
            println("Game is already running!")
            return
        }
        
        println("Starting game service")
        FileEditorManager.getInstance(project).selectedTextEditor?.let { e ->
            game = Game(project, e, scope, toolWindow, baseComponent)
        }
    }
}