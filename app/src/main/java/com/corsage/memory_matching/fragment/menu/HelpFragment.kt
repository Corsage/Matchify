package com.corsage.memory_matching.fragment.menu

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.corsage.memory_matching.R
import com.corsage.memory_matching.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_menu_help.*

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Help Fragment.
 * This is the help screen.
 */

class HelpFragment : BaseFragment() {
    override val TAG = "HelpFragment"

    private lateinit var rules: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rules = requireContext().resources.getStringArray(R.array.menu_help_rules)

        for (s: String in rules) {
            val tv = TextView(requireContext())
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)

            tv.text = s
            tv.typeface = Typeface.DEFAULT_BOLD
            tv.textSize = 18f
            tv.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))

            layoutParams.bottomMargin = 16

            tv.layoutParams = layoutParams

            menu_help_layout.addView(tv)
        }

    }

    override fun themeUI(isDark: Boolean) {
        // ...
    }
}