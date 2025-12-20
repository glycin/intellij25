package com.glycin.intelli25.util

import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO

object PNG {

    val SPEECH_BUBBLE : BufferedImage? by lazy {
        getPng("/sprites/speech-bubble.png", PNG::class.java)
    }

    val RUNZO : BufferedImage? by lazy {
        getPng("/sprites/runzo/runzo_idle.png", PNG::class.java)
    }

    val RUNZO_WALK : BufferedImage? by lazy {
        getPng("/sprites/runzo/runzo_move.png", PNG::class.java)
    }

    val RUNZO_HURT : BufferedImage? by lazy {
        getPng("/sprites/runzo/runzo_hurt.png", PNG::class.java)
    }

    val COIN : BufferedImage? by lazy {
        getPng("/sprites/coin.png", PNG::class.java)
    }

    val BULLET: BufferedImage? by lazy {
        getPng("/sprites/bullet.png", PNG::class.java)
    }

    val FORCE_FIELD : BufferedImage? by lazy {
        getPng("/sprites/effects/force_field.png", PNG::class.java)
    }

    val POOL : BufferedImage? by lazy {
        getPng("/sprites/effects/puddle.png", PNG::class.java)
    }

    val FIREWORK_MISSILE : BufferedImage? by lazy {
        getPng("/sprites/effects/firework.png", PNG::class.java)
    }

    val START_BACKGROUND : BufferedImage? by lazy {
        getPng("/screens/game-start-splash.png", PNG::class.java)
    }

    val STORY_SCREEN_1 : BufferedImage? by lazy {
        getPng("/screens/story_screen_1.png", PNG::class.java)
    }

    val STORY_SCREEN_2 : BufferedImage? by lazy {
        getPng("/screens/story_screen_2.png", PNG::class.java)
    }

    val STORY_SCREEN_3 : BufferedImage? by lazy {
        getPng("/screens/story_screen_3.png", PNG::class.java)
    }

    val STORY_SCREEN_4 : BufferedImage? by lazy {
        getPng("/screens/story_screen_4.png", PNG::class.java)
    }

    val STORY_SCREEN_5 : BufferedImage? by lazy {
        getPng("/screens/story_screen_5.png", PNG::class.java)
    }

    val ARROW : BufferedImage? by lazy {
        getPng("/sprites/arrow.png", PNG::class.java)
    }
}