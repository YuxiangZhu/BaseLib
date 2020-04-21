package com.ly.traffic.app.booster.dokit

import android.app.Application
import com.didichuxing.doraemonkit.DoraemonKit
import com.didichuxing.doraemonkit.kit.AbstractKit

/**
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2019年12月30日 18:24
 * 签名： 天行健，君子以自强不息；地势坤，君子以厚德载物。
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/  -- 志威 zhiwei.org
 *
 * You never know what you can do until you try !
 * ----------------------------------------------------------------
 * 调试相关的application配置，todo 放在application中初始化
 */
internal object AssistantApp {
    /**
     * 配置工具
     */
    fun configKit(application: Application, hosts: ArrayList<String>) {
        val kits = arrayListOf<AbstractKit>(ServerKit(hosts))
        DoraemonKit.install(application, kits)
    }
}