package com.glycin.intelli25.model

import java.awt.image.BufferedImage

class Animation(
    val position: Vec2,
    private val sprites: List<BufferedImage>,
    private val loop: Boolean = false,
    private val frameDelay: Int = 12,
    private val onEnd: () -> Unit = {},
) {
    private var currentSprite : BufferedImage = sprites[0]
    private var currentAnimationIndex = 0
    private var skipFrameCount = 0

    fun getCurrentSprite() = currentSprite

    fun doAnimation() {
        skipFrameCount++
        if(skipFrameCount % frameDelay == 0) {
            currentAnimationIndex++
        }

        if(currentAnimationIndex >= sprites.size - 1) {
            if(loop){
                currentAnimationIndex = 0
                skipFrameCount = 0
            } else {
                onEnd()
            }
        }
        currentSprite = sprites[currentAnimationIndex]
    }
}