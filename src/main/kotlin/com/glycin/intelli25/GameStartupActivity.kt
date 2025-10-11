package com.glycin.intelli25

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class GameStartupActivity: ProjectActivity {
    override suspend fun execute(project: Project) {
        project.getService(GameService::class.java).startGame()
    }
}