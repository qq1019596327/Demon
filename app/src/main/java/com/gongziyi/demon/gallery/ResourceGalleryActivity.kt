package com.gongziyi.demon.gallery

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.ViewCompat
import com.gongziyi.demon.R
import com.gongziyi.demon.gallery.adpater.GalleryAdapter
import com.gongziyi.demon.gallery.view.IGalleryScrollChangeListener
import com.gongziyi.demon.shadowTest.ShadowAngleConstraintLayout
import kotlinx.android.synthetic.main.activity_resource_gallery.*


/**
 * Created on 2019/10/31
 * @author: gongziyi
 * Description:资源画廊Activity
 */
class ResourceGalleryActivity : AppCompatActivity(), IGalleryScrollChangeListener {


    private val mAdapter: GalleryAdapter by lazy {
        GalleryAdapter()
    }
    private val photos by lazy {
        arrayListOf<String>().apply {
             add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573195554230&di=4b61b741d23c4739d5b1b53c0c1f59c5&imgtype=0&src=http%3A%2F%2Ffile5.youboy.com%2Fd%2F173%2F67%2F79%2F1%2F146621.jpg")
             add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=576397792,761328300&fm=26&gp=0.jpg")
             add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573195554230&di=a97cdf836a39443163a5b6b0a3a36c8e&imgtype=0&src=http%3A%2F%2Fwww.ph-fc.com%2Fd%2Ffile%2Fnewhouse%2F2015%2F07%2F55934ad910cff.jpg")
             add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573195554230&di=7e7184666349af6f24f2ddfb9efcc2c5&imgtype=0&src=http%3A%2F%2Fphoto.16pic.com%2F00%2F52%2F49%2F16pic_5249823_b.jpg")
             add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573195554229&di=b9f037d353be2e646f60f7b6619a7449&imgtype=0&src=http%3A%2F%2Fwww.ph-fc.com%2Fd%2Ffile%2Fnewhouse%2F2014%2F10%2F544090684136e.jpg")
             add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573195554229&di=7a7441227ef491a208120ff8c3eef172&imgtype=0&src=http%3A%2F%2Fimage4.cnpp.cn%2Fupload%2Fimages%2F20180118%2F16525869504_840x600.jpg")
             add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573195554229&di=12458d8f3f68571a01028b235cb26e66&imgtype=0&src=http%3A%2F%2Fpic5.nipic.com%2F20100123%2F2572038_214956013573_2.jpg")
             add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573195554229&di=9dc49ba92ca5928aa42b401704d74f47&imgtype=0&src=http%3A%2F%2Fimg3.guilinlife.com%2Fhouse%2FImages%2Fimage%2F201412%2F20141230104732_80884.jpg")
             add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573195554229&di=0575766c03da3569b0f8d3c338f370ea&imgtype=0&src=http%3A%2F%2Fwww.dllp.cn%2FFileUpload%2FPremises%2F20140811%2F8e4e71e2-3ab2-49e1-9e61-fa814b817cab.jpg")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resource_gallery)
        mRecyclerView.let {
            mAdapter.setOnItemChildClickListener { _, _, position -> it.onActiveCardChange(position) }
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
            val layout = view.findViewById<ShadowAngleConstraintLayout>(R.id.mCardLayout)
            layout.setShadow(15 * progress, 1f)
            view.setZ(R.id.mCardLayout, progress)
            view.setZ(R.id.mDownHintText, progress)
            view.setZ(R.id.mUpHintText, progress)
            view.setZ(R.id.mDownText, progress)
            view.setZ(R.id.mUpText, progress)
            view.setZ(R.id.mImageView, progress)
            view.setZ(R.id.mImageTitle, progress)
        }
    }

    override fun onScrollIdleListener(activePosition: Int) {

        Log.i("==============", "activePosition=$activePosition")
    }

    private fun View.setZ(idInt: Int, progress: Float) {
        ViewCompat.setZ(findViewById(idInt), 15 * progress + 1)
    }

}
