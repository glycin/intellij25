package com.glycin.intelli25.upgrades

import com.glycin.intelli25.model.UpgradeAtlasFeature
import com.glycin.intelli25.model.UpgradeAtlasBoost
import com.glycin.intelli25.persistence.GameSaveState

class UpgradeAtlasRepository(
    private val saveState: GameSaveState,
) {

    fun getAllFeatures(): List<UpgradeAtlasFeature> =
        AttackConfig.ALL_FEATURES.map { it.toUpgradeAtlasFeature(saveState) }

    fun getAllBoosts(): List<UpgradeAtlasBoost> =
        UpgradeBoostDef.entries.map { def ->
            UpgradeAtlasBoost(
                title = def.title,
                description = def.description,
                effect = def.effect,
                image = def.image,
                unlocked = saveState.boostsSeen.contains(def.title)
            )
        }

    private fun AttackDef.toUpgradeAtlasFeature(saveState: GameSaveState): UpgradeAtlasFeature =
        UpgradeAtlasFeature(
            title = title,
            description = description,
            effect = effect,
            unlocked = saveState.weaponsSeen.containsKey(title),
            upgrades = boosts.map { it.toUpgradeAtlasBoost(saveState, title) },
        )

    private fun AttackUpgradeDef.toUpgradeAtlasBoost(saveState: GameSaveState, key: String): UpgradeAtlasBoost =
        UpgradeAtlasBoost(
            title = title,
            description = description,
            effect = effect,
            image = image,
            unlocked = saveState.weaponsSeen[key]?.contains(title) ?: false,
        )
}