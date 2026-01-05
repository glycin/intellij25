package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeBackpackItem
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.util.GameGlobalState
import com.intellij.ui.JBColor
import java.awt.Graphics2D
import java.awt.image.BufferedImage

abstract class Attack(
    val ggState: GameGlobalState,
    val player: Player,
    val attackDef: AttackDef,
) {
    var currentLevel: Int = 1
    abstract val maxLevel: Int
    val attackIcon: BufferedImage? = attackDef.image
    val title: String = attackDef.title
    val unlockDescription: String = attackDef.description
    val unlockEffect: String = attackDef.effect
    val color: JBColor = attackDef.color
    val textColor: JBColor = attackDef.textColor

    abstract fun activate()
    abstract fun draw(g: Graphics2D)
    abstract fun move()
    abstract fun getDamage(enemyMidPos: Vec2): Int
    abstract fun getNextUpgrade(): UpgradeOption?

    fun generalLevelUp() {
        player.level++
        player.upgrades.merge(title, UpgradeBackpackItem(attackIcon, 1)) { old, _ ->
            old.copy(count = old.count + 1)
        }
        currentLevel++
        ggState.inUpgradeMenu = false
    }
}