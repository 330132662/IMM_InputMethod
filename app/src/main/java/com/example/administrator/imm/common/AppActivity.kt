package com.example.administrator.imm.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.administrator.imm.R
import com.hjq.bar.TitleBar

open class AppActivity : Activity() {


    protected fun toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }



    protected fun startActivity(cls: Class<*>) {
        startActivity(Intent(this, cls))
    }
}