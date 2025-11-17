package com.glycin.intelli25

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope


@Service(Service.Level.PROJECT)
class GameService(
    private val project: Project,
    private val scope: CoroutineScope,
) {

    private var game: Game? = null

    fun getProjectScope() = scope

    fun startGame() {
        println("Starting game service")
        FileEditorManager.getInstance(project).selectedTextEditor?.let { e ->
            //game = Game(project, e, scope)
        }
    }

    fun stopGame() {
        println("Stopping game service")
    }
}