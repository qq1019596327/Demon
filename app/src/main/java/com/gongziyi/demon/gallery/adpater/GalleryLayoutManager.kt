package com.gongziyi.demon.gallery.adpater

import android.graphics.Rect
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.gongziyi.demon.R
import kotlin.math.abs

/**
 * Created on 2019/11/1
 * @author: gongziyi
 * Description:
 */


class GalleryLayoutManager : RecyclerView.LayoutManager() {
    private var totalWidth: Int = 0
    private var scrollOffset: Int = 0
    private val allRects: SparseArray<Rect> = SparseArray<Rect>()

    var startPadding = 0
    var progressRegion = 0

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
        totalWidth = startPadding

        for (i in 0 until itemCount) {
            val v = recycler!!.getViewForPosition(i)
            addView(v)
            measureChildWithMargins(v, 0, 0)
            calculateItemDecorationsForChild(v, Rect())
            val childWidth = getDecoratedMeasuredWidth(v)
            val childHeight = getDecoratedMeasuredHeight(v)
            var childRect: Rect? = allRects.get(i)
            if (null == childRect) {
                childRect = Rect()
            }
            childRect!!.set(
                    totalWidth,
                    (height - childHeight) / 2,
                    totalWidth + childWidth,
                    (height + childHeight) / 2
            )
            totalWidth += childWidth
            allRects.put(i, childRect)
            if (i == itemCount - 1) {
                val filling = width - startPadding - childWidth
                totalWidth += filling
            }
        }
    }


    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        var dx = dx
        if (scrollOffset + dx < 0) {
            dx = -scrollOffset
        } else if (scrollOffset + dx > totalWidth - width) {
            dx = totalWidth - width - scrollOffset
        }
        offsetChildrenHorizontal(-dx)
        recyclerAndFillView(recycler, state)
        scrollOffset += dx
        return dx
    }


    private fun recyclerAndFillView(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        if (itemCount == 0 || state!!.isPreLayout) return
        val w = width
        val h = height
        detachAndScrapAttachedViews(recycler!!)
        val displayRect = Rect(scrollOffset, 0, scrollOffset + w, h)
        var progress: Float
        var offset: Int
        for (i in 0 until itemCount) {
            val rect = allRects.get(i)
            val view = recycler.getViewForPosition(i)

            if (Rect.intersects(displayRect, rect)) {
                measureChildWithMargins(view, 0, 0)
                addView(view)
                //中心点计算
                val startPos = abs((rect.left - startPadding - scrollOffset)).toFloat()
                if (startPos < progressRegion) {
                    progress = 1 - startPos / progressRegion
                } else {
                    progress = 0f
                }
                val scale = (rect.width() * 0.8f * progress).toInt()

                onViewChange(view, progress)
                layoutDecorated(view,
                        rect.left - scrollOffset ,
                        rect.top,
                        rect.right - scrollOffset + scale ,
                        rect.bottom
                )
            } else {
                removeAndRecycleView(view, recycler)
            }
        }
    }


    private fun onViewChange(view: View, progress: Float) {
        ViewCompat.setZ(view, progress * 10)
        if (view is MotionLayout) {
            view.progress = progress
        }
    }

    fun getTopView(): View? {
        if (childCount == 0) {
            return null
        }
        var result: View? = null
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val viewLeft = getDecoratedLeft(child!!)
            if (viewLeft >= progressRegion) {
                continue
            }
            if (viewLeft in -progressRegion..progressRegion)
                result = child
        }

        return result
    }

}