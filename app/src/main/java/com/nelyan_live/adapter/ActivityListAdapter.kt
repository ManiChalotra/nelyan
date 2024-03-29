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
import com.nelyan_live.modals.homeactivitylist.HomeAcitivityResponseData
import com.nelyan_live.utils.image_base_URl

class ActivityListAdapter(activity: FragmentActivity, internal var homeAcitivitiesList: ArrayList<HomeAcitivityResponseData>,
                          internal var OnCLICK: OnHomeActivitiesRecyclerViewItemClickListner) : RecyclerView.Adapter<ActivityListAdapter.RecyclerViewHolder>() {
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

        fun bindMethod(homeAcitivityList: HomeAcitivityResponseData) {

            tvActivityname.text = homeAcitivityList.activityname
            tvDescription.text = homeAcitivityList.description
            tvAddress.text = homeAcitivityList.address

            if (homeAcitivityList.typeofActivityId == 5){
                tvMsg.text = context!!.getString(R.string.type)+" "+context!!.getString(R.string.sports)

            } else if (homeAcitivityList.typeofActivityId == 9){
                tvMsg.text = context!!.getString(R.string.type)+" "+context!!.getString(R.string.dance)

            } else if (homeAcitivityList.typeofActivityId == 10){
                tvMsg.text = context!!.getString(R.string.type)+" "+context!!.getString(R.string.drawing)

            }else if (homeAcitivityList.typeofActivityId == 11){
                tvMsg.text = context!!.getString(R.string.type)+" "+context!!.getString(R.string.zumba)

            }else if (homeAcitivityList.typeofActivityId == 13){
                tvMsg.text = context!!.getString(R.string.type)+" "+context!!.getString(R.string.tutor_mother_subject)

            }

            tvNameOfShop.text = homeAcitivityList.nameOfShop

            if (homeAcitivityList.activityimages !=null && homeAcitivityList.activityimages.size !=0){
                Glide.with(context!!).load(image_base_URl+homeAcitivityList.activityimages.get(0).images).
                error(R.mipmap.no_image_placeholder).into(ivActivityImage)
            }

            if (homeAcitivityList.isFav ==1){
                ivFavourite!!.setImageResource(R.drawable.heart)

            }else {
                ivFavourite!!.setImageResource(R.drawable.heart_purple)

            }


            ivFavourite!!.setOnClickListener{
                OnCLICK.onAddFavoriteClick(homeAcitivityList.id.toString(), ivFavourite)
            }

/*
            rl_1!!.setOnClickListener{
                context?.OpenActivity(ActivityDetailsActivity::class.java)

            }
*/
            llActivityDetails!!.setOnClickListener{
                OnCLICK.onHomeActivitiesItemClickListner(homeAcitivityList.id.toString(), homeAcitivityList.categoryId.toString(),
                        homeAcitivityList.latitude.toString(), homeAcitivityList.longitude.toString() )

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
        holder.bindMethod(homeAcitivitiesList[position])
    }
    override fun getItemCount(): Int {
        return homeAcitivitiesList.size
    }

    interface OnHomeActivitiesRecyclerViewItemClickListner {
        fun onAddFavoriteClick(eventID: String, ivFavourite: ImageView)
        fun onHomeActivitiesItemClickListner(activityId: String, categoryId: String, postLatitude: String, postLongitude: String)
    }



}