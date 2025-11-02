package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.Pool
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.GameGlobalState
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
    scope: CoroutineScope,
): Attack(ggState, player) {

    private var nextId = 0
    private val poolMap = mutableMapOf<Int, Pool>()
    private val attackIcon = IconUtil.scale(AllIcons.Breakpoints.BreakpointFieldUnsuspendentDisabled, null, 2.5f)
    private val title = "Breakpoint"
    private var size = 60
    private var spawnCooldown = 10000L
    private val baseDamage = 1

    private val upgrades = listOf(
        UpgradeOption(attackIcon, title, "Increase breakpoint spawn rate.") {
            spawnCooldown = 7000L
            ggState.inUpgradeMenu = false
            currentLevel++
        },
        UpgradeOption(attackIcon, title, "Increase breakpoint size.") {
            size = 80
            ggState.inUpgradeMenu = false
            currentLevel++
        },
        UpgradeOption(attackIcon, title, "Further increase breakpoint spawn rate.") {
            spawnCooldown = 4500L
            ggState.inUpgradeMenu = false
            currentLevel++
        },
        UpgradeOption(attackIcon, title, "Further increase breakpoint size.") {
            size = 120
            ggState.inUpgradeMenu = false
            currentLevel++
        }
    )

    init {
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
        g.color = GameColors.jbPurple
        poolMap.values.forEach { it.draw(g) }
    }

    override fun move() {
        poolMap.values.forEach { it.move() }
    }

    override fun getDamage(enemyMidPos: Vec2): Int {
        return poolMap.values.sumOf {
            if(Vec2.distance(enemyMidPos, it.midPoint()) <= it.width) {
                it.damage
            } else 0
        }
    }

    override fun getNextUpgrade(): UpgradeOption? {
        if(currentLevel >= maxLevel) { return null}
        return upgrades[currentLevel - 1]
    }
}