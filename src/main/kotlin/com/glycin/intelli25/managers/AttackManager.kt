package com.glycin.intelli25.managers

import com.glycin.intelli25.model.Enemy
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.upgrades.Attack
import java.awt.Graphics2D

class AttackManager(
    private val ggState: GameGlobalState,
) {
    private var attacks = mutableListOf<Attack>()

    fun update() {
        if(!ggState.inUpgradeMenu){
            attacks.forEach { it.move() }
        }
    }

    fun getDamage(enemy: Enemy, playerMidPos: Vec2): Int {
        val midPos = enemy.midPoint()
        return attacks.sumOf { it.getDamage(midPos, playerMidPos) }
    }

    fun drawAttacks(g: Graphics2D) = attacks.forEach { it.draw(g) }

    fun addAttack(attack: Attack) {
        attacks.add(attack)
    }

    fun getUpgrades(): List<UpgradeOption> {
        return attacks.mapNotNull { it.getNextUpgrade() }
    }
}