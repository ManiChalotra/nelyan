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
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.preferencesKey
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.nelyan_live.R
import com.nelyan_live.data.viewmodel.AppViewModel
import com.nelyan_live.db.DataStoragePreference
import com.nelyan_live.fragments.*
import com.nelyan_live.utils.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.alert_add_post_restiction.*
import kotlinx.android.synthetic.main.bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_drawer.tvLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
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
    var ivHomeUserpic: ImageView? = null
    var tvUserName: TextView? = null
    var tvTitleToolbar: TextView? = null
    var tvAdd: TextView? = null
    var tvFavorite: TextView? = null
    var tvProfile: TextView? = null
    var tvContact: TextView? = null
    var tvNoti: TextView? = null
    var tvSettings: TextView? = null
    var dialog: Dialog? = null
    var consultantUserDialog: Dialog? = null
    var v: View? = null
    var job = Job()
    private var authorization = ""
    private var userType = ""
    private var userlocation = ""
    private var userlat = ""
    private var userlong = ""
    var userId = ""
    lateinit var fragment: Fragment

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onResume() {
        super.onResume()
        authorization = intent?.extras?.getString("authorization").toString()
        Log.d("homeAuthKey", "----------$authorization")

        launch(Dispatchers.Main.immediate) {
             userId = dataStoragePreference.emitStoredValue(preferencesKey<String>("id")).first()
            val userImage = dataStoragePreference.emitStoredValue(preferencesKey<String>("imageLogin")).first()
             userlocation = dataStoragePreference.emitStoredValue(preferencesKey<String>("cityLogin")).first()
            userlat = dataStoragePreference.emitStoredValue(preferencesKey<String>("latitudeLogin")).first()
            userlong = dataStoragePreference.emitStoredValue(preferencesKey<String>("longitudeLogin")).first()
            val userName = dataStoragePreference.emitStoredValue(preferencesKey<String>("nameLogin")).first()
            userType = dataStoragePreference.emitStoredValue(preferencesKey<String>("typeLogin")).first()
          //  Glide.with(this@HomeActivity).load(from_admin_image_base_URl + userImage).error(R.drawable.user_img).into(ivHomeUserpic!!)

            Picasso.get().load(from_admin_image_base_URl + userImage).resize(100, 100)
                    .placeholder(ContextCompat.getDrawable(
                            ivHomeUserpic!!.context,
                            R.drawable.placeholder
                    )!!).into(ivHomeUserpic)

            tvUserName!!.text = userName
        }
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

        val bottomNavigationBar = findViewById<BottomNavigationView>(R.id.navigationbar)
        bottomNavigationBar.setOnNavigationItemSelectedListener(this)
        setDrawerClicks()
        setToolBarClicks()

            try {
                if (intent.hasExtra("chat")) {

                        Log.d("homeAuthKey====", "----------$userId")

                        userId = intent.getStringExtra("chat")!!
                    Log.d("homeAuthKey==22==", "----------$userId")

                    bottomNavigationBar.selectedItemId = R.id.msg
                       // fragment = MessageFragment()
                       // loadFragment(fragment)

                }
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "map") {

                        OpenActivity(ActivitiesFilterActivity::class.java)
                    }
                }
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "sectrofrag") {
                        fragment = SectorizationDetailsFragment()
                        loadFragment(fragment)
                    }
                }
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "activity") {
                        Log.e("vghgv", "hhhhhhh")
                        fragment = ChildCareFragment()
                        val args = Bundle()
                        args.putString("child", "childcare")
                        fragment.arguments = args
                        val fragmentManager = this.supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.frame_container, fragment)
                        fragmentTransaction.commit()
                    }
                }
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "nurFrag") {

                        OpenActivity(HomeChildCareDetailsActivity::class.java)
                    }
                }
                if (intent.hasExtra("activity")) {
                if (intent.getStringExtra("activity") == "tarfrag") {
                        fragment = TraderPublishFragment()
                        val args = Bundle()
                        args.putString("activity", "traderfragment")
                        fragment.arguments = args
                        val fragmentManager = this.supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.frame_container, fragment)
                        fragmentTransaction.commit()
                    }
                }
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "mater") {
                        fragment = MaternalFragmentFragment()
                        loadFragment(fragment)
                    }
                }
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "babyfrag") {
                        fragment = BabySitterFragment()
                        loadFragment(fragment)
                    }
                }
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "nur") {
                        OpenActivity(NurserieActivityy::class.java)
                    }
                }
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "seclist") {
                        fragment = SectorizationListFragment()
                        loadFragment(fragment)

                    }
                }
                if (intent.hasExtra("activity")) {
                    if (intent.getStringExtra("activity") == "acti") {
                        OpenActivity(ActivityDetailsActivity::class.java)
                    }
                }
            }
            catch (e: Exception) {
                fragment = HomeFragment()
                loadFragment(fragment)
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
        iv_back!!.visibility = View.VISIBLE
        iv_back!!.setOnClickListener {
            mDrawerLayout!!.openDrawer(navigation_view!!)
            mDrawerToggle = object : ActionBarDrawerToggle(this@HomeActivity, mDrawerLayout,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    super.onDrawerSlide(drawerView, slideOffset)
                    val min = 0.9f
                    val max = 1.0f
                    val scaleFactor = max - (max - min) * slideOffset
                    mainContainer!!.scaleX = scaleFactor
                    mainContainer!!.scaleY = scaleFactor
                }
            }
            mDrawerLayout!!.addDrawerListener(mDrawerToggle!!)
        }
    }

    private fun setDrawerClicks() {
        tvHome = findViewById(R.id.tvHome)
        tvUserName = findViewById(R.id.tv_user_name)
        ivHomeUserpic = findViewById(R.id.iv_home_userpic)
        tvAdd = findViewById(R.id.tv_myAdd)
        tvFavorite = findViewById(R.id.tvFavorite)
        tvProfile = findViewById(R.id.tvProfile)
        tvNoti = findViewById(R.id.tvNoti)
        tvContact = findViewById(R.id.tvContact)
        tvSettings = findViewById(R.id.tvSettings)

        tvHome!!.setOnClickListener {
            mDrawerLayout!!.closeDrawers()
            val i = Intent(this@HomeActivity, HomeActivity::class.java)
            startActivity(i)
            finishAffinity()
        }

        tvAdd!!.setOnClickListener {
            mDrawerLayout!!.closeDrawers()
            val i = Intent(this@HomeActivity, MyAddActivity::class.java)
            startActivity(i)

        }
        tvFavorite!!.setOnClickListener {
            mDrawerLayout!!.closeDrawers()
            OpenActivity(FavouriteActivity::class.java)
        }

        tvProfile!!.setOnClickListener {
            mDrawerLayout!!.closeDrawers()
            val i = Intent(this@HomeActivity, ProfileActivity::class.java)
            startActivity(i)
        }

        tvContact!!.setOnClickListener {
            mDrawerLayout!!.closeDrawers()
            OpenActivity(ContactUsActivity::class.java) {
                putString("authorization", authorization)
            }
        }

        tvNoti!!.setOnClickListener {
            mDrawerLayout!!.closeDrawers()
            val i = Intent(this@HomeActivity, NotificationActivity::class.java)
            startActivity(i)
        }

        tvSettings!!.setOnClickListener {
            mDrawerLayout!!.closeDrawers()

            OpenActivity(SettingsActivity::class.java) {
                putString("authorization", authorization)
            }
        }

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvLog -> {
                mDrawerLayout!!.closeDrawers()
                showLog()
            }
        }
    }

    fun consultantUserDialogMethod() {
        consultantUserDialog = Dialog(this@HomeActivity)
        consultantUserDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        consultantUserDialog!!.setContentView(R.layout.alert_add_post_restiction)
        consultantUserDialog!!.setCancelable(false)
        consultantUserDialog!!.setCanceledOnTouchOutside(false)

        consultantUserDialog!!.iv_cross.setOnClickListener {
            consultantUserDialog!!.dismiss()
        }
        consultantUserDialog!!.rl_open_settings.setOnClickListener {
            OpenActivity(SettingsActivity::class.java) {
                putString("authorization", authorization)
                consultantUserDialog!!.dismiss()
            }
        }

        consultantUserDialog!!.show()
    }

    override fun onNavigationItemSelected(item: MenuItem):Boolean {

        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_container)

        if(currentFragment is ChatFrag ) {
            Log.e("fasfasfa", "======ChatFrag")
            currentFragment.groupChatVM.disconnectSocket()
        }
        if(currentFragment is MessageFragment )
        {
            currentFragment.messagesVM.disconnectSocket()
        }
        if(currentFragment is EventFragment )
        {
            currentFragment.onDestroy()
        }

        when (item.itemId) {

            R.id.home -> {
                if (currentFragment !is HomeFragment) {
                    iv_bell!!.setImageResource(R.drawable.notification_icon)
                    tvTitleToolbar!!.visibility = View.GONE
                    ivToolBarImage!!.visibility = View.VISIBLE
                    iv_bell!!.setOnClickListener {
                        val i = Intent(this@HomeActivity, NotificationActivity::class.java)
                        startActivity(i) }
                    fragment = HomeFragment()
                    loadFragment(fragment)
                }
            }
            R.id.msg -> {
                if (currentFragment !is MessageFragment) {
                    if(currentFragment is ChatFrag)
                    {
                        Log.e("fasfasfa","======detach")
                        currentFragment.groupChatVM.disconnectSocket()
                    }
                tvTitleToolbar!!.visibility = View.VISIBLE
                ivToolBarImage!!.visibility = View.GONE
                tvTitleToolbar!!.text = "Message"
                iv_bell!!.setImageResource(R.drawable.notification_icon)
                iv_bell!!.setOnClickListener {
                    val i = Intent(this@HomeActivity, NotificationActivity::class.java)
                    startActivity(i)
                }
                fragment = MessageFragment()
                    loadFragment(fragment)
            }
            }
            R.id.publier -> {
                if (currentFragment !is PublisherFrag) {
                    if (userType == "2") {
                        tvTitleToolbar!!.visibility = View.VISIBLE
                        ivToolBarImage!!.visibility = View.GONE
                        tvTitleToolbar!!.text = "Publier"
                        iv_bell!!.setImageResource(R.drawable.notification_icon)
                        iv_bell!!.setOnClickListener {
                            val i = Intent(this@HomeActivity, NotificationActivity::class.java)
                            startActivity(i)
                        }
                        val bundle = Bundle()
                        val frag = PublisherFrag()
                        bundle.putString("authorization", authorization)
                        frag.arguments = bundle
                        fragment = frag
                        loadFragment(fragment)
                    }

                    else {
                        consultantUserDialogMethod()
                    }
                }
            }
            R.id.chat -> {
                if (currentFragment !is ChatFrag) {
                    tvTitleToolbar!!.visibility = View.VISIBLE
                    ivToolBarImage!!.visibility = View.GONE
                    tvTitleToolbar!!.text = userlocation
                    iv_bell!!.setImageResource(R.drawable.tab_on)

                    fragment = ChatFrag(userlocation, userlat, userlong)
                    loadFragment(fragment)
                }
            }
            R.id.event -> {
                if (currentFragment !is EventFragment) {
                    tvTitleToolbar!!.visibility = View.VISIBLE
                    ivToolBarImage!!.visibility = View.GONE
                    tvTitleToolbar!!.text = getString(R.string.upcoming_events) + "\n" + userlocation
                    iv_bell!!.setImageResource(R.drawable.location_circle)
                    iv_bell!!.setOnClickListener {
                        val intent = Intent(this@HomeActivity, ActivitiesOnMapActivity::class.java)
                        intent.putExtra("event", "activity")
                        startActivity(intent)
                    }
                    fragment = EventFragment()
                    loadFragment(fragment)
                }
            }
        }

        return true
    }

    private fun loadFragment(fragment: Fragment): Boolean {



        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit()

        return true
    }

    private fun showLog() {
        dialog = Dialog(this@HomeActivity)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_logout)
        dialog!!.setCancelable(true)
        val rlYes: RelativeLayout = dialog!!.findViewById(R.id.rlYes)
        rlYes.setOnClickListener {
            hitLogoutApi()
        }
        val rlNo: RelativeLayout = dialog!!.findViewById(R.id.rlNo)
        rlNo.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()

    }

    override fun onBackPressed() {

        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_container)
        if (currentFragment is HomeFragment) {
            if (doubleBackToExitPressedOnce) {
                finishAffinity()
                return
            } else {
                myCustomToast("press once again to exit from the App.")
            }
            doubleBackToExitPressedOnce = true
            Handler(Looper.myLooper()!!).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
        else {
                loadFragment(HomeFragment())
                iv_bell!!.setImageResource(R.drawable.notification_icon)
                tvTitleToolbar!!.visibility = View.GONE
                ivToolBarImage!!.visibility = View.VISIBLE
                iv_bell!!.setOnClickListener {
                    val i = Intent(this@HomeActivity, NotificationActivity::class.java)
                    startActivity(i)
                }
                supportFragmentManager.popBackStack()
            }

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
                        val i = Intent(this@HomeActivity, LoginActivity::class.java)
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
        when (value) {
            1 -> {
                pinkColour.invoke(tv_homeTab)
                blackColour.invoke(tv_messageTab)
                blackColour.invoke(tv_publisherTab)
                blackColour.invoke(tv_chatTab)
                blackColour.invoke(tv_eventTab)

            }
            2 -> {
                blackColour.invoke(tv_homeTab)
                pinkColour.invoke(tv_messageTab)
                blackColour.invoke(tv_publisherTab)
                blackColour.invoke(tv_chatTab)
                blackColour.invoke(tv_eventTab)

            }
            3 -> {
                blackColour.invoke(tv_homeTab)
                blackColour.invoke(tv_messageTab)
                pinkColour.invoke(tv_publisherTab)
                blackColour.invoke(tv_chatTab)
                blackColour.invoke(tv_eventTab)

            }
            4 -> {
                blackColour.invoke(tv_homeTab)
                blackColour.invoke(tv_messageTab)
                blackColour.invoke(tv_publisherTab)
                pinkColour.invoke(tv_chatTab)
                blackColour.invoke(tv_eventTab)
            }
            5 -> {
                blackColour.invoke(tv_homeTab)
                blackColour.invoke(tv_messageTab)
                blackColour.invoke(tv_publisherTab)
                blackColour.invoke(tv_chatTab)
                pinkColour.invoke(tv_eventTab)
            }
        }
    }

    val pinkColour = { textView: TextView ->
        textView.setTextColor(Color.parseColor("#9c238d"))
    }
    val blackColour = { textView: TextView ->
        textView.setTextColor(Color.parseColor("#000000"))
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}