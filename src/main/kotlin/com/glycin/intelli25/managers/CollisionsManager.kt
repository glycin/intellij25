package com.glycin.intelli25.managers

import com.glycin.intelli25.model.Bullet
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.shared.GameGeneralState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CollisionsManager(
    private val player: Player,
    private val enemyManager: EnemyManager,
    private val bulletManager: BulletManager,
    scope: CoroutineScope,
    ggState: GameGeneralState,
) {
    val active = true

    init {
        scope.launch(Dispatchers.Default) {
            while (active) {
                checkPlayerToEnemy()
                checkBulletToEnemy()
                delay(ggState.deltaTime)
            }
        }
    }

    fun checkPlayerToEnemy() {
        val enemiesInRange = enemyManager.getEnemies().filter { e ->
            e.rect().intersects(player.rect())
        }

        if (enemiesInRange.isNotEmpty()){
            enemyManager.killAll(enemiesInRange)
        }
    }

    fun checkBulletToEnemy() {
        val bulletsToRemove = mutableListOf<Bullet>()
        bulletManager.getBullets().forEach { b ->
            val enemiesInRange = enemyManager.getEnemies().filter { e ->
                e.rect().intersects(b.rect())
            }
            if (enemiesInRange.isNotEmpty()){
                enemyManager.killAll(enemiesInRange)
                bulletsToRemove.add(b)
            }
        }

        bulletManager.destroy(bulletsToRemove)
    }
}