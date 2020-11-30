package com.viceboy.circularrevealdrawer.widget

import android.transition.Transition
import android.transition.TransitionManager
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.viceboy.circularrevealdrawer.interpolator.SINGLE_BOUNCE


internal fun ViewGroup.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return dpToPx(result)
}

internal fun ViewGroup.dpToPx(dp: Int): Int {
    val displayMetrics = context.resources.displayMetrics
    return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}

internal fun ViewGroup.dpToPx(dp: Float): Float {
    val displayMetrics = context.resources.displayMetrics
    return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).toFloat()
}

internal fun ViewGroup.beginDelayedTransition(transition : Transition) {
    TransitionManager.beginDelayedTransition(this,transition)
}

internal fun View.hideWithTranslationY() {
    this.translationY = this.y.plus(height)
}

internal fun View.animateHideWithTranslationY() {
    val initTranslation = this.y.plus(height)
    animate().translationY(initTranslation).apply {
        duration = 400
        interpolator = SINGLE_BOUNCE
    }
}

internal fun View.hideWithTranslationX() {
    this.translationX = this.x.plus(width)
}

internal fun View.animateHideWithTranslationX() {
    val initTranslation = this.x.plus(width)
    animate().translationX(initTranslation).apply {
        duration = 400
        interpolator = SINGLE_BOUNCE
    }
}

internal fun View.animateShowWithTranslationX() {
    animate().translationX(0f).apply {
        duration = 200
        interpolator = SINGLE_BOUNCE
    }
}

internal fun View.animateShowWithTranslationY() {
    animate().translationY(0f).apply {
        duration = 200
        interpolator = SINGLE_BOUNCE
    }
}

internal fun LinearLayout.forEachChild(action: (View) -> Unit) {
    val arrayOfChild = mutableListOf<TextView>()
    for (i in 0 until childCount) {
        arrayOfChild.add(getChildAt(i) as TextView)
    }

    arrayOfChild.forEach {
        action(it)
    }
}

internal var View.isVisible: Boolean
    get() {
        return visibility == View.VISIBLE
    }
    set(value) {
        if (value)
            this.visibility = View.VISIBLE
        else
            this.visibility = View.GONE
    }

internal fun View.calculateRevealRadius() = width.plus(width / 8).toFloat()