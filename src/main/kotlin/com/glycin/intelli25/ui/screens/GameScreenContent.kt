package com.glycin.intelli25.ui.screens

import com.glycin.intelli25.GameService
import com.glycin.intelli25.model.Enemy
import com.glycin.intelli25.model.StoryReplay
import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.ui.CreditsDialog
import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.ui.GameButton
import com.glycin.intelli25.ui.scaledToJbZoomLevel
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.PNG
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.JBColor
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GridBagLayout
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class GameScreenContent(
    private val project: Project,
    private val toolWindow: ToolWindow,
    private val saveState: GameSaveState,
    onStartOne: () -> Unit,
    onStartTwo: () -> Unit,
    onStartThree: () -> Unit,
): JPanel() {

    private val level1Btn: GameButton
    private val level2Btn: GameButton
    private val level3Btn: GameButton

    init {
        isOpaque = false
        layout = GridBagLayout()
        addDebugBorder(JBColor.RED)

        val contentContainer = JPanel().apply {
            isOpaque = false
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = CENTER_ALIGNMENT
            addDebugBorder(JBColor.GREEN)
        }

        contentContainer.add(Box.createVerticalGlue())

        val titleLabel = JLabel("IDE SURVIVORS").apply {
            font = Fonts.pixelFont.scaledToJbZoomLevel(42.0f)
            foreground = GameColors.jbGreen
            alignmentX = CENTER_ALIGNMENT
            horizontalAlignment = SwingConstants.CENTER
            addDebugBorder(JBColor.BLUE)
        }
        contentContainer.add(titleLabel)
        contentContainer.add(Box.createVerticalStrut(5))

        val imagePanel = object : JPanel() {

            private val targetW = 512
            private val targetH = 351
            private val aspectRatio = targetW.toDouble() / targetH

            init {
                isOpaque = false
                preferredSize = Dimension(targetW, targetH)
                maximumSize = Dimension(targetW, targetH)
                minimumSize = Dimension(targetW, targetH)
                alignmentX = CENTER_ALIGNMENT
                addDebugBorder(JBColor.CYAN)
            }

            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                var drawW = width
                var drawH = (width / aspectRatio).toInt()
                if (drawH > height) {
                    drawH = height
                    drawW = (height * aspectRatio).toInt()
                }

                val x = (width - drawW) / 2
                val y = (height - drawH) / 2

                g.drawImage(PNG.START_BACKGROUND, x, y, drawW, drawH, null)
            }
        }
        contentContainer.add(imagePanel)
        contentContainer.add(Box.createVerticalStrut(5))

        val levelLabel = JLabel("SELECT LEVEL").apply {
            font = Fonts.jbMono.scaledToJbZoomLevel(20.0f)
            foreground = GameColors.jbPurple
            alignmentX = CENTER_ALIGNMENT
            addDebugBorder(JBColor.MAGENTA)
        }
        contentContainer.add(levelLabel)

        contentContainer.add(Box.createVerticalStrut(10))

        level1Btn = createSquareLevelButton("1", onStartOne)
        level2Btn = createSquareLevelButton("2", onStartTwo)
        level3Btn = createSquareLevelButton("3", onStartThree)

        val levelRow = JPanel().apply {
            isOpaque = false
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            addDebugBorder(JBColor.YELLOW)
            add(level1Btn)
            add(Box.createHorizontalStrut(15))
            add(level2Btn)
            add(Box.createHorizontalStrut(15))
            add(level3Btn)
        }
        levelRow.alignmentX = CENTER_ALIGNMENT
        contentContainer.add(levelRow)

        contentContainer.add(Box.createVerticalStrut(40))

        val footerPanel = JPanel().apply {
            isOpaque = false
            layout = FlowLayout(FlowLayout.CENTER, 15, 0)
            alignmentX = CENTER_ALIGNMENT

            addDebugBorder(JBColor.ORANGE)
            add(createFooterButton("How to play") { showHowToPlayDialog() })
            add(createFooterButton("Diary") {
                toolWindow.hide()
                showDiaryDialog()
            })
            add(createFooterButton("Credits") { showCredits() })
        }
        contentContainer.add(footerPanel)

        contentContainer.add(Box.createVerticalGlue())

        add(contentContainer)

        refreshState()
    }

    fun refreshState() {
        level2Btn.isEnabled = saveState.levelsBeaten >= 1
        level2Btn.filled = saveState.levelsBeaten >= 1
        level3Btn.isEnabled = saveState.levelsBeaten >= 2
        level3Btn.filled = saveState.levelsBeaten >= 2
    }

    private fun createSquareLevelButton(text: String, action: () -> Unit): GameButton {
        return GameButton(
            backgroundColor = GameColors.jbPurple,
            hoverColor = GameColors.jbOrange,
            text = text,
            preferredWidth = 65,
            preferredHeight = 65,
        ).apply {
            font = Fonts.jbMono.scaledToJbZoomLevel(20.0f)
            addActionListener { action() }
            addDebugBorder(JBColor.PINK)
        }
    }

    private fun createFooterButton(text: String, action: () -> Unit): JButton {
        return GameButton(
            backgroundColor = GameColors.jbBlue,
            hoverColor = GameColors.jbOrange,
            text = text,
            preferredWidth = 160,
            preferredHeight = 60,
        ).apply {
            font = Fonts.jbMono.scaledToJbZoomLevel(18.0f)
            addActionListener { action() }
            addDebugBorder(JBColor.WHITE)
        }
    }

    private fun JComponent.addDebugBorder(color: Color) {
        val debugBorderWidth = 0
        @Suppress("KotlinConstantConditions")
        if (debugBorderWidth > 0) {
            border = BorderFactory.createLineBorder(color, debugBorderWidth)
        }
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (g is Graphics2D) {
            g.color = GameColors.black
            g.fillRect(0, 0, width, height)
        }
    }

    private fun showHowToPlayDialog() {
        Messages.showInfoMessage(
            project,
            """
                - Move with W A S D.
                - Defeat enemies and collect the coins they drop.
                - After the experience bar is filled, choose an upgrade.
                - Mix and match upgrades to become strong enough to survive.
                - Press escape during the game to pause.
                - Survive!
        """.trimIndent(),
            "How To Play IDE SURVIVORS"
        )
    }

    private fun showCredits() {
        CreditsDialog(project).show()
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
                        StoryReplay("Intro", unlocked = saveState.dialoguesSeen >= 1) {
                            val dialogueScreen = DialogueScreen(
                                title = "Introduction...",
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
                        StoryReplay("2001-2009", unlocked = saveState.dialoguesSeen >= 2) {
                            val dialogueScreen = DialogueScreen(
                                title = "2001-2009",
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
                        StoryReplay("2010-2017", unlocked = saveState.dialoguesSeen >= 3) {
                            val dialogueScreen = DialogueScreen(
                                title = "2010-2017",
                                texts = CutsceneTexts.screenThree,
                                scope = projectScope,
                                backGroundImages = mapOf(0 to PNG.STORY_SCREEN_3),
                                onReadyToStart = { wrapper?.enableOk() }
                            )
                            wrapper = DialogueScreenWrapper(project, "Done!", dialogueScreen)
                            wrapper?.show()
                        },
                        StoryReplay("2018-2026", unlocked = saveState.dialoguesSeen >= 4) {
                            val dialogueScreen = DialogueScreen(
                                title = "2018-2026",
                                texts = CutsceneTexts.screenFour,
                                scope = projectScope,
                                backGroundImages = mapOf(0 to PNG.STORY_SCREEN_4, 4 to PNG.STORY_SCREEN_5),
                                onReadyToStart = { wrapper?.enableOk() }
                            )
                            wrapper = DialogueScreenWrapper(project, "Done!", dialogueScreen)
                            wrapper?.show()
                        },
                        StoryReplay("Outro", unlocked = saveState.dialoguesSeen >= 4) {
                            val dialogueScreen = DialogueScreen(
                                title = "Outro...",
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
                tabbedPane.addTab("Enemy atlas", enemyAtlasPanel)
                tabbedPane.addTab("Upgrades", upgradesPanel)

                return tabbedPane
            }
        }.show()
    }
}