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
import com.viceboy.circularrevealdrawer.widget.DURATION_STAGGER_REVEAL
import com.viceboy.circularrevealdrawer.widget.DURATION_STAGGER_REVEAL_DELAY

class StaggerRevealTransition : Fade(IN) {

    init {
        duration = DURATION_STAGGER_REVEAL
        startDelay = DURATION_STAGGER_REVEAL_DELAY
        interpolator = FastOutLinearInInterpolator()
        propagation = SidePropagation().apply {
            setSide(Gravity.BOTTOM)
            setPropagationSpeed(1f)
        }
    }

    override fun createAnimator(sceneRoot: ViewGroup?, startValues: TransitionValues?, endValues: TransitionValues?): Animator? {
        val view = startValues?.view?:endValues?.view?:return null
        val fadeAnimator =  super.createAnimator(sceneRoot, startValues, endValues)
        return AnimatorSet().apply {
            playTogether(
                fadeAnimator,
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y,view.height.toFloat(),0f)
            )
        }
    }
}