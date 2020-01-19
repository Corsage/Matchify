package com.corsage.memory_matching.ext

import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.corsage.memory_matching.R

/**
 * @author [Jay Chowdhary](https://github.com/Corsage)
 * @since 1.0
 *
 * Extension.
 * Various extension functions for various purposes that don't really belong in a specific file.
 */

/**
 * Methods that make adding [Fragment] to [AppCompatActivity] easier.
 */
inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment, fragment.javaClass.simpleName) }
}

/**
 * Allow all [RecyclerView] to run animation on request.
 */
fun RecyclerView.runLayoutAnimation() {
    val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down)
    layoutAnimation = controller
    adapter?.notifyDataSetChanged()
    scheduleLayoutAnimation()
}

/**
 * Hack to make it possible to use functions with return types in place where [Unit] is expected.
 */
fun Any.unitify() {
    /* do nothing */
}