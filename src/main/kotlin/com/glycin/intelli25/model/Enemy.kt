package com.glycin.intelli25.model

import com.glycin.intelli25.util.EnemyPNG
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.image.BufferedImage
import kotlin.math.roundToInt

class Enemy(
    val id: Int,
    var position: Vec2,
    val points : Int,
    val type: EnemyType = EnemyType.entries.random(),
    val damage: Int = 1,
    val maxHp: Int = 20,
    private var player: Player,
    private var width: Int = 50,
    private var height: Int = 50,
    private val speed: Float = 1f,
    private val image: BufferedImage?,
) {
    var currentHp = maxHp
    private var facing = EnemyFacing.LEFT

    fun midPoint() = Vec2(position.x + (width / 2), position.y + (height / 2))

    fun rect() = Rectangle(position.x.roundToInt(), position.y.roundToInt(), width, height)

    fun move() {
        val dir = (player.midPoint() - position).normalized()
        position += dir * speed

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
            width = 10,
            height = 15,
            player = player,
        )
    }

    companion object{
        fun createOfType(id: Int, position: Vec2, player: Player, type: EnemyType): Enemy {
            return when(type){
                EnemyType.BUG -> Enemy(
                    id = id,
                    position = position,
                    points = 10,
                    type = type,
                    player = player,
                    damage = 1,
                    maxHp = 20,
                    speed = 1f,
                    image = EnemyPNG.bug,
                )
                EnemyType.BLOCKER -> Enemy(
                    id = id,
                    position = position,
                    points = 20,
                    type = type,
                    player = player,
                    damage = 2,
                    maxHp = 40,
                    speed = 1.5f,
                    image = EnemyPNG.blocker,
                )
                EnemyType.BURNING_CALENDAR -> Enemy(
                    id = id,
                    position = position,
                    points = 30,
                    type = type,
                    player = player,
                    damage = 3,
                    maxHp = 50,
                    speed = 1.9f,
                    image = EnemyPNG.calendar,
                )
                EnemyType.PHANTOM -> Enemy(
                    id = id,
                    position = position,
                    points = 50,
                    type = type,
                    player = player,
                    damage = 10,
                    maxHp = 100,
                    speed = 0.2f,
                    image = EnemyPNG.phantom,
                )
                EnemyType.DEMON -> Enemy(
                    id = id,
                    position = position,
                    points = 50,
                    type = type,
                    player = player,
                    damage = 10,
                    maxHp = 20,
                    speed = 2.5f,
                    image = EnemyPNG.demon,
                )
                EnemyType.BEES -> Enemy(
                    id = id,
                    position = position,
                    points = 30,
                    type = type,
                    player = player,
                    damage = 3,
                    maxHp = 10,
                    speed = 3f,
                    image = EnemyPNG.bees,
                )
                EnemyType.VAMPIRE -> Enemy(
                    id = id,
                    position = position,
                    points = 30,
                    type = type,
                    player = player,
                    damage = 3,
                    maxHp = 10,
                    speed = 3f,
                    image = EnemyPNG.vampire,
                )
            }
        }
        fun getAllowedTypes(enemyTier: Int): List<EnemyType> {
            return EnemyType.entries.filter { it.tier <= enemyTier }
        }
    }
}

enum class EnemyType(val tier: Int) {
    BUG(1),
    BLOCKER(1),
    BURNING_CALENDAR(2),
    PHANTOM(2),
    DEMON(3),
    BEES(3),
    VAMPIRE(3),
}

private enum class EnemyFacing{
    LEFT,
    RIGHT,
}