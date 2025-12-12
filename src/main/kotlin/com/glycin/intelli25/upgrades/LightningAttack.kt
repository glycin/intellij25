package com.glycin.intelli25.upgrades

import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.model.Animation
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.model.upgradeOption
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.SpriteSheetImageLoader
import com.glycin.intelli25.util.UpgradePNG
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
): Attack(ggState, player) {
    private val animationBaseWidth = 128
    private val animationBaseHeight = 256
    private val attackSprites = SpriteSheetImageLoader.loadSprites("/sprites/effects/lightning.png", 64, 128, 10)
    private var attackAnimation: Animation? = null

    override val attackIcon = UpgradePNG.coffee
    override val title = "Kotlin"
    override val unlockDescription: String
        get() = "Unlocks the power of Kotlin, the JVM language made by JetBrains."
    override val unlockEffect: String
        get() = "New weapon that randomly strikes enemies for heavy damage"

    private val basicAttackDamage: Int = 50
    private var lightningRadius = 40
    private var attackCooldown =  5000L

    private val upgradeOne = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Kotlin support"
        description = "IntelliJ added kotlin support"
        effect = "Decreases lightning strike cooldown"
        onSelect = {
            attackCooldown = 3000L
            generalLevelUp()
        }
    }

    private val upgradeTwo = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Kotlin multi-platform"
        description = "IntelliJ added support for KMP"
        effect = "Increases lighting strike impact radius"
        onSelect = {
            lightningRadius = 80
            generalLevelUp()
        }
    }

    private val upgradeThree = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "K2 Mode"
        description = "IntelliJ added support for the K2 compiler"
        effect = "Decreases lightning strike cooldown even further"
        onSelect = {
            attackCooldown = 1500L
            generalLevelUp()
        }
    }

    private val upgradeFour = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Kotlin notebooks"
        description = "IntelliJ can create and run kotlin notebooks"
        effect = "Decreases cooldown even further and increases impact radius"
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
                        attackAnimation = Animation(pos, attackSprites) {
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

    override fun getDamage(enemyMidPos: Vec2): Int = 0

    override fun getNextUpgrade(): UpgradeOption? {
        if(currentLevel >= maxLevel) { return null}
        return upgrades[currentLevel - 1]
    }
}