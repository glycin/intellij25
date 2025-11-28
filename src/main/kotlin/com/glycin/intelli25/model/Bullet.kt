package com.glycin.intelli25.model

import com.glycin.intelli25.util.PNG
import java.awt.Graphics2D
import java.awt.Rectangle
import kotlin.math.atan2
import kotlin.math.roundToInt

class Bullet(
    val id: Long,
    var position: Vec2,
    var direction: Vec2,
    val damage: Int,
    val radius: Int = 15,
    val speed: Float = 10.0f,
) {
    val innerRadius: Int = radius - 5

    fun rect() = Rectangle(position.x.roundToInt(), position.y.roundToInt(), innerRadius, innerRadius)
    fun midPoint() = Vec2(position.x + (radius / 2), position.y + (radius / 2))

    fun move() {
        position += direction * speed
    }

    fun draw(g: Graphics2D) {
        val cx = position.x + radius / 2f
        val cy = position.y + radius / 2f
        val angle = atan2(direction.y, direction.x).toDouble()
        val oldTransform = g.transform
        g.rotate(angle, cx.toDouble(), cy.toDouble())
        g.drawImage(
            PNG.BULLET,
            (cx - radius / 2f).roundToInt(),
            (cy - radius / 2f).roundToInt(),
            radius,
            radius,
            null
        )
        g.transform = oldTransform
    }
}