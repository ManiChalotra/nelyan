package com.nelyan_live.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.ViewUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyan_live.R
import com.nelyan_live.modals.MyadsEvent
import com.nelyan_live.modals.myads.MyAdsData
import com.nelyan_live.ui.ActivityFormActivity
import com.nelyan_live.utils.image_url_local

class MyAddAdapter(var context: Context, internal var myadsEventlist: ArrayList<MyAdsData>) :
        RecyclerView.Adapter<MyAddAdapter.RecyclerViewHolder>() {
    var inflater: LayoutInflater
    var dialog: Dialog? = null
    var dialog1: Dialog? = null

    inner class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvEdit = itemView.findViewById(R.id.tvEdit) as TextView
        var ll_1 = itemView.findViewById(R.id.ll_1) as LinearLayout
        var tvAdName = itemView.findViewById(R.id.tv_ad_name) as TextView
        var tvLocation = itemView.findViewById(R.id.tv_location) as TextView
        var tvActivityname = itemView.findViewById(R.id.tv_activityname) as TextView
        var tvDescription = itemView.findViewById(R.id.tv_description) as TextView
        var tvMsg = itemView.findViewById(R.id.tv_msg) as TextView
        var tvDelete = itemView.findViewById(R.id.tvDelete) as TextView
        var iv_dot = itemView.findViewById(R.id.iv_dot) as ImageView
        var imageAds = itemView.findViewById(R.id.image_ads) as ImageView
       // var iv_cncl = itemView.findViewById(R.id.tvDelete) as ImageView

        fun bind(myadsList: MyAdsData) {

            if (myadsList.categoryId == 1){
                tvActivityname.setText(myadsList.nameOfShop)
                tvLocation.setText(myadsList.address)
                tvDescription.setText(myadsList.description)
                tvAdName.setText(myadsList.activityname)
                tvMsg.setText(myadsList.adMsg)
                if (myadsList.activityimages.size > 0)
                Glide.with(context).load(image_url_local + myadsList.activityimages.get(0).images).error(R.mipmap.no_image_placeholder).into(imageAds)
                else
                    imageAds.setImageResource(R.mipmap.no_image_placeholder)
            } else if (myadsList.categoryId == 2){
                tvActivityname.setText(myadsList.addInfo)
                tvLocation.setText(myadsList.address)
                tvDescription.setText(context.getString(R.string.description1)+" "+myadsList.description)
                tvAdName.setText(myadsList.name)
                tvMsg.setText(context.getString(R.string.available_place) +" "+ myadsList.availablePlace)

                if (myadsList.nurseryimages.size > 0)
                    Glide.with(context).load(image_url_local + myadsList.nurseryimages.get(0).images).error(R.mipmap.no_image_placeholder).into(imageAds)
                else
                    imageAds.setImageResource(R.mipmap.no_image_placeholder)
            }
     else if (myadsList.categoryId == 3){
                tvAdName.setText(myadsList.name)
               // tvActivityname.setText(myadsList.addInfo)
                tvLocation.setText(myadsList.address)
                tvDescription.setText(context.getString(R.string.description1)+" "+myadsList.description)
                tvMsg.setText(context.getString(R.string.available_place) +" "+ myadsList.availablePlace)

                if (myadsList.nurseryasistantimages.size > 0)
                    Glide.with(context).load(image_url_local + myadsList.nurseryasistantimages.get(0).images).error(R.mipmap.no_image_placeholder).into(imageAds)
                else
                    imageAds.setImageResource(R.mipmap.no_image_placeholder)
            }
        }




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val v = inflater.inflate(R.layout.list_my_add, parent, false)
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        holder.bind(myadsEventlist[position])

        holder.iv_dot.setOnClickListener {
            if (holder.ll_1.visibility == View.VISIBLE) {
// Its visible
                holder.ll_1.visibility = View.GONE
            } else {
// Either gone or invisible
                holder.ll_1.visibility = View.VISIBLE
            }
        }
        holder.tvEdit.setOnClickListener { //  holder.ll_1.setVisibility(View.GONE);
            context.startActivity(Intent(context, ActivityFormActivity::class.java))
        }
        holder.tvDelete.setOnClickListener { //  holder.ll_1.setVisibility(View.GONE);
            dailogDelete()
        }
    }

    override fun getItemCount(): Int {
        return myadsEventlist.size
    }

    fun dailogDelete() {
        dialog1 = Dialog(context)
        dialog1!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1!!.setContentView(R.layout.alert_chat_delete)
        dialog1!!.setCancelable(true)
        val rl_1: RelativeLayout
        rl_1 = dialog1!!.findViewById(R.id.rl_1)
        val tvMessage = dialog1!!.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = "Are you sure want to\ndelete my add?"
        rl_1.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            dialog1!!.dismiss()
        }
        dialog1!!.show()
    }

    fun dailogDot() {
        var iv_dot: ImageView
        dialog = Dialog(context)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_dot)
        dialog!!.setCancelable(true)
        val window = dialog!!.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.RIGHT or Gravity.TOP
        wlp.x = -10 //x position
        wlp.y = 0
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp
        var tvEdit = dialog!!.findViewById<TextView>(R.id.tvEdit)
        val tvDelet = dialog!!.findViewById<TextView>(R.id.tvDelete)
        tvEdit = dialog!!.findViewById(R.id.tvEdit)
        tvEdit.setOnClickListener {
            context.startActivity(Intent(context, ActivityFormActivity::class.java))
            dialog!!.dismiss()
        }
        tvDelet.setOnClickListener {
            dailogDelete()
            dialog!!.dismiss()
        }
        dialog!!.show()
    }

    init {
        inflater = LayoutInflater.from(context)
    }
}