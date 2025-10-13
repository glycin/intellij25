package com.glycin.intelli25.ideaevolved

import com.glycin.intelli25.shared.Vec2
import java.awt.Graphics2D
import kotlin.math.roundToInt

class Bullet(
    var position: Vec2,
    var direction: Vec2,
    val radius: Int = 25,
    val speed: Float = 5.0f,
) {

    fun move() {
        position += direction * speed
        println(position)
    }

    fun draw(g: Graphics2D) {
        g.fillOval(position.x.roundToInt(), position.y.roundToInt(), radius, radius)
    }
}