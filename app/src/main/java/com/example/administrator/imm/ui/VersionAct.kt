package com.example.administrator.imm.ui

import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.example.administrator.imm.BuildConfig
import com.example.administrator.imm.R
import com.example.administrator.imm.common.AppActivity
import com.example.administrator.imm.common.GoogleStoreHelper
import com.example.administrator.imm.common.MmkvUtil
import com.google.android.material.textview.MaterialTextView

/**
 *  版本页面  2025年3月12日10:10:25
 */
class VersionAct : AppActivity() {
    private val btn_next: AppCompatButton by lazy { findViewById(R.id.btn_next) }
    private val tv_version: MaterialTextView by lazy { findViewById(R.id.tv_version) }

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
        val v = MmkvUtil.getString(MmkvUtil.VERSION_NAME, "");
        tv_version.text = "Version：$v";
    }
}