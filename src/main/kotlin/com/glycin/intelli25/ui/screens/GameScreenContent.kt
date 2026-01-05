package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.GameService
import com.glycin.intelli25.model.Enemy
import com.glycin.intelli25.model.StoryReplay
import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.ui.StartButton
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.PNG
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.components.JBTabbedPane
import java.awt.BorderLayout
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GridBagLayout
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class GameScreenContent(
    private val project: Project,
    private val onStart: () -> Unit,
    private val toolWindow: ToolWindow,
    startButtonText: String,
): JPanel() {

    init {
        isOpaque = false
        layout = BorderLayout()

        val titleLabel = JLabel("IDE Survivors").apply {
            font = Fonts.pixelFont.deriveFont(24.0f)
            foreground = GameColors.white
            horizontalAlignment = SwingConstants.CENTER
            border = BorderFactory.createEmptyBorder(20, 0, 20, 0)
        }
        add(titleLabel, BorderLayout.NORTH)

        val buttonColumn = JPanel().apply {
            isOpaque = false
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = CENTER_ALIGNMENT
        }

        buttonColumn.add(Box.createVerticalStrut(200))

        val startButton = StartButton(
            backgroundColor = GameColors.jbRed,
            hoverColor = GameColors.jbOrange,
            text = startButtonText,
        ).apply {
            alignmentX = CENTER_ALIGNMENT
            addActionListener { onStart() }
        }

        val howToPlayButton = StartButton(
            backgroundColor = GameColors.jbPurple,
            hoverColor = GameColors.jbOrange,
            text = "How to play",
        ).apply {
            alignmentX = CENTER_ALIGNMENT
            addActionListener { showHowToPlayDialog() }
        }

        val openDiaryButton = StartButton(
            backgroundColor = GameColors.jbPurple,
            hoverColor = GameColors.jbOrange,
            text = "Open Diary",
        ).apply {
            alignmentX = CENTER_ALIGNMENT
            addActionListener {
                toolWindow.hide()
                showDiaryDialog()
            }
        }

        val creditsButton = StartButton(
            backgroundColor = GameColors.jbPurple,
            hoverColor = GameColors.jbOrange,
            text = "Credits",
        ).apply {
            alignmentX = CENTER_ALIGNMENT
            addActionListener {
                showCredits()
            }
        }

        buttonColumn.add(startButton)
        buttonColumn.add(Box.createVerticalStrut(12))
        buttonColumn.add(howToPlayButton)
        buttonColumn.add(Box.createVerticalStrut(12))
        buttonColumn.add(openDiaryButton)
        buttonColumn.add(Box.createVerticalStrut(12))
        buttonColumn.add(creditsButton)

        val buttonWrapper = JPanel(GridBagLayout()).apply {
            isOpaque = false
            add(buttonColumn)
        }
        add(buttonWrapper, BorderLayout.CENTER)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if(g is Graphics2D) {
            g.color = GameColors.black
            g.fillRect(0, 0, width, height)
            g.drawImage(PNG.START_BACKGROUND, (width / 2) - 256, (height / 2) - 450, 512, 512, null)
        }
    }

    private fun showHowToPlayDialog() {
        Messages.showInfoMessage(
            project,
            """
            How to play:
            - Move with W A S D.
            - Defeat enemies and collect the coins they drop.
            - After the top bar is filled choose your upgrade!
            - Mix and match upgrades to become strong enough to survive!
            - Survive as long as you can!
        """.trimIndent(),
            "How To Play IDE Survivors"
        )
    }

    private fun showCredits() {
        Messages.showInfoMessage(
            project,
            """
                This game was created by Alexander Chatzizacharias (https://github.com/glycin).
                Sponsored by JetBrains for the 25th anniversary of IntelliJ.
            """.trimIndent(),
            "Credits"
        )
    }

    private fun showDiaryDialog() {
        object : DialogWrapper(project, true) {
            private var wrapper : DialogueScreenWrapper? = null

            init {
                title = "My Diary"
                init()
            }

            override fun createCenterPanel(): JComponent {
                val saveState = service<GameSaveState>()
                val allEnemies = Enemy.getAllAsEntries(saveState)
                val projectScope = project.service<GameService>().getProjectScope()

                val tabbedPane = JBTabbedPane()
                val enemyAtlasPanel = EnemyAtlasPanel(allEnemies).apply {
                    layout = BoxLayout(this, BoxLayout.Y_AXIS)
                }

                val upgradesPanel = UpgradeAtlasPanel().apply {
                    layout = BoxLayout(this, BoxLayout.Y_AXIS)
                }

                val storyPanel = StoryPanel(
                    listOf(
                        StoryReplay("Replay intro", unlocked = saveState.dialoguesSeen >= 1) {
                            val dialogueScreen = DialogueScreen(
                                title = "Origins...",
                                texts = CutsceneTexts.screenOne,
                                scope = projectScope,
                                backGroundImages = mapOf(0 to PNG.STORY_SCREEN_1),
                                onReadyToStart = {
                                    wrapper?.enableOk()
                                }
                            )
                            wrapper = DialogueScreenWrapper(project, "Done!", dialogueScreen)
                            wrapper?.show()
                        },
                        StoryReplay("Replay level 1: 2001-2009", unlocked = saveState.dialoguesSeen >= 2) {
                            val dialogueScreen = DialogueScreen(
                                title = "When I was born...",
                                texts = CutsceneTexts.screenTwo,
                                scope = projectScope,
                                backGroundImages = mapOf(0 to PNG.STORY_SCREEN_2),
                                onReadyToStart = {
                                    wrapper?.enableOk()
                                }
                            )
                            wrapper = DialogueScreenWrapper(project, "Done!", dialogueScreen)
                            wrapper?.show()
                         },
                        StoryReplay("Replay level 2: 2010-2017", unlocked = saveState.dialoguesSeen >= 3) {
                            val dialogueScreen = DialogueScreen(
                                title = "My teenage years",
                                texts = CutsceneTexts.screenThree,
                                scope = projectScope,
                                backGroundImages = mapOf(0 to PNG.STORY_SCREEN_3),
                                onReadyToStart = { wrapper?.enableOk() }
                            )
                            wrapper = DialogueScreenWrapper(project, "Done!", dialogueScreen)
                            wrapper?.show()
                        },
                        StoryReplay("Replay level 3: 2018-2026", unlocked = saveState.dialoguesSeen >= 4) {
                            val dialogueScreen = DialogueScreen(
                                title = "Adulthood!",
                                texts = CutsceneTexts.screenFour,
                                scope = projectScope,
                                backGroundImages = mapOf(0 to PNG.STORY_SCREEN_4, 8 to PNG.STORY_SCREEN_5),
                                onReadyToStart = { wrapper?.enableOk() }
                            )
                            wrapper = DialogueScreenWrapper(project, "Done!", dialogueScreen)
                            wrapper?.show()
                        },
                        StoryReplay("Replay outro", unlocked = saveState.dialoguesSeen >= 4) {
                            val dialogueScreen = DialogueScreen(
                                title = "Party time!",
                                texts = CutsceneTexts.outro,
                                scope = projectScope,
                                backGroundImages = mapOf(0 to PNG.STORY_SCREEN_5),
                                onReadyToStart = { wrapper?.enableOk() }
                            )
                            wrapper = DialogueScreenWrapper(project, "Done!", dialogueScreen)
                            wrapper?.show()
                        }
                    )
                ).apply {
                    layout = BoxLayout(this, BoxLayout.Y_AXIS)
                }

                tabbedPane.addTab("The story", storyPanel)
                tabbedPane.addTab("Enemy Atlas", enemyAtlasPanel)
                tabbedPane.addTab("Upgrades", upgradesPanel)

                return tabbedPane
            }
        }.show()
    }
}