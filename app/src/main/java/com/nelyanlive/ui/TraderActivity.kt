package com.nelyanlive.ui

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.adapter.DayTimeRepeatAdapter
import com.nelyanlive.adapter.ProductDetailRepeatAdapter
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.modals.DayTimeModel
import com.nelyanlive.modals.ProductDetailDataModel
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_addactivity.*
import kotlinx.android.synthetic.main.activity_trader.*
import kotlinx.android.synthetic.main.activity_trader.countycode
import kotlinx.android.synthetic.main.activity_trader.ivBack
import kotlinx.android.synthetic.main.activity_trader.ivImg1
import kotlinx.android.synthetic.main.activity_trader.ivImg2
import kotlinx.android.synthetic.main.activity_trader.trader_type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class TraderActivity : OpenCameraGallery(), View.OnClickListener, CoroutineScope,
    DayTimeRepeatAdapter.OnDayTimeRecyclerViewItemClickListner,
    ProductDetailRepeatAdapter.ProductRepeatListener {

    private val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(AppViewModel::class.java)
    }
    private var imageSelectedType = ""
    private var isImageSelected = false

    private var days = false
    private var product = false
    private var dayErrorNumber = 0
    var productErrorNumber = 0

    private val job by lazy { kotlinx.coroutines.Job() }
    private lateinit var dayTimeRepeatAdapter: DayTimeRepeatAdapter
    private lateinit var productDetailRepeatAdapter: ProductDetailRepeatAdapter
    private var countryCodee = "+33"
    private var productPhotoPosition = 0
    private var selectDayGroup: JSONArray = JSONArray()
    private var productDetailsGroup: JSONArray = JSONArray()
    private var media: JSONArray = JSONArray()

    var clickPosition: String = ""

    // dialo for progress
    private var progressDialog = ProgressDialog(this)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    // Initialize Places variables
    private val googleMapKey = "AIzaSyDQWqIXO-sNuMWupJ7cNNItMhR4WOkzXDE"
    private val placePickerRequest = 1

    private var cityName = ""
    private var cityLatitude = ""
    private var cityLongitude = ""
    var rvDayTime: RecyclerView? = null
    var rvProductDetails: RecyclerView? = null
    private lateinit var dayTimeModelArrayList: ArrayList<DayTimeModel>
    private lateinit var productArrayList: ArrayList<ProductDetailDataModel>
    var traderTypeId: String = ""
    var DaySelect: String = ""
    private var imagePathList = ArrayList<MultipartBody.Part>()

    private var selectedImages = mutableListOf("", "", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trader)

        initializeClicks()
        rvDayTime = findViewById(R.id.rvDayTime)
        rvProductDetails = findViewById(R.id.rv_product_details)

        countycode.setOnCountryChangeListener {
            countryCodee = countycode.selectedCountryCode.toString()
        }

        dayTimeRepeatAdapter = DayTimeRepeatAdapter(this, dayTimeModelArrayList, this)
        rvDayTime!!.adapter = dayTimeRepeatAdapter

        productDetailRepeatAdapter =
            ProductDetailRepeatAdapter(this, productArrayList, this@TraderActivity)
        rvProductDetails!!.layoutManager = LinearLayoutManager(this)
        rvProductDetails!!.adapter = productDetailRepeatAdapter

        hitTypeTradeActivityApi()
        traderTypeResponse()

    }

    private fun initializeClicks() {

        dayTimeModelArrayList = ArrayList()
        dayTimeModelArrayList.add(DayTimeModel("", "", "", "", ""))

        productArrayList = ArrayList()
        productArrayList.add(ProductDetailDataModel("", "", "", ""))

        ivImg.setOnClickListener(this)
        ivImg1.setOnClickListener(this)
        ivImg2.setOnClickListener(this)
        tv_address.setOnClickListener(this)
        btn_trader_submit.setOnClickListener(this)
        ivBack!!.setOnClickListener(this)
        tvAddTraderDay!!.setOnClickListener(this)
        tvAddTraderProduct!!.setOnClickListener(this)

    }

    private fun showPlacePicker() {
        Places.initialize(applicationContext, googleMapKey)
        val fields: List<Place.Field> =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent: Intent =
            Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, placePickerRequest)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == placePickerRequest) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)

                // cityName = place.name.toString()
                tv_address.text = place.address.toString()

                cityLatitude = place.latLng?.latitude.toString()
                cityLongitude = place.latLng?.longitude.toString()
                val geocoder = Geocoder(this, Locale.getDefault())
                val list = geocoder.getFromLocation(
                    place.latLng?.latitude!!.toDouble(),
                    place.latLng?.longitude!!.toDouble(),
                    1
                )
                cityName = if (!list[0].locality.isNullOrBlank()) {
                    list[0].locality
                } else {
                    place.name.toString()
                }

                Log.i(
                    "dddddd",
                    "Place: " + place.name + ", " + place.id + "," + place.address + "," + place.latLng
                )
            }
        }
    }

    private fun hitTypeTradeActivityApi() {
        if (checkIfHasNetwork(this@TraderActivity)) {
            val authkey = AllSharedPref.restoreString(this, "auth_key")
            appViewModel.sendTraderTypeTraderData(security_key, authkey)
        } else {
            showSnackBar(this@TraderActivity, getString(R.string.no_internet_error))
        }
    }

    private fun hitFinalTraderPostApi() {

        if (checkIfHasNetwork(this@TraderActivity)) {
            val authkey = AllSharedPref.restoreString(this, "auth_key")

            appViewModel.send_addPostTraderData(
                security_key,
                authkey,
                "3",
                traderTypeId,
                et_trader_shop_name.text.toString().trim(),
                et_description_trader.text.toString().trim(),
                countryCodee,
                et_trader_phone.text.toString().trim(),
                tv_address.text.toString().trim(),
                cityName,
                cityLatitude,
                cityLongitude,
                et_trader_email.text.toString(),
                et_web_address.text.toString(),
                selectDayGroup.toString(),
                productDetailsGroup.toString(),
                media.toString(),
                if (days) "1" else "0",
                if (product) "1" else "0"
            )

        } else {
            showSnackBar(this@TraderActivity, getString(R.string.no_internet_error))
        }
    }

    val type: ArrayList<String> = ArrayList()
    val typeId: ArrayList<String> = ArrayList()


    private fun traderTypeResponse() {

        appViewModel.observeActivityTypeTraderResponse()!!
            .observe(this, androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {

                    if (response.body() != null) {
                        val jsonObject = JSONObject(response.body().toString())
                        val jsonArray = jsonObject.getJSONArray("data")
                        type.add("")
                        typeId.add("")
                        for (i in 0 until jsonArray.length()) {
                            val name = jsonArray.getJSONObject(i).get("name").toString()
                            val idd = jsonArray.getJSONObject(i).get("id").toString()
                            type.add(name)
                            typeId.add(idd)
                        }
                        val arrayAdapter1 = ArrayAdapter(this, R.layout.customspinner, type)
                        trader_type.adapter = arrayAdapter1
                        trader_type.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    traderTypeId = typeId[position]
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                }
                            }
                    }
                } else {
                    ErrorBodyResponse(response, this, null)
                }
            })

        appViewModel.observeAddPostTraderResponse()!!
            .observe(this, androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    Log.d("addPostTraderResopnse", "-----------" + Gson().toJson(response.body()))
                    if (response.body() != null) {

                        progressDialog.hidedialog()
                        finishAffinity()
                        OpenActivity(HomeActivity::class.java)

                    }
                } else {
                    progressDialog.hidedialog()
                    ErrorBodyResponse(response, this, null)
                }
            })

        appViewModel.observeUploadImageResponse()!!
            .observe(this, androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    progressDialog.hidedialog()
                    Log.d("urlDataLoading", "------------" + Gson().toJson(response.body()))
                    if (response.body() != null) {
                        if (response.body()!!.data != null) {
                            val image = response.body()!!.data!![0].image.toString()

                            when (imageSelectedType) {

                                "1" -> {
                                    isImageSelected = true
                                    selectedImages[0] = image
                                    Glide.with(this).asBitmap().load(image_base_URl + image)
                                        .into(ivImg1!!)

                                }
                                "2" -> {
                                    isImageSelected = true
                                    selectedImages[1] = image
                                    Glide.with(this).asBitmap().load(image_base_URl + image)
                                        .into(ivImg2!!)
                                }
                                "3" -> {
                                    isImageSelected = true
                                    selectedImages[2] = image

                                    Glide.with(this).asBitmap().load(image_base_URl + image)
                                        .into(ivImg!!)
                                }
                                "4" -> {
                                    productArrayList[productPhotoPosition].image = image
                                    productDetailRepeatAdapter.notifyItemChanged(
                                        productPhotoPosition
                                    )
                                }
                            }
                            imageSelectedType = ""
                        }
                    }
                } else {
                    ErrorBodyResponse(response, this, null)
                }
            })

        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
        })

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.ivImg -> {
                imageSelectedType = "3"
                checkPermission(this)
            }
            R.id.ivImg1 -> {
                imageSelectedType = "1"
                checkPermission(this)
            }
            R.id.ivImg2 -> {
                imageSelectedType = "2"
                checkPermission(this)
            }
            R.id.tv_address -> {
                showPlacePicker()
            }

            R.id.tvAddTraderDay -> {

                Log.e("TraderActivity", "--Activity--" + dayTimeModelArrayList.size)
                if (dayTimeModelArrayList.size == 0) {
                    dayTimeModelArrayList.add(
                        DayTimeModel(
                            "", "", "", "", ""
                        )
                    )
                    rvDayTime!!.visibility = View.VISIBLE
                    tvAddTraderDay.visibility = View.GONE
                    dayTimeRepeatAdapter.notifyDataSetChanged()

                } else {
                    tvAddTraderDay.visibility = View.GONE
                }

            }

            R.id.tvAddTraderProduct -> {

                Log.e("TraderActivity", "--Activity--" + productArrayList.size)
                if (productArrayList.size == 0) {
                    productArrayList.add(
                        ProductDetailDataModel(
                            "", "", "", ""
                        )
                    )
                    rv_product_details!!.visibility = View.VISIBLE
                    txt_productdetails!!.visibility = View.VISIBLE
                    tvAddTraderProduct.visibility = View.GONE
                    productDetailRepeatAdapter.notifyDataSetChanged()

                } else {
                    tvAddTraderProduct.visibility = View.GONE
                }
            }

            R.id.btn_trader_submit -> {

                if (!isImageSelected) {
                    myCustomToast(getString(R.string.media_missing_error))
                } else {
                    if (traderTypeId == "") {
                        myCustomToast(getString(R.string.trader_type_missing_error))
                    } else {
                        if (et_trader_shop_name.text.isEmpty()) {
                            myCustomToast(getString(R.string.shop_name_missing_error))
                        } else {
                            if (et_description_trader.text.isEmpty()) {
                                myCustomToast(getString(R.string.description_missing))
                            } else {
                                if (tv_address.text.isEmpty()) {
                                    myCustomToast(getString(R.string.address_missing_error))
                                } else {

                                    days = true

                                    var ageErrorString = ""
                                    dayErrorNumber = 0

                                    var productErrorString = ""
                                    productErrorNumber = 0


                                    if (productArrayList.size == 0) {

                                    } else {
                                        productErrorString = getProductError(
                                            productArrayList[productArrayList.size - 1].image,
                                            productErrorString,
                                            getString(R.string.select_image_in_previous),
                                            1
                                        )
                                        productErrorString = getProductError(
                                            productArrayList[productArrayList.size - 1].productTitle,
                                            productErrorString,
                                            getString(R.string.fill_title_previous),
                                            2
                                        )
                                        productErrorString = getProductError(
                                            productArrayList[productArrayList.size - 1].productPrice,
                                            productErrorString,
                                            getString(R.string.fill_price_previous),
                                            3
                                        )
                                        productErrorString = getProductError(
                                            productArrayList[productArrayList.size - 1].description,
                                            productErrorString,
                                            getString(R.string.fill_description_in_previous),
                                            4
                                        )
                                    }
                                    if (dayErrorNumber != 0 && ageErrorString.isNotEmpty()) {
                                        myCustomToast(ageErrorString)
                                    } else {
//
                                        if (productErrorNumber != 0 && productErrorString.isNotEmpty()) {
                                            myCustomToast(productErrorString)
                                        } else {
                                            media = JSONArray()
                                            for (i in 0 until selectedImages.size) {
                                                val json = JSONObject()
                                                json.put("image", selectedImages[i])
                                                media.put(json)
                                            }

                                            if (dayTimeModelArrayList.size == 0) {
                                                Log.d(
                                                    TraderActivity::class.java.name,
                                                    "TraderActivity_SelectDay   " + dayTimeModelArrayList.size
                                                )

                                                if (product) {
                                                    productDetailsGroup = JSONArray()
                                                    for (i in 0 until productArrayList.size) {
                                                        val json = JSONObject()
                                                        json.put(
                                                            "image",
                                                            productArrayList[i].image
                                                        )
                                                        json.put(
                                                            "title",
                                                            productArrayList[i].productTitle
                                                        )
                                                        json.put(
                                                            "price",
                                                            productArrayList[i].productPrice
                                                        )
                                                        json.put(
                                                            "description",
                                                            productArrayList[i].description
                                                        )
                                                        Log.d(
                                                            TraderActivity::class.java.name,
                                                            "TraderJson_product   " + json
                                                        )
                                                        productDetailsGroup.put(json)
                                                    }
                                                }
                                                hitFinalTraderPostApi()

                                            } else {

                                                if (days) {
                                                    selectDayGroup = JSONArray()
                                                    for (i in 0 until dayTimeModelArrayList.size) {

                                                        Log.d(
                                                            TraderActivity::clickPosition.name,
                                                            "TraderActivity_daylist   " + dayTimeModelArrayList.size
                                                        )
                                                        val json = JSONObject()
                                                        json.put(
                                                            "day_name",
                                                            dayTimeModelArrayList[i].selectedDay
                                                        )


                                                        if (dayTimeModelArrayList[dayTimeModelArrayList.size - 1].firstStarttime!!.isEmpty() && dayTimeModelArrayList[dayTimeModelArrayList.size - 1].secondStarttime!!.isEmpty() && dayTimeModelArrayList[dayTimeModelArrayList.size - 1].firstEndtime!!.isEmpty() && dayTimeModelArrayList[dayTimeModelArrayList.size - 1].secondEndtime!!.isEmpty() && dayTimeModelArrayList[i].selectedDay!!.isNotEmpty()) {
                                                            Log.d(
                                                                TraderActivity::class.java.name,
                                                                "TraderActivity_519  "
                                                            )
                                                            json.put(
                                                                "time_from",
                                                                dayTimeModelArrayList[i].firstStarttime
                                                            )
                                                            json.put(
                                                                "time_to",
                                                                dayTimeModelArrayList[i].firstEndtime
                                                            )
                                                            json.put(
                                                                "secondStartTime",
                                                                dayTimeModelArrayList[i].secondStarttime
                                                            )
                                                            json.put(
                                                                "secondEndTime",
                                                                dayTimeModelArrayList[i].secondEndtime
                                                            )
                                                            selectDayGroup.put(json)
                                                            hitFinalTraderPostApi()
//                                                            myCustomToast(getString(R.string.selecttime_txt))
                                                        } else if (dayTimeModelArrayList[dayTimeModelArrayList.size - 1].firstStarttime!!.isEmpty() && dayTimeModelArrayList[dayTimeModelArrayList.size - 1].firstEndtime!!.isNotEmpty()) {

                                                            Log.d(
                                                                TraderActivity::class.java.name,
                                                                "TraderActivity_528  "
                                                            )

                                                            myCustomToast(getString(R.string.selectmorningtime_txt))
                                                        } else if (dayTimeModelArrayList[dayTimeModelArrayList.size - 1].firstStarttime!!.isNotEmpty() && dayTimeModelArrayList[dayTimeModelArrayList.size - 1].firstEndtime!!.isEmpty()) {
                                                            Log.d(
                                                                TraderActivity::class.java.name,
                                                                "TraderActivity_534  "
                                                            )
                                                            json.put(
                                                                "time_from",
                                                                dayTimeModelArrayList[i].firstStarttime
                                                            )
                                                            json.put(
                                                                "time_to",
                                                                dayTimeModelArrayList[i].firstEndtime
                                                            )
                                                            json.put(
                                                                "secondStartTime",
                                                                dayTimeModelArrayList[i].secondStarttime
                                                            )
                                                            json.put(
                                                                "secondEndTime",
                                                                dayTimeModelArrayList[i].secondEndtime
                                                            )
                                                            selectDayGroup.put(json)
                                                            hitFinalTraderPostApi()
//                                                            myCustomToast(getString(R.string.selectmorninglasttime_txt))
                                                        } else if (dayTimeModelArrayList[dayTimeModelArrayList.size - 1].secondStarttime!!.isEmpty() && dayTimeModelArrayList[dayTimeModelArrayList.size - 1].secondEndtime!!.isNotEmpty()) {
                                                            Log.d(
                                                                TraderActivity::class.java.name,
                                                                "TraderActivity_540  "
                                                            )


                                                            myCustomToast(getString(R.string.selecteveningtime_txt))
                                                        } else if (dayTimeModelArrayList[dayTimeModelArrayList.size - 1].secondStarttime!!.isNotEmpty() && dayTimeModelArrayList[dayTimeModelArrayList.size - 1].secondEndtime!!.isEmpty()) {
//
                                                            Log.d(
                                                                TraderActivity::class.java.name,
                                                                "TraderActivity_545  "
                                                            )
                                                            json.put(
                                                                "time_from",
                                                                dayTimeModelArrayList[i].firstStarttime
                                                            )
                                                            json.put(
                                                                "time_to",
                                                                dayTimeModelArrayList[i].firstEndtime
                                                            )
                                                            json.put(
                                                                "secondStartTime",
                                                                dayTimeModelArrayList[i].secondStarttime
                                                            )
                                                            json.put(
                                                                "secondEndTime",
                                                                dayTimeModelArrayList[i].secondEndtime
                                                            )
                                                            selectDayGroup.put(json)
                                                            hitFinalTraderPostApi()
//                                                            myCustomToast(getString(R.string.selecteveninglasttime_txt))
                                                        } else {
                                                            json.put(
                                                                "time_from",
                                                                dayTimeModelArrayList[i].firstStarttime
                                                            )
                                                            json.put(
                                                                "time_to",
                                                                dayTimeModelArrayList[i].firstEndtime
                                                            )
                                                            json.put(
                                                                "secondStartTime",
                                                                dayTimeModelArrayList[i].secondStarttime
                                                            )
                                                            json.put(
                                                                "secondEndTime",
                                                                dayTimeModelArrayList[i].secondEndtime
                                                            )
                                                            Log.d(
                                                                TraderActivity::class.java.name,
                                                                "TraderJson_Days   " + json
                                                            )
                                                            Log.d(
                                                                TraderActivity::class.java.name,
                                                                "TraderActivity_DaysFirstData   " + json
                                                            )

                                                            selectDayGroup.put(json)
                                                            if (product) {
                                                                Log.d(
                                                                    TraderActivity::class.java.name,
                                                                    "TraderActivity_Products   " + product
                                                                )
                                                                productDetailsGroup = JSONArray()
                                                                for (i in 0 until productArrayList.size) {

                                                                    Log.d(
                                                                        TraderActivity::class.java.name,
                                                                        "TraderActivity_Products_644   " + productArrayList.size
                                                                    )
                                                                    val json = JSONObject()
                                                                    json.put(
                                                                        "image",
                                                                        productArrayList[i].image
                                                                    )
                                                                    json.put(
                                                                        "title",
                                                                        productArrayList[i].productTitle
                                                                    )
                                                                    json.put(
                                                                        "price",
                                                                        productArrayList[i].productPrice
                                                                    )
                                                                    json.put(
                                                                        "description",
                                                                        productArrayList[i].description
                                                                    )
                                                                    Log.d(
                                                                        TraderActivity::class.java.name,
                                                                        "TraderJson_product   " + json
                                                                    )
                                                                    productDetailsGroup.put(json)
                                                                }
                                                            }
                                                            hitFinalTraderPostApi()
                                                            Toast.makeText(
                                                                this,
                                                                "SuccessFul",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }

                                                    }
                                                } else {
                                                    Log.d(
                                                        TraderActivity::class.java.name,
                                                        "TraderActivity_Day_Value   " + days
                                                    )
                                                }
                                            }

//                                            if (product) {
//                                                productDetailsGroup = JSONArray()
//                                                for (i in 0 until productArrayList.size) {
//                                                    val json = JSONObject()
//                                                    json.put("image", productArrayList[i].image)
//                                                    json.put(
//                                                        "title",
//                                                        productArrayList[i].productTitle
//                                                    )
//                                                    json.put(
//                                                        "price",
//                                                        productArrayList[i].productPrice
//                                                    )
//                                                    json.put(
//                                                        "description",
//                                                        productArrayList[i].description
//                                                    )
//                                                    Log.d(
//                                                        TraderActivity::class.java.name,
//                                                        "TraderJson_product   " + json
//                                                    )
//                                                    productDetailsGroup.put(json)
//                                                }
//                                            }
////                                            hitFinalTraderPostApi()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

/* fun ApiData() {

     var ageErrorString = ""
     dayErrorNumber = 0
     var productErrorString = ""
     productErrorNumber = 0
     productErrorString = getProductError(
         productArrayList[productArrayList.size - 1].image,
         productErrorString,
         getString(R.string.select_image_in_previous),
         1
     )
     productErrorString = getProductError(
         productArrayList[productArrayList.size - 1].productTitle,
         productErrorString,
         getString(R.string.fill_title_previous),
         2
     )
     productErrorString = getProductError(
         productArrayList[productArrayList.size - 1].productPrice,
         productErrorString,
         getString(R.string.fill_price_previous),
         3
     )
     productErrorString = getProductError(
         productArrayList[productArrayList.size - 1].description,
         productErrorString,
         getString(R.string.fill_description_in_previous),
         4
     )

     if (dayErrorNumber != 0 && ageErrorString.isNotEmpty()) {
         myCustomToast(ageErrorString)
     } else {

         if (productErrorNumber != 0 && productErrorString.isNotEmpty()) {
             myCustomToast(productErrorString)
         } else {
             media = JSONArray()
             for (i in 0 until selectedImages.size) {
                 val json = JSONObject()
                 json.put("image", selectedImages[i])
                 media.put(json)
             }

             if (days) {
                 selectDayGroup = JSONArray()
                 for (i in 0 until dayTimeModelArrayList.size) {
                     val json = JSONObject()
                     json.put(
                         "day_name",
                         dayTimeModelArrayList[i].selectedDay
                     )
                     json.put(
                         "time_from",
                         dayTimeModelArrayList[i].firstStarttime
                     )
                     json.put(
                         "time_to",
                         dayTimeModelArrayList[i].firstEndtime
                     )
                     json.put(
                         "secondStartTime",
                         dayTimeModelArrayList[i].secondStarttime
                     )
                     json.put(
                         "secondEndTime",
                         dayTimeModelArrayList[i].secondEndtime
                     )
                     selectDayGroup.put(json)
                     Log.d(
                         TraderActivity::class.java.name,
                         "TraderActivity_traderpost   " + json
                     )
                 }
             }

             if (product) {
                 productDetailsGroup = JSONArray()
                 for (i in 0 until productArrayList.size) {
                     val json = JSONObject()
                     json.put("image", productArrayList[i].image)
                     json.put(
                         "title",
                         productArrayList[i].productTitle
                     )
                     json.put(
                         "price",
                         productArrayList[i].productPrice
                     )
                     json.put(
                         "description",
                         productArrayList[i].description
                     )
                     productDetailsGroup.put(json)
                 }
             }
             hitFinalTraderPostApi()

         }
     }
 }*/

    private fun getDayError(
        ageFrom: String?,
        ageErrorString: String,
        s: String,
        i: Int
    ): String {

        return when {
            ageFrom.isNullOrBlank() -> when {
                ageErrorString.isBlank() -> {
                    s
                }
                else -> ageErrorString
            }
            else -> {
                days = true
                dayErrorNumber = i
                ageErrorString
            }
        }
    }

    private fun getProductError(
        productFrom: String?,
        productErrorString: String,
        s: String,
        i: Int
    ): String {

        return when {
            productFrom.isNullOrBlank() -> when {
                productErrorString.isBlank() -> {
                    s
                }
                else -> productErrorString
            }
            else -> {
                product = true
                productErrorNumber = i
                productErrorString
            }
        }
    }


    override fun getRealImagePath(imgPath: String?) {

        uploadImageServer(imgPath)
    }

    private fun uploadImageServer(imgPath: String?) {

        val mFile: File?
        mFile = File(imgPath!!)
        val imageFile: RequestBody = mFile.asRequestBody("image/*".toMediaTypeOrNull())
        val photo: MultipartBody.Part?
        photo = MultipartBody.Part.createFormData("image", mFile.name, imageFile)
        imagePathList.add(0, photo)
        val type: RequestBody = "image".toRequestBody("text/plain".toMediaTypeOrNull())

        val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
        if (checkIfHasNetwork(this)) {
            appViewModel.sendUploadImageData(type, users, imagePathList)
            progressDialog.setProgressDialog()
        } else {
            showSnackBar(this, getString(R.string.no_internet_error))
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun dayTimeAdd(list: java.util.ArrayList<DayTimeModel>, position: Int) {

        when {
            dayTimeModelArrayList[list.size - 1].selectedDay.isNullOrEmpty() -> {
                myCustomToast(getString(R.string.select_day_previous))
                Log.d(
                    TraderActivity::class.java.name,
                    "TraderActivity_898   " + dayTimeModelArrayList[list.size - 1].selectedDay
                )

            }
            dayTimeModelArrayList[list.size - 1].selectedDay!!.isNotEmpty() -> {
                Log.d(
                    TraderActivity::class.java.name,
                    "TraderActivity_905   " + dayTimeModelArrayList[list.size - 1].selectedDay
                )
                dayTimeModelArrayList.add(DayTimeModel("", "", "", "", ""))
                dayTimeRepeatAdapter.notifyDataSetChanged()
            }

            /*dayTimeModelArrayList[list.size - 1].firstStarttime.isNullOrEmpty() -> {
//                myCustomToast(getString(R.string.select_morning_time_previous))
                if (dayTimeModelArrayList[list.size - 1].secondStarttime!!.isNotEmpty() && dayTimeModelArrayList[list.size - 1].secondEndtime!!.isNotEmpty()) {
                    dayTimeModelArrayList.add(DayTimeModel("", "", "", "", ""))
                    dayTimeRepeatAdapter.notifyDataSetChanged()
                } else {
                    myCustomToast(getString(R.string.select_morning_time_previous))
                }

            }
            dayTimeModelArrayList[list.size - 1].firstEndtime.isNullOrEmpty() -> {
//                myCustomToast(getString(R.string.select_morning_time_previous))

                if (dayTimeModelArrayList[list.size - 1].secondStarttime!!.isNotEmpty() && dayTimeModelArrayList[list.size - 1].secondEndtime!!.isNotEmpty()) {
                    dayTimeModelArrayList.add(DayTimeModel("", "", "", "", ""))
                    dayTimeRepeatAdapter.notifyDataSetChanged()
                } else {
                    myCustomToast(getString(R.string.select_morning_time_previous))
                }
            }
            dayTimeModelArrayList[list.size - 1].secondStarttime.isNullOrEmpty() -> {
//                myCustomToast(getString(R.string.select_evening_time_previous))
                if (dayTimeModelArrayList[list.size - 1].firstStarttime!!.isNotEmpty() && dayTimeModelArrayList[list.size - 1].firstEndtime!!.isNotEmpty()) {
                    dayTimeModelArrayList.add(DayTimeModel("", "", "", "", ""))
                    dayTimeRepeatAdapter.notifyDataSetChanged()
                } else {
                    myCustomToast(getString(R.string.select_evening_time_previous))
                }
            }
            dayTimeModelArrayList[list.size - 1].secondEndtime.isNullOrEmpty() -> {
//                myCustomToast(getString(R.string.select_evening_time_previous))
                if (dayTimeModelArrayList[list.size - 1].firstStarttime!!.isNotEmpty() && dayTimeModelArrayList[list.size - 1].firstEndtime!!.isNotEmpty()) {
                    dayTimeModelArrayList.add(DayTimeModel("", "", "", "", ""))
                    dayTimeRepeatAdapter.notifyDataSetChanged()
                } else {
                    myCustomToast(getString(R.string.select_evening_time_previous))
                }

            }*/

            else -> {
                dayTimeModelArrayList.add(DayTimeModel("", "", "", "", ""))
                dayTimeRepeatAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onSingleTraderDay(list: java.util.ArrayList<DayTimeModel>, position: Int) {

        rvDayTime!!.visibility = View.VISIBLE
//        txt_productdetails.visibility = View.VISIBLE
        tvAddTraderDay.visibility = View.GONE
        list.add(
            DayTimeModel(
                "", "", "", "", ""
            )
        )
        dayTimeRepeatAdapter.notifyDataSetChanged()
    }

    override fun onSingleTraderProduct(
        list: ArrayList<ProductDetailDataModel>,
        position: Int
    ) {
        rv_product_details.visibility = View.VISIBLE
        txt_productdetails.visibility = View.VISIBLE
        tvAddTraderProduct.visibility = View.GONE
        list.add(
            ProductDetailDataModel(
                "", "", "", ""
            )
        )
        productDetailRepeatAdapter.notifyDataSetChanged()
    }

    override fun onRemoveTraderProductItem(position: Int, list: Int) {
        txt_productdetails.visibility = View.GONE

        Log.d(TraderActivity::class.java.name, "TraderActivity_Remove_Position  " + position)

        Log.d(
            AddActivity::class.java.name,
            "OnRemoveClick   " + position + "    clickPosition   " + clickPosition + "   ListSize  " + list
        )

        clickPosition = position.toString()
        productDetailRepeatAdapter.notifyItemChanged(position)
        if (list == 0) {
            rv_product_details.visibility = View.GONE
            rv_product_details.visibility = View.GONE
            tvAddTraderProduct.visibility = View.VISIBLE
        } else {

        }
    }

    override fun onRemoveTraderDay(position: Int, list: Int) {
        Log.d(
            TraderActivity::class.java.name,
            "OnRemoveClick_Age   " + position + "    clickPosition   " + clickPosition + "   ListSize  " + list
        )

        clickPosition = position.toString()
        dayTimeRepeatAdapter.notifyItemChanged(position)
        if (list == 0) {
            rvDayTime!!.visibility = View.GONE
            tvAddTraderDay.visibility = View.VISIBLE
        } else {

        }
    }

    override fun ontraderItemClick(
        list: java.util.ArrayList<ProductDetailDataModel>,
        pos: Int
    ) {

        when {
            productArrayList[list.size - 1].image.isNullOrEmpty() -> {
                myCustomToast(getString(R.string.select_image_in_previous))
            }
            productArrayList[list.size - 1].productTitle.isNullOrEmpty() -> {
                myCustomToast(getString(R.string.fill_title_previous))

            }
            productArrayList[list.size - 1].productPrice.isNullOrEmpty() -> {
                myCustomToast(getString(R.string.fill_price_previous))

            }
            productArrayList[list.size - 1].description.isNullOrEmpty() -> {
                myCustomToast(getString(R.string.fill_description_in_previous))

            }

            else -> {
                productArrayList.add(ProductDetailDataModel("", "", "", ""))
                productDetailRepeatAdapter.notifyDataSetChanged()
            }
        }


    }

    override fun addCameraGalleryImage(list: ArrayList<ProductDetailDataModel>, pos: Int) {
        productPhotoPosition = pos
        imageSelectedType = "4"
        checkPermission(this)
    }

}