package com.nelyan_live.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.nelyan_live.R
import com.nelyan_live.modals.HomeModal
import com.nelyan_live.utils.from_admin_image_base_URl

import kotlinx.android.synthetic.main.row_home.view.*
import java.util.*

class MyHomeAdapter(var context: Context, var mlist: ArrayList<HomeModal>, var listner: OnHomeRecyclerViewItemClickListner) : RecyclerView.Adapter<MyHomeAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.row_home, parent, false), listner)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.initalize(mlist, position)
    }

    override fun getItemCount(): Int {
        return mlist.size
    }


    inner class MyViewHolder(itemView: View, var listner: OnHomeRecyclerViewItemClickListner) : RecyclerView.ViewHolder(itemView) {

        val img = itemView.iv_homeimg
        val text = itemView.tv_hometxt

    fun initalize(list: ArrayList<HomeModal>, position: Int) {

            itemView.findViewById<ShapeableImageView>(R.id.iv_homeimg).shapeAppearanceModel = itemView.findViewById<ShapeableImageView>(R.id.iv_homeimg).shapeAppearanceModel.toBuilder()
                    .setTopRightCorner(CornerFamily.ROUNDED, 100F)
                    .setTopLeftCorner(CornerFamily.ROUNDED, 0F)
                    .setBottomLeftCorner(CornerFamily.ROUNDED, 100F)
                    .setBottomRightCorner(CornerFamily.ROUNDED, 100F)
                    .build()


            Glide.with(context).asBitmap().load(from_admin_image_base_URl + mlist[position].img).into(img)
            text.text = mlist[position].task

            itemView.setOnClickListener {
                listner.onItemHomeClickListner(list, position)
            }

        }
    }

    interface OnHomeRecyclerViewItemClickListner {
        fun onItemHomeClickListner(list: ArrayList<HomeModal>, position: Int)
    }


}