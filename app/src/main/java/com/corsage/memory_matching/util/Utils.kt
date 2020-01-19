package com.corsage.memory_matching.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Handler
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import com.corsage.memory_matching.activity.SplashActivity
import com.facebook.drawee.view.SimpleDraweeView
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.sqrt

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Utility class.
 * A collection of utility functions used throughout the app.
 */

class Utils {
    companion object {
        private const val TAG = "Utils"

        /**
         * Credits to: chubakueno from https://math.stackexchange.com
         */
        fun findOptimalSquareSize(width: Double, height: Double, squares: Int): Double {
            val sx: Double
            val sy: Double

            val px = ceil(sqrt(squares *  width / height))
            if (floor(px * height / width) * px < squares) {
                sx = height / ceil(px * height / width)
            } else {
                sx = width / px
            }

            val py = ceil(sqrt(squares * height / width))
            if (floor(py * width / height) * py < squares) {
                sy = width / ceil(width * py / height)
            } else {
                sy = height / py
            }

            return max(sx, sy)
        }

        fun convertDpToPx(context: Context, dp: Float): Float {
            return dp * context.resources.displayMetrics.density
        }

        fun createGradientBitmap(bitmap: Bitmap, color1: Int, color2: Int): Bitmap {
            val width = bitmap.width
            val height = bitmap.height

            val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(newBitmap)

            canvas.drawBitmap(bitmap, 0f, 0f, null)

            val paint = Paint()
            val shader = LinearGradient(0f, 0f, 0f, height.toFloat(), color1, color2, Shader.TileMode.CLAMP)

            paint.shader = shader
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

            return newBitmap
        }

        fun scaleInAnimation(view: View): ObjectAnimator {
            return ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleX", 0f, 1f),
                PropertyValuesHolder.ofFloat("scaleY", 0f, 1f),
                PropertyValuesHolder.ofFloat("alpha", 0f, 1f))
        }

        fun scaleDownAnimation(view: View): ObjectAnimator {
            return ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleX", 1f, 0f),
                PropertyValuesHolder.ofFloat("scaleY", 1f, 0f),
                PropertyValuesHolder.ofFloat("alpha", 1f, 0f))
        }

        fun performCardTurn(v: SimpleDraweeView, url: String? = null) {
            val oa1 = ObjectAnimator.ofFloat(v, "scaleX", 1f, 0f)
            val oa2 = ObjectAnimator.ofFloat(v, "scaleX", 0f, 1f)
            oa1.interpolator = DecelerateInterpolator()
            oa2.interpolator = AccelerateDecelerateInterpolator()
            oa1.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    v.setImageURI(url)
                    oa2.start()
                }
            })
            oa1.start()
        }

        fun restartApplication(activity: Activity) {
            Toast.makeText(activity, "Restarting application.", Toast.LENGTH_SHORT).show()

            Handler().postDelayed({
                val intent = Intent(activity, SplashActivity::class.java)
                activity.startActivity(intent)
                activity.finishAffinity()
            }, 500)
        }
    }
}