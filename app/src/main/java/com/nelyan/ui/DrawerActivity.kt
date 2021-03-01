package com.nelyan.ui

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
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.nelyan.R
import com.nelyan.fragments.FavoriteFragment
import com.nelyan.fragments.MyAddFragment

class DrawerActivity : AppCompatActivity() {
    var mContext: Context? = null
    var navigation: NavigationView? = null
    var drawerLayout: DrawerLayout? = null
    var ll_Profile: LinearLayout? = null
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
                    changeFragment(FavoriteFragment())
                R.id.profile -> startActivity(Intent(this@DrawerActivity, com.nelyan.ui.ProfileActivity::class.java))
                R.id.contact -> startActivity(Intent(this@DrawerActivity, ContactUsActivity::class.java))
                R.id.noti -> startActivity(Intent(this@DrawerActivity, com.nelyan.ui.TermsActivity::class.java))
                R.id.settings -> startActivity(Intent(this@DrawerActivity, com.nelyan.ui.SettingsActivity::class.java))
                R.id.logout -> {
                    builder!!.setMessage("Are you sure you want to logout?")
                            .setCancelable(false)
                            .setPositiveButton("Yes") { dialog, id ->
                                startActivity(Intent(this@DrawerActivity, com.nelyan.ui.LoginActivity::class.java))
                                finishAffinity()
                            }
                            .setNegativeButton("No") { dialog, id -> //  Action for 'NO' Button
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
}