package com.glycin.intelli25.managers

import com.glycin.intelli25.model.Enemy
import com.glycin.intelli25.model.EnemyType
import com.glycin.intelli25.model.Pickup
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.util.randomPointOnCircle
import com.intellij.ui.JBColor
import com.jetbrains.rd.util.concurrentMapOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D
import java.util.concurrent.atomic.AtomicLong

class EnemyManager(
    private val ggState: GameGlobalState,
    player: Player,
    scope: CoroutineScope,
) {
    private var enemyMap = concurrentMapOf<Int, Enemy>()
    private var pickupsMap = concurrentMapOf<Int, Pickup>()
    private var idCounter = 0
    private val elapsedTimeMs = AtomicLong(0L)

    init {
        scope.launch(Dispatchers.Default) {
            while (ggState.gameActive) {
                if(!ggState.inUpgradeMenu){
                    enemyMap.forEach { e -> e.value.move() }
                    pickupsMap.filter { p -> p.value.picked }.forEach { it.value.move() }
                }
                delay(ggState.deltaTime)
            }
        }

        scope.launch(Dispatchers.Default) {
            while (ggState.gameActive) {
                if (!ggState.inUpgradeMenu){
                    repeat(ggState.spawnCountPerCooldown) {
                        idCounter++
                        val p = randomPointOnCircle(1000.0f, Vec2(ggState.maxX / 2f, ggState.maxY / 2f))
                        val spawned = Enemy.createOfType(idCounter, p, player, Enemy.getAllowedTypes(ggState.enemyTier).random())
                        enemyMap[spawned.id] = spawned
                    }
                }
                delay(ggState.enemySpawnCooldown)
            }
        }

        scope.launch(Dispatchers.Default) {
            while (ggState.gameActive) {
                if(!ggState.inUpgradeMenu){
                    val elapsedSeconds = elapsedTimeMs.addAndGet(1000L) / 1000L
                    if(elapsedSeconds > 0 && elapsedSeconds % 30 == 0L){
                        println("Now at seconds: $elapsedSeconds")
                        ggState.spawnCountPerCooldown *= 2
                        ggState.enemySpawnCooldown -= 100L
                    }

                    if(elapsedSeconds > 0 && elapsedSeconds % 120 == 0L){
                        println("Increasing enemy tier!")
                        ggState.enemyTier++
                    }
                }
                delay(1000L) // Update every second
            }
        }
    }

    fun damage(enemy: Enemy, damage: Int) {
        enemy.currentHp -= damage
        if(enemy.currentHp <= 0){
            pickupsMap[enemy.id] = enemy.getPickup()
            ggState.score += enemy.points
            enemyMap.remove(enemy.id)
        }
    }

    fun removeAllPickups(pickups: List<Pickup>) {
        pickups.forEach {
            pickupsMap.remove(it.id)
        }
    }

    fun removePickup(pickup: Pickup) {
        pickupsMap.remove(pickup.id)
    }

    fun getEnemies() = enemyMap.values.toList()

    fun getPickups() = pickupsMap.values.toList()

    fun drawEnemies(g: Graphics2D) {
        enemyMap.values.forEach {e -> e.draw(g) }
    }

    fun drawPickups(g: Graphics2D) {
        g.color = JBColor.PINK
        pickupsMap.values.forEach {e -> e.draw(g) }
    }
}