package com.ly.traffic.app.booster

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.ly.traffic.app.libui.loading.LoadingDialog

/**
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2019年12月06日 14:22
 * 签名： 天行健，君子以自强不息；地势坤，君子以厚德载物。
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/  -- 志威 zhiwei.org
 *
 * You never know what you can do until you try !
 * ----------------------------------------------------------------
 * Activity的封装base基类 todo 设置竖屏 使用的时候 注意
 */
abstract class BaseActivity : AppCompatActivity() {

    private var loadingDialog: LoadingDialog? = null

    open val portraitScreen: Boolean = true//默认竖屏，特殊的，可以override该属性即可

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initConfig()
    }

    /**
     * 基础配置
     */
    @SuppressLint("SourceLockedOrientationActivity")
    private fun initConfig() {
        //progressLoading
        loadingDialog = LoadingDialog(this)
        //全局禁止横屏
        if (portraitScreen)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /**
     * 显式loading
     * @param show true显式 false 不显示
     */
    fun showProgress(show: Boolean) {
        if (show) {
            loadingDialog?.show()
        } else {
            loadingDialog?.dismiss()
        }
    }


    fun showWithState(@DrawableRes state: Int = R.drawable.ic_loading_ing, msg: String) {
        loadingDialog?.show(state, msg)
    }

    fun dismissWithState(@DrawableRes state: Int = R.drawable.ic_loading_ing, msg: String) {
        loadingDialog?.dismiss(state, msg)
    }

    /**
     * 显示不同文案
     *
     * @param loadMessage loading提示文案
     */
    fun showProgress(loadMessage: String?) {
        loadingDialog?.setMessage(loadMessage)
        loadingDialog?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog?.dismiss()
    }

}