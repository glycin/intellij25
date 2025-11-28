package com.glycin.intelli25.ui

import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.PNG
import kotlinx.coroutines.CoroutineScope
import java.awt.BorderLayout
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JPanel

private const val SCROLL_X_PADDING = 5
private const val SCROLL_Y_PADDING = 3
private const val SCROLL_BOTTOM_PADDING = 15

class DialogComponent(
    deltaTime: Long,
    texts: List<String>,
    scope: CoroutineScope,
    private val onClose: () -> Unit
): JPanel() {

    private val scrollPane: DialogScrollPane
    private val nextButton: JButton
    private var readyToStart = false

    init {
        layout = BorderLayout()
        isOpaque = false
        scrollPane = DialogScrollPane(texts, scope, deltaTime){
            nextButton.text = "Start!"
            readyToStart = true
        }.apply {
            setBounds(width + SCROLL_X_PADDING, 0 + SCROLL_Y_PADDING, width - SCROLL_X_PADDING, height - SCROLL_BOTTOM_PADDING)
        }

        nextButton = JButton("Next").apply {
            isFocusable = false
            font = Fonts.pixelFont.deriveFont(Font.BOLD, 16f)
            foreground = GameColors.jbOrange
            background = GameColors.transparent
            isContentAreaFilled = false
            isOpaque = false
            addActionListener {
                if(readyToStart){
                    onClose()
                } else {
                    scrollPane.nextText()
                }
            }
        }

        val buttonPanel = JPanel(BorderLayout()).apply {
            isOpaque = false
            border = BorderFactory.createEmptyBorder(10, 20, 0, 20)
            add(nextButton, BorderLayout.EAST)
        }
        add(buttonPanel, BorderLayout.NORTH)

        val centerPanel = object : JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                if (g is Graphics2D) {
                    g.drawImage(PNG.SPEECH_BUBBLE, 0, 0, width, height, null)
                }
            }
        }.apply {
            layout = BorderLayout()
            isOpaque = false
            add(scrollPane, BorderLayout.CENTER)
            border = BorderFactory.createEmptyBorder(20, 20, 20, 20)
        }

        add(centerPanel, BorderLayout.CENTER)
    }

    fun deactivate() {
        scrollPane.stop()
    }
}