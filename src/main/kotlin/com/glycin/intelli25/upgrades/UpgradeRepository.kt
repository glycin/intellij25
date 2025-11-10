package com.glycin.intelli25.upgrades

import com.glycin.intelli25.managers.AttackManager
import com.glycin.intelli25.model.Player
import com.glycin.intelli25.model.UpgradeOption
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

    private val basicUpgrades = listOf(
        UpgradeOption(
            icon = IconUtil.scale(AllIcons.Breakpoints.MultipleBreakpointsMuted, null, 2.5f),
            title = "Pizza slice",
            description = "Increase your health",
            onSelect = {
                ggState.healthMultiplier *= 2
                defaultLevelUp()
            }
        ),
        UpgradeOption(
            icon = IconUtil.scale(AllIcons.Actions.RunAll, null, 2.5f),
            title = "Coffee",
            description = "Increase your speed",
            onSelect = {
                ggState.speedMultiplier *= 2
                defaultLevelUp()
            }
        ),
        UpgradeOption(
            icon = IconUtil.scale(AllIcons.Actions.ShowWriteAccess, null, 2.5f),
            title = "RGB Keyboard",
            description = "Increase your damage",
            onSelect = {
                ggState.damageMultiplier *= 2
                defaultLevelUp()
            }
        ),
        UpgradeOption(
            icon = IconUtil.scale(AllIcons.CodeWithMe.CwmInvite, null, 2.5f),
            title = "The Internet",
            description = "Increase your experience pickup range",
            onSelect = {
                ggState.xpPickUpRangeMultiplier *= 2
                defaultLevelUp()
            }
        ),
        UpgradeOption(
            icon = IconUtil.scale(AllIcons.Ide.FeedbackRatingFocusedOn, null, 2.5f),
            title = "Conference",
            description = "Increase experience gained",
            onSelect = {
                ggState.xpMultiplier *= 2
                defaultLevelUp()
            }
        ),
        UpgradeOption(
            icon = IconUtil.scale(AllIcons.Nodes.Alias, null, 2.5f),
            title = "Energy drink",
            description = "Increase your health regeneration rate",
            onSelect = {
                ggState.regenRatePerSecondMultiplier *= 2
                defaultLevelUp()
            }
        )
    )

    fun getRandomUpgrades(attackManager: AttackManager): List<UpgradeOption> {
        val weaponUnlocks = if(ggState.weaponsEquipped != ggState.maxWeapons) {
            attackUpgrades.map { (key, attack) ->
                UpgradeOption(
                    icon = attack.attackIcon,
                    title = attack.title,
                    description = attack.unlockDescription,
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