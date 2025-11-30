package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.model.upgradeOption
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.UpgradePNG
import com.glycin.intelli25.util.pointOnCircle
import java.awt.Graphics2D
import kotlin.math.roundToInt

class RotatingAttack(
    ggState: GameGlobalState,
    player: Player
): Attack(ggState, player) {

    override val attackIcon = UpgradePNG.coffee
    override val title = "Artificial Intelligence"
    override val unlockDescription: String
        get() = "Unlocks the power of AI, the latest transformative innovation in tech."
    override val unlockEffect: String
        get() = "New weapon that adds a drone that flies around and protects Runzo."

    private val radius = 250f
    private val basicAttackDamage: Int = 20
    private var speed =  0.005f
    private var widthHeight = 25
    private val objectPositions = mutableListOf(pointOnCircle(radius, player.midPoint(), 0.0f))
    private var pointValues = mutableListOf(0.0f)

    private val upgradeOne = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "AI Chat"
        description = ""
        effect = "Increase rotating speed of the AI assistant"
        onSelect = {
            speed =  0.015f
            generalLevelUp()
        }
    }

    private val upgradeTwo = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Junie"
        description = "IntelliJ added Junie"
        effect = "Add an additional AI assistant"
        onSelect = {
            objectPositions.add(pointOnCircle(radius, player.midPoint(), 0.0f))
            pointValues[0] = 1.0f
            pointValues.add(0.0f)
            generalLevelUp()
        }
    }

    private val upgradeThree = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "K2 Mode"
        description = "IntelliJ added support for the K2 compiler"
        effect = "Add an additional AI assistant and increase rotation speed of the drones"
        onSelect = {
            speed =  0.025f
            objectPositions.add(pointOnCircle(radius, player.midPoint(), 0.0f))
            pointValues[0] = 0.66f
            pointValues[1] = 1.33f
            pointValues.add(0.0f)
            generalLevelUp()
        }
    }

    private val upgrades = when(ggState.chosenGameLevel) {
        1 -> emptyList()
        2 -> emptyList()
        else -> listOf(upgradeOne, upgradeTwo, upgradeThree)
    }

    override val maxLevel: Int = upgrades.size + 1

    override fun draw(g: Graphics2D) {
        g.color = GameColors.jbGreen
        objectPositions.forEach { vector ->
            g.fillRect(vector.x.roundToInt(), vector.y.roundToInt(), widthHeight, widthHeight)
        }
    }

    override fun move() {
        for(i in 0..<pointValues.size) {
            val newPoint = pointValues[i] + speed
            objectPositions[i] = pointOnCircle(radius, player.midPoint(), newPoint)
            pointValues[i] = newPoint
        }
    }

    override fun getDamage(enemyMidPos: Vec2): Int {
        val inRange = objectPositions.any {
            Vec2.distance(enemyMidPos, it) <= widthHeight
        }

        return if (inRange) basicAttackDamage * ggState.damageMultiplier else 0
    }

    override fun getNextUpgrade(): UpgradeOption? {
        if(currentLevel >= maxLevel) { return null}
        return upgrades[currentLevel - 1]
    }
}