package com.glycin.intelli25.persistence

import com.intellij.openapi.components.SerializablePersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.State

@Service(Service.Level.APP)
@State(name = "RuneeSaveState", storages = [Storage("runeeSaveState.xml")])
class GameSaveState: SerializablePersistentStateComponent<GameSaveState.SaveState>(SaveState()) {

    var levelsBeaten: Int
        get() = state.levelsBeaten
        set(value) {
            updateState {
                it.copy(levelsBeaten = value)
            }
        }

    var dialoguesSeen: Int
        get() = state.dialoguesSeen
        set(value) {
            updateState {
                it.copy(dialoguesSeen = value)
            }
        }

    data class SaveState (
        var levelsBeaten: Int = 0,
        var dialoguesSeen: Int = 0,
    )
}