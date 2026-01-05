package com.glycin.intelli25.model

import com.glycin.intelli25.upgrades.AttackDef
import com.glycin.intelli25.upgrades.AttackUpgradeDef
import com.glycin.intelli25.upgrades.UpgradeBoostDef
import com.glycin.intelli25.util.GameColors
import com.intellij.ui.JBColor
import java.awt.image.BufferedImage

data class UpgradeOption(
    val icon: BufferedImage,
    val title: String,
    val subTitle: String,
    val description: String,
    val effect: String,
    val color: JBColor,
    val textColor: JBColor,
    val onSelect: (UpgradeOption) -> Unit,
)

class UpgradeOptionFromAttackDefBuilder {
    var attackDefinition: AttackDef? = null
    var attackUpgradeDefinition: AttackUpgradeDef? = null
    var onSelect: ((UpgradeOption) -> Unit)? = null

    fun build(): UpgradeOption {
        requireNotNull(attackDefinition) { "attack def is needed" }
        requireNotNull(attackUpgradeDefinition) { "attack upgrade def is needed" }
        requireNotNull(onSelect) { "onSelect must be set" }
        return UpgradeOption(
            icon = attackUpgradeDefinition!!.image!!,
            title = attackUpgradeDefinition!!.title,
            subTitle = "",
            description = attackUpgradeDefinition!!.description,
            effect = attackUpgradeDefinition!!.effect,
            color = attackDefinition!!.color,
            textColor = attackDefinition!!.textColor,
            onSelect = onSelect!!
        )
    }
}

class UpgradeOptionFromBoostBuilder {
    var boost: UpgradeBoostDef? = null
    var onSelect: ((UpgradeOption) -> Unit)? = null

    fun build(): UpgradeOption {
        requireNotNull(boost) { "Boost is needed" }
        requireNotNull(onSelect) { "onSelect must be set" }
        return UpgradeOption(
            icon = boost!!.image!!,
            title = boost!!.title,
            subTitle = "",
            description = boost!!.description,
            effect = boost!!.effect,
            color = GameColors.jbGrayLight,
            textColor = GameColors.jbGrayText,
            onSelect = onSelect!!
        )
    }
}

fun upgradeOptionFromBoost(init: UpgradeOptionFromBoostBuilder.() -> Unit): UpgradeOption {
    return UpgradeOptionFromBoostBuilder().apply(init).build()
}

fun upgradeOptionFromAttackDef(init: UpgradeOptionFromAttackDefBuilder.() -> Unit): UpgradeOption {
    return UpgradeOptionFromAttackDefBuilder().apply(init).build()
}