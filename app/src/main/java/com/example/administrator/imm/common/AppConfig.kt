package com.example.administrator.imm.common

import com.example.administrator.imm.BuildConfig

class AppConfig {
    companion object {
        fun getHostUrl(): String {
            return "";
        }

        fun isDebug(): Boolean {
            return BuildConfig.DEBUG;
        }

        fun getPackageName(): String {
            return BuildConfig.APPLICATION_ID
        }

        /**
         * 获取当前应用的版本名
         */
        fun getVersionName(): String {
            return BuildConfig.VERSION_NAME
        }

        /**
         * 获取当前应用的版本码
         */
        fun getVersionCode(): Int {
            return BuildConfig.VERSION_CODE
        }
    }
}