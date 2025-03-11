package com.example.administrator.imm.ui

import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.example.administrator.imm.R
import com.example.administrator.imm.common.AppActivity

/**
 *  引导页3  start  期待
 */
class G3Act : AppActivity() {
    private val btn_next: AppCompatButton by lazy { findViewById(R.id.btn_next) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_g3)
        this.initView();
    }

    private fun initView() {
        btn_next.setOnClickListener {
            startActivity(ExpListAct::class.java)
        }
    }
}