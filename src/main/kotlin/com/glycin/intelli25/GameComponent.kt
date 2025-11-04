package com.glycin.intelli25

import com.glycin.intelli25.managers.AttackManager
import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.util.GameGlobalState
import com.intellij.openapi.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JComponent

class GameComponent(
    private val ggState: GameGlobalState,
    private val player: Player,
    private val attackManager: AttackManager,
    private val enemyManager: EnemyManager,
    scope: CoroutineScope,
): JComponent(), Disposable {

    init {
        scope.launch(Dispatchers.Default) {
            while (ggState.gameActive) {
                player.update()
                repaint()
                delay(ggState.deltaTime)
            }
        }

        scope.launch {
            while (ggState.gameActive) {
                player.regenerate()
                delay(1000L)
            }
        }

        enableEvents(0)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if(g is Graphics2D) {
            player.draw(g)
            attackManager.drawAttacks(g)
            enemyManager.drawEnemies(g)
            enemyManager.drawPickups(g)
        }
    }

    override fun dispose() {
        ggState.gameActive = false
    }
}