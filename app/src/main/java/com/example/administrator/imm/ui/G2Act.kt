package com.example.administrator.imm.ui

import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.widget.AppCompatButton
import com.example.administrator.imm.R
import com.example.administrator.imm.common.AppActivity
import com.example.administrator.imm.common.IMMHelper
import com.example.administrator.imm.common.MmkvUtil
import timber.log.Timber

/**
 *  引导页2  视频页
 */
class G2Act : AppActivity() {
    private val btn_next: AppCompatButton by lazy { findViewById(R.id.btn_next) }
    val videoview: VideoView? by lazy { findViewById(R.id.videoview) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_g2)
        this.initView();
    }

    private fun initView() {
        btn_next.setOnClickListener {
            if (IMMHelper.isMyImeDefault(this)) {
                startActivity(G3Act::class.java)
            } else {
                this.chooseImm();
            }
        }
        playVideo("android.resource://" + packageName + "/" + R.raw.a)
    }

    private var mp4Controller: MediaController? = null;
    private fun playVideo(pathOrUrl: String) {
        videoview?.setVideoURI(Uri.parse(pathOrUrl.toString()));
        mp4Controller = MediaController(this, true);
        videoview?.setMediaController(mp4Controller!!);videoview?.requestFocus();
        mp4Controller?.setMediaPlayer(videoview)
        videoview?.setOnErrorListener(object : MediaPlayer.OnErrorListener {
            override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
                Timber.e("视频播放有误 $p1 $p2")
                if (MmkvUtil.getString(MmkvUtil.AdLocalfile, "").equals("")) {
                }
                return true;
            }
        })
        videoview?.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
            override fun onCompletion(mp: MediaPlayer?) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//               低版本Android 播放完就重启
//                    restartApp();
                } else {
                }
//                videoview?.stopPlayback()
                videoview!!.start();
//                startPlay()
            }

        })
//        ToastUtils.show("开始播放 ")
        videoview?.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
            override fun onPrepared(p0: MediaPlayer?) {
                Timber.i("onPrepared ${p0.toString()}")

                videoview!!.start()
            }

        })
    }

    private fun chooseImm() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showInputMethodPicker()
    }

    override fun onDestroy() {
        videoview?.stopPlayback()
        super.onDestroy()
    }
}