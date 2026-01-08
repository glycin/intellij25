package com.glycin.intelli25.model

import com.glycin.intelli25.util.GameGlobalState
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import kotlin.math.roundToInt

class Player(
    var position: Vec2,
    var speed: Float = 2.5f,
    val width: Int,
    val height: Int,
    private val ggState: GameGlobalState,
    private val onLevelUp: () -> Unit,
    private val onDeath: () -> Unit,
) {
    val playHitDistSq = (width * width)
    val chestPickupRangeSq = (50 * 50)
    val pickUpRange: Float = 50.0f

    var experience = 0.0f
    var experienceNeeded = 10
    var level = 1

    val keyMap = mutableMapOf(
        "UP" to false,
        "LEFT" to false,
        "DOWN" to false,
        "RIGHT" to false,
    )

    val upgrades = mutableMapOf<String, UpgradeBackpackItem>()

    private val baseMaxHp = 300
    var currentHp = baseMaxHp

    private val baseRegenRate = 5
    private val animator = PlayerAnimator()
    private var state = PlayerState.IDLE
    private var facing = PlayerFacing.LEFT

    fun midPoint() = Vec2(position.x + (width / 2), position.y + (height / 2))

    fun update() {
        if(ggState.inUpgradeMenu) return

        var dir = Vec2.zero

        if (keyMap["UP"] == true && position.y > ggState.minY){
            dir += Vec2.up
        }

        if (keyMap["LEFT"] == true && position.x > ggState.minX) {
            dir += Vec2.left
        }

        if (keyMap["DOWN"] == true && (position.y + height) <= ggState.maxY) {
            dir += Vec2.down
        }

        if (keyMap["RIGHT"] == true && (position.x + width) <= ggState.maxX) {
            dir += Vec2.right
        }

        if(dir.x < 0) {
            facing = PlayerFacing.LEFT
        } else if (dir.x > 0) {
            facing = PlayerFacing.RIGHT
        }

        if(state != PlayerState.HURT) {
            if(dir != Vec2.zero) {
                if(state != PlayerState.WALK) { state = PlayerState.WALK }
            } else {
                state = PlayerState.IDLE
            }
        }

        animator.animate(state)
        position += dir * (speed * ggState.speedMultiplier)
    }

    fun regenerate() {
        val newHp = currentHp + (baseRegenRate * ggState.regenRatePerSecondMultiplier)
        currentHp = newHp.coerceIn(0, maxHp())
    }

    fun draw(g: Graphics2D) {
        val currentSprite = animator.currentSprite

        if(facing == PlayerFacing.LEFT) {
            g.drawImage(currentSprite, position.x.roundToInt() + width, position.y.roundToInt(), -width, height, null)
        }else{
            g.drawImage(currentSprite, position.x.roundToInt(), position.y.roundToInt(), width, height, null)
        }
    }

    fun addExp(xpCount: Int) {
        experience += xpCount
        if(experience >= experienceNeeded) {
            experience -= experienceNeeded
            experienceNeeded = (experienceNeeded * 1.7).roundToInt()
            ggState.inUpgradeMenu = true
            onLevelUp.invoke()
        }
    }

    fun hurt(damage: Int) {
        currentHp -= damage
        state = PlayerState.HURT
        if(currentHp <= 0) {
            onDeath.invoke()
        }
    }

    fun unhurt() {
        if(state == PlayerState.HURT) {
            state = PlayerState.IDLE
        }
    }

    fun levelUp() {
        ggState.inUpgradeMenu = true
        onLevelUp()
    }

    fun maxHp() = baseMaxHp * ggState.healthMultiplier
}