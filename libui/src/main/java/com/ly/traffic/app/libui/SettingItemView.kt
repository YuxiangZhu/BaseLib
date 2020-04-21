package com.ly.traffic.app.libui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.ly.traffic.app.libui.databinding.VSettingItemViewBinding
import kotlinx.android.synthetic.main.v_setting_item_view.view.*

/**
 * 自定义的设置页面的item的条目View,暂时只是简单的dataBinding的使用，后期需要高级的双向绑定支持
 * Author: gzw48760.
 * Date: 2018/11/28 0028,9:39.
 * You never know what you can do until you try !
 */
class SettingItemView : CardView {

    //by lazy 必须是val类型
    private val tvTitle: AppCompatTextView by lazy { tv_title_setting_item_widget }//标题title
    private val tvContent by lazy { tv_content_setting_item_widget }//item的中间灰色文案
    private val ivIcon by lazy { iv_icon_setting_item_widget }//item 的图标icon
    private val settingView: View by lazy { cl_root_setting_item_view }//根布局的引用，用于设置onClickListener
    private val tvTipsNum by lazy { tv_tips_setting_item_widget }


    /**
     * View的构造函数
     * @param context context上下文
     */
    constructor(context: Context) : this(context, null)

    /**
     * 双参数构造函数
     * @param context
     * @param attrs view的定义属性配置
     */
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    /**
     * 三参数构造函数
     * @param context
     * @param attrs view的定义属性配置
     * @param defStyleAttr 默认的样式配置
     */
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        //1、关联布局
        VSettingItemViewBinding.inflate(LayoutInflater.from(context), this, true)
        //3、读取属性定义,并根据属性值，配置内部控件
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView)
        //title
        val title = attributes.getString(R.styleable.SettingItemView_item_title)
        if (!title.isNullOrEmpty()) {
            tvTitle.text = title
        }
        //提示数字
        val tipsNum = attributes.getInt(R.styleable.SettingItemView_item_tips_num, 0)
        if (tipsNum > 0) {
            tvTipsNum.visibility = View.VISIBLE
            tvTipsNum.text = "$tipsNum"
        } else {
            tvTipsNum.visibility = View.GONE
        }

        //内容文案,颜色
        val content = attributes.getString(R.styleable.SettingItemView_item_content)
        if (!content.isNullOrEmpty()) {
            tvContent.text = content
        }
        val color =
            attributes.getColor(R.styleable.SettingItemView_item_content_color, Color.LTGRAY)
        tvContent.setTextColor(color)
        //icon,if null set view gone
        val iconResId = attributes.getResourceId(R.styleable.SettingItemView_item_icon, -1)
        if (iconResId != -1) {
            ivIcon.setImageResource(iconResId)
            ivIcon.visibility = View.VISIBLE
        } else {
            ivIcon.visibility = View.GONE
        }

        //箭头展示,默认展示
        val showArrow = attributes.getBoolean(R.styleable.SettingItemView_item_show_arrow, true)
        if (showArrow) {
            val drawable = context.getDrawable(R.drawable.ic_enter_arrow_grey_14dp)
            tvContent.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        } else {
            tvContent.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
        //recycle
        attributes.recycle()
    }

    /**
     * 设置item的title
     * @param title
     */
    fun setTitle(@NonNull title: String) {
        tvTitle.text = title
    }

    /**
     * 设置提醒数目[num]是Int类型的数值
     */
    fun setTipsNum(@NonNull @IntRange(from = 1L, to = 100L) num: Int) {
        if (num > 0) {
            tvTipsNum.text = "$num"
            tvTipsNum.visibility = View.VISIBLE
        } else {
            tvTipsNum.visibility = View.GONE
        }
    }

    /**
     * 设置item的中间desc的content
     */
    fun setContent(@NonNull content: String) {
        tvContent.text = content
    }

    /**
     * 设置item的Desc的字体颜色
     */
    fun setContentColor(@ColorInt color: Int) {
        tvContent.setTextColor(color)
    }

    /**
     * 设置图标icon
     * @param id 图标icon的resId
     */
    fun setIcon(id: Int) {
        ivIcon.setImageResource(id)
    }

    /**
     * 根布局，设置点击事件,这里重写函数，可以使得DataBinding中能够关联到，
     * 而且这里的settingView需要时具体的layout中的布局，而不是settingItemView本身，
     * 或者rootView，否则会循环set，估计是需要处理事件分发
     */
    override fun setOnClickListener(listener: OnClickListener?) {
        settingView.setOnClickListener(listener)
    }

    companion object {
        /**
         * 设置提醒数目,[numStr]是String格式的数值
         */
        @JvmStatic
        @BindingAdapter("item_tips_num")
        fun setTipsNumStr(siv: SettingItemView, @NonNull numStr: String?) {
            val num = numStr?.toIntOrNull() ?: 0
            siv.setTipsNum(num)
        }


        /**
         * 设置item的中间desc的content,支援DataBinding的形式
         */
        @JvmStatic
        @BindingAdapter("item_content")
        fun setContentStr(siv: SettingItemView, @NonNull content: String?) {
            siv.setContent(content ?: "")
        }

    }
}