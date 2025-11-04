package com.glycin.intelli25.model

import javax.swing.Icon

data class UpgradeOption(
    val icon: Icon,
    val title: String,
    val description: String,
    val onSelect: (UpgradeOption) -> Unit,
)