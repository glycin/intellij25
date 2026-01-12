package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Enemy
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.Pool
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.model.upgradeOptionFromAttackDef
import com.glycin.intelli25.ui.SpatialGrid
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.randomPointInCircle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D

class PoolAttack(
    ggState: GameGlobalState,
    player: Player,
    private val scope: CoroutineScope,
): Attack(ggState, player, AttackConfig.TOXIC_POOL_WEAPON) {

    private var nextId = 0
    private val poolMap = mutableMapOf<Int, Pool>()
    private var size = 100
    private var spawnCooldown = 10000L
    private val baseDamage = 100

    private val upgradeOne = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[0]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            spawnCooldown = 7000L
            generalLevelUp()
        }
    }

    private val upgradeTwo = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[1]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            size = 150
            generalLevelUp()
        }
    }

    private val upgradeThree = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[2]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            spawnCooldown = 4500L
            generalLevelUp()
        }
    }

    private val upgradeFour = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[3]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            size = 200
            generalLevelUp()
        }
    }

    private val upgrades = when(ggState.chosenGameLevel) {
        1 -> emptyList()
        2 -> listOf(upgradeOne, upgradeTwo, upgradeThree)
        else -> listOf(upgradeOne, upgradeTwo, upgradeThree, upgradeFour)
    }

    override val maxLevel: Int = upgrades.size + 1

    override fun activate() {
        scope.launch(Dispatchers.Default) {
            while (ggState.gameActive){
                if(!ggState.inUpgradeMenu) {
                    val playerMid = player.midPoint
                    val spawnPos = randomPointInCircle(500f, playerMid)
                    val newPool = Pool(
                        id = nextId,
                        position = spawnPos,
                        direction = (playerMid - spawnPos).normalized(),
                        damage = baseDamage,
                        width = size,
                        height = size,
                        scope = scope,
                    ) {
                        poolMap.remove(it)
                    }

                    poolMap[nextId] = newPool
                    nextId++
                }
                delay(spawnCooldown)
            }
        }
    }

    override fun draw(g: Graphics2D) {
        poolMap.values.forEach { it.draw(g) }
    }

    override fun move() {
        poolMap.values.forEach { it.move() }
    }

    override fun checkCollisions(enemyGrid: SpatialGrid<Enemy>, playerMidPos: Vec2, onHit: (Enemy, Int) -> Unit) {
        poolMap.values.forEach {
            val poolHitBoxSq = it.width * it.width
            val enemies = enemyGrid.retrieve(it.position)
            for(enemy in enemies) {
                if(Vec2.distanceNoSqr(enemy.midPoint, it.midPoint) <= poolHitBoxSq) {
                    onHit(enemy, it.damage * ggState.damageMultiplier)
                }
            }
        }
    }

    override fun getNextUpgrade(): UpgradeOption? {
        if(currentLevel >= maxLevel) { return null}
        return upgrades[currentLevel - 1]
    }
}