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

    val jbRed = JBColor(Color(254, 41, 88, 255), Color(254, 41, 88, 255))
    val jbRedLight = JBColor(Color(255, 93, 129, 255), Color(255, 93, 129, 255))
    val jbBlue = JBColor(Color(1, 127, 255, 255), Color(1, 127, 255, 255))
    val jbBlueLight = JBColor(Color(64, 150, 237, 255), Color(64, 150, 237, 255))
    val jbOrange = JBColor(Color(255, 129, 0, 255), Color(252, 129, 23, 255))
    val jbOrangeLight = JBColor(Color(255, 146, 35, 255), Color(255, 146, 35, 255))
    val jbPurple = JBColor(Color(133, 85, 173, 255), Color(133, 85, 173, 255))
    val jbGreen = JBColor(Color(71, 224, 84, 255), Color(71, 224, 84, 255))
}