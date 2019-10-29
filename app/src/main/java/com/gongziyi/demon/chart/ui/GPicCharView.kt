package com.gongziyi.demon.chart.ui

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.gongziyi.demon.R
import kotlin.math.max
import kotlin.math.min


/**
 * Created on 2019/10/22
 * @author: gongziyi
 * Description:饼图
 */
open class GPicCharView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    //默认笔
    protected val mCuttingPen: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
    }
    //阴影笔
    protected val mShadowPen: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
    }


    //保存的线段集
    private val pathSet = arrayListOf<Path>()
    //高亮线段
    private val indexPath = Path()

    /**高亮指针*/
    protected var mHighlightIndex: Int = -1
    /**数据对象*/
    protected var mDataHelper: IPicCharDataHelper? = null
    /**颜料集*/
    protected var mColorList: IntArray
    /**高亮颜色*/
    protected var mHighlightColor: Int = Color.WHITE

    /**环图间隔*/
    protected var mInterval: Float = 0f
    /**镂空半径ro线段宽度*/
    protected var mHollowingRadius: Float = 50f
    /**阴影半径*/
    protected var mShadowRadius: Float = 50f
    /**高亮框宽度*/
    protected var mHighlightWidth: Float = 20f
    /**线段模式 影响计算方式 0=实心模式, 1=线段模式*/
    protected var mPathMode: Int = MODE_FILL

    companion object {
        /**
         * 实心模式
         * 该模式下mHollowingRadius视为镂空半径
         */
        const val MODE_FILL = 0
        /**
         * 线段模式
         * 该模式下mHollowingRadius视为线段宽度
         */
        const val MODE_STROKE = 1
    }

    init {
        //关闭物理加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        context.obtainStyledAttributes(attrs, R.styleable.GPicCharView).apply {
            mInterval = getDimension(R.styleable.GPicCharView_mInterval, mInterval)
            mHollowingRadius =
                getDimension(R.styleable.GPicCharView_mHollowingRadius, mHollowingRadius)
            mShadowRadius = getDimension(R.styleable.GPicCharView_mShadowRadius, mShadowRadius)
            mHighlightWidth =
                getDimension(R.styleable.GPicCharView_mHighlightWidth, mHighlightWidth)
            mHighlightColor = getColor(R.styleable.GPicCharView_mHighlightColor, mHighlightColor)
            mPathMode = getInt(R.styleable.GPicCharView_mPathMode, mPathMode)

            recycle()
        }
        //示例数据
        mDataHelper = PicCharDataExample()
        //默认颜色
        mColorList = intArrayOf(
            Color.parseColor("#3AA0FF"),
            Color.parseColor("#FACC13"),
            Color.parseColor("#28E0C8"),
            Color.parseColor("#FB7293")
        )
        mShadowPen.maskFilter = BlurMaskFilter(mShadowRadius, BlurMaskFilter.Blur.OUTER)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        refreshWhole()
    }


    /**全部刷新*/
    private fun refreshWhole() {
        mDataHelper?.let {
            refreshBasePath(it)
            refreshHighlight(it)
        }
    }

    /**刷新基础饼*/
    protected open fun refreshBasePath(it: IPicCharDataHelper) {
        val outerRadius = (min(mWidth, mHeight) shr 1) - mShadowRadius - (mHighlightWidth / 2)
        val innerRadius =
            if (mPathMode == MODE_FILL) mHollowingRadius else outerRadius - mHollowingRadius

        val outerPath = getCirclePath(outerRadius)
        val innerPath = getCirclePath(innerRadius)
        initPaths(outerPath, innerPath, it)
        invalidate()
    }


    /**刷新高亮*/
    protected open fun refreshHighlight(it: IPicCharDataHelper) {
        val outerRadius = (min(mWidth, mHeight) shr 1) - mShadowRadius
        val innerRadius =
            if (mPathMode == MODE_FILL) {
                mHollowingRadius - (mHighlightWidth / 2)
            } else {
                outerRadius - mHollowingRadius - mHighlightWidth

            }

        val outerPath = getCirclePath(outerRadius)
        val innerPath = getCirclePath(innerRadius)
        initHighlightPath(outerPath, innerPath, it)
        invalidate()
    }


    /**
     * 获取轮廓
     * @param measure 原型线测量类
     * @param pos 第一点位置
     * @param length 第二点位置 因为是圆形 所以要用长度去确定圆弧位置
     *
     */
    private fun Path.getContour(measure: PathMeasure, pos: FloatArray, length: Float) {
        moveTo(0f, 0f)
        lineTo(pos[0], pos[1])
        measure.getSegment(0f, length, this, false)
        close()
    }

    /**旋转线段*/
    private fun Path.setRotate(degrees: Float) {
        transform(Matrix().apply {
            setRotate(degrees * 360, 0f, 0f)
        })
    }

    /**获取相对圆的起点位置*/
    private fun PathMeasure.getZeroPos(): FloatArray {
        val zeroPos = FloatArray(2)
        getPosTan(0f, zeroPos, null)
        return zeroPos
    }

    /**获取基础圆形*/
    protected open fun getBaseCircleMeasure(): PathMeasure = Path().let {
        it.addCircle(0f, 0f, max(mWidth shr 1, mHeight shr 1).toFloat(), Path.Direction.CW)
        PathMeasure(it, true)
    }

    /**获取基础圆形*/
    protected open fun getCirclePath(radius: Float) = Path().apply {
        var mRadius = radius
        if (radius < 0) {
            mRadius = 0f
        }
        addCircle(0f, 0f, mRadius, Path.Direction.CW)
    }

    /**
     * 获取切变线矩阵
     * lp/rp 可以用于拓宽或裁切矩形
     * @param progress 进度 也就是相对与0,0点的夹角
     */
    protected open fun getEdgePathPair(right: Float, radius: Float, progress: Float): Pair<Path, Path> {
        val lP = Path()
        lP.addRect(0f, -radius, right, radius, Path.Direction.CW)
        val rP = Path()
        rP.addRect(0f, -radius, right, radius, Path.Direction.CW)
        rP.setRotate(progress)
        return lP to rP
    }

    /**版本判断*/
    private fun isKitkat() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    /**初始化基础饼*/
    protected open fun initPaths(outerPath: Path, innerPath: Path, data: IPicCharDataHelper) {
        if (!isKitkat()) return

        val totalValue = data.getTotalValue()
        val pathMeasure = getBaseCircleMeasure()
        val zeroPos = pathMeasure.getZeroPos()

        //清空集合
        pathSet.clear()
        var progress = 0f
        for (i in 0 until data.getItemCount()) {
            pathSet.add(Path().apply {
                val value = data.getValueByIndex(i)
                val length = value / totalValue * pathMeasure.length
                //绘制三角形
                getContour(pathMeasure, zeroPos, length)

                //裁剪
                if (mInterval > 0) {
                    val edgeMatrix =
                        getEdgePathPair(zeroPos[0], mInterval / 2, value / totalValue)

                    op(edgeMatrix.first, Path.Op.DIFFERENCE)
                    op(edgeMatrix.second, Path.Op.DIFFERENCE)
                }
                //内外边框裁剪
                op(innerPath, Path.Op.DIFFERENCE)
                op(outerPath, Path.Op.INTERSECT)

                //旋转
                setRotate(progress / totalValue)
                progress += value

            })
        }
    }

    /**初始化高亮饼*/
    protected open fun initHighlightPath(outerPath: Path, innerPath: Path, data: IPicCharDataHelper) {
        if (!isKitkat()) return
        if (mHighlightIndex !in 0 until data.getItemCount()) return

        val totalValue = data.getTotalValue()
        val pathMeasure = getBaseCircleMeasure()
        val zeroPos = pathMeasure.getZeroPos()
        var progress = 0f
        for (i in 0 until mHighlightIndex) {
            progress += data.getValueByIndex(i)
        }
        indexPath.apply {
            reset()
            val value = data.getValueByIndex(mHighlightIndex)
            val length = value / totalValue * pathMeasure.length
            //绘制三角形
            getContour(pathMeasure, zeroPos, length)

            //裁切拼接
            if (mHighlightWidth > 0) {
                val pp =
                    getEdgePathPair(zeroPos[0], mHighlightWidth / 2, value / totalValue)

                op(pp.first, Path.Op.UNION)
                op(pp.second, Path.Op.UNION)

                if (mPathMode == MODE_FILL && mHollowingRadius <= 0) {
                    val iP = Path()
                    iP.op(pp.first, pp.second, Path.Op.INTERSECT)
                    iP.setRotate(0.5f)
                    op(iP, Path.Op.UNION)
                }
            }

            //内外边框裁剪
            op(innerPath, Path.Op.DIFFERENCE)
            op(outerPath, Path.Op.INTERSECT)

            //旋转
            setRotate(progress / totalValue)
        }
    }

    //设置数据
    open fun setData(data: IPicCharDataHelper?) {
        if (data == null) {
            pathSet.clear()
        } else {
            mDataHelper = data
            post {
                refreshWhole()
            }
        }
        invalidate()
    }

    //设置数据
    open fun setData(data: List<IPicCharData>) {
        setData(object : IPicCharDataHelper {
            override fun getItemCount() = data.size
            override fun getValueByIndex(index: Int) = data.getOrNull(index)?.getValue() ?: 0f
            override fun getTotalValue(): Float {
                var total = 0f
                data.forEach {
                    total += it.getValue()
                }
                return total
            }
        })
    }


    //设置间隔
    open fun setInterval(px: Float) {
        if (mInterval != px) {
            mInterval = px
            refreshWhole()
        }
    }

    //中心半径 或者 段长度
    open fun setHollowingOrRadius(px: Float) {
        if (mHollowingRadius != px) {
            mHollowingRadius = px
            refreshWhole()
        }
    }


    //线集模式
    open fun setPathMode(mode: Int) {
        if (mPathMode != mode) {
            mPathMode = mode
            mDataHelper?.apply {
                refreshWhole()
            }
        }
    }

    //设置颜色集
    open fun setColorList(colors: IntArray) {
        mColorList = colors
        invalidate()
    }


    //设置高亮
    open fun setHighlightIndex(index: Int) {
        if (mHighlightIndex != index) {
            mHighlightIndex = index
            mDataHelper?.apply {
                refreshHighlight(this)
            }
        }
    }


    //阴影半径
    open fun setShadowRadiusRadius(px: Float) {
        if (mShadowRadius != px) {
            mShadowRadius = px
            mShadowPen.maskFilter = BlurMaskFilter(mShadowRadius, BlurMaskFilter.Blur.OUTER)
            refreshWhole()
        }
    }

    //高亮宽度
    open fun setHighlightWidth(px: Float) {
        if (mHighlightWidth != px) {
            mHighlightWidth = px
            mDataHelper?.apply {
                refreshWhole()
            }
        }
    }

    //设置高亮颜色
    open fun setHighlightColor(color: Int) {
        mHighlightColor = color
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val rw = mWidth shr 1
        val rh = mHeight shr 1
        canvas.save()
        canvas.translate(rw.toFloat(), rh.toFloat())
        canvas.rotate(-90f)
        //先画默认集合 忽略高亮
        pathSet.forEachIndexed { index, it ->
            mCuttingPen.color = mColorList[index % mColorList.size]
            canvas.drawPath(it, mCuttingPen)
        }
        //在画高亮单位
        pathSet.getOrNull(mHighlightIndex)?.apply {
            val color = mColorList[mHighlightIndex % mColorList.size]
            //从阴影到 高亮线框 再到默认线框
            mShadowPen.color = color
            canvas.drawPath(indexPath, mShadowPen)

            mCuttingPen.color = mHighlightColor
            canvas.drawPath(indexPath, mCuttingPen)

            mCuttingPen.color = color
            canvas.drawPath(this, mCuttingPen)
        }
        canvas.restore()
    }


}

private class PicCharDataExample : IPicCharDataHelper {
    override fun getItemCount() = 4
    override fun getValueByIndex(index: Int) = 1F
    override fun getTotalValue(): Float = 4f
}

//数据提供者
interface IPicCharDataHelper {
    //数据总数
    fun getItemCount(): Int

    //数据的当前值
    fun getValueByIndex(index: Int): Float

    //数据的总值
    fun getTotalValue(): Float
}

interface IPicCharData {
    fun getValue(): Float
}
