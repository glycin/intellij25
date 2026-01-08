package com.glycin.intelli25.model

data class DamageNumber(
    var x: Float,
    var y: Float,
    val dmg: Int,
    var lifeTime: Float = 0.5f
)