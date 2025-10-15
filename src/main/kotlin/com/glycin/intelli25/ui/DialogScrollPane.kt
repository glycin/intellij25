package com.glycin.intelli25.ui

import com.glycin.intelli25.shared.GameColors
import com.intellij.openapi.application.EDT
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.ui.JBUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Font
import java.awt.Insets
import javax.swing.JTextPane
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants

class DialogScrollPane(
    private val texts: List<String>,
    private val scope: CoroutineScope,
    private val deltaTime: Long,
): JBScrollPane() {

    private var active = false
    private var currentTextIndex = 0

    private val textPane: JTextPane = JTextPane().apply {
        val color = GameColors.black
        isEditable = false
        foreground = color
        margin = Insets(10, 15, 30, 15)
        val doc = styledDocument
        val font = Fonts.pixelFont
        val style = SimpleAttributeSet().apply {
            StyleConstants.setFontFamily(inputAttributes, font.family)
            StyleConstants.setFontSize(inputAttributes, 32)
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
        setViewportView(textPane)
        viewportBorder = JBUI.Borders.empty(0, 0, 15, 0)
        verticalScrollBarPolicy = VERTICAL_SCROLLBAR_AS_NEEDED
        horizontalScrollBarPolicy = HORIZONTAL_SCROLLBAR_NEVER
        animateText()
    }

    fun animateText() {
        if(currentTextIndex >= texts.size) { return }
        active = true
        scope.launch (Dispatchers.EDT) {
            var curIndex = 0
            while(curIndex < texts[currentTextIndex].length && active) {
                textPane.text = texts[currentTextIndex].take(++curIndex)
                textPane.caretPosition = textPane.text.length
                repaint()
                delay(deltaTime)
            }
            active = false
        }
    }

    fun nextText() {
        currentTextIndex++
        animateText()
    }

    fun stop() {
        active = false
    }
}