package com.gongziyi.demon.chart.provider

import android.view.View
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseViewHolder
import com.gongziyi.demon.R
import com.gongziyi.demon.chart.ChartAdapter
import com.gongziyi.demon.chart.bean.DataBean
import com.gongziyi.demon.chart.ui.GPicCharView
import com.gongziyi.demon.chart.ui.IPicCharDataHelper

/**
 * Created on 2019/10/22
 * @author: gongziyi
 * Description:饼图item样式
 */
class PieChartItemProvider : AbstractChartProvider<DataBean>() {


    override fun layout(): Int = R.layout.chart_pie_item_layout

    override fun viewType(): Int = ChartAdapter.TYPE_CUSTOM_PIE_CHART

    var index = 0
    val mColors by lazy {
        intArrayOf(
            ContextCompat.getColor(mContext, R.color.color_08f),
            ContextCompat.getColor(mContext, R.color.color_f78)
        )
    }
    val mShowdownColors by lazy {
        intArrayOf(
            ContextCompat.getColor(mContext, R.color.color_a08f),
            ContextCompat.getColor(mContext, R.color.color_af78)
        )
    }

    override fun convert(helper: BaseViewHolder, entry: DataBean, position: Int) {
        helper.getView<GPicCharView>(R.id.mPieChar).apply {
            val list = entry.listData
            setColorList(mColors)
            setShadowColors(mShowdownColors)
            setHighlightIndex(index)
            setData(object : IPicCharDataHelper {
                override fun getTotalValue(): Float = list?.let {
                    var count = 0f
                    it.forEach {
                        count += it.mValue
                    }
                    count
                } ?: 0f

                override fun getItemCount() = list?.size ?: 0
                override fun getValueByIndex(index: Int) =
                    list?.getOrNull(index)?.mValue ?: 0f
            })
        }
        helper.getView<View>(R.id.mMoneySwitchIcon).setOnClickListener {
            helper.getView<GPicCharView>(R.id.mPieChar).setHighlightIndex(++index % 2)
        }
    }
}
