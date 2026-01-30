package com.glycin.intelli25.ui

import com.intellij.util.ui.JBUI
import java.awt.Font
import java.awt.GraphicsEnvironment

object Fonts {
    val pixelFont: Font = try {
        val fontUrl = javaClass.getResourceAsStream("/fonts/PixeloidMono.ttf")
        Font.createFont(Font.TRUETYPE_FONT, fontUrl).also {
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(it)
        }
    } catch (e: Exception) {
        println("Failed to load pixel font: ${e.message}")
        Font(Font.SANS_SERIF, Font.BOLD, 16)
    }

    val jbMono: Font = try {
        val fontUrl = javaClass.getResourceAsStream("/fonts/JetBrainsMono-Bold.ttf")
        Font.createFont(Font.TRUETYPE_FONT, fontUrl).also {
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(it)
        }
    } catch (e: Exception) {
        println("Failed to load pixel font: ${e.message}")
        Font(Font.SANS_SERIF, Font.BOLD, 16)
    }
}

/**
 * Extension function to create a scaled font that adapts to the IDE "Zoom" level.
 * Without this adoption, the fonts could look too small or too big depending on the "Zoom" level
 */
fun Font.scaledToJbZoomLevel(size: Float): Font {
    // ATTENTION: it's important to keep an explicit type "Float", otherwise an Int will be used
    // and a wrong overload of method "scaleFontSize" will be used that accepts "style" as a parameter, not "size"
    // (yeah, it's quite a poor API design on the Swing side...)
    val sizeScaled: Float = JBUI.scaleFontSize(size).toFloat()
    return this.deriveFont(sizeScaled)
}
