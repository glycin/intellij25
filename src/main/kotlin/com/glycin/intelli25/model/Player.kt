package com.glycin.intelli25.model

import java.awt.Graphics2D
import java.awt.Rectangle
import kotlin.math.roundToInt

class Player(
    var position: Vec2,
    private val width: Int,
    private val height: Int,
) {
    private var state = PlayerState.IDLE
    private var facing = PlayerFacing.LEFT
    private val animator = PlayerAnimator()

    fun midPoint() = Vec2(position.x + (width / 2), position.y + (height / 2))

    fun rect() : Rectangle = Rectangle(position.x.roundToInt(), position.y.roundToInt(), width, height)

    fun update() {
        animator.animate(state)
    }

    fun draw(g: Graphics2D) {
        val currentSprite = animator.getCurrentSprite()

        if(facing == PlayerFacing.LEFT) {
            g.drawImage(currentSprite, position.x.roundToInt() + width, position.y.roundToInt(), -width, height, null)
        }else{
            g.drawImage(currentSprite, position.x.roundToInt(), position.y.roundToInt(), width, height, null)
        }
    }
}