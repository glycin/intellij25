package com.glycin.intelli25.ui

import com.glycin.intelli25.util.GameColors
import com.intellij.openapi.application.EDT
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.JBUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Dimension
import java.awt.Font
import java.awt.Insets
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
        margin = Insets(10, 15, 30, 15)
        val doc = styledDocument
        val font = Fonts.pixelFont
        val style = SimpleAttributeSet().apply {
            StyleConstants.setFontFamily(inputAttributes, font.family)
            StyleConstants.setFontSize(inputAttributes, 16)
            StyleConstants.setItalic(inputAttributes, (font.style and Font.ITALIC) != 0)
            StyleConstants.setBold(inputAttributes, (font.style and Font.BOLD) != 0)
            StyleConstants.setForeground(inputAttributes, color)
        }

        doc.setCharacterAttributes(0, doc.length, style, false)
    }

    init {
        isOpaque = false
        viewport.isOpaque = false
        textPane.isOpaque = false
        textPane.background = GameColors.transparent
        border = BorderFactory.createEmptyBorder()
        setViewportView(textPane)
        viewportBorder = JBUI.Borders.empty(0, 0, 15, 0)
        verticalScrollBarPolicy = VERTICAL_SCROLLBAR_NEVER
        horizontalScrollBarPolicy = HORIZONTAL_SCROLLBAR_NEVER
        mouseWheelListeners.forEach { removeMouseWheelListener(it) }
        animateText()
    }

    fun animateText() {
        if(currentTextIndex >= texts.size) { return }
        animationJob = scope.launch (Dispatchers.Default) {
            var curIndex = 0
            while(curIndex < texts[currentTextIndex].length) {
                textPane.text = texts[currentTextIndex].take(++curIndex)
                textPane.caretPosition = textPane.text.length
                repaint()
                delay(deltaTime)
            }
        }
    }

    fun nextText() {
        currentTextIndex++
        if(animationJob?.isActive == true) {
            animationJob?.cancel()
            textPane.text = texts[currentTextIndex]
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