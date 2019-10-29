package com.gongziyi.demon.chart

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.MultipleItemRvAdapter
import com.gongziyi.demon.chart.bean.DataBean
import com.gongziyi.demon.chart.provider.BarChartExampleProvider
import com.gongziyi.demon.chart.provider.CustomPieChartExampleProvider
import com.gongziyi.demon.chart.provider.PieChartExampleProvider

/**
 * Created on 2019/10/8
 * @author: gongziyi
 * Description:抽象图表适配器
 */
class ChartAdapter(datas: List<DataBean>? = null) :
    MultipleItemRvAdapter<DataBean, BaseViewHolder>(datas) {

    companion object {
        //饼状图
        val TYPE_PIE_CHART = 100
        //自定义饼图
        val TYPE_CUSTOM_PIE_CHART = 102
        //单柱图
        val TYPE_BAR_CHART = 200
        //双柱图
        val TYPE_DOUBLE_BAR_CHART = 201
        //堆叠图
        val TYPE_STACKING_BAR_CHART = 202

    }

    init {
        finishInitialize()
    }

    override fun registerItemProvider() {
        /**环形表格 右侧文本*/
        mProviderDelegate.registerProvider(PieChartExampleProvider())
        mProviderDelegate.registerProvider(BarChartExampleProvider())
        mProviderDelegate.registerProvider(CustomPieChartExampleProvider())
    }

    override fun getViewType(t: DataBean): Int = t.type

}


