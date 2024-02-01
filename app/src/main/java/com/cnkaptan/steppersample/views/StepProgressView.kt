package com.cnkaptan.steppersample.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.cnkaptan.steppersample.R

class StepProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mFont: Typeface? = null
        private set

    private var mCompletedColor = ContextCompat.getColor(context, R.color.completed_color)
    private val mCompletedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = mCompletedColor
    }

    private val mInCompleteColor = ContextCompat.getColor(context, R.color.incomplete_color)
    private val mIncompletePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = mInCompleteColor
    }

    private val mInnerCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    private var textPaint = Paint().apply {
        color = Color.BLACK
        textSize = spToFloat(13f)
        textAlign = Paint.Align.CENTER
    }


    private var mCircleRadius = dpToFloat(15f)
    private var mInnerCircleRadius = dpToFloat(2f)
    private var lineHeight = dpToFloat(4f)
    private val completedIcon by lazy {
        ContextCompat.getDrawable(
            context,
            R.drawable.ic_step_completed
        )
    }


    private val mSteps = mutableListOf<Step>()

    init {
        initAttrs(context, attrs, defStyleAttr)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs == null) return

        context.obtainStyledAttributes(attrs, R.styleable.StepProgressView, defStyleAttr, 0).use {
            mCompletedColor =
                it.getColor(R.styleable.StepProgressView_completedColor, mCompletedColor)
            mCircleRadius =
                it.getDimension(R.styleable.StepProgressView_circleRadius, mCircleRadius)
            mInnerCircleRadius = mCircleRadius - dpToFloat(4f)
            mFont = it.getResourceId(R.styleable.StepProgressView_stepTextsFont, -1).takeIf {
                it != -1
            }?.let { fontId ->
                ResourcesCompat.getFont(context, fontId)
            }?.also {
                textPaint.typeface = it
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = measuredHeight

        val widthWithoutPadding = width - paddingLeft - paddingRight
        val heightWithoutPadding = height - paddingTop - paddingBottom

        setMeasuredDimension(widthWithoutPadding, heightWithoutPadding)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.e(TAG, "onDraw: ${mCircleRadius} - Width: ${width} - Height: ${height}")
        val mCircleCenterHeight = mCircleRadius + paddingTop
        val totalLineWidth = width - mSteps.size * (mCircleRadius * 2) - paddingLeft - paddingRight
        val perLineWidth = totalLineWidth / (mSteps.size - 1)


        mSteps.forEachIndexed { index, step ->
            val circleCenterWidth =
                paddingLeft + mCircleRadius + index * (perLineWidth + mCircleRadius * 2)
            val textX = circleCenterWidth
            val textY = mCircleCenterHeight - ((textPaint.descent() + textPaint.ascent()) / 2)

            drawOuterCircle(step, canvas, circleCenterWidth, mCircleCenterHeight)

            if (index < mSteps.size - 1) {
                drawLine(step, canvas, circleCenterWidth, mCircleCenterHeight, perLineWidth)
            }

            when (step.stepState) {
                StepState.COMPLETED -> {
                    completedIcon?.let {
                        it.setBounds(
                            (circleCenterWidth - it.intrinsicWidth / 2).toInt(),
                            (mCircleCenterHeight - it.intrinsicHeight / 2).toInt(),
                            (circleCenterWidth + it.intrinsicWidth / 2).toInt(),
                            (mCircleCenterHeight + it.intrinsicHeight / 2).toInt()
                        )
                        it.draw(canvas)
                    }
                }

                StepState.CURRENT, StepState.INCOMPLETE -> {
                    canvas.drawCircle(
                        circleCenterWidth,
                        mCircleCenterHeight,
                        mInnerCircleRadius,
                        mInnerCirclePaint
                    )
                    canvas.drawText(step.id.toString(), textX, textY, textPaint)
                }
            }
        }

        val currentStep = mSteps.find { it.stepState == StepState.CURRENT }

        currentStep?.let {
            val textX = mSteps.indexOf(it) * (perLineWidth + mCircleRadius * 2)
            val textY =
                mCircleRadius * 2 + paddingTop - ((textPaint.descent() + textPaint.ascent()) * 2)
            val text = resources.getString(it.textId)
            canvas.drawText(text, textX, textY, textPaint)
        }
    }

    private fun drawLine(
        step: Step,
        canvas: Canvas,
        circleCenterWidth: Float,
        mCircleCenterHeight: Float,
        perLineWidth: Float
    ) {
        val linePaint =
            if (step.stepState == StepState.COMPLETED) mCompletedPaint else mIncompletePaint
        canvas.drawRect(
            circleCenterWidth + mCircleRadius,
            mCircleCenterHeight - lineHeight / 2,
            circleCenterWidth + mCircleRadius + perLineWidth,
            mCircleCenterHeight + lineHeight / 2,
            linePaint
        )
    }

    private fun drawOuterCircle(
        step: Step,
        canvas: Canvas,
        circleCenterWidth: Float,
        mCircleCenterHeight: Float
    ) {
        val outerCirclePaint = if (step.stepState == StepState.INCOMPLETE) {
            mIncompletePaint
        } else {
            mCompletedPaint
        }

        canvas.drawCircle(
            circleCenterWidth,
            mCircleCenterHeight,
            mCircleRadius,
            outerCirclePaint
        )
    }


    fun setSteps(steps: List<Step>) {
        mSteps.clear()
        mSteps.addAll(steps)
        invalidate()
    }

    companion object {
        private val TAG = "StepProgressView"
    }
}
