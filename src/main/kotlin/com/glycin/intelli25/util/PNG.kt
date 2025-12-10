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

    val COIN : BufferedImage? by lazy {
        getPng("/sprites/coin.png", PNG::class.java)
    }

    val BULLET: BufferedImage? by lazy {
        getPng("/sprites/bullet.png", PNG::class.java)
    }

    val FORCE_FIELD : BufferedImage? by lazy {
        getPng("/sprites/effects/force_field.png", PNG::class.java)
    }
}