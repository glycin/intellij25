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
            "Git integration" to FreezingAttack(scope, ggState, player),
            "Tools" to TargetedAttack(scope, enemyManager, ggState, player),
        )
        2 -> mutableMapOf(
            "Enterprise Ready Integration" to AreaAttack(ggState, player),
            "Build & Deployment Tools" to PoolAttack(ggState, player, scope),
            "Kotlin" to LightningAttack(ggState, player, scope)
        )
        else -> mutableMapOf(
            "AI Assistant" to RotatingAttack(ggState, player),
            "Enterprise Ready Integration" to AreaAttack(ggState, player),
            "Build & Deployment Tools" to PoolAttack(ggState, player, scope),
            "Kotlin" to LightningAttack(ggState, player, scope)
        )
    }

    private val pizzaUpgrade = upgradeOption {
        val upgradeIcon = UpgradePNG.pizza
        val title = "Pizza slice"

        icon = UpgradePNG.pizza
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

        icon = UpgradePNG.coffee
        upgradePathTitle = "Coffee"
        subTitle = ""
        description = "A tasty sip for a bolt of energy"
        effect = "Increases your movement speed"
        onSelect = {
            ggState.speedMultiplier *= 2
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val performanceUpgrade = upgradeOption {
        val upgradeIcon = UpgradePNG.coffee
        val title = "Performance fix"

        icon = UpgradePNG.coffee
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

        icon = UpgradePNG.firewall
        upgradePathTitle = "Firewall shield"
        subTitle = ""
        description = "The best defense is..."
        effect = "Decreases enemy damage"
        onSelect = {
            ggState.enemyDamageMultiplier -= 1
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val autoRefactoringUpgrade = upgradeOption {
        val upgradeIcon = UpgradePNG.duck
        val title = "Auto Refactoring"

        icon = UpgradePNG.duck
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
        val upgradeIcon = UpgradePNG.coffee
        val title = "Intention Actions"

        icon = UpgradePNG.coffee
        upgradePathTitle = "Intention Actions"
        subTitle = ""
        description = "For when your intentions are clear"
        effect = "Increases your damage"
        onSelect = {
            ggState.damageMultiplier *= 2 //TODO: We have this twice
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val codeInspectionsUpgrade = upgradeOption {
        val upgradeIcon = UpgradePNG.coffee
        val title = "Code Inspections"

        icon = UpgradePNG.coffee
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
        val upgradeIcon = UpgradePNG.coffee
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

    private val basicUpgrades = listOf(
        pizzaUpgrade, coffeeUpgrade, performanceUpgrade, firewallUpgrade, autoRefactoringUpgrade, intentionActionsUpgrade, codeInspectionsUpgrade, dukeUpgrade
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