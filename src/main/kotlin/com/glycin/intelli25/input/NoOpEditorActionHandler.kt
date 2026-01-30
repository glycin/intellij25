package com.glycin.intelli25.input

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler

/**
 * An EditorActionHandler that prevents default editor actions from executing.
 * Used to disable caret movement and other editor actions while the game is running.
 *
 * @param mutedEditor The editor whose actions should be muted
 * @param originalAction The original action handler to invoke for other editors
 */
class NoOpEditorActionHandler(
    private val mutedEditor: Editor,
    private val originalAction: EditorActionHandler
) : EditorActionHandler() {
    override fun doExecute(editor: Editor, caret: Caret?, dataContext: DataContext) {
        if (editor == mutedEditor) {
            // Do nothing for the muted editor - this prevents caret movement and other editor actions
        } else {
            // Invoke the original action for other editors
            originalAction.execute(editor, caret, dataContext)
        }
    }
}
