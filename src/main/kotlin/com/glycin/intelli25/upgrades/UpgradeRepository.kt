package com.glycin.intelli25.upgrades

import com.glycin.intelli25.managers.AttackManager
import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeBackpackItem
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
        val upgradeIcon = UpgradeBoostDef.PIZZA.image
        val title = UpgradeBoostDef.PIZZA.title

        icon = upgradeIcon
        upgradePathTitle = title
        description = UpgradeBoostDef.PIZZA.description
        effect = UpgradeBoostDef.PIZZA.effect
        onSelect = {
            ggState.regenRatePerSecondMultiplier *= 2
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val coffeeUpgrade = upgradeOption {
        val upgradeIcon = UpgradeBoostDef.COFFEE.image
        val title = UpgradeBoostDef.COFFEE.title

        icon = upgradeIcon
        upgradePathTitle = title
        description = UpgradeBoostDef.COFFEE.description
        effect = UpgradeBoostDef.COFFEE.effect
        onSelect = {
            ggState.speedMultiplier = (ggState.speedMultiplier * 1.5f).coerceAtMost(20.0f)
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val performanceUpgrade = upgradeOption {
        val upgradeIcon = UpgradeBoostDef.PERFORMANCE.image
        val title = UpgradeBoostDef.PERFORMANCE.title

        icon = upgradeIcon
        upgradePathTitle = title
        description = UpgradeBoostDef.PERFORMANCE.description
        effect = UpgradeBoostDef.PERFORMANCE.effect
        onSelect = {
            ggState.healthMultiplier *= 2
            player.currentHp = player.maxHp()
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val firewallUpgrade = upgradeOption {
        val upgradeIcon = UpgradeBoostDef.FIREWALL.image
        val title = UpgradeBoostDef.FIREWALL.title

        icon = upgradeIcon
        upgradePathTitle = title
        description = UpgradeBoostDef.FIREWALL.description
        effect = UpgradeBoostDef.FIREWALL.effect
        onSelect = {
            ggState.enemySpeedPenalty += 0.2f
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val autoRefactoringUpgrade = upgradeOption {
        val upgradeIcon = UpgradeBoostDef.AUTO_REFACTORING.image
        val title = UpgradeBoostDef.AUTO_REFACTORING.title

        icon = upgradeIcon
        upgradePathTitle = title
        description = UpgradeBoostDef.AUTO_REFACTORING.description
        effect = UpgradeBoostDef.AUTO_REFACTORING.effect
        onSelect = {
            ggState.damageMultiplier *= 2
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val intentionActionsUpgrade = upgradeOption {
        val upgradeIcon = UpgradeBoostDef.INTENTION_ACTIONS.image
        val title = UpgradeBoostDef.INTENTION_ACTIONS.title

        icon = upgradeIcon
        upgradePathTitle = title
        description = UpgradeBoostDef.INTENTION_ACTIONS.description
        effect = UpgradeBoostDef.INTENTION_ACTIONS.effect
        onSelect = {
            ggState.spawnCountPerCooldown = (ggState.spawnCountPerCooldown / 1.3f).roundToInt()
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val codeInspectionsUpgrade = upgradeOption {
        val upgradeIcon = UpgradeBoostDef.CODE_INSPECTIONS.image
        val title = UpgradeBoostDef.CODE_INSPECTIONS.title

        icon = upgradeIcon
        upgradePathTitle = title
        description = UpgradeBoostDef.CODE_INSPECTIONS.description
        effect = UpgradeBoostDef.CODE_INSPECTIONS.effect
        onSelect = {
            ggState.xpPickUpRangeMultiplier *= 2
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val dukeUpgrade = upgradeOption {
        val upgradeIcon = UpgradeBoostDef.DUKE.image
        val title = UpgradeBoostDef.DUKE.title

        icon = upgradeIcon
        upgradePathTitle = title
        description = UpgradeBoostDef.DUKE.description
        effect = UpgradeBoostDef.DUKE.effect
        onSelect = {
            ggState.xpMultiplier *= 2
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val codeFreezeUpgrade = upgradeOption {
        val upgradeIcon = UpgradeBoostDef.CODE_FREEZE.image
        val title = UpgradeBoostDef.CODE_FREEZE.title

        icon = upgradeIcon
        upgradePathTitle = title
        description = UpgradeBoostDef.CODE_FREEZE.description
        effect = UpgradeBoostDef.CODE_FREEZE.effect
        onSelect = {
            ggState.enemySpawnCooldown += 200L
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val pushToProd = upgradeOption {
        val upgradeIcon = UpgradeBoostDef.PUSH_TO_PROD.image
        val title = UpgradeBoostDef.PUSH_TO_PROD.title

        icon = upgradeIcon
        upgradePathTitle = title
        description = UpgradeBoostDef.PUSH_TO_PROD.description
        effect = UpgradeBoostDef.PUSH_TO_PROD.effect
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
        player.upgrades.merge(title, UpgradeBackpackItem(inventoryIcon, 1)) { old, _ ->
            old.copy(count = old.count + 1)
        }
        ggState.inUpgradeMenu = false
    }
}