package com.glycin.intelli25.managers

import com.glycin.intelli25.model.Bullet
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.model.Vec2
import com.intellij.ui.JBColor
import com.jetbrains.rd.util.concurrentMapOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D

class AttackManager(
    scope: CoroutineScope,
    player: Player,
    ggState: GameGlobalState,
) {
    private var nextId = 0L
    private var bullets = concurrentMapOf<Long, Bullet>()
    var active = true

    init {
        scope.launch(Dispatchers.Default) {
            while (active) {
                bullets.filter { (id, b) ->
                    b.position.x > ggState.maxX || b.position.x < 0 || b.position.y > ggState.maxY || b.position.y < 0
                }.forEach { (id, b) ->
                    bullets.remove(id)
                }

                bullets.forEach { (id, b) ->
                    b.move()
                }
                delay(ggState.deltaTime)
            }
        }

        scope.launch(Dispatchers.Default) {
            while (active) {
                if(!ggState.inUpgradeMenu) {
                    addBullet(player.midPoint(), Vec2(ggState.mouseX.toFloat(), ggState.mouseY.toFloat()))
                }
                delay(ggState.normalAttackDelay)
            }
        }
    }

    fun destroy(removedBullets: List<Bullet>) {
        removedBullets.forEach { bullets.remove(it.id) }
    }

    fun getBullets() = bullets.values.toList()

    fun addBullet(playerPosition: Vec2, mousePosition: Vec2) {
        val direction = (mousePosition - playerPosition).normalized()
        bullets[nextId] = Bullet(nextId, playerPosition, direction)
        nextId++
    }

    fun drawBullets(g: Graphics2D) {
        g.color = JBColor.GREEN
        bullets.values.forEach { b -> b.draw(g) }
    }
}