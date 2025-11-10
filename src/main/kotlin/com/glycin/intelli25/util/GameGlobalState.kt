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

    // Upgrade Modifiers
    var damageMultiplier: Int = 1,
    var speedMultiplier: Int = 1,
    var healthMultiplier: Int = 1,
    var xpPickUpRangeMultiplier: Int = 1,
    var xpMultiplier: Int = 1,
    var regenRatePerSecondMultiplier: Int = 1,

    // Enemy state
    var enemySpawnCooldown: Long = 2000L,
    var spawnCountPerCooldown: Int = 1,
    var enemyTier: Int = 1,

    // Inventory
    val maxWeapons: Int = 3,
    var weaponsEquipped: Int = 1,

    var score: Int = 0,
)