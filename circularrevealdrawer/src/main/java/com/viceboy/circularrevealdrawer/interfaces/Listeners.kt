package com.viceboy.circularrevealdrawer.interfaces

import android.animation.Animator
import android.transition.Transition
import android.view.View

interface CircularMenuRevealListener {
    fun onMenuExpandStart()
    fun onMenuExpandEnd()
    fun onMenuCollapseStart()
    fun onMenuCollapseEnd()
}

interface CircularRevealListener {
    fun onMenuExpanded()
    fun onMenuCollapsed()
}

interface OnProfileImageClickListener {
    fun onClick(view: View)
}

interface MenuItemClickListener {
    fun getMenuListener(menus: Array<String>): HashMap<String, View.OnClickListener>
}

abstract class AnimatorListener : Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator?) = Unit

    override fun onAnimationCancel(animation: Animator?) = Unit

    override fun onAnimationEnd(animation: Animator?) = Unit

    override fun onAnimationRepeat(animation: Animator?) = Unit
}

abstract class TransitionListener : Transition.TransitionListener {
    override fun onTransitionCancel(transition: Transition?) = Unit
    override fun onTransitionEnd(transition: Transition?) = Unit
    override fun onTransitionPause(transition: Transition?) = Unit
    override fun onTransitionResume(transition: Transition?) = Unit
    override fun onTransitionStart(transition: Transition?) = Unit
}