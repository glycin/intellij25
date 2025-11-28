package com.glycin.intelli25.util

import com.intellij.ui.JBColor
import java.awt.Color

object GameColors {
    val transparent = JBColor(Color(0,0,0,0), Color(0,0,0,0))
    val black = Color.black.toJbColor()
    val white = Color.white.toJbColor()
    val yellow = Color.yellow.toJbColor()
    val red = Color.red.toJbColor()
    val green = Color.green.toJbColor()

    val jbRed = JBColor(Color(255, 35, 85, 255), Color(255, 35, 85, 255))
    val jbBlue = JBColor(Color(0, 125, 255, 255), Color(0, 125, 255, 255))
    val jbOrange = JBColor(Color(255, 129, 0, 255), Color(252, 129, 23, 255))
    val jbPurple = JBColor(Color(133, 85, 173, 255), Color(133, 85, 173, 255))
    val jbGreen = JBColor(Color(71, 224, 84, 255), Color(71, 224, 84, 255))

    val jb2001Blue = JBColor(Color(25,91,168, 255), Color(25,91, 168, 255))
    val jb2001Orange = JBColor(Color(242,138,32, 255), Color(242,138,32, 255))
    val jb2001Grey = JBColor(Color(214,211,206, 255), Color(214,211,206, 255))
}