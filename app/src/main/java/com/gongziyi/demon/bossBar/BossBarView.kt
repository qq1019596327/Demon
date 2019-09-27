package com.gongziyi.demon.bossBar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.gongziyi.demon.R

/**
 * Created on 2019/7/9
 * @author: gongziyi
 * Description:boss底部状态栏
 */
class BossBarView : View, ValueAnimator.AnimatorUpdateListener {


    private var mRect: RectF = RectF()
    private var mBgPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
    }
    private var mLinePaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.parseColor("#CECCCC")
        strokeWidth = 2f
    }

    private var mPath: Path = Path()

    private var offsetHeader: Int = 0
    private var bezierOuter: Int = 0
    private var bezierInner: Int = 0
    private var bezierRadius: Int = 0


    private var show: UpliftPosition? = null
    private var lastShow: UpliftPosition? = null
    private var mProgress: Float = 1f

    private val mAnnotation = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 300
        addUpdateListener(this@BossBarView)
    }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BossBarView)
        offsetHeader = typedArray.getDimensionPixelSize(R.styleable.BossBarView_offset_header, 0)
        bezierRadius = typedArray.getDimensionPixelSize(R.styleable.BossBarView_bezier_radius, offsetHeader)
        bezierOuter = typedArray.getDimensionPixelSize(R.styleable.BossBarView_bezier_outer, bezierRadius)
        bezierInner = typedArray.getDimensionPixelSize(R.styleable.BossBarView_bezier_inner, bezierOuter)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mRect.set(0f, offsetHeader.toFloat(), measuredWidth.toFloat(), measuredHeight.toFloat())
    }


    fun showUplift(view: View, isAnnotation: Boolean = true) {
        val core = view.left + (view.measuredWidth shr 1)
        showUplift(core, isAnnotation)
    }

    protected open fun showUplift(core: Int, isAnnotation: Boolean = true) {
        show?.let {
            //忽略同点
            if (it.mCore == core)
                return
            it.isOpen = false
            lastShow = it
        }
        show = UpliftPosition().apply {
            radius = bezierRadius
            outer = bezierOuter
            inner = bezierInner
            mCore = core
        }
        if (isAnnotation) {
            mAnnotation.start()
        } else {
            postInvalidate()
        }
    }


    fun reset(isAnnotation: Boolean = true) {
        show?.let {
            it.isOpen = false
        }
        lastShow?.let {
            it.isOpen = false
        }
        if (isAnnotation) {
            mAnnotation.start()
        } else {
            postInvalidate()
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val strokeWidth = 2f
        canvas.save()
        canvas.translate(0f, strokeWidth)
        canvas.drawRect(mRect, mBgPaint)
        resetPath(offsetHeader.toFloat())
        canvas.drawPath(mPath, mBgPaint)
        canvas.drawPath(mPath, mLinePaint)
        canvas.restore()
    }

    /**重新计算path*/
    private fun resetPath(baseY: Float) {
        mPath.reset()
        mPath.moveTo(0f, baseY)
        //从左至右绘制 需要决定谁先开始
        val startShow = show?.mCore ?: 0 <= lastShow?.mCore ?: 0
        (if (startShow) show else lastShow)?.addUplift(mPath, baseY, offsetHeader, mProgress)
        (if (!startShow) show else lastShow)?.addUplift(mPath, baseY, offsetHeader, mProgress)
        mPath.lineTo(measuredWidth.toFloat(), baseY)
    }


    private class UpliftPosition {
        var radius: Int = 0
        var outer: Int = 0
        var inner: Int = 0
        var mCore: Int = 0

        var isOpen: Boolean = true

        fun getStart(): Float = mCore - radius.toFloat()
        fun getStartOuter(): Float = mCore - outer.toFloat()
        fun getStartInner(): Float = mCore - inner.toFloat()
        fun getEndInner(): Float = mCore + inner.toFloat()
        fun getEndOuter(): Float = mCore + outer.toFloat()
        fun getEnd(): Float = mCore + radius.toFloat()


        fun addUplift(path: Path, baseY: Float, offsetHeader: Int, progress: Float) {
            if (progress == 1f && !isOpen) return

            val upliftHeight = offsetHeader * if (isOpen) 1 - progress else progress

            path.lineTo(getStart(), baseY)
            path.cubicTo(getStartOuter(), baseY, getStartInner(), upliftHeight, mCore.toFloat(), upliftHeight)
            path.cubicTo(getEndInner(), upliftHeight, getEndOuter(), baseY, getEnd(), baseY)
        }


    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        mProgress = animation.animatedValue as Float
        if (mProgress == 1f) {
            if (show?.isOpen == false) show = null
            if (lastShow?.isOpen == false) lastShow = null
        }
        postInvalidateOnAnimation()
    }

    override fun onDetachedFromWindow() {
        mAnnotation.removeUpdateListener(this)
        super.onDetachedFromWindow()
    }
}