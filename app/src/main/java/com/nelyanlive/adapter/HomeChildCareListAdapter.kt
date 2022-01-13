package com.nelyanlive.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.modals.homechildcare.HomeChildCareeData
import com.nelyanlive.ui.HomeChildCareDetailsActivity
import com.nelyanlive.utils.OpenActivity
import com.nelyanlive.utils.image_base_URl

class HomeChildCareListAdapter(var context: Context, internal var childCareDatalist: ArrayList<HomeChildCareeData>,
                               internal var listner: OnChatListItemClickListner) : RecyclerView.Adapter<HomeChildCareListAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.list_activitylist, parent, false), listner) }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bindMethod(childCareDatalist[position])
    }
    override fun getItemCount(): Int {
        return childCareDatalist.size
    }

    inner class RecyclerViewHolder(view: View, var listner:OnChatListItemClickListner): RecyclerView.ViewHolder(view){

        var tvAddress = itemView.findViewById(R.id.tv_address) as TextView
        var tvActivityname = itemView.findViewById(R.id.tv_activityname) as TextView
        var tvNameOfShop = itemView.findViewById(R.id.tv_nameOfShop) as TextView
        var tvDescription = itemView.findViewById(R.id.tv_description) as TextView
        var tvMsg = itemView.findViewById(R.id.tv_available_place) as TextView
        var ivActivityImage = itemView.findViewById(R.id.iv_activity_image) as ImageView
        var ageicon = itemView.findViewById(R.id.ageicon) as ImageView
        var ivFavourite = itemView.findViewById(R.id.iv_favouritessss) as ImageView
        var rl_1 = itemView.findViewById(R.id.rl_1) as RelativeLayout
        var llActivityDetails = itemView.findViewById(R.id.ll_activity_details) as LinearLayout

        fun bindMethod(homeChildCareList: HomeChildCareeData) {

            tvActivityname.text = homeChildCareList.name
            tvDescription.text = homeChildCareList.description
            tvAddress.text = homeChildCareList.city
            ageicon.visibility = View.GONE
            tvMsg.text = context.getString(R.string.available_place)+" "+homeChildCareList.availableplace.toString()

            when (homeChildCareList.ChildcareType.toString()) {
                "1" -> {
                    tvNameOfShop.text = context.getString(R.string.type1)+" "+ context.getString(R.string.nursery)
                }
                "2" -> {
                    tvNameOfShop.text = context.getString(R.string.type1)+" "+context.getString(R.string.maternal_asistant)
                }
                "3" -> {
                    tvNameOfShop.text = context.getString(R.string.type1)+" "+ context.getString(R.string.babySitter)
                }
            }
            if (homeChildCareList.ChildCareImages !=null && homeChildCareList.ChildCareImages.size !=0){
                Glide.with(context).load(image_base_URl + homeChildCareList.ChildCareImages[0].image).
                error(R.mipmap.no_image_placeholder).into(ivActivityImage)
            }

            if (homeChildCareList.isFav ==1){
                ivFavourite.setImageResource(R.drawable.heart)

            }else {
                ivFavourite.setImageResource(R.drawable.heart_purple)

            }

            ivFavourite.setOnClickListener{
                listner.onAddFavoriteClick(adapterPosition, homeChildCareList.id.toString(), ivFavourite)
            }


            llActivityDetails.setOnClickListener{
                context.OpenActivity(HomeChildCareDetailsActivity::class.java){
                    putString("activityId", homeChildCareList.id.toString())
                    putString("categoryId", homeChildCareList.type.toString())
                    putString("latti", homeChildCareList.latitude.toString())
                    putString("longi", homeChildCareList.longitude.toString())
                }

            }
        }

    }

    interface OnChatListItemClickListner{
        fun onItemClickListner(position: Int)
        fun onAddFavoriteClick(position: Int, postId: String?, ivFavourite: ImageView)
    }



}