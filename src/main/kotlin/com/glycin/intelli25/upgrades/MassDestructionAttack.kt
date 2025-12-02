package com.glycin.intelli25.upgrades

import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.UpgradePNG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import kotlin.random.Random

class MassDestructionAttack(
    private val enemyManager: EnemyManager,
    private val scope: CoroutineScope,
    ggState: GameGlobalState,
    player: Player,
): Attack(ggState, player) {

    private var triggerChance = 1//%
    override val maxLevel: Int = 1
    override val attackIcon: BufferedImage? = UpgradePNG.coffee
    override val title: String = "Quality of life"
    override val unlockDescription: String
        get() = "Some description"
    override val unlockEffect: String
        get() = "New weapon, that rarely destroys all enemies"

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
        g.color = GameColors.red
        g.fillRect(0, 0, ggState.maxX, ggState.maxY)
    }

    override fun move() { }

    override fun getDamage(enemyMidPos: Vec2): Int = 0

    override fun getNextUpgrade(): UpgradeOption? {
        TODO("Not yet implemented")
    }

    private fun explode() {
        enemyManager.destroyAll()
    }
}