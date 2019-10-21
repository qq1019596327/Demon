package com.gongziyi.demon.chart.provider

import android.graphics.Color
import com.chad.library.adapter.base.BaseViewHolder
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.gongziyi.demon.R
import com.gongziyi.demon.chart.ChartAdapter
import com.gongziyi.demon.chart.bean.DataBean

/**
 * Created on 2019/10/8
 * @author: gongziyi
 * Description:柱状图
 */

class BarChartExampleProvider : AbstractChartProvider<DataBean>() {


    override fun layout(): Int = R.layout.chart_bar_example

    override fun viewType(): Int = ChartAdapter.TYPE_BAR_CHART

    override fun convert(helper: BaseViewHolder, entry: DataBean, position: Int) {
        helper.getView<BarChart>(R.id.mBarChar)?.apply {
            initChar(this)
            val yVals = entry.getBarData()
            val dataSet = BarDataSet(yVals, "Election Results")
            dataSet.setDrawIcons(false)
            dataSet.colors = colors

            data = BarData(dataSet).apply {
                setValueFormatter(DefaultValueFormatter(2))
                setValueTextSize(10f)
                setValueTextColor(Color.BLACK)
            }
            xAxis.setValueFormatter { value, axis ->
                entry.listData?.getOrNull(value.toInt())?.mName ?: ""
            }
            val size = yVals.size
            xAxis.axisMinimum = 0f
            xAxis.axisMaximum = if (size == 0) 1f else size.toFloat()
            xAxis.labelCount = size
            xAxis.setCenterAxisLabels(true)
            xAxis.setAvoidFirstLastClipping(false)
            setMaximum(entry.getMaxValue())
            highlightValues(null)
            invalidate()
        }
    }


    private fun initChar(chart: BarChart) {
        chart.apply {
            //柱状图的阴影
            setDrawBarShadow(false)
            //是否显示表格背景颜色
            setDrawGridBackground(false)
            //设置双击,两指拉伸等交互的开关
            setScaleEnabled(false)
            isDoubleTapToZoomEnabled = false
            openFloatPopup()
        }

        chart.xAxis.apply {
            //设置X轴显示位置
            position = XAxis.XAxisPosition.BOTTOM
            //X轴纵向分割线，一般不设置显示
            setDrawGridLines(false)
            // X轴显示Value值的精度，与自定义X轴返回的Value值精度一致
            textSize = 10f
            //设置X轴显示文字旋转角度-60意为逆时针旋转60度
            labelRotationAngle = 60f
            axisLineWidth = 1f
            granularity = 1f
        }

        chart.axisLeft.apply {
            // 网格线 宽度 颜色 虚线
            setDrawGridLines(true)
            granularity = 1.0f
            gridLineWidth = 0.5f
            //启用虚线
            enableGridDashedLine(0.2f, 0.1f, 0f)
            //不绘制沿轴线
            setDrawAxisLine(false)
            //是否绘制标签
        }

        chart.axisRight.isEnabled = false

    }
}
