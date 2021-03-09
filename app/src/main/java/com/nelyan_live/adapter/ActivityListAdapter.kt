package com.nelyan_live.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.AppUtils
import com.nelyan_live.R
import com.nelyan_live.fragments.ActivityDetailsFragment
import com.nelyan_live.fragments.FavoriteFragment

class ActivityListAdapter(activity: FragmentActivity, listing: OnMyEventRecyclerViewItemClickListner) : RecyclerView.Adapter<ActivityListAdapter.RecyclerViewHolder>() {
    var context: Context? = null
    var a: Activity
    var rl_1: RelativeLayout? = null
    var image: ImageView? = null
    var iv_fev: ImageView? = null
    var listner: OnMyEventRecyclerViewItemClickListner

    inner class RecyclerViewHolder(view: View?) : RecyclerView.ViewHolder(view!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val v = LayoutInflater.from(a).inflate(R.layout.list_activitylist, parent, false)
        //     View v = inflater.inflate(R.layout.list_activitylist, parent, false);
        rl_1 = v.findViewById(R.id.rl_1)
        rl_1!!.setOnClickListener(View.OnClickListener { AppUtils.gotoFragment(a, ActivityDetailsFragment(), R.id.frame_container, false) })
        iv_fev = v.findViewById(R.id.iv_fev)
        iv_fev!!.setOnClickListener(View.OnClickListener { AppUtils.gotoFragment(a, FavoriteFragment(), R.id.frame_container, false) })
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return 3
    }

    interface OnMyEventRecyclerViewItemClickListner {
        fun onMyEventItemClickListner()
    }

    //    public ActivityListAdapter( Context context) {
    //        this.context = context;
    //        inflater = LayoutInflater.from(context);
    //    }
    init {
        a = activity
        listner = listing
    }
}