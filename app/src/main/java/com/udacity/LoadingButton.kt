package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private  var myCanvas: Canvas? = null
    private val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
    private var animWidth = 0f
    private var sweepAngle = 0f

    val buttonBackgroundPaint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.buttonBackground)
    }

    val buttonTextPaint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.white)
        textSize = 50.0f

    }
    val circlePaint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.circleBackground)
        textSize = 50.0f

    }

    val downloadPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.downloadGreen)
        isAntiAlias = true
    }

    public var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Clicked -> Log.d("WWD", "button clicked")
            ButtonState.Loading -> do_animation()
            ButtonState.Completed ->stop_animation()
        }
    }

    private fun stop_animation() {
        valueAnimator.end()
    }

    init {

    }

    public fun do_animation() {
        valueAnimator.duration = 3000
        valueAnimator.addUpdateListener {
            animWidth = it.animatedFraction * widthSize.toFloat()
            sweepAngle = it.animatedFraction * 360f
            invalidate()
        }
        valueAnimator.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(ContextCompat.getColor(context, R.color.buttonBackground))
        canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), buttonBackgroundPaint )
        when (buttonState) {
            ButtonState.Start, ButtonState.Completed -> {
                canvas?.drawText("Download", 200.0f, 75.0f, buttonTextPaint)
            }

            ButtonState.Clicked, ButtonState.Loading -> {
                canvas?.drawRect(0f, 0f, animWidth, heightSize.toFloat(), downloadPaint)
                var oval = RectF(
                    500f,
                    5f,
                    500f + (heightSize.toFloat() * 0.9f),
                    5f + (heightSize.toFloat() * 0.9f)
                )
                canvas?.drawArc(oval, 0f, sweepAngle, true, circlePaint)
                canvas?.drawText("We are loading", 150.0f, 75.0f, buttonTextPaint)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}