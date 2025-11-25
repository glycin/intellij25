package com.glycin.intelli25.model

import com.glycin.intelli25.ui.PNG
import java.awt.Graphics2D
import kotlin.math.roundToInt

class Pickup(
    val id: Int,
    var position: Vec2,
    val xp: Int = 5,
    private val width: Int,
    private val height: Int,
    private val player: Player,
) {
    var picked: Boolean = false
    private val speed = 4.0f

    fun midPoint() = Vec2(position.x + (width / 2), position.y + (height / 2))

    fun move() {
        if(picked) {
            position += (player.midPoint() - midPoint()).normalized() * speed
        }
    }

    fun draw(g: Graphics2D) {
        g.drawImage(PNG.GEM, position.x.roundToInt(), position.y.roundToInt(), width, height, null)
    }
}