package com.glycin.intelli25.model

data class GameStartupSettings(
    val chosenGameLevel: Int,
    val gameDuration: Long,
) {
    companion object {
        fun createLevelOneSettings(): GameStartupSettings {
            return GameStartupSettings(
                chosenGameLevel = 1,
                gameDuration =  3 * 60 * 1000L // 3 minutes
            )
        }

        fun createLevelTwoSettings(): GameStartupSettings {
            return GameStartupSettings(
                chosenGameLevel = 2,
                gameDuration = 6 * 60 * 1000L // 6 minutes
            )
        }

        fun createLevelThreeSettings(): GameStartupSettings {
            return GameStartupSettings(
                chosenGameLevel = 3,
                gameDuration = 10 * 60 * 1000L // 10 minutes
            )
        }
    }
}
