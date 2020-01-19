package com.corsage.memory_matching.fragment

import androidx.fragment.app.Fragment
import com.corsage.memory_matching.Application

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * The base structure for fragment classes.
 * Sets up commonly used functionality.
 */

abstract class BaseFragment : Fragment() {
    abstract val TAG: String

    private val settings = Application.settingsManager

    override fun onStart() {
        super.onStart()
        themeUI(settings.nightMode)
    }

    abstract fun themeUI(isDark: Boolean)
}