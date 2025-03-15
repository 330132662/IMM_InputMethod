package com.example.administrator.imm.http

import android.graphics.Bitmap

class EventClick {
    var pos = -1 ;
    var img:Bitmap? = null



    constructor(pos: Int, img: Bitmap?) {
        this.pos = pos
        this.img = img
    }
}