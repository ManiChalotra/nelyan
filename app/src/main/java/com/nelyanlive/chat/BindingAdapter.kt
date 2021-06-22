package com.nelyanlive.chat

import android.content.res.ColorStateList
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nelyanlive.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapter {


    @BindingAdapter(value = ["setRecyclerAdapter"], requireAll = false)
    @JvmStatic
    fun setRecyclerAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        recyclerView.adapter = adapter
    }

    @BindingAdapter(value = ["setBorderColor"], requireAll = false)
    @JvmStatic
    fun setBorderColor(ivImage: CircleImageView, str:String) {
        if(str=="0")
        {
            ivImage.borderColor = ContextCompat.getColor(ivImage.context,R.color.white)
        }
        else
        {
            ivImage.borderColor = ContextCompat.getColor(ivImage.context,R.color.red)

        }
    }

    @BindingAdapter(value = ["setTime"], requireAll = false)
    @JvmStatic
    fun setTime(tvText: TextView, str: String) {

        if(str.isNotEmpty()) {
            tvText.text = SimpleDateFormat("h:mm a d MMM").format(Date(str.toLong() * 1000))
        }
        else
        {
            tvText.text = "now"
        }
    }

    @BindingAdapter(value = ["setTickStatus"], requireAll = false)
    @JvmStatic
    fun setTickStatus(iv: ImageView, data: ChatListResponse) {

        if(data.unreadcount !="0") {
            iv.visibility = View.GONE
        }
        else
        {
            if(data.readStatus =="0")
            {
                iv.setImageDrawable(ContextCompat.getDrawable(iv.context,R.drawable.blue_double_check))
                iv.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(iv.context,R.color.grey))

            }
            else
            {
                iv.setImageDrawable(ContextCompat.getDrawable(iv.context,R.drawable.blue_double_check))
            }
        }

    }

    @BindingAdapter(value = ["setMessageTime"], requireAll = false)
    @JvmStatic
    fun setMessageTime(tvText: TextView, str: String) {

        if(str.isNotEmpty()) {
            val data1 = Date(str.toLong() * 1000)
            val data2 = Date()
            data2.hours = 0
            data2.minutes = 0

            val diff: Long = data2.time - data1.time

            if(diff<0)
            {
                val hr = SimpleDateFormat("h").format(Date(str.toLong() * 1000))
                val am_pm = SimpleDateFormat("a").format(Date(str.toLong() * 1000))
                val min = SimpleDateFormat("mm").format(Date(str.toLong() * 1000))

                Log.e("dsfgasdfasdfadsf", "====$hr==$min==$am_pm=====")

                tvText.text = "${if(hr.toInt()<13){hr}else{(hr.toInt()-12).toString()}}:${min} ${am_pm}"

            }
            else {
                if (diff > 0 && diff < (60 * 60 * 1000)) {
                    tvText.text = "Yesterday"

                } else {
                    tvText.text = SimpleDateFormat("dd/MM/yyyy").format(Date(str.toLong() * 1000))
                }
            }

        }
        else
        {
            tvText.text = "now"
        }
    }

    @BindingAdapter(value = ["setMessageListTime"], requireAll = false)
    @JvmStatic
    fun setMessageListTime(tvText: TextView, str: String) {

        if(str.isNotEmpty()) {
            val data1 = Date(str.toLong() * 1000)
            val data2 = Date()
            data2.hours = 0
            data2.minutes = 0

            val diff: Long = data2.time - data1.time

            if(diff<0)
            {
                tvText.text = "Today"

            }
            else {
                if (diff > 0 && diff < (60 * 60 * 1000)) {
                    tvText.text = "Yesterday"

                } else {
                    tvText.text = SimpleDateFormat("dd/MM/yyyy").format(Date(str.toLong() * 1000))
                }
            }

        }

    }


    @BindingAdapter(value = ["setChatVisibility","setUserID"], requireAll = false)
    @JvmStatic
    fun setChatVisibility(ll: View, str: String, userId: String) {



        Log.e("dsfgasdfasdfadsf", "====$33333=====")
        Log.e("dsfgasdfasdfadsf", "====$33333=====${str}=$33333=====${str}")

        if(str==userId)
        {
            ll.visibility = View.VISIBLE
        }
        else
        {ll.visibility = View.GONE}
    }


    @BindingAdapter(value = ["setLeftChatVisibility","setUserID"], requireAll = false)
    @JvmStatic
    fun setLeftChatVisibility(ll: View, str: String, userId: String) {



        Log.e("dsfgasdfasdfadsf", "====$33333=====")
        Log.e("dsfgasdfasdfadsf", "====$33333=====${str}=$33333=====${str}")

        if(str!=userId)
        {
            ll.visibility = View.VISIBLE
        }
        else
        {ll.visibility = View.GONE}
    }

    @BindingAdapter(value = ["setDrawableTint"], requireAll = false)
    @JvmStatic
    fun setDrawableTint(tv: TextView, str: ChatData) {



        Log.e("dsfgasdfasdfadsf", "====$33333=====")
        Log.e("dsfgasdfasdfadsf", "====$33333=====${str}=$33333=====${str}")


        if(str.isFlag=="") {
            if(str.readStatus=="1")
            {
                tv.compoundDrawableTintList = ColorStateList.valueOf(ContextCompat.getColor(tv.context,R.color.blue))
            }
            else
            {
                tv.compoundDrawableTintList = ColorStateList.valueOf(ContextCompat.getColor(tv.context,R.color.lightgrey))
            }
        } else {
            tv.compoundDrawableTintList = ColorStateList.valueOf(ContextCompat.getColor(tv.context,R.color.grey))
        }




    }





    @BindingAdapter(value = ["setChatImage"], requireAll = false)
    @JvmStatic
    fun setChatImage(
        ivImage: ImageView,
        str: String?

    ) {
        if(!str.isNullOrEmpty()) {


            Picasso.get().load("http://3.13.214.27:1052/uploads/users/$str").resize(100, 100)
                    .placeholder(ContextCompat.getDrawable(
                    ivImage.context,
                    R.drawable.placeholder
            )!!).into(ivImage)

        }
        else
        {
            ivImage.setImageDrawable(ContextCompat.getDrawable(ivImage.context,R.drawable.placeholder))
        }
    }

    @BindingAdapter(value = ["setChatMessages"], requireAll = false)
    @JvmStatic
    fun setChatMessages(
        ivImage: ImageView,
        str: String?

    ) {
        if(!str.isNullOrEmpty()) {


            Picasso.get().load("http://3.13.214.27:1052/uploads/users/$str")
                    .placeholder(ContextCompat.getDrawable(
                    ivImage.context,
                    R.drawable.placeholder
            )!!).into(ivImage)

        }
        else
        {
            ivImage.setImageDrawable(ContextCompat.getDrawable(ivImage.context,R.drawable.placeholder))
        }
    }

}