package com.nelyanlive.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.modals.myFavourite.FavouritePost
import com.nelyanlive.utils.image_base_URl

class MyFavouritePostListAdapter(activity: FragmentActivity, internal var favouritePostList: ArrayList<FavouritePost>,
                                 internal var OnCLICK: OnFavPostRecyclerViewItemClickListner) : RecyclerView.Adapter<MyFavouritePostListAdapter.RecyclerViewHolder>() {
    var context: Context? = null
    var image: ImageView? = null


    inner class RecyclerViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {

        var tvAddress = itemView.findViewById(R.id.tv_address) as TextView
        var tvActivityname = itemView.findViewById(R.id.tv_activityname) as TextView
        var tvNameOfShop = itemView.findViewById(R.id.tv_nameOfShop) as TextView
        var tvDescription = itemView.findViewById(R.id.tv_description) as TextView
        var tvMsg = itemView.findViewById(R.id.tv_available_place) as TextView
        var ivActivityImage = itemView.findViewById(R.id.iv_activity_image) as ImageView
        var ivFavourite = itemView.findViewById(R.id.iv_favouritessss) as ImageView
        var rl_1 = itemView.findViewById(R.id.rl_1) as RelativeLayout
        var llActivityDetails = itemView.findViewById(R.id.ll_activity_details) as LinearLayout

        fun bindMethod(favouritePost: FavouritePost) {
            ivFavourite.setImageResource(R.drawable.heart)

            if (favouritePost.postType ==1){
                tvActivityname.text = favouritePost.PostData.activityname
                tvDescription.text = favouritePost.PostData.description
                tvAddress.text = favouritePost.PostData.address

                when (favouritePost.PostData.typeofActivityId) {
                    5 -> {
                        tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.sports)

                    }
                    9 -> {
                        tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.dance)

                    }
                    10 -> {
                        tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.drawing)

                    }
                    11 -> {
                        tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.zumba)

                    }
                    13 -> {
                        tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.tutor_mother_subject)

                    }
                }
                tvMsg.text = context!!.getString(R.string.name_of_shop1)+" "+favouritePost.PostData.nameOfShop

                if (favouritePost.PostData.activityimages !=null && favouritePost.PostData.activityimages.size !=0){
                    Glide.with(context!!).load(image_base_URl+favouritePost.PostData.activityimages.get(0).images).
                    error(R.mipmap.no_image_placeholder).into(ivActivityImage)
                }

            }
            else if (favouritePost.postType ==2){
                tvActivityname.text = favouritePost.PostData.name
                tvDescription.text = favouritePost.PostData.description
                tvAddress.text = favouritePost.PostData.city

                when (favouritePost.PostData.ChildcareType) {
                    1 -> {
                        tvNameOfShop.text = context!!.getString(R.string.type1)+" "+ context!!.getString(R.string.nursery)

                    }
                    2 -> {
                        tvNameOfShop.text = context!!.getString(R.string.type1)+" "+ context!!.getString(R.string.maternal_assistant)

                    }
                    3 -> {
                        tvNameOfShop.text = context!!.getString(R.string.type1)+" "+ context!!.getString(R.string.babySitter)

                    }
                }
                tvMsg.text = context!!.getString(R.string.available_place)+" "+favouritePost.PostData.availableplace.toString()


            if (favouritePost.PostData.ChildCareImages !=null && favouritePost.PostData.ChildCareImages.size !=0){
                Glide.with(context!!).load(image_base_URl+favouritePost.PostData.ChildCareImages.get(0).image).
                error(R.mipmap.no_image_placeholder).into(ivActivityImage)
            }



            }
            else if (favouritePost.postType ==3){
                tvActivityname.text = favouritePost.PostData.nameOfShop
                tvDescription.text = favouritePost.PostData.description
                tvAddress.text = favouritePost.PostData.address

                when (favouritePost.PostData.typeofTraderId) {
                    15 -> {
                        tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.doctor)
                    }
                    16 -> {
                        tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.food_court)

                    }
                    17 -> {
                        tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.plant_nursury)

                    }
                    18 -> {
                        tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.tutor_mathematics)

                    }
                    19 -> {
                        tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.gym_trainer)

                    }
                    20 -> {
                        tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.yoga_trainer)

                    }
                    21 -> {
                        tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.gadget_repair)

                    }
                }

                tvMsg.text = context!!.getString(R.string.email_address1)+" "+favouritePost.PostData.email




                if (favouritePost.PostData.tradersimages !=null && favouritePost.PostData.tradersimages.size !=0){
                    Glide.with(context!!).load(image_base_URl+favouritePost.PostData.tradersimages.get(0).images).
                    error(R.mipmap.no_image_placeholder).into(ivActivityImage)
                }


            }



            ivFavourite.setOnClickListener{
                OnCLICK.onAddFavouritePostClick(adapterPosition, favouritePost.PostData.id.toString(), favouritePost.postType.toString(), ivFavourite)
                favouritePostList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)

            }


            llActivityDetails.setOnClickListener{
                OnCLICK.onFavouritePostItemClickListner(favouritePost.PostData.id.toString(), favouritePost.postType.toString() )

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        context = parent.context
        return RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.list_activitylist, parent, false))


    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bindMethod(favouritePostList[position])
    }
    override fun getItemCount(): Int {
        return favouritePostList.size
    }

    interface OnFavPostRecyclerViewItemClickListner {
        fun onFavouritePostItemClickListner(activityId: String, categoryId: String)
        fun onAddFavouritePostClick(position: Int, postId: String?, postType: String?, ivFavourite: ImageView)
    }



}