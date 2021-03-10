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
import com.nelyan_live.ui.ActivityDetailsActivity
import com.nelyan_live.ui.FavouriteActivity
import com.nelyan_live.utils.OpenActivity

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
        context = parent.context
        //     View v = inflater.inflate(R.layout.list_activitylist, parent, false);
        rl_1 = v.findViewById(R.id.rl_1)
        rl_1!!.setOnClickListener(View.OnClickListener {
            context?.OpenActivity(ActivityDetailsActivity::class.java)
            //AppUtils.gotoFragment(a, ActivityDetailsFragment(), R.id.frame_container, false)
        })
        iv_fev = v.findViewById(R.id.iv_fev)
        iv_fev!!.setOnClickListener(View.OnClickListener {

           context?.OpenActivity(FavouriteActivity::class.java)
           // AppUtils.gotoFragment(a, FavoriteFragment(), R.id.frame_container, false)
        })
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return 30
    }

    interface OnMyEventRecyclerViewItemClickListner {
        fun onMyEventItemClickListner()
    }

    init {
        a = activity
        listner = listing
    }
}