package com.glycin.intelli25.managers

import com.glycin.intelli25.model.Enemy
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.upgrades.Attack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D

class AttackManager(
    private val ggState: GameGlobalState,
    scope: CoroutineScope,
) {
    private var attacks = mutableListOf<Attack>()

    init {
        scope.launch(Dispatchers.Default) {
            while (ggState.gameActive) {
                if(!ggState.inUpgradeMenu){
                    attacks.forEach { it.move() }
                }

                delay(ggState.deltaTime)
            }
        }
    }

    fun getDamage(enemy: Enemy): Int {
        val midPos = enemy.midPoint()
        return attacks.sumOf { it.getDamage(midPos) }
    }

    fun drawAttacks(g: Graphics2D) = attacks.forEach { it.draw(g) }

    fun addAttack(attack: Attack) {
        attacks.add(attack)
    }

    fun getUpgrades(): List<UpgradeOption> {
        return attacks.mapNotNull { it.getNextUpgrade() }
    }
}