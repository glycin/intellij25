package com.glycin.intelli25.upgrades

import com.glycin.intelli25.managers.AttackManager
import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.upgradeOption
import com.glycin.intelli25.util.GameGlobalState
import com.glycin.intelli25.util.UpgradePNG
import kotlinx.coroutines.CoroutineScope
import java.awt.image.BufferedImage
import kotlin.math.roundToInt
import kotlin.random.Random

class UpgradeRepository(
    private val ggState: GameGlobalState,
    private val player: Player,
    enemyManager: EnemyManager,
    scope: CoroutineScope,
) {
    private val attackUpgrades = when(ggState.chosenGameLevel) {
        1 -> mutableMapOf(
            "Enterprise Ready Integration" to AreaAttack(ggState, player),
            "Version control" to FreezingAttack(scope, ggState, player),
        )
        2 -> mutableMapOf(
            "Enterprise Ready Integration" to AreaAttack(ggState, player),
            "Version control" to FreezingAttack(scope, ggState, player),
            "Build & Deployment Tools" to PoolAttack(ggState, player, scope),
            "Kotlin" to LightningAttack(ggState, player, enemyManager, scope),
            "Impeccable style" to TargetedAttack(scope, enemyManager, ggState, player),
        )
        else -> mutableMapOf(
            "Enterprise Ready Integration" to AreaAttack(ggState, player),
            "Version control" to FreezingAttack(scope, ggState, player),
            "Build & Deployment Tools" to PoolAttack(ggState, player, scope),
            "Kotlin" to LightningAttack(ggState, player, enemyManager, scope),
            "Impeccable style" to TargetedAttack(scope, enemyManager, ggState, player),
            "AI Assistant" to RotatingAttack(ggState, player),
        )
        //"Cool stuff" to MassDestructionAttack(enemyManager, scope, ggState, player),
    }

    private val pizzaUpgrade = upgradeOption {
        val upgradeIcon = UpgradePNG.pizza
        val title = "Pizza slice"

        icon = upgradeIcon
        upgradePathTitle = "Pizza slice"
        subTitle = ""
        description = "Yum"
        effect = "Increases your health regeneration rate"
        onSelect = {
            ggState.regenRatePerSecondMultiplier *= 2
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val coffeeUpgrade = upgradeOption {
        val upgradeIcon = UpgradePNG.coffee
        val title = "Coffee"

        icon = upgradeIcon
        upgradePathTitle = "Coffee"
        subTitle = ""
        description = "A tasty sip for a bolt of energy"
        effect = "Increases your movement speed"
        onSelect = {
            ggState.speedMultiplier *= 1.5f
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val performanceUpgrade = upgradeOption {
        val upgradeIcon = UpgradePNG.performance
        val title = "Performance fix"

        icon = upgradeIcon
        upgradePathTitle = "Performance fix"
        subTitle = ""
        description = "Code running so well!"
        effect = "Increases your total health"
        onSelect = {
            ggState.healthMultiplier *= 2
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val firewallUpgrade = upgradeOption {
        val upgradeIcon = UpgradePNG.firewall
        val title = "Firewall shield"

        icon = upgradeIcon
        upgradePathTitle = "Firewall shield"
        subTitle = ""
        description = "The best defense is..."
        effect = "Decreases enemy speed"
        onSelect = {
            ggState.enemySpeedPenalty += 0.2f
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val autoRefactoringUpgrade = upgradeOption {
        val upgradeIcon = UpgradePNG.duck
        val title = "Auto Refactoring"

        icon = upgradeIcon
        upgradePathTitle = "Auto Refactoring"
        subTitle = ""
        description = "For when you don't want to do things yourself"
        effect = "Increases your damage"
        onSelect = {
            ggState.damageMultiplier *= 2
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val intentionActionsUpgrade = upgradeOption {
        val upgradeIcon = UpgradePNG.intentions
        val title = "Intention Actions"

        icon = upgradeIcon
        upgradePathTitle = "Intention Actions"
        subTitle = ""
        description = "For when your intentions are clear"
        effect = "Decrease the amount of enemies spawned"
        onSelect = {
            ggState.spawnCountPerCooldown = (ggState.spawnCountPerCooldown / 1.3f).roundToInt()
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val codeInspectionsUpgrade = upgradeOption {
        val upgradeIcon = UpgradePNG.inspections
        val title = "Code Inspections"

        icon = upgradeIcon
        upgradePathTitle = "Code Inspections"
        subTitle = ""
        description = "Go go gadget inspections!"
        effect = "Increase your experience pickup range"
        onSelect = {
            ggState.xpPickUpRangeMultiplier *= 2
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val dukeUpgrade = upgradeOption {
        val upgradeIcon = UpgradePNG.duke
        val title = "The Duke"

        icon = upgradeIcon
        upgradePathTitle = title
        subTitle = ""
        description = "Every new version of the Duke, IntelliJ is right there"
        effect = "Increase the amount of experience gained"
        onSelect = {
            ggState.xpMultiplier *= 2
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val codeFreezeUpgrade = upgradeOption {
        val upgradeIcon = UpgradePNG.freeze
        val title = "Code freeze"

        icon = upgradeIcon
        upgradePathTitle = title
        subTitle = ""
        description = "For when you need to be sure nothing will break"
        effect = "Decrease the speed in which new enemies appear"
        onSelect = {
            ggState.enemySpawnCooldown += 200L
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val pushToProd = upgradeOption {
        val upgradeIcon = UpgradePNG.push
        val title = "Push to Prod"

        icon = upgradeIcon
        upgradePathTitle = title
        subTitle = ""
        description = "For when you feel adventurous"
        effect = "Increase the amount of enemies appearing"
        onSelect = {
            ggState.enemySpawnCooldown -= 300L
            ggState.spawnCountPerCooldown = (ggState.spawnCountPerCooldown * 1.3f).roundToInt()
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val basicUpgrades = listOf(
        pizzaUpgrade, coffeeUpgrade, performanceUpgrade, firewallUpgrade, autoRefactoringUpgrade,
        intentionActionsUpgrade, codeInspectionsUpgrade, dukeUpgrade, codeFreezeUpgrade,
        pushToProd
    )

    fun getRandomUpgrades(attackManager: AttackManager): List<UpgradeOption> {
        val weaponUnlocks = if(ggState.weaponsEquipped != ggState.maxWeapons) {
            attackUpgrades.map { (key, attack) ->
                upgradeOption {
                    icon = attack.attackIcon
                    upgradePathTitle = attack.title
                    description = attack.unlockDescription
                    subTitle = ""
                    effect = attack.unlockEffect
                    onSelect = {
                        attackManager.addAttack(attack)
                        ggState.weaponsEquipped++
                        attackUpgrades.remove(key)
                        defaultLevelUp(attack.title, attack.attackIcon)
                        attack.activate()
                    }
                }
            }
        } else emptyList()

        val weaponUpgrades = attackManager.getUpgrades() + weaponUnlocks
        val chosenWeaponUpgrades = if(weaponUpgrades.isNotEmpty()) {
            if(weaponUpgrades.count() == 1) {
                weaponUpgrades.take(1)
            } else {
                val randomChance = Random.nextInt(0, 100)
                weaponUpgrades.shuffled().take(if(randomChance >= 80) 2 else 1)
            }
        } else emptyList()

        return (basicUpgrades.shuffled().take(3 - chosenWeaponUpgrades.size) + chosenWeaponUpgrades).shuffled()
    }

    private fun defaultLevelUp(title: String, inventoryIcon: BufferedImage?) {
        player.level++
        player.upgrades.putIfAbsent(title, inventoryIcon)
        ggState.inUpgradeMenu = false
    }
}