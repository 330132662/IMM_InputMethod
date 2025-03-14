package com.example.administrator.imm.common

import android.app.Application
import android.os.Build
import com.example.administrator.imm.http.RequestHandler
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import com.hjq.demo.http.glide.GlideApp
import com.hjq.gson.factory.GsonFactory
import com.hjq.http.EasyConfig
import com.hjq.http.config.RequestServer
import com.hjq.language.MultiLanguages
import com.hjq.language.OnLanguageListener
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.style.MIUIStyle
import com.tencent.mmkv.MMKV
import okhttp3.OkHttpClient
import timber.log.Timber
import java.util.Locale

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initSDK()
    }

    private fun initSDK() {
        MultiLanguages.init(this)
        MMKV.initialize(this);

        MultiLanguages.setOnLanguageListener(object : OnLanguageListener {
            override fun onAppLocaleChange(oldLocale: Locale?, newLocale: Locale?) {
                Timber.d("onAppLocaleChange{${newLocale?.language}}")
            }

            override fun onSystemLocaleChange(oldLocale: Locale?, newLocale: Locale?) {
                Timber.d("onSystemLocaleChange{${newLocale?.language}}")
            }

        })


        DialogX.DEBUGMODE = AppConfig.isDebug()
        DialogX.init(this)

        /*if (brand == "xiaomi") {
            DialogX.globalStyle = MIUIStyle()
        } else {
            DialogX.globalStyle = IOSStyle()
        }*/
        DialogX.globalStyle = MIUIStyle()
        DialogX.implIMPLMode = DialogX.IMPL_MODE.VIEW
        DialogX.useHaptic = true
        DialogX.globalTheme = DialogX.THEME.AUTO

        DialogX.onlyOnePopTip = false

        // 网络请求框架初始化
        val okHttpClient: OkHttpClient = OkHttpClient.Builder().build()

        EasyConfig.with(okHttpClient)
            // 是否打印日志
            .setLogEnabled(AppConfig.isDebug())
            // 设置服务器配置
            .setServer(RequestServer("http://baidu.com"))
            // 设置请求处理策略
            .setHandler(RequestHandler(this)).addHeader(MmkvUtil.Token, "")
            .addHeader(MmkvUtil.Version, AppConfig.getVersionName())
            .addHeader(MmkvUtil.MN, MmkvUtil.getString(MmkvUtil.MN, "-"))
            .addHeader("v-code", "${AppConfig.getVersionCode()}").addHeader(
                "phone",
                Build.BRAND + "-" + Build.MODEL + "-" + Build.PRODUCT + "-" + Build.BOARD + "-" + Build.DEVICE + "-Android" + Build.VERSION.RELEASE + "-API" + Build.VERSION.SDK_INT
            )
            // 设置请求重试次数
            .setRetryCount(1).into()

        // 设置 Json 解析容错监听
        GsonFactory.setJsonCallback { typeToken: TypeToken<*>, fieldName: String?, jsonToken: JsonToken ->

        }

        // 初始化日志打印
        if (AppConfig.isDebug()) {
            Timber.plant(DebugLoggerTree())
        }

    }
    override fun onLowMemory() {
        super.onLowMemory()
        // 清理所有图片内存缓存
         GlideApp.get(this).onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        // 根据手机内存剩余情况清理图片内存缓存
        GlideApp.get(this).onTrimMemory(level)
    }

}