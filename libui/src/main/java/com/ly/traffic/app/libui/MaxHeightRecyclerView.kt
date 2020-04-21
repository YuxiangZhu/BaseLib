package com.ly.traffic.app.libui

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2020年02月19日 10:07
 * 签名： 天行健，君子以自强不息；地势坤，君子以厚德载物。
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/  -- 志威 zhiwei.org
 *
 * You never know what you can do until you try !
 * ----------------------------------------------------------------
 * 有最大高度设置的RecyclerView
 */
class MaxHeightRecyclerView : RecyclerView {

    private var _maxHeight: Int = 0//最大高度

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initConfig(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initConfig(context, attrs)
    }

    /**
     * 初始化配置
     */
    private fun initConfig(context: Context, attrs: AttributeSet?) {
        val array =
            context.obtainStyledAttributes(attrs, R.styleable.MaxHeightRecyclerView)
        _maxHeight =
            array.getLayoutDimension(R.styleable.MaxHeightRecyclerView_rv_maxHeight, _maxHeight)

        array.recycle()
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val specHeight = if (_maxHeight > 0) {
            MeasureSpec.makeMeasureSpec(_maxHeight, MeasureSpec.AT_MOST)
        } else heightSpec

        super.onMeasure(widthSpec, specHeight)
    }
}