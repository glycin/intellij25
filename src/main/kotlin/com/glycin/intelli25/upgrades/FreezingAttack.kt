package com.glycin.intelli25.upgrades

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

class FreezingAttack(
    private val scope: CoroutineScope,
    ggState: GameGlobalState,
    player: Player,
): Attack(ggState, player) {

    private var minCooldown = 30_000L
    private var maxCooldown = 90_000L
    private var freezeTime = 5_000L
    private var nextActivationTime = 0L

    override val attackIcon: BufferedImage? = UpgradePNG.coffee
    override val title: String = "Version control"
    override val unlockDescription: String
        get() = "New weapon that occasionally freezes all enemies in place"
    override val unlockEffect: String
        get() = "New weapon that occasionally freezes all enemies in place"

    override val maxLevel: Int = 3

    override fun draw(g: Graphics2D) {
        g.color = GameColors.jbBlue
        g.fillRect(0, 0, ggState.maxX, ggState.maxY)
    }

    override fun move() {}

    override fun activate() {
        scope.launch(Dispatchers.Default) {
            while (ggState.gameActive) {
                if(!ggState.inUpgradeMenu && ggState.elapsedTime > nextActivationTime) {
                    ggState.frozen = true
                    delay(freezeTime)
                    ggState.inUpgradeMenu = false
                    nextActivationTime = ggState.elapsedTime + Random.nextLong(minCooldown, maxCooldown)
                }
                delay(1000L)
            }
        }
    }

    override fun getDamage(enemyMidPos: Vec2): Int = 0

    override fun getNextUpgrade(): UpgradeOption? {
        TODO("Not yet implemented")
    }
}