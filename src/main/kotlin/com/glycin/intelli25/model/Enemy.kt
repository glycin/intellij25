package com.glycin.intelli25.model

import java.awt.Graphics2D
import java.awt.Rectangle
import kotlin.math.roundToInt

class Enemy(
    var id: Int,
    var position: Vec2,
    private var player: Player,
    private var width: Int,
    private var height: Int,
    private val speed: Float = 1f,
) {
    var damage = 1
    var maxHp = 10
    var currentHp = maxHp

    fun midPoint() = Vec2(position.x + (width / 2), position.y + (height / 2))

    fun rect() = Rectangle(position.x.roundToInt(), position.y.roundToInt(), width, height)

    fun move() {
        val dir = (player.midPoint() - position).normalized()
        position += dir * speed
    }

    fun draw(g: Graphics2D) {
        g.fillRect(position.x.roundToInt(), position.y.roundToInt(), width, height)
    }

    fun getPickup(): Pickup {
        return Pickup(
            id = id,
            position = midPoint(),
            width = 10,
            height = 15,
        )
    }
}