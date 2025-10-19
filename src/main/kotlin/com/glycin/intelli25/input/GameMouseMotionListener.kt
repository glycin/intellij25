package com.glycin.intelli25.input

import com.glycin.intelli25.util.GameGlobalState
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener

class GameMouseMotionListener(
    private val ggState: GameGlobalState,
): MouseMotionListener {
    override fun mouseDragged(e: MouseEvent) {}

    override fun mouseMoved(e: MouseEvent) {
        ggState.mouseX = e.x
        ggState. mouseY = e.y
    }
}