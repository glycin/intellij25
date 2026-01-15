package com.glycin.intelli25.upgrades

import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.model.Enemy
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.ui.SpatialGrid
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.GameGlobalState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D
import kotlin.random.Random

// This weapons is unused for now, but who knows, maybe in the future :D
class MassDestructionAttack(
    private val enemyManager: EnemyManager,
    private val scope: CoroutineScope,
    ggState: GameGlobalState,
    player: Player,
): Attack(ggState, player, AttackConfig.SIDE_SHOT_WEAPON) {

    private var triggerChance = 1//%
    override val maxLevel: Int = 1

    override fun activate() {
        scope.launch(Dispatchers.Default) {
            while (ggState.gameActive) {
                val random = Random.nextInt(0, 100)
                if(random <= triggerChance) {
                    explode()
                }
                delay(10_000L)
            }
        }
    }

    override fun draw(g: Graphics2D) {
        g.color = GameColors.jbRed
        g.fillRect(0, 0, ggState.maxX, ggState.maxY)
    }

    override fun move() { }

    override fun checkCollisions(enemyGrid: SpatialGrid<Enemy>, playerMidPos: Vec2, onHit: (Enemy, Int) -> Unit) { }

    override fun getNextUpgrade(): UpgradeOption? {
        TODO("Not yet implemented")
    }

    private fun explode() {
        enemyManager.destroyAll()
    }
}