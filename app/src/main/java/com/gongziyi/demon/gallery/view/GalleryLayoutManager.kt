package com.gongziyi.demon.gallery.adpater

import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * Created on 2019/11/1
 * @author: gongziyi
 * Description:
 */


class GalleryLayoutManager : RecyclerView.LayoutManager() {
    /**当前总宽度,动态计算产生*/
    private var mTotalWidth: Int = 0
    /**当前滑动位置,动态计算产生*/
    private var scrollOffset: Int = 0
    /**上一次itemCount*/
    private var lastItemSize = 0
    /**矩阵集合*/
    private val allRects: SparseArray<Rect> = SparseArray<Rect>()
    /**视图进度变化 progress变化曲线 0f->1f->0f*/
    private var onViewByProgressChange: ((view: View, progress: Float) -> Unit)? = null

    /**开始位置*/
    var startPadding = 0
    /**平滑区 意为此区域所产生的进度值均为 1f 并且在总长的首尾格增加对应长度的拖拽区域 */
    var smoothRegion = 0
    /**进度区 包含平滑区,当小于平滑区时,无视平滑区的值 2dp*/
    var progressRegion = 0
    /**膨胀补偿 防止扩大时遮挡下一个视图 dp*/
    var paddingOffset = 0
    /**展开大小dp*/
    var openSize = 0

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun canScrollHorizontally(): Boolean = true

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        calculateChildrenSize(recycler)
        recyclerAndFillView(recycler, state)
    }

    //计算子View大小及
    private fun calculateChildrenSize(recycler: RecyclerView.Recycler?) {
        val region = smoothRegion
        mTotalWidth = startPadding + region
        for (i in 0 until itemCount) {
            val v = recycler!!.getViewForPosition(i)
            addView(v)
            measureChildWithMargins(v, 0, 0)
            val rect = Rect()
            calculateItemDecorationsForChild(v, rect)
            val childWidth = getDecoratedMeasuredWidth(v)
            val childHeight = getDecoratedMeasuredHeight(v)
            var childRect: Rect? = allRects.get(i)
            if (null == childRect) {
                childRect = Rect()
            }
            childRect.set(
                mTotalWidth,
                (height - childHeight) shr 1,
                mTotalWidth + childWidth,
                (height + childHeight) shr 1
            )
            mTotalWidth += childWidth
            allRects.put(i, childRect)
            if (i == itemCount - 1) {
                mTotalWidth += (width - startPadding - childWidth)
            }
        }
        mTotalWidth += region
        //在数据个数发生变化或者当前滑动位置超过重宽度时重置滑动位置
        if (scrollOffset > mTotalWidth - width || lastItemSize != itemCount) {
            scrollOffset = region
        }
        lastItemSize = itemCount
    }


    override fun scrollHorizontallyBy(px: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        var dx = px
        if (scrollOffset + dx < 0) {
            dx = -scrollOffset
        } else if (scrollOffset + dx > mTotalWidth - width) {
            dx = mTotalWidth - width - scrollOffset
        }
        recyclerAndFillView(recycler, state)
        offsetChildrenHorizontal(-dx)
        scrollOffset += dx
        return dx
    }


    private fun recyclerAndFillView(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        if (itemCount == 0 || state!!.isPreLayout) return
        val w = width
        val h = height
        detachAndScrapAttachedViews(recycler!!)
        val displayRect = Rect(scrollOffset, 0, scrollOffset + w, h)
        for (i in 0 until itemCount) {
            val rect = allRects.get(i)
            val view = recycler.getViewForPosition(i)
            if (Rect.intersects(displayRect, rect)) {
                fillView(view, rect)
            } else {
                removeAndRecycleView(view, recycler)
            }
        }
    }

    private fun fillView(view: View, rect: Rect) {
        //基于00点获得开始点.
        val startPos = rect.left - startPadding - scrollOffset
        val progress = getProgress(abs(startPos).toFloat())
        val offset = if (startPos > 0) ((1 - progress) * paddingOffset).toInt() else 0
        addView(view)
        measureChildWithMargins(view, 0, 0)
        //基于开始点的正数
        val scale = (openSize * progress).toInt()
        onViewChange(view, progress)
        layoutDecorated(
            view,
            rect.left - scrollOffset + offset,
            rect.top,
            rect.right - scrollOffset + scale + offset,
            rect.bottom
        )
    }


    /**计算进度值*/
    private fun getProgress(startPos: Float): Float {
        //当平滑区<进度区
        val mSmoothRegion = if (smoothRegion < progressRegion) smoothRegion else 0
        return when {
            //开始点位于平滑区内==1
            startPos < smoothRegion -> 1f
            startPos < progressRegion -> 1 - (startPos - mSmoothRegion) / (progressRegion - mSmoothRegion)
            //开始位置大于平滑区进度为0
            else -> 0f
        }
    }

    private fun onViewChange(view: View, progress: Float) {
        ViewCompat.setZ(view, progress * 10)
        onViewByProgressChange?.invoke(view, progress)
    }

    fun getTopView(): View? {
        if (childCount == 0) {
            return null
        }
        var result: View? = null
        var lastAbs: Int = Int.MAX_VALUE
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val startAbs = abs(getDecoratedLeft(child!!) - startPadding)
            if (startAbs < lastAbs) {
                result = child
                lastAbs = startAbs
            }
        }
        return result
    }

    override fun getDecoratedLeft(child: View): Int {
        val position = getPosition(child)
        return allRects.get(position).left - scrollOffset
    }

    fun setOnViewByProgressChange(fun0: (view: View, progress: Float) -> Unit) {
        onViewByProgressChange = fun0
    }

    override fun smoothScrollToPosition(rv: RecyclerView, state: RecyclerView.State?, pos: Int) {
        super.smoothScrollToPosition(rv, state, pos)
        if (pos !in 0 until itemCount) return
        getSmoothScroller(rv).let {
            it.targetPosition = pos
            startSmoothScroll(it)
        }
    }

    private fun getSmoothScroller(recyclerView: RecyclerView): LinearSmoothScroller {
        return object : LinearSmoothScroller(recyclerView.context) {
            override fun calculateDxToMakeVisible(view: View, snapPreference: Int): Int =
                -(getDecoratedLeft(view) - startPadding)

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float = 0.3f
        }
    }
}