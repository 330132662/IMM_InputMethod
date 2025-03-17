package com.example.administrator.imm.ui

import android.graphics.Color
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.AppCompatButton
import com.example.administrator.imm.R
import com.example.administrator.imm.common.AppActivity
import com.example.administrator.imm.http.BrowserView
import com.example.administrator.imm.http.ExplainTextApi
import com.example.administrator.imm.model.CommonResp
import com.hjq.http.EasyHttp
import com.hjq.http.lifecycle.ApplicationLifecycle
import com.hjq.http.listener.OnHttpListener
import okhttp3.Call

/**
 *  引导页1
 */
class G1Act : AppActivity() {
    private val btn_next: AppCompatButton by lazy { findViewById(R.id.btn_next) }
    private val webview: BrowserView by lazy { findViewById(R.id.webview) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_g1)
        this.initView();
    }

    private fun initView() {
        btn_next.setOnClickListener {
            startActivity(G2Act::class.java)
        }
        webview.settings.apply {
            javaScriptEnabled = true // 启用JS
            domStorageEnabled = true // 启用DOM存储
            setSupportZoom(true)     // 支持缩放

        }
//        webview.loadUrl("https://m.163.com/")
        reqExplain();
    }

    private var htmlData: String = ""
    private fun reqExplain() {
        val api = ExplainTextApi()
        //        api.setLast_id(0);
        EasyHttp.get(ApplicationLifecycle.getInstance()).api(api)
            .request(object : OnHttpListener<CommonResp> {
                override fun onSucceed(result: CommonResp, cache: Boolean) {
                    super.onSucceed(result, cache)
                    htmlData = result.data!!;

                }

                override fun onSucceed(result: CommonResp) {
                    htmlData = result.data!!;
                }

                override fun onEnd(call: Call) {
                    super.onEnd(call)
                    loadData();
                }

                override fun onFail(e: Exception) {
                }
            })
    }

    private fun loadData() {

        // 设置WebView透明
        webview.setBackgroundColor(Color.TRANSPARENT)
        webview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null) // 可选，解决部分设备兼容性问题

// 强制网页内容透明（需注入CSS）
        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                injectTransparentCSS()
            }
        }
//        webview.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null)
        webview.loadData(htmlData, "text/html", "utf-8");
    }


    private fun injectTransparentCSS() {
        val css = "body { background-color: transparent !important; }"
        val js = """
        var style = document.createElement('style');
        style.type = 'text/css';
        style.innerHTML = '$css';
        document.head.appendChild(style);
    """.trimIndent()

        webview.evaluateJavascript(js, null)
    }

}