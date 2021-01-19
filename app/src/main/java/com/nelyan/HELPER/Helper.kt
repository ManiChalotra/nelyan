package com.nauatili.Helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.widget.Toast


object Helper {

    @JvmStatic
    fun showToast(context: Context, msg: Int) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    annotation class JvmStatic

    fun logoutApiCall(activity: Activity) {

    }
}