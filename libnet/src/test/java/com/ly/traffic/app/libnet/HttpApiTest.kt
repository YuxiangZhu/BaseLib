package com.ly.traffic.app.libnet

import org.junit.Test

/**
 * 作者： 志威  zhiwei.org
 * 主页： Github: https://github.com/zhiwei1990
 * 日期： 2020年03月25日 16:13
 * 签名： 天行健，君子以自强不息；地势坤，君子以厚德载物。
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/  -- 志威 zhiwei.org
 *
 * You never know what you can do until you try !
 * ----------------------------------------------------------------
 * HttpApi的单元测试
 */
class HttpApiTest {

    @Test
    fun testGet() {
        HttpApi.initConfig("http://ebk.qa.17u.cn/cxyopenapi/handmachine/authority/getStewardAuthority")
//        val get = HttpApi.get("http://ebk.qa.17u.cn/cxyopenapi/handmachine/order/saleTicketCount/39")
        HttpApi.get(emptyMap(), "?stewardId=39", object : IHttpCallback {
            override fun onSucceeded(string: String?) {
                println(string)
            }

            override fun onFailed(message: String?) {
                println(message)
            }
        })

    }
}