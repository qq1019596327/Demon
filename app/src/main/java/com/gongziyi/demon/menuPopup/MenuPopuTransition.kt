package com.gongziyi.demon.menuPopup

import android.annotation.SuppressLint
import android.transition.AutoTransition
import android.transition.Transition


/**
 * Created on 2019/8/7
 * @author: gongziyi
 * Description:
 */
@SuppressLint("NewApi")
class MenuPopuTransition(val onTransition: ((isStart: Boolean) -> Unit)? = null) : AutoTransition(),
    Transition.TransitionListener {

    private var lastTime = 0L

    override fun onTransitionEnd(transition: Transition) {
        if (lastTime + duration <= System.currentTimeMillis()) {
            onTransition?.invoke(false)
        }
    }

    override fun onTransitionStart(transition: Transition?) {
        lastTime = System.currentTimeMillis()
        onTransition?.invoke(true)
    }

    override fun onTransitionResume(transition: Transition?) = Unit
    override fun onTransitionPause(transition: Transition?) = Unit
    override fun onTransitionCancel(transition: Transition?) = Unit

    init {
        addListener(this)
        duration = 300
    }
}