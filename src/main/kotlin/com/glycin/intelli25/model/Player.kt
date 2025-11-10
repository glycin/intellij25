package com.glycin.intelli25.model

import com.glycin.intelli25.util.GameGlobalState
import java.awt.Graphics2D
import java.awt.Rectangle
import kotlin.math.roundToInt

class Player(
    var position: Vec2,
    var direction: Vec2 = Vec2.zero,
    var speed: Float = 10.0f,
    val width: Int,
    val height: Int,
    private val ggState: GameGlobalState,
    private val onLevelUp: () -> Unit,
) {
    var experience = 0.0f
    var experienceNeeded = 10
    var pickUpRange: Float = 50.0f
    var level = 1
    var currentHp = 100

    private val baseMaxHp = 100
    private val baseRegenRate = 1
    private val animator = PlayerAnimator()
    private var state = PlayerState.IDLE
    private var facing = PlayerFacing.LEFT
    private var moving = false

    fun midPoint() = Vec2(position.x + (width / 2), position.y + (height / 2))

    fun rect() : Rectangle = Rectangle(position.x.roundToInt(), position.y.roundToInt(), width, height)

    fun update() {
        animator.animate(state)

        if(moving) {
            position += direction * (speed * ggState.speedMultiplier)
        }
    }

    fun regenerate() {
        val newHp = currentHp + (baseRegenRate * ggState.regenRatePerSecondMultiplier)
        currentHp = newHp.coerceIn(0, maxHp())
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
        if(ggState.inUpgradeMenu) return

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

    fun addExp(xpCount: Int) {
        experience += xpCount
        if(experience >= experienceNeeded) {
            experience -= experienceNeeded
            experienceNeeded *= 2 // TODO: Make the scaling better
            ggState.inUpgradeMenu = true
            onLevelUp.invoke()
        }
    }

    fun hurt(damage: Int) {
        currentHp -= damage
    }

    fun maxHp() = baseMaxHp * ggState.healthMultiplier
}