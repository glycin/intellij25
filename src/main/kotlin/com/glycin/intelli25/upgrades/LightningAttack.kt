package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Player
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
import kotlin.math.roundToInt

class LightningAttack(
    ggState: GameGlobalState,
    player: Player,
    scope: CoroutineScope,
): Attack(ggState, player) {

    override val attackIcon = IconUtil.scale(AllIcons.Actions.Lightning, null, 2.5f)
    override val title = "Git integration"
    override val unlockDescription: String
        get() = "Unlocks the power of Git, which randomly strikes an area around you for heavy damage."

    private val basicAttackDamage: Int = 50
    private val maxFrameAlive: Int = 60
    private var frameAliveCount: Int = 0
    private var activeLightningPosition: Vec2? = null
    private var lightningRadius = 20
    private var attackCooldown = 5000L //ms

    private val upgrades = listOf(
        UpgradeOption(attackIcon, title, "Decreases lightning strike cooldown.") {
            attackCooldown = 3000L
            generalLevelUp()
        },
        UpgradeOption(attackIcon, title, "Increases lighting strike impact radius.") {
            lightningRadius = 40
            generalLevelUp()
        },
        UpgradeOption(attackIcon, title, "Decreases lightning strike cooldown even further.") {
            attackCooldown = 1500L
            generalLevelUp()
        },
        UpgradeOption(attackIcon, title, "Decreases cooldown even further and increases impact radius.") {
            lightningRadius = 80
            attackCooldown = 900L
            generalLevelUp()
        }
    )

    init {
        scope.launch(Dispatchers.Default) {
            while (ggState.gameActive) {
                if(!ggState.inUpgradeMenu) {
                    activeLightningPosition = randomPointInCircle(ggState.maxX - 300f, player.midPoint())
                }
                delay(attackCooldown)
            }
        }
    }

    override fun draw(g: Graphics2D) {
        g.color = GameColors.jbOrange
        activeLightningPosition?.let {
            g.fillOval(activeLightningPosition!!.x.roundToInt(), activeLightningPosition!!.y.roundToInt(), lightningRadius * 2, lightningRadius * 2)
            frameAliveCount++
            if(frameAliveCount >= maxFrameAlive) {
                activeLightningPosition = null
                frameAliveCount = 0
            }
        }
    }

    override fun move() { }

    override fun getDamage(enemyMidPos: Vec2): Int {
        return activeLightningPosition?.let { pos ->
            if(Vec2.distance(enemyMidPos, pos) <= lightningRadius) {
                basicAttackDamage * ggState.damageMultiplier
            } else 0
        } ?: 0
    }

    override fun getNextUpgrade(): UpgradeOption? {
        if(currentLevel >= maxLevel) { return null}
        return upgrades[currentLevel - 1]
    }
}