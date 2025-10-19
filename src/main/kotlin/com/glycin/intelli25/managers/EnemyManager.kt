package com.glycin.intelli25.managers

import com.glycin.intelli25.model.Enemy
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
import kotlin.random.Random

class EnemyManager(
    player: Player,
    ggState: GameGlobalState,
    scope: CoroutineScope,
) {
    private var enemyMap = concurrentMapOf<Int, Enemy>()
    var active = true

    init {
        scope.launch(Dispatchers.Default) {
            while (active) {
                enemyMap.forEach { e ->
                    e.value.move()
                }
                delay(ggState.deltaTime)
            }
        }

        scope.launch(Dispatchers.Default) {
            while (active) {
                val cooldown = Random.nextLong(2_000, 15_000)
                val spawned = Enemy(
                    id = enemyMap.count() + 1,
                    position = randomPointOnCircle(1000.0f, Vec2(ggState.maxX / 2f, ggState.maxY / 2f)),
                    player = player,
                    width = 25,
                    height = 25,
                )
                enemyMap[spawned.id] = spawned
                delay(cooldown)
            }
        }
    }

    fun kill(enemy: Enemy) {
        enemyMap.remove(enemy.id)
    }

    fun killAll(enemies: List<Enemy>) {
        enemies.forEach { enemyMap.remove(it.id) }
    }

    fun getEnemies() = enemyMap.values.toList()

    fun drawEnemies(g: Graphics2D) {
        g.color = JBColor.YELLOW
        enemyMap.values.forEach {e -> e.draw(g) }
    }
}