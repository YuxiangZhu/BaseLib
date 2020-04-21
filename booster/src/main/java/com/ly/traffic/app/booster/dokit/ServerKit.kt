package com.ly.traffic.app.booster.dokit

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.InputCallback
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.SingleChoiceListener
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.CacheDiskStaticUtils
import com.didichuxing.doraemonkit.kit.AbstractKit
import com.didichuxing.doraemonkit.kit.Category
import com.ly.traffic.app.booster.R

/**
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2019年12月30日 17:50
 * 签名： 天行健，君子以自强不息；地势坤，君子以厚德载物。
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/  -- 志威 zhiwei.org
 *
 * You never know what you can do until you try !
 * ----------------------------------------------------------------
 * 配置请求接口的host，包含qa 线上 和自定义的local host
 */
internal class ServerKit(private val hosts: ArrayList<String>) : AbstractKit() {

    /**
     * icon点击事件，这里的context是application，不适合dialog使用
     */
    override fun onClick(context: Context?) {
        if (context == null) return
        val activity = ActivityUtils.getTopActivity()
        //用于展示
        val hostTitles: List<String> = listOf(
            "线上接口地址",
            "预发接口",
            "QA接口",
            "研发Native"
        )
        val defaultHost = hosts.last()
        //选中的host
        val checkHost =
            CacheDiskStaticUtils.getString(CONFIG_KEY_SERVER_HOST) ?: defaultHost
        var checkedIndex = hosts.indexOf(checkHost)
        if (checkedIndex < 0) checkedIndex = hostTitles.size - 1//也就是研发Native那个
        //启用输入的host
        MaterialDialog(activity).title(text = "切换Server接口")
            .cancelable(false)
            .cancelOnTouchOutside(false)
            .listItemsSingleChoice(
                items = hostTitles,
                initialSelection = checkedIndex,
                selection = object : SingleChoiceListener {
                    override fun invoke(dialog: MaterialDialog, index: Int, text: CharSequence) {
                        //如果选择了研发native，index就会超过host的index
                        if (index < hosts.size) {
                            CacheDiskStaticUtils.put(CONFIG_KEY_SERVER_HOST, hosts[index])
                        }
                    }
                })
            .input(
                hint = "请输入项目接口的baseUrl地址",
                prefill = checkHost,
                callback = object : InputCallback {
                    override fun invoke(dialog: MaterialDialog, sequence: CharSequence) {
                        //和上面list选择同时存在的时候，这里先走到，然后才是上面的回调，所以会被覆盖
                        CacheDiskStaticUtils.put(CONFIG_KEY_SERVER_HOST, sequence.toString())
                    }
                })
            .positiveButton(text = "确定")
            .negativeButton(text = "取消")
            .show()
    }

    override fun getIcon(): Int {
        return R.drawable.icon_server_host
    }

    override fun onAppInit(context: Context?) {

    }

    override fun getName(): Int {
        return R.string.str_aide_title_server_host
    }

    override fun getCategory(): Int {
        return Category.BIZ
    }
}


//用于标记host配置的key
const val CONFIG_KEY_SERVER_HOST = "server_host"