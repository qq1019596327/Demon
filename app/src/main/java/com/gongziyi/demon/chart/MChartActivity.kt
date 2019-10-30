package com.gongziyi.demon.chart

import android.os.Bundle
import android.util.Log
import androidx.annotation.IntRange
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gongziyi.demon.R
import com.gongziyi.demon.chart.bean.DataBean
import com.gongziyi.demon.chart.bean.EntryData
import kotlinx.android.synthetic.main.res_activity_chart.*

/**
 * Created on 2019/10/8
 * @author: gongziyi
 * Description: 报表展示
 */
class MChartActivity : AppCompatActivity() {

    private val data by lazy {
        arrayListOf<DataBean>().apply {
            add(DataBean(ChartAdapter.TYPE_CUSTOM_PIE_CHART).apply {
                listData = arrayListOf<EntryData>().apply {
                    for (i in 0..1) {
                        val entry = EntryData()
                        entry.mValue = (Math.random() * 10).toFloat()
                        entry.mName = "饼图数据$i"
                        add(entry)
                    }
                }
            })
            add(DataBean(ChartAdapter.TYPE_CUSTOM_LEGEND_PIE_CHART).apply {
                listData = arrayListOf<EntryData>().apply {
                    for (i in 0..6) {
                        val entry = EntryData()
                        entry.mValue = (Math.random() * 1000).toInt().toFloat()
                        entry.mName = "饼图${i}数据"
                        add(entry)
                    }
                }
            })
            add(DataBean(ChartAdapter.TYPE_PIE_CHART).apply {
                listData = arrayListOf<EntryData>().apply {
                    for (i in 0..3) {
                        val entry = EntryData()
                        entry.mValue = (Math.random() * 10).toFloat()
                        entry.mName = "饼图数据$i"
                        add(entry)
                    }
                }
            })
            add(DataBean(ChartAdapter.TYPE_BAR_CHART).apply {
                listData = arrayListOf<EntryData>().apply {
                    for (i in 0..100) {
                        val entry = EntryData()
                        entry.mValue = (Math.random() * 1000000).toFloat()
                        entry.mName = "单柱图$i"
                        add(entry)
                    }
                }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_activity_chart)
        mRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = ChartAdapter(data)
        }

        for (i in 0..100) {
            val max = (Math.random() * 1000 + 10).toInt()
            val filling = max.offsetFilling()
            println("原始值=$max; 原始值.offsetFilling=$filling; virtualPosition=2")
        }


    }


    /**
     * 抵消填充函数
     * 例: 2234.offsetFilling(0)=2500
     * 例: 2634.offsetFilling(0)=3000
     * 例: 4.offsetFilling(0)=5
     * 例: 89.offsetFilling(0)=90
     * 例: 10.offsetFilling(0)=15
     * 例: 14.offsetFilling(2)=20
     * 例: 18.offsetFilling(2)=25
     * 例: 11.offsetFilling(2)=15
     * 只能对10以上的数字进行抵消填充
     * @param virtualPosition 补位 默认=2 可输入范围0..9
     */
    private fun Int.offsetFilling(@IntRange(from = 0L, to = 9L) virtualPosition: Int = 2): Int {
        val length = toString().length
        //最小倍数单位
        var multiple = 1
        //抵消的最小单位
        var offset = 1
        //虚拟值
        var virtual = 0
        //判断是否符合计算规则
        if (length >= 2) {
            for (i in 0 until length - 2) {
                multiple *= 10
            }
            var toInt = toString()[1].toString().toInt() + virtualPosition
            if (toInt >= 10) {
                virtual = virtualPosition
                toInt %= 10
            }
            if (toInt >= 5) {
                multiple *= 10
            } else {
                val division = if (virtual == 0) 5 + virtualPosition else 5
                offset = division - toInt
            }
        }
        val filling = (this / multiple + virtual + offset) * multiple
        Log.i("offsetFilling", "Int=$this, Int.offsetFilling($virtualPosition)=$filling")
        return filling
    }

}