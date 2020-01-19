package com.corsage.memory_matching.fragment.menu

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.corsage.memory_matching.Application
import com.corsage.memory_matching.R
import com.corsage.memory_matching.fragment.BaseFragment
import com.corsage.memory_matching.model.collection.GameMode
import com.corsage.memory_matching.util.Utils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_menu_settings.*

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Settings Fragment.
 * This is the settings screen.
 */

class SettingsFragment : BaseFragment() {
    override val TAG = "SettingsFragment"

    private val settings = Application.settingsManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showCurrentSettings()

        settings_switch_night.setOnCheckedChangeListener { _, isChecked ->
            settings.nightMode = isChecked
            Utils.restartApplication(requireActivity())
        }

        settings_night.setOnClickListener {
            settings_switch_night.isChecked = !settings.nightMode
        }

        settings_grid_size.setOnClickListener {
            val editText = EditText(requireContext())
            editText.setRawInputType(InputType.TYPE_CLASS_NUMBER)

            MaterialAlertDialogBuilder(context)
                .setTitle(requireContext().resources.getString(R.string.settings_grid_size))
                .setMessage(requireContext().resources.getString(R.string.settings_desc_grid_size))
                .setView(editText)
                .setPositiveButton(resources.getString(R.string.menu_save)) { _, _ ->
                    val num = editText.text.toString().toIntOrNull()

                    if (num == null) {
                        Toast.makeText(requireContext(), resources.getString(R.string.error_number_positive), Toast.LENGTH_LONG).show()
                    } else if (num % settings.matchCount != 0) {
                        Toast.makeText(requireContext(), resources.getString(R.string.settings_grid_divisible), Toast.LENGTH_LONG).show()
                    } else if (num < settings.matchCount) {
                        Toast.makeText(requireContext(), resources.getString(R.string.settings_grid_bound_low), Toast.LENGTH_LONG).show()
                    } else if (num > 100) {
                        Toast.makeText(requireContext(), resources.getString(R.string.settings_grid_bound_high), Toast.LENGTH_LONG).show()
                    } else {
                        settings.gridSize = num
                        updateSettings()
                    }
                }
                .show()
        }

        settings_match_count.setOnClickListener {
            val editText = EditText(requireContext())
            editText.setRawInputType(InputType.TYPE_CLASS_NUMBER)

            MaterialAlertDialogBuilder(context)
                .setTitle(requireContext().resources.getString(R.string.settings_match_count))
                .setMessage(requireContext().resources.getString(R.string.settings_desc_match_count))
                .setView(editText)
                .setPositiveButton(resources.getString(R.string.menu_save)) { _, _ ->
                    val num = editText.text.toString().toIntOrNull()

                    if (num == null) {
                        Toast.makeText(requireContext(), resources.getString(R.string.error_number), Toast.LENGTH_LONG).show()
                    } else if (num <= 1) {
                        Toast.makeText(requireContext(), resources.getString(R.string.settings_match_bound_low), Toast.LENGTH_LONG).show()
                    } else if (num > 10) {
                        Toast.makeText(requireContext(), resources.getString(R.string.settings_match_bound_high), Toast.LENGTH_LONG).show()
                    } else if (settings.gridSize % num != 0) {
                        // Update the grid size as well.
                        Toast.makeText(requireContext(), resources.getString(R.string.settings_match_grid), Toast.LENGTH_LONG).show()

                        // Default to 10 pairs.
                        settings.gridSize = num * 10
                        settings.matchCount = num
                        updateSettings(false)
                    } else {
                        settings.matchCount = num
                        updateSettings()
                    }
                }
                .show()
        }

        settings_view_time.setOnClickListener {
            val editText = EditText(requireContext())
            editText.setRawInputType(InputType.TYPE_CLASS_NUMBER)

            MaterialAlertDialogBuilder(context)
                .setTitle(requireContext().resources.getString(R.string.settings_view_time))
                .setMessage(requireContext().resources.getString(R.string.settings_desc_view_time))
                .setView(editText)
                .setPositiveButton(resources.getString(R.string.menu_save)) { _, _ ->
                    val num = editText.text.toString().toIntOrNull()

                    if (num == null) {
                        Toast.makeText(requireContext(), resources.getString(R.string.error_number), Toast.LENGTH_LONG).show()
                    }else if (num < 0) {
                        Toast.makeText(requireContext(), resources.getString(R.string.settings_view_bound_low), Toast.LENGTH_LONG).show()
                    } else if (num > 10) {
                        Toast.makeText(requireContext(), resources.getString(R.string.settings_view_bound_high), Toast.LENGTH_LONG).show()
                    } else {
                        settings.viewTime = num
                        updateSettings()
                    }
                }
                .show()
        }

        settings_game_mode.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle(requireContext().resources.getString(R.string.settings_game_mode))
                .setItems(arrayOf(GameMode.NORMAL.toString(), GameMode.HARD.toString(), GameMode.TIME_TRIAL.toString())) {
                    dialog, which  ->  settings.gameMode = which
                    updateSettings()
                    dialog.dismiss()
                }
                .show()

        }

        settings_lose_time.setOnClickListener {
            val editText = EditText(requireContext())
            editText.setRawInputType(InputType.TYPE_CLASS_NUMBER)

            MaterialAlertDialogBuilder(context)
                .setTitle(requireContext().resources.getString(R.string.settings_lose_time))
                .setMessage(requireContext().resources.getString(R.string.settings_desc_lose_time))
                .setView(editText)
                .setPositiveButton(resources.getString(R.string.menu_save)) { _, _ ->
                    val num = editText.text.toString().toIntOrNull()

                    if (num == null) {
                        Toast.makeText(requireContext(), resources.getString(R.string.error_number), Toast.LENGTH_LONG).show()
                    } else if (num < 0) {
                        Toast.makeText(requireContext(), resources.getString(R.string.settings_time_bound_low), Toast.LENGTH_LONG).show()
                    } else if (num > 300) {
                        Toast.makeText(requireContext(), resources.getString(R.string.settings_time_bound_high), Toast.LENGTH_LONG).show()
                    } else {
                        settings.timeLimit = num
                        updateSettings()
                    }
                }
                .show()
        }
    }

    private fun updateSettings(showMessage: Boolean = true) {
        if (showMessage) Toast.makeText(requireContext(), resources.getString(R.string.settings_saved), Toast.LENGTH_SHORT).show()
        showCurrentSettings()
    }

    private fun showCurrentSettings() {
        settings_switch_night.isChecked = settings.nightMode
        menu_settings_text_game_mode.text = GameMode.from(settings.gameMode).toString()
        menu_settings_text_grid_size.text = settings.gridSize.toString()
        menu_settings_text_match_count.text = settings.matchCount.toString()
        menu_settings_text_view_time.text = resources.getString(R.string.format_seconds, settings.viewTime / 1000)
        menu_settings_text_lose_time.text = resources.getString(R.string.format_seconds, settings.timeLimit / 1000)
    }

    override fun themeUI(isDark: Boolean) {
        // ...
    }
}