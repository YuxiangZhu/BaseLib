package com.ly.traffic.app.booster

import android.app.Application
import androidx.annotation.Keep
import com.blankj.utilcode.util.CacheDiskStaticUtils
import com.ly.traffic.app.booster.dokit.AssistantApp
import com.ly.traffic.app.booster.dokit.CONFIG_KEY_SERVER_HOST

/**
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2020年04月01日 18:28
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
abstract class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AssistantApp.configKit(
            this, arrayListOf(
                SERVER_HOST_RELEASE,
                SERVER_HOST_PRETEST,
                SERVER_HOST_QA
            )
        )
    }


    //用于配置项目的baseUrl
    abstract val SERVER_HOST_RELEASE: String//正式接口地址
    abstract val SERVER_HOST_PRETEST: String//预发接口
    abstract val SERVER_HOST_QA: String//qa地址

    /**
     * 获取项目接口api的基础baseHost，业务场景，存在本地host、qa、预发以及线上正式host等
     * 统一配置管理，便于调试区分
     */
    fun apiHost(): String {
        val host = if (BuildConfig.DEBUG) SERVER_HOST_QA else SERVER_HOST_RELEASE
        return CacheDiskStaticUtils.getString(CONFIG_KEY_SERVER_HOST, host)
    }

}
