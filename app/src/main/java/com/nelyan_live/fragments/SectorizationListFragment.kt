package com.nelyan_live.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.R
import com.nelyan_live.adapter.SectorizationAdapter
import com.nelyan_live.ui.SectorizationMapActivity

class SectorizationListFragment : Fragment() {
  lateinit  var v: View
    var mContext: Context? = null
    var ivMap: ImageView? = null
    var ivMap1: ImageView? = null
    var ivBack: ImageView? = null
    var recyclerview: RecyclerView? = null
    var recyclerview1: RecyclerView? = null
    var sectorizationAdapter: SectorizationAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_sectorization_list, container, false)
        mContext = activity
        ivBack = v.findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener {
            val fm = requireActivity()!!.supportFragmentManager
            val f = fm.findFragmentById(R.id.frame_container)
            fm.popBackStack()
        })
        ivMap = v.findViewById(R.id.ivMap)
        ivMap1 = v.findViewById(R.id.ivMap1)
        ivMap1!!.setOnClickListener(View.OnClickListener {
            val i = Intent(activity, SectorizationMapActivity::class.java)
            startActivity(i)
        })
        ivMap!!.setOnClickListener(View.OnClickListener {
            val i = Intent(activity, SectorizationMapActivity::class.java)
            startActivity(i)
        })
        recyclerview = v.findViewById(R.id.recyclerview)
        sectorizationAdapter = SectorizationAdapter(requireActivity())
        recyclerview!!.setLayoutManager(LinearLayoutManager(mContext))
        recyclerview!!.setAdapter(sectorizationAdapter)
        recyclerview1 = v.findViewById(R.id.recyclerview1)
        sectorizationAdapter = SectorizationAdapter(requireActivity())
        recyclerview1!!.setLayoutManager(LinearLayoutManager(mContext))
        recyclerview1!!.setAdapter(sectorizationAdapter)
        return v
    }
}