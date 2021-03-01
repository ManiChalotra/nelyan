package com.nelyan.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.nelyan.AppUtils
import com.nelyan.R
import com.nelyan.fragments.ActivityListFragment
import com.nelyan.fragments.ChatListFragment
import com.nelyan.fragments.TraderListingFragment
import com.nelyan.modals.HomeModal
import com.nelyan.ui.SectorizationActivity
import java.util.*

class MyHomeAdapter(activity: FragmentActivity, datalist: ArrayList<HomeModal>) : RecyclerView.Adapter<MyHomeAdapter.Vh>() {
    var a: Activity
    var context: Context? = null
    var ll1: LinearLayout? = null
    var datalist: ArrayList<HomeModal>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(a).inflate(R.layout.row_home, parent, false)
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.img.setImageResource(datalist[position].img)
        holder.text.text = datalist[position].task
        holder.ll1.setOnClickListener {
            if (position == 0) {
                AppUtils.gotoFragment(a, ActivityListFragment(), R.id.frame_container, false)
            } else if (position == 1) {
                AppUtils.gotoFragment(a, ChatListFragment(), R.id.frame_container, false)
            } else if (position == 2) {
                val i = Intent(a, SectorizationActivity::class.java)
                a.startActivity(i)
            } else if (position == 3) {
                AppUtils.gotoFragment(a, TraderListingFragment(), R.id.frame_container, false)
            }
        }
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView
        var ll1: LinearLayout
        var text: TextView

        init {
            img = itemView.findViewById(R.id.iv_homeimg)
            ll1 = itemView.findViewById(R.id.ll1)
            text = itemView.findViewById(R.id.tv_hometxt)
        }
    }

    init {
        a = activity
        this.datalist = datalist
    }
}