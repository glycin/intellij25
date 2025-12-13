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

    val terminal : BufferedImage? by lazy {
        getPng("$BASE_PATH/terminal.png",UpgradePNG::class.java)
    }

    val git : BufferedImage? by lazy {
        getPng("$BASE_PATH/git.png",UpgradePNG::class.java)
    }

    val kotlin : BufferedImage? by lazy {
        getPng("$BASE_PATH/kotlin.png",UpgradePNG::class.java)
    }

    val profiler : BufferedImage? by lazy {
        getPng("$BASE_PATH/profiler.png",UpgradePNG::class.java)
    }

    val buildTools : BufferedImage? by lazy {
        getPng("$BASE_PATH/build-tools.png",UpgradePNG::class.java)
    }

    val junie : BufferedImage? by lazy {
        getPng("$BASE_PATH/junie.png",UpgradePNG::class.java)
    }

    val style : BufferedImage? by lazy {
        getPng("$BASE_PATH/style.png",UpgradePNG::class.java)
    }

    val performance : BufferedImage? by lazy {
        getPng("$BASE_PATH/performance.png",UpgradePNG::class.java)
    }

    val intentions : BufferedImage? by lazy {
        getPng("$BASE_PATH/intentions.png",UpgradePNG::class.java)
    }

    val inspections : BufferedImage? by lazy {
        getPng("$BASE_PATH/inspections.png",UpgradePNG::class.java)
    }

    val duke : BufferedImage? by lazy {
        getPng("$BASE_PATH/duke.png",UpgradePNG::class.java)
    }

    val freeze : BufferedImage? by lazy {
        getPng("$BASE_PATH/freeze.png",UpgradePNG::class.java)
    }

    val push : BufferedImage? by lazy {
        getPng("$BASE_PATH/push.png",UpgradePNG::class.java)
    }
}