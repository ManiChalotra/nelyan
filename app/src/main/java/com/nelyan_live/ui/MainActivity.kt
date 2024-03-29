package com.nelyan_live.ui

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
import androidx.datastore.preferences.core.preferencesKey
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.nelyan_live.db.DataStoragePreference
import com.nelyan_live.R
import com.nelyan_live.utils.OpenActivity
import com.nelyan_live.utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    var mContext: Context? = null
    var ivLogo: ImageView? = null
    var timer: Timer? = null


    private lateinit var dataStoragePreference: DataStoragePreference
    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        printKeyHash(this)

        try {
            //fcm tokencommit today
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(
                            "gg",
                            "Fetching FCM registration token failed",
                            task.exception
                    )
                    return@OnCompleteListener
                }
                // Get new FCM registration token
                val token = task.result
                launch(Dispatchers.Main.immediate) {
                    dataStoragePreference = DataStoragePreference(this@MainActivity)
                    if (dataStoragePreference != null) {
                        dataStoragePreference?.save(token, preferencesKey("fcmToken"))
                    } else {
                        toast("preference is null")
                    }
                }
                Log.d("newFcmToken", "---------" + token)
            })
        } catch (e: Exception) {
            e.printStackTrace()

        }



        mContext = this
        ivLogo = findViewById(R.id.ivLogo)
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                launch (Dispatchers.Main.immediate){
                    val email =  dataStoragePreference.emitStoredValue(preferencesKey<String>("emailLogin"))?.first()
                    val authkey  =  dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))?.first()
                    if(!email.isNullOrEmpty() && !authkey.isNullOrEmpty()){
                        OpenActivity(HomeActivity::class.java)
                        finishAffinity()
                    }else {
                        val i = Intent(this@MainActivity, WalkthroughActivity::class.java)
                        startActivity(i)
                        finishAffinity()

                    }
                }


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