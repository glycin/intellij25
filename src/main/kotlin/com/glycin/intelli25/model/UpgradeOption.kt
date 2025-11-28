package com.glycin.intelli25.model

import java.awt.image.BufferedImage

data class UpgradeOption(
    val icon: BufferedImage,
    val title: String,
    val subTitle: String,
    val description: String,
    val effect: String,
    val onSelect: (UpgradeOption) -> Unit,
)

class UpgradeOptionBuilder {
    var icon: BufferedImage? = null
    var upgradePathTitle: String? = null
    var subTitle: String? = null
    var description: String? = null
    var effect: String? = null
    var onSelect: ((UpgradeOption) -> Unit)? = null

    fun build(): UpgradeOption {
        requireNotNull(icon) { "icon must be set" }
        requireNotNull(upgradePathTitle) { "title must be set" }
        requireNotNull(subTitle) { "subTitle must be set" }
        requireNotNull(description) { "description must be set" }
        requireNotNull(effect) { "effect must be set" }
        requireNotNull(onSelect) { "onSelect must be set" }
        return UpgradeOption(icon!!, upgradePathTitle!!, subTitle!!, description!!, effect!!, onSelect!!)
    }
}

fun upgradeOption(init: UpgradeOptionBuilder.() -> Unit): UpgradeOption {
    return UpgradeOptionBuilder().apply(init).build()
}