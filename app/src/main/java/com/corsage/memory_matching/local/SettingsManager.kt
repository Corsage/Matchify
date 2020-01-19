package com.corsage.memory_matching.local

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.corsage.memory_matching.ext.unitify
import com.corsage.memory_matching.model.collection.GameMode

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Settings Manager.
 * A SharedPreferences manager that takes care of various settings.
 */

class SettingsManager(context: Context) {
    private val NAME = "settings_storage"

    private val KEY_GRID_SIZE = "grid_size"
    private val KEY_MATCH_COUNT = "match_count"
    private val KEY_NIGHT_MODE = "night_mode"
    private val KEY_GAME_MODE = "game_mode"
    private val KEY_VIEW_TIME = "view_time"
    private val KEY_LOSE_TIME = "lose_TIME"

    private val prefs: SharedPreferences

    init {
        prefs = context.getSharedPreferences(NAME, Application.MODE_PRIVATE)
    }

    /**
     * Time limit (default: 12000ms).
     */
    var timeLimit: Int
        set(value) = prefs.edit().putInt(KEY_LOSE_TIME, value * 1000).commit().unitify()
        get() = prefs.getInt(KEY_LOSE_TIME, 120000)

    /**
     * Amount of time to view the cards (default:  3000ms).
     */
    var viewTime: Int
        set(value) = prefs.edit().putInt(KEY_VIEW_TIME, value * 1000).commit().unitify()
        get() = prefs.getInt(KEY_VIEW_TIME, 3000)

    /**
     * Type of game mode (default: NORMAL).
     */
    var gameMode: Int
        set(value) = prefs.edit().putInt(KEY_GAME_MODE, value).commit().unitify()
        get() = prefs.getInt(KEY_GAME_MODE, GameMode.NORMAL.id)

    /**
     * Total amount of grid items (default: 20).
     */
    var gridSize: Int
        set(value) = prefs.edit().putInt(KEY_GRID_SIZE, value).commit().unitify()
        get() = prefs.getInt(KEY_GRID_SIZE, 20)

    /**
     * Number of products to match (default: 2).
     */
    var matchCount: Int
        set(value) = prefs.edit().putInt(KEY_MATCH_COUNT, value).commit().unitify()
        get() = prefs.getInt(KEY_MATCH_COUNT, 2)

    /**
     * Night theme.
     */
    var nightMode: Boolean
        set(value) = prefs.edit().putBoolean(KEY_NIGHT_MODE, value).commit().unitify()
        get() = prefs.getBoolean(KEY_NIGHT_MODE, false)

    /**
     * Clear all settings.
     */
    fun clear() {
        prefs.edit().clear().apply()
    }
}