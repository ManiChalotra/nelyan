package com.nelyan.utils


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.nelyan.R
import com.nelyan.ui.LoginActivity
import java.util.regex.Matcher
import java.util.regex.Pattern

val base_URL = "http://3.13.214.27:1052/api/"
val security_key = "nelyan@2021"
val UNAUTHORIZED = "Invalid Authorization Key"
val device_Type = "1"
val image_base_URl = "http://3.13.214.27:1052/uploads/users/"
val FOR_FACEBOOK_TYPE = "1"
val FOR_GOOGLE_TYPE = "2"
var OAUTH_GOOGLE_CLIENT_ID = "679915298408-s8dalhfhf8d3tecve1vdjtlo1uttm3u1.apps.googleusercontent.com"

/*error message key*/
val MESSAGE = "msg"

// initalize the viewModel instance
fun isUserNameValid(username: String): Boolean {
    //val p:Pattern=  Pattern.compile("^[ a-zA-Z0-9._-]{3,}\$")
  val p: Pattern = Pattern.compile("^[a-zA-Z\\s]*\$")
    //val p: Pattern = Pattern.compile("^[A-Za-z]+$")
    val m: Matcher = p.matcher(username)
    return m.matches()
}



fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}



fun isPasswordValid(password: String): Boolean {
    return Pattern.compile(
        "^(?=[^0-9]*[0-9])(?=(?:[^A-Za-z]*[A-Za-z]){2})(?=[^@#\$%^&+=]*[@#\$%^&+=])\\S{8,20}\$"
    ).matcher(password).matches()
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

fun iscityValid(c: String): Boolean {
    return c.matches("([a - zA - Z] + |[a - zA - Z] + \\s[a - zA - Z] + )".toRegex())
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
    var intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}




fun wrapTabIndicatorToTitle(tabLayout: TabLayout, externalMargin: Int, internalMargin: Int) {
    val tabStrip = tabLayout.getChildAt(0)
    if (tabStrip is ViewGroup) {
        val childCount = tabStrip.childCount
        for (i in 0 until childCount) {
            val tabView = tabStrip.getChildAt(i)
            //set minimum width to 0 for instead for small texts, indicator is not wrapped as expected
            tabView.minimumWidth = 0
            // set padding to 0 for wrapping indicator as title
            tabView.setPadding(0, tabView.paddingTop, 0, tabView.paddingBottom)
            // setting custom margin between tabs
            if (tabView.layoutParams is ViewGroup.MarginLayoutParams) {
                val layoutParams = tabView.layoutParams as ViewGroup.MarginLayoutParams
                if (i == 0) {
                    // left
                    settingMargin(layoutParams, externalMargin, internalMargin)
                } else if (i == childCount - 1) {
                    // right
                    settingMargin(layoutParams, internalMargin, externalMargin)
                } else {
                    // internal
                    settingMargin(layoutParams, internalMargin, internalMargin)
                }
            }
        }
        tabLayout.requestLayout()
    }
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


fun tintSwitchButton(sw: SwitchCompat, resolvedColor: Int) {
    val states = arrayOf(
        intArrayOf(-android.R.attr.state_pressed),
        intArrayOf(android.R.attr.state_pressed)
    )

    DrawableCompat.setTintList(
        sw?.trackDrawable, ColorStateList(
            states,
            intArrayOf(resolvedColor, resolvedColor)
        )
    )

    DrawableCompat.setTintList(
        sw?.thumbDrawable, ColorStateList(
            states,
            intArrayOf(Color.WHITE, Color.WHITE)
        )
    )
}


fun failureMethod(
    mContext: Context,
    error: String?,
    progressBar: com.tuyenmonkey.mkloader.MKLoader?
) {
    progressBar?.hideProgressBar()
    if (error == UNAUTHORIZED) {
        // here delete your database or Local Preferences ..
        // AllSharedPref.clear(mContext)
        Toast.makeText(
            mContext,
            "You are already logged in other device",
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(mContext, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        mContext.startActivity(intent)
    } else {
        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show()
    }
}


fun checkIfHasNetwork(activity: Activity): Boolean {

    val cm =
        (activity)!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = cm!!.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected

}



fun Activity.myCustomToast(message: String){
    val inflater = layoutInflater
    val layout: View = inflater.inflate(R.layout.custom_toast_layout, findViewById(R.id.toast_layout_root) )
   /*
    val image: ImageView = layout.findViewById<View>(R.id.image) as ImageView
    image.setImageResource(R.drawable.logo)
    */
    val text = layout.findViewById<View>(R.id.text) as TextView
    text.text = message
    val toast = Toast(applicationContext)
    toast.setGravity( Gravity.BOTTOM, 0, 100)
    toast.duration = Toast.LENGTH_LONG
    toast.setView(layout)
    toast.show()

}














