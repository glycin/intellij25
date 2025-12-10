package com.glycin.intelli25.model

import com.glycin.intelli25.util.EnemyPNG
import com.glycin.intelli25.util.GameGlobalState
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.image.BufferedImage
import kotlin.math.roundToInt

class Enemy(
    val id: Int,
    var position: Vec2,
    val points : Int,
    val damage: Int = 1,
    val maxHp: Int = 20,
    var width: Int = 50,
    var height: Int = 50,
    private val speed: Float = 1f,
    private val image: BufferedImage?,
    private var player: Player,
    private val ggState: GameGlobalState,
) {
    var currentHp = maxHp
    private var facing = EnemyFacing.LEFT

    fun midPoint() = Vec2(position.x + (width / 2), position.y + (height / 2))

    fun move() {
        val dir = (player.midPoint() - position).normalized()
        position += dir * (speed - ggState.enemySpeedPenalty).coerceAtLeast(0.1f)

        facing = if(dir.x < 0) {
            EnemyFacing.LEFT
        } else {
            EnemyFacing.RIGHT
        }
    }

    fun draw(g: Graphics2D) {
        if(facing == EnemyFacing.LEFT) {
            g.drawImage(image, position.x.roundToInt() + width, position.y.roundToInt(), -width, height, null)
        }else{
            g.drawImage(image, position.x.roundToInt(), position.y.roundToInt(), width, height, null)
        }
    }

    fun getPickup(): Pickup {
        return Pickup(
            id = id,
            position = midPoint(),
            width = 32,
            height = 32,
            player = player,
        )
    }

    companion object{
        fun createOfType(id: Int, position: Vec2, player: Player, type: EnemyType, ggState: GameGlobalState): Enemy {
            return when(type){
                EnemyType.BUG -> Enemy(
                    id = id,
                    position = position,
                    points = 10,
                    player = player,
                    damage = 1,
                    maxHp = 10,
                    speed = 0.7f,
                    image = EnemyPNG.bug,
                    ggState = ggState
                )
                EnemyType.BLOCKER -> Enemy(
                    id = id,
                    position = position,
                    points = 20,
                    player = player,
                    damage = 2,
                    maxHp = 20,
                    speed = 1.5f,
                    image = EnemyPNG.blocker,
                    ggState = ggState
                )
                EnemyType.BURNING_CALENDAR -> Enemy(
                    id = id,
                    position = position,
                    points = 30,
                    player = player,
                    damage = 3,
                    maxHp = 50,
                    speed = 1.9f,
                    image = EnemyPNG.calendar,
                    ggState = ggState
                )
                EnemyType.PHANTOM -> Enemy(
                    id = id,
                    position = position,
                    points = 50,
                    player = player,
                    damage = 10,
                    maxHp = 100,
                    speed = 0.5f,
                    image = EnemyPNG.phantom,
                    ggState = ggState
                )
                EnemyType.DEMON -> Enemy(
                    id = id,
                    position = position,
                    points = 50,
                    player = player,
                    damage = 10,
                    maxHp = 20,
                    speed = 2.5f,
                    image = EnemyPNG.demon,
                    ggState = ggState
                )
                EnemyType.BEES -> Enemy(
                    id = id,
                    position = position,
                    points = 30,
                    player = player,
                    damage = 3,
                    maxHp = 10,
                    speed = 3f,
                    image = EnemyPNG.bees,
                    ggState = ggState
                )
                EnemyType.VAMPIRE -> Enemy(
                    id = id,
                    position = position,
                    points = 30,
                    player = player,
                    damage = 3,
                    maxHp = 10,
                    speed = 3f,
                    image = EnemyPNG.vampire,
                    ggState = ggState
                )
            }
        }

        fun getAllowedTypes(chosenLevel: Int, enemyTier: Int): List<EnemyType> {
            return EnemyType.entries.filter {
                it.minLevel <= chosenLevel && it.tier <= enemyTier
            }
        }
    }
}

enum class EnemyType(val minLevel: Int, val tier: Int) {
    BUG(1 , 1),
    BLOCKER(1, 2),
    BURNING_CALENDAR(1, 3),
    PHANTOM(2, 1),
    DEMON(2, 2),
    BEES(3, 3),
    VAMPIRE(3, 4),
}

private enum class EnemyFacing{
    LEFT,
    RIGHT,
}