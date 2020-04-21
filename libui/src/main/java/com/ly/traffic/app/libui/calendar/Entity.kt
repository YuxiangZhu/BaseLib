package com.ly.traffic.app.libui.calendar

import androidx.annotation.Keep
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.ly.traffic.app.libui.R

/**
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2020年03月31日 13:53
 * 签名： 天行健，君子以自强不息；地势坤，君子以厚德载物。
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/  -- 志威 zhiwei.org
 *
 * You never know what you can do until you try !
 * ----------------------------------------------------------------
 */
@Keep
data class MonthEntity(
    val title: String,//month item的数据title，也就是 2020年3月 这样的标题
    val month: Int,//当前月的月份数字
    val days: List<DayEntity>//当前月份的日期day
) {
    @Keep
    data class DayEntity(
        val type: DayType = DayType.EMPTY,//标记用的，区分当前day的特性，如空白、周末、选中等Type，参见
        val day: Int = -1,//当前日
        val solar: LunarTools.Solar? = null,//公历的日期对象
        val desc: String = ""//备注信息
    )

    @Keep
    enum class DayType {
        EMPTY,//每月1号之前，当周的空白
        COMMON,//普通的日期
        WEEKDAY,//周末
        SELECTED,//选中的
        UNAVAILABLE,//不可选的
        BETWEEN,//连选模式下的 中间选择
        BEGIN,//连选模式下的起点
        END,//连选模式下的终点
        SINGLE,//单选点
        TODAY,//今日
    }
}

/**
 * 将日期数据返回到ui上
 */
fun parseDay(day: MonthEntity.DayEntity): String {
    return when {
        day.type == MonthEntity.DayType.TODAY -> "今天"
        day.day in 1..31 -> "${day.day}"
        else -> ""
    }
}

@BindingAdapter("android:textColor", "position", requireAll = true)
fun textColor(tv: AppCompatTextView, entity: MonthEntity.DayEntity, position: Int) {
    val color = when {
        entity.type == MonthEntity.DayType.TODAY -> R.color.colorAccent
        (entity.type != MonthEntity.DayType.UNAVAILABLE) && (position % 7 == 0 || position % 7 == 6) -> R.color.colorAccent
        entity.type == MonthEntity.DayType.UNAVAILABLE -> R.color.colorSubTitleGrey
        entity.type == MonthEntity.DayType.SINGLE -> R.color.colorHighLight
        else -> R.color.colorSubTitleBlack
    }
    tv.setTextColor(ContextCompat.getColor(tv.context, color))
    if (tv.isSelected) tv.setTextColor(ContextCompat.getColor(tv.context, R.color.colorWhite))
}