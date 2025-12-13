package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.Pool
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.model.upgradeOption
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.UpgradePNG
import com.glycin.intelli25.util.randomPointInCircle
import com.intellij.icons.AllIcons
import com.intellij.util.IconUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D

class PoolAttack(
    ggState: GameGlobalState,
    player: Player,
    private val scope: CoroutineScope,
): Attack(ggState, player) {

    private var nextId = 0
    private val poolMap = mutableMapOf<Int, Pool>()
    override val attackIcon = UpgradePNG.coffee
    override val title = "Build & Deployment tools"
    override val unlockDescription: String
        get() = "Unlocks the power of build and deployment tools, which are essential to every developer nowadays"
    override val unlockEffect: String
        get() = "New weapon that spawns toxic pools around Runzo that hurt enemies"

    private var size = 60
    private var spawnCooldown = 10000L
    private val baseDamage = 1

    private val upgradeOne = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Maven 3 Integration"
        description = "Maven 3 Integration was added"
        effect = "Increase spawn rate of the pools."
        onSelect = {
            spawnCooldown = 7000L
            generalLevelUp()
        }
    }

    private val upgradeTwo = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Gradle Support"
        description = "IntelliJ added gradle support"
        effect = "Increase size of the spawned pools"
        onSelect = {
            size = 80
            generalLevelUp()
        }
    }

    private val upgradeThree = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Docker Support"
        description = "IntelliJ added docker support"
        effect = "Further increase breakpoint spawn rate"
        onSelect = {
            spawnCooldown = 4500L
            generalLevelUp()
        }
    }

    private val upgradeFour = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Kubernetes support"
        description = "Added k8s support!"
        effect = "Further increase breakpoint size"
        onSelect = {
            size = 120
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