package com.glycin.intelli25.model

import com.intellij.ui.JBColor
import java.awt.Graphics2D
import java.awt.Rectangle
import kotlin.math.roundToInt

class Enemy(
    val id: Int,
    var position: Vec2,
    val points : Int,
    val type: EnemyType = EnemyType.entries.random(),
    val damage: Int = 1,
    val maxHp: Int = 20,
    private var player: Player,
    private var width: Int,
    private var height: Int,
    private val speed: Float = 1f,
) {
    var currentHp = maxHp

    fun midPoint() = Vec2(position.x + (width / 2), position.y + (height / 2))

    fun rect() = Rectangle(position.x.roundToInt(), position.y.roundToInt(), width, height)

    fun move() {
        val dir = (player.midPoint() - position).normalized()
        position += dir * speed
    }

    fun draw(g: Graphics2D) {
        when (type) {
            EnemyType.SNAIL -> g.color = JBColor.yellow
            EnemyType.MOSQUITO -> g.color = JBColor.pink
            EnemyType.LARVA -> g.color = JBColor.blue
            EnemyType.LADYBUG -> g.color = JBColor.green
            EnemyType.SPIDER -> g.color = JBColor.white
            EnemyType.BEE -> g.color = JBColor.red
            EnemyType.WASP -> g.color = JBColor.CYAN
            EnemyType.BEATLE -> g.color = JBColor.MAGENTA
            EnemyType.ANT -> g.color = JBColor.orange
            EnemyType.MANTIS -> g.color = JBColor.darkGray
        }
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

    companion object{
        fun createOfType(id: Int, position: Vec2, player: Player, type: EnemyType): Enemy {
            return when(type){
                EnemyType.SNAIL -> Enemy(
                    id = id,
                    position = position,
                    points = 10,
                    type = type,
                    player = player,
                    width = 15,
                    height = 15,
                    damage = 1,
                    maxHp = 20,
                    speed = 1f,
                )
                EnemyType.MOSQUITO -> Enemy(
                    id = id,
                    position = position,
                    points = 20,
                    type = type,
                    player = player,
                    width = 10,
                    height = 10,
                    damage = 2,
                    maxHp = 40,
                    speed = 1.5f,
                )
                EnemyType.LARVA -> Enemy(
                    id = id,
                    position = position,
                    points = 30,
                    type = type,
                    player = player,
                    width = 12,
                    height = 12,
                    damage = 3,
                    maxHp = 50,
                    speed = 1.9f,
                )
                EnemyType.LADYBUG -> Enemy(
                    id = id,
                    position = position,
                    points = 50,
                    type = type,
                    player = player,
                    width = 15,
                    height = 15,
                    damage = 10,
                    maxHp = 100,
                    speed = 0.2f,
                )
                EnemyType.SPIDER -> Enemy(
                    id = id,
                    position = position,
                    points = 50,
                    type = type,
                    player = player,
                    width = 18,
                    height = 18,
                    damage = 10,
                    maxHp = 20,
                    speed = 2.5f,
                )
                EnemyType.BEE -> Enemy(
                    id = id,
                    position = position,
                    points = 30,
                    type = type,
                    player = player,
                    width = 5,
                    height = 5,
                    damage = 3,
                    maxHp = 10,
                    speed = 3f,
                )
                EnemyType.WASP -> Enemy(
                    id = id,
                    position = position,
                    points = 30,
                    type = type,
                    player = player,
                    width = 7,
                    height = 7,
                    damage = 3,
                    maxHp = 10,
                    speed = 3f,
                )
                EnemyType.BEATLE -> Enemy(
                    id = id,
                    position = position,
                    points = 80,
                    type = type,
                    player = player,
                    width = 20,
                    height = 20,
                    damage = 20,
                    maxHp = 250,
                    speed = 2f,
                )
                EnemyType.ANT -> Enemy(
                    id = id,
                    position = position,
                    points = 80,
                    type = type,
                    player = player,
                    width = 15,
                    height = 15,
                    damage = 20,
                    maxHp = 250,
                    speed = 2f,
                )
                EnemyType.MANTIS -> Enemy(
                    id = id,
                    position = position,
                    points = 200,
                    type = type,
                    player = player,
                    width = 25,
                    height = 25,
                    damage = 30,
                    maxHp = 500,
                    speed = 1.5f,
                )
            }
        }
        fun getAllowedTypes(enemyTier: Int): List<EnemyType> {
            return EnemyType.entries.filter { it.tier <= enemyTier }
        }
    }
}

enum class EnemyType(val tier: Int) {
    SNAIL(1),
    MOSQUITO(2),
    LARVA(2),
    LADYBUG(3),
    SPIDER(3),
    BEE(4),
    WASP(4),
    BEATLE(5),
    ANT(5),
    MANTIS(6),
}