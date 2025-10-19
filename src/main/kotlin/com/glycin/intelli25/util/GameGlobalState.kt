package com.glycin.intelli25.util

data class GameGlobalState(
    val minX: Int,
    var minY: Int,
    var maxX: Int,
    var maxY: Int,
    var deltaTime: Long,
    @Volatile var mouseX: Int = -1,
    @Volatile var mouseY: Int = -1,
    var normalAttackShootDelay: Long = 5000L //ms,
)