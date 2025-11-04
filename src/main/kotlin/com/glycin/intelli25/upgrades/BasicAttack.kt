package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Bullet
import com.glycin.intelli25.model.Enemy
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.util.GameGlobalState
import com.intellij.icons.AllIcons
import com.intellij.ui.JBColor
import com.intellij.util.IconUtil
import com.jetbrains.rd.util.concurrentMapOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Graphics2D
import kotlin.math.roundToInt

class BasicAttack(
    ggState: GameGlobalState,
    player: Player,
    scope: CoroutineScope,
) : Attack(ggState, player) {

    private var nextId = 0L
    private var bullets = concurrentMapOf<Long, Bullet>()
    private var attackDelay: Long = 2000L //ms

    override val attackIcon = IconUtil.scale(AllIcons.Nodes.Artifact, null, 2.5f)
    override val  title = "Debugger"
    private val basicAttackDamage: Int = 10

    private val upgrades = listOf(
        UpgradeOption(attackIcon, title, "Increase firing speed of debugging bullets.") {
            attackDelay = 1250L
            generalLevelUp()
        },
        UpgradeOption(attackIcon, title, "Fire additional lines of debugging bullets.") {
            generalLevelUp()
        },
        UpgradeOption(attackIcon, title, "Increase firing speed even more!") {
            attackDelay = 250L
            generalLevelUp()
        },
        UpgradeOption(attackIcon, title, "Fire even more lines of debugging bullets.") {
            generalLevelUp()
        }
    )

    init {
        scope.launch(Dispatchers.Default) {
            while (ggState.gameActive) {
                if(!ggState.inUpgradeMenu) {
                    when(currentLevel) {
                        1, 2 -> {
                            addBullet(player.midPoint(), Vec2.left)
                            addBullet(player.midPoint(), Vec2.right)
                        }
                        3, 4 -> {
                            addBullet(player.midPoint(), Vec2(1.0f, -1.0f))
                            addBullet(player.midPoint(), Vec2.left)
                            addBullet(player.midPoint(), Vec2(1.0f, 1.0f))

                            addBullet(player.midPoint(), Vec2(-1.0f, -1.0f))
                            addBullet(player.midPoint(), Vec2.right)
                            addBullet(player.midPoint(), Vec2(-1.0f, 1.0f))
                        }
                        5 -> {
                            addBullet(player.midPoint(), Vec2(1.0f, -1.0f))
                            addBullet(player.midPoint(), Vec2.left)
                            addBullet(player.midPoint(), Vec2(1.0f, 1.0f))
                            addBullet(player.midPoint(), Vec2.up)

                            addBullet(player.midPoint(), Vec2(-1.0f, -1.0f))
                            addBullet(player.midPoint(), Vec2.right)
                            addBullet(player.midPoint(), Vec2(-1.0f, 1.0f))
                            addBullet(player.midPoint(), Vec2.down)
                        }
                    }
                }
                delay(attackDelay)
            }
        }
    }

    override fun draw(g: Graphics2D) {
        g.color = JBColor.GREEN
        bullets.values.forEach { b -> b.draw(g) }
    }

    override fun move() {
        bullets.filter { (_, b) ->
            b.position.x > ggState.maxX || b.position.x < 0 || b.position.y > ggState.maxY || b.position.y < 0
        }.forEach { (id, _) ->
            bullets.remove(id)
        }

        bullets.forEach { (_, b) ->
            b.move()
        }
    }

    override fun getDamage(enemyMidPos: Vec2): Int {
        return bullets.values.sumOf { b ->
            if(Vec2.distance(enemyMidPos, b.midPoint()) <= b.radius) {
                b.damage * ggState.damageMultiplier
            } else {
                0
            }
        }
    }

    override fun getNextUpgrade(): UpgradeOption? {
        if(currentLevel >= maxLevel) { return null}
        return upgrades[currentLevel - 1]
    }

    fun addBullet(playerPosition: Vec2, direction: Vec2) {
        bullets[nextId] = Bullet(nextId, playerPosition, direction, basicAttackDamage)
        nextId++
    }
}