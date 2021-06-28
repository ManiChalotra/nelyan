package com.nelyanlive

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {
    @SuppressLint("NewApi")
    fun gotoFragment(mContext: Context, fragment: Fragment?, frame_container: Int, isBackStack: Boolean) {
        val manager = (mContext as AppCompatActivity).supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.addToBackStack(null)
        if (isBackStack) {
            val oldFragment = mContext.supportFragmentManager.findFragmentById(frame_container)
            transaction.replace(frame_container, fragment!!)
            transaction.addToBackStack(null)
            transaction.hide(oldFragment!!)
        } else {
            transaction.replace(frame_container, fragment!!)
        }
        transaction.commit()
    }


    fun getFormattedDate(time: String?): String? {
        var time = time
        val orignalformat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        var date: Date? = null
        try {
            date = orignalformat.parse(time)
            time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return time
    }

}