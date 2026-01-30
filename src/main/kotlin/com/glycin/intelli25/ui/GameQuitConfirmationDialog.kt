package com.glycin.intelli25.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import javax.swing.*
import javax.swing.border.Border

/**
 * Modal dialog that is shown to confirm game exit.
 *
 * A dialog wrapper is used instead of JPopup ([com.intellij.openapi.ui.popup.JBPopupFactory]) because we need a blocking UI logic for the case when we show
 * this dialog when confirming Game Quit on Editor Close event
 * (see [com.glycin.intelli25.ShowStopGameConfirmationDialogOnEditorCloseListener]).
 *
 * One of the consequences of using a dialog is that it doesn't have rounded corners as in the popup.
 * Achieving this would require some fragile, potentially buggy logic.
 *
 * This dialog uses a custom panel with custom rendered buttons created in [createCenterPanel],
 * the default dialogs actions are not used
 */
class GameQuitConfirmationDialog(
    project: Project,
    private val title: String = "GAME PAUSED",
    private val quitButtonLabel: String = "QUIT GAME",
    private val onResume: () -> Unit = {},
    private val onQuit: () -> Unit = {}
) : DialogWrapper(project) {

    private var userWantsToQuit = false

    init {
        init()
    }

    /**
     * Shows the dialog and returns the user's choice
     *
     * @return true if the user clicked "QUIT GAME", false if user clicked "RESUME"
     */
    fun showAndGetResult(): Boolean {
        show()
        return userWantsToQuit
    }

    // Create a wrapper panel that includes the custom title and content
    override fun createCenterPanel(): JComponent {
        return JPanel().apply {
            layout = BorderLayout()

            // Add a title panel at the top (similar to popup title)
            val titleLabel = JLabel(title).apply {
                horizontalAlignment = SwingConstants.CENTER
                font = Fonts.jbMono.deriveFont(16.0f)

                val vPadding = 16
                val hPadding = 24
                border = BorderFactory.createEmptyBorder(vPadding, hPadding, vPadding, hPadding)
            }

            val quitConfirmationPanel = GameQuitConfirmationPanel(
                onResume = {
                    userWantsToQuit = false
                    this@GameQuitConfirmationDialog.onResume()
                    close(0)
                },
                onQuit = {
                    userWantsToQuit = true
                    this@GameQuitConfirmationDialog.onQuit()
                    close(0)
                },
                quitButtonLabel = quitButtonLabel
            )

            add(titleLabel, BorderLayout.NORTH)
            add(quitConfirmationPanel, BorderLayout.CENTER)

            // Make sure that the window border is empty to avoid any extra empty space at the top of the dialog
            removeDefaultPanelBorderAndMakeDraggable(titleLabel)
        }
    }


    private fun removeDefaultPanelBorderAndMakeDraggable(titleLabel: JLabel) {
        // Make the dialog undecorated (no window controls like close, minimize and no standard title (we render it in our way))
        setUndecorated(true)

        window?.let { win ->
            // Remove the remaining empty space at the top of the dialog
            (win as? JDialog)?.let { dialog ->
                dialog.rootPane?.border = null
            }

            // As we have hidden the default decorations and the default border, the dialog is not draggable by default.
            // We manually make the dialog draggable, from the title
            DragWindowUtil.setupDragListeners(titleLabel, win)
        }
    }

    override fun createContentPaneBorder(): Border? {
        return null
    }

    override fun createNorthPanel(): JComponent? {
        return null
    }

    override fun createSouthPanel(): JComponent? {
        return null
    }
}
