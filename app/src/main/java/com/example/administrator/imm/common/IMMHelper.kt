package com.example.administrator.imm.common

import android.content.Context
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import com.example.administrator.imm.BuildConfig


class IMMHelper {
    companion object {
        private val imeId = BuildConfig.APPLICATION_ID + "/.AndroidInputMethodService";
        fun isMyImeEnabled(ctx: Context): Boolean {
            val imm = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                ?: return false

            // 获取所有已启用的输入法列表
            val enabledInputMethods = imm.enabledInputMethodList

            // 遍历列表，检查是否存在自己的输入法
            for (info in enabledInputMethods) {
                if (info.id == imeId) {
                    return true
                }
            }
            return false
        }

        fun isMyImeDefault(ctx: Context): Boolean {
            val currentIme = Settings.Secure.getString(
                ctx.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD
            )
            return currentIme != null && currentIme.equals(imeId)
        }

    }
}