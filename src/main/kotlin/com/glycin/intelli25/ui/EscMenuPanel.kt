package com.glycin.intelli25.ui

import com.glycin.intelli25.util.GameColors
import com.intellij.openapi.ui.popup.JBPopup
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel

class EscMenuPanel(
    private val onResume: () -> Unit,
    private val onQuit: () -> Unit
) : JPanel() {
    private var popup: JBPopup? = null

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        background = GameColors.black
        border = BorderFactory.createEmptyBorder(20, 40, 20, 40)

        val resumeBtn = createMenuButton("RESUME") {
            popup?.cancel()
            onResume()
        }

        val quitBtn = createMenuButton("QUIT GAME") {
            popup?.cancel()
            onQuit()
        }

        add(Box.createVerticalGlue())
        add(resumeBtn)
        add(Box.createVerticalStrut(15))
        add(quitBtn)
        add(Box.createVerticalGlue())
    }

    fun setPopup(jbPopup: JBPopup) {
        this.popup = jbPopup
    }

    private fun createMenuButton(text: String, action: () -> Unit): JButton {
        return GameButton(
            backgroundColor = GameColors.jbPurple,
            hoverColor = GameColors.jbOrange,
            textColor = GameColors.white,
            text = text,
            arc = 20,
        ).apply {
            addActionListener { action() }
        }
    }
}