package com.corsage.memory_matching.fragment

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.corsage.memory_matching.Application
import com.corsage.memory_matching.R
import com.corsage.memory_matching.util.Utils
import fr.tvbarthel.lib.blurdialogfragment.BlurDialogEngine

abstract  class BaseDialogFragment() : DialogFragment() {
    abstract val TAG: String

    private val settings = Application.settingsManager

    private lateinit var  mBlurEngine: BlurDialogEngine

    abstract fun themeUI(isDark: Boolean)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBlurEngine = BlurDialogEngine(requireActivity())
        mBlurEngine.setBlurRadius(5)
        mBlurEngine.setDownScaleFactor(5f)

        if (settings.nightMode) { setStyle(STYLE_NO_TITLE, R.style.Theme_Corsage_Dark_Dialog) }
        else { setStyle(STYLE_NO_TITLE, R.style.Theme_Corsage_Light_Dialog) }
    }

    override fun onStart() {
        super.onStart()
        themeUI(settings.nightMode)

        if (dialog != null && dialog?.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val view = dialog!!.window!!.decorView
            val ao = Utils.scaleInAnimation(view)
            ao.duration = 400
            ao.start()
        }
    }

    override fun onResume() {
        super.onResume()
        mBlurEngine.onResume(retainInstance)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mBlurEngine.onDismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBlurEngine.onDetach()
    }

    override fun onDestroyView() {
        if (dialog != null) {
            dialog!!.setDismissMessage(null)
        }
        super.onDestroyView()
    }
}