package com.glycin.intelli25.ui

import com.glycin.intelli25.util.GameColors
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.JBInsets
import com.intellij.util.ui.JBUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Dimension
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JTextPane
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants

class DialogScrollPane(
    private val texts: List<String>,
    private val scope: CoroutineScope,
    private val deltaTime: Long,
    private val onFinished: () -> Unit
): JBScrollPane() {

    private var currentTextIndex = 0
    private var animationJob : Job? = null

    private val textPane: JTextPane = JTextPane().apply {
        border = BorderFactory.createEmptyBorder()
        val color = GameColors.black
        preferredSize = Dimension(512, 100)
        isEditable = false
        foreground = color
        margin = JBInsets(10, 15, 30, 15)
        isOpaque = false
        background = GameColors.transparent
    }

    init {
        isOpaque = false
        viewport.isOpaque = false
        border = BorderFactory.createEmptyBorder()
        setViewportView(textPane)
        viewportBorder = JBUI.Borders.emptyBottom(15)
        verticalScrollBarPolicy = VERTICAL_SCROLLBAR_NEVER
        horizontalScrollBarPolicy = HORIZONTAL_SCROLLBAR_NEVER
        mouseWheelListeners.forEach { removeMouseWheelListener(it) }
        animateText()
    }

    fun animateText() {
        if(currentTextIndex >= texts.size) { return }

        val fullText = texts[currentTextIndex]
        val doc = textPane.styledDocument
        val font = Fonts.pixelFont
        textPane.text = fullText

        val hiddenStyle = SimpleAttributeSet().apply {
            StyleConstants.setFontFamily(this, font.family)
            StyleConstants.setFontSize(this, 16)
            StyleConstants.setItalic(this, (font.style and Font.ITALIC) != 0)
            StyleConstants.setBold(this, (font.style and Font.BOLD) != 0)
            StyleConstants.setForeground(this, GameColors.transparent)
        }

        val visibleStyle = SimpleAttributeSet().apply {
            StyleConstants.setFontFamily(this, font.family)
            StyleConstants.setFontSize(this, 16)
            StyleConstants.setItalic(this, (font.style and Font.ITALIC) != 0)
            StyleConstants.setBold(this, (font.style and Font.BOLD) != 0)
            StyleConstants.setForeground(this, GameColors.black)
        }

        doc.setCharacterAttributes(0, fullText.length, hiddenStyle, true)

        animationJob = scope.launch(Dispatchers.Default) {
            var curIndex = 0
            while(curIndex < fullText.length) {
                curIndex++
                doc.setCharacterAttributes(0, curIndex, visibleStyle, true)
                textPane.caretPosition = curIndex
                repaint()
                delay(deltaTime)
            }
        }
    }

    fun nextText() {
        currentTextIndex++
        if(animationJob?.isActive == true) {
            animationJob?.cancel()
            val doc = textPane.styledDocument
            val font = Fonts.pixelFont
            val style = SimpleAttributeSet().apply {
                StyleConstants.setFontFamily(this, font.family)
                StyleConstants.setFontSize(this, 16)
                StyleConstants.setItalic(this, (font.style and Font.ITALIC) != 0)
                StyleConstants.setBold(this, (font.style and Font.BOLD) != 0)
                StyleConstants.setForeground(this, GameColors.black)
            }
            doc.setCharacterAttributes(0, textPane.text.length, style, true)
        }
        animateText()
        if(currentTextIndex >= texts.size - 1) {
            onFinished()
        }
    }

    fun stop() {
        animationJob?.cancel()
    }
}