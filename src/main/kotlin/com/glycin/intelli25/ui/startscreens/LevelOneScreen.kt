package com.glycin.intelli25.ui.startscreens

import com.glycin.intelli25.GameService
import com.glycin.intelli25.persistence.GameSaveState
import com.glycin.intelli25.ui.Fonts
import com.glycin.intelli25.ui.PNG
import com.glycin.intelli25.ui.StartButton
import com.glycin.intelli25.util.GameColors
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GridBagLayout
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.SwingConstants

class LevelOneScreen(
    private val project: Project,
    private val toolWindow: ToolWindow,
    private val saveState: GameSaveState,
): JPanel() {
    init{
        isOpaque = false
        layout = BorderLayout()

        val baseContent = LevelOneScreenContent {
            if(saveState.dialoguesSeen == 0) {
                val projectScope = project.service<GameService>().getProjectScope()
                val dialogueScreen = LevelOneDialogueScreen(projectScope) {
                    toolWindow.hide()
                    saveState.dialoguesSeen++
                }
                remove(it)
                add(dialogueScreen)
                revalidate()
                repaint()
            } else {
                //TODO: Start game
                toolWindow.hide()
            }
        }

        add(baseContent)
    }
}

private class LevelOneScreenContent(
    private val onStart: (JPanel) -> Unit,
): JPanel() {

    init {
        isOpaque = false
        background = GameColors.white
        layout = BorderLayout()

        val titleLabel = JLabel("Runee, the IJ Survivor").apply {
            font = Fonts.pixelFont.deriveFont(24.0f)
            foreground = GameColors.jb2001Blue
            horizontalAlignment = SwingConstants.CENTER
            border = BorderFactory.createEmptyBorder(50, 0, 0, 0)
        }
        add(titleLabel, BorderLayout.NORTH)

        val buttonPanel = JPanel(GridBagLayout()).apply {
            isOpaque = false
        }
        val startButton = StartButton(
            backgroundColor = GameColors.jb2001Blue,
            hoverColor = GameColors.jb2001Orange,
            text = "Start Game",
        ).apply {
            addActionListener {
                onStart(this@LevelOneScreenContent)
            }
        }
        buttonPanel.add(startButton)
        add(buttonPanel, BorderLayout.CENTER)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if(g is Graphics2D) {
            val standardSize = 20
            g.color = GameColors.white
            g.fillRect(0, 0, width, height)
            g.color = GameColors.black
            g.font = Font("Monaco", Font.PLAIN, 10)
            g.drawString("IntelliJ IDEA - [D:\\dev\\runee_survivor.ipr] - D:\\dev\\runee_survivor\\src\\Main.java", 5, 10)

            g.font = Font("Courier New", Font.ITALIC, 14)
            g.color = Color.lightGray
            val commentLines = listOf(
                "/*",
                " * Created by IntelliJ IDEA",
                " * To change template for new class use",
                " * \"Source Code\" options (Tools | IDE Options), Template tab.",
                " */",
                "",
            )
            val fontMetrics = g.fontMetrics
            var y = 80 + fontMetrics.ascent
            for (line in commentLines) {
                g.drawString(line, 40, y)
                y += fontMetrics.height
            }

            g.font = Font("Courier New", Font.BOLD, 14)
            g.color = Color(48,48,152)
            val startY = y + fontMetrics.ascent
            g.drawString("public class", 40, startY)
            g.drawString("  public static void", 40, startY + fontMetrics.height)

            g.font = Font("Courier New", Font.PLAIN, 14)
            g.color = GameColors.black
            g.drawString("             Main {", 40, startY)
            g.drawString("                     main(String[] args) {", 40, startY + fontMetrics.height)
            g.drawString("      System.out.println(                              );", 40, startY + (fontMetrics.height * 2))
            g.drawString("  )", 40, startY + (fontMetrics.height * 3))
            g.drawString("}", 40, startY + (fontMetrics.height * 4))

            g.color = Color(46, 151, 46)
            g.drawString("                         \"HELP ME, I AM STUCK IN HERE\"", 40, startY + (fontMetrics.height * 2))

            g.color = GameColors.jb2001Grey
            g.fillRect(width - standardSize, 40, standardSize, height)
            g.fillRect(0, 40, standardSize, height)
            g.fillRect(0, height - standardSize, width, standardSize)
            g.fillRect(0, 20, width, standardSize)

            g.drawImage(PNG.firstLogo, (width / 2) - 64, height - 256, 128, 128, null)
        }
    }
}