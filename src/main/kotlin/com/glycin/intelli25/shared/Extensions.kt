package com.glycin.intelli25.shared

import com.intellij.ui.JBColor
import java.awt.Color

fun Long.getDeltaTime() = 1000L / this

fun Color.toJbColor() = JBColor(this, this)
