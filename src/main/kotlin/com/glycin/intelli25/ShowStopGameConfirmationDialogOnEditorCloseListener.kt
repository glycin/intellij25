package com.glycin.intelli25

import com.glycin.intelli25.ui.UiComponent
import com.glycin.intelli25.util.GameGlobalState
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.vfs.VirtualFile

/**
 * Listener that intercepts editor close events and shows a confirmation dialog
 * when the user tries to close the editor with an active game.
 *
 * It also listens for the "project close" event
 * to filter out the cases when the editor is being closed automatically due to a project or IDE is closed
 */
class ShowStopGameConfirmationDialogOnEditorCloseListener(
    private val project: Project,
    private val game: Game,
    private val gameState: GameGlobalState,
    private val gameEditor: Editor,
    private var gameUiComponent: UiComponent
) : FileEditorManagerListener.Before,
    ProjectManagerListener {

    @Volatile
    private var isProjectClosing = false

    override fun projectClosingBeforeSave(project: Project) {
        if (project == this.project) {
            isProjectClosing = true
        }
    }

    override fun beforeFileClosed(source: FileEditorManager, file: VirtualFile) {
        val isOurEditor = isGameEditorBeingClosed(source, file)
        if (isOurEditor && gameState.gameActive) {
            confirmGameQuitAndCloseEditorIfNeeded()
        }
    }

    private fun confirmGameQuitAndCloseEditorIfNeeded() {
        if (isProjectClosing || project.isDisposed) {
            // If a project is closing/disposed, just stop the game without confirmation
            game.stopGameWithoutEditorCleanups()
        } else {
            // Otherwise, show confirmation for explicit user close
            val wantsToQuit = gameUiComponent.showGameQuitConfirmationDialogBlocking()

            if (wantsToQuit) {
                // User clicked "QUIT GAME" - let the close proceed
                // But first make sure the game stops
                game.stopGameWithoutEditorCleanups()
            } else {
                // User clicked "RESUME" - veto the close operation
                throw ProcessCanceledException()
            }
        }
    }

    private fun isGameEditorBeingClosed(
        source: FileEditorManager,
        file: VirtualFile
    ): Boolean {
        val fileEditors = source.getEditors(file)
        return fileEditors.any { fileEditor ->
            fileEditor is TextEditor && fileEditor.editor == gameEditor
        }
    }
}