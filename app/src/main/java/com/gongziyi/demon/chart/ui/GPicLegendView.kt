package com.gongziyi.demon.chart.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

/**
 * Created on 2019/10/30
 * @author: gongziyi
 * Description:
 */
class GPicLegendView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }
    private var mWidth = 0
    private var mheight = 0


    init {
        val background = background
        if (background is ColorDrawable) {
            val color = background.color
            mPaint.color = color
            super.setBackground(null)
        }
    }


    override fun setBackgroundColor(color: Int) {
        mPaint.color = color
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mheight = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val rw = mWidth shr 1
        val rh = mheight shr 1
        canvas.save()
        canvas.translate(rw.toFloat(), rh.toFloat())
        val min = min(rw, rh)
        canvas.drawCircle(0f, 0f, min - 3f, mPaint)
        canvas.restore()
    }
}