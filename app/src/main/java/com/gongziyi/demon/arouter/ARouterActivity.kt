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
            .withString("name", "单p,群p，交换，野合，暴露，单男，双飞，sm,调教，捆绑，窒息交配，换装，易装，人兽，道具。")
            .navigation()
    }

    fun argumentToBAndCall(view: View) {
        ARouter.getInstance()
            .build("/main/activity")
            .withString("name", "单p,群p，交换，野合，暴露，单男，双飞，sm,调教，捆绑，窒息交配，换装，易装，人兽，道具。")
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
