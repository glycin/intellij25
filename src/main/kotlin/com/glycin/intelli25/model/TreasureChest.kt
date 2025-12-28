package com.glycin.intelli25.model

import com.glycin.intelli25.util.PNG
import java.awt.Graphics2D
import kotlin.math.roundToInt

class TreasureChest(
    val id: Int,
    var position: Vec2,
    private val width: Int = 32,
    private val height: Int = 32,
) {
    fun midPoint() = Vec2(position.x + (width / 2), position.y + (height / 2))

    fun draw(g: Graphics2D) {
        g.drawImage(PNG.CHEST, position.x.roundToInt(), position.y.roundToInt(), width, height, null)
    }
}