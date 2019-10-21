package com.gongziyi.demon.chart

import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.gongziyi.demon.R
import com.gongziyi.demon.chart.bean.EntryData
import kotlinx.android.synthetic.main.custom_marker_view.view.*
import java.text.DecimalFormat

/**
 * Created on 2019/10/15
 * @author: gongziyi
 * Description:
 */
class CurrencyMarkerView(context: Context) : MarkerView(context, R.layout.custom_marker_view) {

    val mFormat by lazy {
        DecimalFormat(",##0.00")
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.let {
            val data = it.data
            tvContent.text = when {
                data is String -> data
                data is Array<*> -> {
                    val buffer = StringBuffer()
                    data.forEach {
                        if (buffer.isNotEmpty()) {
                            buffer.append("\n")
                        }
                        when (it) {
                            is EntryData ->
                                buffer.append("${it.mName}:${mFormat.format(it.mValue)}")
                            is String ->
                                buffer.append(it)
                        }
                    }
                    buffer.toString()
                }
                data is EntryData -> {
                    "${data.mName}:${mFormat.format(data.mValue)}"
                }
                else -> ""
            }
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2) + 2).toFloat(), (-height).toFloat())
    }


}