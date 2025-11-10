package com.glycin.intelli25.managers

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.toPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CollisionsManager(
    private val player: Player,
    private val enemyManager: EnemyManager,
    private val attackManager: AttackManager,
    private val ggState: GameGlobalState,
    scope: CoroutineScope,
) {
    init {
        scope.launch(Dispatchers.Default) {
            while (ggState.gameActive) {
                checkPlayerToEnemy()
                checkAttackToEnemy()
                checkPlayerToPickup()
                delay(ggState.deltaTime)
            }
        }
    }

    fun checkPlayerToEnemy() {
        val enemiesInRange = enemyManager.getEnemies().filter { e ->
            e.rect().intersects(player.rect()) || e.rect().contains(player.midPoint().toPoint())
        }

        if (enemiesInRange.isNotEmpty()){
            player.hurt(enemiesInRange.sumOf { it.damage })
        }
    }

    fun checkPlayerToPickup() {
        val pickUpsInRange = enemyManager.getPickups().filter { p ->
            Vec2.distance(p.midPoint(), player.midPoint()) <= (player.pickUpRange * ggState.xpPickUpRangeMultiplier)
        }

        pickUpsInRange.forEach { pickUp ->
            pickUp.picked = true
            if(Vec2.distance(pickUp.position, player.midPoint()) <= 10f) {
                player.addExp(pickUp.xp * ggState.xpMultiplier)
                enemyManager.removePickup(pickUp)
            }
        }
    }

    fun checkAttackToEnemy() {
        enemyManager.getEnemies().forEach { e ->
            val dmg = attackManager.getDamage(e)
            if(dmg > 0){
                enemyManager.damage(e, dmg)
            }
        }
    }
}