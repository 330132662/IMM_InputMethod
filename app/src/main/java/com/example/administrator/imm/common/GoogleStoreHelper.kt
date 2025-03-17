package com.example.administrator.imm.common

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import java.net.URLEncoder

class GoogleStoreHelper {

    companion object {
        fun isGooglePlayAvailable(context: Context): Boolean {
            return try {
                context.packageManager.getPackageInfo("com.android.vending", 0)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }


        fun openAppInPlayStore(context: Context, packageName: String) {
            try {
                // 尝试使用应用内跳转
                context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("market://details?id=$packageName")
                    setPackage("com.android.vending") // 强制使用Google Play
                })
            } catch (e: ActivityNotFoundException) {
                // 回退到网页版
                context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                })
            }
        }
        fun rateApp(context: Context) {
            val packageName = context.packageName
            try {
                context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("market://details?id=$packageName&reviewId=0")
                    setPackage("com.android.vending")
                })
            } catch (e: Exception) {
                context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                })
            }
        }

        fun openDeveloperApps(context: Context, developerName: String) {
            val encodedName = URLEncoder.encode(developerName, "UTF-8")
            try {
                context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("market://search?q=pub:$encodedName")
                    setPackage("com.android.vending")
                })
            } catch (e: Exception) {
                context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://play.google.com/store/apps/developer?id=$encodedName")
                })
            }
        }

    }

}