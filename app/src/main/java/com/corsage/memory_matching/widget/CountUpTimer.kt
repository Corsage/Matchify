package com.corsage.memory_matching.widget

import android.os.CountDownTimer

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Count Up Timer.
 * A timer that counts up to a [duration], derived from [CountDownTimer].
 */

abstract class CountUpTimer(protected var duration: Long, private val timeElapsed: Int = 0) : CountDownTimer(duration - timeElapsed * 1000, INTERVAL) {
    var second = 0

    companion object {
        const val INTERVAL: Long = 1000
    }

    init {
        // Lower duration has previous time has elapsed.
        duration -= timeElapsed * 1000
    }

    abstract fun onTick(second: Int)

    override fun onTick(millisUntilFinished: Long) {
        // Offset the seconds passed by the time elapsed.
        second = ((duration - millisUntilFinished + timeElapsed * 1000) / INTERVAL).toInt()
        onTick(second)
    }

    override fun onFinish() {
        onTick((duration / INTERVAL).toInt())
    }
}
