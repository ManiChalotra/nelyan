package com.nelyan.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.nelyan.R
import com.nelyan.ui.*

class DrawerNewHomeFragment : Fragment() {
    var mContext: Context? = null
    var navigation: NavigationView? = null
    var iv_bell: ImageView? = null
    var ll_Profile: LinearLayout? = null
    var builder: AlertDialog.Builder? = null
    var container: FrameLayout? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var container = container
        val root = inflater.inflate(R.layout.fragment_drawer_new_home, container, false)
        mContext = context
        // ll_Profile=findViewById(R.id.ll_Profile);
        val navigationView = root.findViewById<View>(R.id.navigation) as NavigationView
        /*     View hView =  navigationView.getHeaderView(0);
        ll_Profile = hView.findViewById(R.id.lvProfile);*/iv_bell = root.findViewById(R.id.iv_bell)
        iv_bell!!.setOnClickListener(View.OnClickListener {
            val i = Intent(activity, NotificationActivity::class.java)
            startActivity(i)
        })
        builder = AlertDialog.Builder(requireContext()!!)
        navigation = root.findViewById(R.id.navigation)
        navigation!!.setItemIconTintList(null)
        val ivMenu = root.findViewById<ImageView>(R.id.ivMenu)
        ivMenu.setOnClickListener { drawerLayout!!.openDrawer(GravityCompat.START) }
        container = root.findViewById(R.id.container)
        drawerLayout = root.findViewById(R.id.drawer)
        requireActivity()!!.supportFragmentManager.beginTransaction().replace(R.id.container, com.nelyan.fragments.HomeFragment()).commit()
        navigation!!.setCheckedItem(R.id.nav_home)
        navigation!!.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            val id = item.itemId
            when (id) {
                R.id.nav_home ->                         //drawerLayout.closeDrawer(GravityCompat.START);
                    //    changeFragment(new HomeFragment());
                    // startActivity(new Intent(mContext,HomeActivity.class));
                    changeFragment(com.nelyan.fragments.HomeFragment())
                R.id.add -> {
                    changeFragment(com.nelyan.fragments.MyAddFragment())
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.fev -> {
                    //  startActivity(new Intent(DrawerActivity.this,AfterSignupActivity.class));
                    changeFragment(com.nelyan.fragments.FavoriteFragment())
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.profile -> {
                    startActivity(Intent(mContext, ProfileActivity::class.java))
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.contact -> {
                    startActivity(Intent(mContext, ContactUsActivity::class.java))
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.noti -> {
                    startActivity(Intent(mContext, TermsActivity::class.java))
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.settings -> {
                    startActivity(Intent(mContext, SettingsActivity::class.java))
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.logout -> {
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                    builder!!.setMessage("Are you sure you want to logout?")
                            .setCancelable(false)
                            .setPositiveButton("Yes") { dialog, id ->
                                startActivity(Intent(mContext, LoginActivity::class.java))
                                requireActivity()!!.finishAffinity()
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
        return root
    }

    fun changeFragment(fragment: Fragment?) {
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.container, fragment!!).commit()
    }

    companion object {
        var drawerLayout: DrawerLayout? = null
    }
}