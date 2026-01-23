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
) : JComponent() {

    private val scoreFont = Fonts.pixelFont.deriveFont(Font.BOLD, 20f)

    // Use smaller font size for the controls
    private val controlsFont = Fonts.pixelFont.deriveFont(Font.BOLD, 15f)

    private data class LabelValuePair(val label: String, val value: String)

    init {
        isOpaque = false
        setBounds(0, 0, ggState.maxX, ggState.maxY)
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        if (g is Graphics2D) {
            drawExpBar(g)
            drawPlayerHp(g)
            drawInventory(g)

            drawTopRightCornerUI(g)
        }
    }

    private fun drawExpBar(g: Graphics2D) {
        g.color = GameColors.white
        g.fillRect(0, 0, ggState.maxX, BAR_HEIGHT)
        g.color = GameColors.jbOrange
        val filledWidth = (player.experience / player.experienceNeeded) * ggState.maxX
        g.fillRect(0, 0, filledWidth.roundToInt(), BAR_HEIGHT)
    }

    private fun drawLabelWithValue(
        g: Graphics2D,
        label: String,
        value: String,
        leftBorder: Int,
        valueColumnStart: Int,
        y: Int
    ) {
        g.drawString("$label:", leftBorder, y)
        g.drawString(value, valueColumnStart, y)
    }

    private data class ColumnPositions(val leftBorder: Int, val valueColumnStart: Int)

    private fun calculateColumnPositions(g: Graphics2D, pairs: List<LabelValuePair>): ColumnPositions {
        val metrics = g.fontMetrics
        val maxLabelWidth = pairs.maxOf { metrics.stringWidth("${it.label}:") }
        val maxValueWidth = pairs.maxOf { metrics.stringWidth(it.value) }
        val leftBorder = ggState.maxX - 20 - maxLabelWidth - metrics.stringWidth(" ") - maxValueWidth
        val valueColumnStart = leftBorder + maxLabelWidth + metrics.stringWidth(" ")
        return ColumnPositions(leftBorder, valueColumnStart)
    }

    private fun getTimeValue(): String {
        val maxTimeMs = ggState.gameDuration
        val elapsedMs = ggState.elapsedTime
        val remainingMs = (maxTimeMs - elapsedMs).coerceAtLeast(0L)
        val totalSeconds = remainingMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    private fun getScoreAndTimePairs(): List<LabelValuePair> {
        return listOf(
            LabelValuePair("Time", getTimeValue()),
            LabelValuePair("Score", "${ggState.score}")
        )
    }

    private fun getControlPairs(): List<LabelValuePair> {
        return listOf(
            LabelValuePair("Move", "WASD/Arrows"),
            LabelValuePair("Pause", "ESC")
        )
    }

    private fun drawPlayerHp(g: Graphics2D) {
        val x = player.position.x.roundToInt() - ggState.minX
        val y = (player.position.y.roundToInt() + player.height + 10) - ggState.minY
        val hpWidth = max((player.currentHp.toFloat() / player.maxHp().toFloat()) * player.width, 0f).roundToInt()
        g.color = GameColors.jbRed
        g.fillRect(x, y, player.width, 5)
        g.color = GameColors.jbGreen
        g.fillRect(x, y, hpWidth, 5)
    }

    private fun drawLabelValuePairs(
        g: Graphics2D,
        pairs: List<LabelValuePair>,
        columns: ColumnPositions,
        startY: Int
    ): Int {
        val metrics = g.fontMetrics
        var y = startY

        pairs.forEach { (label, value) ->
            drawLabelWithValue(g, label, value, columns.leftBorder, columns.valueColumnStart, y)
            y += metrics.height + 5
        }

        return y
    }

    private fun drawTimeAndScore(
        g: Graphics2D,
        timeAndScorePairs: List<LabelValuePair>,
        columns: ColumnPositions
    ): Int {
        g.font = scoreFont
        g.color = GameColors.jbOrange

        val startY = BAR_HEIGHT + g.fontMetrics.height + 10

        return drawLabelValuePairs(g, timeAndScorePairs, columns, startY)
    }

    private fun drawControls(g: Graphics2D, controlPairs: List<LabelValuePair>, columns: ColumnPositions, startY: Int) {
        g.font = controlsFont
        g.color = GameColors.jbOrange

        drawLabelValuePairs(g, controlPairs, columns, startY)
    }

    private fun drawTopRightCornerUI(g: Graphics2D) {
        val scoreAndTimePairs = getScoreAndTimePairs()
        val controlPairs = getControlPairs()

        // Calculate max label width across both sections for left alignment
        g.font = scoreFont
        val scoreFontMetrics = g.fontMetrics
        val scoreMaxLabelWidth = scoreAndTimePairs.maxOf { scoreFontMetrics.stringWidth("${it.label}:") }

        g.font = controlsFont
        val controlsFontMetrics = g.fontMetrics
        val controlsMaxLabelWidth = controlPairs.maxOf { controlsFontMetrics.stringWidth("${it.label}:") }

        // Use the maximum label width for alignment
        val maxLabelWidth = maxOf(scoreMaxLabelWidth, controlsMaxLabelWidth)

        // Calculate value widths separately for each section
        val scoreMaxValueWidth = scoreAndTimePairs.maxOf { scoreFontMetrics.stringWidth(it.value) }
        val controlsMaxValueWidth = controlPairs.maxOf { controlsFontMetrics.stringWidth(it.value) }

        // Use the largest value width to determine the left border
        val maxValueWidth = maxOf(scoreMaxValueWidth, controlsMaxValueWidth)
        val scoreSpaceWidth = scoreFontMetrics.stringWidth(" ")
        val controlsSpaceWidth = scoreSpaceWidth

        val leftBorder = ggState.maxX - 20 - maxLabelWidth - scoreSpaceWidth - maxValueWidth

        // Create separate column positions for each section - values aligned separately
        val scoreColumns = ColumnPositions(leftBorder, leftBorder + maxLabelWidth + scoreSpaceWidth)
        val controlsColumns = ColumnPositions(leftBorder, leftBorder + maxLabelWidth + controlsSpaceWidth)

        val yAfterScore = drawTimeAndScore(g, scoreAndTimePairs, scoreColumns)
        drawControls(g, controlPairs, controlsColumns, yAfterScore)
    }

    private fun drawInventory(g: Graphics2D) {
        val startX = 10
        val startY = BAR_HEIGHT + 10
        val itemWidth = 25
        val itemHeight = 25

        player.upgrades.values.forEachIndexed { i, item ->
            val x = startX + (i * (itemWidth + 10))
            g.color = GameColors.black
            g.fillRect(x - 2, startY - 2, itemWidth + 5, itemHeight + 5)
            g.color = GameColors.jbOrange
            g.drawRect(x - 2, startY - 2, itemWidth + 5, itemHeight + 5)
            g.drawImage(item.image, x, startY, itemWidth, itemHeight, null)
            g.font = scoreFont
            g.color = GameColors.white
            g.drawString("x${item.count}", x, startY + itemHeight + 25)
        }
    }
}