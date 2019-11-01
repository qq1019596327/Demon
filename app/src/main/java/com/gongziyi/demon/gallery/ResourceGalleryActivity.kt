package com.gongziyi.demon.gallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gongziyi.demon.R
import com.gongziyi.demon.gallery.adpater.GallerySnapHelper
import com.gongziyi.demon.gallery.adpater.GalleryAdapter
import com.gongziyi.demon.gallery.adpater.GalleryLayoutManager
import kotlinx.android.synthetic.main.activity_resource_gallery.*


/**
 * Created on 2019/10/31
 * @author: gongziyi
 * Description:资源画廊Activity
 */
class ResourceGalleryActivity : AppCompatActivity() {

    var startPadding = 100
    var progressRegion = 300

    private val mAdapter: GalleryAdapter by lazy {
        GalleryAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resource_gallery)
        mRecyclerView.let {
            it.layoutManager = GalleryLayoutManager().apply {
                startPadding = this@ResourceGalleryActivity.startPadding
                progressRegion = this@ResourceGalleryActivity.progressRegion
            }
            it.adapter = mAdapter
            GallerySnapHelper().apply {
                startPadding = this@ResourceGalleryActivity.startPadding
            }.attachToRecyclerView(it)
        }
        mStartPadding.layoutParams.width = startPadding
        mProgressRegion.layoutParams.width = progressRegion
    }

}
