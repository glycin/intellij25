package com.glycin.intelli25.util

import com.glycin.intelli25.ui.PNG
import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO

private const val BASE_PATH = "/sprites/enemies"
object EnemyPNG {

    val bug : BufferedImage? by lazy {
        try {
            val url = PNG::class.java.getResource("$BASE_PATH/Bug.png")
            url?.let { ImageIO.read(it) }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    val blocker : BufferedImage? by lazy {
        try {
            val url = PNG::class.java.getResource("$BASE_PATH/Blocker.png")
            url?.let { ImageIO.read(it) }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    val calendar : BufferedImage? by lazy {
        try {
            val url = PNG::class.java.getResource("$BASE_PATH/BurningCalendar.png")
            url?.let { ImageIO.read(it) }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    val phantom : BufferedImage? by lazy {
        try {
            val url = PNG::class.java.getResource("$BASE_PATH/BurnoutPhantom.png")
            url?.let { ImageIO.read(it) }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    val demon : BufferedImage? by lazy {
        try {
            val url = PNG::class.java.getResource("$BASE_PATH/DoomscrollDemon.png")
            url?.let { ImageIO.read(it) }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    val vampire : BufferedImage? by lazy {
        try {
            val url = PNG::class.java.getResource("$BASE_PATH/LegacyVampires.png")
            url?.let { ImageIO.read(it) }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    val bees : BufferedImage? by lazy {
        try {
            val url = PNG::class.java.getResource("$BASE_PATH/MeetingBees.png")
            url?.let { ImageIO.read(it) }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}