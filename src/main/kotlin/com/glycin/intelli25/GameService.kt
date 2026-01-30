package com.glycin.intelli25

import com.glycin.intelli25.model.GameStartupSettings
import com.glycin.intelli25.ui.ToolWindowBaseComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ide.scratch.ScratchRootType
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import kotlinx.coroutines.CoroutineScope

@Service(Service.Level.PROJECT)
class GameService(
    private val project: Project,
    private val scope: CoroutineScope,
) {

    private var game: Game? = null

    fun getProjectScope() = scope

    fun startGame(
        toolWindow: ToolWindow,
        baseComponent: ToolWindowBaseComponent,
        gameStartupSettings: GameStartupSettings
    ) {
        if (game != null) {
            return
        }

        val editor = getSelectedEditorOrCreateScratchFileEditor()
        editor?.let { e ->
            game = Game(project, e, scope, toolWindow, baseComponent, gameStartupSettings)
        }
    }

    private fun getSelectedEditorOrCreateScratchFileEditor(): Editor? {
        val selectedEditor = FileEditorManager.getInstance(project).selectedTextEditor
        // If a user doesn't have any open editor, create a scratch file editor as the game requires some editor
        return selectedEditor ?: createScratchFileEditor()
    }

    private fun createScratchFileEditor(): Editor? = ScratchRootType.getInstance()
        .createScratchFile(project, "IDE Survivors Game", null, "")
        ?.let { file ->
            FileEditorManager.getInstance(project).openTextEditor(OpenFileDescriptor(project, file), true)
        }

    fun resetGame() {
        game = null
    }
}