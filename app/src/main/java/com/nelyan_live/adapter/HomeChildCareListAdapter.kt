package com.nelyan_live.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.R
import com.nelyan_live.modals.homechildcare.HomeChildCareResponseData

class HomeChildCareListAdapter(var context: Context, internal var childCareDatalist: ArrayList<HomeChildCareResponseData>, var listner: OnChatListItemClickListner) : RecyclerView.Adapter<HomeChildCareListAdapter.RecyclerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {

        return RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.list_activitylist, parent, false), listner)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.initalize(childCareDatalist[position])
    }
    override fun getItemCount(): Int {
        return childCareDatalist.size
    }

    inner class RecyclerViewHolder(view: View, var listner:OnChatListItemClickListner): RecyclerView.ViewHolder(view){

        fun initalize(childCareDatalist: HomeChildCareResponseData){
            itemView.setOnClickListener {
                listner!!.onItemClickListner(adapterPosition)
            }

        }
    }

    interface OnChatListItemClickListner{
        fun onItemClickListner(position: Int)
    }



}