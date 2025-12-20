package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.model.upgradeOption
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.PNG
import java.awt.Graphics2D
import kotlin.math.roundToInt

class AreaAttack(
    ggState: GameGlobalState,
    player: Player,
) : Attack(ggState, player, AttackConfig.ENTERPRISE_READY_FEATURE) {

    private val basicAttackDamage: Int = 1
    private var diameter = player.width + 30

    private val upgradeOne = upgradeOption {
        val boost = attackDef.boosts[0]
        icon = attackIcon
        upgradePathTitle = title
        subTitle = boost.title
        description = boost.description
        effect = boost.effect
        onSelect = {
            diameter = player.width + 60
            generalLevelUp()
        }
    }

    private val upgradeTwo = upgradeOption {
        val boost = attackDef.boosts[1]
        icon = attackIcon
        upgradePathTitle = title
        subTitle = boost.title
        description = boost.description
        effect = boost.effect
        onSelect = {
            diameter = player.width + 90
            generalLevelUp()
        }
    }

    private val upgradeThree = upgradeOption {
        val boost = attackDef.boosts[2]
        icon = attackIcon
        upgradePathTitle = title
        subTitle = boost.title
        description = boost.description
        effect = boost.effect
        onSelect = {
            diameter = player.width + 120
            generalLevelUp()
        }
    }

    private val upgradeFour = upgradeOption {
        val boost = attackDef.boosts[3]
        icon = attackIcon
        upgradePathTitle = title
        subTitle = boost.title
        description = boost.description
        effect = boost.effect
        onSelect = {
            diameter = player.width + 160
            generalLevelUp()
        }
    }

    private val upgradeFive = upgradeOption {
        val boost = attackDef.boosts[4]
        icon = attackIcon
        upgradePathTitle = title
        subTitle = boost.title
        description = boost.description
        effect = boost.effect
        onSelect = {
            diameter = player.width + 190
            generalLevelUp()
        }
    }

    private val upgradeSix = upgradeOption {
        val boost = attackDef.boosts[5]
        icon = attackIcon
        upgradePathTitle = title
        subTitle = boost.title
        description = boost.description
        effect = boost.effect
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

    override fun getDamage(enemyMidPos: Vec2): Int {
        return if(Vec2.distance(enemyMidPos, player.midPoint()) <= (diameter / 2)) {
            basicAttackDamage * ggState.damageMultiplier
        } else
            0
    }

    override fun getNextUpgrade(): UpgradeOption? {
        if(currentLevel >= maxLevel) { return null}
        return upgrades[currentLevel - 1]
    }
}