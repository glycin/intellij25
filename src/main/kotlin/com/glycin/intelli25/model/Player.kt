package com.glycin.intelli25.model

import java.awt.Graphics2D
import java.awt.Rectangle
import kotlin.math.roundToInt

class Player(
    var position: Vec2,
    var direction: Vec2 = Vec2.zero,
    var speed: Float = 10.0f,
    private val width: Int,
    private val height: Int,
) {
    private val animator = PlayerAnimator()

    private var state = PlayerState.IDLE
    private var facing = PlayerFacing.LEFT
    private var moving = false

    fun midPoint() = Vec2(position.x + (width / 2), position.y + (height / 2))

    fun rect() : Rectangle = Rectangle(position.x.roundToInt(), position.y.roundToInt(), width, height)

    fun update() {
        animator.animate(state)

        if(moving) {
            position += direction * speed
        }
    }

    fun draw(g: Graphics2D) {
        val currentSprite = animator.getCurrentSprite()

        if(facing == PlayerFacing.LEFT) {
            g.drawImage(currentSprite, position.x.roundToInt() + width, position.y.roundToInt(), -width, height, null)
        }else{
            g.drawImage(currentSprite, position.x.roundToInt(), position.y.roundToInt(), width, height, null)
        }
    }

    fun movePlayer(dir: Vec2) {
        moving = true
        if(state != PlayerState.WALK) { state = PlayerState.WALK }
        if(dir.x < 0) {
            facing = PlayerFacing.LEFT
        } else if (dir.x > 0) {
            facing = PlayerFacing.RIGHT
        }

        position += dir * speed
    }

    fun stopMoving() {
        state = PlayerState.IDLE
        moving = false
    }
}