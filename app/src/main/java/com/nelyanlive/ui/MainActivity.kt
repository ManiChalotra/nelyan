package com.nelyanlive.ui

import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.datastore.preferences.core.preferencesKey
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.nelyanlive.HELPER.LanguageHelper
import com.nelyanlive.R
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.utils.CheckPlayStoreVersion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainActivity : CheckPlayStoreVersion() {
    var mContext: Context? = null
    var ivLogo: ImageView? = null

    private lateinit var dataStoragePreference: DataStoragePreference
    private var job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("Language", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString("language", "fr")!!

        Log.d("sharedPreferences---", "--------language------------$language")
        LanguageHelper.setLocale(this, language)
        printKeyHash(this)

        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(
                        "gg",
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    return@OnCompleteListener
                }
                val token = task.result
                launch(Dispatchers.Main.immediate) {
                    dataStoragePreference = DataStoragePreference(this@MainActivity)
                    dataStoragePreference.save(token, preferencesKey("fcmToken"))
                    Log.d("newFcmToken", "---------$token")

                }
            })
        } catch (e: Exception) {
            e.printStackTrace()

        }

        mContext = this
        ivLogo = findViewById(R.id.ivLogo)
        GetVersionCode().execute()

        /* timer = Timer()
         timer!!.schedule(object : TimerTask() {
             override fun run() {
                 launch (Dispatchers.Main.immediate){
                     dataStoragePreference = DataStoragePreference(this@MainActivity)
                     val email = dataStoragePreference.emitStoredValue(preferencesKey<String>("emailLogin")).first()
                     val authkey  = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()


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
         }, 3000)*/
    }


    fun printKeyHash(context: Activity): String? {
        val packageInfo: PackageInfo
        var key: String? = null
        try {
            val packageName = context.applicationContext.packageName
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
        Log.d("key_fb", "--------------------$key")
        return key
    }


}