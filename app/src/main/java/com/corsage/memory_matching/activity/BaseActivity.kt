package com.corsage.memory_matching.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.corsage.memory_matching.Application
import com.corsage.memory_matching.R

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * The base structure for activity classes.
 * Sets up commonly used functionality.
 */


abstract class BaseActivity : AppCompatActivity() {
    abstract val TAG: String

    private val settings = Application.settingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Only vertical orientation.
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Set the app theme.
        if (settings.nightMode) {
            setTheme(R.style.Theme_Corsage_Dark)
        } else {
            setTheme(R.style.Theme_Corsage_Light)
        }
    }

    override fun onStart() {
        super.onStart()
        themeUI(settings.nightMode)
    }

    abstract fun themeUI(isDark: Boolean)
}