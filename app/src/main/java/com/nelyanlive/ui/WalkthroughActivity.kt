package com.nelyanlive.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
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

    private  fun checkCredentails(){
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}