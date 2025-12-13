package com.glycin.intelli25.model

import com.glycin.intelli25.util.PNG
import java.awt.Graphics2D
import kotlin.math.atan2
import kotlin.math.roundToInt

class TargetedProjectile(
    val id: Long,
    var position: Vec2,
    val damage: Int,
    val radius:Int = 15,
    val width: Int = 19,
    val height: Int = 32,
    val target: Enemy,
    val speed: Float = 5.0f,
) {
    var angle = 0.0
    fun midPoint() = Vec2(position.x + (radius / 2), position.y + (radius / 2))

    fun move() {
        val direction = (target.midPoint() - midPoint()).normalized()
        angle = atan2(direction.y, direction.x).toDouble() + Math.PI / 2
        position += direction * speed
    }

    fun draw(g: Graphics2D) {
        val cx = position.x + width / 2f
        val cy = position.y + height / 2f
        val oldTransform = g.transform
        g.rotate(angle, cx.toDouble(), cy.toDouble())
        g.drawImage(
            PNG.FIREWORK_MISSILE,
            (cx - width / 2f).roundToInt(),
            (cy - height / 2f).roundToInt(),
            width,
            height,
            null
        )
        g.transform = oldTransform
    }
}