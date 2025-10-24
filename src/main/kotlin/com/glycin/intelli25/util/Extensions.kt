package com.glycin.intelli25.util

import com.glycin.intelli25.model.Vec2
import com.intellij.ui.JBColor
import java.awt.Color
import java.awt.Point
import kotlin.math.roundToInt

fun Long.getDeltaTime() = 1000L / this

fun Color.toJbColor() = JBColor(this, this)

fun Vec2.toPoint() : Point = Point(x.roundToInt(), y.roundToInt())