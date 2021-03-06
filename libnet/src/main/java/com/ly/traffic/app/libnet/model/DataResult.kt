package com.ly.traffic.app.libnet.model

import androidx.annotation.Keep

/**
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2020年03月25日 11:20
 * 签名： 天行健，君子以自强不息；地势坤，君子以厚德载物。
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/  -- 志威 zhiwei.org
 *
 * You never know what you can do until you try !
 * ----------------------------------------------------------------
 * 数据响应的封装类 ，密封类sealed
 * 其实可以使用kotlin标准库中的Result
 */
@Keep
sealed class DataResult<out R> {

    //成功状态的时候
    @Keep
    data class Success<out T>(val data: T) : DataResult<T>()

    //错误，失败状态的时候
    @Keep
    data class Error(val exception: Exception) : DataResult<Nothing>()

    //加载数据中
    @Keep
    object Loading : DataResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }

}

/**
 *  返回结果如果是Success类，且data非null，才认为是成功的。
 */
val DataResult<*>.succeeded
    get() = this is DataResult.Success && data != null