package com.glycin.intelli25.model

import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.util.EnemyPNG
import com.glycin.intelli25.util.GameGlobalState
import java.awt.Graphics2D
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
    val chestDropChance: Double = 0.0,//%
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
            position = position,
            width = 32,
            height = 32,
            player = player,
            xp = points,
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
                    speed = 0.8f,
                    image = EnemyPNG.bug,
                    chestDropChance = 0.3,
                    ggState = ggState
                )
                EnemyType.BLOCKER -> Enemy(
                    id = id,
                    position = position,
                    points = 20,
                    player = player,
                    damage = 2,
                    maxHp = 20,
                    speed = 0.6f,
                    image = EnemyPNG.blocker,
                    chestDropChance = 0.5,
                    ggState = ggState
                )
                EnemyType.BURNING_CALENDAR -> Enemy(
                    id = id,
                    position = position,
                    points = 30,
                    player = player,
                    damage = 3,
                    maxHp = 30,
                    speed = 1.2f,
                    image = EnemyPNG.calendar,
                    chestDropChance = 0.5,
                    ggState = ggState
                )
                EnemyType.PHANTOM -> Enemy(
                    id = id,
                    position = position,
                    points = 40,
                    player = player,
                    damage = 5,
                    maxHp = 40,
                    speed = 1f,
                    image = EnemyPNG.phantom,
                    chestDropChance = 0.6,
                    ggState = ggState
                )
                EnemyType.DEMON -> Enemy(
                    id = id,
                    position = position,
                    points = 50,
                    player = player,
                    damage = 10,
                    maxHp = 50,
                    speed = 0.3f,
                    image = EnemyPNG.demon,
                    chestDropChance = 0.6,
                    ggState = ggState
                )
                EnemyType.BEES -> Enemy(
                    id = id,
                    position = position,
                    points = 60,
                    player = player,
                    damage = 2,
                    maxHp = 10,
                    speed = 3f,
                    image = EnemyPNG.bees,
                    chestDropChance = 0.8,
                    ggState = ggState
                )
                EnemyType.VAMPIRE -> Enemy(
                    id = id,
                    position = position,
                    points = 100,
                    player = player,
                    damage = 20,
                    maxHp = 200,
                    speed = 2f,
                    image = EnemyPNG.vampire,
                    chestDropChance = 1.0,
                    ggState = ggState
                )
            }
        }

        fun getAllowedTypes(chosenLevel: Int, enemyTier: Int): List<EnemyType> {
            return EnemyType.entries.filter {
                it.minLevel <= chosenLevel && it.tier <= enemyTier
            }
        }

        fun getAllAsEntries(saveState: GameSaveState): List<EnemyEntry> {
            val enemiesSeen = saveState.enemiesSeen.split(",")
            return EnemyType.entries.map { it.toEntry(enemiesSeen) }
        }

        private fun EnemyType.toEntry(enemiesSeen: List<String>): EnemyEntry {
            return when(this){
                EnemyType.BUG -> EnemyEntry(
                    name = "BUG",
                    description = "Slow and predictable, but able to overwhelm you!",
                    image = EnemyPNG.bug,
                    seen = enemiesSeen.contains("BUG")
                )
                EnemyType.BLOCKER -> EnemyEntry(
                    name = "BLOCKER",
                    description = "Harder to remove than bugs, but seen from miles away!",
                    image = EnemyPNG.blocker,
                    seen = enemiesSeen.contains("BLOCKER")
                )
                EnemyType.BURNING_CALENDAR -> EnemyEntry(
                    name = "DEADLINE",
                    description = "It shows up before you notice it, and always hit hard.",
                    image = EnemyPNG.calendar,
                    seen = enemiesSeen.contains("BURNING_CALENDAR")
                )
                EnemyType.PHANTOM -> EnemyEntry(
                    name = "BURNOUT PHANTOM",
                    description = "It creeps up slowly. Once it hits, it’s quickly game over.",
                    image = EnemyPNG.phantom,
                    seen = enemiesSeen.contains("PHANTOM")
                )
                EnemyType.DEMON -> EnemyEntry(
                    name = "DOOMSCROLL DEMON",
                    description = "It feels harmless, but it keeps you scrolling and hits hard in the end.",
                    image = EnemyPNG.demon,
                    seen = enemiesSeen.contains("DEMON")
                )
                EnemyType.BEES -> EnemyEntry(
                    name = "Meeting Bees",
                    description = "Buzz buzz, and your day is gone. Fast and deadly.",
                    image = EnemyPNG.bees,
                    seen = enemiesSeen.contains("BEES")
                )
                EnemyType.VAMPIRE -> EnemyEntry(
                    name = "Legacy Vampire",
                    description = "Nothing drains productivity faster than legacy code. Hard to remove, deals heavy damage.",
                    image = EnemyPNG.vampire,
                    seen = enemiesSeen.contains("VAMPIRE")
                )
            }
        }
    }
}

enum class EnemyType(val minLevel: Int, val tier: Int) {
    BUG(1 , 1),
    BLOCKER(1, 2),
    BURNING_CALENDAR(2, 2),
    PHANTOM(2, 3),
    DEMON(2, 3),
    BEES(3, 3),
    VAMPIRE(3, 4),
}

private enum class EnemyFacing{
    LEFT,
    RIGHT,
}