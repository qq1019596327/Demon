package com.gongziyi.demon.bossBar

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.gongziyi.demon.R
import kotlinx.android.synthetic.main.res_activity_boss_bar.*
import kotlin.properties.Delegates

class BossBarActivity : AppCompatActivity(), View.OnClickListener {


    private val mControlGroup by lazy {
        arrayOf(mMenuCityIcon, mMenuOverviewIcon)
    }

    /**选中页面监听器*/
    private var selectedPager: Int by Delegates.observable(-1) { k, old, position ->
        if (old != position) {
            if (position != -1) {
                val controlGroup = mControlGroup[position]
                controlGroup.selectionIcon(true)
                layoutBarView?.showUplift(controlGroup)
            }else{
                layoutBarView?.reset()
            }
            // 修改选中状态
            if (old != -1) {
                mControlGroup[old].selectionIcon(false)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_activity_boss_bar)
        mMenuCityIcon.setOnClickListener(this)
        mMenuCityText.setOnClickListener(this)
        mMenuOverviewIcon.setOnClickListener(this)
        mMenuOverviewText.setOnClickListener(this)
        mMenuTopIcon.setOnClickListener(this)
        mMenuTopText.setOnClickListener(this)
        mMenuMotionLayout.setOnClickListener(this)
    }



    override fun onClick(v: View?) {
        when (v) {
            mMenuMotionLayout -> selectedPager = -1
            mMenuCityIcon, mMenuCityText -> selectedPager = 0
            mMenuOverviewIcon, mMenuOverviewText -> selectedPager = 1
            mMenuTopIcon, mMenuTopText -> {
                //滑动置顶
            }
        }
    }

    private fun LottieAnimationView.selectionIcon(isSelect: Boolean) {
        speed = if (isSelect) Math.abs(speed) else -Math.abs(speed)
        playAnimation()
    }

}
