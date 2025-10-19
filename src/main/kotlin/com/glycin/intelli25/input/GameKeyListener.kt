package com.glycin.intelli25.input

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.Vec2
import java.awt.KeyEventDispatcher
import java.awt.event.KeyEvent

class GameKeyListener(
    private val player: Player,
): KeyEventDispatcher {

    private var keyMap = mutableMapOf(
        "UP" to false,
        "LEFT" to false,
        "DOWN" to false,
        "RIGHT" to false,
    )

    override fun dispatchKeyEvent(e: KeyEvent?): Boolean {
        if(e?.id == KeyEvent.KEY_PRESSED) {
            when(e.keyCode) {
                KeyEvent.VK_W, KeyEvent.VK_UP -> keyMap["UP"] = true
                KeyEvent.VK_A, KeyEvent.VK_LEFT -> keyMap["LEFT"] = true
                KeyEvent.VK_S, KeyEvent.VK_DOWN -> keyMap["DOWN"] = true
                KeyEvent.VK_D, KeyEvent.VK_RIGHT -> keyMap["RIGHT"] = true
            }

            var dir = Vec2.zero

            if (keyMap["UP"] == true){
                dir += Vec2.up
            }

            if (keyMap["LEFT"] == true){
                dir += Vec2.left
            }

            if (keyMap["DOWN"] == true){
                dir += Vec2.down
            }

            if (keyMap["RIGHT"] == true){
                dir += Vec2.right
            }
            player.movePlayer(dir)
        }

        if(e?.id == KeyEvent.KEY_RELEASED) {
            when(e.keyCode) {
                KeyEvent.VK_W, KeyEvent.VK_UP -> {
                    keyMap["UP"] = false
                    player.stopMoving()
                }
                KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                    keyMap["LEFT"] = false
                    player.stopMoving()
                }
                KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                    keyMap["DOWN"] = false
                    player.stopMoving()
                }
                KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                    keyMap["RIGHT"] = false
                    player.stopMoving()
                }
            }
        }

        return true
    }
}