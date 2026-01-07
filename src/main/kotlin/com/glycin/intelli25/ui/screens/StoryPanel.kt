package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.model.StoryReplay
import com.glycin.intelli25.ui.StartButton
import com.glycin.intelli25.util.GameColors
import java.awt.BorderLayout
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.*

class StoryPanel(
    stories: List<StoryReplay>
) : JPanel(BorderLayout()) {

    init {
        isOpaque = false

        val buttonColumn = JPanel().apply {
            isOpaque = false
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = CENTER_ALIGNMENT
        }

        stories.forEachIndexed { index, story ->
            val button = StartButton(
                backgroundColor = GameColors.jbPurple,
                hoverColor = GameColors.jbOrange,
                textColor = GameColors.white,
                text = story.label,
                filled = story.unlocked,
                preferredWidth = 250,
                preferredHeight = 64
            ).apply {
                alignmentX = CENTER_ALIGNMENT
                isEnabled = story.unlocked

                addActionListener {
                    if (isEnabled) {
                        story.onOpen()
                    }
                }
            }

            if (index > 0) {
                buttonColumn.add(Box.createVerticalStrut(12))
            }
            buttonColumn.add(button)
        }

        val wrapper = JPanel().apply {
            isOpaque = false
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(Box.createVerticalGlue())
            add(buttonColumn)
            add(Box.createVerticalGlue())
        }

        add(wrapper, BorderLayout.CENTER)
    }

    override fun paintComponent(g: Graphics?) {
        if(g is Graphics2D) {
            g.color = GameColors.black
            g.fillRect(0, 0, width, height)
        }
        super.paintComponent(g)
    }
}
