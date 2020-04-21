package com.ly.traffic.app.libui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ly.traffic.app.libui.databinding.ItemMonthCalendarBinding

/**
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2020年03月31日 14:13
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
class MonthAdapter : RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {

    private var months: List<MonthEntity>? = null//月份数据，内含当月的day的list数据
    private var mListener: OnDayClickListener? = null//点击接口

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MonthViewHolder(
        ItemMonthCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = months?.size ?: 0

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val entity = months?.get(position) ?: return
        holder.bind(entity)
        val dayAdapter = DayAdapter(entity.days, mListener)
        holder.adapter(dayAdapter)

    }

    /**
     * 设置日历控件的数据，月份为单位
     */
    fun setMonths(months: List<MonthEntity>) {
        this.months = months
        notifyDataSetChanged()
    }


    /**
     * 注册点击事件回调
     */
    fun setListener(listener: OnDayClickListener) {
        this.mListener = listener
    }


    /**
     * 日历控件的月份数据viewHolder
     */
    class MonthViewHolder(private val binding: ItemMonthCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(entity: MonthEntity) {
            binding.info = entity
            binding.executePendingBindings()
        }

        fun adapter(adapter: DayAdapter) {
            binding.adapter = adapter
        }
    }

    /**
     * 点击日期控件的回调接口
     */
    interface OnDayClickListener {
        fun onDayClick(year: Int, month: Int, day: Int)
    }


}