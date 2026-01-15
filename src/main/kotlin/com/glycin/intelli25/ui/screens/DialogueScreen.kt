package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.ui.DialogComponent
import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.util.GameColors
import kotlinx.coroutines.CoroutineScope
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class DialogueScreen(
    val title: String,
    texts: List<String>,
    scope: CoroutineScope,
    private val backGroundImages: Map<Int, BufferedImage?>,
    private val onReadyToStart: (() -> Unit)? = null
) : JPanel() {

    private val dialoguePanel: DialogComponent
    private var currentTextIndex = 0

    init {
        isOpaque = false
        layout = BorderLayout()
        preferredSize = Dimension(800, 600)
        minimumSize = Dimension(600, 400)
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

        add(dialoguePanel, BorderLayout.SOUTH)
    }

    fun advance() {
        currentTextIndex++
        repaint()
        revalidate()
        dialoguePanel.advance()
    }

    fun deactivate() = dialoguePanel.deactivate()

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (g is Graphics2D) {
            g.color = GameColors.black
            g.fillRect(0, 0, width, height)
            val image = backGroundImages.filterKeys { it <= currentTextIndex }.maxByOrNull { it.key }?.value

            image?.let {
                val imgWidth = it.width
                val imgHeight = it.height

                val x = (width - imgWidth) / 2
                val y = height / 2 - imgHeight / 2

                g.drawImage(it, x, y, imgWidth, imgHeight, null)
            }
        }
    }
}