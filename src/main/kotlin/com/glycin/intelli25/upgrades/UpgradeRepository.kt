package com.glycin.intelli25.upgrades

import com.glycin.intelli25.managers.AttackManager
import com.glycin.intelli25.managers.EnemyManager
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeBackpackItem
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.upgradeOptionFromBoost
import com.glycin.intelli25.util.GameGlobalState
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

    private val pizzaUpgrade = upgradeOptionFromBoost {
        val upgradeIcon = UpgradeBoostDef.PIZZA.image
        val title = UpgradeBoostDef.PIZZA.title

        boost = UpgradeBoostDef.PIZZA
        onSelect = {
            ggState.regenRatePerSecondMultiplier *= 2
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val coffeeUpgrade = upgradeOptionFromBoost {
        val upgradeIcon = UpgradeBoostDef.COFFEE.image
        val title = UpgradeBoostDef.COFFEE.title

        boost = UpgradeBoostDef.COFFEE
        onSelect = {
            ggState.speedMultiplier = (ggState.speedMultiplier * 1.5f).coerceAtMost(20.0f)
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val performanceUpgrade = upgradeOptionFromBoost {
        val upgradeIcon = UpgradeBoostDef.PERFORMANCE.image
        val title = UpgradeBoostDef.PERFORMANCE.title

        boost = UpgradeBoostDef.PERFORMANCE
        onSelect = {
            ggState.healthMultiplier *= 2
            player.currentHp = player.maxHp()
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val firewallUpgrade = upgradeOptionFromBoost {
        val upgradeIcon = UpgradeBoostDef.FIREWALL.image
        val title = UpgradeBoostDef.FIREWALL.title
        boost = UpgradeBoostDef.FIREWALL
        onSelect = {
            ggState.enemySpeedPenalty += 0.2f
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val autoRefactoringUpgrade = upgradeOptionFromBoost {
        val upgradeIcon = UpgradeBoostDef.AUTO_REFACTORING.image
        val title = UpgradeBoostDef.AUTO_REFACTORING.title
        boost = UpgradeBoostDef.AUTO_REFACTORING
        onSelect = {
            ggState.damageMultiplier *= 2
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val intentionActionsUpgrade = upgradeOptionFromBoost {
        val upgradeIcon = UpgradeBoostDef.INTENTION_ACTIONS.image
        val title = UpgradeBoostDef.INTENTION_ACTIONS.title
        boost = UpgradeBoostDef.INTENTION_ACTIONS
        onSelect = {
            ggState.spawnCountPerCooldown = (ggState.spawnCountPerCooldown / 1.3f).roundToInt()
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val codeInspectionsUpgrade = upgradeOptionFromBoost {
        val upgradeIcon = UpgradeBoostDef.CODE_INSPECTIONS.image
        val title = UpgradeBoostDef.CODE_INSPECTIONS.title
        boost = UpgradeBoostDef.CODE_INSPECTIONS
        onSelect = {
            ggState.xpPickUpRangeMultiplier *= 2
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val dukeUpgrade = upgradeOptionFromBoost {
        val upgradeIcon = UpgradeBoostDef.DUKE.image
        val title = UpgradeBoostDef.DUKE.title

        boost = UpgradeBoostDef.DUKE
        onSelect = {
            ggState.xpMultiplier *= 2
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val codeFreezeUpgrade = upgradeOptionFromBoost {
        val upgradeIcon = UpgradeBoostDef.CODE_FREEZE.image
        val title = UpgradeBoostDef.CODE_FREEZE.title
        boost = UpgradeBoostDef.CODE_FREEZE
        onSelect = {
            ggState.enemySpawnCooldown += 200L
            defaultLevelUp(title, upgradeIcon)
        }
    }

    private val pushToProd = upgradeOptionFromBoost {
        val upgradeIcon = UpgradeBoostDef.PUSH_TO_PROD.image
        val title = UpgradeBoostDef.PUSH_TO_PROD.title

        boost = UpgradeBoostDef.PUSH_TO_PROD
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
                UpgradeOption(
                    icon = attack.attackIcon!!,
                    title = attack.title,
                    subTitle = "",
                    description = attack.unlockDescription,
                    effect = attack.unlockEffect,
                    color = attack.color,
                    textColor = attack.textColor,
                    onSelect = {
                        attackManager.addAttack(attack)
                        ggState.weaponsEquipped++
                        attackUpgrades.remove(key)
                        defaultLevelUp(attack.title, attack.attackIcon)
                        attack.activate()
                    }
                )
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