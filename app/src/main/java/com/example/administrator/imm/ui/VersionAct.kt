package com.example.administrator.imm.ui

import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.example.administrator.imm.BuildConfig
import com.example.administrator.imm.R
import com.example.administrator.imm.common.AppActivity
import com.example.administrator.imm.common.GoogleStoreHelper

/**
 *  版本页面  2025年3月12日10:10:25
 */
class VersionAct : AppActivity() {
    private val btn_next: AppCompatButton by lazy { findViewById(R.id.btn_next) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_v)
        this.initView();
    }

    private fun initView() {
        btn_next.setOnClickListener {
//            去谷歌应用商店
//            GoogleStoreHelper.openAppInPlayStore(this, BuildConfig.APPLICATION_ID);
            GoogleStoreHelper.openAppInPlayStore(this, "com.android.chrome");
        }
    }
}