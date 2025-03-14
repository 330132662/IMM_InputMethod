package com.example.administrator.imm.http

import com.hjq.http.config.IRequestApi

class ListApi : IRequestApi {
    override fun getApi(): String {
        return "/api/Icon/list";
    }

    var type_id: Int = 0;
    var last_id: Int = 0;
}