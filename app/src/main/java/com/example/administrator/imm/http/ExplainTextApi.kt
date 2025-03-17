package com.example.administrator.imm.http

import com.hjq.http.config.IRequestCache
import com.hjq.http.model.CacheMode

/**
 * 富文本 接口
 */
class ExplainTextApi : BaseApi(), IRequestCache {
    val name = "explain";
    override fun getApi(): String {
        return "/api/Icon/getCommonConfig";
    }

    override fun getCacheMode(): CacheMode {
        return CacheMode.USE_CACHE_FIRST
    }

    override fun getCacheTime(): Long {
        return 10 * 1000L
    }
}