package com.glycin.intelli25.util

import java.awt.image.BufferedImage

private const val BASE_PATH = "/sprites/enemies"
object EnemyPNG {

    val bug : BufferedImage? by lazy {
        getPng("$BASE_PATH/Bug.png",EnemyPNG::class.java)
    }

    val blocker : BufferedImage? by lazy {
        getPng("$BASE_PATH/Blocker.png",EnemyPNG::class.java)
    }

    val calendar : BufferedImage? by lazy {
        getPng("$BASE_PATH/BurningCalendar.png",EnemyPNG::class.java)
    }

    val phantom : BufferedImage? by lazy {
        getPng("$BASE_PATH/BurnoutPhantom.png",EnemyPNG::class.java)
    }

    val demon : BufferedImage? by lazy {
        getPng("$BASE_PATH/DoomscrollDemon.png",EnemyPNG::class.java)
    }

    val vampire : BufferedImage? by lazy {
        getPng("$BASE_PATH/LegacyVampires.png",EnemyPNG::class.java)
    }

    val bees : BufferedImage? by lazy {
        getPng("$BASE_PATH/bees.png",EnemyPNG::class.java)
    }

    val unknown : BufferedImage? by lazy {
        getPng("$BASE_PATH/unknown_enemy.png",EnemyPNG::class.java)
    }
}