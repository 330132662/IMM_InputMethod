package com.example.administrator.imm.ui

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.angcyo.tablayout.DslTabLayout
import com.example.administrator.imm.R
import com.example.administrator.imm.adapter.ExpPackageAdapter
import com.example.administrator.imm.common.AppActivity
import com.example.administrator.imm.common.MmkvUtil
import com.example.administrator.imm.http.EventClick
import com.example.administrator.imm.http.TypeApi
import com.example.administrator.imm.http.VersionApi
import com.example.administrator.imm.model.CommonResp
import com.example.administrator.imm.model.TypeResp
import com.example.administrator.imm.model.VersionResp
import com.google.android.material.textview.MaterialTextView
import com.hjq.http.EasyHttp
import com.hjq.http.lifecycle.ApplicationLifecycle
import com.hjq.http.listener.OnHttpListener
import okhttp3.Call
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 *  表情包列表  版本号
 *
 */
class ExpListAct : AppActivity() {

    /*private var tl_2: SegmentTabLayout? =
        null //: SegmentTabLayout by lazy { findViewById(R.id.tl_2) }*/

    private val t_version: MaterialTextView by lazy { findViewById(R.id.t_version) }
    private val t_title: MaterialTextView by lazy { findViewById(R.id.t_title) }
    private val recy_explist: RecyclerView by lazy { findViewById(R.id.recy_explist) }
    private val tab_layout: DslTabLayout by lazy { findViewById(R.id.tab_layout) }
    private var expPkgAdapter = ExpPackageAdapter(this);


//    private val titles = arrayOf("EMOJIS", "STICKERS")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_explist)
        EventBus.getDefault().register(this);
        this.initView();
        reqV();
    }

    @Subscribe
    fun eventClick(e: EventClick) {
        val id = tabData?.get(e.pos)?.id!!;
        ExpDetailAct.show(this, id);
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this);
    }

    //    val decoration = ItemDecoration() ;
    private fun initView() {
//        val mDecorView = window.decorView

        /*tl_2 = mDecorView.findViewById(R.id.tl_2);
        tl_2?.setTabData(titles)*/
        recy_explist.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.top = 10
                outRect.bottom = 10
                outRect.left = 10
                outRect.right = 10
            }
        })
        recy_explist.adapter = expPkgAdapter;
        t_version.setOnClickListener {
            startActivity(VersionAct::class.java)
        }
        t_title.setOnClickListener {
//            startActivity(ExpDetailAct::class.java)
        }/* val def :DslSelectorConfig =  {

         }*/
        tab_layout.configTabLayoutConfig {
            //选中view的回调
            onSelectViewChange = { fromView, selectViewList, reselect, fromUser ->
                val toView = selectViewList.first()
                if (reselect) {
                    //重复选择
                }
            }
            //选中index的回调
            /*onSelectIndexChange = { fromIndex, selectIndexList, reselect, fromUser ->
                val toIndex = selectIndexList.first()
//                toast("$toIndex")
                if (toIndex == 0) {
                    loadEmoji()
                } else {
                    reqType();
                }
            }*/
        }
    }

    override fun onResume() {
        reqType();
        super.onResume()
    }

    private var tabData: List<TypeResp.DataDTO>? = mutableListOf();

    /**
     * 获取 表情包的 组
     */
    private fun reqType() {
        EasyHttp.get(ApplicationLifecycle.getInstance()).api(TypeApi())
            .request(object : OnHttpListener<TypeResp> {
                override fun onSucceed(result: TypeResp, cache: Boolean) {
                    super.onSucceed(result, cache)
                    tabData = result.data
                }

                override fun onSucceed(typeResp: TypeResp) {
                    tabData = typeResp.data
                }

                override fun onEnd(call: Call) {
                    super.onEnd(call)
                    //                loadFragment();
                    loadTab()
                }

                override fun onFail(e: java.lang.Exception) {
                }
            })
    }

    private fun loadTab() {
        expPkgAdapter.setDataList(tabData);

    }

    private fun reqV() {
        val api = VersionApi()
        EasyHttp.get(ApplicationLifecycle.getInstance()).api(api)
            .request(object : OnHttpListener<VersionResp> {
                override fun onSucceed(result: VersionResp, cache: Boolean) {
                    super.onSucceed(result, cache)
                    val v: String = result.data.newversion;
                    MmkvUtil.save(MmkvUtil.VERSION_NAME, v);
                    t_version.text = "Version：$v"
                }

                override fun onSucceed(result: VersionResp) {

                }

                override fun onEnd(call: Call) {
                    super.onEnd(call)

                }

                override fun onFail(e: Exception) {
                }
            })
    }

    /**
     *  模拟一条数据  展示一组emoji
     */
    private fun loadEmoji() {
        tabData = mutableListOf()
//        tabData.clear()
        val em = TypeResp.DataDTO()
        em.id = -1
        em.count = 136;
        em.icon = "";
        em.name = "Emojis";
        (tabData as MutableList<TypeResp.DataDTO>).add(em)
        expPkgAdapter.setDataList(tabData);
    }
}