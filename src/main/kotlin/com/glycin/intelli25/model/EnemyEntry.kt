package com.glycin.intelli25.model

import java.awt.image.BufferedImage

data class EnemyEntry(
    val name: String,
    val description: String,
    val image: BufferedImage?,
    val seen: Boolean
)