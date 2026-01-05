package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Bullet
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.model.upgradeOptionFromAttackDef
import com.glycin.intelli25.util.GameGlobalState
import com.jetbrains.rd.util.concurrentMapOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D

class BasicAttack(
    ggState: GameGlobalState,
    player: Player,
    private val scope: CoroutineScope,
) : Attack(ggState, player, AttackConfig.PRODUCTIVITY_FEATURE) {

    private var nextId = 0L
    private val bullets = concurrentMapOf<Long, Bullet>()
    private var attackDelay: Long = 1000L //ms
    private val basicAttackDamage: Int = 10
    private var invincibleBullet = false

    private val upgradeOne = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[0]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            attackDelay = 750L
            generalLevelUp()
        }
    }

    private val upgradeTwo = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[1]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            generalLevelUp()
        }
    }

    private val upgradeThree = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[2]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            attackDelay = 500L
            generalLevelUp()
        }
    }

    private val upgradeFour = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[3]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            generalLevelUp()
        }
    }

    private val upgradeFive = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[4]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            attackDelay = 250L
            generalLevelUp()
        }
    }

    private val upgradeSix = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[5]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            generalLevelUp()
        }
    }

    private val upgradeSeven = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[6]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            attackDelay = 100L
            generalLevelUp()
        }
    }

    private val upgradeEight = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[7]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            invincibleBullet = true
            generalLevelUp()
        }
    }

    private val upgrades = when(ggState.chosenGameLevel) {
        1 -> listOf(upgradeOne, upgradeTwo)
        2 -> listOf(upgradeOne, upgradeTwo, upgradeThree, upgradeFour, upgradeFive, upgradeSix)
        else -> listOf(upgradeOne, upgradeTwo, upgradeThree, upgradeFour, upgradeFive, upgradeSix, upgradeSeven, upgradeEight)
    }

    override val maxLevel: Int = upgrades.size + 1

    override fun activate() {
        scope.launch(Dispatchers.Default) {
            while (ggState.gameActive) {
                if(!ggState.inUpgradeMenu) {
                    when(currentLevel) {
                        1, 2 -> {
                            addBullet(player.midPoint(), Vec2.left)
                            addBullet(player.midPoint(), Vec2.right)
                        }
                        3, 4 -> {
                            addBullet(player.midPoint(), Vec2(1.0f, -1.0f))
                            addBullet(player.midPoint(), Vec2(-1.0f, 1.0f))
                            addBullet(player.midPoint(), Vec2.right)
                            addBullet(player.midPoint(), Vec2.left)
                        }
                        5, 6 -> {
                            addBullet(player.midPoint(), Vec2(1.0f, -1.0f))
                            addBullet(player.midPoint(), Vec2.right)
                            addBullet(player.midPoint(), Vec2(1.0f, 1.0f))

                            addBullet(player.midPoint(), Vec2(-1.0f, -1.0f))
                            addBullet(player.midPoint(), Vec2.left)
                            addBullet(player.midPoint(), Vec2(-1.0f, 1.0f))
                        }
                        7, 8, 9 -> {
                            addBullet(player.midPoint(), Vec2(1.0f, -1.0f))
                            addBullet(player.midPoint(), Vec2.right)
                            addBullet(player.midPoint(), Vec2(1.0f, 1.0f))
                            addBullet(player.midPoint(), Vec2.up)

                            addBullet(player.midPoint(), Vec2(-1.0f, -1.0f))
                            addBullet(player.midPoint(), Vec2.left)
                            addBullet(player.midPoint(), Vec2(-1.0f, 1.0f))
                            addBullet(player.midPoint(), Vec2.down)
                        }
                    }
                }
                delay(attackDelay)
            }
        }
    }

    override fun draw(g: Graphics2D) {
        bullets.values.forEach { b -> b.draw(g) }
    }

    override fun move() {
        bullets.filter { (_, b) ->
            b.position.x > ggState.maxX || b.position.x < 0 || b.position.y > ggState.maxY || b.position.y < 0
        }.forEach { (id, _) ->
            bullets.remove(id)
        }

        bullets.forEach { (_, b) ->
            b.move()
        }
    }

    override fun getDamage(enemyMidPos: Vec2): Int {
        val bulletsToRemove = ArrayList<Bullet>()

        val damage = bullets.values.sumOf { b ->
            if(Vec2.distance(enemyMidPos, b.midPoint()) <= (b.radius * 2)) {
                bulletsToRemove.add(b)
                b.damage * ggState.damageMultiplier
            } else {
                0
            }
        }

        if(!invincibleBullet) {
            bulletsToRemove.forEach {
                bullets.remove(it.id)
            }
        }

        return damage
    }

    override fun getNextUpgrade(): UpgradeOption? {
        if(currentLevel >= maxLevel) { return null}
        return upgrades[currentLevel - 1]
    }

    private fun addBullet(playerPosition: Vec2, direction: Vec2) {
        bullets[nextId] = Bullet(nextId, playerPosition, direction, basicAttackDamage)
        nextId++
    }
}