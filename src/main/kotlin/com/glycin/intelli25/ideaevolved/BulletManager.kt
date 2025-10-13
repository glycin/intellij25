package com.glycin.intelli25.ideaevolved

import com.glycin.intelli25.shared.GameGeneralState
import com.glycin.intelli25.shared.Vec2
import com.intellij.ui.JBColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D

class BulletManager(
    scope: CoroutineScope,
    private val ggState: GameGeneralState,
) {

    private var bullets = mutableListOf<Bullet>()
    var active = true

    init {
        scope.launch(Dispatchers.Default) {
            while (active) {
                bullets.filter { b ->
                    b.position.x > ggState.maxX || b.position.x < 0 || b.position.y > ggState.maxY || b.position.y < 0
                }.forEach { b ->
                    bullets.remove(b)
                }

                bullets.forEach { b ->
                    b.move()
                }
                delay(ggState.deltaTime)
            }
        }
    }

    fun addBullet(playerPosition: Vec2, mousePosition: Vec2) {
        val direction = (mousePosition - playerPosition).normalized()
        bullets.add(Bullet(playerPosition, direction))
    }

    fun drawBullets(g: Graphics2D) {
        g.color = JBColor.GREEN
        bullets.forEach { b -> b.draw(g) }
    }
}