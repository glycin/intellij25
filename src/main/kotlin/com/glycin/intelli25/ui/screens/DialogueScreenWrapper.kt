package com.glycin.intelli25.ui.screens

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.Action
import javax.swing.JComponent

class DialogueScreenWrapper(
    project: Project,
    private val dialogue: DialogueScreen
) : DialogWrapper(project) {

    private var readyToStart = false

    init {
        title = dialogue.title
        isModal = true
        init()

        setOKButtonText("Start!")
        isOKActionEnabled = false
        cancelAction.putValue(Action.NAME, "Next")
    }

    override fun createCenterPanel(): JComponent = dialogue

    fun enableOk() {
        readyToStart = true
        isOKActionEnabled = true

        getButton(okAction)?.let { btn ->
            rootPane?.defaultButton = btn
            btn.requestFocusInWindow()
        }
    }

    override fun doCancelAction() {
        if (!readyToStart) {
            dialogue.advance()
            return
        }

        dialogue.deactivate()
        super.doCancelAction()
    }

    override fun doOKAction() {
        dialogue.deactivate()
        super.doOKAction()
    }
}