package com.nelyan_live.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.meherr.mehar.db.DataStoragePreference
import com.nelyan_live.R
import com.nelyan_live.fragments.*
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_drawer.tvLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener
        , View.OnClickListener, CoroutineScope, CommunicationListner {

    val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java)
    }

    val dataStoragePreference by lazy {
        DataStoragePreference(this)
    }

    var navigationbar: BottomNavigationView? = null
    var a = 1
    var doubleBackToExitPressedOnce = false
    var navigation_view: NavigationView? = null
    var mDrawerLayout: DrawerLayout? = null
    var mDrawerToggle: ActionBarDrawerToggle? = null
    var mainContainer: RelativeLayout? = null
    var iv_back: ImageView? = null
    var ivToolBarImage: ImageView? = null
    var iv_bell: ImageView? = null
    var tvHome: TextView? = null
    var tvLogin: TextView? = null
    var tvTitleToolbar: TextView? = null
    var tvAdd: TextView? = null
    var tvFavorite: TextView? = null
    var tvProfile: TextView? = null
    var tvContact: TextView? = null
    var tvNoti: TextView? = null
    var tvSettings: TextView? = null
    var dialog: Dialog? = null
    var v: View? = null
    var job = Job()
    private var authorization = ""


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onResume() {
        super.onResume()
        authorization = intent?.extras?.getString("authorization").toString()
        Log.d("homeAuthKey", "----------" + authorization)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initalize()
        checkMvvmresponse()

        navigationbar = findViewById(R.id.navigationbar)
        tvTitleToolbar = findViewById(R.id.tvTitleToolbar)
        ivToolBarImage = findViewById(R.id.ivToolBarImage)
        iv_bell = findViewById(R.id.iv_bell)
        mDrawerLayout = findViewById(R.id.mDrawerLayout)
        loadFragment(HomeFragment())
        //loadFragment(new DrawerNewHomeFragment());
        val bottomNavigationBar = findViewById<BottomNavigationView>(R.id.navigationbar)
        bottomNavigationBar.setOnNavigationItemSelectedListener(this)
        setDrawerClicks()
        setToolBarClicks()

       /* val bottomNavigationView = findViewById<View>(R.id.navigationbar) as BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    myCustomToast("1")
                }
                R.id.msg -> {
                    myCustomToast("2")
                }
                R.id.publier -> {
                    myCustomToast("3")
                }
                R.id.chat -> {
                    myCustomToast("4")
                }
                R.id.event -> {
                    myCustomToast("5")
                }

            }
            true
        }

*/


        try {
            try {
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "map") {

                        OpenActivity(ActivityFragmentActivity::class.java)

                       /* Log.e("vghgv", "hhhhhhh")
                        var fragment: Fragment? = null
                        fragment = ActivityFragment()
                        // Fragment fragment = new ActivityFragment();
                        val args = Bundle()
                        args.putString("unamea", "Homeactivity")
                        fragment!!.arguments = args
                        val fragmentManager = this.supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.frame_container, fragment)
                        fragmentTransaction.commit()*/

                    }
                }
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "sectrofrag") {
                        var fragment: Fragment? = null
                        fragment = SectorizationDetailsFragment()
                        loadFragment(fragment)
                    }
                }
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "activity") {
                        Log.e("vghgv", "hhhhhhh")
                        var fragment: Fragment? = null
                        fragment = ChildCareFragment()
                        val args = Bundle()
                        args.putString("child", "childcare")
                        fragment!!.arguments = args
                        val fragmentManager = this.supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.frame_container, fragment)
                        fragmentTransaction.commit()
                    }
                }
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "nurFrag") {
                        var fragment: Fragment? = null
                        fragment = NurseFragment()
                        val args = Bundle()
                        args.putString("activity", "Nurseriefragment")
                        fragment!!.arguments = args
                        val fragmentManager = this.supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.frame_container, fragment)
                        fragmentTransaction.commit()
                        //   bottomNavigationView.setSelectedItemId(R.id.home);
                    }
                }
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "tarfrag") {
                        var fragment: Fragment? = null
                        fragment = TraderPublishFragment()
                        val args = Bundle()
                        args.putString("activity", "traderfragment")
                        fragment!!.arguments = args
                        val fragmentManager = this.supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.frame_container, fragment)
                        fragmentTransaction.commit()
                        //   bottomNavigationView.setSelectedItemId(R.id.home);
                    }
                }
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "mater") {
                        var fragment: Fragment? = null
                        fragment = MaternalFragmentFragment()
                        loadFragment(fragment)
                        //   bottomNavigationView.setSelectedItemId(R.id.home);
                    }
                }
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "babyfrag") {
                        var fragment: Fragment? = null
                        fragment = BabySitterFragment()
                        loadFragment(fragment)
                        //   bottomNavigationView.setSelectedItemId(R.id.home);
                    }
                }
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "nur") {
                        OpenActivity(NurserieActivityy::class.java)
                       /* var fragment: Fragment? = null
                        fragment = NurserieFragment()
                        loadFragment(fragment)*/
                        //   bottomNavigationView.setSelectedItemId(R.id.home);
                    }
                }
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "seclist") {
                        var fragment: Fragment? = null
                        fragment = SectorizationListFragment()
                        loadFragment(fragment)

                        //   bottomNavigationView.setSelectedItemId(R.id.home);
                    }
                }
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "acti") {
                        OpenActivity(ActivityDetailsActivity::class.java)
                       /* var fragment: Fragment? = null
                        fragment = ActivityDetailsFragment()
                        //    loadFragment( fragment);
                        val args = Bundle()
                        args.putString("activity", "Home")
                        fragment!!.arguments = args
                        val fragmentManager = this.supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.frame_container, fragment)
                        fragmentTransaction.commit()*/
                    }
                }
            } catch (e: Exception) {
                var fragment: Fragment? = null
                fragment = HomeFragment()
                loadFragment(fragment)
            }
        } catch (e: Exception) {
        }
    }

    private fun initalize() {
        tvLog.setOnClickListener(this)

    }

    private fun setToolBarClicks() {
        iv_back = findViewById(R.id.iv_back)
        navigation_view = findViewById(R.id.navigation_view)
        mainContainer = findViewById(R.id.mainContainer)
        iv_back!!.setImageResource(R.drawable.ic_menu)
        iv_back!!.setVisibility(View.VISIBLE)
        iv_back!!.setOnClickListener(View.OnClickListener {
            mDrawerLayout!!.openDrawer(navigation_view!!)
            mDrawerToggle = object : ActionBarDrawerToggle(this@HomeActivity, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    super.onDrawerSlide(drawerView, slideOffset)
                    //  closeKeyboard();
                    val min = 0.9f
                    val max = 1.0f
                    val scaleFactor = max - (max - min) * slideOffset
                    mainContainer!!.setScaleX(scaleFactor)
                    mainContainer!!.setScaleY(scaleFactor)
                }
            }
            mDrawerLayout!!.addDrawerListener(mDrawerToggle!!)
        })
    }

    private fun setDrawerClicks() {
        tvHome = findViewById(R.id.tvHome)
        tvHome!!.setOnClickListener(View.OnClickListener {
            mDrawerLayout!!.closeDrawers()
            val i = Intent(this@HomeActivity, HomeActivity::class.java)
            startActivity(i)
            finishAffinity()
        })
        tvLogin = findViewById(R.id.tvLogin)
        tvLogin!!.setOnClickListener(View.OnClickListener {
            mDrawerLayout!!.closeDrawers()
            val i = Intent(this@HomeActivity, com.nelyan_live.ui.LoginActivity::class.java)
            startActivity(i)
        })
        tvAdd = findViewById(R.id.tvAdd)
        tvAdd!!.setOnClickListener(View.OnClickListener {
            mDrawerLayout!!.closeDrawers()
            val i = Intent(this@HomeActivity, com.nelyan_live.ui.AddActivity::class.java)
            startActivity(i)


            //  AppUtils.gotoFragment(mContext, new MyAddFragment(), R.id.frame_container, false);
        })
        tvFavorite = findViewById(R.id.tvFavorite)
        tvFavorite!!.setOnClickListener(View.OnClickListener {
            mDrawerLayout!!.closeDrawers()
            OpenActivity(FavouriteActivity::class.java)//AppUtils.gotoFragment(this, FavoriteFragment(), R.id.frame_container, false)
        })
        tvProfile = findViewById(R.id.tvProfile)
        tvProfile!!.setOnClickListener(View.OnClickListener {
            mDrawerLayout!!.closeDrawers()
            val i = Intent(this@HomeActivity, com.nelyan_live.ui.ProfileActivity::class.java)
            startActivity(i)
        })
        tvContact = findViewById(R.id.tvContact)
        tvContact!!.setOnClickListener(View.OnClickListener {
            mDrawerLayout!!.closeDrawers()

            OpenActivity(ContactUsActivity::class.java) {
                putString("authorization", authorization)
            }

        })
        tvNoti = findViewById(R.id.tvNoti)
        tvNoti!!.setOnClickListener(View.OnClickListener {
            mDrawerLayout!!.closeDrawers()
            val i = Intent(this@HomeActivity, com.nelyan_live.ui.NotificationActivity::class.java)
            startActivity(i)
        })
        tvSettings = findViewById(R.id.tvSettings)
        tvSettings!!.setOnClickListener(View.OnClickListener {
            mDrawerLayout!!.closeDrawers()

            OpenActivity(SettingsActivity::class.java) {
                putString("authorization", authorization)
            }

        })

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvLog -> {
                mDrawerLayout!!.closeDrawers()
                showLog()

            }

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.home -> {
                //DrawerNewHomeFragment.drawerLayout.openDrawer(GravityCompat.START);
                iv_bell!!.setImageResource(R.drawable.notification_icon)
                tvTitleToolbar!!.visibility = View.GONE
                ivToolBarImage!!.visibility = View.VISIBLE
                iv_bell!!.setOnClickListener {
                    val i = Intent(this@HomeActivity, com.nelyan_live.ui.NotificationActivity::class.java)
                    startActivity(i)
                }
                fragment = HomeFragment()
            }
            R.id.msg -> {
                tvTitleToolbar!!.visibility = View.VISIBLE
                ivToolBarImage!!.visibility = View.GONE
                tvTitleToolbar!!.text = "Message"
                iv_bell!!.setImageResource(R.drawable.notification_icon)
                iv_bell!!.setOnClickListener {
                    val i = Intent(this@HomeActivity, com.nelyan_live.ui.NotificationActivity::class.java)
                    startActivity(i)
                }
                fragment = MessageFragment()
            }
            R.id.publier -> {
                tvTitleToolbar!!.visibility = View.VISIBLE
                ivToolBarImage!!.visibility = View.GONE
                tvTitleToolbar!!.text = "Publier"
                iv_bell!!.setImageResource(R.drawable.notification_icon)
                iv_bell!!.setOnClickListener {
                    val i = Intent(this@HomeActivity, com.nelyan_live.ui.NotificationActivity::class.java)
                    startActivity(i)
                }
                val bundle = Bundle()
                val frag = PublisherFrag()
                bundle.putString("authorization", authorization)
                frag.arguments = bundle
                fragment = frag
            }
            R.id.chat -> {
                tvTitleToolbar!!.visibility = View.VISIBLE
                ivToolBarImage!!.visibility = View.GONE
                tvTitleToolbar!!.text = "Maisons-Laffite"
                iv_bell!!.setImageResource(R.drawable.tab_on)
                iv_bell!!.setOnClickListener {
                    //iv_bell.get
                }
                fragment = com.nelyan_live.ui.ChatFrag()
            }
            R.id.event -> {
                tvTitleToolbar!!.visibility = View.VISIBLE
                ivToolBarImage!!.visibility = View.GONE
                tvTitleToolbar!!.text = "Upcoming Event\nMaisons-Laffitle"
                iv_bell!!.setImageResource(R.drawable.location_circle)
                iv_bell!!.setOnClickListener {
                    val intent = Intent(this@HomeActivity, Activity2Activity::class.java)
                    intent.putExtra("event", "activity")
                    startActivity(intent)
                }
                fragment = EventFragment()
            }
        }
        return loadFragment(fragment)
    }


    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit()
            return true
        } else {
            myCustomToast("Fragment is null")
        }
        return false
    }

    private fun showLog() {
        dialog = Dialog(this@HomeActivity)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_logout)
        dialog!!.setCancelable(true)
        val rlYes: RelativeLayout
        val rlNo: RelativeLayout
        rlYes = dialog!!.findViewById(R.id.rlYes)
        rlYes.setOnClickListener {
            hitLogoutApi()
        }
        rlNo = dialog!!.findViewById(R.id.rlNo)
        rlNo.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()

    }

    override fun onBackPressed() {

        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_container)
        if (currentFragment is HomeFragment) {
            if (doubleBackToExitPressedOnce) {
                finishAffinity()
                return
            }else{
                myCustomToast("press once again to exit from the App.")
            }
            doubleBackToExitPressedOnce = true
            Handler(Looper.myLooper()!!).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else {
            if(currentFragment !is HomeFragment){
                loadFragment(HomeFragment())
                iv_bell!!.setImageResource(R.drawable.notification_icon)
                tvTitleToolbar!!.visibility = View.GONE
                ivToolBarImage!!.visibility = View.VISIBLE
                iv_bell!!.setOnClickListener {
                    val i = Intent(this@HomeActivity, com.nelyan_live.ui.NotificationActivity::class.java)
                    startActivity(i)
                }
                supportFragmentManager.popBackStack()
            }else{



            }
        }

       /* val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_container)
        if (currentFragment is HomeFragment) {
            if (doubleBackToExitPressedOnce) {
                finishAffinity()
                return
            }else{
                myCustomToast("press once again to exit from the App.")
            }
            doubleBackToExitPressedOnce = true
            Handler(Looper.myLooper()!!).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
        else if (currentFragment is  ActivityFragment) {
               super.onBackPressed()
           } else if (currentFragment is ActivityDetailsFragment) {
               super.onBackPressed()
           } else if (currentFragment is NurserieFragment) {
               super.onBackPressed()
           } else if (currentFragment is  TraderPublishFragment) {
               super.onBackPressed()
           } else if (currentFragment is ChildCareFragment) {
               super.onBackPressed()
           } else if (currentFragment is  SectorizationDetailsFragment) {
               super.onBackPressed()
           } else if (currentFragment is  SectorizationListFragment) {
               super.onBackPressed()
           } else {
               super.onBackPressed()
           }
*/
    }


    private fun hitLogoutApi() {
        appViewModel.sendLogoutData(security_key, authorization)
        homeProgressBar?.showProgressBar()
    }

    private fun checkMvvmresponse() {
        appViewModel.observeLogoutResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    homeProgressBar?.hideProgressBar()
                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    Log.d("logoutResponse", "---------" + Gson().toJson(response.body()))
                    val message = jsonObject.get("msg").toString()
                    myCustomToast(message)

                    launch(Dispatchers.Main.immediate) {
                        val i = Intent(this@HomeActivity, com.nelyan_live.ui.LoginActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(i)
                        dialog!!.dismiss()
                        dataStoragePreference.deleteDataBase()
                    }

                }
            } else {
                ErrorBodyResponse(response, this, homeProgressBar)
            }
        })

        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
            homeProgressBar?.hideProgressBar()
        })
    }

    override fun onFargmentActive(value: Int) {
        when(value){
            1->{
                pinkColour.invoke(tv_homeTab)
                blackColour.invoke(tv_messageTab)
                blackColour.invoke(tv_publisherTab)
                blackColour.invoke(tv_chatTab)
                blackColour.invoke(tv_eventTab)

            }
            2->{
                blackColour.invoke(tv_homeTab)
                pinkColour.invoke(tv_messageTab)
                blackColour.invoke(tv_publisherTab)
                blackColour.invoke(tv_chatTab)
                blackColour.invoke(tv_eventTab)

            }
            3->{
                blackColour.invoke(tv_homeTab)
                blackColour.invoke(tv_messageTab)
                pinkColour.invoke(tv_publisherTab)
                blackColour.invoke(tv_chatTab)
                blackColour.invoke(tv_eventTab)

            }
            4->{
                blackColour.invoke(tv_homeTab)
                blackColour.invoke(tv_messageTab)
                blackColour.invoke(tv_publisherTab)
                pinkColour.invoke(tv_chatTab)
                blackColour.invoke(tv_eventTab)
            }
            5->{
                blackColour.invoke(tv_homeTab)
                blackColour.invoke(tv_messageTab)
                blackColour.invoke(tv_publisherTab)
                blackColour.invoke(tv_chatTab)
                pinkColour.invoke(tv_eventTab)
            }
        }
    }

    val pinkColour = {textView:TextView->
        textView.setTextColor(Color.parseColor("#9c238d"))
    }
    val blackColour = {textView:TextView->
        textView.setTextColor(Color.parseColor("#000000"))
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}