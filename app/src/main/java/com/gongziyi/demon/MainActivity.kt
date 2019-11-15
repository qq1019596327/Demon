package com.gongziyi.demon

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gongziyi.demon.arouter.ARouterActivity
import com.gongziyi.demon.chart.MChartActivity
import com.gongziyi.demon.constraintDemon.ConstraintLayoutActivity
import com.gongziyi.demon.coordinator.CoordinatorLayoutActivity
import com.gongziyi.demon.flexBox.FlexBoxActivity
import com.gongziyi.demon.gallery.ResourceGalleryActivity
import com.gongziyi.demon.gyroscope.GyroscopeActivity
import com.gongziyi.demon.menuPopup.MenuPopupActivity
import com.gongziyi.demon.shadowTest.ShadowTestActivity
import com.gongziyi.demon.twoLevelHeader.TwoLevelHeaderActivity
import kotlinx.android.synthetic.main.res_activity_main.*

class MainActivity : AppCompatActivity() {

    private val data by lazy {
        arrayListOf(
            "弹性盒子模型" to FlexBoxActivity::class.java
            , "二楼效果" to TwoLevelHeaderActivity::class.java
            , "弹出菜单" to MenuPopupActivity::class.java
            , "ConstraintLayout进阶用法" to ConstraintLayoutActivity::class.java
            , "路由测试" to ARouterActivity::class.java
            , "陀螺仪测试Activity" to GyroscopeActivity::class.java
            , "报表示例" to MChartActivity::class.java
            , "阴影测试" to ShadowTestActivity::class.java
            , "资源画廊" to ResourceGalleryActivity::class.java
            , "CoordinatorLayout使用" to CoordinatorLayoutActivity::class.java
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_activity_main)
        mRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mRecyclerView.adapter = MainAdapter(data).apply {
            setOnItemClickListener { adapter, view, position ->
                startActivity(Intent(this@MainActivity, data[position].second))
            }
        }
    }
}
