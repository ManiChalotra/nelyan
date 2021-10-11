package com.nelyanlive.utils

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import com.nelyanlive.R
import com.nelyanlive.ui.HomeActivity
import com.yanzhenjie.album.mvp.BaseActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException


/**
 * Created by AIM on 10/18/2018.
 */
abstract class CheckPlayStoreVersion : BaseActivity() {

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
                        openUpdateDialog()
                    } else {
                        Handler().postDelayed({

                            /*if (getPrefrence(GlobalVariables.CONSTANTS.ISLOGIN, false)) {


                                    if (getPrefrence(GlobalVariables.CONSTANTS.USER_TYPE, "").equals("1")) {
                                    activitySwitcher(HomeActivity::class.java, true)
                                } else  if (getPrefrence(GlobalVariables.CONSTANTS.USER_TYPE, "").equals("2")){
                                    activitySwitcher(HomeMenderActivity::class.java, true)
                                }

                            } else {
                                activitySwitcher(LoginActivity::class.java, true)
                            }*/

                        }, 3000)
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
            } else {
                Log.e("datacheck","three")

                Handler().postDelayed({


                   /* if (getPrefrence(GlobalVariables.CONSTANTS.ISLOGIN, false)) {

                         if (getPrefrence(GlobalVariables.CONSTANTS.USER_TYPE, "").equals("1")) {
                            activitySwitcher(HomeActivity::class.java, true)
                        } else  if (getPrefrence(GlobalVariables.CONSTANTS.USER_TYPE, "").equals("2")){
                            activitySwitcher(HomeMenderActivity::class.java, true)
                        }

                    } else {
                        OpenActivity(HomeActivity::class.java)
                        activitySwitcher(LoginActivity::class.java, true)
                    }*/


                }, 3000)


            }
        }
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