package com.gongziyi.demon.chart.bean

import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieEntry
import kotlin.math.max

/**
 * Created on 2019/10/15
 * @author: gongziyi
 * Description:
 */
class DataBean(val type: Int) {

    var listData: List<EntryData>? = null


    fun getPieData(): List<PieEntry> = arrayListOf<PieEntry>().apply {
        listData?.forEach {
            add(PieEntry(it.mValue, it.mName, it))
        }
    }

    fun getBarData(): List<BarEntry> = arrayListOf<BarEntry>().apply {
        listData?.forEachIndexed { index, it ->
            add(BarEntry(index.toFloat(), it.mValue, it ))
        }
    }

    fun getMaxValue(): Float {
        var maxValue = 0f
        listData?.forEach {
            maxValue = max(maxValue, it.mValue)
        }
        return maxValue
    }

}

class EntryData() {
    var mValue: Float = 0f
    var lastValue: Float = 0f
    var mName: String = ""
    var lastName: String = ""
}