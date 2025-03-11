package com.example.administrator.imm.ui

import android.os.Bundle
import com.example.administrator.imm.R
import com.example.administrator.imm.common.AppActivity
import com.flyco.tablayout.SegmentTabLayout

/**
 *  表情包列表
 */
class ExpListAct : AppActivity() {
    private var tl_2: SegmentTabLayout? =
        null //: SegmentTabLayout by lazy { findViewById(R.id.tl_2) }

    private val titles = arrayOf("精选", "热门","aaaaaaaa")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_explist)
        this.initView();
    }

    private fun initView() {
        val mDecorView = window.decorView
        tl_2 = mDecorView.findViewById(R.id.tl_2);
        tl_2?.setTabData(titles)
    }
}