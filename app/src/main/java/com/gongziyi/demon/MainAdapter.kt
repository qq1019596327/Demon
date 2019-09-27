package com.gongziyi.demon

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created on 2019/4/12
 * @author: gongziyi
 * Description:测试适配器
 */
class MainAdapter(data: List<Pair<String, Class<out AppCompatActivity>>>) :
    BaseQuickAdapter<Pair<String, Class<out AppCompatActivity>>, BaseViewHolder>(R.layout.res_item_main_button, data) {


    override fun onCreateDefViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val holder = super.onCreateDefViewHolder(parent, viewType)
        holder.addOnClickListener(R.id.mItemButton)
        return holder
    }

    override fun convert(helper: BaseViewHolder, item: Pair<String, Class<out AppCompatActivity>>) {
        helper.setText(R.id.mItemButton, item.first)
    }
}