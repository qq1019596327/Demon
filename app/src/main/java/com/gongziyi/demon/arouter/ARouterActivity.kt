package com.gongziyi.demon.arouter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.gongziyi.demon.R

class ARouterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_activity_arouter)
    }


    fun normalToB(view: View) {
        ARouter.getInstance()
            .build("/main/activity")
            .navigation()
    }

    fun argumentToB(view: View) {
        ARouter.getInstance()
            .build("/main/activity")
            .withString("name", "徐志摩曾说过：“一生中至少该有一次，为了某个人而忘记了自己，不求结果，不求同行，不求曾经拥有，甚至不求你爱我，只求在我最美的年华里，遇见你。”")
            .navigation()
    }

    fun argumentToBAndCall(view: View) {
        ARouter.getInstance()
            .build("/main/activity")
            .withString("name", "有一句话说：“人的一生会遇到两个人，一个惊艳了时光，一个温柔了岁月。”")
            .navigation(this, object : NavigationCallback {
                override fun onLost(postcard: Postcard?) {
                    Log.i("==========","onLost")
                }

                override fun onFound(postcard: Postcard?) {
                    Log.i("==========","onFound")
                }

                override fun onInterrupt(postcard: Postcard?) {
                    Log.i("==========","onInterrupt")

                }

                override fun onArrival(postcard: Postcard?) {
                    Log.i("==========","onArrival")
                }

            })
    }
}
