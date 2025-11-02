package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Enemy
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.util.GameGlobalState
import java.awt.Graphics2D

abstract class Attack(
    val ggState: GameGlobalState,
    val player: Player,
) {
    val maxLevel: Int = 5
    var currentLevel: Int = 1

    abstract fun draw(g: Graphics2D)
    abstract fun move()
    abstract fun getDamage(enemyMidPos: Vec2): Int
    abstract fun getNextUpgrade(): UpgradeOption?
}