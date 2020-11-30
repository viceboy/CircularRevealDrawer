package com.viceboy.circularrevealdrawer.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.core.content.ContextCompat
import com.viceboy.circularrevealdrawer.*
import com.viceboy.circularrevealdrawer.interfaces.AnimatorListener
import com.viceboy.circularrevealdrawer.interfaces.CircularMenuRevealListener
import com.viceboy.circularrevealdrawer.interpolator.LINEAR_OUT_SLOW_IN

internal class CircularRevealMenu @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    View(context, attrs, defStyleAttr, defStyleRes) {

    var innerCircleColor = 0
    var outerCircleColor = 0

    //Below inner circle coordinates will be set by parent layout based on hamburger menu position
    var innerCircleX = 0f
    var innerCircleY = 0f

    //Below outer circle coordinates will be set by parent layout based on hamburger menu position
    var outerCircleX = 0f
    var outerCircleY = 0f

    private var startPos = 0f
    private var endPos = 0f
    private var maxRadius = 0f

    private var isRunning = false
    private var isMenuExpanded: Boolean = false

    private var onMenuExpandOrCollapse: CircularMenuRevealListener? = null

    private val mPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            strokeWidth = 8f
            color = Color.GREEN
            style = Paint.Style.FILL
        }
    }

    private var innerCircleRadius = 0f
        set(value) {
            if (field != value) {
                field = value
                postInvalidateOnAnimation()
            }
        }

    private var outerCircleRadius = 0f
        set(value) {
            if (field != value) {
                field = value
                postInvalidateOnAnimation()
            }
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        maxRadius = calculateRevealRadius()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        mPaint.color = if (outerCircleColor == 0) ContextCompat.getColor(
            context,
            R.color.indigo
        ) else outerCircleColor
        canvas?.drawCircle(outerCircleX, outerCircleY, outerCircleRadius, mPaint)
        mPaint.color = if (innerCircleColor == 0) ContextCompat.getColor(
            context,
            R.color.inner_indigo
        ) else innerCircleColor
        canvas?.drawCircle(innerCircleX, innerCircleY, innerCircleRadius, mPaint)
        super.onDraw(canvas)
    }

    /**
     * listener used for onMenuCollapse(start/end) or onMenuExpand(start/end)
     */
    fun setCircularMenuRevealListener(listener: CircularMenuRevealListener) {
        onMenuExpandOrCollapse = listener
    }

    /**
     * Expand or collapse the circular reveal menu
     */
    fun toggleMenu(delay: Long = 0,onAnimationEnd : ()->Unit = {}) {
        if (!isRunning) {
            if (isMenuExpanded) {
                startPos = maxRadius
                endPos = 0f
            } else {
                startPos = 0f
                endPos = maxRadius
            }
            AnimatorSet().apply {
                startDelay = delay
                addListener(object : AnimatorListener(){
                    override fun onAnimationEnd(animation: Animator?) {
                        onAnimationEnd.invoke()
                    }
                })
                playTogether(createInnerCircleAnimator(), createOuterCircleAnimator())
                start()
            }
        }
    }

    /**
     * Create Inner circle Animator
     */
    private fun createInnerCircleAnimator(): ValueAnimator {
        return ValueAnimator.ofFloat(startPos, endPos).apply {
            interpolator = LINEAR_OUT_SLOW_IN
            if (!isMenuExpanded)
                startDelay = DURATION_INNER_CIRCLE_START_DELAY
            duration = DURATION_INNER_CIRCLE_ANIMATION
            addUpdateListener {
                innerCircleRadius = animatedValue as Float
            }

            addListener(object : AnimatorListener() {

                override fun onAnimationEnd(animation: Animator?) {
                    if (startDelay > 0) {
                        this@CircularRevealMenu.isRunning = false
                        isMenuExpanded = !isMenuExpanded
                    }
                }

                override fun onAnimationStart(animation: Animator?) {
                    if (startDelay > 0) this@CircularRevealMenu.isRunning = true
                }
            })
        }
    }

    /**
     * Create Outer circle animator
     */
    private fun createOuterCircleAnimator(): ValueAnimator {
        return ValueAnimator.ofFloat(startPos, endPos).apply {
            if (isMenuExpanded) startDelay = DURATION_OUTER_CIRCLE_START_DELAY
            interpolator = AccelerateInterpolator()
            duration = DURATION_OUTER_CIRCLE_ANIMATION
            addUpdateListener {
                outerCircleRadius = animatedValue as Float
            }

            addListener(object : AnimatorListener() {

                override fun onAnimationEnd(animation: Animator?) {
                    if (endPos == 0f) onMenuExpandOrCollapse?.onMenuCollapseEnd()
                    if (startPos == 0f) {
                        this@CircularRevealMenu.setBackgroundColor(context.getColor(R.color.dark_scrim))
                        onMenuExpandOrCollapse?.onMenuExpandEnd()
                    }
                    if (startDelay > 0) {
                        isMenuExpanded = !isMenuExpanded
                        this@CircularRevealMenu.isRunning = false
                    }
                }


                override fun onAnimationStart(animation: Animator?) {
                    if (endPos == 0f) {
                        this@CircularRevealMenu.background = null
                        onMenuExpandOrCollapse?.onMenuCollapseStart()
                    }
                    if (startPos == 0f) {
                        onMenuExpandOrCollapse?.onMenuExpandStart()
                    }
                    if (startDelay > 0) this@CircularRevealMenu.isRunning = true
                }
            })
        }
    }

}