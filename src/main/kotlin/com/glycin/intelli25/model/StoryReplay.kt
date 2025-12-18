package com.glycin.intelli25.model

class StoryReplay(
    val label: String,
    val unlocked: Boolean,
    val onOpen: () -> Unit,
)