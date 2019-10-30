package com.gongziyi.demon.chart.provider

import android.content.Context
import android.graphics.Color
import android.util.Size
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.gongziyi.demon.R
import com.gongziyi.demon.chart.ChartAdapter
import com.gongziyi.demon.chart.bean.DataBean
import com.gongziyi.demon.chart.bean.EntryData
import com.gongziyi.demon.chart.ui.GPicCharView
import com.gongziyi.demon.chart.ui.IPicCharDataHelper

/**
 * Created on 2019/10/22
 * @author: gongziyi
 * Description:饼图图例样式
 */
class PieChartLegendProvider :
    BaseItemProvider<DataBean, BaseViewHolder>() {


    override fun layout(): Int = R.layout.chart_pie_legend_layout

    override fun viewType(): Int = ChartAdapter.TYPE_CUSTOM_LEGEND_PIE_CHART

    private val mColors by lazy {
        intArrayOf(
            ContextCompat.getColor(mContext, R.color.color_08f),
            ContextCompat.getColor(mContext, R.color.color_3c8),
            ContextCompat.getColor(mContext, R.color.color_fa6),
            ContextCompat.getColor(mContext, R.color.color_caf),
            ContextCompat.getColor(mContext, R.color.color_f78),
            ContextCompat.getColor(mContext, R.color.color_fe2),
            ContextCompat.getColor(mContext, R.color.color_5ec),
            ContextCompat.getColor(mContext, R.color.color_88f)
        )
    }
    private val mShowdownColors by lazy {
        intArrayOf(
            ContextCompat.getColor(mContext, R.color.color_a08f),
            ContextCompat.getColor(mContext, R.color.color_a3c8),
            ContextCompat.getColor(mContext, R.color.color_afa6),
            ContextCompat.getColor(mContext, R.color.color_acaf),
            ContextCompat.getColor(mContext, R.color.color_af78),
            ContextCompat.getColor(mContext, R.color.color_afe2),
            ContextCompat.getColor(mContext, R.color.color_a5ec),
            ContextCompat.getColor(mContext, R.color.color_a88f)
        )
    }


    override fun convert(helper: BaseViewHolder, entry: DataBean, position: Int) {
        PieChartLegendViewHolder(helper)
            .initRecyclerView(mContext)
            .setColors(mColors, mShowdownColors)
            .setData(entry.listData)
            .setIndex(0)
    }


}


class PieChartLegendViewHolder(val helper: BaseViewHolder) : IPicCharDataHelper,
    BaseQuickAdapter.OnItemClickListener {
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        setIndex(position)
    }


    override fun getItemCount(): Int = mData?.size ?: 0

    override fun getValueByIndex(index: Int): Float = mData?.getOrNull(index)?.mValue ?: 0f

    override fun getTotalValue(): Float {
        if (mTotalValue == 0f) {
            mData?.forEach {
                mTotalValue += it.mValue
            }
        }
        return mTotalValue
    }

    fun getPieChar() = helper.getView<GPicCharView>(R.id.mPieChar)
    fun getRecyclerView() = helper.getView<RecyclerView>(R.id.mPieRecyclerView)


    private var mData: List<EntryData>? = null
    private var mTotalValue = 0f
    private var mAdapter: LegendAdapter? = null

    fun initRecyclerView(context: Context): PieChartLegendViewHolder {
        val recyclerView = getRecyclerView()
        if (recyclerView.layoutManager == null) {
            recyclerView.layoutManager = FullyGridLayoutManager(context, 3)
        }
        val adapter = recyclerView.adapter
        if (adapter == null) {
            mAdapter = LegendAdapter()
            mAdapter!!.onItemClickListener = this
            recyclerView.adapter = mAdapter
        } else {
            mAdapter = adapter as LegendAdapter
        }
        return this
    }

    fun setColors(colors: IntArray, showdownColors: IntArray): PieChartLegendViewHolder {
        getPieChar().apply {
            setColorList(colors)
            setShadowColors(showdownColors)
        }
        mAdapter?.setColors(colors)
        return this
    }

    fun setData(data: List<EntryData>?): PieChartLegendViewHolder {
        mData = data
        mTotalValue = 0f
        mAdapter?.setNewData(data)
        mAdapter?.setTotalValue(getTotalValue())
        getPieChar().setData(this)
        return this
    }

    fun setIndex(index: Int): PieChartLegendViewHolder {
        mAdapter?.setIndex(index)
        getPieChar().setHighlightIndex(index)
        mAdapter!!.notifyDataSetChanged()
        mData?.getOrNull(index)?.let {
            helper.setText(R.id.mUpPieText, "${it.mName},${it.mValue.toInt()}")
                .setText(R.id.mDownPieText, "${(it.mValue / getTotalValue() * 100).toInt()}%")
        }
        return this
    }
}

private class LegendAdapter :
    BaseQuickAdapter<EntryData, BaseViewHolder>(R.layout.chart_pie_legend_item) {

    private var mIndex = 0
    private var mColors: IntArray? = null
    private var mTotalValue = 0f

    fun setColors(colors: IntArray?) {
        mColors = colors
    }

    fun setIndex(index: Int) {
        mIndex = index
    }

    fun setTotalValue(float: Float) {
        mTotalValue = float
    }

    override fun convert(helper: BaseViewHolder, item: EntryData) {
        helper.apply {
            setBackgroundColor(R.id.mItemBgLayout, getBgColor())
            setBackgroundColor(R.id.mItemLegend, getLegendColor())
            setText(R.id.mItemName, item.mName)
            setText(R.id.mItemCount, "${item.mValue.toInt()}")
            setText(R.id.mItemPercentage, "${(item.mValue / mTotalValue * 100).toInt()}%")
        }
    }

    fun BaseViewHolder.getBgColor(): Int = if (adapterPosition == mIndex) {
        Color.parseColor("#F7F6F9")
    } else {
        Color.WHITE
    }

    fun BaseViewHolder.getLegendColor(): Int = mColors?.let {
        it[adapterPosition % it.size]
    } ?: Color.TRANSPARENT


}


private class FullyGridLayoutManager : GridLayoutManager {


    constructor(context: Context, spanCount: Int) : super(context, spanCount)

    constructor(context: Context, spanCount: Int, orientation: Int, reverseLayout: Boolean) : super(
        context, spanCount, orientation, reverseLayout
    )

    private val mMeasuredDimension = IntArray(2)

    override fun onMeasure(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        widthSpec: Int,
        heightSpec: Int
    ) {
        val widthMode = View.MeasureSpec.getMode(widthSpec)
        val heightMode = View.MeasureSpec.getMode(heightSpec)
        val widthSize = View.MeasureSpec.getSize(widthSpec)
        val heightSize = View.MeasureSpec.getSize(heightSpec)

        var width = 0
        var height = 0
        val count = itemCount
        val span = spanCount
        for (i in 0 until count) {
            measureScrapChild(
                recycler, i,
                View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                mMeasuredDimension
            )

            if (orientation == LinearLayoutManager.HORIZONTAL) {
                if (i % span == 0) {
                    width = width + mMeasuredDimension[0]
                }
                if (i == 0) {
                    height = mMeasuredDimension[1]
                }
            } else {
                if (i % span == 0) {
                    height = height + mMeasuredDimension[1]
                }
                if (i == 0) {
                    width = mMeasuredDimension[0]
                }
            }
        }

        when (widthMode) {
            View.MeasureSpec.EXACTLY -> width = widthSize
        }

        when (heightMode) {
            View.MeasureSpec.EXACTLY -> height = heightSize
        }

        setMeasuredDimension(width, height)
    }

    internal val mState = RecyclerView.State()

    private fun measureScrapChild(
        recycler: RecyclerView.Recycler, position: Int, widthSpec: Int,
        heightSpec: Int, measuredDimension: IntArray
    ) {
        val itemCount = mState.itemCount
        if (position < itemCount) {
            try {
                val view = recycler.getViewForPosition(0)
                if (view != null) {
                    val p = view.layoutParams as RecyclerView.LayoutParams
                    val childWidthSpec = ViewGroup.getChildMeasureSpec(
                        widthSpec,
                        paddingLeft + paddingRight, p.width
                    )
                    val childHeightSpec = ViewGroup.getChildMeasureSpec(
                        heightSpec,
                        paddingTop + paddingBottom, p.height
                    )
                    view.measure(childWidthSpec, childHeightSpec)
                    measuredDimension[0] = view.measuredWidth + p.leftMargin + p.rightMargin
                    measuredDimension[1] = view.measuredHeight + p.bottomMargin + p.topMargin
                    recycler.recycleView(view)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}
