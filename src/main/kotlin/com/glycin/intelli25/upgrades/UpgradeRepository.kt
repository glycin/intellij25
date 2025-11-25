package com.glycin.intelli25.upgrades

import com.glycin.intelli25.managers.AttackManager
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
import com.glycin.intelli25.model.upgradeOption
import com.glycin.intelli25.util.GameGlobalState
import com.intellij.icons.AllIcons
import com.intellij.util.IconUtil
import kotlinx.coroutines.CoroutineScope

class UpgradeRepository(
    private val ggState: GameGlobalState,
    private val player: Player,
    scope: CoroutineScope,
) {
    private val attackUpgrades = mutableMapOf(
        "Kotlin null safety" to AreaAttack(ggState, player),
        "AI Assistant" to RotatingAttack(ggState, player),
        "Git integration" to LightningAttack(ggState, player, scope),
        "Breakpoint" to PoolAttack(ggState, player, scope)
    )

    private val pizzaUpgrade = upgradeOption {
        icon = IconUtil.scale(AllIcons.Breakpoints.MultipleBreakpointsMuted, null, 2.5f)
        upgradePathTitle = "Pizza slice"
        subTitle = ""
        description = "Yum"
        effect = "Increases your health regeneration rate"
        onSelect = {
            ggState.regenRatePerSecondMultiplier *= 2
            defaultLevelUp()
        }
    }

    private val coffeeUpgrade = upgradeOption {
        icon = IconUtil.scale(AllIcons.Actions.RunAll, null, 2.5f)
        upgradePathTitle = "Coffee"
        subTitle = ""
        description = "A tasty sip for a bolt of energy"
        effect = "Increases your movement speed"
        onSelect = {
            ggState.speedMultiplier *= 2
            defaultLevelUp()
        }
    }

    private val performanceUpgrade = upgradeOption {
        icon = IconUtil.scale(AllIcons.Actions.RunAll, null, 2.5f)
        upgradePathTitle = "Performance fix"
        subTitle = ""
        description = "Code running so well!"
        effect = "Increases your total health"
        onSelect = {
            ggState.healthMultiplier *= 2
            defaultLevelUp()
        }
    }

    private val firewallUpgrade = upgradeOption {
        icon = IconUtil.scale(AllIcons.Actions.Scratch, null, 2.5f)
        upgradePathTitle = "Firewall shield"
        subTitle = ""
        description = "The best defense is..."
        effect = "Decreases enemy damage"
        onSelect = {
            ggState.enemyDamageMultiplier -= 1
            defaultLevelUp()
        }
    }

    private val autoRefactoringUpgrade = upgradeOption {
        icon = IconUtil.scale(AllIcons.Actions.ShowWriteAccess, null, 2.5f)
        upgradePathTitle = "Auto Refactoring"
        subTitle = ""
        description = "For when you don't want to do things yourself"
        effect = "Increases your damage"
        onSelect = {
            ggState.damageMultiplier *= 2
            defaultLevelUp()
        }
    }

    private val intentionActionsUpgrade = upgradeOption {
        icon = IconUtil.scale(AllIcons.Actions.RunAll, null, 2.5f)
        upgradePathTitle = "Intention Actions"
        subTitle = ""
        description = "For when your intentions are clear"
        effect = "Increases your damage"
        onSelect = {
            ggState.damageMultiplier *= 2 //TODO: We have this twice
            defaultLevelUp()
        }
    }

    private val codeInspectionsUpgrade = upgradeOption {
        icon = IconUtil.scale(AllIcons.CodeWithMe.CwmInvite, null, 2.5f)
        upgradePathTitle = "Code Inspections"
        subTitle = ""
        description = "Go go gadget inspections!"
        effect = "Increase your experience pickup range"
        onSelect = {
            ggState.xpPickUpRangeMultiplier *= 2
            defaultLevelUp()
        }
    }

    private val dukeUpgrade = upgradeOption {
        icon = IconUtil.scale(AllIcons.Actions.RunAll, null, 2.5f)
        upgradePathTitle = "The Duke"
        subTitle = ""
        description = "Every new version of the Duke, IntelliJ is right there"
        effect = "Increase the amount of experience gained"
        onSelect = {
            ggState.xpMultiplier *= 2
            defaultLevelUp()
        }
    }

    private val basicUpgrades = listOf(
        pizzaUpgrade, coffeeUpgrade, performanceUpgrade, firewallUpgrade, autoRefactoringUpgrade, intentionActionsUpgrade, codeInspectionsUpgrade, dukeUpgrade
    )

    fun getRandomUpgrades(attackManager: AttackManager): List<UpgradeOption> {
        val weaponUnlocks = if(ggState.weaponsEquipped != ggState.maxWeapons) {
            attackUpgrades.map { (key, attack) ->
                UpgradeOption(
                    icon = attack.attackIcon,
                    title = attack.title,
                    description = attack.unlockDescription,
                    subTitle = "",
                    effect = attack.unlockEffect,
                    onSelect = {
                        attackManager.addAttack(attack)
                        ggState.weaponsEquipped++
                        attackUpgrades.remove(key)
                        defaultLevelUp()
                    }
                )
            }
        } else emptyList()

        val weaponUpgrades = attackManager.getUpgrades()

        return (weaponUnlocks + weaponUpgrades + basicUpgrades).shuffled().take(3)
    }

    private fun defaultLevelUp() {
        player.level++
        ggState.inUpgradeMenu = false
    }
}