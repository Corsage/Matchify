package com.corsage.memory_matching.activity

import android.os.Bundle
import com.corsage.memory_matching.R
import com.corsage.memory_matching.ext.addFragment
import com.corsage.memory_matching.fragment.game.GameFragment
import kotlinx.android.synthetic.main.activity_game.*

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Game Activity.
 * This activity will hold all game-related fragments.
 */

class GameActivity : BaseActivity() {
    override val TAG = "GameActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        addFragment(GameFragment(), R.id.game_fragment_container)
    }

    override fun themeUI(isDark: Boolean) {
        if (isDark) game_fragment_container.setBackgroundResource(R.drawable.bg_gradient_dark)
        else game_fragment_container.setBackgroundResource(R.drawable.bg_gradient)
    }
}
