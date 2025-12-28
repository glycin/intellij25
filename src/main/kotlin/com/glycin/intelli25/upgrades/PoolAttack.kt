package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.Pool
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.model.upgradeOption
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
): Attack(ggState, player, AttackConfig.BUILD_TOOLS_FEATURE) {

    private var nextId = 0
    private val poolMap = mutableMapOf<Int, Pool>()
    private var size = 100
    private var spawnCooldown = 10000L
    private val baseDamage = 1

    private val upgradeOne = upgradeOption {
        val boost = attackDef.boosts[0]
        icon = attackIcon
        upgradePathTitle = title
        subTitle = boost.title
        description = boost.description
        effect = boost.effect
        onSelect = {
            spawnCooldown = 7000L
            generalLevelUp()
        }
    }

    private val upgradeTwo = upgradeOption {
        val boost = attackDef.boosts[1]
        icon = attackIcon
        upgradePathTitle = title
        subTitle = boost.title
        description = boost.description
        effect = boost.effect
        onSelect = {
            size = 150
            generalLevelUp()
        }
    }

    private val upgradeThree = upgradeOption {
        val boost = attackDef.boosts[2]
        icon = attackIcon
        upgradePathTitle = title
        subTitle = boost.title
        description = boost.description
        effect = boost.effect
        onSelect = {
            spawnCooldown = 4500L
            generalLevelUp()
        }
    }

    private val upgradeFour = upgradeOption {
        val boost = attackDef.boosts[3]
        icon = attackIcon
        upgradePathTitle = title
        subTitle = boost.title
        description = boost.description
        effect = boost.effect
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
                    val playerMid = player.midPoint()
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

    override fun getDamage(enemyMidPos: Vec2): Int {
        return poolMap.values.sumOf {
            if(Vec2.distance(enemyMidPos, it.midPoint()) <= it.width) {
                it.damage * ggState.damageMultiplier
            } else 0
        }
    }

    override fun getNextUpgrade(): UpgradeOption? {
        if(currentLevel >= maxLevel) { return null}
        return upgrades[currentLevel - 1]
    }
}