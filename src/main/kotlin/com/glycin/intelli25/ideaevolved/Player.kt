package com.glycin.intelli25.ideaevolved

import com.glycin.intelli25.shared.Vec2
import com.intellij.ui.JBColor
import java.awt.Graphics2D
import kotlin.math.roundToInt

class Player(
    private var position: Vec2,
    private val width: Int,
    private val height: Int,
) {
    private fun midPoint() = Vec2(position.x + (width / 2), position.y + (height / 2))

    fun draw(g: Graphics2D) {
        g.color = JBColor.BLUE
        g.fillRect(position.x.roundToInt(), position.y.roundToInt(), width, height)
    }
}