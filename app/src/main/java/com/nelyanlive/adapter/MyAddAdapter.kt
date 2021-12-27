package com.nelyanlive.adapter

import android.app.Dialog
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.modals.myAd.ActivityimageMyAds
import com.nelyanlive.modals.myAd.AgeGroupMyAds
import com.nelyanlive.modals.myAd.EventMyAds
import com.nelyanlive.modals.myAd.GetActivitypostMyAds
import com.nelyanlive.ui.ActivityDetailsActivity
import com.nelyanlive.utils.OpenActivity
import com.nelyanlive.utils.image_base_URl
import kotlinx.android.synthetic.main.alert_chat_delete.*


class MyAddAdapter(
    var context: Context, internal var myadsActivitylist: ArrayList<GetActivitypostMyAds>,
    internal var activitiesDeleteEditListener: OnActivitiesDeleteEditClickListner
) :
    RecyclerView.Adapter<MyAddAdapter.RecyclerViewHolder>() {
    var inflater: LayoutInflater = LayoutInflater.from(context)
    var dialog: Dialog? = null
    private var popupWindow: PopupWindow? = null
    var dialog1: Dialog? = null
    var isMenuOpend = "0"

    inner class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvAddress = itemView.findViewById(R.id.tv_address) as TextView
        var tvActivityname = itemView.findViewById(R.id.tv_activityname) as TextView
        var tvNameOfShop = itemView.findViewById(R.id.tv_nameOfShop) as TextView
        var tvDescription = itemView.findViewById(R.id.tv_description) as TextView
        var tvMsg = itemView.findViewById(R.id.tv_available_place) as TextView
        var ivActivityImage = itemView.findViewById(R.id.iv_activity_image) as ImageView
        var ivDot = itemView.findViewById(R.id.iv_favouritessss) as ImageView
        var rl_1 = itemView.findViewById(R.id.rl_1) as RelativeLayout
        var llActivityDetails = itemView.findViewById(R.id.ll_pass) as LinearLayout

        fun bind(myadsList: GetActivitypostMyAds) {
            ivDot.setImageResource(R.drawable.option_dot)

            ivDot.setOnClickListener {
                Log.e("Dot Click", "Clikkceckk")
                callPopup(ivDot,
                    adapterPosition, myadsList.id,
                    myadsList.activityname,
                    myadsList.typeofActivityId, myadsList.nameOfShop,
                    myadsList.description,
                    myadsList.website,
                    myadsList.address,
                    myadsList.minAge,
                    myadsList.maxAge,
                    myadsList.latitude,
                    myadsList.longitude,
                    myadsList.city,
                    myadsList.country_code,
                    myadsList.phone,
                    myadsList.ageGroups,
                    myadsList.events,
                    myadsList.activityimages
                )

            }

            tvActivityname.text = myadsList.activityname
            tvDescription.text = myadsList.description
            tvAddress.text = myadsList.address
            tvNameOfShop.text = myadsList.nameOfShop

            if (myadsList.activityimages.size != null && myadsList.activityimages.size != 0)
                Glide.with(context).load(image_base_URl + myadsList.activityimages[0].images)
                    .error(R.mipmap.no_image_placeholder).into(ivActivityImage)
            else
                ivActivityImage.setImageResource(R.mipmap.no_image_placeholder)

            llActivityDetails.setOnClickListener {

                context.OpenActivity(ActivityDetailsActivity::class.java) {

                    Log.e(
                        "fafsdfa",
                        "===ActivityDetailsActivity==${myadsList.latitude}=====${myadsList.longitude}====="
                    )

                    putString("activityId", myadsList.id.toString())
                    putString("categoryId", myadsList.categoryId.toString())
                    putString("lati", myadsList.latitude)
                    putString("longi", myadsList.longitude)

                }
            }
        }
    }

    private fun callPopup(
        ivDot: ImageView,
        adapterPosition: Int,
        adId: Int,
        activityname: String,
        typeofActivityId: Int,
        nameOfShop: String,
        description: String,
        website: String,
        address: String,
        minage: String,
        maxage: String,
        latti: String,
        longi: String,
        city: String,
        countryCode: String,
        phone: String,
        ageGroups: ArrayList<AgeGroupMyAds>,
        events: ArrayList<EventMyAds>,
        activityimages: ArrayList<ActivityimageMyAds>
    ) {
        val layoutInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = layoutInflater.inflate(R.layout.alert_dot, null)
        val editt = popupView.findViewById<View>(R.id.tvEdit) as TextView
        val deletee = popupView.findViewById<View>(R.id.tvDelete) as TextView
        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow!!.isTouchable = true
        popupWindow!!.isOutsideTouchable = true

        popupWindow!!.showAsDropDown(ivDot, -250, 0, Gravity.END)

        editt.setOnClickListener {
            activitiesDeleteEditListener.onEditActivitiesAdClick(
                adapterPosition,
                adId.toString(),
                typeofActivityId.toString(),
                nameOfShop,
                activityname,
                description,
                website,
                countryCode,
                phone,
                address,
                minage,
                maxage,
                latti,
                longi,
                city,
                ageGroups,
                activityimages,
                events
            )
            popupWindow!!.dismiss()
        }

        deletee.setOnClickListener {
            dailogDelete(adapterPosition, adId.toString())
            popupWindow!!.dismiss()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val v = inflater.inflate(R.layout.list_activitylist, parent, false)
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        holder.bind(myadsActivitylist[position])
    }

    override fun getItemCount(): Int {
        return myadsActivitylist.size
    }

    fun dailogDelete(adapterPosition: Int, adId: String) {
        dialog1 = Dialog(context)
        dialog1!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1!!.setContentView(R.layout.alert_chat_delete)
        dialog1!!.setCancelable(true)
        val tvMessage = dialog1!!.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = context.getString(R.string.are_you_sure_want_to_delete_this)
        dialog1!!.tvYes.setOnClickListener {
            dialog1!!.dismiss()
            myadsActivitylist.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
            activitiesDeleteEditListener.onActivitiesDeleteAdClick(adapterPosition, adId)
        }
        dialog1!!.tvNo.setOnClickListener {
            dialog1!!.dismiss()
        }
        dialog1!!.show()
    }

    interface OnActivitiesDeleteEditClickListner {
        fun onActivitiesDeleteAdClick(position: Int, adID: String?)
        fun onEditActivitiesAdClick(
            position: Int,
            adID: String?,
            activityTypeId: String,
            nameofShop: String,
            nameofActivity: String,
            description: String,
            website: String,
            countryCode: String,
            phoneNumber: String,
            address: String,
            minage: String,
            maxage: String,
            latti: String,
            longi: String,
            city: String,
            ageGroupListMyAds: ArrayList<AgeGroupMyAds>,
            ActivityimagesList: ArrayList<ActivityimageMyAds>,
            eventMyAdsList: ArrayList<EventMyAds>
        )
    }

}