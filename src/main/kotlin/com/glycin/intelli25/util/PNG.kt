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

    val firstLogo : BufferedImage? by lazy {
        try {
            val url = PNG::class.java.getResource("/screens/intellij-2001.png")
            url?.let { ImageIO.read(it) }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    val secondLogo : BufferedImage? by lazy {
        try {
            val url = PNG::class.java.getResource("/screens/intellij-2010.png")
            url?.let { ImageIO.read(it) }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}