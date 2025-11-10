package com.glycin.intelli25.ui

import com.glycin.intelli25.model.Player
import com.glycin.intelli25.util.GameColors
import com.glycin.intelli25.util.GameGlobalState
import com.intellij.openapi.Disposable
import com.intellij.ui.JBColor
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JComponent
import kotlin.math.max
import kotlin.math.roundToInt

private const val barHeight = 20
class InGameComponent(
    private val player: Player,
    private val ggState: GameGlobalState,
): JComponent(), Disposable {

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
            drawScore(g)
        }
    }

    private fun drawExpBar(g: Graphics2D) {
        g.color = GameColors.white
        g.fillRect(0, 0, ggState.maxX, barHeight)
        g.color = JBColor.blue
        val filledWidth = (player.experience / player.experienceNeeded) * ggState.maxX
        g.fillRect(0, 0, filledWidth.roundToInt(), barHeight)
    }

    private fun drawPlayerHp(g: Graphics2D) {
        val x = player.position.x.roundToInt()
        val y = player.position.y.roundToInt() + player.height + 10
        val hpWidth = max((player.currentHp.toFloat() / player.maxHp().toFloat()) * player.width, 0f).roundToInt()
        g.color = GameColors.red
        g.fillRect(x, y, player.width, 5)
        g.color = GameColors.green
        g.fillRect(x, y, hpWidth, 5)
    }

    private fun drawScore(g: Graphics2D) {
        g.font = scoreFont
        g.color = GameColors.white
        val scoreText = "${ggState.score}"
        val metrics = g.getFontMetrics(scoreFont)
        val x = ggState.maxX - metrics.stringWidth(scoreText) - 20
        val y = barHeight + metrics.height + 10
        g.drawString(scoreText, x, y)
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }
}