package com.nelyanlive.ui

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.adapter.*
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.traderPostDetails.TraderDaysTiming
import com.nelyanlive.modals.traderPostDetails.TraderProduct
import com.nelyanlive.modals.traderPostDetails.Tradersimage
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.alert_share.*
import kotlinx.android.synthetic.main.fragment_activity_details.*
import kotlinx.android.synthetic.main.fragment_trader_publish.*
import kotlinx.android.synthetic.main.fragment_trader_publish.ivMapImage
import kotlinx.android.synthetic.main.fragment_trader_publish.nvsMain
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.json.JSONArray
import org.json.JSONObject
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.util.*
import kotlin.coroutines.CoroutineContext

class TraderPublishActivty : AppCompatActivity() , OnMapReadyCallback, View.OnClickListener, CoroutineScope {

    var ivShare: ImageView? = null
    var ivBack: ImageView? = null
    var postID : String=""
    var mMap: GoogleMap? = null
    var indicator: ScrollingPagerIndicator? = null
    var rc: RecyclerView? = null

    var rvTraderImages: RecyclerView? = null
    private var shareDialog: ShareDialog? = null

    private val dataStoragePreference by lazy { DataStoragePreference(this@TraderPublishActivty) }
    private var authkey: String? = null

    var rvProducts: RecyclerView? = null
    var rvDays: RecyclerView? = null
    
    var latitude = ""
    var longitude = ""
    var userId = ""
    var datalistDays = ArrayList<TraderDaysTiming>()

    var dialog: Dialog? = null
    private var listTradersimage = ArrayList<Tradersimage>()
    private var listTraderProduct = ArrayList<TraderProduct>()

    private val job by lazy {
        Job()
    }

    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_trader_publish)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        ivBack = findViewById(R.id.ivBack)
        ivShare = findViewById(R.id.ivShare)
        indicator = findViewById(R.id.trader_indicator)
        rvTraderImages = findViewById(R.id.rc_trader_images)
        rvDays = findViewById(R.id.rv_shops_timings)
        rvProducts = findViewById(R.id.rv_traderproduct)

        launch(Dispatchers.Main.immediate) {
             userId = dataStoragePreference.emitStoredValue(preferencesKey<String>("id")).first()
        }
        ivMapImage.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        nvsMain.requestDisallowInterceptTouchEvent(true)
                        // Disable touch on transparent view
                        return false
                    }

                    MotionEvent.ACTION_UP -> {
                        nvsMain.requestDisallowInterceptTouchEvent(false)
                        return true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        nvsMain.requestDisallowInterceptTouchEvent(true)
                        return false
                    }
                }
                return true
            }
        })
        ivBack!!.setOnClickListener(this)
        ivShare!!.setOnClickListener(this)

        if (intent.extras !=null){
            postID= intent.getStringExtra("postId")!!
        }

        launch(Dispatchers.Main.immediate) {
            authkey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                .first()
            appViewModel.postDetailsApiData(security_key, authkey, "3", postID, "3")
            trader_details_progressbar?.showProgressBar()

        }
         checkMvvmResponse()

    }

    fun dailogshare() {
        dialog = Dialog(this)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_share)
        dialog!!.setCancelable(true)

        dialog!!.ll_twitter_share.setOnClickListener {
            CommonMethodsKotlin.twitterShare(this)
            dialog!!.dismiss()
        }
        dialog!!.ll_instra_share.setOnClickListener {
            CommonMethodsKotlin.instaShare(this)
            dialog!!.dismiss()
        }
        dialog!!.ll_messanger_share.setOnClickListener {
            CommonMethodsKotlin.messageShare(this)
            dialog!!.dismiss()
        }
        dialog!!.ll_gmail_share.setOnClickListener {
            val email = Intent(Intent.ACTION_SEND)
            email.putExtra(Intent.EXTRA_SUBJECT, "Nelyan App")
            email.putExtra(Intent.EXTRA_TEXT, "Nelyan.. social app. \n"+
                    "https://play.google.com/store/apps/details?id=com.nelyan")
            email.setPackage("com.google.android.gm")
            email.type = "message/rfc822"
            try { startActivity(email) }
            catch (ex: ActivityNotFoundException) { myCustomToast( getString(R.string.gmail_not_error)) }

            dialog!!.dismiss()
        }
        dialog!!.ll_whatsapp_share.setOnClickListener {
            CommonMethodsKotlin.whatsAppShare(this)

            dialog!!.dismiss()
        }
        dialog!!.ll_fv_share.setOnClickListener {
            CommonMethodsKotlin.fbShare(this)

            val content =  ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.nelyan"))
                    .setShareHashtag( ShareHashtag.Builder().setHashtag("Nelyan App").build())
                    .build()

            try {
                shareDialog!!.show(content) // Show ShareDialog

            }
            catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "Facebook have not been installed.", Toast.LENGTH_SHORT).show()
            }


            dialog!!.dismiss()
        }
        dialog!!.show()
    }


    private fun checkMvvmResponse() {

        appViewModel.observePostDetailApiResponse()!!.observe(this, androidx.lifecycle.Observer { response ->

            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {

                    trader_details_progressbar?.hideProgressBar()
                    Log.d("postDetailsResponse", "-------------" + Gson().toJson(response.body()))


                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    if(!jsonObject.getJSONObject("data").isNull("user")) {
                        setUserData(jsonObject.getJSONObject("data").getJSONObject("user"))
                    }
                    val message = jsonObject.get("msg").toString()
                    myCustomToast(message)

                    tv_shop_name.text = jsonObject.getJSONObject("data").get("nameOfShop").toString()

                    when (jsonObject.getJSONObject("data").get("typeofTraderId").toString()) {
                        "15" -> {
                            tv_trader_type.text = this.getString(R.string.doctor)
                        }
                        "16" -> {
                            tv_trader_type.text = this.getString(R.string.food_court)
                        }
                        "17" -> {
                            tv_trader_type.text = this.getString(R.string.plant_nursury)
                        }
                        "18" -> {
                            tv_trader_type.text = this.getString(R.string.tutor_mathematics)
                        }
                        "19" -> {
                            tv_trader_type.text = this.getString(R.string.gym_trainer)
                        }
                        "20" -> {
                            tv_trader_type.text = this.getString(R.string.yoga_trainer)
                        }
                        "21" -> {
                            tv_trader_type.text = this.getString(R.string.gadget_repair)
                        }
                    }
                    tv_trader_desc.text = jsonObject.getJSONObject("data").get("description").toString()

                    if(jsonObject.getJSONObject("data").get("phone").toString().isNotBlank()) {
                        tv_trader_phone.text =
                            jsonObject.getJSONObject("data").get("country_code").toString() + "-" +
                                    jsonObject.getJSONObject("data").get("phone").toString()

                    }
                    else
                    {
                        tv_trader_phone.visibility = View.GONE
                        tvPhoneTrader.visibility = View.GONE
                    }
                    tv_trader_email.text = jsonObject.getJSONObject("data").get("email").toString()
                    tv_website.text = jsonObject.getJSONObject("data").get("website").toString()
                    tv_trader_address.text = jsonObject.getJSONObject("data").get("address").toString() +" "+
                            jsonObject.getJSONObject("data").get("city").toString()

                    longitude = jsonObject.getJSONObject("data").get("longitude").toString()
                    latitude = jsonObject.getJSONObject("data").get("latitude").toString()

                    val listArray: JSONArray = jsonObject.getJSONObject("data").getJSONArray("tradersimages")
                    val listArrayTraderDaysTimings: JSONArray = jsonObject.getJSONObject("data").getJSONArray("traderDaysTimings")
                    val listArrayProducts: JSONArray = jsonObject.getJSONObject("data").getJSONArray("traderProducts")

                    val mSizeOfData: Int = listArray.length()
                    val mSizeOfTraderDaysTimings: Int = listArrayTraderDaysTimings.length()
                    val mSizeOfProducts: Int = listArrayProducts.length()


/*Set Trader Images List in adapter*/

                    if (listTradersimage != null) {
                        listTradersimage.clear()
                    }
                    else {
                        listTradersimage = ArrayList()
                    }
                    for (i in 0 until mSizeOfData) {
                        val model = listArray.getJSONObject(i)
                        val createdAt = model.get("createdAt")
                        val updatedAt = model.get("updatedAt")
                        val tradersId = model.get("tradersId")
                        val images = model.get("images")
                        val mediaType = model.get("mediaType")
                        val id = model.get("id")

                        listTradersimage.add(Tradersimage(createdAt.toString(), id.toString().toInt(), images.toString(), mediaType.toString().toInt(),
                                tradersId.toString().toInt(), updatedAt.toString()))
                    }

                    if (mSizeOfData != 0) {
                        val traderDetailsImageAdapter = TraderDetailsImageAdapter(this, listTradersimage)
                        rvTraderImages!!.adapter = traderDetailsImageAdapter
                        indicator!!.attachToRecyclerView(rvTraderImages!!)
                    }
/*Set Age Group List in adapter*/
                    if (datalistDays != null) {
                        datalistDays.clear()
                    }
                    else {
                        datalistDays = ArrayList()
                    }

                    for (j in 0 until mSizeOfTraderDaysTimings) {
                        val dayssModel = listArrayTraderDaysTimings.getJSONObject(j)
                        val day = dayssModel.get("day")
                        val startTime = dayssModel.get("startTime")
                        val endTime = dayssModel.get("endTime")
                        val secondStartTime = dayssModel.get("secondStartTime")
                        val secondEndTime = dayssModel.get("secondEndTime")
                        val id = dayssModel.get("id")
                        val traderId = dayssModel.get("traderId")
                        val createdAt = dayssModel.get("createdAt")
                        val updatedAt = dayssModel.get("updatedAt")

                        datalistDays.add(TraderDaysTiming(createdAt.toString(), day.toString(), endTime.toString(),
                                id.toString().toInt(), secondEndTime.toString(), secondStartTime.toString(), startTime.toString(),
                                traderId.toString().toInt(), updatedAt.toString()))
                    }

                    if (mSizeOfTraderDaysTimings != 0) {
                        val traderShopsDaysAdapter = TraderShopsDaysAdapter(this, this@TraderPublishActivty, datalistDays)
                        val lmmanager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                        rvDays!!.layoutManager = lmmanager
                        rvDays!!.adapter = traderShopsDaysAdapter

                        rvDays!!.visibility = View.VISIBLE
                        tv_trader_no_days!!.visibility = View.GONE

                    }
                    else {
                        rvDays!!.visibility = View.GONE
                        tv_trader_no_days!!.visibility = View.VISIBLE

                    }

/*Set Product Deatils List in adapter*/
                    if (listTraderProduct != null) {
                        listTraderProduct.clear()
                    }
                    else {
                        listTraderProduct = ArrayList()
                    }

                    if (mSizeOfProducts != 0) {

                        for (k in 0 until mSizeOfProducts) {
                            val model = listArrayProducts.getJSONObject(k)
                            val productId = model.get("id")
                            val traderId = model.get("traderId")
                            val image = model.get("image")
                            val title = model.get("title")
                            val price = model.get("price")
                            val description = model.get("description")
                            val createdAt = model.get("createdAt")
                            val updatedAt = model.get("updatedAt")

                            listTraderProduct.add(TraderProduct(createdAt.toString(), description.toString(), productId.toString().toInt(),
                                    image.toString(), price.toString(), title.toString(), traderId.toString().toInt(),
                                    updatedAt.toString()))
                        }

                        val tradersProductListAdapter = TradersProductListAdapter(this, listTraderProduct )
                        rvProducts!!.adapter = tradersProductListAdapter

                        rvProducts!!.visibility = View.VISIBLE
                        tv_trader_no_product!!.visibility = View.GONE

                    }
                    else {
                        rvProducts!!.visibility = View.GONE
                        tv_trader_no_product!!.visibility = View.VISIBLE

                    }
                }
            } else {
                ErrorBodyResponse(response, this, trader_details_progressbar)
                trader_details_progressbar?.hideProgressBar()
            }
        })


        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
            trader_details_progressbar?.hideProgressBar()
        })
    }

    private fun setUserData(jsonObject: JSONObject) {


            if(userId!=jsonObject.getString("id")) {
                tvMessage.visibility = View.VISIBLE
                ivMessage.visibility = View.VISIBLE
                ivMessage.setOnClickListener {
                startActivity(
                    Intent(this@TraderPublishActivty, Chat1Activity::class.java)
                        .putExtra("senderID", jsonObject.getString("id"))
                        .putExtra("senderName", jsonObject.getString("name"))
                        .putExtra("senderImage", jsonObject.getString("image"))
                        .putExtra("userId", userId)
                )
                }

    }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        try {
            if (intent.extras !=null){
                val latti= intent.getStringExtra("latti")
                val longi= intent.getStringExtra("longi")

                val india = LatLng(latti!!.toDouble(), longi!!.toDouble())
                mMap!!.addMarker(MarkerOptions()
                    .position(india)
                    .title("Activity"))
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(india,10f))
            }
        }
        catch (e: Exception) {
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
         R.id.ivShare -> {
             dailogshare()
         }
         R.id.ivBack -> {
             onBackPressed()
         }
        }
        
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

}