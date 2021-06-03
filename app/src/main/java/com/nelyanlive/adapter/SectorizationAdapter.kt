package com.nelyanlive.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.nelyanlive.AppUtils
import com.nelyanlive.R
import com.nelyanlive.fragments.SectorizationDetailsFragment

class SectorizationAdapter(activity: FragmentActivity) : RecyclerView.Adapter<SectorizationAdapter.RecyclerViewHolder>() {
    var context: Context? = null
    var a: Activity
    var inflater: LayoutInflater? = null
    var rl_1: RelativeLayout? = null
    var iv_fev: ImageView? = null

    inner class RecyclerViewHolder(view: View?) : RecyclerView.ViewHolder(view!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val v = LayoutInflater.from(a).inflate(R.layout.list_sectorization, parent, false)
        //     View v = inflater.inflate(R.layout.list_activitylist, parent, false);
        rl_1 = v.findViewById(R.id.rl_1)
        rl_1!!.setOnClickListener(View.OnClickListener {
            AppUtils.gotoFragment(a, SectorizationDetailsFragment(), R.id.frame_container, false) })
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return 2
    }

    init {
        a = activity
    }
}