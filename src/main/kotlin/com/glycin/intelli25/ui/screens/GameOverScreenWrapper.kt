package com.glycin.intelli25.ui.screens

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.wm.ToolWindow
import javax.swing.Action
import javax.swing.JComponent

class GameOverScreenWrapper(
    private val screen: GameOverScreen,
    private val toolWindow: ToolWindow,
    project: Project,
): DialogWrapper(project) {

        init {
            title = screen.title
            isModal = true
            init()

            setOKButtonText("Continue!")
            isOKActionEnabled = false
            cancelAction.putValue(Action.NAME, "Close")
        }

        override fun createCenterPanel(): JComponent = screen

        override fun doCancelAction() {
            close(OK_EXIT_CODE)
            super.doCancelAction()
        }

        override fun doOKAction() {
            toolWindow.show()
            close(OK_EXIT_CODE)
            super.doOKAction()
        }
}