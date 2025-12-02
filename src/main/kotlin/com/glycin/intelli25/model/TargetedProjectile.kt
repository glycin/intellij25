package com.glycin.intelli25.model

import com.glycin.intelli25.util.GameColors
import java.awt.Graphics2D
import kotlin.math.roundToInt

class TargetedProjectile(
    val id: Long,
    var position: Vec2,
    val damage: Int,
    val radius:Int = 15,
    val target: Enemy,
    val speed: Float = 10.0f,
) {
    fun midPoint() = Vec2(position.x + (radius / 2), position.y + (radius / 2))

    fun move() {
        val direction = (target.midPoint() - midPoint()).normalized()
        position += direction * speed
    }

    fun draw(g: Graphics2D) {
        g.color = GameColors.jbRed
        g.fillOval(position.x.roundToInt(), position.y.roundToInt(), radius, radius)
    }
}