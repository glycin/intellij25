package com.glycin.intelli25.model

import java.awt.image.BufferedImage

data class UpgradeAtlasBoost(
    val title: String,
    val description: String,
    val effect: String,
    val image: BufferedImage?,
    val unlocked: Boolean,
)