package com.nelyanlive.utils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.datastore.preferences.core.preferencesKey
import com.nelyanlive.R
import com.nelyanlive.current_location.SharedUtil
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.ui.HomeActivity
import com.nelyanlive.ui.WalkthroughActivity
import com.yanzhenjie.album.mvp.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.util.*
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException
import kotlin.coroutines.CoroutineContext


/**
 * Created by AIM on 10/18/2018.
 */
abstract class CheckPlayStoreVersion : BaseActivity(), CoroutineScope {

    inner class GetVersionCode : AsyncTask<Void, String, String>() {

        override fun doInBackground(vararg voids: Void): String? {

            val currentVersion_PatternSeq =
                "<div[^>]*?>Current\\sVersion</div><span[^>]*?>(.*?)><div[^>]*?>(.*?)><span[^>]*?>(.*?)</span>"
            val appVersion_PatternSeq = "htlgb\">([^<]*)</s"
            var playStoreAppVersion: String? = null

            var inReader: BufferedReader? = null
            var uc: URLConnection? = null
            val urlData = StringBuilder()


            val url: URL
            try {
                url = URL("https://play.google.com/store/apps/details?id=$packageName")
                uc = url.openConnection()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (uc == null) {
                return null
            }
            uc.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6"
            )
            try {
                inReader = BufferedReader(InputStreamReader(uc.getInputStream()))
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (null != inReader) {
                try {
                    do {
                        val line = inReader.readLine()
                        urlData.append(line)
                    } while (line != null)

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            // Get the current version pattern sequence
            val versionString = getAppVersion(currentVersion_PatternSeq, urlData.toString())
            if (null == versionString) {
                return null
            } else {
                // get version from "htlgb">X.X.X</span>
                playStoreAppVersion = getAppVersion(appVersion_PatternSeq, versionString)
            }

            return playStoreAppVersion

        }


        override fun onPostExecute(onlineVersion: String?) {
            super.onPostExecute(onlineVersion)
            Log.e("datacheck","one")
            dataStoragePreference = DataStoragePreference(this@CheckPlayStoreVersion)
            if (onlineVersion != null && onlineVersion.isNotEmpty()) {
                Log.e("datacheck","two")
                try {
                    val current_version = java.lang.Float.valueOf(
                        packageManager.getPackageInfo(
                            packageName,
                            0
                        ).versionName.replace(".", "")
                    )
                    val online_version = java.lang.Float.valueOf(onlineVersion.replace(".", ""))!!
                    val diff = java.lang.Float.compare(current_version, online_version)

                        if (diff < 0) {

                            if (SharedUtil.getPref(applicationContext)==true)
                                openUpdateDialog()
                            else
                                callNextActivity()
                        } else {
                            callNextActivity()
                        }

                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
            } else {
                Log.e("datacheck","three")
                callNextActivity()
            }
        }
    }
    var timer: Timer? = null
    private lateinit var dataStoragePreference: DataStoragePreference
    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private fun callNextActivity() {
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                launch (Dispatchers.Main.immediate){
                    dataStoragePreference = DataStoragePreference(this@CheckPlayStoreVersion)
                    val email = dataStoragePreference.emitStoredValue(preferencesKey<String>("emailLogin")).first()
                    val authkey  = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()


                    if(!email.isNullOrEmpty() && !authkey.isNullOrEmpty()){
                        OpenActivity(HomeActivity::class.java)
                        finishAffinity()
                    }else {
                        val i = Intent(this@CheckPlayStoreVersion, WalkthroughActivity::class.java)
                        startActivity(i)
                        finishAffinity()

                    }
                }
            }
        }, 3000)
    }

    private fun getAppVersion(patternString: String, inputString: String): String? {
        try {
            //Create a pattern
            val pattern = Pattern.compile(patternString) ?: return null
//Match the pattern string in provided string
            val matcher = pattern.matcher(inputString)
            if (null != matcher && matcher.find()) {
                return matcher.group(1)
            }
        } catch (ex: PatternSyntaxException) {
            ex.printStackTrace()
        }

        return null
    }

    private fun openUpdateDialog() {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.setContentView(R.layout.update_app_dialog)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)

        val update = dialog.findViewById<View>(R.id.update) as TextView

        update.setOnClickListener {
          SharedUtil.savePref(this,true)
            try {
                startActivity(
                    Intent(
                        "android.intent.action.VIEW",
                        Uri.parse("market://details?id=$packageName")
                    )
                )
            } catch (e: Exception) {
                startActivity(
                    Intent(
                        "android.intent.action.VIEW",
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
        }

        dialog.show()
    }
}