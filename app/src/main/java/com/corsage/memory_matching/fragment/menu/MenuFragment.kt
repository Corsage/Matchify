package com.corsage.memory_matching.fragment.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.corsage.memory_matching.R
import com.corsage.memory_matching.activity.GameActivity
import com.corsage.memory_matching.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_menu.*

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Menu Fragment.
 * This is the main menu screen.
 */

class MenuFragment : BaseFragment() {
    override val TAG = "MenuFragment"

    private lateinit var viewsToFadeIn: ArrayList<View>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animateButtons()

        menu_btn_play.setOnClickListener {
            startActivity(Intent(requireContext(), GameActivity::class.java))
        }

        menu_btn_help.setOnClickListener {
            val manager = requireActivity().supportFragmentManager
            val ft = manager.beginTransaction()
            ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out,
                R.anim.fade_in, R.anim.fade_out)
            ft.replace(R.id.menu_fragment_container, HelpFragment())
            ft.addToBackStack("help")
            ft.commit()
        }

        menu_btn_settings.setOnClickListener {
            val manager = requireActivity().supportFragmentManager
            val ft = manager.beginTransaction()
            ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out,
                R.anim.fade_in, R.anim.fade_out)
            ft.replace(R.id.menu_fragment_container, SettingsFragment())
            ft.addToBackStack("settings")
            ft.commit()
        }
    }

    private fun animateButtons() {
        viewsToFadeIn = arrayListOf(menu_btn_play, menu_btn_settings, menu_btn_help, menu_img_logo, menu_title)

        for (v: View in viewsToFadeIn) {
            v.alpha = 0f
        }

        for (v: View in viewsToFadeIn) {
            v.animate().alpha(1.0f).setDuration(2000).start()
        }
    }

    override fun themeUI(isDark: Boolean) {
        // ...
    }
}