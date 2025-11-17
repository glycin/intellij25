package com.glycin.intelli25.ui

import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO

object PNG {

    val SPEECH_BUBBLE : BufferedImage? by lazy {
        try {
            val url = PNG::class.java.getResource("/sprites/speech-bubble.png")
            url?.let { ImageIO.read(it) }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    val RUNEE : BufferedImage? by lazy {
        try {
            val url = PNG::class.java.getResource("/sprites/runee.png")
            url?.let { ImageIO.read(it) }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    val firstLogo : BufferedImage? by lazy {
        try {
            val url = PNG::class.java.getResource("/logos/intellij-2001.png")
            url?.let { ImageIO.read(it) }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}