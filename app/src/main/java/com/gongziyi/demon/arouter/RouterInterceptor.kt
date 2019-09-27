package com.gongziyi.demon.arouter

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor

/**
 * Created on 2019/7/31
 * @author: gongziyi
 * Description:路由拦截器
 */
@Interceptor(priority = 9, name = "测试路由拦截器")
class RouterInterceptor : IInterceptor {
    val userRights: Int = 0b00111

    override fun process(postcard: Postcard, callback: InterceptorCallback) {
        val extras = postcard.extra
        if (userRights and extras == extras && extras != 0) {
            callback.onContinue(postcard)  // 处理完成，交还控制权

        } else {
            callback.onInterrupt(RuntimeException("我觉得有点异常"));      // 觉得有问题，中断路由流程
        }
    }

    override fun init(context: Context?) {

    }

}