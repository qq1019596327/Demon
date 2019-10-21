package com.gongziyi.demon.chart

import android.os.Bundle
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
    }
}