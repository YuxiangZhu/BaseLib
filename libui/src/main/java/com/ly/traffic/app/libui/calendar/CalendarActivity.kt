package com.ly.traffic.app.libui.calendar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ly.traffic.app.libui.R
import com.ly.traffic.app.libui.databinding.ActivityCalendarBinding
import kotlinx.android.synthetic.main.activity_calendar.*
import java.util.*

/**
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2020年03月31日 12:13
 * 签名： 天行健，君子以自强不息；地势坤，君子以厚德载物。
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/  -- 志威 zhiwei.org
 *
 * You never know what you can do until you try !
 * ----------------------------------------------------------------
 * 简单的自定义日历选择界面,todo 目前仅支持单选 2020年4月1日
 */
class CalendarActivity : AppCompatActivity() {

    private val monthsList = mutableListOf<MonthEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityCalendarBinding>(this, R.layout.activity_calendar)
            .apply {
                adapter = MonthAdapter().also {
                    it.setMonths(monthsList)
                    it.setListener(object : MonthAdapter.OnDayClickListener {
                        override fun onDayClick(year: Int, month: Int, day: Int) {
                            val data = Intent()
                            data.putExtra(INTENT_KEY_YEAR, year)
                            data.putExtra(INTENT_KEY_MONTH, month)
                            data.putExtra(INTENT_KEY_DAY, day)
                            setResult(Activity.RESULT_OK, data)
                            finish()
                        }
                    })
                }
            }
        toolbar_cal.setNavigationOnClickListener { finish() }
        val maxDay = intent.getIntExtra(INTENT_KEY_MAX_DAY, 60)
        //初始化数据
        initData(maxDay)
    }

    /**
     * 初始化月份list的数据
     */
    private fun initData(maxDay: Int) {
        val currentCal = Calendar.getInstance()
        val nowYear = currentCal[Calendar.YEAR]
        val nowMonth = currentCal[Calendar.MONTH]
        val nowDay = currentCal[Calendar.DAY_OF_MONTH]//当前日
        //归置 显示日历，要显示当月的1号开始
        val currentDayOfYear = currentCal.get(Calendar.DAY_OF_YEAR)
        currentCal.set(nowYear, nowMonth, 1)

        val endCal = Calendar.getInstance()
        endCal.add(Calendar.DAY_OF_YEAR, maxDay)

        while (currentCal.time.before(endCal.time)) {
            val tmpCal = currentCal.clone() as Calendar

            //以周日为第一天的标准形式，若周一为第一天，则第一周的空格数 为（dayOfWeek-1)-1
            val firstWeekEmptyDays = tmpCal.get(Calendar.DAY_OF_WEEK) - 1
            //生成第一周的空格数据
            val daysList = mutableListOf<MonthEntity.DayEntity>()
            for (j in 0 until firstWeekEmptyDays) {
                //添加空日期
                daysList.add(MonthEntity.DayEntity())
            }

            //添加月份的真实日期数据
            val tmpMonth = tmpCal.get(Calendar.MONTH)
            val tmpYear = tmpCal.get(Calendar.YEAR)
            //获取当月的总天数
            val maxDayOfMonth = tmpCal.getActualMaximum(Calendar.DAY_OF_MONTH)
            for (dayIndex in 1..maxDayOfMonth) {
                val dayType =
                    if (tmpMonth == nowMonth && dayIndex < nowDay || currentDayOfYear + maxDay <= tmpCal.get(
                            Calendar.DAY_OF_YEAR
                        )
                    ) MonthEntity.DayType.UNAVAILABLE
                    else if (tmpMonth == nowMonth && dayIndex == nowDay) MonthEntity.DayType.TODAY
                    else MonthEntity.DayType.COMMON
                //当前月
                daysList.add(
                    MonthEntity.DayEntity(
                        dayType,
                        dayIndex,
                        LunarTools.Solar(nowYear, tmpMonth + 1, dayIndex),
                        LunarTools.getLunarDay(
                            LunarTools.Solar(
                                nowYear, tmpMonth + 1,//月份计数是0开始的
                                dayIndex
                            )
                        )
                    )
                )
                //单月内的日期循环 i++
                tmpCal.add(Calendar.DAY_OF_MONTH, 1)
            }
            //将月份数据加入monthsList
            monthsList.add(MonthEntity("${tmpYear}年${tmpMonth + 1}月", tmpMonth + 1, daysList))
            //类似于i++，将calendar后移一个月
            currentCal.add(Calendar.MONTH, 1)
        }

    }

    companion object {
        const val INTENT_KEY_YEAR = "year"
        const val INTENT_KEY_MONTH = "month"
        const val INTENT_KEY_DAY = "day"
        const val INTENT_KEY_MAX_DAY = "max_day"
    }
}