package com.example.administrator.imm.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2020/08/28
 *    desc   :  选择 物品列表类型
 */
class MyFragmentAdapter constructor( fragmentActivity: FragmentActivity, frag:List<Fragment>) :
    FragmentStateAdapter(fragmentActivity) {
    private var fragments: List<Fragment>? = frag

    override fun getItemCount(): Int {
        return fragments?.size!!
    }

    override fun createFragment(position: Int): Fragment {
        return fragments!![position];
    }


}