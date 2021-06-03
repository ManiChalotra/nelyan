package com.nelyanlive.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nelyanlive.R

class ItemsAdapter(var context: Context) : RecyclerView.Adapter<ItemsAdapter.MyViewHolder>() {
    private var inflater: LayoutInflater? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        return MyViewHolder( LayoutInflater.from(viewGroup.context).inflate(R.layout.res, viewGroup, false))
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {}

    // Return the type of your itemsData (invoked by the layout manager)
    override fun getItemCount(): Int {
        return 5
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}