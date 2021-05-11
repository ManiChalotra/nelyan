package com.nelyan_live.ui

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
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
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.nelyan_live.R
import com.nelyan_live.adapter.*
import com.nelyan_live.db.DataStoragePreference
import com.nelyan_live.modals.traderPostDetails.TraderDaysTiming
import com.nelyan_live.modals.traderPostDetails.TraderProduct
import com.nelyan_live.modals.traderPostDetails.Tradersimage
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.alert_share.*

import kotlinx.android.synthetic.main.fragment_trader_publish.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.json.JSONArray
import org.json.JSONObject
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.util.ArrayList
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

    var datalistDays = ArrayList<TraderDaysTiming>()
  //  var daysList : ArrayList<ActivitiesEventsDaysModel> = ArrayList()

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


        ivBack!!.setOnClickListener(this)
        ivShare!!.setOnClickListener(this)


    /*    datalist.add(DetailsImageModal(R.drawable.img_1))
        datalist.add(DetailsImageModal(R.drawable.img_4))
        datalist.add(DetailsImageModal(R.drawable.img_1))
        datalist.add(DetailsImageModal(R.drawable.img_4))
        datalist.add(DetailsImageModal(R.drawable.img_1))
    */
        
        if (intent.extras !=null){
            postID= intent.getStringExtra("postId")!!
        }

        launch(Dispatchers.Main.immediate) {
            authkey = dataStoragePreference?.emitStoredValue(preferencesKey<String>("auth_key"))?.first()
            appViewModel.postDetailsApiData(security_key, authkey, "3", postID, "3")
            trader_details_progressbar?.showProgressBar()

        }

         checkMvvmResponse()

        /* val ad = DetailsImageAdapter(this, datalist)
         rc!!.setAdapter(ad)
         indicator!!.attachToRecyclerView(rc!!)
 */
    }

    fun dailogshare() {
        dialog = Dialog(this)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_share)
        dialog!!.setCancelable(true)
        val ll_1: LinearLayout
        ll_1 = dialog!!.findViewById(R.id.ll_public)

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
            //  email.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>("riteshsaini22@hotmail.com"))
            email.putExtra(Intent.EXTRA_SUBJECT, "Nelyan App")
            email.putExtra(Intent.EXTRA_TEXT, "Nelyan.. social app. \n"+
                    "https://play.google.com/store/apps/details?id=com.nelyan")
            email.setPackage("com.google.android.gm")
            email.type = "message/rfc822"
            try {
                startActivity(email)
            } catch (ex: ActivityNotFoundException) {
                myCustomToast( getString(R.string.gmail_not_error))
            }

            dialog!!.dismiss()
        }
        dialog!!.ll_whatsapp_share.setOnClickListener {
            CommonMethodsKotlin.whatsAppShare(this)

            dialog!!.dismiss()
        }
        dialog!!.ll_fv_share.setOnClickListener {
            CommonMethodsKotlin.fbShare(this)

            var content =  ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.nelyan"))
                    .setShareHashtag( ShareHashtag.Builder().setHashtag("Nelyan App").build())
                    .build()

            try {
                shareDialog!!.show(content) // Show ShareDialog

            } catch (ex: ActivityNotFoundException) {
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

                    val message = jsonObject.get("msg").toString()
                    myCustomToast(message)


                    tv_shop_name.text = jsonObject.getJSONObject("data").get("nameOfShop").toString()
                    var traderTypeId= jsonObject.getJSONObject("data").get("typeofTraderId").toString()

                    if (traderTypeId.equals("15")) {
                        tv_trader_type.text = this.getString(R.string.doctor)
                    } else if (traderTypeId.equals("16")) {
                        tv_trader_type.text = this.getString(R.string.food_court)
                    } else if (traderTypeId.equals("17")) {
                        tv_trader_type.text = this.getString(R.string.plant_nursury)
                    } else if (traderTypeId.equals("18")) {
                        tv_trader_type.text = this.getString(R.string.tutor_mathematics)
                    } else if (traderTypeId.equals("19")) {
                        tv_trader_type.text = this.getString(R.string.gym_trainer)
                    } else if (traderTypeId.equals("20")) {
                        tv_trader_type.text = this.getString(R.string.yoga_trainer)
                    } else if (traderTypeId.equals("21")) {
                        tv_trader_type.text = this.getString(R.string.gadget_repair)
                    }
                    tv_trader_desc.text = jsonObject.getJSONObject("data").get("description").toString()
                    tv_trader_phone.text = jsonObject.getJSONObject("data").get("country_code").toString() + "-" +
                            jsonObject.getJSONObject("data").get("phone").toString()
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
                        listTradersimage!!.clear()
                    } else {
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
                        rvTraderImages!!.setAdapter(traderDetailsImageAdapter)
                        indicator!!.attachToRecyclerView(rvTraderImages!!)
                    }


/*Set Age Group List in adapter*/
                    if (datalistDays != null) {
                        datalistDays!!.clear()
                    } else {
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
                        rvDays!!.setLayoutManager(lmmanager)
                        rvDays!!.setAdapter(traderShopsDaysAdapter)

                        rvDays!!.visibility = View.VISIBLE
                        tv_trader_no_days!!.visibility = View.GONE

                    } else {
                        rvDays!!.visibility = View.GONE
                        tv_trader_no_days!!.visibility = View.VISIBLE

                    }




/*Set Product Deatils List in adapter*/
                    if (listTraderProduct != null) {
                        listTraderProduct!!.clear()
                    } else {
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

                    } else {
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


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        val india = LatLng(48.946697, 2.153927)
        mMap!!.addMarker(MarkerOptions()
                .position(india)
                .title("Marker in Sydney"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(india))
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