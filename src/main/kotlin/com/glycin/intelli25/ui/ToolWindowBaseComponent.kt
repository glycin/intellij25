package com.glycin.intelli25.ui

import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.ui.startscreens.FinalScreen
import com.glycin.intelli25.ui.startscreens.LevelOneScreen
import com.glycin.intelli25.ui.startscreens.LevelThreeScreen
import com.glycin.intelli25.ui.startscreens.LevelTwoScreen
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import java.awt.CardLayout
import javax.swing.JPanel

class ToolWindowBaseComponent(
    project: Project,
    toolWindow: ToolWindow,
    gameState: GameSaveState
) : JPanel(CardLayout()) {
    private val levelOne = LevelOneScreen(project, toolWindow, gameState)
    private val levelTwo = LevelTwoScreen(project, toolWindow, gameState)
    private val levelThree = LevelThreeScreen(project, toolWindow, gameState)
    private val finalScreen = FinalScreen(project, toolWindow)

    init {
        add(levelOne, "LEVEL_ONE")
        add(levelTwo, "LEVEL_TWO")
        add(levelThree, "LEVEL_THREE")
        add(finalScreen, "FINAL")
    }

    fun showScreen(levelBeaten: Int) {
        val cl = layout as CardLayout
        when (levelBeaten) {
            0 -> cl.show(this, "LEVEL_ONE")
            1 -> cl.show(this, "LEVEL_TWO")
            2 -> cl.show(this, "LEVEL_THREE")
            else -> cl.show(this, "FINAL")
        }
    }
}