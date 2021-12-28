package com.nelyanlive.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.nelyanlive.R
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.ui.LoginActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import java.util.regex.Pattern

//val base_URL = "https://app.nelyan.com/api/"
val base_URL = "http://192.168.1.109:1052/api/"
//val base_URL = "http://3.13.214.27:1052/api/"   //old
val security_key = "nelyan@2021"
val UNAUTHORIZED = "Invalid Authorization Key"
val InVALID_AUTH_TOEKN = "Invalid Auth Token"
val Age_Group_Required  = "ageGroup field is required"
val InVALID_Credentials = "Invalid Credentials"
val device_Type = "1"
   //  val from_admin_image_base_URl = "http://3.13.214.27:1052/uploads/users/"  //old
val from_admin_image_base_URl = "https://app.nelyan.com/uploads/users/"
   //val image_base_URl = "http://3.13.214.27:1052"   // old
val image_base_URl = "https://app.nelyan.com"
const val socketBaseUrl = "https://app.nelyan.com"
//const val socketBaseUrl = "http://192.168.1.109:1052"
val FOR_FACEBOOK_TYPE = "2"
val FOR_GOOGLE_TYPE = "1"

val MESSAGE = "msg"

fun isUserNameValid(username: String): Boolean {
    val p: Pattern = Pattern.compile("^[a-zA-Z\\s]*\$")
    val m: Matcher = p.matcher(username)
    return m.matches()
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun ProgressBar.showProgressBar() {
    visibility = View.VISIBLE
}

fun ProgressBar.hideProgressBar() {
    visibility = View.GONE
}

fun com.tuyenmonkey.mkloader.MKLoader.showProgressBar() {
    visibility = View.VISIBLE
}

fun com.tuyenmonkey.mkloader.MKLoader.hideProgressBar() {
    visibility = View.GONE
}

fun isEmailValid(email: String): Boolean {
    return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
    ).matcher(email).matches()
}

fun showSnackBar(activity: Activity, messageToShow: String): Snackbar? {
    try {
        val snackbar =
                Snackbar.make(
                        activity.findViewById(android.R.id.content),
                        messageToShow,
                        Snackbar.LENGTH_SHORT
                )
        val snackbarView = snackbar.view
        val textView = snackbarView.findViewById<TextView>(R.id.snackbar_text)
        textView.maxLines = 3
        snackbar.setAction(activity.getString(R.string.strok)) { snackbar.dismiss() }
        snackbar.setActionTextColor(ContextCompat.getColor(activity, android.R.color.white))
        snackbar.view.setBackgroundColor(activity.resources.getColor(R.color.colorPrimary))
        snackbar.duration = 1500
        snackbar.show()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun <T> Context.OpenActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

private fun settingMargin(layoutParams: ViewGroup.MarginLayoutParams, start: Int, end: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        layoutParams.marginStart = start
        layoutParams.marginEnd = end
        layoutParams.leftMargin = start
        layoutParams.rightMargin = end
    } else {
        layoutParams.leftMargin = start
        layoutParams.rightMargin = end
    }
}

fun failureMethod(
        mContext: Context,
        error: String?,
        progressBar: com.tuyenmonkey.mkloader.MKLoader?
) {
    progressBar?.hideProgressBar()
    when (error) {
        InVALID_AUTH_TOEKN -> {
            Toast.makeText(
                    mContext,
                    "You are already logged in other device",
                    Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(mContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            mContext.startActivity(intent)
            GlobalScope.launch {
                DataStoragePreference(mContext).deleteDataBase()
            }
        }

        UNAUTHORIZED -> {
            Toast.makeText(
                    mContext,
                    "You are already logged in other device",
                    Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(mContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            mContext.startActivity(intent)
        }
        InVALID_Credentials -> {
            Toast.makeText(
                    mContext,
                    "Incorrect email or password",
                    Toast.LENGTH_SHORT
            ).show()
        }
        Age_Group_Required -> {
            Toast.makeText(
                    mContext,
                    "Age Group Required",
                    Toast.LENGTH_SHORT
            ).show()
        }
        else -> {
            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
        }
    }
}

fun checkIfHasNetwork(activity: Activity): Boolean {
    val cm =(activity).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = cm.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun Activity.myCustomToast(message: String) {
    val inflater = layoutInflater
    val layout: View = inflater.inflate(R.layout.custom_toast_layout, findViewById(R.id.toast_layout_root))
    val text = layout.findViewById<View>(R.id.text) as TextView
    text.text = message
    val toast = Toast(applicationContext)
    toast.setGravity(Gravity.BOTTOM, 0, 100)
    toast.duration = Toast.LENGTH_LONG
    toast.view = layout
    toast.show()
}