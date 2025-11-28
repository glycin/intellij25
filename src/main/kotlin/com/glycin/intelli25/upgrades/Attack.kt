package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.util.GameGlobalState
import java.awt.Graphics2D
import java.awt.image.BufferedImage

abstract class Attack(
    val ggState: GameGlobalState,
    val player: Player,
) {
    var currentLevel: Int = 1
    abstract val maxLevel: Int
    abstract val attackIcon: BufferedImage?
    abstract val title: String
    abstract val unlockDescription: String
    abstract val unlockEffect: String
    abstract fun draw(g: Graphics2D)
    abstract fun move()
    abstract fun getDamage(enemyMidPos: Vec2): Int
    abstract fun getNextUpgrade(): UpgradeOption?

    fun generalLevelUp() {
        player.level++
        currentLevel++
        ggState.inUpgradeMenu = false
    }
}