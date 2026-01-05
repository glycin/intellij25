package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.model.upgradeOptionFromAttackDef
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.SpriteSheetImageLoader
import com.glycin.intelli25.util.pointOnCircle
import java.awt.Graphics2D
import kotlin.math.roundToInt

class RotatingAttack(
    ggState: GameGlobalState,
    player: Player
): Attack(ggState, player, AttackConfig.AI_FEATURE) {

    private val droneImage = SpriteSheetImageLoader.loadSprites("/sprites/effects/drone.png", 48, 48, 4).first()
    private val radius = 250f
    private val basicAttackDamage: Int = 20
    private var speed =  0.001f
    private val widthHeight = 64
    private val objectPositions = mutableListOf(pointOnCircle(radius, player.midPoint(), 0.0f))
    private var pointValues = mutableListOf(0.0f)

    private val upgradeOne = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[0]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            speed =  0.005f
            generalLevelUp()
        }
    }

    private val upgradeTwo = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[1]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            objectPositions.add(pointOnCircle(radius, player.midPoint(), 0.0f))
            pointValues[0] = 1.0f
            pointValues.add(0.0f)
            generalLevelUp()
        }
    }

    private val upgradeThree = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[2]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            speed =  0.01f
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
        objectPositions.forEach { vector ->
            g.drawImage(
                droneImage,
                vector.x.roundToInt(),
                vector.y.roundToInt(),
                widthHeight,
                widthHeight,
                null
            )
        }
    }

    override fun move() {
        for(i in 0..<pointValues.size) {
            val newPoint = pointValues[i] + speed
            objectPositions[i] = pointOnCircle(radius, player.midPoint(), newPoint)
            pointValues[i] = newPoint
        }
    }

    override fun activate() {}

    override fun getDamage(enemyMidPos: Vec2): Int {
        val inRange = objectPositions.any {
            val objetMidPoint = Vec2(it.x + (widthHeight / 2), it.y + (widthHeight / 2))
            Vec2.distance(enemyMidPos, objetMidPoint) <= widthHeight
        }

        return if (inRange) basicAttackDamage * ggState.damageMultiplier else 0
    }

    override fun getNextUpgrade(): UpgradeOption? {
        if(currentLevel >= maxLevel) { return null}
        return upgrades[currentLevel - 1]
    }
}