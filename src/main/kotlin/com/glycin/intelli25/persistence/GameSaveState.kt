package com.glycin.intelli25.persistence

import com.intellij.openapi.components.SerializablePersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.State

@Service(Service.Level.APP)
@State(name = "RunzoSaveState", storages = [Storage("runzoSaveState.xml")])
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

    var enemiesSeen: String
        get() = state.enemiesSeen
        set(value) {
            updateState { it.copy(enemiesSeen = value) }
        }

    data class SaveState (
        var levelsBeaten: Int = 0,
        var dialoguesSeen: Int = 0,
        var enemiesSeen: String = "",
    )
}