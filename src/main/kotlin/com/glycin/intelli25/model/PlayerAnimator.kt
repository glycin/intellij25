package com.glycin.intelli25.model

import com.glycin.intelli25.util.SpriteSheetImageLoader
import java.awt.image.BufferedImage

private const val STANDARD_CELL_WIDTH = 40
private const val STANDARD_CELL_HEIGHT = 40
private const val BASE_PATH = "/sprites"

class PlayerAnimator {

    private val idle = SpriteSheetImageLoader.loadSprites("$BASE_PATH/pip-sitting.png", STANDARD_CELL_WIDTH, STANDARD_CELL_HEIGHT, 8)
    private val walking = SpriteSheetImageLoader.loadSprites("$BASE_PATH/pip-walk.png", STANDARD_CELL_WIDTH, STANDARD_CELL_HEIGHT, 8)

    private var currentSprite : BufferedImage = idle[0]
    private var currentAnimationIndex = 0
    private var skipFrameCount = 0

    fun animate(state: PlayerState) {
        when(state) {
            PlayerState.WALK -> showAnimation(walking)
            PlayerState.IDLE -> showAnimation(idle)
        }
    }

    fun getCurrentSprite() = currentSprite

    private fun showAnimation(sprites: List<BufferedImage>, frameDelay: Int = 12) {
        skipFrameCount++
        if(skipFrameCount % frameDelay == 0) {
            currentAnimationIndex++
        }

        if(currentAnimationIndex >= sprites.size - 1) {
            currentAnimationIndex = 0
            skipFrameCount = 0
        }
        currentSprite = sprites[currentAnimationIndex]
    }
}