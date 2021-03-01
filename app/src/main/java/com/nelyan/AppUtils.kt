package com.nelyan

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
            transaction.hide(Objects.requireNonNull(oldFragment)!!)
        } else {
            transaction.replace(frame_container, fragment!!)
        }
        transaction.commit()
    }
}