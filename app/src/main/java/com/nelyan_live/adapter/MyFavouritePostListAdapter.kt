package com.nelyan_live.adapter

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
import com.nelyan_live.R
import com.nelyan_live.modals.myFavourite.FavouritePost
import com.nelyan_live.utils.image_base_URl

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

        fun bindMethod(favouritePostList: FavouritePost) {

            if (favouritePostList.postType ==1){
                tvActivityname.text = favouritePostList.PostData.activityname
                tvDescription.text = favouritePostList.PostData.description
                tvAddress.text = favouritePostList.PostData.address

                if (favouritePostList.PostData.typeofActivityId == 5){
                    tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.sports)

                } else if (favouritePostList.PostData.typeofActivityId == 9){
                    tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.dance)

                } else if (favouritePostList.PostData.typeofActivityId == 10){
                    tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.drawing)

                }else if (favouritePostList.PostData.typeofActivityId == 11){
                    tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.zumba)

                }else if (favouritePostList.PostData.typeofActivityId == 13){
                    tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.tutor_mother_subject)

                }
                tvMsg.text = context!!.getString(R.string.name_of_shop1)+" "+favouritePostList.PostData.nameOfShop

                if (favouritePostList.PostData.activityimages !=null && favouritePostList.PostData.activityimages.size !=0){
                    Glide.with(context!!).load(image_base_URl+favouritePostList.PostData.activityimages.get(0).images).
                    error(R.mipmap.no_image_placeholder).into(ivActivityImage)
                }

            }
            else if (favouritePostList.postType ==2){
                tvActivityname.text = favouritePostList.PostData.name
                tvDescription.text = favouritePostList.PostData.description
                tvAddress.text = favouritePostList.PostData.city

                if (favouritePostList.PostData.ChildcareType ==1){
                    tvNameOfShop.text = context!!.getString(R.string.type1)+" "+ context!!.getString(R.string.nursery)

                } else if (favouritePostList.PostData.ChildcareType ==2){
                    tvNameOfShop.text = context!!.getString(R.string.type1)+" "+ context!!.getString(R.string.maternal_assistant)

                } else if (favouritePostList.PostData.ChildcareType ==3){
                    tvNameOfShop.text = context!!.getString(R.string.type1)+" "+ context!!.getString(R.string.babySitter)

                }
                tvMsg.text = context!!.getString(R.string.available_place)+" "+favouritePostList.PostData.availableplace.toString()


            if (favouritePostList.PostData.ChildCareImages !=null && favouritePostList.PostData.ChildCareImages.size !=0){
                Glide.with(context!!).load(image_base_URl+favouritePostList.PostData.ChildCareImages.get(0).image).
                error(R.mipmap.no_image_placeholder).into(ivActivityImage)
            }



            }
            else if (favouritePostList.postType ==3){
                tvActivityname.text = favouritePostList.PostData.nameOfShop
                tvDescription.text = favouritePostList.PostData.description
                tvAddress.text = favouritePostList.PostData.address

                if (favouritePostList.PostData.typeofTraderId == 15){
                    tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.doctor)
                }
                else if (favouritePostList.PostData.typeofTraderId == 16){
                    tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.food_court)

                }
                else if (favouritePostList.PostData.typeofTraderId == 17){
                    tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.plant_nursury)

                }
                else if (favouritePostList.PostData.typeofTraderId == 18){
                    tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.tutor_mathematics)

                }
                else if (favouritePostList.PostData.typeofTraderId == 19){
                    tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.gym_trainer)

                }
                else if (favouritePostList.PostData.typeofTraderId == 20){
                    tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.yoga_trainer)

                }
                else if (favouritePostList.PostData.typeofTraderId == 21){
                    tvNameOfShop.text = context!!.getString(R.string.type1)+" "+context!!.getString(R.string.gadget_repair)

                }

                tvMsg.text = context!!.getString(R.string.email_address1)+" "+favouritePostList.PostData.email


                if (favouritePostList.PostData.isFav ==1){
                    ivFavourite!!.setImageResource(R.drawable.heart)

                }else {
                    ivFavourite!!.setImageResource(R.drawable.heart_purple)

                }

                if (favouritePostList.PostData.tradersimages !=null && favouritePostList.PostData.tradersimages.size !=0){
                    Glide.with(context!!).load(image_base_URl+favouritePostList.PostData.tradersimages.get(0).images).
                    error(R.mipmap.no_image_placeholder).into(ivActivityImage)
                }


            }



            ivFavourite!!.setOnClickListener{
                OnCLICK.onAddFavouritePostClick(adapterPosition, favouritePostList.PostData.id.toString(), favouritePostList.postType.toString(), ivFavourite)

                /*context?.OpenActivity(ActivityDetailsActivity::class.java){
                    putString("activityId", homeAcitivityList.id.toString())
                    putString("categoryId", homeAcitivityList.categoryId.toString())
                }
*/
            }

/*
            rl_1!!.setOnClickListener{
                context?.OpenActivity(ActivityDetailsActivity::class.java)

            }
*/
            llActivityDetails!!.setOnClickListener{
                OnCLICK.onFavouritePostItemClickListner(favouritePostList.PostData.id.toString(), favouritePostList.postType.toString() )

                /*context?.OpenActivity(ActivityDetailsActivity::class.java){
                    putString("activityId", homeAcitivityList.id.toString())
                    putString("categoryId", homeAcitivityList.categoryId.toString())
                }
*/
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        context = parent.context
        return RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.list_activitylist, parent, false))

              /*  val v = LayoutInflater.from(context).inflate(R.layout.list_activitylist, parent, false)
        return RecyclerViewHolder(v)*/
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