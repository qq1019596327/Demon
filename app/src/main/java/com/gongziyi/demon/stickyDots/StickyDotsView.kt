package com.gongziyi.demon.stickyDots

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.*

/**
 * Created on 2019/11/20
 * @author: gongziyi
 * Description:小红点
 */
class StickyDotsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    /**圆点半径*/
    private var mRadius = 50f
    /**最大拉伸长度*/
    private var mMaxStretchLength = 300f
    /**硬度*/
    private var mHardness = 0.2f

    //趋势进度
    private var mTrendProgress = 1f
    //画笔
    private val mPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            /**小红点颜色*/
            color = Color.RED
            /**是否Debug*/
            style = Paint.Style.STROKE
        }
    }
    //鼻涕线段
    private var mSnivelPath = Path()

    //原始左
    private val oLeft: Point = Point()
    //原始右
    private val oRight: Point = Point()

    //目标中心点
    private val tCore: Point = Point()
    //目标左
    private val tLeft: Point = Point()
    //目标右
    private val tRight: Point = Point()


    private val temporaryArray: IntArray = IntArray(2)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = (event.x - (width shr 1)).toInt()
        val y = (event.y - (height shr 1)).toInt()
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                abs(x) < mRadius && abs(y) < mRadius
            }
            MotionEvent.ACTION_MOVE -> {
                val length = getLength(x, y)
                //计算进度
                if (length <= mMaxStretchLength && mTrendProgress > 0f) {
                    mTrendProgress = (mMaxStretchLength - length) / mMaxStretchLength
                } else {
                    mTrendProgress = -1f
                }
                tCore.set(x, y)
                postInvalidate()
                true
            }
            MotionEvent.ACTION_UP -> {
                mTrendProgress = 1f
                tCore.set(0, 0)
                postInvalidate()
                true
            }
            else -> super.onTouchEvent(event)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val widthRadius = width shr 1
        val heightRadius = height shr 1
        canvas.save()
        canvas.translate(widthRadius.toFloat(), heightRadius.toFloat())
        canvas.drawCircle(tCore.xf(), tCore.yf(), mRadius, mPaint)
        if (mTrendProgress > 0 && mTrendProgress < 1) {
            canvas.drawCircle(0f, 0f, mTrendProgress.getProgressValue(mRadius, mHardness), mPaint)

            mSnivelPath.reset()
            if (mTrendProgress >= 0) {
                val angle = getAngle(tCore.x, tCore.y)
                val oRadius = mTrendProgress.getProgressValue(mRadius, mHardness)
                val tAngle = mTrendProgress.getProgressValue(90f, mHardness)
                getPoint(angle + 90, oRadius, temporaryArray)
                oLeft.set(temporaryArray[0], temporaryArray[1])
                getPoint(angle - 90, oRadius, temporaryArray)
                oRight.set(temporaryArray[0], temporaryArray[1])
                getPoint(angle + 180 + tAngle, mRadius, temporaryArray)
                tLeft.set(tCore.x + temporaryArray[0], tCore.y + temporaryArray[1])
                getPoint(angle + 180 - tAngle, mRadius, temporaryArray)
                tRight.set(tCore.x + temporaryArray[0], tCore.y + temporaryArray[1])
                mSnivelPath.moveTo(oLeft)
                mSnivelPath.quadTo(tRight, tCore.x / 2f, tCore.y / 2f)
                mSnivelPath.lineTo(tLeft)
                mSnivelPath.quadTo(oRight, tCore.x / 2f, tCore.y / 2f)
                mSnivelPath.close()
            }
            canvas.drawPath(mSnivelPath, mPaint)

            /**是否Debug*/
            canvas.drawLine(oLeft.xf(), oLeft.yf(), tLeft.xf(), tLeft.yf(), mPaint)
            canvas.drawLine(oRight.xf(), oRight.yf(), tRight.xf(), tRight.yf(), mPaint)
        }
        canvas.restore()
    }

    //计算点基于00点的角度
    fun getAngle(dx: Int, dy: Int): Double {
        var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble()))
        if (angle < 0) {
            angle += 360
        }
        return angle
    }

    //计算点到00点的长度
    fun getLength(dx: Int, dy: Int): Float = sqrt(abs(dx * dx + dy * dy).toDouble()).toFloat()

    /**
     * 根据角度和长度计算基于00点的位置
     */
    fun getPoint(angle: Double, length: Float, point: IntArray) {
        val radian = angle * Math.PI / 180
        point[0] = (cos(radian) * length).toInt()
        point[1] = (sin(radian) * length).toInt()
    }

    private fun Float.getProgressValue(value: Float, retain: Float): Float {
        if (retain > 1) {
            return value
        }
        return value * retain + value * (1 - retain) * this
    }

    private fun Point.xf(): Float = x.toFloat()
    private fun Point.yf(): Float = y.toFloat()
    private fun Path.moveTo(target: Point) = moveTo(target.xf(), target.yf())
    private fun Path.lineTo(target: Point) = lineTo(target.xf(), target.yf())
    private fun Path.quadTo(target: Point, x: Float, y: Float) =
        quadTo(x, y, target.xf(), target.yf())
}