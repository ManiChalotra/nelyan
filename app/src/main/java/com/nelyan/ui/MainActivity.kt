package com.nelyan.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.nelyan.R
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class MainActivity : AppCompatActivity() {
    var mContext: Context? = null
    var ivLogo: ImageView? = null
    var timer: Timer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        printKeyHash(this)
        mContext = this
        ivLogo = findViewById(R.id.ivLogo)
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                val i = Intent(this@MainActivity, WalkthroughActivity::class.java)
                startActivity(i)
                finish()
            }
        }, 3000)
    }



    fun printKeyHash(context: Activity): String? {
        val packageInfo: PackageInfo
        var key: String? = null
        try { //getting application package name, as defined in manifest
            val packageName = context.applicationContext.packageName
//Retriving package info
            packageInfo = context.packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES
            )
            Log.e("Package gjfghjfghName=", context.applicationContext.packageName)
            for (signature in packageInfo.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                key = String(Base64.encode(md.digest(), 0))
                Log.e("key_hash", key)
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Log.e("Name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("No such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
        }
        Log.d("key_fb","--------------------"+key)
        return key
    }



}