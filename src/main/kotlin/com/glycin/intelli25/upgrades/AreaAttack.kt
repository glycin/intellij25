package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.model.upgradeOption
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.UpgradePNG
import java.awt.Graphics2D
import kotlin.math.roundToInt

class AreaAttack(
    ggState: GameGlobalState,
    player: Player
) : Attack(ggState, player) {
    override val attackIcon = UpgradePNG.leaf
    override val title: String = "Enterprise Ready"
    override val unlockDescription: String
        get() = "Unlocks the power of integrations for Enterprise grade production environments"
    override val unlockEffect: String
        get() = "New weapon that adds a damaging area around Runzo."

    private val basicAttackDamage: Int = 1
    private var diameter = player.width + 10
    private val upgradeOne = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "JUnit Integration"
        description = "IntelliJ added JUnit integration in IntelliJ"
        effect = "Increase size of protective area"
        onSelect = {
            diameter = player.width + 50
            generalLevelUp()
        }
    }

    private val upgradeTwo = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "J2EE Support"
        description = "IntelliJ added J2EE support"
        effect = "Increase size of protective area"
        onSelect = {
            diameter = player.width + 80
            generalLevelUp()
        }
    }

    private val upgradeThree = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Spring Framework"
        description = "IntelliJ support for Spring framework"
        effect = "Increase size of protective area"
        onSelect = {
            diameter = player.width + 120
            generalLevelUp()
        }
    }

    private val upgradeFour = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Spring boot"
        description = "Added support for spring boot!"
        effect = "Increase size of protective area"
        onSelect = {
            diameter = player.width + 150
            generalLevelUp()
        }
    }

    private val upgradeFive = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Profiler"
        description = "Added a built in profiler"
        effect = "Increase size of protective area"
        onSelect = {
            diameter = player.width + 180
            generalLevelUp()
        }
    }

    private val upgradeSix = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Spring debugger"
        description = "Added a spring debugger!"
        effect = "Increase size of protective area"
        onSelect = {
            diameter = player.width + 220
            generalLevelUp()
        }
    }

    private val upgrades = when(ggState.chosenGameLevel) {
        1 -> listOf(upgradeOne, upgradeTwo, upgradeThree)
        2 -> listOf(upgradeOne, upgradeTwo, upgradeThree, upgradeFour)
        else -> listOf(upgradeOne, upgradeTwo, upgradeThree, upgradeFour, upgradeFive, upgradeSix)
    }

    override val maxLevel: Int = upgrades.size + 1

    override fun draw(g: Graphics2D) {
        g.color = GameColors.white
        val playerMid = player.midPoint()
        g.drawOval(playerMid.x.roundToInt() - (diameter / 2), playerMid.y.roundToInt() - (diameter / 2), diameter, diameter)
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