package com.glycin.intelli25.model

import com.glycin.intelli25.util.toPoint
import java.awt.Graphics2D
import java.awt.Rectangle
import kotlin.math.roundToInt

class Bullet(
    val id: Long,
    var position: Vec2,
    var direction: Vec2,
    val radius: Int = 15,
    val speed: Float = 10.0f,
) {
    val innerRadius: Int = radius - 5

    fun rect() = Rectangle(position.x.roundToInt(), position.y.roundToInt(), innerRadius, innerRadius)
    fun middleAsPoint() = Vec2(position.x + (radius / 2), position.y + (radius / 2)).toPoint()

    fun move() {
        position += direction * speed
    }

    fun draw(g: Graphics2D) {
        g.fillOval(position.x.roundToInt(), position.y.roundToInt(), radius, radius)
    }
}