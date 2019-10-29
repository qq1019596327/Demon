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
            centerText = generateCenterSpannableText("高端设备,5000","40%")
        }

        mPieChart.legend.apply {

        }
    }



    private fun generateCenterSpannableText(top: String, bottom: String): SpannableString {
        return SpannableString("$top\n$bottom").apply {
            setSpan(RelativeSizeSpan(1f), 0, top.length, 0)
            setSpan(StyleSpan(Typeface.NORMAL), 0, top.length, 0)
            setSpan(ForegroundColorSpan(Color.parseColor("#8A8C99")), 0, top.length, 0)
            setSpan(RelativeSizeSpan(1.4f),length-bottom.length, length , 0)
            setSpan(StyleSpan(Typeface.BOLD), length-bottom.length, length , 0)
            setSpan(ForegroundColorSpan(Color.parseColor("#33394E")), length-bottom.length, length , 0)
        }
    }
}
