package com.viceboy.circularrevealdrawer.interpolator

import android.view.animation.Interpolator
import androidx.core.view.animation.PathInterpolatorCompat

val LINEAR_OUT_SLOW_IN: Interpolator by lazy {
    PathInterpolatorCompat.create(0.4f, 0.0f, 0.2f, 1f)
}

val SINGLE_BOUNCE: Interpolator by lazy {
    PathInterpolatorCompat.create(0.595f, 0.565f, 0.490f, 1.110f)
}