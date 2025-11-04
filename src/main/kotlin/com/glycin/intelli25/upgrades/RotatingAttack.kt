package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.pointOnCircle
import com.intellij.icons.AllIcons
import com.intellij.util.IconUtil
import java.awt.Graphics2D
import kotlin.math.roundToInt

class RotatingAttack(
    ggState: GameGlobalState,
    player: Player
): Attack(ggState, player) {

    override val attackIcon = IconUtil.scale(AllIcons.Javaee.WebService, null, 2.5f)
    override val title = "AI Assistant"
    private val radius = 250f
    private val basicAttackDamage: Int = 20
    private var speed =  0.005f
    private var widthHeight = 25
    private val objectPositions = mutableListOf(pointOnCircle(radius, player.midPoint(), 0.0f))
    private var pointValues = mutableListOf(0.0f)

    private val upgrades = listOf(
        UpgradeOption(attackIcon, title, "Increase rotating speed of the AI assistant") {
            speed =  0.015f
            generalLevelUp()
        },
        UpgradeOption(attackIcon, title, "Add an additional AI assistant") {
            objectPositions.add(pointOnCircle(radius, player.midPoint(), 0.0f))
            pointValues[0] = 1.0f
            pointValues.add(0.0f)
            generalLevelUp()
        },
        UpgradeOption(attackIcon, title, "Further increase rotating speed and size of the AI assistant") {
            speed =  0.025f
            generalLevelUp()
        },
        UpgradeOption(attackIcon, title, "Add an additional AI assistant") {
            objectPositions.add(pointOnCircle(radius, player.midPoint(), 0.0f))
            pointValues[0] = 0.66f
            pointValues[1] = 1.33f
            pointValues.add(0.0f)
            generalLevelUp()
        }
    )

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