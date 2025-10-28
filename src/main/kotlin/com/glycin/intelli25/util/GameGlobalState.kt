package com.glycin.intelli25.util

data class GameGlobalState(
    // World things
    val minX: Int,
    var minY: Int,
    var maxX: Int,
    var maxY: Int,
    var deltaTime: Long,
    var inUpgradeMenu: Boolean = false,

    // Player settings
    var normalAttackDelay: Long = 2000L //ms,
)