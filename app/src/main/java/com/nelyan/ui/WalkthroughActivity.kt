package com.nelyan.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.meherr.mehar.db.DataStoragePreference
import com.nelyan.R
import com.nelyan.adapter.ImageSliderCustomAdapter
import com.nelyan.utils.OpenActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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
    var tv: TextView? = null
    var tv_walk: TextView? = null
    var tv_walkdesc: TextView? = null
    var login: LinearLayout? = null
    var signup: LinearLayout? = null

    override fun onResume() {
        super.onResume()
        checkCredentails()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walkthrough)
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
        list!!.add(R.drawable.walkthrough_3)
        list!!.add(R.drawable.walkthrough_5)
        list!!.add(R.drawable.walkthrough_4)
        text = ArrayList()
        text!!.add(R.string.walkone)
        text!!.add(R.string.walktwo)
        text!!.add(R.string.walkthree)
        text!!.add(R.string.walkfive)
        text!!.add(R.string.walkfour)
        listtext = ArrayList()
        listtext!!.add(R.string.walkonedesc)
        listtext!!.add(R.string.walktwodesc)
        listtext!!.add(R.string.walkthreedesc)
        listtext!!.add(R.string.walkfivedesc)
        listtext!!.add(R.string.walkfourdesc)


        mPager!!.setAdapter(ImageSliderCustomAdapter(this, list!!, text!!, listtext!!))
        indicator!!.setViewPager(mPager)
        mPager!!.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })
        login!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@WalkthroughActivity, SignupActivity::class.java)
            startActivity(i)
        })
        signup!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@WalkthroughActivity, LoginActivity::class.java)
            startActivity(i)
        })
    }

    private  fun checkCredentails(){

        launch (Dispatchers.Main.immediate){
           val email =  dataStoragePreference.emitStoredValue(preferencesKey<String>("emailLogin"))?.first()
           val authkey  =  dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))?.first()
            if(!email.isNullOrEmpty() || !authkey.isNullOrEmpty()){
                OpenActivity(HomeActivity::class.java)
            }
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}