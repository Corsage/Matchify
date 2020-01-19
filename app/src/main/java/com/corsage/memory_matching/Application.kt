package com.corsage.memory_matching

import android.app.Application
import com.corsage.memory_matching.local.SettingsManager
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Application class.
 * Used to initialize [Fresco] as well as [SettingsManager].
 */

class Application : Application() {

    companion object {
        lateinit var settingsManager: SettingsManager
    }

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)

        // Initialize settings manager.
        settingsManager = SettingsManager(this)

        if (settingsManager.nightMode) { setTheme(R.style.Theme_Corsage_Dark) }
        else { setTheme(R.style.Theme_Corsage_Light) }
    }

}