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
import com.nelyanlive.modals.homeactivitylist.HomeAcitivityResponseData
import com.nelyanlive.utils.image_base_URl

class ActivityListAdapter(activity: FragmentActivity, internal var homeAcitivitiesList: ArrayList<HomeAcitivityResponseData>,
                          internal var OnCLICK: OnHomeActivitiesRecyclerViewItemClickListner) : RecyclerView.Adapter<ActivityListAdapter.RecyclerViewHolder>() {


    var context: Context? = null
    var image: ImageView? = null


    inner class RecyclerViewHolder(view: View?) : RecyclerView.ViewHolder(view!!)
    {
        var tvAddress = itemView.findViewById(R.id.tv_address) as TextView
        var tvActivityname = itemView.findViewById(R.id.tv_activityname) as TextView
        var tvNameOfShop = itemView.findViewById(R.id.tv_nameOfShop) as TextView
        var tvDescription = itemView.findViewById(R.id.tv_description) as TextView
        var tvMsg = itemView.findViewById(R.id.tv_available_place) as TextView
        var tv_minmaxage = itemView.findViewById(R.id.tv_minmaxage) as TextView
        var ivActivityImage = itemView.findViewById(R.id.iv_activity_image) as ImageView
        var ivFavourite = itemView.findViewById(R.id.iv_favouritessss) as ImageView
        var rl_1 = itemView.findViewById(R.id.rl_1) as RelativeLayout
        var llActivityDetails = itemView.findViewById(R.id.ll_activity_details) as LinearLayout

        fun bindMethod(homeAcitivityList: HomeAcitivityResponseData)
        {
            tvActivityname.text = homeAcitivityList.activityname
            tvDescription.text = homeAcitivityList.description
            tvAddress.text = homeAcitivityList.address
            tv_minmaxage.text = "  Min ${homeAcitivityList.minAge} ${context!!.getString(R.string.years_masculinesmall)} -  Max ${homeAcitivityList.maxAge} ${context!!.getString(R.string.years_masculinesmall)}"
            /*
                                   [{"id":5,"name":"Sorties et parcs",
                                   "created":1606905733,"updated":1628196399},
                                   {"id":14,"name":"Activité sportive","created"
                                   :1625873590,"updated":1631190238},
                                   {"id":26,"name":"Divers","created":1631190223,
                                   "updated":1631191269},{"id":27,"name":
                                   "Loisir et ateliers","created":1631191322,
                                   "updated":1631191322},{"id":28,"name":
                                   "Cours et langues","created":1631192176,
                                   "updated":1631192176},{"id":29,
                                   "name":"Activité culturelle et artistique",
                                   "created":1631192435,"updated":1633186017},
                                   {"id":30,"name":"Association",
                                   "created":1633071219,"updated":1633071219}]
                                    */
            when (homeAcitivityList.typeofActivityId) {
                5 -> {
                    tvMsg.text = context!!.getString(R.string.type)+" "+"Sorties et parcs"

                }
                14 -> {
                    tvMsg.text = context!!.getString(R.string.type)+" "+"Activité sportive"

                }
                26 -> {
                    tvMsg.text = context!!.getString(R.string.type)+" "+"Divers"

                }
                27 -> {
                    tvMsg.text = context!!.getString(R.string.type)+" "+"Loisir et ateliers"

                }
                28 -> {
                    tvMsg.text = context!!.getString(R.string.type)+" "+"Cours et langues"

                }
                29 -> {
                    tvMsg.text = context!!.getString(R.string.type)+" "+"Activité culturelle et artistique"

                }
                30 -> {
                    tvMsg.text = context!!.getString(R.string.type)+" "+"Association"

                }
            }

            tvNameOfShop.text = homeAcitivityList.nameOfShop

            if (homeAcitivityList.activityimages !=null && homeAcitivityList.activityimages.size !=0){
                Glide.with(context!!).load(image_base_URl+ homeAcitivityList.activityimages[0].images).
                error(R.mipmap.no_image_placeholder).into(ivActivityImage)
            }

            if (homeAcitivityList.isFav ==1){
                ivFavourite.setImageResource(R.drawable.heart)

            }else {
                ivFavourite.setImageResource(R.drawable.heart_purple)

            }


            ivFavourite.setOnClickListener{
                OnCLICK.onAddFavoriteClick(homeAcitivityList.id.toString(), ivFavourite)
            }

            llActivityDetails.setOnClickListener{
                OnCLICK.onHomeActivitiesItemClickListner(homeAcitivityList.id.toString(), homeAcitivityList.categoryId.toString(),
                    homeAcitivityList.latitude,
                    homeAcitivityList.longitude
                )

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        context = parent.context
        return RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.list_activitylist, parent, false))


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