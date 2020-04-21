package com.ly.traffic.app.libui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ly.traffic.app.libui.databinding.ItemDayCalendarBinding

/**
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2020年03月31日 14:03
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
class DayAdapter(
    private val days: List<MonthEntity.DayEntity>,
    private val listener: MonthAdapter.OnDayClickListener?
) :
    RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    private var selPos = -1//默认选择的item 的position


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DayViewHolder(
        ItemDayCalendarBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount() = days.size

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val entity = days[position]

        holder.bind(entity, position) {
            entity.solar ?: return@bind
            //position
            selPos = position
            //点击事件
            listener?.onDayClick(
                entity.solar.solarYear,
                entity.solar.solarMonth,
                entity.solar.solarDay
            )
            notifyDataSetChanged()
        }
    }


    /**
     * 日期day的viewHolder
     */
    class DayViewHolder(private val binding: ItemDayCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * 绑定数据到view
         */
        fun bind(
            entity: MonthEntity.DayEntity,
            position: Int,
            block: () -> Unit
        ) {
            if (entity.day in 1..31 && entity.type != MonthEntity.DayType.UNAVAILABLE) {
                itemView.setOnClickListener {
                    entity.solar ?: return@setOnClickListener
                    //
                    block()
                }
            }
            binding.info = entity
            binding.position = position
            binding.executePendingBindings()
        }
    }
}
