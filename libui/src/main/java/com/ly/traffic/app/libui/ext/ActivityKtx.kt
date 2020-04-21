package com.ly.traffic.app.libui.ext

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.ly.traffic.app.libcore.showToast

/**
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2020年03月19日 14:39
 * 签名： 天行健，君子以自强不息；地势坤，君子以厚德载物。
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/  -- 志威 zhiwei.org
 *
 * You never know what you can do until you try !
 * ----------------------------------------------------------------
 * Activity相关的扩展
 */

//<editor-folder desc="Activity相关的扩展函数">

/**
 * Activity中弹出toast提示
 * [text] tips文案
 * [duration] Toast的short或 long的显示flag ，或者填写 0 或 1
 */
fun Activity.showToast(text: String?, duration: Int = Toast.LENGTH_SHORT) {
    text?.showToast(this, duration)
}

/**
 * Activity中使用DataBinding时，setContentView
 * [layout] 布局layout
 * @return T 对应layout的binding
 */
fun <T : ViewDataBinding> Activity.bindView(@LayoutRes layout: Int): T {
    return DataBindingUtil.setContentView(this, layout)
}

/**
 * Activity中使用DataBinding时，setContentView
 * [view] View
 * @return T? 对应layout的binding 可能为null
 */
fun <T : ViewDataBinding> Activity.bindView(view: View): T? {
    return DataBindingUtil.bind<T>(view)
}

/**
 * 扩展ComponentActivity的使用liveData观察者方式，简便一下写法
 * [live]liveData
 * [observer] liveData的Observer
 */
fun <T : Any> ComponentActivity.observe(live: LiveData<T>, observer: Observer<T>) {
    live.observe(this, observer)
}

//</editor-folder>

// <editor-folder desc="Activity相关的扩展属性">


/**
 * 给ComponentActivity扩展lifeCycleOwner属性，因为它实现了lifeCycleOwner、ViewModelStoreOwner等JetPack相关的
 * 而顶层的Activity是没有实现的
 */
val ComponentActivity.viewLifeCycleOwner: LifecycleOwner
    get() = this


//</editor-folder>