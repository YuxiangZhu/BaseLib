package com.ly.traffic.app.libui.loading

import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.ly.traffic.app.libui.R
import com.ly.traffic.app.libui.databinding.VLoadingDialogBinding

/**
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2020年03月30日 14:41
 * 签名： 天行健，君子以自强不息；地势坤，君子以厚德载物。
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/  -- 志威 zhiwei.org
 *
 * You never know what you can do until you try !
 * ----------------------------------------------------------------
 * 网络进度、成功、失败、提示 等状态的dialog
 */
class LoadingDialog : AppCompatDialog {

    private lateinit var binding: VLoadingDialogBinding

    val obMsg = ObservableField<String>("正在加载...")
    val obLoading = ObservableBoolean(true)
    val obState = ObservableInt(R.drawable.ic_loading_ing)//状态图片

    constructor(context: Context) : this(context, R.style.LoadingDialog) {
        initView()
    }

    constructor(context: Context?, theme: Int) : super(context, theme) {
        initView()
    }

    constructor(
        context: Context?,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener) {
        initView()
    }

    private fun initView() {
        binding = VLoadingDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //触摸点击不可取消掉
        setCanceledOnTouchOutside(false)
        //obv data
        binding.loading = obLoading
        binding.msg = obMsg
        binding.state = obState
    }

    /**
     * 提示文案
     */
    fun setMessage(msg: String?) {
        obMsg.set(msg)
    }


    fun show(@DrawableRes state: Int, msg: String) {
        obLoading.set(state == R.drawable.ic_loading_ing)
        obState.set(state)
        obMsg.set(msg)
        super.show()
    }


    fun dismiss(@DrawableRes state: Int, msg: String) {
        obLoading.set(state == R.drawable.ic_loading_ing)
        obState.set(state)
        obMsg.set(msg)
        Handler().postDelayed({ super.dismiss() }, 1000)
    }
}