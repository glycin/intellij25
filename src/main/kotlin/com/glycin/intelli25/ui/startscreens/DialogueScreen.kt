package com.glycin.intelli25.ui.startscreens

import com.glycin.intelli25.ui.DialogComponent
import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.util.PNG
import com.glycin.intelli25.util.GameColors
import kotlinx.coroutines.CoroutineScope
import java.awt.BorderLayout
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class DialogueScreen(
    val title: String,
    texts: List<String>,
    scope: CoroutineScope,
    private val onReadyToStart: (() -> Unit)? = null
) : JPanel() {

    private val dialoguePanel: DialogComponent

    init {
        isOpaque = false
        layout = BorderLayout()

        val titleLabel = JLabel(title).apply {
            font = Fonts.pixelFont.deriveFont(24.0f)
            foreground = GameColors.white
            horizontalAlignment = SwingConstants.LEFT
            border = BorderFactory.createEmptyBorder(20, 20, 20, 0)
        }
        add(titleLabel, BorderLayout.NORTH)

        dialoguePanel = DialogComponent(
            deltaTime = 1000 / 120L,
            texts = texts,
            scope = scope,
            onReadyToStart = { onReadyToStart?.invoke() }
        ).apply {
            border = BorderFactory.createEmptyBorder(0, 100, 40, 100)
            isOpaque = false
        }

        add(dialoguePanel, BorderLayout.SOUTH) // bottom of screen
    }

    fun advance() = dialoguePanel.advance()

    fun deactivate() = dialoguePanel.deactivate()

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (g is Graphics2D) {
            g.color = GameColors.black
            g.fillRect(0, 0, width, height)
            g.drawImage(PNG.RUNZO, width / 2, height / 4, 128, 128, null)
        }
    }
}