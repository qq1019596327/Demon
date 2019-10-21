package com.gongziyi.demon.chart.provider

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.chad.library.adapter.base.BaseViewHolder
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.gongziyi.demon.R
import com.gongziyi.demon.chart.ChartAdapter
import com.gongziyi.demon.chart.bean.DataBean

/**
 * Created on 2019/10/8
 * @author: gongziyi
 * Description:饼状图
 */

class PieChartExampleProvider : AbstractChartProvider<DataBean>() {


    override fun layout(): Int = R.layout.chart_pie_example

    override fun viewType(): Int = ChartAdapter.TYPE_PIE_CHART

    override fun convert(helper: BaseViewHolder, entry: DataBean, position: Int) {
        helper.getView<PieChart>(R.id.mPieChar)?.apply {
            initChar(this)
            val dataSet = PieDataSet(entry.getPieData(), "Election Results")
            dataSet.setDrawIcons(false)
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0f, 40f)
            dataSet.selectionShift = 5f
            dataSet.colors = colors

            data = PieData(dataSet).apply {
                setValueFormatter(PercentFormatter())
                setValueTextSize(11f)
                setValueTextColor(Color.BLACK)
            }

            highlightValues(null)
            invalidate()
        }
    }


    private fun initChar(mPieChart: PieChart) {

        mPieChart.apply {

        }

        mPieChart.legend.apply {

        }

    }


    private fun generateCenterSpannableText(): SpannableString {
        return SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda").apply {
            setSpan(RelativeSizeSpan(1.7f), 0, 14, 0)
            setSpan(StyleSpan(Typeface.NORMAL), 14, length - 15, 0)
            setSpan(ForegroundColorSpan(Color.GRAY), 14, length - 15, 0)
            setSpan(RelativeSizeSpan(.8f), 14, length - 15, 0)
            setSpan(StyleSpan(Typeface.ITALIC), length - 14, length, 0)
            setSpan(ForegroundColorSpan(ColorTemplate.getHoloBlue()), length - 14, length, 0)
        }

    }
}
