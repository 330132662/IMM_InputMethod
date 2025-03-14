package com.example.administrator.imm.http

import com.hjq.http.config.IRequestApi

open class BaseApi : IRequestApi {
    val type = "android"
    override fun getApi(): String {
        return "/";
    }
}