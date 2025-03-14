package com.example.administrator.imm.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.administrator.imm.R
import com.example.administrator.imm.adapter.GridAdapter

class ExpFrag(typeId: Int) : Fragment(typeId) {
    private var typeId = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData();
    }
    private var gridAdapter: GridAdapter? = null
    private var biaoqing: List<Drawable>? = null
    private fun initData() {

        val recyclerView: RecyclerView = rootView?.findViewById<RecyclerView>(R.id.list)!!

        //        recyclerView.addItemDecoration(new GridSpaceDecoration1());
        gridAdapter = GridAdapter(context)
        biaoqing = ArrayList<Drawable>()
        /*for (i in 0..59) {
            biaoqing.add(resources.getDrawable(R.mipmap.ic_launcher))
        }
        gridAdapter.setDataList(biaoqing)*/
        recyclerView.adapter = gridAdapter
    }

    /** 根布局 */
    private var rootView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.frag_exp, container, false)
        return rootView;
        return super.onCreateView(inflater, container, savedInstanceState)
    }


}