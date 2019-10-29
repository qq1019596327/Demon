package com.gongziyi.demon.shadowTest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.MultipleItemRvAdapter
import com.chad.library.adapter.base.provider.BaseItemProvider
import com.gongziyi.demon.R
import kotlinx.android.synthetic.main.res_activity_shadow_test.*

/**
 * Created on 2019/9/16
 * @author: gongziyi
 * Description:
 */
class ShadowTestActivity : AppCompatActivity() {

    private val TestList by lazy {
        arrayListOf(
            "Lemon (柠檬) - 春茶 (はるちゃ)/Kobasolo (コバソロ)"
            , 3456789
            , "夢ならば"
            , 3456789
            , "どれほどよかったでしょう"
            , 3456789
            , "未だにあなたのことを夢にみる"
            , 3456789
            , "忘れた物を取りに帰るように"
            , 3456789
            , "古びた思い出の埃を払う"
            , 3456789
            , "戻らない幸せがあることを"
            , 3456789
            , "最後にあなたが教えてくれた"
            , 3456789
            , "言えずに隠してた昏い過去も"
            , 3456789
            , "あなたがいなきゃ"
            , 3456789
            , "永遠に昏いまま"
            , 3456789
            , "きっともうこれ以上"
            , 3456789
            , "傷つくことなど"
            , 3456789
            , "ありはしないとわかっている"
            , 3456789
            , "あの日の悲しみさえ"
            , 3456789
            , "あの日の苦しみさえ"
            , 3456789
            , "そのすべてを愛してた"
            , 3456789
            , "あなたとともに"
            , 3456789
            , "胸に残り離れない"
            , 3456789
            , "苦いレモンの匂い"
            , 3456789
            , "雨が降り止むまでは帰れない"
            , 3456789
            , "今でもあなたはわたしの光"
            , 3456789
            , "暗闇であなたの背をなぞった"
            , 3456789
            , "その輪郭を鮮明に覚えている"
            , 3456789
            , "受け止めきれないものと"
            , 3456789
            , "出会うたび"
            , 3456789
            , "溢れてやまないのは涙だけ"
            , 3456789
            , "何をしていたの"
            , 3456789
            , "何を見ていたの"
            , 3456789
            , "わたしの知らない横顔で"
            , 3456789
            , "どこかであなたが今"
            , 3456789
            , "わたしと同じ様な"
            , 3456789
            , "涙にくれ"
            , 3456789
            , "淋しさの中にいるなら"
            , 3456789
            , "わたしのことなどどうか"
            , 3456789
            , "忘れてください"
            , 3456789
            , "そんなことを心から願うほどに"
            , 3456789
            , "今でもあなたはわたしの光"
            , 3456789
            , "自分が思うより"
            , 3456789
            , "恋をしていたあなたに"
            , 3456789
            , "あれから思うように"
            , 3456789
            , "息ができない"
            , 3456789
            , "あんなに側にいたのに"
            , 3456789
            , "まるで嘘みたい"
            , 3456789
            , "とても忘れられない"
            , 3456789
            , "それだけが確か"
            , 3456789
            , "あの日の悲しみさえ"
            , 3456789
            , "あの日の苦しみさえ"
            , 3456789
            , "そのすべてを愛してた"
            , 3456789
            , "あなたとともに"
            , 3456789
            , "胸に残り離れない"
            , 3456789
            , "苦いレモンの匂い"
            , 3456789
            , "雨が降り止むまでは帰れない"
            , 3456789
            , "切り分けた果実の片方の様に"
            , 3456789
            , "今でもあなたはわたしの光"
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.res_activity_shadow_test)
        mRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = TestAdapter(TestList)
        }
    }
}

private class TestAdapter(list: List<Any>? = null) :
    MultipleItemRvAdapter<Any, BaseViewHolder>(list) {

    init {
        finishInitialize()
    }

    override fun registerItemProvider() {
        mProviderDelegate.registerProvider(StringItemProvider())
        mProviderDelegate.registerProvider(IntItemProvider())
    }

    override fun getViewType(t: Any?): Int = when (t) {
        is String -> 200
        is Int -> 300
        else -> 100
    }


}

private class StringItemProvider : BaseItemProvider<Any, BaseViewHolder>() {
    override fun layout(): Int = R.layout.res_item_shadow_test

    override fun viewType(): Int = 200

    override fun convert(helper: BaseViewHolder, data: Any?, position: Int) {
        if (data is String) {
            helper.setText(R.id.mItemText, data)
        }
    }

}

private class IntItemProvider : BaseItemProvider<Any, BaseViewHolder>() {
    override fun layout(): Int = R.layout.res_item_shadow_test_int
    override fun viewType(): Int = 300

    override fun convert(helper: BaseViewHolder, data: Any?, position: Int) {
        if (data is Int) {
            helper.setText(R.id.mItemText, data?.toString())
        }
    }

}


