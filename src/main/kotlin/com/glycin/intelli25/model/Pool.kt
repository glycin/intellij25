package com.glycin.intelli25.model

import com.glycin.intelli25.util.PNG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D
import kotlin.math.roundToInt

class Pool(
    val id: Int,
    var position: Vec2,
    var direction: Vec2,
    val damage: Int,
    val width: Int = 35,
    val height: Int = 35,
    val speed: Float = 0.05f,
    scope: CoroutineScope,
    onKill: (Int) -> Unit,
) {

    init {
        scope.launch(Dispatchers.Main) {
            delay(10_000L)
            onKill.invoke(id)
        }
    }

    var midPoint = Vec2(position.x + (width / 2), position.y + (height / 2))

    fun move() {
        position += direction * speed
        midPoint = Vec2(position.x + (width / 2), position.y + (height / 2))
    }

    fun draw(g: Graphics2D) {
        g.drawImage(
            PNG.POOL,
            position.x.roundToInt(),
            position.y.roundToInt(),
            width,
            height,
            null
        )
    }
}