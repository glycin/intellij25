package com.glycin.intelli25.model

data class UpgradeAtlasFeature(
    val title: String,
    val description: String,
    val effect: String,
    val unlocked: Boolean,
    val upgrades: List<UpgradeAtlasBoost>,
)