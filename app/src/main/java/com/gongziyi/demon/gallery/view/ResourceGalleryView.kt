package com.gongziyi.demon.gallery.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gongziyi.demon.gallery.adpater.GalleryLayoutManager
import com.gongziyi.demon.gallery.adpater.GallerySnapHelper

/**
 * Created on 2019/11/4
 * @author: gongziyi
 * Description:资源画廊view
 */
class ResourceGalleryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {


    private var mListener: IGalleryScrollChangeListener? = null

    private val mLayoutManager: GalleryLayoutManager by lazy {
        getDefaultLayoutManager()
    }
    private val mSnapHelper: GallerySnapHelper by lazy {
        getDefaultSnapHelper()
    }

    private var currentAdapterPosition = NO_POSITION


    init {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_IDLE) {
                    val pos = getActiveViewPosition()
                    if (pos == NO_POSITION || pos == currentAdapterPosition) {
                        return
                    }
                    onActiveCardChange(pos, false)
                }
            }
        })

    }


    /**获取默认布局管理器*/
    private fun getDefaultLayoutManager() = GalleryLayoutManager().let {
        it.setOnViewByProgressChange { v, p -> mListener?.onViewChangeCall(v, p) }
        it
    }

    /**获取默认滑动捕捉助手*/
    private fun getDefaultSnapHelper() = GallerySnapHelper()

    /**
     * 选中变更
     */
    fun onActiveCardChange(pos: Int, isScroll: Boolean = true) {
        if (pos !in 0 until mLayoutManager.itemCount) return
        if (isScroll && currentAdapterPosition != pos) {
            smoothScrollToPosition(pos)
        }
        mListener?.onScrollIdleListener(pos)
        currentAdapterPosition = pos

    }


    /**获取当前活动的视图位置*/
    private fun getActiveViewPosition(): Int =
        mLayoutManager.getTopView()?.let { getChildAdapterPosition(it) } ?: NO_POSITION


    /**覆盖setAdapter*/
    override fun setAdapter(adapter: Adapter<*>?) {
        layoutManager = mLayoutManager
        mSnapHelper.attachToRecyclerView(this)
        setHasFixedSize(true)
        super.setAdapter(adapter)
    }

    /**覆盖setLayoutManager*/
    override fun setLayoutManager(layout: LayoutManager?) {
        if (layout is GalleryLayoutManager) {
            super.setLayoutManager(layout)
        }
    }

    /**
     * 添加相关监听
     */
    fun addOnScrollChangeListener(listener: IGalleryScrollChangeListener) {
        mListener = listener
    }

    fun setStartPadding(value: Int) {
        mLayoutManager.startPadding = value
        mSnapHelper.startPadding = value
    }

    fun setProgressRegion(value: Int) {
        mLayoutManager.progressRegion = value
    }

    fun setSmoothRegion(value: Int) {
        mLayoutManager.smoothRegion = value
    }

    fun setPaddingOffset(value: Int) {
        mLayoutManager.expandOffset = value
    }

    fun setMagnification(value: Float) {
        mLayoutManager.magnification = value
    }
}


interface IGalleryScrollChangeListener {
    /**视图根据进度变更*/
    fun onViewChangeCall(view: View, progress: Float)

    fun onScrollIdleListener(activePosition: Int)

}


