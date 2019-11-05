package com.gongziyi.demon.gallery

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.gongziyi.demon.R
import com.gongziyi.demon.gallery.adpater.GallerySnapHelper
import com.gongziyi.demon.gallery.adpater.GalleryAdapter
import com.gongziyi.demon.gallery.adpater.GalleryLayoutManager
import com.gongziyi.demon.gallery.view.IGalleryScrollChangeListener
import kotlinx.android.synthetic.main.activity_resource_gallery.*


/**
 * Created on 2019/10/31
 * @author: gongziyi
 * Description:资源画廊Activity
 */
class ResourceGalleryActivity : AppCompatActivity(), IGalleryScrollChangeListener {

    var startPadding = 100
    var progressRegion = 200
    var smoothRegion = 50
    var paddingOffset = 200
    var magnification = 0.8f
    private val mAdapter: GalleryAdapter by lazy {
        GalleryAdapter()
    }
    private val photos by lazy {
        arrayListOf<String>().apply {
            add("https://i1.zhiaigou.com/uploads/tu/201910/10372/7f29cd1f31_33.jpg")
            add("https://i1.zhiaigou.com/uploads/tu/201910/10273/440a1897c3_33.jpg")
            add("https://i1.zhiaigou.com/uploads/tu/201910/10078/8a2e508c69_22.jpg")
            add("https://i1.zhiaigou.com/uploads/tu/201909/10295/9fc5ff6217_33.jpg")
            add("https://i1.zhiaigou.com/uploads/tu/201903/10105/z6c9a9x.jpg")
            add("https://i1.zhiaigou.com/uploads/tu/201808/100/888.jpg")
            add("https://i1.zhiaigou.com/uploads/tu/201903/10194/v8a98z89a.jpg")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resource_gallery)
        mStartPadding.layoutParams.width = startPadding
        mProgressRegion.layoutParams.width = progressRegion * 2
        mSmoothRegion.layoutParams.width = smoothRegion * 2
        mPaddingOffset.layoutParams.width = paddingOffset

        mRecyclerView.let {
            mAdapter.setOnItemChildClickListener { _, _, position -> it.onActiveCardChange(position) }
            it.setStartPadding(startPadding)
            it.setProgressRegion(progressRegion)
            it.setSmoothRegion(smoothRegion)
            it.setPaddingOffset(paddingOffset
            )
            it.setMagnification(magnification)
            it.adapter = mAdapter
            it.addOnScrollChangeListener(this)
        }
    }

    fun setNewDataArray(view: View) {
        val count = (Math.random() * 20).toInt()
        mAdapter.setNewData(arrayListOf<String>().apply {
            for (i in 0..count) {
                add(photos[i % photos.size])
            }
        })
    }

    override fun onViewChangeCall(view: View, progress: Float) {
        if (view is MotionLayout) {
            view.progress = progress
        }
    }

    override fun onScrollIdleListener(activePosition: Int) {
        Log.i("==============", "activePosition=$activePosition")
    }

}
