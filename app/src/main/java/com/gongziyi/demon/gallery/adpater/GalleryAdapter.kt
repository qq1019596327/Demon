package com.gongziyi.demon.gallery.adpater

import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gongziyi.demon.R


/**
 * Created on 2019/10/31
 * @author: gongziyi
 * Description:画廊适配器
 */
class GalleryAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_gallery_layout) {

    init {
        setNewData(arrayListOf<String>().apply {
            add("https://i1.zhiaigou.com/uploads/tu/201910/10372/7f29cd1f31_33.jpg")
            add("https://i1.zhiaigou.com/uploads/tu/201910/10273/440a1897c3_33.jpg")
            add("https://i1.zhiaigou.com/uploads/tu/201910/10078/8a2e508c69_22.jpg")
            add("https://i1.zhiaigou.com/uploads/tu/201909/10295/9fc5ff6217_33.jpg")
            add("https://i1.zhiaigou.com/uploads/tu/201903/10105/z6c9a9x.jpg")
            add("https://i1.zhiaigou.com/uploads/tu/201808/100/888.jpg")
            add("https://i1.zhiaigou.com/uploads/tu/201903/10194/v8a98z89a.jpg")
        })
    }

    private val colors by lazy {
        intArrayOf(
                ContextCompat.getColor(mContext, R.color.color_08f),
                ContextCompat.getColor(mContext, R.color.color_3c8),
                ContextCompat.getColor(mContext, R.color.color_fa6),
                ContextCompat.getColor(mContext, R.color.color_caf),
                ContextCompat.getColor(mContext, R.color.color_f78),
                ContextCompat.getColor(mContext, R.color.color_fe2),
                ContextCompat.getColor(mContext, R.color.color_5ec),
                ContextCompat.getColor(mContext, R.color.color_88f)
        )
    }

    private val bgColors by lazy {
        intArrayOf(
                ContextCompat.getColor(mContext, R.color.color_a08f),
                ContextCompat.getColor(mContext, R.color.color_a3c8),
                ContextCompat.getColor(mContext, R.color.color_afa6),
                ContextCompat.getColor(mContext, R.color.color_acaf),
                ContextCompat.getColor(mContext, R.color.color_af78),
                ContextCompat.getColor(mContext, R.color.color_afe2),
                ContextCompat.getColor(mContext, R.color.color_a5ec),
                ContextCompat.getColor(mContext, R.color.color_a88f)
        )
    }


    override fun convert(helper: BaseViewHolder, item: String?) {
        helper.setBackgroundColor(R.id.mBoundary, colors[helper.adapterPosition % colors.size])
        helper.itemView.setBackgroundColor(bgColors[helper.adapterPosition % bgColors.size])
        Glide.with(mContext)
                .load(item)
                .into(helper.getView(R.id.mImageView))
    }

}