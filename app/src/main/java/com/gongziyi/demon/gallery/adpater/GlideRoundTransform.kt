package com.gongziyi.demon.gallery.adpater

import android.content.Context
import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.security.MessageDigest
import kotlin.math.roundToInt


/**
 * Created on 2019/11/8
 * @author: gongziyi
 * Description:
 */
class GlideRoundTransform @JvmOverloads constructor(context: Context, dp: Int = 4) :
    BitmapTransformation() {

    private val radius by lazy {
        context.resources.displayMetrics.density * dp
    }

    val id: String get() = javaClass.name + radius.roundToInt()

    private fun roundCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
        if (source == null) return null

        val width = source.width
        val height = source.height

        var result: Bitmap? = pool.get(width, height, Bitmap.Config.ARGB_8888)
        if (result == null) {
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(result!!)
        val paint = Paint()
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rectF, radius, radius, paint)
        return result
    }


    override fun transform(pool: BitmapPool, transform: Bitmap, outW: Int, outH: Int): Bitmap? {
        val bitmap = TransformationUtils.centerCrop(pool, transform, outW, outH)
        return roundCrop(pool, bitmap)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) = Unit

}