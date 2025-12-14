package com.glycin.intelli25.model

import com.glycin.intelli25.util.PNG

class PlayerAnimator {

    private val walk = PNG.RUNZO_WALK
    private val idle = PNG.RUNZO
    private val hurt = PNG.RUNZO_HURT

    var currentSprite = PNG.RUNZO
        private set

    fun animate(state: PlayerState) {
        currentSprite = when(state) {
            PlayerState.WALK -> walk
            PlayerState.IDLE -> idle
            PlayerState.HURT -> hurt
        }
    }
}