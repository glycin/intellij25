package com.glycin.intelli25.upgrades

import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.model.Animation
import com.glycin.intelli25.model.Enemy
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.TargetedProjectile
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.model.upgradeOptionFromAttackDef
import com.glycin.intelli25.ui.SpatialGrid
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.SpriteSheetImageLoader
import com.jetbrains.rd.util.concurrentMapOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D
import kotlin.math.roundToInt

class TargetedAttack(
    private val scope: CoroutineScope,
    private val enemyManager: EnemyManager,
    ggState: GameGlobalState,
    player: Player,
): Attack(ggState, player, AttackConfig.FIREWORK_WEAPON) {

    private val boomEffects = SpriteSheetImageLoader.loadSprites(
        "/sprites/effects/firework_boom.png",
        256,
        256,
        30,
    )
    private val animations = mutableListOf<Animation>()
    private var cooldown = 5000L
    private val damage = 1000
    private var projectileCount = 1
    private var nextId = 0L
    private val projectiles = concurrentMapOf<Long, TargetedProjectile>()

    private val upgradeOne = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[0]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            cooldown = 3500L
            generalLevelUp()
        }
    }

    private val upgradeTwo = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[1]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            projectileCount++
            generalLevelUp()
        }
    }

    private val upgradeThree = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[2]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            cooldown = 2500L
            generalLevelUp()
        }
    }

    private val upgradeFour = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[3]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            cooldown = 1500L
            projectileCount += 2
            generalLevelUp()
        }
    }

    private val upgrades = when(ggState.chosenGameLevel) {
        1 -> emptyList()
        2 -> listOf(upgradeOne)
        else -> listOf(upgradeOne, upgradeTwo, upgradeThree, upgradeFour)
    }

    override val maxLevel: Int = upgrades.size + 1

    override fun activate() {
        scope.launch(Dispatchers.Default) {
            while(ggState.gameActive) {
                if(!ggState.inUpgradeMenu){
                    enemyManager.getClosestEnemies(projectileCount, player.midPoint).forEach {
                        addProjectile(it)
                    }
                }
                delay(cooldown)
            }
        }
    }

    override fun draw(g: Graphics2D) {
        projectiles.values.forEach { it.draw(g) }
        animations.removeIf { it.done }
        animations.forEach { anim ->
            anim.doAnimation()
            g.drawImage(
                anim.getCurrentSprite(),
                anim.position.x.roundToInt() - 128,
                anim.position.y.roundToInt() - 128,
                256,
                256,
                null
            )
        }
    }

    override fun move() {
        projectiles.values.forEach { it.move() }
    }

    override fun checkCollisions(enemyGrid: SpatialGrid<Enemy>, playerMidPos: Vec2, onHit: (Enemy, Int) -> Unit) {
        val attacksToRemove = ArrayList<TargetedProjectile>()

        projectiles.values.forEach { tp ->
            val projectileMidPoint = tp.midPoint
            val enemies = enemyGrid.retrieve(projectileMidPoint)
            for(enemy in enemies) {
                if(Vec2.distanceNoSqr(enemy.midPoint, projectileMidPoint) <= tp.hitBoxSq) {
                    attacksToRemove.add(tp)
                    animations.add(Animation(
                        position = enemy.midPoint,
                        sprites = boomEffects,
                        frameDelay = 4
                    ))
                    onHit(enemy, tp.damage * ggState.damageMultiplier)
                }
            }
        }

        attacksToRemove.addAll(projectiles.values.filter { it.target.currentHp <= 0 })

        attacksToRemove.forEach {
            projectiles.remove(it.id)
        }
    }

    override fun getNextUpgrade(): UpgradeOption? {
        if(currentLevel >= maxLevel) { return null}
        return upgrades[currentLevel - 1]
    }

    private fun addProjectile(target: Enemy) {
        val new = TargetedProjectile(
            id = nextId,
            position = player.midPoint,
            damage = damage,
            target = target,
        )
        projectiles[nextId] = new
        nextId++
    }
}