package com.gongziyi.demon.menuPopup

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.constraintlayout.widget.ConstraintSet
import com.gongziyi.demon.R
import kotlinx.android.synthetic.main.res_activity_menu_popup.*

class MenuPopupActivity : AppCompatActivity() {


    private lateinit var initialState: ConstraintSet
    private lateinit var showState: ConstraintSet
    private val mTransition by lazy {
        MenuPopuTransition {
            if (it) {
                mId_gb?.clear()
            } else {
                mId_gb.startAnimation()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_activity_menu_popup)
        initialState = ConstraintSet().apply {
            clone(mRootLayout)
        }
        showState = ConstraintSet().apply {
            clone(mRootLayout)
            constrainCircle(R.id.pot0, R.id.centerView, 197 * 3, 310f)
            constrainCircle(R.id.pot1, R.id.centerView, 197 * 3, 333f)
            constrainCircle(R.id.pot2, R.id.centerView, 197 * 3, 0f)
            constrainCircle(R.id.pot3, R.id.centerView, 197 * 3, 27f)
            constrainCircle(R.id.pot4, R.id.centerView, 197 * 3, 50f)
        }

        mShowButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition(mRootLayout, mTransition)
            }
            showState.applyTo(mRootLayout)
        }
        mCloseButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition(mRootLayout)
            }
            initialState.applyTo(mRootLayout)
        }

    }
}
