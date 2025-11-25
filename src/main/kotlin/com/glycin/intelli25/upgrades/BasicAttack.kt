package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.Bullet
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.Vec2
import com.glycin.intelli25.model.upgradeOption
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

class BasicAttack(
    ggState: GameGlobalState,
    player: Player,
    scope: CoroutineScope,
) : Attack(ggState, player) {

    private var nextId = 0L
    private var bullets = concurrentMapOf<Long, Bullet>()
    private var attackDelay: Long = 2000L //ms
    override val unlockDescription: String
        get() = "This is already unlocked"
    override val unlockEffect: String
        get() = "This is already unlocked"

    override val attackIcon = IconUtil.scale(AllIcons.Nodes.Artifact, null, 2.5f)
    override val title = "Productivity"
    override val maxLevel: Int = 9
    private val basicAttackDamage: Int = 100 //TODO: Set back to 10
    private var invincibleBullet = false

    private val upgradeOne = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "HTML & CSS Support"
        description = "IntelliJ added html and css support"
        effect = "Increase firing speed of projectiles"
        onSelect = {
            attackDelay = 1600L
            generalLevelUp()
        }
    }

    private val upgradeTwo = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "SQL Support"
        description = "IntelliJ added SQL support"
        effect = "Adds two additional projectile lines"
        onSelect = {
            attackDelay = 1200L
            generalLevelUp()
        }
    }

    private val upgradeThree = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Search Everywhere"
        description = "IntelliJ added the search everywhere feature"
        effect = "Further increases firing speed of projectiles"
        onSelect = {
            attackDelay = 1000L
            generalLevelUp()
        }
    }

    private val upgradeFour = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Embedded terminal"
        description = "Added an embedded terminal!"
        effect = "Adds two additional projectile lines"
        onSelect = {
            generalLevelUp()
        }
    }

    private val upgradeFive = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Debugger"
        description = "Added an embedded debugger!"
        effect = "Increase firing speed of projectiles"
        onSelect = {
            attackDelay = 750L
            generalLevelUp()
        }
    }

    private val upgradeSix = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Decompiler"
        description = "Added a decompiler for fast peeking!"
        effect = "Fire additional projectile lines"
        onSelect = {
            generalLevelUp()
        }
    }

    private val upgradeSeven = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "JDK in the IDE"
        description = "Now you can choose your JDK in the IDE!"
        effect = "Increase firing speed to the max!"
        onSelect = {
            attackDelay = 400L
            generalLevelUp()
        }
    }

    private val upgradeEight = upgradeOption {
        icon = attackIcon
        upgradePathTitle = title
        subTitle = "Command completion"
        description = "Added the command completion feature!"
        effect = "Maximum productivity! Projectiles no longer disappear after hitting an enemy"
        onSelect = {
            invincibleBullet = true
            generalLevelUp()
        }
    }

    private val upgrades = when(ggState.chosenGameLevel) {
        1 -> listOf(upgradeOne, upgradeTwo)
        2 -> listOf(upgradeOne, upgradeTwo, upgradeThree, upgradeFour, upgradeFive, upgradeSix)
        else -> listOf(upgradeOne, upgradeTwo, upgradeThree, upgradeFour, upgradeFive, upgradeSix, upgradeSeven, upgradeEight)
    }

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
                            addBullet(player.midPoint(), Vec2(-1.0f, 1.0f))
                            addBullet(player.midPoint(), Vec2.right)
                            addBullet(player.midPoint(), Vec2.left)
                        }
                        5, 6 -> {
                            addBullet(player.midPoint(), Vec2(1.0f, -1.0f))
                            addBullet(player.midPoint(), Vec2.right)
                            addBullet(player.midPoint(), Vec2(1.0f, 1.0f))

                            addBullet(player.midPoint(), Vec2(-1.0f, -1.0f))
                            addBullet(player.midPoint(), Vec2.left)
                            addBullet(player.midPoint(), Vec2(-1.0f, 1.0f))
                        }
                        7, 8 -> {
                            addBullet(player.midPoint(), Vec2(1.0f, -1.0f))
                            addBullet(player.midPoint(), Vec2.right)
                            addBullet(player.midPoint(), Vec2(1.0f, 1.0f))
                            addBullet(player.midPoint(), Vec2.up)

                            addBullet(player.midPoint(), Vec2(-1.0f, -1.0f))
                            addBullet(player.midPoint(), Vec2.left)
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
        val bulletsToRemove = ArrayList<Bullet>()

        val damage = bullets.values.sumOf { b ->
            if(Vec2.distance(enemyMidPos, b.midPoint()) <= b.radius) {
                bulletsToRemove.add(b)
                b.damage * ggState.damageMultiplier
            } else {
                0
            }
        }

        if(!invincibleBullet) {
            bulletsToRemove.forEach {
                bullets.remove(it.id)
            }
        }

        return damage
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