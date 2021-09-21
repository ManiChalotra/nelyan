package com.nelyanlive.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.nelyanlive.HELPER.LanguageHelper
import com.nelyanlive.R
import com.nelyanlive.adapter.ImageSliderCustomAdapter
import com.nelyanlive.db.DataStoragePreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import me.relex.circleindicator.CircleIndicator
import java.util.*
import kotlin.coroutines.CoroutineContext

class WalkthroughActivity : AppCompatActivity(), CoroutineScope {

    val dataStoragePreference by lazy { DataStoragePreference(this@WalkthroughActivity) }

    private  var job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    var mContext: Context? = null
    var indicator: CircleIndicator? = null
    var mPager: ViewPager? = null
    var list: ArrayList<Int>? = null
    var listtext: ArrayList<Int>? = null
    var text: ArrayList<Int>? = null
    var login: LinearLayout? = null
    var signup: LinearLayout? = null


    var MY_REQUEST_CODE = 112233


    lateinit var appUpdateManager: AppUpdateManager

    override fun onResume() {
        super.onResume()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walkthrough)
        appUpdateManager = AppUpdateManagerFactory.create(this)
        checkUpdate()
        mContext = this
        setView()
    }

    fun setView() {
        mPager = findViewById(R.id.view_pager)
        login = findViewById(R.id.ll_login)
        signup = findViewById(R.id.ll_signup)
        indicator = findViewById(R.id.indicator_product)
        list = ArrayList()
        list!!.add(R.drawable.walkthrough_1)
        list!!.add(R.drawable.walkthrough_2)
        list!!.add(R.drawable.walkthrough_5)
        list!!.add(R.drawable.walkthrough_4)
        text = ArrayList()
        text!!.add(R.string.walkone)
        text!!.add(R.string.walktwo)
        text!!.add(R.string.walkfive)
        text!!.add(R.string.walkfour)
        listtext = ArrayList()
        listtext!!.add(R.string.walkonedesc)
        listtext!!.add(R.string.walktwodesc)
        listtext!!.add(R.string.walkfivedesc)
        listtext!!.add(R.string.walkfourdesc)


        mPager!!.adapter = ImageSliderCustomAdapter(this, list!!, text!!, listtext!!)
        indicator!!.setViewPager(mPager)
        mPager!!.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })
        login!!.setOnClickListener {

            val i = Intent(this@WalkthroughActivity, SignupActivity::class.java)
            startActivity(i)
        }
        signup!!.setOnClickListener {

            val i = Intent(this@WalkthroughActivity, LoginActivity::class.java)
            startActivity(i)
        }
    }

    private fun checkUpdate() {
        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        // Checks that the platform will allow the specified type of update.
        Log.d("TAG", "Checking for updates")
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->

            Log.d("TAG", "Checking for updates===11===${appUpdateInfo.updateAvailability()==UpdateAvailability.UPDATE_AVAILABLE}")
            Log.d("TAG", "Checking for updates===22===${appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)}")

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                // Request the update.
                Log.d("TAG", "Update available")
                appUpdateManager.registerListener(listener)
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    MY_REQUEST_CODE)
            }
            else {
                Log.d("TAG", "No Update available")
            } } }

    private val listener: InstallStateUpdatedListener = InstallStateUpdatedListener { installState ->
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            Log.d("TAG", "An update has been downloaded")
            Toast.makeText(this,getString(R.string.update_complete), Toast.LENGTH_SHORT).show()
            // appUpdateManager.unregisterListener(listener)
            appUpdateManager.completeUpdate()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}