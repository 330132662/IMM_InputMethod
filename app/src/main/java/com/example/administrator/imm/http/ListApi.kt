package com.example.administrator.imm.http

import com.hjq.http.config.IRequestCache
import com.hjq.http.model.CacheMode

class ListApi : BaseApi(), IRequestCache {
    override fun getApi(): String {
        return "/api/Icon/list";
    }

    var type_id: Int = 0;
    var last_id: Int = 0;
    override fun getCacheMode(): CacheMode {
        return CacheMode.USE_CACHE_FIRST
    }

    override fun getCacheTime(): Long {
        return 10 * 1000L
    }
}