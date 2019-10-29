package com.gongziyi.demon.shadowTest

import android.content.Context
import android.graphics.Color
import android.graphics.Outline
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.constraintlayout.widget.ConstraintLayout
import com.gongziyi.demon.R

/**
 * Created by jc on 2018-05-17.
 * Version:1.0
 * Description: 流程中心的Item的view
 * ChangeLog:
 */

class ShadowAngleConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        const val NORMAL = 0
        const val UPPER = 1
        const val LEFT = 2
        const val LOWER = 3
        const val RIGHT = 4

    }

    //圆角
    private var mRadius: Float = 0f
    //阴影扩散大小
    private var mShadowElevation: Float = 10f
    //阴影透明度
    private var mShadowAlpha: Float = 1f
    //阴影颜色
    private var mShadowColor: Int = Color.GRAY
    //模式
    private var mRadiusMode = NORMAL


    init {
        context.obtainStyledAttributes(attrs, R.styleable.ShadowAngleConstraintLayout)?.apply {
            mRadius = getDimension(R.styleable.ShadowAngleConstraintLayout_mRadius, mRadius)
            mShadowElevation =
                getDimension(
                    R.styleable.ShadowAngleConstraintLayout_mShadowElevation,
                    mShadowElevation
                )
            mShadowAlpha =
                getFloat(R.styleable.ShadowAngleConstraintLayout_mShadowAlpha, mShadowAlpha)
            mShadowColor =
                getColor(R.styleable.ShadowAngleConstraintLayout_mShadowColor, mShadowColor)
            mRadiusMode = getInt(R.styleable.ShadowAngleConstraintLayout_mRadiusMode, mRadiusMode)
            recycle()
        }
        setRadiusAndShadow(mRadius, mShadowElevation, mShadowColor, mShadowAlpha, mRadiusMode)
    }


    fun setShadowColor(shadowColor: Int) {
        if (mShadowColor == shadowColor) return
        mShadowColor = shadowColor
        setShadowColorInner(mShadowColor)
    }

    fun setRadius(radius: Float) {
        if (mRadius == radius) return
        setRadiusAndShadow(radius, mShadowElevation, mShadowColor, mShadowAlpha, mRadiusMode)
    }

    fun setShadow(shadow: Float, alpha: Float) {
        if (mShadowElevation == shadow && mShadowAlpha == alpha) return
        setRadiusAndShadow(mRadius, shadow, mShadowColor, alpha, mRadiusMode)
    }

    fun setRadiusMode(radiusMode: Int) {
        if (radiusMode == mRadiusMode) return
        setRadiusAndShadow(mRadius, mShadowElevation, mShadowColor, alpha, radiusMode)
    }

    fun getRadius() = mRadius
    fun getShadowElevation() = mShadowElevation
    fun getShadowAlpha() = mShadowAlpha
    fun getShadowColor() = mShadowColor
    fun getRadiusMode() = mRadiusMode


    private fun setShadowColorInner(shadowColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            outlineAmbientShadowColor = shadowColor
            outlineSpotShadowColor = shadowColor
        }
    }

    protected open fun setRadiusAndShadow(
        radius: Float,
        shadow: Float,
        color: Int,
        alpha: Float,
        radiusMode: Int
    ) {
        mRadius = radius
        mShadowElevation = shadow
        mShadowAlpha = alpha
        mShadowColor = color
        mRadiusMode = radiusMode
        if (useFeature()) {
            elevation = mShadowElevation
//            val mPath = if (mRadius > 0 && mRadiusMode != NORMAL) Path() else null
            setShadowColorInner(mShadowColor)
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    val w = view.width
                    val h = view.height
                    if (w == 0 || h == 0) return
//                    mPath?.apply {
//                        when (mRadiusMode) {
//                            UPPER -> {
//                                moveTo(mRadius, 0f)
//                                lineTo(w - mRadius, 0f)
//                                quadTo(w.toFloat(), 0f, w.toFloat(), mRadius)
//                                lineTo(w.toFloat(), h.toFloat())
//                                lineTo(0f, h.toFloat())
//                                lineTo(0f, mRadius)
//                                quadTo(0f, 0f, mRadius, 0f)
//                                close()
//                            }
//                            RIGHT -> {
//                                moveTo(0f, 0f)
//                                lineTo(w - mRadius, 0f)
//                                quadTo(w.toFloat(), 0f, w.toFloat(), mRadius)
//                                lineTo(w.toFloat(), h - mRadius)
//                                quadTo(w.toFloat(), h.toFloat(), w - mRadius, h.toFloat())
//                                lineTo(0f, h.toFloat())
//                                lineTo(0f, 0f)
//                                close()
//                            }
//                            LOWER -> {
//                                moveTo(0f, 0f)
//                                lineTo(w.toFloat(), 0f)
//                                lineTo(w.toFloat(), h - mRadius)
//                                quadTo(w.toFloat(), h.toFloat(), w.toFloat() - mRadius, h.toFloat())
//                                lineTo(mRadius, h.toFloat())
//                                quadTo(0f, h.toFloat(), 0f, h - mRadius)
//                                lineTo(0f, 0f)
//                                close()
//                            }
//                            LEFT -> {
//                                moveTo(mRadius, 0f)
//                                lineTo(w.toFloat(), 0f)
//                                lineTo(w.toFloat(), h.toFloat())
//                                lineTo(mRadius, h.toFloat())
//                                quadTo(0f, h.toFloat(), 0f, h - mRadius)
//                                lineTo(0f, mRadius)
//                                quadTo(0f, 0f, mRadius, 0f)
//                                close()
//
//                            }
//                        }
//                    }

                    outline.alpha = if (mShadowElevation == 0f) 1f else mShadowAlpha


                    if (mRadius <= 0) {
                        outline.setRect(0, 0, w, h)
                    } else {
//                        if (mPath != null) {
//                            outline.setConvexPath(mPath)
//                        } else {
                        outline.setRoundRect(0, 0, w, h, mRadius)
//                        }
                    }
                }
            }
            clipToOutline = mRadius > 0
        }
        invalidate()
    }


    private fun useFeature() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

}
