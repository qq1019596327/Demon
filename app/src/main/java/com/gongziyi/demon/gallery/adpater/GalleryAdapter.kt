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
        helper.setBackgroundColor(R.id.mCardLayout, bgColors[helper.adapterPosition % colors.size])
        helper.addOnClickListener(R.id.mCardLayout)
        Glide.with(mContext)
            .load(item)
            .into(helper.getView(R.id.mImageView))
    }

}