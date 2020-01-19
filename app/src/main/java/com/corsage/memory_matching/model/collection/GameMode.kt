package com.corsage.memory_matching.model.collection

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Game Mode.
 * A enumeration class containing all possible game modes.
 */

enum class GameMode(val id: Int) {
    NORMAL(0),
    HARD(1),
    TIME_TRIAL(2);

    override fun toString(): String = when (id) {
        0 -> "Normal"
        1 -> "Hard"
        2 ->  "Time Trial"
        else -> "Normal"
    }

    companion object {
        /**
         * Tries to find a GameMode object with a given [value].
         * @return a GameMode? object.
         */
        fun from(value: Int): GameMode? {
            return values().find { it.id == value }
        }
    }
}