package com.gongziyi.demon.chart.provider

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.github.mikephil.charting.charts.BarChart
import com.gongziyi.demon.R
import com.gongziyi.demon.chart.CurrencyMarkerView

/**
 * Created on 2019/10/15
 * @author: gongziyi
 * Description:抽象提供者
 */
abstract class AbstractChartProvider<T>() : BaseItemProvider<T, BaseViewHolder>() {

    protected val colors by lazy {
        arrayListOf(
            ContextCompat.getColor(mContext, R.color.color_3aa)
            , ContextCompat.getColor(mContext, R.color.color_fac)
            , ContextCompat.getColor(mContext, R.color.color_28e)
            , ContextCompat.getColor(mContext, R.color.color_fb7)
        )
    }

    protected fun getColorByIndex(index: Int) = colors[index % colors.size]


    /**开启弹窗*/
    protected fun BarChart.openFloatPopup() {
        val markerView = CurrencyMarkerView(mContext)
        markerView.chartView = this
        marker = markerView
    }


    /**设置标注最大值*/
    protected fun BarChart.setMaximum(maxV: Float) {
        when {
            maxV <= 0.1f -> {
                axisLeft.granularity = 0.01f
                axisLeft.axisMinimum = 0f
                axisLeft.axisMaximum = 0.1f
            }
            maxV <= 1f -> {
                axisLeft.granularity = 0.1f
                axisLeft.axisMinimum = 0f
                axisLeft.axisMaximum = 1f
            }
            else -> {
                val max = maxV.toInt().offsetFilling().toFloat()
                axisLeft.granularity = max / 4
                axisLeft.axisMinimum = 0f
                axisLeft.axisMaximum = max
            }
        }
    }

    /**
     * 抵消填充函数
     * 例: 2234.offsetFilling()=2500
     * 例: 2634.offsetFilling()=3000
     * 例: 4.offsetFilling()=5
     * 例: 89.offsetFilling()=90
     */

    private fun Int.offsetFilling(): Int = if (this <= 10) {
        this
    } else {
        val length = toString().length
        var multiple = 1
        var offset = 1
        if (length > 2) {
            for (i in 0 until length - 2) {
                multiple *= 10
            }
            val toInt = toString()[1].toString().toInt()
            if (toInt >= 5) {
                multiple *= 10
            } else {
                offset = 5 - toInt
            }
        }
        ((this / multiple + offset) * multiple)
    }

}