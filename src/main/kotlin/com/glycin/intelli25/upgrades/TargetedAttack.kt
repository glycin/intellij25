package com.glycin.intelli25.upgrades

import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.model.Animation
import com.glycin.intelli25.model.Enemy
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.TargetedProjectile
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.model.upgradeOption
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.SpriteSheetImageLoader
import com.glycin.intelli25.util.UpgradePNG
import com.intellij.refactoring.introduceParameter.onClickCallback
import com.jetbrains.rd.util.concurrentMapOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import kotlin.math.roundToInt

class TargetedAttack(
    private val scope: CoroutineScope,
    private val enemyManager: EnemyManager,
    ggState: GameGlobalState,
    player: Player,
): Attack(ggState, player) {

    private val boomEffects = SpriteSheetImageLoader.loadSprites(
        "/sprites/effects/firework_boom.png",
        256,
        256,
        30,
    )
    private val animations = mutableListOf<Animation>()
    private var cooldown = 5000L
    private val damage = 10
    private var projectileCount = 1
    private var nextId = 0L
    private val projectiles = concurrentMapOf<Long, TargetedProjectile>()

    override val attackIcon: BufferedImage? = UpgradePNG.coffee
    override val title: String = "Impeccable style"
    override val unlockDescription: String
        get() = "Serving developers with style"
    override val unlockEffect: String
        get() = "New weapon that fires a projectile to the closest enemy"

    private val upgradeOne = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Dark theme"
        description = "IntelliJ added a dark theme"
        effect = "Decreases weapon cooldown"
        onSelect = {
            cooldown = 3500L
            generalLevelUp()
        }
    }

    private val upgradeTwo = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Jetbrains Monotype"
        description = "IntelliJ added the jb monotype"
        effect = "Adds an extra projectile"
        onSelect = {
            projectileCount++
            generalLevelUp()
        }
    }

    private val upgradeThree = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "New UI"
        description = "IntelliJ got a redesign"
        effect = "Further decreases cooldown"
        onSelect = {
            cooldown = 2500L
            generalLevelUp()
        }
    }

    private val upgradeFour = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Custom themes"
        description = "Intellij added custom theme support!"
        effect = "Further decreases cooldown and adds two additional projectiles"
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

    override fun getDamage(enemyMidPos: Vec2): Int {
        val attacksToRemove = ArrayList<TargetedProjectile>()
        val damage = projectiles.values.sumOf { tp ->
            if(Vec2.distance(enemyMidPos, tp.midPoint()) <= tp.radius) {
                attacksToRemove.add(tp)
                animations.add(Animation(
                    position = enemyMidPos,
                    sprites = boomEffects,
                ))
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
        if(currentLevel >= maxLevel) { return null}
        return upgrades[currentLevel - 1]
    }

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