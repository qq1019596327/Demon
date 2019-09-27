package com.gongziyi.demon.arouter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.gongziyi.demon.R
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.res_activity_brouter.*
import java.security.MessageDigest


@Route(path = "/main/activity", extras = 0b00010)
class BRouterActivity : AppCompatActivity() {

    @Autowired(name = "name")
    @JvmField
    var mTextString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_activity_brouter)
        ARouter.getInstance().inject(this)

        mTextString?.let { text1.text = it }
    }

    fun onFinish(v: View) {
        text1.text = "{\"projectId\":[\"55\"],\"pageNo\":[\"1\"],\"pageSize\":[\"10\"]}".toMd5("yd_cloud").toUpperCase()
    }
}


/**
 * md5加密
 *
 * @param str 被加密数据
 * @return 加密后数据 如果出现异常则返回""
 */
private fun String.toMd5(slat: String? = null): String = try {
    val sb = StringBuffer()
    val dataStr = if (slat.isNullOrBlank()) this else this + slat
    MessageDigest.getInstance("MD5")?.apply {
        update(dataStr.toByteArray())
        digest().let {
            for (i in it.indices) {
                var v = it[i].toInt()
                v = if (v < 0) 0x100 + v else v
                val cc = Integer.toHexString(v)
                if (cc.length == 1)
                    sb.append('0')
                sb.append(cc)
            }
        }
    }
    sb.toString()
} catch (e: Exception) {
    ""
}