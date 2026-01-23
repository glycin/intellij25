package com.glycin.intelli25.input

import com.glycin.intelli25.Game
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.ui.UiComponent
import java.awt.KeyEventDispatcher
import java.awt.event.KeyEvent

class GameKeyListener(
    private val player: Player,
    private val ui: UiComponent?,
    private val game: Game,
) : KeyEventDispatcher {

    /**
     * NOTE: In addition to handling arrow keys (UP, DOWN, LEFT, RIGHT), we need to mute the default editor actions.
     * See `disableDefaultEditorCaretActions` method in [com.glycin.intelli25.Game]
     */
    override fun dispatchKeyEvent(e: KeyEvent?): Boolean {
        if (e?.id == KeyEvent.KEY_PRESSED) {
            when (e.keyCode) {
                KeyEvent.VK_W, KeyEvent.VK_UP -> player.keyMap["UP"] = true
                KeyEvent.VK_A, KeyEvent.VK_LEFT -> player.keyMap["LEFT"] = true
                KeyEvent.VK_S, KeyEvent.VK_DOWN -> player.keyMap["DOWN"] = true
                KeyEvent.VK_D, KeyEvent.VK_RIGHT -> player.keyMap["RIGHT"] = true
                KeyEvent.VK_ESCAPE -> {
                    ui?.showGameQuitConfirmationDialog(
                        onQuit = {
                            game.stopGame()
                        },
                    )
                }
            }
        }

        if (e?.id == KeyEvent.KEY_RELEASED) {
            when (e.keyCode) {
                KeyEvent.VK_W, KeyEvent.VK_UP -> player.keyMap["UP"] = false
                KeyEvent.VK_A, KeyEvent.VK_LEFT -> player.keyMap["LEFT"] = false
                KeyEvent.VK_S, KeyEvent.VK_DOWN -> player.keyMap["DOWN"] = false
                KeyEvent.VK_D, KeyEvent.VK_RIGHT -> player.keyMap["RIGHT"] = false
            }
        }

        return true
    }
}