package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Animation
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.model.upgradeOption
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.SpriteSheetImageLoader
import com.glycin.intelli25.util.UpgradePNG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D
import java.awt.TexturePaint
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage
import kotlin.random.Random

class FreezingAttack(
    private val scope: CoroutineScope,
    ggState: GameGlobalState,
    player: Player,
): Attack(ggState, player) {

    private val effectSprites = SpriteSheetImageLoader.loadSprites("/sprites/effects/snow.png", 64, 64, 16)
    private var effectAnimation: Animation? = null
    private var minCooldown = 30_000L
    private var maxCooldown = 90_000L
    private var freezeTime = 5_000L
    private var nextActivationTime = 0L

    override val attackIcon: BufferedImage? = UpgradePNG.git
    override val title: String = "Version control"
    override val unlockDescription: String
        get() = "New weapon that occasionally freezes all enemies in place"
    override val unlockEffect: String
        get() = "New weapon that occasionally freezes all enemies in place"

    private val upgradeOne = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "CVS & VSS"
        description = "IntelliJ added CVS and VSS support"
        effect = "Increases the time enemies stay frozen"
        onSelect = {
            freezeTime = 10_000L
            generalLevelUp()
        }
    }

    private val upgradeTwo = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Subversion"
        description = "IntelliJ added Subversion support"
        effect = "Increases how often this weapon activates"
        onSelect = {
            minCooldown = 25_000L
            maxCooldown = 75_000L
            generalLevelUp()
        }
    }

    private val upgradeThree = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Git"
        description = "IntelliJ added git support"
        effect = "Increases how often this weapon activates and how long enemies are frozen"
        onSelect = {
            minCooldown = 15_000L
            maxCooldown = 50_000L
            freezeTime = 14_500L
            generalLevelUp()
        }
    }

    private val upgrades = when(ggState.chosenGameLevel) {
        1 -> listOf(upgradeOne, upgradeTwo)
        else -> listOf(upgradeOne, upgradeTwo, upgradeThree)
    }

    override val maxLevel: Int = upgrades.size + 1

    override fun draw(g: Graphics2D) {
        if(ggState.frozen) {
            effectAnimation?.doAnimation()
            effectAnimation?.getCurrentSprite()?.let { image ->
                val anchorRect = Rectangle2D.Float(
                    0f,
                    0f,
                    image.width.toFloat(),
                    image.height.toFloat()
                )

                val texturePaint = TexturePaint(image, anchorRect)
                val originalPaint = g.paint
                g.paint = texturePaint
                val fillArea = Rectangle2D.Float(
                    0f,
                    0f,
                    ggState.maxX.toFloat(),
                    ggState.maxY.toFloat()
                )
                g.fill(fillArea)
                g.paint = originalPaint
            }
        }
    }

    override fun move() {}

    override fun activate() {
        scope.launch(Dispatchers.Default) {
            while (ggState.gameActive) {
                if(!ggState.inUpgradeMenu && ggState.elapsedTime > nextActivationTime) {
                    ggState.frozen = true
                    effectAnimation = Animation(
                        position = Vec2.zero,
                        sprites = effectSprites,
                        loop = true,
                    )
                    delay(freezeTime)
                    effectAnimation = null
                    ggState.frozen = false
                    nextActivationTime = ggState.elapsedTime + Random.nextLong(minCooldown, maxCooldown)
                }
                delay(1000L)
            }
        }
    }

    override fun getDamage(enemyMidPos: Vec2): Int = 0

    override fun getNextUpgrade(): UpgradeOption? {
        if(currentLevel >= maxLevel) { return null}
        return upgrades[currentLevel - 1]
    }
}