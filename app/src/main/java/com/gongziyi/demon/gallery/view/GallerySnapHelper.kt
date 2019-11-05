package com.gongziyi.demon.gallery.adpater

import android.view.View
import androidx.recyclerview.widget.*

/**
 * Created on 2019/11/1
 * @author: gongziyi
 * Description:
 */

class GallerySnapHelper : LinearSnapHelper() {
    var startPadding = 100

    private var mHorizontalHelper: OrientationHelper? = null
    private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return mHorizontalHelper!!
    }

    //判断支持水平滚动，修改水平方向的位置，是修改的out[0]的值
    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View)
        : IntArray? = IntArray(2).let {
        if (layoutManager.canScrollHorizontally()) {
            it[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager))
        } else {
            it[0] = 0
        }
        it
    }

    private fun distanceToStart(targetView: View, helper: OrientationHelper)
        : Int = helper.getDecoratedStart(targetView) - helper.startAfterPadding - startPadding


    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        if (layoutManager is GalleryLayoutManager) {
            return layoutManager.getTopView()
        }
        return super.findSnapView(layoutManager)
    }


}
