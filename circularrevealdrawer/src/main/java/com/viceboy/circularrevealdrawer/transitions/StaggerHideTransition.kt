package com.viceboy.circularrevealdrawer.transitions

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.transition.Fade
import android.transition.SidePropagation
import android.transition.TransitionValues
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.viceboy.circularrevealdrawer.widget.DURATION_STAGGER_HIDE


class StaggerHideTransition : Fade(OUT) {

    init {
        duration = DURATION_STAGGER_HIDE
        interpolator = FastOutLinearInInterpolator()
        propagation = SidePropagation().apply {
            setSide(Gravity.BOTTOM)
            setPropagationSpeed(1F)
        }
    }

    override fun createAnimator(sceneRoot: ViewGroup?, startValues: TransitionValues?, endValues: TransitionValues?): Animator? {
        val view = startValues?.view ?: endValues?.view ?: return null
        val fade = super.createAnimator(sceneRoot, startValues, endValues)
        return AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, View.ALPHA,1f,0f),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y,0f, view.height/2.toFloat())
            )
        }
    }

}