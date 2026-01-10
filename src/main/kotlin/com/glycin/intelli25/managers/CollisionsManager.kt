package com.glycin.intelli25.managers

import com.glycin.intelli25.model.Enemy
import com.glycin.intelli25.model.Pickup
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.ui.SpatialGrid
import com.glycin.intelli25.util.GameGlobalState

class CollisionsManager(
    private val player: Player,
    private val enemyManager: EnemyManager,
    private val attackManager: AttackManager,
    private val ggState: GameGlobalState,
) {
    private val pickUpRangeSq = 25 * 25
    private val enemyGrid = SpatialGrid<Enemy>(ggState.maxX, ggState.maxY, cellSize = 200)
    private val pickupGrid = SpatialGrid<Pickup>(ggState.maxX, ggState.maxY, cellSize = 200)

    fun update() {
        enemyGrid.clear()
        pickupGrid.clear()

        val enemies = enemyManager.getEnemies()
        val pickups = enemyManager.getPickups()
        val playerMid = player.midPoint()

        enemies.forEach { enemy ->
            if (enemy.currentHp > 0) {
                enemyGrid.insert(enemy, enemy.midPoint())
            }
        }

        pickups.forEach { pickup ->
            pickupGrid.insert(pickup, pickup.midPoint())
        }

        checkPlayerToEnemy(playerMid)
        checkAttackToEnemy()
        checkPlayerToPickup(playerMid)
    }

    fun updateGridBounds() {
        enemyGrid.updateBounds(ggState.maxX, ggState.maxY)
        pickupGrid.updateBounds(ggState.maxX, ggState.maxY)
    }

    fun checkPlayerToEnemy(playerMid: Vec2) {
        if(ggState.inUpgradeMenu) return

        val enemies = enemyGrid.retrieve(playerMid)
        var totalDamage = 0

        for(enemy in enemies) {
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

    fun checkPlayerToPickup(playerMid: Vec2) {
        val pickups = pickupGrid.retrieve(playerMid)
        val magnetRange = player.pickUpRange * ggState.xpPickUpRangeMultiplier
        val magnetRangeSq = magnetRange * magnetRange

        for(pickup in pickups) {
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