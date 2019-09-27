package com.gongziyi.demon.twoLevelHeader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gongziyi.demon.R
import kotlinx.android.synthetic.main.res_activity_two_level_header.*

/**
 * 自定义二楼/下拉刷新/及Lottie动画
 */
class TwoLevelHeaderActivity : AppCompatActivity() {

    /**权限控制*/
    var autoCode: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_activity_two_level_header)
        header.setOnTwoLevelListener {
            header.finishTwoLevel()
            autoCode
        }
        classics.canToTwoLevel = autoCode
        header.setEnablePullToCloseTwoLevel(true)
        header.setFloorDuration(500)
        header.setRefreshRage(0.5f)
        header.setFloorRage(1.5f)
        header.setMaxRage(2.5f)

    }
}
