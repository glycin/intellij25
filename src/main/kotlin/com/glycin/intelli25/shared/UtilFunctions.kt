package com.glycin.intelli25.shared

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

fun randomPointOnCircle(radius: Float, center: Vec2, rng: Random = Random.Default): Vec2 {
    require(radius >= 0.0) { "radius must be non-negative" }
    val theta = rng.nextDouble(0.0, 2.0 * PI).toFloat()
    val x = center.x + radius * cos(theta)
    val y = center.y + radius * sin(theta)
    return Vec2(x, y)
}