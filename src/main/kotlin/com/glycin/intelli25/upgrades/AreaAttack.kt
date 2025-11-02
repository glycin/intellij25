package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Enemy
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.GameGlobalState
import com.intellij.icons.AllIcons
import com.intellij.util.IconUtil
import java.awt.Graphics2D
import kotlin.math.roundToInt

class AreaAttack(
    ggState: GameGlobalState,
    player: Player
) : Attack(ggState, player) {

    private val attackIcon = IconUtil.scale(AllIcons.Ide.LocalScope, null, 2.5f)
    private val title = "Kotlin null safety"
    private val basicAttackDamage: Int = 1
    private var diameter = player.width + 10

    private val upgrades = listOf(
        UpgradeOption(attackIcon, title, "Increase size of protective area") {
            ggState.inUpgradeMenu = false
            diameter = player.width + 50
            currentLevel++
        },
        UpgradeOption(attackIcon, title, "Further increase size of protective area") {
            ggState.inUpgradeMenu = false
            diameter = player.width + 80
            currentLevel++
        },
        UpgradeOption(attackIcon, title, "Even further increase size of protective area") {
            ggState.inUpgradeMenu = false
            diameter = player.width + 150
            currentLevel++
        },
        UpgradeOption(attackIcon, title, "Even even further increase size of protective area.") {
            ggState.inUpgradeMenu = false
            diameter = player.width + 200
            currentLevel++
        }
    )

    override fun draw(g: Graphics2D) {
        g.color = GameColors.white
        val playerMid = player.midPoint()
        g.drawOval(playerMid.x.roundToInt() - (diameter / 2), playerMid.y.roundToInt() - (diameter / 2), diameter, diameter)
    }

    override fun move() { }

    override fun getDamage(enemyMidPos: Vec2): Int {
        return if(Vec2.distance(enemyMidPos, player.midPoint()) <= (diameter / 2)) {
            basicAttackDamage
        } else
            0
    }

    override fun getNextUpgrade(): UpgradeOption? {
        if(currentLevel >= maxLevel) { return null}
        return upgrades[currentLevel - 1]
    }
}