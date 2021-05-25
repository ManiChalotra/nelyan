package com.nelyan_live.chat

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyan_live.R
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
                tvText.text = SimpleDateFormat("h:mm").format(Date(str.toLong() * 1000))

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





    @BindingAdapter(value = ["setChatImage"], requireAll = false)
    @JvmStatic
    fun setChatImage(
        ivImage: ImageView,
        str: String?

    ) {
        if(!str.isNullOrEmpty()) {

                    Glide.with(ivImage.context).load("http://3.13.214.27:1052/uploads/users/$str").dontTransform()
                            .override(200, 200).placeholder(
                                    ContextCompat.getDrawable(
                                            ivImage.context,
                                            R.drawable.placeholder
                                    )
                            ).into(ivImage)

        }
        else
        {
            ivImage.setImageDrawable(ContextCompat.getDrawable(ivImage.context,R.drawable.placeholder))
        }
    }

}