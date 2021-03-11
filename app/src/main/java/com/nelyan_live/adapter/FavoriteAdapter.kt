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
import com.nelyan_live.ui.NurserieActivity
import com.nelyan_live.ui.NurseryActivity
import com.nelyan_live.utils.OpenActivity

class FavoriteAdapter(activity: FragmentActivity) : RecyclerView.Adapter<FavoriteAdapter.RecyclerViewHolder>() {
    var context: Context? = null
    var a: Activity
    var inflater: LayoutInflater? = null
    var rl_1: RelativeLayout? = null
    var iv_fev: ImageView? = null

    inner class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv_fev: ImageView
        var iv_unfev: ImageView

        init {
            iv_fev = view.findViewById(R.id.iv_fev)
            iv_unfev = view.findViewById(R.id.iv_unfev)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val v = LayoutInflater.from(a).inflate(R.layout.list_favorite, parent, false)
        context = parent.context
        //     View v = inflater.inflate(R.layout.list_activitylist, parent, false);
        rl_1 = v.findViewById(R.id.rl_1)
        rl_1!!.setOnClickListener(View.OnClickListener {
           context?.OpenActivity(NurseryActivity::class.java)// AppUtils.gotoFragment(a, NurserieFragment(), R.id.frame_container, false)
        })

        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.iv_fev.setOnClickListener {
            holder.iv_fev.visibility = View.GONE
            holder.iv_unfev.visibility = View.VISIBLE
        }
        holder.iv_unfev.setOnClickListener {
            holder.iv_fev.visibility = View.VISIBLE
            holder.iv_unfev.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

    init {
        a = activity
    }
}