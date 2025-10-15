package com.glycin.intelli25.ui

import com.glycin.intelli25.shared.GameColors
import com.glycin.intelli25.shared.GameGeneralState
import com.intellij.openapi.Disposable
import com.intellij.ui.JBColor
import kotlinx.coroutines.CoroutineScope
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JButton
import javax.swing.JComponent

private const val SCROLL_X_PADDING = 5
private const val SCROLL_Y_PADDING = 3
private const val SCROLL_BOTTOM_PADDING = 25

class DialogComponent(
    texts: List<String>,
    private val ggState: GameGeneralState,
    scope: CoroutineScope,
): JComponent(), Disposable {

    private val scrollPane: DialogScrollPane
    private val nextButton: JButton
    val speechPosX = (ggState.maxX / 2) - 256

    init {
        setBounds(0, ggState.maxY - 256, ggState.maxX, ggState.maxY / 4)

        scrollPane = DialogScrollPane(texts, scope, ggState.deltaTime).apply {
            setBounds(speechPosX + SCROLL_X_PADDING, 0 + SCROLL_Y_PADDING, 512 - SCROLL_X_PADDING, height - SCROLL_BOTTOM_PADDING)
        }

        nextButton = JButton("Next").apply {
            isFocusable = false
            font = Fonts.pixelFont.deriveFont(Font.BOLD, 16f)
            foreground = JBColor.GREEN
            background = GameColors.transparent
            isContentAreaFilled = false
            setBounds(speechPosX + 512 + 50, (height / 2) + 50, 100, 50)

            addActionListener {
                scrollPane.nextText()
            }
        }

        add(scrollPane)
        add(nextButton)
        repaint()
        revalidate()
        isOpaque = false
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        setBounds(0, ggState.maxY - 256, ggState.maxX, ggState.maxY / 4)
        scrollPane.setBounds(speechPosX + SCROLL_X_PADDING, 0 + SCROLL_Y_PADDING, 512 - SCROLL_X_PADDING, height - SCROLL_BOTTOM_PADDING)
        if(g is Graphics2D) {
            g.drawImage(PNG.SPEECH_BUBBLE, speechPosX, 0, 512, height, null)
        }
    }

    fun deactivate() {
        scrollPane.stop()
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }
}