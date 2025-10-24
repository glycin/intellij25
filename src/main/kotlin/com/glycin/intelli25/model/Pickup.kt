package com.glycin.intelli25.model

import java.awt.Graphics2D
import kotlin.math.roundToInt

class Pickup(
    val id: Int,
    var position: Vec2,
    val width: Int,
    val height: Int,
) {
    fun midPoint() = Vec2(position.x + (width / 2), position.y + (height / 2))

    fun draw(g: Graphics2D) {
        g.fillOval(position.x.roundToInt(), position.y.roundToInt(), width, height)
    }
}