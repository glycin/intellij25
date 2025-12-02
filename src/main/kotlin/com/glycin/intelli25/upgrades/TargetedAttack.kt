package com.glycin.intelli25.upgrades

import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.model.Enemy
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.TargetedProjectile
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.UpgradePNG
import com.jetbrains.rd.util.concurrentMapOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class TargetedAttack(
    private val scope: CoroutineScope,
    private val enemyManager: EnemyManager,
    ggState: GameGlobalState,
    player: Player,
): Attack(ggState, player) {

    private var cooldown = 5000L
    private val damage = 10
    private var projectileCount = 1
    private var nextId = 0L
    private val projectiles = concurrentMapOf<Long, TargetedProjectile>()

    override val attackIcon: BufferedImage? = UpgradePNG.coffee
    override val title: String = "Tools"
    override val unlockDescription: String
        get() = "Some description"
    override val unlockEffect: String
        get() = "New weapon that fires a projectile to the closest enemy"

    override fun activate() {
        scope.launch(Dispatchers.Default) {
            while(ggState.gameActive) {
                if(!ggState.inUpgradeMenu){
                    enemyManager.getClosestEnemies(projectileCount, player.midPoint()).forEach {
                        addProjectile(it)
                    }
                }
                delay(cooldown)
            }
        }
    }

    override fun draw(g: Graphics2D) {
        projectiles.values.forEach { it.draw(g) }
    }

    override fun move() {
        projectiles.values.forEach { it.move() }
    }

    override fun getDamage(enemyMidPos: Vec2): Int {
        val attacksToRemove = ArrayList<TargetedProjectile>()
        val damage = projectiles.values.sumOf { tp ->
            if(Vec2.distance(enemyMidPos, tp.midPoint()) <= tp.radius) {
                attacksToRemove.add(tp)
                tp.damage * ggState.damageMultiplier
            } else {
                0
            }
        }

        attacksToRemove.addAll(projectiles.values.filter { it.target.currentHp <= 0 })

        attacksToRemove.forEach {
            projectiles.remove(it.id)
        }

        return damage
    }

    override fun getNextUpgrade(): UpgradeOption? {
        TODO("Not yet implemented")
    }

    override val maxLevel: Int = 1

    private fun addProjectile(target: Enemy) {
        val new = TargetedProjectile(
            id = nextId,
            position = player.midPoint(),
            damage = damage,
            target = target,
        )
        projectiles[nextId] = new
        nextId++
    }
}