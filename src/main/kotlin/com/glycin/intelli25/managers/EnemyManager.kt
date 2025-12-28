package com.glycin.intelli25.managers

import com.glycin.intelli25.model.Enemy
import com.glycin.intelli25.model.EnemyType
import com.glycin.intelli25.model.Pickup
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.TreasureChest
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.util.randomPointOnCircle
import com.intellij.openapi.components.service
import com.jetbrains.rd.util.concurrentMapOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D
import java.util.concurrent.atomic.AtomicLong
import kotlin.random.Random

class EnemyManager(
    private val ggState: GameGlobalState,
    private val seenEnemies: MutableSet<EnemyType>,
    player: Player,
    scope: CoroutineScope,
) {
    private var enemyMap = concurrentMapOf<Int, Enemy>()
    private var pickupsMap = concurrentMapOf<Int, Pickup>()
    private var treasureMap = concurrentMapOf<Int, TreasureChest>()
    private var idCounter = 0
    private var chestCounter = 0
    private val elapsedTimeMs = AtomicLong(0L)

    init {
        scope.launch(Dispatchers.Default) {
            while (ggState.gameActive) {
                if(!ggState.inUpgradeMenu){
                    enemyMap.takeIf { !ggState.frozen }?.forEach { e -> e.value.move() }
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
                        val p = randomPointOnCircle((ggState.maxX / 2f) + 50, Vec2(ggState.maxX / 2f, ggState.maxY / 2f))
                        val type = Enemy.getAllowedTypes(ggState.chosenGameLevel, ggState.enemyTier).random()

                        if(!seenEnemies.contains(type)) {
                            registerNewEnemy(type)
                        }

                        val spawned = Enemy.createOfType(
                            id = idCounter,
                            position = p,
                            player = player,
                            type = type,
                            ggState = ggState,
                        )
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
                    if(elapsedSeconds > 0 && elapsedSeconds % 60 == 0L){
                        println("Now at seconds: $elapsedSeconds")
                        ggState.spawnCountPerCooldown *= 2
                        ggState.enemySpawnCooldown -= 100L
                    }

                    if(elapsedSeconds > 0 && elapsedSeconds % 90 == 0L){
                        println("Increasing enemy tier!")
                        ggState.enemyTier++
                    }
                }
                delay(1000L)
            }
        }
    }

    fun damage(enemy: Enemy, damage: Int) {
        enemy.currentHp -= damage
        if(enemy.currentHp <= 0){
            if(Random.nextDouble(100.0) <= enemy.chestDropChance) {
                chestCounter++
                treasureMap[chestCounter] = TreasureChest(
                    id = chestCounter,
                    position = enemy.position,
                )
            } else {
                pickupsMap[enemy.id] = enemy.getPickup()
            }
            ggState.score += enemy.points
            enemyMap.remove(enemy.id)
        }
    }

    fun removePickup(pickup: Pickup) {
        pickupsMap.remove(pickup.id)
    }

    fun removeChest(chest: TreasureChest) {
        treasureMap.remove(chest.id)
    }

    fun getEnemies() = enemyMap.values.toList()

    fun getPickups() = pickupsMap.values.toList()

    fun getChests() = treasureMap.values.toList()

    fun drawEnemies(g: Graphics2D) {
        enemyMap.values.forEach {e -> e.draw(g) }
    }

    fun drawPickups(g: Graphics2D) {
        pickupsMap.values.forEach {e -> e.draw(g) }
        treasureMap.values.forEach {e -> e.draw(g) }
    }

    fun getClosestEnemies(count: Int, playerPosition: Vec2): List<Enemy> {
        if(enemyMap.size <= count) { return emptyList() }

        val enemies = enemyMap.values.toList().sortedBy {
            Vec2.distance(playerPosition, it.midPoint())
        }
        return enemies.take(count)
    }

    fun getEnemiesInCircle(pos: Vec2, radius: Int): List<Enemy> {
        if(enemyMap.isEmpty()) return emptyList()

        return enemyMap.values.toList().filter {
            Vec2.distance(it.midPoint(), pos) <= radius
        }
    }

    fun destroyAll() {
        val enemies = enemyMap.values.toList()
        enemies.forEach { e ->
            val pickup = e.getPickup()
            pickupsMap[e.id] = pickup
            ggState.score += e.points
            pickup.picked = true
            enemyMap.remove(e.id)
        }
    }

    private fun registerNewEnemy(newType: EnemyType) {
        seenEnemies.add(newType)
        service<GameSaveState>().enemiesSeen = seenEnemies.joinToString(",") { it.name }
    }
}