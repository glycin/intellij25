package com.glycin.intelli25.util

import java.awt.image.BufferedImage

private const val BASE_PATH = "/sprites/upgrades"

object UpgradePNG {
    val coffee : BufferedImage? by lazy {
        getPng("$BASE_PATH/coffee.png",UpgradePNG::class.java)
    }

    val firewall : BufferedImage? by lazy {
        getPng("$BASE_PATH/shield.png",UpgradePNG::class.java)
    }

    val pizza : BufferedImage? by lazy {
        getPng("$BASE_PATH/pizza.png",UpgradePNG::class.java)
    }

    val duck : BufferedImage? by lazy {
        getPng("$BASE_PATH/rubberDuck.png",UpgradePNG::class.java)
    }

    val leaf : BufferedImage? by lazy {
        getPng("$BASE_PATH/leaf.png",UpgradePNG::class.java)
    }
}