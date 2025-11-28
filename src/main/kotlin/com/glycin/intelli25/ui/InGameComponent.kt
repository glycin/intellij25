package com.glycin.intelli25.ui

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.GameGlobalState
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JComponent
import kotlin.math.max
import kotlin.math.roundToInt

private const val BAR_HEIGHT = 20

class InGameComponent(
    private val player: Player,
    private val ggState: GameGlobalState,
): JComponent() {

    private val scoreFont = Fonts.pixelFont.deriveFont(Font.BOLD, 20f)

    init {
        isOpaque = false
        setBounds(0, 0, ggState.maxX, ggState.maxY)
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        if(g is Graphics2D) {
            drawExpBar(g)
            drawPlayerHp(g)
            drawTime(g)
            drawScore(g)
            drawInventory(g)
        }
    }

    private fun drawExpBar(g: Graphics2D) {
        g.color = GameColors.white
        g.fillRect(0, 0, ggState.maxX, BAR_HEIGHT)
        g.color = GameColors.jbOrange
        val filledWidth = (player.experience / player.experienceNeeded) * ggState.maxX
        g.fillRect(0, 0, filledWidth.roundToInt(), BAR_HEIGHT)
    }

    private fun drawPlayerHp(g: Graphics2D) {
        val x = player.position.x.roundToInt()
        val y = player.position.y.roundToInt() + player.height + 10
        val hpWidth = max((player.currentHp.toFloat() / player.maxHp().toFloat()) * player.width, 0f).roundToInt()
        g.color = GameColors.jbRed
        g.fillRect(x, y, player.width, 5)
        g.color = GameColors.jbGreen
        g.fillRect(x, y, hpWidth, 5)
    }

    private fun drawTime(g: Graphics2D) {
        g.font = scoreFont
        g.color = GameColors.jbOrange
        val timeMs = ggState.elapsedTime
        val totalSeconds = timeMs / 1000
        val minutes  = totalSeconds / 60
        val seconds = totalSeconds % 60
        val timeText = "%02d:%02d".format(minutes, seconds)
        val metrics = g.getFontMetrics(scoreFont)
        val x = ggState.maxX - metrics.stringWidth(timeText) - 20
        val y = BAR_HEIGHT + metrics.height + 10
        g.drawString(timeText, x, y)
    }

    private fun drawScore(g: Graphics2D) {
        g.font = scoreFont
        g.color = GameColors.jbOrange
        val scoreText = "${ggState.score}"
        val metrics = g.getFontMetrics(scoreFont)
        val x = ggState.maxX - metrics.stringWidth(scoreText) - 20
        val y = BAR_HEIGHT + (metrics.height * 2) + 15
        g.drawString(scoreText, x, y)
    }

    private fun drawInventory(g: Graphics2D) {
        val startX = 10
        val startY = BAR_HEIGHT + 10
        val itemWidth = 25
        val itemHeight = 25

        player.upgrades.values.forEachIndexed { i, icon ->
            val x = startX + (i * (itemWidth + 10))
            g.color = GameColors.black
            g.fillRect(x - 2, startY - 2, itemWidth + 5, itemHeight + 5)
            g.color = GameColors.jbOrange
            g.drawRect(x - 2, startY - 2, itemWidth + 5, itemHeight + 5)
            g.drawImage(icon, x, startY, itemWidth, itemHeight, null)
        }
    }
}