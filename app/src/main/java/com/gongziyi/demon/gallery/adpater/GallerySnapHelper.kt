package com.gongziyi.demon.gallery.adpater

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Created on 2019/11/1
 * @author: gongziyi
 * Description:
 */

class GallerySnapHelper : LinearSnapHelper() {
    private var mHorizontalHelper: OrientationHelper? = null
    var startPadding = 100

//    private var recyclerView: RecyclerView? = null
//
//
//    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
//        super.attachToRecyclerView(recyclerView)
//        if (recyclerView != null && recyclerView.layoutManager !is CardSliderLayoutManager) {
//            throw InvalidParameterException("LayoutManager must be instance of CardSliderLayoutManager")
//        }
//        this.recyclerView = recyclerView
//    }

    override fun calculateDistanceToFinalSnap(
            layoutManager: RecyclerView.LayoutManager,
            targetView: View
    ): IntArray? {
        val out = IntArray(2)
        //判断支持水平滚动，修改水平方向的位置，是修改的out[0]的值
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager))
        } else {
            out[0] = 0
        }
        return out
    }

    private fun distanceToStart(targetView: View, helper: OrientationHelper): Int {
        val decoratedStart = helper.getDecoratedStart(targetView)
        return decoratedStart - helper.startAfterPadding - startPadding
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        return findStartView(layoutManager, getHorizontalHelper(layoutManager))
    }

    private fun findStartView(layoutManager: RecyclerView.LayoutManager, helper: OrientationHelper): View? {
        when (layoutManager) {
            is LinearLayoutManager -> {
                val firstChild = layoutManager.findFirstVisibleItemPosition()
                val lastChild = layoutManager.findLastVisibleItemPosition()
                if (firstChild == RecyclerView.NO_POSITION) return null


                //这行的作用是如果是最后一个，翻到最后一条，解决显示不全的问题
                if (lastChild == layoutManager.getItemCount() - 1) {
                    return layoutManager.findViewByPosition(lastChild)
                }

                val child = layoutManager.findViewByPosition(firstChild)
                //获取偏左显示的Item
                val decoratedEnd = helper.getDecoratedEnd(child)
                val decoratedMeasurement = helper.getDecoratedMeasurement(child)
                return if (decoratedEnd >= decoratedMeasurement / 2 && decoratedEnd > 0) {
                    child
                } else {
                    layoutManager.findViewByPosition(firstChild + 1)
                }
            }
            is GalleryLayoutManager -> {
                return layoutManager.getTopView()
            }
        }
        return super.findSnapView(layoutManager)
    }


    private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return mHorizontalHelper!!
    }
}
