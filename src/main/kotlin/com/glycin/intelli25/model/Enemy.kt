package com.glycin.intelli25.model

import com.glycin.intelli25.shared.Vec2
import java.awt.Graphics2D
import java.awt.Rectangle
import kotlin.math.roundToInt
import kotlin.random.Random

class Enemy(
    var id: Int,
    var position: Vec2,
    private var player: Player,
    private var width: Int,
    private var height: Int,
    private val speed: Float = Random.Default.nextDouble(0.5, 5.0).toFloat(),
) {
    fun midPoint() = Vec2(position.x + (width / 2), position.y + (height / 2))

    fun rect() = Rectangle(position.x.roundToInt(), position.y.roundToInt(), width, height)

    fun move() {
        val dir = (player.midPoint() - position).normalized()
        position += dir * speed
    }

    fun draw(g: Graphics2D) {
        g.fillRect(position.x.roundToInt(), position.y.roundToInt(), width, height)
    }
}