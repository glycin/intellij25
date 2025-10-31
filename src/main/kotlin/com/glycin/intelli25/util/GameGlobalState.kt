package com.glycin.intelli25.util

data class GameGlobalState(
    // World things
    val minX: Int,
    var minY: Int,
    var maxX: Int,
    var maxY: Int,
    var deltaTime: Long,
    var inUpgradeMenu: Boolean = false,
    var gameActive: Boolean = true,

    // Player state
    var normalAttackDelay: Long = 2000L, //ms
    var normalAttackDamage: Int = 2,

    // Enemy state
    var enemySpawnCooldown: Long = 2000L,
    var spawnCountPerCooldown: Int = 1,
)