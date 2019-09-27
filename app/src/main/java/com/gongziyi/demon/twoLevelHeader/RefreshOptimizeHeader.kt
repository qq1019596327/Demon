package com.gongziyi.demon.twoLevelHeader

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Animation
import com.airbnb.lottie.LottieAnimationView
import com.gongziyi.demon.R
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshKernel
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.header.TwoLevelHeader
import com.scwang.smartrefresh.layout.internal.InternalAbstract
import kotlinx.android.synthetic.main.res_layout_refresh_optimize_header.view.*

/**
 * Created on 2019/7/17
 * @author: gongziyi
 * Description:自定义二楼动画刷新类
 */
class RefreshOptimizeHeader @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : InternalAbstract(context, attrs, defStyleAttr), RefreshHeader {


    var canToTwoLevel: Boolean = true
        set(value) {
            field = value
            mRefreshHeaderBg.visibilityOrGone(canToTwoLevel)
        }

    init {
        View.inflate(context, R.layout.res_layout_refresh_optimize_header, this)
    }


    private var canMovingBg = true

    /**手指控制时动画进度控制*/
    override fun onMoving(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {
        super.onMoving(isDragging, percent, offset, height, maxDragHeight)
        Log.i(
            "onMoving",
            "isDragging=$isDragging,  percent=$percent,  offset=$offset,  height=$height,  maxDragHeight=$maxDragHeight"
        )
        if (canMovingBg) {
            mRefreshHeaderBg.progress = getBgProgress(percent)
        }
        mRefreshHeaderIcon.progress = getLoadingProgress(offset, height, maxDragHeight)
        mRefreshHeaderBottom.setGuidelineBegin(offset)
    }

    /**计算背景动画进度*/
    private fun getBgProgress(percent: Float): Float =
        if (percent < 1 || !canToTwoLevel) {
            0f
        } else {
            ((percent - 1) / 0.5f) * 0.6f
        }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
        super.onInitialized(kernel, height, maxDragHeight)
        mRefreshHeaderBg.visibilityOrGone(canToTwoLevel)
    }

    /**计算文本动画进度*/
    private fun getLoadingProgress(offset: Int, height: Int, maxDragHeight: Int): Float {
        var progress: Float
        if (offset < height) {
            progress = (offset.toFloat() / height.toFloat()) * 0.8f
        } else {
            progress = 0.8f
            if (canToTwoLevel) {
                progress += ((offset - height).toFloat() / (maxDragHeight - height).toFloat()) * 0.4f
            }
        }
        return progress
    }

    /**状态切换回调*/
    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        super.onStateChanged(refreshLayout, oldState, newState)
        Log.i("onStateChanged", "$newState")
        when (newState) {
            RefreshState.ReleaseToRefresh -> {
                //滑动到刷新区域
                if (canToTwoLevel) {
                    mRefreshHeaderTwoLevelHint.visibilityOrGone(false)
                }
            }
            RefreshState.ReleaseToTwoLevel -> {
                //滑动到开启二楼区域
                if (canToTwoLevel) {
                    mRefreshHeaderBg.apply {
                        if (!isAnimating) {
                            repeatCount = Animation.INFINITE
                            setMinAndMaxProgress(0.6f, 1f)
                            playAnimation()
                        }
                    }
                    mRefreshHeaderTwoLevelHint.visibilityOrGone(true)
                }
                canMovingBg = false
            }
            RefreshState.Refreshing -> {
                //刷新中动画开启
                mRefreshHeaderIcon.visibilityOrGone(false)
                mRefreshHeaderLoading.apply {
                    progress = 0f
                    repeatCount = Animation.INFINITE
                    visibility = View.VISIBLE
                    playAnimation()
                }
            }
            RefreshState.RefreshFinish -> {
                //刷新动画结束
                mRefreshHeaderLoading.repeatCount = 0
            }
            RefreshState.TwoLevel -> {
                (parent as TwoLevelHeader).finishTwoLevel()
            }
            RefreshState.None -> {
                //手动控制
                mRefreshHeaderIcon.visibilityOrGone(true)
                mRefreshHeaderLoading.visibilityOrGone(false)
                canMovingBg = true
                if (canToTwoLevel) {
                    mRefreshHeaderTwoLevelHint.visibilityOrGone(false)
                    mRefreshHeaderBg.apply {
                        pauseAnimation()
                        setMinAndMaxProgress(0f, 1f)
                        speed = Math.abs(speed)
                        progress = 0f
                    }
                }
            }
        }
    }


    override fun getSpinnerStyle(): SpinnerStyle = SpinnerStyle.FixedBehind

}

fun View.visibilityOrGone(isVisibility: Boolean) {
    visibility = if (isVisibility) View.VISIBLE else View.GONE
}