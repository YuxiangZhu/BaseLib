package com.ly.traffic.app.libui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.util.SizeUtils

/**
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2019年12月24日 15:50
 * 签名： 天行健，君子以自强不息；地势坤，君子以厚德载物。
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/  -- 志威 zhiwei.org
 *
 * You never know what you can do until you try !
 * ----------------------------------------------------------------
 * 字母索引侧边栏
 */
class SideBar : View {

    private lateinit var mLetterListener: OnLetterChangedListener//字母列表变化接口

    //可以动态变化的字母列表的list，因为可能实际用不到全部26个字母
    private var mLetters = emptyList<String>()
    private var mChose = -1//当前选择的字母

    private val mPaint = Paint()//绘制的画笔

    private var mTextDialog: AppCompatTextView? = null//用于显示选择的字母的悬浮text

    init {
        //透明背景
        setBackgroundColor(Color.TRANSPARENT)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val height = height // 获取对应高度
        val width = width // 获取对应宽度
        var singleHeight = 0
        if (mLetters.isNotEmpty()) {
            singleHeight = height / mLetters.size // 获取每一个字母的高度
        }

        for (i in mLetters.indices) {
            mPaint.color = Color.parseColor("#606060")
            mPaint.typeface = Typeface.DEFAULT_BOLD
            mPaint.isAntiAlias = true
            mPaint.textSize = SizeUtils.sp2px(12f).toFloat()
            // 选中的状态
            if (i == mChose) {
                mPaint.color = Color.parseColor("#179dff")
                mPaint.isFakeBoldText = true
            }
            // x坐标等于中间-字符串宽度的一半.
            val xPos = width / 2 - mPaint.measureText(mLetters[i]) / 2
            val yPos = singleHeight * i + singleHeight / 2.toFloat()
            canvas?.drawText(mLetters[i], xPos, yPos, mPaint)
            mPaint.reset() // 重置画笔
        }
    }


    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        event ?: return super.dispatchTouchEvent(event)
        val action = event.action
        val y = event.y // 点击y坐标
        val oldChoose = mChose
        val listener = mLetterListener
        val c = (y / height * mLetters.size).toInt() // 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
        when (action) {
            MotionEvent.ACTION_UP -> {
                setBackgroundColor(Color.TRANSPARENT)
                mChose = -1
                invalidate()
                mTextDialog?.visibility = GONE
            }
            else -> {
                setBackgroundResource(R.drawable.shape_bg_sidebar)
                if (oldChoose != c && c >= 0 && c < mLetters.size) {
                    listener.onLetterChanged(mLetters[c])
                    mTextDialog?.text = mLetters[c]
                    mTextDialog?.visibility = VISIBLE
                    mChose = c
                    invalidate()
                }
            }
        }
        return true
    }


    /**
     * 更新索引表的内容[indexList]索引数据
     */
    fun setIndexText(indexList: List<String>) {
        mLetters = indexList
        invalidate()
    }

    /**
     * 设置用于显示选择的索引的View
     */
    fun setTextView(tv: AppCompatTextView?) {
        tv ?: return
        this.mTextDialog = tv
    }

    /**
     * 公开接口设置方式[listener]
     */
    fun setLetterChangeListener(listener: OnLetterChangedListener) {
        this.mLetterListener = listener
    }


    /**
     * 字母索引选择变化的接口
     */
    interface OnLetterChangedListener {
        fun onLetterChanged(letter: String?)
    }

    companion object {
        //字母索引的列表字母
        val LETTER_LIST = arrayOf(
            "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"
        )
    }
}