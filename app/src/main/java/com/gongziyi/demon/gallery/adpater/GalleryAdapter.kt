package com.gongziyi.demon.gallery.adpater

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gongziyi.demon.R


/**
 * Created on 2019/10/31
 * @author: gongziyi
 * Description:画廊适配器
 */
class GalleryAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_gallery_layout) {
    private val options by lazy {
        RequestOptions.bitmapTransform(GlideRoundTransform(mContext))
    }

    override fun convert(helper: BaseViewHolder, item: String?) {
        helper.setText(R.id.mIndexText, "${helper.adapterPosition + 1}/$itemCount")
        helper.addOnClickListener(R.id.mCardLayout)


        Glide.with(mContext)
            .load(item)
            .apply(options)
            .into(helper.getView(R.id.mImageView))
    }
}