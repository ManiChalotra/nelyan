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

        /*

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
         }*/


    }

    override fun getItemCount(): Int {
        return mlist.size
    }

    inner class MyViewHolder(itemView: View, var listner: OnHomeRecyclerViewItemClickListner) : RecyclerView.ViewHolder(itemView) {

        val img = itemView.iv_homeimg
        val text = itemView.tv_hometxt

        fun initalize(list: ArrayList<HomeModal>, position: Int) {

            itemView.findViewById<ShapeableImageView>(R.id.iv_homeimg).setShapeAppearanceModel(
                    itemView.findViewById<ShapeableImageView>(R.id.iv_homeimg).getShapeAppearanceModel().toBuilder()
                            .setTopRightCorner(CornerFamily.ROUNDED, 100F)
                            .setTopLeftCorner(CornerFamily.ROUNDED, 0F)
                            .setBottomLeftCorner(CornerFamily.ROUNDED, 100F)
                            .setBottomRightCorner(CornerFamily.ROUNDED, 100F)
                            .build())

            Glide.with(context).asBitmap().load(from_admin_image_base_URl + mlist.get(position).img).into(img)
            text.text = mlist.get(position).task

            itemView.setOnClickListener {
                listner!!.onItemHomeClickListner(list, position)
            }
        }
    }

    interface OnHomeRecyclerViewItemClickListner {
        fun onItemHomeClickListner(list: ArrayList<HomeModal>, position: Int)
    }


}