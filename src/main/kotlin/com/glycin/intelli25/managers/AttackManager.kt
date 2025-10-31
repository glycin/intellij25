package com.glycin.intelli25.managers

import com.glycin.intelli25.model.Enemy
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.upgrades.Attack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D

class AttackManager(
    scope: CoroutineScope,
    player: Player,
    private val ggState: GameGlobalState,
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

    fun getDamage(enemy: Enemy) = attacks.sumOf { it.getDamage(enemy) }

    fun drawAttacks(g: Graphics2D) = attacks.forEach { it.draw(g) }

    fun addAttack(attack: Attack) = attacks.add(attack)
}