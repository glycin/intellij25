package com.glycin.intelli25.upgrades

import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.model.Animation
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.model.upgradeOptionFromAttackDef
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.SpriteSheetImageLoader
import com.glycin.intelli25.util.randomPointInCircle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D
import kotlin.math.roundToInt

class LightningAttack(
    ggState: GameGlobalState,
    player: Player,
    private val enemyManager: EnemyManager,
    private val scope: CoroutineScope,
): Attack(ggState, player, AttackConfig.LIGHTNING_STRIKE_WEAPON) {
    private val animationBaseWidth = 128
    private val animationBaseHeight = 256
    private val attackSprites = SpriteSheetImageLoader.loadSprites("/sprites/effects/lightning.png", 64, 128, 10)
    private var attackAnimation: Animation? = null
    private val basicAttackDamage: Int = 50
    private var lightningRadius = 40
    private var attackCooldown =  5000L

    private val upgradeOne = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[0]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            attackCooldown = 3000L
            generalLevelUp()
        }
    }

    private val upgradeTwo = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[1]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            lightningRadius = 80
            generalLevelUp()
        }
    }

    private val upgradeThree = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[2]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            attackCooldown = 1500L
            generalLevelUp()
        }
    }

    private val upgradeFour = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[3]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            lightningRadius = 120
            attackCooldown = 900L
            generalLevelUp()
        }
    }

    private val upgrades = when(ggState.chosenGameLevel) {
        1 -> emptyList()
        2 -> listOf(upgradeOne)
        else -> listOf(upgradeOne, upgradeTwo, upgradeThree, upgradeFour)
    }

    override val maxLevel: Int = upgrades.size + 1

    override fun activate()
    {
        scope.launch(Dispatchers.Default) {
            while (ggState.gameActive) {
                if(!ggState.inUpgradeMenu) {
                    randomPointInCircle(ggState.maxX - 300f, Vec2(ggState.maxX / 2.0f, ggState.maxY / 2.0f)).let { pos ->
                        attackAnimation = Animation(position = pos, sprites = attackSprites, frameDelay = 8) {
                            attackAnimation = null
                        }
                        enemyManager.getEnemiesInCircle(pos, lightningRadius).forEach { e ->
                            enemyManager.damage(e,basicAttackDamage * ggState.damageMultiplier)
                        }
                    }
                }
                delay(attackCooldown)
            }
        }
    }

    override fun draw(g: Graphics2D) {
        attackAnimation?.let {
            it.doAnimation()
            val animationWidth = animationBaseWidth + lightningRadius
            val animationHeight = animationBaseHeight + lightningRadius
            g.drawImage(
                it.getCurrentSprite(),
                it.position.x.roundToInt() - (animationWidth / 2) + (lightningRadius / 2),
                it.position.y.roundToInt() - (animationHeight - lightningRadius / 2),
                animationWidth,
                animationHeight,
                null
            )
        }
    }

    override fun move() { }

    override fun getDamage(enemyMidPos: Vec2, playerMidPos: Vec2): Int = 0

    override fun getNextUpgrade(): UpgradeOption? {
        if(currentLevel >= maxLevel) { return null}
        return upgrades[currentLevel - 1]
    }
}