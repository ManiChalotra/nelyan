/*
package com.nelyan_live.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nelyan_live.AppUtils
import com.nelyan_live.R
import com.nelyan_live.ui.ChildCareActivity

class TraderListingFragment : Fragment() {
    lateinit  var mContext: Context
    var ivHeart: ImageView? = null
    var ivBack: ImageView? = null
    var ivMap: ImageView? = null
    var title: TextView? = null
    var tvFilter: TextView? = null
    var rl_1: RelativeLayout? = null
    lateinit  var v: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_trader_listing, container, false)
        mContext = requireContext()
        ivBack = v.findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener {
            val fm = requireActivity()!!.supportFragmentManager
            val f = fm.findFragmentById(R.id.frame_container)
            fm.popBackStack()
        })
        ivMap = v.findViewById(R.id.ivMap)
        ivMap!!.setOnClickListener(View.OnClickListener {
            val i = Intent(activity, ChildCareActivity::class.java)
            startActivity(i)
        })
        tvFilter = v.findViewById(R.id.tvFilter)
        tvFilter!!.setOnClickListener(View.OnClickListener { AppUtils.gotoFragment(mContext, TradeFilterFragment(), R.id.frame_container, false) })
        ivHeart = v.findViewById(R.id.ivHeart)
        ivHeart!!.setOnClickListener(View.OnClickListener { AppUtils.gotoFragment(mContext, FavoriteFragment(), R.id.frame_container, false) })
        rl_1 = v.findViewById(R.id.rl_1)
        rl_1!!.setOnClickListener(View.OnClickListener { AppUtils.gotoFragment(mContext, com.nelyan_live.fragments.TraderPublishFragment(), R.id.frame_container, false) })
        return v
    }
}*/
