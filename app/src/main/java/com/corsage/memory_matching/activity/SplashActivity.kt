package com.corsage.memory_matching.activity

import android.content.Intent
import android.os.Bundle
import com.corsage.memory_matching.R
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Splash Activity.
 * This activity is used to just display the splash screen.
 */

class SplashActivity : BaseActivity() {
    override val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }

    override fun themeUI(isDark: Boolean) {
        if (isDark) splash_container.setBackgroundResource(R.drawable.bg_gradient_dark)
        else splash_container.setBackgroundResource(R.drawable.bg_gradient)
    }
}
