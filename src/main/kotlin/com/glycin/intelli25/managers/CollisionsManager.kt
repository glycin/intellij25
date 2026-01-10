package com.glycin.intelli25.managers

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.util.GameGlobalState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CollisionsManager(
    private val player: Player,
    private val enemyManager: EnemyManager,
    private val attackManager: AttackManager,
    private val ggState: GameGlobalState,
) {
    private val pickUpRangeSq = 25 * 25

    fun update() {
        checkPlayerToEnemy()
        checkAttackToEnemy()
        checkPlayerToPickup()
    }

    fun checkPlayerToEnemy() {
        if(ggState.inUpgradeMenu) return

        val enemies = enemyManager.getEnemies()
        val playerMid = player.midPoint()
        var totalDamage = 0

        for(i in enemies.indices) {
            val enemy = enemies[i]

            if(Vec2.distanceNoSqr(enemy.midPoint(), playerMid) <= player.playHitDistSq) {
                totalDamage += enemy.damage
            }
        }

        if (totalDamage > 0){
            player.hurt(totalDamage)
        } else {
            player.unhurt()
        }
    }

    fun checkPlayerToPickup() {
        val pickups = enemyManager.getPickups()
        val playerMid = player.midPoint()
        val magnetRange = player.pickUpRange * ggState.xpPickUpRangeMultiplier
        val magnetRangeSq = magnetRange * magnetRange

        for(i in pickups.indices) {
            val pickup = pickups[i]
            val distance = Vec2.distanceNoSqr(pickup.midPoint(), playerMid)
            if(distance <= magnetRangeSq) {
                pickup.picked = true
                if(distance <= pickUpRangeSq) {
                    player.addExp(pickup.xp * ggState.xpMultiplier)
                    enemyManager.removePickup(pickup)
                }
            }
        }

        val chests = enemyManager.getChests()
        for(i in chests.indices) {
            val chest = chests[i]
            if(Vec2.distanceNoSqr(chest.midPoint(), playerMid) <= player.chestPickupRangeSq) {
                player.levelUp()
                enemyManager.removeChest(chest)
            }
        }
    }

    fun checkAttackToEnemy() {
        val enemies = enemyManager.getEnemies()
        val playerMid = player.midPoint()
        for(i in enemies.indices) {
            val enemy = enemies[i]
            val damage = attackManager.getDamage(enemy, playerMid)
            if(damage > 0) {
                enemyManager.damage(enemy, damage)
            }
        }
    }
}