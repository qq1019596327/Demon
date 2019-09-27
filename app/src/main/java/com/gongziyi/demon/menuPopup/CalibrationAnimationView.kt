package com.gongziyi.demon.menuPopup

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ViewAnimator
import kotlin.math.log

/**
 * Created on 2019/8/5
 * @author: gongziyi
 * Description:
 */
class CalibrationAnimationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ValueAnimator.AnimatorUpdateListener {


    /**基础path*/
    private val mPath = Path()
    /**绘制用path*/
    private val mDrawPath = Path()
    /**path测量类*/
    private var mPathMeasure = PathMeasure()
    /**view最大测量高度*/
    private var mMaxHeightMeasure = 0
    /**绘制偏移padding*/
    private var mDrawPadding = 10f

    /**绘制用笔*/
    private val mPaint = Paint().apply {
        isAntiAlias = true
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 8f
        pathEffect = DashPathEffect(FloatArray(2) { 4f }, 0f)
    }
    private val mAnimation by lazy {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 300
        }
    }

    private var mProgress = 0f

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightMeasure = MeasureSpec.getSize(heightMeasureSpec)
        mPath.reset()
        mPath.addCircle(
            heightMeasure.toFloat(),
            heightMeasure.toFloat(),
            heightMeasure.toFloat() - mDrawPadding,
            Path.Direction.CW
        )
        mPathMeasure = PathMeasure(mPath, false)
        setMeasuredDimension(heightMeasure * 2, heightMeasure)
    }

    fun startAnimation() {
        mProgress = 0f
        mAnimation.start()
    }

    fun clear() {
        if (mAnimation.isRunning && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAnimation.pause()
        }
        mProgress = 0f
        postInvalidateOnAnimation()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //用于判断自动开启状态
        if (mProgress > 0) {
            canvas.save()
            canvas.rotate(-90 - 90 * mProgress, (measuredWidth shr 1).toFloat(), measuredHeight.toFloat())
            mDrawPath.reset()
            mPathMeasure.getSegment(0f, (mPathMeasure.length / 2) * mProgress, mDrawPath, true)
            canvas.drawPath(mDrawPath, mPaint)
            canvas.restore()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mMaxHeightMeasure = Math.max(mMaxHeightMeasure, measuredHeight)
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        mProgress = animation.animatedValue as Float
        postInvalidateOnAnimation()

    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mAnimation.addUpdateListener(this)
    }

    override fun onDetachedFromWindow() {
        mAnimation.removeUpdateListener(this)
        super.onDetachedFromWindow()
    }
}