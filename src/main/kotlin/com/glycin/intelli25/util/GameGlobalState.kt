package com.glycin.intelli25.util

data class GameGlobalState(
    // World things
    val minX: Int,
    var minY: Int,
    var maxX: Int,
    var maxY: Int,
    var deltaTime: Long,
    var inUpgradeMenu: Boolean = false,

    // Input things
    @Volatile var mouseX: Int = -1,
    @Volatile var mouseY: Int = -1,

    // Player settings
    var normalAttackDelay: Long = 2000L //ms,
)