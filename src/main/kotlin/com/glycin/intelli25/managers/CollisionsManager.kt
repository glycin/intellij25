package com.glycin.intelli25.managers

import com.glycin.intelli25.model.Bullet
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
    private val bulletManager: BulletManager,
    scope: CoroutineScope,
    ggState: GameGlobalState,
) {
    val active = true

    init {
        scope.launch(Dispatchers.Default) {
            while (active) {
                checkPlayerToEnemy()
                checkBulletToEnemy()
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
            enemyManager.killAll(enemiesInRange)
        }
    }

    fun checkPlayerToPickup() {
        val pickUpsInRange = enemyManager.getPickups().filter { p ->
            Vec2.distance(p.midPoint(), player.midPoint()) <= player.pickUpRange
        }

        if (pickUpsInRange.isNotEmpty()){
            player.addExp(pickUpsInRange.count())
            enemyManager.removeAllPickups(pickUpsInRange)
        }
    }

    fun checkBulletToEnemy() {
        val bulletsToRemove = mutableListOf<Bullet>()
        bulletManager.getBullets().forEach { b ->
            val enemiesInRange = enemyManager.getEnemies().filter { e ->
                e.rect().intersects(b.rect()) || e.rect().contains(b.middleAsPoint())
            }
            if (enemiesInRange.isNotEmpty()){
                enemyManager.killAll(enemiesInRange)
                bulletsToRemove.add(b)
            }
        }

        bulletManager.destroy(bulletsToRemove)
    }
}