package com.example.administrator.imm.http

class VersionApi : BaseApi() {
    override fun getApi(): String {
        return "/api/Icon/getVersion";
    }
}