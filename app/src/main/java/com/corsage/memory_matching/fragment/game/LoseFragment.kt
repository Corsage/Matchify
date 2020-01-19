package com.corsage.memory_matching.fragment.game

import android.animation.Animator
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.corsage.memory_matching.R
import com.corsage.memory_matching.fragment.BaseDialogFragment
import com.corsage.memory_matching.util.Utils
import kotlinx.android.synthetic.main.fragment_game_lose.*

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Lose Fragment.
 * This is the lose dialog screen.
 */

class LoseFragment : BaseDialogFragment() {
    override val TAG = "LoseFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return inflater.inflate(R.layout.fragment_game_lose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        game_lose_btn_menu.setOnClickListener {
            dismiss()
            requireActivity().finish()
        }

        game_lose_btn_replay.setOnClickListener {
            if (dialog != null && dialog?.window != null) {
                val v = dialog!!.window!!.decorView
                val ao = Utils.scaleDownAnimation(v)

                ao.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationEnd(animation: Animator?) {
                        val parent = parentFragment
                        if (parent is GameFragment) {
                            Handler().postDelayed({
                                parent.gameManager.restartGame()
                            }, 400)
                        }
                        dismiss()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        // ...
                    }

                    override fun onAnimationRepeat(animation: Animator?) {
                        // ...
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        // ...
                    }
                })

                ao.duration = 400
                ao.start()
            }
        }
    }

    override fun themeUI(isDark: Boolean) {
        if (isDark) {
            game_lose_img.setImageDrawable(BitmapDrawable(resources, Utils.createGradientBitmap(game_lose_img.drawable.toBitmap(),
                ContextCompat.getColor(requireContext(), R.color.colorDarkPrimary),
                ContextCompat.getColor(requireContext(), R.color.colorDarkAccent)
            )))

            game_lose_btn_menu.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorDarkPrimary))
            game_lose_btn_replay.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorDarkAccent))

        } else {
            game_lose_img.setImageDrawable(BitmapDrawable(resources, Utils.createGradientBitmap(game_lose_img.drawable.toBitmap(),
                ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                ContextCompat.getColor(requireContext(), R.color.colorAccent)
            )))

            game_lose_btn_menu.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            game_lose_btn_replay.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        }
    }
}