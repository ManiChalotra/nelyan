package com.nelyan_live.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.datastore.preferences.core.preferencesKey
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.nelyan_live.db.DataStoragePreference
import com.nelyan_live.R
import com.nelyan_live.fragments.MyAddFragment
import com.nelyan_live.utils.OpenActivity
import kotlinx.android.synthetic.main.nav_header_drawer.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DrawerActivity : AppCompatActivity(), CoroutineScope {
    var mContext: Context? = null
    var navigation: NavigationView? = null
    var drawerLayout: DrawerLayout? = null
    var ll_Profile: LinearLayout? = null
    private val job by lazy {
        Job()
    }

    private val dataStoragePreference by lazy {
        DataStoragePreference(this@DrawerActivity)
    }

    var builder: AlertDialog.Builder? = null
    var container: FrameLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        mContext = this@DrawerActivity
        // ll_Profile=findViewById(R.id.ll_Profile);
        val navigationView = findViewById<View>(R.id.navigation) as NavigationView
        /*     View hView =  navigationView.getHeaderView(0);
        ll_Profile = hView.findViewById(R.id.lvProfile);*/builder = AlertDialog.Builder(this@DrawerActivity)
        navigation = findViewById(R.id.navigation)
        navigation!!.setItemIconTintList(null)
        val ivMenu = findViewById<ImageView>(R.id.ivMenu)
        ivMenu.setOnClickListener { drawerLayout!!.openDrawer(GravityCompat.START) }
        container = findViewById(R.id.container)
        drawerLayout = findViewById(R.id.drawer)
        startActivity(Intent(this@DrawerActivity, HomeActivity::class.java))



        //getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
        navigation!!.setCheckedItem(R.id.nav_home)
        navigation!!.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            val id = item.itemId
            when (id) {
                R.id.nav_home -> {
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                    //    changeFragment(new HomeFragment());
                    startActivity(Intent(this@DrawerActivity, HomeActivity::class.java))
                }
                R.id.add -> changeFragment(MyAddFragment())
                R.id.fev ->                       //  startActivity(new Intent(DrawerActivity.this,AfterSignupActivity.class));
                    OpenActivity(FavouriteActivity::class.java)
                //changeFragment(FavoriteFragment())
                R.id.profile -> startActivity(Intent(this@DrawerActivity, ProfileActivity::class.java))
                R.id.contact -> startActivity(Intent(this@DrawerActivity, ContactUsActivity::class.java))
                R.id.noti -> startActivity(Intent(this@DrawerActivity, TermsActivity::class.java))
                R.id.settings -> startActivity(Intent(this@DrawerActivity, SettingsActivity::class.java))
                R.id.logout -> {
                    builder!!.setMessage(getString(R.string.logout_message))
                            .setCancelable(false)
                            .setPositiveButton(getString(R.string.yes)) { dialog, id ->
                                startActivity(Intent(this@DrawerActivity, LoginActivity::class.java))
                                finishAffinity()
                            }
                            .setNegativeButton(getString(R.string.no)) { dialog, id -> //  Action for 'NO' Button
                                dialog.cancel()
                            }
                    //Creating dialog box
                    val alert = builder!!.create()
                    alert.show()
                }
            }
            true
        })
    }

    fun changeFragment(fragment: Fragment?) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.container, fragment!!).commit()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}