package com.glycin.intelli25.util

import com.glycin.intelli25.GameService
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.ui.ToolWindowBaseComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

fun randomPointOnCircle(radius: Float, center: Vec2, rng: Random = Random.Default): Vec2 {
    require(radius >= 0.0) { "radius must be non-negative" }
    val theta = rng.nextDouble(0.0, 2.0 * PI).toFloat()
    val x = center.x + radius * cos(theta)
    val y = center.y + radius * sin(theta)
    return Vec2(x, y)
}

fun pointOnCircle(radius: Float, center: Vec2, point: Float): Vec2 {
    require(radius >= 0.0) { "radius must be non-negative" }
    val theta = point * PI.toFloat()
    val x = center.x + radius * cos(theta)
    val y = center.y + radius * sin(theta)
    return Vec2(x, y)
}

fun randomPointInCircle(radius: Float, center: Vec2, rng: Random = Random.Default): Vec2 {
    require(radius >= 0.0) { "radius must be non-negative" }

    val r = sqrt(rng.nextDouble(0.0, (radius * radius).toDouble())).toFloat()
    val theta = rng.nextDouble(0.0, 2.0 * PI).toFloat()

    val x = center.x + r * cos(theta)
    val y = center.y + r * sin(theta)
    return Vec2(x, y)
}

fun startGame(project: Project, toolWindow: ToolWindow, toolWindowBaseComponent: ToolWindowBaseComponent) {
    val gameService = project.getService(GameService::class.java)
    gameService.startGame(toolWindow, toolWindowBaseComponent)
}