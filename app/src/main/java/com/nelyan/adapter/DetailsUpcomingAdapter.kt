package com.nelyan.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.nelyan.R

class DetailsUpcomingAdapter(activityDetailsActivity: FragmentActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var a: Activity

    inner class RecyclerViewHolder(view: View?) : RecyclerView.ViewHolder(view!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(a).inflate(R.layout.row_upcoming_details, parent, false)
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return 2
    }

    init {
        a = activityDetailsActivity
    }
}