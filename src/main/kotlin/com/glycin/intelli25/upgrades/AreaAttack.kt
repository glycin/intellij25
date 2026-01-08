package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.model.upgradeOptionFromAttackDef
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.PNG
import java.awt.Graphics2D
import kotlin.math.roundToInt

class AreaAttack(
    ggState: GameGlobalState,
    player: Player,
) : Attack(ggState, player, AttackConfig.FORCE_FIELD_WEAPON) {

    private val basicAttackDamage: Int = 1
    private var diameter = player.width + 30

    private val upgradeOne = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[0]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            diameter = player.width + 60
            generalLevelUp()
        }
    }

    private val upgradeTwo = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[1]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            diameter = player.width + 90
            generalLevelUp()
        }
    }

    private val upgradeThree = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[2]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            diameter = player.width + 120
            generalLevelUp()
        }
    }

    private val upgradeFour = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[3]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            diameter = player.width + 160
            generalLevelUp()
        }
    }

    private val upgradeFive = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[4]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            diameter = player.width + 190
            generalLevelUp()
        }
    }

    private val upgradeSix = upgradeOptionFromAttackDef {
        val boost = attackDef.upgrades[5]
        attackDefinition = attackDef
        attackUpgradeDefinition = boost
        onSelect = {
            diameter = player.width + 250
            generalLevelUp()
        }
    }

    private val upgrades = when(ggState.chosenGameLevel) {
        1 -> listOf(upgradeOne, upgradeTwo, upgradeThree)
        2 -> listOf(upgradeOne, upgradeTwo, upgradeThree, upgradeFour)
        else -> listOf(upgradeOne, upgradeTwo, upgradeThree, upgradeFour, upgradeFive, upgradeSix)
    }

    override val maxLevel: Int = upgrades.size + 1

    private var angle = 0.0

    override fun draw(g: Graphics2D) {
        val playerMid = player.midPoint()
        val x = playerMid.x.roundToInt() - (diameter / 2)
        val y = playerMid.y.roundToInt() - (diameter / 2)
        val oldTransform = g.transform
        angle -= 0.005
        val twoPi = (Math.PI * 2.0)
        angle = (angle % twoPi + twoPi) % twoPi

        g.rotate(angle, playerMid.x.toDouble(), playerMid.y.toDouble())
        g.drawImage(PNG.FORCE_FIELD, x, y, diameter, diameter, null)
        g.transform = oldTransform
    }

    override fun move() { }

    override fun activate() { }

    override fun getDamage(enemyMidPos: Vec2, playerMidPos: Vec2): Int {
        val hitDistanceSq = (diameter / 2) * (diameter / 2)
        return if(Vec2.distanceNoSqr(enemyMidPos, playerMidPos) <= hitDistanceSq) {
            basicAttackDamage * ggState.damageMultiplier
        } else
            0
    }

    override fun getNextUpgrade(): UpgradeOption? {
        if(currentLevel >= maxLevel) { return null}
        return upgrades[currentLevel - 1]
    }
}