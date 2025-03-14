package com.example.administrator.imm.http

import com.hjq.http.config.IRequestApi

class TypeApi : IRequestApi {
    override fun getApi(): String {
        return "/api/Icon/typelist";
    }
}