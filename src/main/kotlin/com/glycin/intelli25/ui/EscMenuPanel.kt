package com.glycin.intelli25.ui

import com.glycin.intelli25.util.GameColors
import com.intellij.openapi.ui.popup.JBPopup
import com.intellij.ui.JBColor
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

        val resumeBtn = createMenuButton("RESUME", GameColors.jbGreen) {
            popup?.cancel()
            onResume()
        }

        val quitBtn = createMenuButton("QUIT GAME", GameColors.red) {
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

    private fun createMenuButton(text: String, buttonColor: JBColor, action: () -> Unit): JButton {
        return StartButton(
            backgroundColor = buttonColor,
            hoverColor = GameColors.jbOrange,
            textColor = GameColors.white,
            text = text,
            arc = 24,
            filled = false
        ).apply {
            addActionListener { action() }
        }
    }
}