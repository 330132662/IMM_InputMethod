package com.example.administrator.imm.ui

import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.example.administrator.imm.R
import com.example.administrator.imm.common.AppActivity

/**
 *  引导页2  视频页
 */
class G2Act : AppActivity() {
    private  val btn_next: AppCompatButton by lazy { findViewById(R.id.btn_next) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_g2)
        this.initView();
    }

    private fun initView() {
        btn_next.setOnClickListener {
            startActivity(G3Act::class.java)
        }
    }
}