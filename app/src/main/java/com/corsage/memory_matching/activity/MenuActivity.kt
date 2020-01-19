package com.corsage.memory_matching.activity

import android.os.Bundle
import com.corsage.memory_matching.R
import com.corsage.memory_matching.ext.addFragment
import com.corsage.memory_matching.fragment.menu.MenuFragment
import kotlinx.android.synthetic.main.activity_menu.*


/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Menu Activity.
 * This activity will hold all menu-related fragments.
 */

class MenuActivity : BaseActivity() {
    override val TAG = "MenuActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        addFragment(MenuFragment(), R.id.menu_fragment_container)
    }

    override fun themeUI(isDark: Boolean) {
        if (isDark) menu_fragment_container.setBackgroundResource(R.drawable.bg_gradient_dark)
        else menu_fragment_container.setBackgroundResource(R.drawable.bg_gradient)
    }
}
