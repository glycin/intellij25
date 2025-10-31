package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Bullet
import com.glycin.intelli25.model.Enemy
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.util.GameGlobalState
import com.intellij.ui.JBColor
import com.jetbrains.rd.util.concurrentMapOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D

class BasicAttack(
    ggState: GameGlobalState,
    player: Player,
    scope: CoroutineScope,
) : Attack(ggState, player) {

    private var nextId = 0L
    private var bullets = concurrentMapOf<Long, Bullet>()

    init {
        scope.launch(Dispatchers.Default) {
            while (ggState.gameActive) {
                if(!ggState.inUpgradeMenu) {
                    addBullet(player.midPoint(), Vec2.left)
                    addBullet(player.midPoint(), Vec2.right)
                }
                delay(ggState.normalAttackDelay)
            }
        }
    }

    override fun draw(g: Graphics2D) {
        g.color = JBColor.GREEN
        bullets.values.forEach { b -> b.draw(g) }
    }

    override fun move() {
        bullets.filter { (_, b) ->
            b.position.x > ggState.maxX || b.position.x < 0 || b.position.y > ggState.maxY || b.position.y < 0
        }.forEach { (id, _) ->
            bullets.remove(id)
        }

        bullets.forEach { (_, b) ->
            b.move()
        }
    }

    override fun getDamage(enemy: Enemy): Int {
        return bullets.values.sumOf { b ->
            if(Vec2.distance(enemy.midPoint(), b.midPoint()) <= b.radius) {
                b.damage
            } else {
                0
            }
        }
    }

    fun addBullet(playerPosition: Vec2, direction: Vec2) {
        bullets[nextId] = Bullet(nextId, playerPosition, direction, ggState.normalAttackDamage)
        nextId++
    }
}