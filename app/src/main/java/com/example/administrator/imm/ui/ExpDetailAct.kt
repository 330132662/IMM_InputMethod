package com.example.administrator.imm.ui

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.administrator.imm.R
import com.example.administrator.imm.adapter.ExpDetailAdapter
import com.example.administrator.imm.common.AppActivity
import com.example.administrator.imm.common.AppConfig.Companion.getHostUrl
import com.example.administrator.imm.http.ListApi
import com.example.administrator.imm.model.ListResp
import com.google.android.material.textview.MaterialTextView
import com.hjq.demo.http.glide.GlideApp
import com.hjq.http.EasyHttp
import com.hjq.http.lifecycle.ApplicationLifecycle
import com.hjq.http.listener.OnHttpListener
import okhttp3.Call

/**
 *  表情包的表情列表  2025年3月12日10:10:25
 */
class ExpDetailAct : AppActivity() {
    private var pkdId = 0;
    private var gridAdapter: ExpDetailAdapter? = ExpDetailAdapter(this)
    private val tv_title: MaterialTextView by lazy { findViewById(R.id.tv_title) }
    private val tv_desc: MaterialTextView by lazy { findViewById(R.id.tv_desc) }
    private val rv_explist: RecyclerView by lazy { findViewById(R.id.rv_explist) }
    private val iv_ic: AppCompatImageView by lazy { findViewById(R.id.iv_ic) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_detail);
        this.initView();
    }

    private fun initView() {
        pkdId = intent.getIntExtra(K1, 0);
        rv_explist.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                val m = 10;
                outRect.top = m
                outRect.bottom = m
                outRect.left = m
                outRect.right = m
            }
        })
        rv_explist.adapter = gridAdapter;
        reqList();
    }

    companion object {
        val K1 = "pkgId";
        fun show(ctx: Context, pkgId: Int) {
            val intent = Intent(ctx, ExpDetailAct::class.java)
            intent.putExtra(K1, pkgId)
            ctx.startActivity(intent);
        }
    }


    private var expList: List<ListResp.DataDTO>? = null
    private fun reqList() {
        val api = ListApi()
        api.setTypeId(pkdId);
        EasyHttp.get(ApplicationLifecycle.getInstance()).api(api)
            .request(object : OnHttpListener<ListResp> {
                override fun onSucceed(result: ListResp, cache: Boolean) {
                    super.onSucceed(result, cache)
                    expList = result.data
                }

                override fun onSucceed(typeResp: ListResp) {
                    expList = typeResp.data
                }

                override fun onEnd(call: Call) {
                    super.onEnd(call)
                    refreshExpList()
                }

                override fun onFail(e: java.lang.Exception) {
                }
            })
    }

    private fun refreshExpList() {
        if (expList == null || expList!!.isEmpty()) {
            return;
        }
        val first = expList?.get(0) ?: return;
        tv_title.text = first?.name;
        tv_desc.text = "${expList?.size} Emojis";

        gridAdapter?.setDataList(expList);

        var relPath: String = first.icon
        if (!relPath.startsWith("http")) {
            relPath = getHostUrl() + relPath
        }
        GlideApp.with(this).asBitmap().load(relPath)
            .apply(RequestOptions().transform(RoundedCorners(20))).into(iv_ic)

    }
}