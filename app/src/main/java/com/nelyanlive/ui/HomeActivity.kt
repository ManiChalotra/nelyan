package com.nelyanlive.ui

import android.Manifest
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.preferencesKey
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.current_location.LocationService
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.fragments.*
import com.nelyanlive.utils.*
import com.nelyanlive.utils.fcm.MyFirebaseMessagingService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.alert_add_post_restiction.*
import kotlinx.android.synthetic.main.bottom_navigation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.coroutines.CoroutineContext

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener, CoroutineScope, CommunicationListner {

    val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(AppViewModel::class.java)
    }

    val dataStoragePreference by lazy {
        DataStoragePreference(this)
    }

    var a = 1
    var doubleBackToExitPressedOnce = false
    var navigation_view: NavigationView? = null
    var mDrawerLayout: DrawerLayout? = null
    var mDrawerToggle: ActionBarDrawerToggle? = null
    var mainContainer: RelativeLayout? = null
    var bottomNavigationBar: BottomNavigationView? = null
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


    companion object {
        var locationService: LocationService? = null

    }

    private lateinit var sharedPreferences: SharedPreferences
    private var foregroundOnlyLocationServiceBound = false
    private lateinit var foregroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver
    private lateinit var homeFusedLocation: FusedLocationProviderClient


    var MY_REQUEST_CODE = 112233
    // lateinit var appUpdateManager: AppUpdateManager


    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationService.LocalBinder
            locationService = binder.service
            foregroundOnlyLocationServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            locationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }


    override fun onResume() {
        super.onResume()

        ClearPreference()

        Log.d("homeAuthKey", "----------$authorization")

        launch(Dispatchers.Main.immediate) {
            userId = dataStoragePreference.emitStoredValue(preferencesKey<String>("id")).first()
            authorization =
                dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
            val userImage =
                dataStoragePreference.emitStoredValue(preferencesKey<String>("imageLogin")).first()
            userlocation =
                dataStoragePreference.emitStoredValue(preferencesKey<String>("cityLogin")).first()
            userlat = dataStoragePreference.emitStoredValue(preferencesKey<String>("latitudeLogin"))
                .first()
            userlong =
                dataStoragePreference.emitStoredValue(preferencesKey<String>("longitudeLogin"))
                    .first()
            val userName =
                dataStoragePreference.emitStoredValue(preferencesKey<String>("nameLogin")).first()
            userType =
                dataStoragePreference.emitStoredValue(preferencesKey<String>("typeLogin")).first()
            callBadgeService(authorization)

            Picasso.get().load(from_admin_image_base_URl + userImage)
                .placeholder(
                    ContextCompat.getDrawable(
                        ivHomeUserpic!!.context,
                        R.drawable.placeholder
                    )!!
                ).into(ivHomeUserpic)

            tvUserName!!.text = userName
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            foregroundOnlyBroadcastReceiver,
            IntentFilter(LocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        )

    }

    private fun callBadgeService(authorization: String) {
        if (checkIfHasNetwork(this)) {

            appViewModel.badgeApiData(security_key, authorization)
        } else {
            showSnackBar(this, getString(R.string.no_internet_error))
        }

        appViewModel.observeBadgeApiResponse()!!.observe(this, Observer { response ->

            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {

                    Log.e(
                        "observeBadgeApiResponse",
                        "-------------" + Gson().toJson(response.body())
                    )
                    val jsonMain = JSONObject(response.body().toString())
                    Log.e("socket===", jsonMain.toString())

                    val data = jsonMain.getJSONObject("data")
                    val status = data.getString("status")

                    if (status == "1") ivBadge.visibility = View.VISIBLE

                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val serviceIntent = Intent(this, LocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        if (foregroundOnlyLocationServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
        super.onStop()
    }

    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //  appUpdateManager = AppUpdateManagerFactory.create(this)
        //  checkUpdate()

        ClearPreference()

        MyFirebaseMessagingService.chatNotifyLive.observe(this, Observer {

            if (it.equals("true")) {
                ivBadge.visibility = View.VISIBLE
            } else {
                ivBadge.visibility = View.GONE
            }
        })


        homeFusedLocation = LocationServices.getFusedLocationProviderClient(this)

        if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            homeFusedLocation.lastLocation.addOnSuccessListener { location: Location? ->
            }
        }

        initalize()
        checkMvvmresponse()

        //location
        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()
        sharedPreferences = getSharedPreferences("preference_file_key", Context.MODE_PRIVATE)

        tvTitleToolbar = findViewById(R.id.tvTitleToolbar)
        ivToolBarImage = findViewById(R.id.ivToolBarImage)
        iv_bell = findViewById(R.id.iv_bell)
        mDrawerLayout = findViewById(R.id.mDrawerLayout)
        loadFragment(HomeFragment())

        bottomNavigationBar = findViewById(R.id.navigationbar)
        bottomNavigationBar!!.setOnNavigationItemSelectedListener(this)
        bottomNavigationBar!!.setOnNavigationItemReselectedListener {
            Log.d(HomeActivity::class.java.name, "HomeActivity_BottomReselet  ")

            val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_container)
            if (currentFragment !is HomeFragment) {
                iv_bell!!.visibility = View.GONE
                tvTitleToolbar!!.visibility = View.GONE
                ivToolBarImage!!.visibility = View.VISIBLE
                fragment = HomeFragment()
                loadFragment(fragment)
            }
        }
        setDrawerClicks()
        setToolBarClicks()
        try {

            if (intent.hasExtra("groupChat")) {
                launch(Dispatchers.Main.immediate) {
                    userlocation = dataStoragePreference.emitStoredValue(preferencesKey<String>("cityLogin")).first()

                    userlat = dataStoragePreference.emitStoredValue(preferencesKey<String>("latitudeLogin")).first()

                    userlong = dataStoragePreference.emitStoredValue(preferencesKey<String>("longitudeLogin")).first()

                    Log.e(
                        "dataHome ------",
                        "-----222-----$userlocation------$userlat------$userlong----"
                    )
                    bottomNavigationBar!!.selectedItemId = R.id.chat
                }
            }

            if (intent.hasExtra("chat")) {
                userId = intent.getStringExtra("chat")!!
                bottomNavigationBar!!.selectedItemId = R.id.msg
            }
            if (intent.hasExtra("activity")) {
                if (intent.getStringExtra("activity") == "map") {

                    OpenActivity(ActivitiesFilterActivity::class.java)
                }
            }

            if (intent.hasExtra("activity")) {
                if (intent.getStringExtra("activity") == "nurFrag") {
                    OpenActivity(HomeChildCareDetailsActivity::class.java)
                }
            }
            if (intent.hasExtra("activity")) {
                if (intent.getStringExtra("activity") == "acti") {
                    OpenActivity(ActivityDetailsActivity::class.java)
                }
            }
            if (intent.hasExtra("eventfragment")) {
                if (intent.getStringExtra("eventfragment").equals("EventFragment")) {
                    tvTitleToolbar!!.visibility = View.VISIBLE
                    ivToolBarImage!!.visibility = View.GONE
                    tvTitleToolbar!!.text =
                        getString(R.string.upcoming_events) + "\n" + userlocation
                    iv_bell!!.setImageResource(R.drawable.location_circle)
                    iv_bell!!.imageTintList = null
                    fragment = EventFragment(userlat, userlong, userlocation)
                    loadFragment(fragment)
                }
            }
        } catch (e: Exception) {
            fragment = HomeFragment()
            loadFragment(fragment)
        }
    }


    /* private fun checkUpdate() {
       // Returns an intent object that you use to check for an update.
       val appUpdateInfoTask = appUpdateManager.appUpdateInfo
       // Checks that the platform will allow the specified type of update.
       Log.d("TAG", "Checking for updates")
       appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
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
    */
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

            val inputManager =
                it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                it.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )

            mDrawerLayout!!.openDrawer(navigation_view!!)
            mDrawerToggle = object : ActionBarDrawerToggle(
                this@HomeActivity, mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
            ) {
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

        consultantUserDialog!!.iv_cross.setOnClickListener { consultantUserDialog!!.dismiss() }
        consultantUserDialog!!.rl_open_settings.setOnClickListener {
            OpenActivity(SettingsActivity::class.java) {
                putString("authorization", authorization)
                consultantUserDialog!!.dismiss()
            }
        }

        consultantUserDialog!!.show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_container)
        Log.e("fasfasfa", "======$currentFragment")
        if (currentFragment is ChatFrag) {
            currentFragment.groupChatVM.disconnectSocket()
        }
        if (currentFragment is MessageFragment) {
            currentFragment.messagesVM.disconnectSocket()
        }
        if (currentFragment is EventFragment) {
            currentFragment.onDestroy()
        }

        when (item.itemId) {

            R.id.home -> {
                if (currentFragment !is HomeFragment) {
                    iv_bell!!.visibility = View.GONE
                    tvTitleToolbar!!.visibility = View.GONE
                    ivToolBarImage!!.visibility = View.VISIBLE
                    fragment = HomeFragment()
                    loadFragment(fragment)
                }
            }
            R.id.msg -> {
                ivBadge.visibility = View.GONE
                if (currentFragment !is MessageFragment) {
                    tvTitleToolbar!!.visibility = View.VISIBLE
                    ivToolBarImage!!.visibility = View.GONE
                    tvTitleToolbar!!.text = getString(R.string.message)
                    iv_bell!!.visibility = View.GONE
                    fragment = MessageFragment()
                    loadFragment(fragment)
                }
            }
            R.id.publier -> {
                if (currentFragment !is PublisherFrag) {
                    launch(Dispatchers.Main.immediate) {
                        val userType2 = dataStoragePreference.emitStoredValue(preferencesKey<String>("typeLogin")).first()
                        if (userType2 == "2") {
                            tvTitleToolbar!!.visibility = View.VISIBLE
                            ivToolBarImage!!.visibility = View.GONE
                            tvTitleToolbar!!.text = getString(R.string.publisher)
                            iv_bell!!.visibility = View.GONE
                            val bundle = Bundle()
                            val frag = PublisherFrag()
                            bundle.putString("authorization", authorization)
                            frag.arguments = bundle
                            fragment = frag
                            loadFragment(fragment)
                        } else {
                            consultantUserDialogMethod()
                        }
                    }
                }
            }
            R.id.chat -> {
                if (currentFragment !is ChatFrag) {
                    tvTitleToolbar!!.visibility = View.VISIBLE
                    ivToolBarImage!!.visibility = View.GONE
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val list =
                        geocoder.getFromLocation(userlat.toDouble(), userlong.toDouble(), 1)

                    tvTitleToolbar!!.text = if (!list[0].locality.isNullOrBlank()) {
                        list[0].locality
                    } else {
                        userlocation
                    }
                    fragment = ChatFrag(userlocation, userlat, userlong, iv_bell!!)
                    loadFragment(fragment)
                }
            }
            R.id.event -> {
                if (currentFragment !is EventFragment) {
                   // bottomNavigationBar!!.menu.getItem(4).isCheckable = false
                    tvTitleToolbar!!.visibility = View.VISIBLE
                    ivToolBarImage!!.visibility = View.GONE
                    tvTitleToolbar!!.text = getString(R.string.upcoming_events) + "\n" + userlocation
                    iv_bell!!.setImageResource(R.drawable.location_circle)
                    iv_bell!!.imageTintList = null
                    fragment = EventFragment(userlat, userlong, userlocation)
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
        rlYes.setOnClickListener { hitLogoutApi() }
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
                myCustomToast(getString(R.string.press_again_to_exit))
            }
            doubleBackToExitPressedOnce = true
            Handler(Looper.myLooper()!!).postDelayed(
                { doubleBackToExitPressedOnce = false },
                2000
            )
        } else {
            iv_bell!!.visibility = View.GONE
            tvTitleToolbar!!.visibility = View.GONE
            ivToolBarImage!!.visibility = View.VISIBLE
            fragment = HomeFragment()
            loadFragment(fragment)
            bottomNavigationBar!!.selectedItemId = R.id.home
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
                    Log.d("logoutResponse", "---222------" + Gson().toJson(response.body()))
                    val message = jsonObject.get("msg").toString()
                    myCustomToast(message)

                    launch(Dispatchers.Main.immediate) {
                        val i = Intent(this@HomeActivity, LoginActivity::class.java)
                        i.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
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

    fun ClearPreference() {
        AllSharedPref.clearFilterValue(this, "returnName")
        AllSharedPref.clearFilterValue(this, "returnLocation")
        AllSharedPref.clearFilterValue(this, "returnDistance")
        AllSharedPref.clearFilterValue(this, "minage")
        AllSharedPref.clearFilterValue(this, "maxage")
        AllSharedPref.clearFilterValue(this, "SelectValueactivity")
        AllSharedPref.clearFilterValue(this, "Age")

        AllSharedPref.clearFilterValue(this, "returnNameEvent")
        AllSharedPref.clearFilterValue(this, "returnLocationEvent")
        AllSharedPref.clearFilterValue(this, "returnDistanceEvent")
        AllSharedPref.clearFilterValue(this, "minage")
        AllSharedPref.clearFilterValue(this, "maxage")
        AllSharedPref.clearFilterValue(this, "SelectValueEvent")
        AllSharedPref.clearFilterValue(this, "AgeEvent")

        AllSharedPref.clearFilterValue(this, "returnnamechild")
        AllSharedPref.clearFilterValue(this, "returnlocationchild")
        AllSharedPref.clearFilterValue(this, "returndistancechild")
        AllSharedPref.clearFilterValue(this, "Selectvaluechild")

        AllSharedPref.clearFilterValue(this, "returnnametrade")
        AllSharedPref.restoreString(this, "returnlocationtrade")
        AllSharedPref.restoreString(this, "returndistancetrade")
        AllSharedPref.restoreString(this, "SelectValuetrade")

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}