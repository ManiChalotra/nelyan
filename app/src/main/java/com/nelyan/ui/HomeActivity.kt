package com.nelyan.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.nelyan.AppUtils
import com.nelyan.R
import com.nelyan.fragments.ActivityDetailsFragment
import com.nelyan.fragments.ActivityFragment
import com.nelyan.fragments.BabySitterFragment
import com.nelyan.fragments.ChildCareFragment
import com.nelyan.fragments.DrawerFragment
import com.nelyan.fragments.DrawerNewHomeFragment
import com.nelyan.fragments.EventFragment
import com.nelyan.fragments.FavoriteFragment
import com.nelyan.fragments.HomeFragment
import com.nelyan.fragments.MaternalFragmentFragment
import com.nelyan.fragments.MessageFragment
import com.nelyan.fragments.MyAddFragment
import com.nelyan.fragments.NurseFragment
import com.nelyan.fragments.NurserieFragment
import com.nelyan.fragments.SectorizationDetailsFragment
import com.nelyan.fragments.SectorizationListFragment
import com.nelyan.fragments.TraderPublishFragment

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
   lateinit  var mContext: Context
    var drawerLayout: DrawerLayout? = null
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
    var tvLog: TextView? = null
    var dialog: Dialog? = null
    var v: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mContext = this
        navigationbar = findViewById(R.id.navigationbar)
        tvTitleToolbar = findViewById(R.id.tvTitleToolbar)
        ivToolBarImage = findViewById(R.id.ivToolBarImage)
        iv_bell = findViewById(R.id.iv_bell)
        mDrawerLayout = findViewById(R.id.mDrawerLayout)
        loadFragment(HomeFragment())
        //loadFragment(new DrawerNewHomeFragment());
        val navigationbar = findViewById<BottomNavigationView>(R.id.navigationbar)
        navigationbar.setOnNavigationItemSelectedListener(this)
        val toolbar = supportActionBar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigationbar)
        setDrawerClicks()
        setToolBarClicks()
        try {
            try {
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "map") {
                        Log.e("vghgv", "hhhhhhh")
                        var fragment: Fragment? = null
                        fragment = ActivityFragment()
                        // Fragment fragment = new ActivityFragment();
                        val args = Bundle()
                        args.putString("unamea", "Homeactivity")
                        fragment!!.arguments = args
                        val fragmentManager = this.supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.frame_container, fragment)
                        fragmentTransaction.commit()
                        /*  getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_container, fragment)
                                .commit();*/
                        //   loadFragment( fragment);
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
                        var fragment: Fragment? = null
                        fragment = NurserieFragment()
                        loadFragment(fragment)
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
                        var fragment: Fragment? = null
                        fragment = ActivityDetailsFragment()
                        //    loadFragment( fragment);
                        val args = Bundle()
                        args.putString("activity", "Home")
                        fragment!!.arguments = args
                        val fragmentManager = this.supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.frame_container, fragment)
                        fragmentTransaction.commit()
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
            val i = Intent(this@HomeActivity, com.nelyan.ui.LoginActivity::class.java)
            startActivity(i)
        })
        tvAdd = findViewById(R.id.tvAdd)
        tvAdd!!.setOnClickListener(View.OnClickListener {
            mDrawerLayout!!.closeDrawers()
            val i = Intent(this@HomeActivity, com.nelyan.ui.AddActivity::class.java)
            startActivity(i)


            //  AppUtils.gotoFragment(mContext, new MyAddFragment(), R.id.frame_container, false);
        })
        tvFavorite = findViewById(R.id.tvFavorite)
        tvFavorite!!.setOnClickListener(View.OnClickListener {
            mDrawerLayout!!.closeDrawers()
            AppUtils.gotoFragment(mContext, FavoriteFragment(), R.id.frame_container, false)
        })
        tvProfile = findViewById(R.id.tvProfile)
        tvProfile!!.setOnClickListener(View.OnClickListener {
            mDrawerLayout!!.closeDrawers()
            val i = Intent(this@HomeActivity, com.nelyan.ui.ProfileActivity::class.java)
            startActivity(i)
        })
        tvContact = findViewById(R.id.tvContact)
        tvContact!!.setOnClickListener(View.OnClickListener {
            mDrawerLayout!!.closeDrawers()
            val i = Intent(this@HomeActivity, com.nelyan.ui.ContactUsActivity::class.java)
            startActivity(i)
        })
        tvNoti = findViewById(R.id.tvNoti)
        tvNoti!!.setOnClickListener(View.OnClickListener {
            mDrawerLayout!!.closeDrawers()
            val i = Intent(this@HomeActivity, com.nelyan.ui.NotificationActivity::class.java)
            startActivity(i)
        })
        tvSettings = findViewById(R.id.tvSettings)
        tvSettings!!.setOnClickListener(View.OnClickListener {
            mDrawerLayout!!.closeDrawers()
            val i = Intent(this@HomeActivity, com.nelyan.ui.SettingsActivity::class.java)
            startActivity(i)
        })
        tvLog = findViewById(R.id.tvLog)
        tvLog!!.setOnClickListener(View.OnClickListener {
            mDrawerLayout!!.closeDrawers()
            showLog()
        })
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
                    val i = Intent(this@HomeActivity, com.nelyan.ui.NotificationActivity::class.java)
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
                    val i = Intent(this@HomeActivity, com.nelyan.ui.NotificationActivity::class.java)
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
                    val i = Intent(this@HomeActivity, com.nelyan.ui.NotificationActivity::class.java)
                    startActivity(i)
                }
                fragment = com.nelyan.ui.PublisherFrag()
            }
            R.id.chat -> {
                tvTitleToolbar!!.visibility = View.VISIBLE
                ivToolBarImage!!.visibility = View.GONE
                tvTitleToolbar!!.text = "Maisons-Laffite"
                iv_bell!!.setImageResource(R.drawable.tab_on)
                iv_bell!!.setOnClickListener {
                    //iv_bell.get
                }
                fragment = com.nelyan.ui.ChatFrag()
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
                    .addToBackStack("null")
                    .commit()
            return true
        }
        return false
    }

    private fun showLog() {
        dialog = Dialog(mContext!!)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_logout)
        dialog!!.setCancelable(true)
        val rlYes: RelativeLayout
        val rlNo: RelativeLayout
        rlYes = dialog!!.findViewById(R.id.rlYes)
        rlYes.setOnClickListener {
            val i = Intent(mContext, com.nelyan.ui.LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
            //   mContext.startActivity(new Intent(mContext, LoginActivity.class));
            dialog!!.dismiss()
        }
        rlNo = dialog!!.findViewById(R.id.rlNo)
        rlNo.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_container)
        if (currentFragment == HomeFragment()) {
            if (doubleBackToExitPressedOnce) {
                finishAffinity()
                return
            }
            doubleBackToExitPressedOnce = true
            //  Snackbar.make(login_relative, "", BaseTransientBottomBar.LENGTH_SHORT).show();
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else if (currentFragment == ActivityFragment()) {
            super.onBackPressed()
        } else if (currentFragment == ActivityDetailsFragment()) {
            super.onBackPressed()
        } else if (currentFragment == NurserieFragment()) {
            super.onBackPressed()
        } else if (currentFragment == TraderPublishFragment()) {
            super.onBackPressed()
        } else if (currentFragment == ChildCareFragment()) {
            super.onBackPressed()
        } else if (currentFragment == SectorizationDetailsFragment()) {
            super.onBackPressed()
        } else if (currentFragment == SectorizationListFragment()) {
            super.onBackPressed()
        } else {
            finish()
            super.onBackPressed()
            //navigationbar.getMenu().getItem(0).setChecked(true);
        }
    }
}