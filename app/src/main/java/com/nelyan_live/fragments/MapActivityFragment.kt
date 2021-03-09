package com.nelyan_live.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.nelyan_live.AppUtils
import com.nelyan_live.R

class MapActivityFragment : Fragment() {
    lateinit  var mContext: Context
    var ivBack: ImageView? = null
    var ivFilter: ImageView? = null
    var rl_1: RelativeLayout? = null
    var v: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_map_activity, container, false)
        mContext = requireActivity()
        ivBack = v!!.findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener {
            val fm = requireActivity()!!.supportFragmentManager
            val f = fm.findFragmentById(R.id.frame_container)
            fm.popBackStack()
        })
        ivFilter = v!!.findViewById(R.id.ivFilter)
        ivFilter!!.setOnClickListener(View.OnClickListener {
            //    AppUtils.gotoFragment(mContext, new ActivityFragment(), R.id.frame_container, false);
        })
        rl_1 = v!!.findViewById(R.id.rl_1)
        rl_1!!.setOnClickListener(View.OnClickListener { AppUtils.gotoFragment(mContext, ActivityListFragment(), R.id.frame_container, false) })
        return v
    }
}