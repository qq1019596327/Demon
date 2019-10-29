package com.gongziyi.demon.chart.provider

import com.chad.library.adapter.base.BaseViewHolder
import com.gongziyi.demon.R
import com.gongziyi.demon.chart.ChartAdapter
import com.gongziyi.demon.chart.bean.DataBean
import com.gongziyi.demon.chart.ui.GPicCharView
import com.gongziyi.demon.chart.ui.IPicCharDataHelper

/**
 * Created on 2019/10/22
 * @author: gongziyi
 * Description:自定义饼图提供者
 */
class CustomPieChartExampleProvider : AbstractChartProvider<DataBean>() {


    override fun layout(): Int = R.layout.chart_pie_custom_example

    override fun viewType(): Int = ChartAdapter.TYPE_CUSTOM_PIE_CHART

    var index = 0

    override fun convert(helper: BaseViewHolder, entry: DataBean, position: Int) {
        helper.getView<GPicCharView>(R.id.mPieChar).apply {
            val list = entry.listData
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
            setOnClickListener {
                index++
                setHighlightIndex(index % (list?.size ?: 0))

            }
        }
    }
}
