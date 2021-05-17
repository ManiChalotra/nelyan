package com.nelyan_live.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.R
import com.nelyan_live.data.network.responsemodels.trader_type.TraderTypeResponse
import com.nelyan_live.modals.activity_type.ActivityTypeResponse
import com.nelyan_live.modals.childcaretype.ChildCareTypeRespone
import com.nelyan_live.ui.EditActivity
import com.nelyan_live.ui.EditBabySitterActivity
import com.nelyan_live.ui.EditTraderActivity
import kotlinx.android.synthetic.main.item_vehicles.view.*

class ChildCareTypeDialogboxAdapter(var editBabySitterActivity: EditBabySitterActivity, var modelTypes: MutableList<ChildCareTypeRespone.Data>?,
                                    var onclicklistener: onClickListener) :
        RecyclerView.Adapter<ChildCareTypeDialogboxAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):MyViewHolder {
        return MyViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_vehicles, p0, false))
    }
    interface  onClickListener
    {
        fun subcatClick(subcatName: String?,id: String)
    }

    override fun getItemCount(): Int {
        return modelTypes!!.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.titleText.text = modelTypes!![position].categoryName
        holder.itemView.setOnClickListener {
            onclicklistener.subcatClick(modelTypes!![position].categoryName, modelTypes!![position].id.toString())
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}