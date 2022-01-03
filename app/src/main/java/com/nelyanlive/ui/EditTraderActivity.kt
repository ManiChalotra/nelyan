package com.nelyanlive.ui

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.adapter.DayTimeRepeatAdapter
import com.nelyanlive.adapter.EditProductDetailRepeatAdapter
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.DayTimeModel
import com.nelyanlive.modals.myAd.*
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_addactivity.*
import kotlinx.android.synthetic.main.activity_trader.*
import kotlinx.android.synthetic.main.activity_trader.countycode
import kotlinx.android.synthetic.main.activity_trader.ivBack
import kotlinx.android.synthetic.main.activity_trader.ivImg1
import kotlinx.android.synthetic.main.activity_trader.ivImg2
import kotlinx.android.synthetic.main.activity_trader.ivImg3
import kotlinx.android.synthetic.main.activity_trader.trader_type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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

class EditTraderActivity : OpenCameraGallery(), View.OnClickListener, CoroutineScope,
    EditProductDetailRepeatAdapter.ProductRepeatListener,
    DayTimeRepeatAdapter.OnDayTimeRecyclerViewItemClickListner {

    private val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(
            AppViewModel::class.java
        )
    }

    private var imageSelectedType = ""

    private val job by lazy { kotlinx.coroutines.Job() }
    private val dataStoragePreference by lazy { DataStoragePreference(this) }
    private lateinit var productDetailRepeatAdapter: EditProductDetailRepeatAdapter
    private lateinit var dayTimeAdapter: DayTimeRepeatAdapter
    private var countryCodee = "91"
    private var productPhotoPosition = 0
    private var selectDayGroup: JSONArray = JSONArray()
    private var productDetailsGroup: JSONArray = JSONArray()

    var productErrorNumber = 0
    var dayErrornumber = 0
    private var product = false
    private var dayTime = false
    var clickPosition: String = ""

    // dialo for progress
    private var progressDialog = ProgressDialog(this)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    // Initialize Places variables
    private val googleMapKey = "AIzaSyDQWqIXO-sNuMWupJ7cNNItMhR4WOkzXDE"
    private val PLACE_PICKER_REQUEST = 1

    private var cityName = ""
    private var cityLatitude = ""
    private var cityLongitude = ""
    private lateinit var dayTimeModelArrayList: ArrayList<TraderDaysTimingMyAds>
    private var dayTimeList: ArrayList<DayTimeModel> = ArrayList()
    private lateinit var productDetailDataModelArrayList: ArrayList<TraderProductMyAds>
    var traderTypeId: String = ""

    private var imagePathList = ArrayList<MultipartBody.Part>()


    var image1: String = ""
    var image2: String = ""
    var image3: String = ""

    private var postID = ""
    private var authKey = ""
    private lateinit var traderimageList: ArrayList<TradersimageMyAds>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trader)
        traderimageList = ArrayList()
        productDetailDataModelArrayList = ArrayList()

        initalizeClicks()

        countycode.setOnCountryChangeListener {
            countryCodee = countycode.selectedCountryCode.toString()
        }

        traderTypeResponse()

    }

    private fun initalizeClicks() {
        ivImg.setOnClickListener(this)
        ivImg1.setOnClickListener(this)
        ivImg2.setOnClickListener(this)
        ivImg3.setOnClickListener(this)
        tvAddTraderDay.setOnClickListener(this)
        tvAddTraderProduct.setOnClickListener(this)
        ivplus.setOnClickListener(this)
        tv_address.setOnClickListener(this)
        btn_trader_submit.setOnClickListener(this)
        ivBack!!.setOnClickListener(this)

        if (intent.extras != null) {
            et_trader_shop_name.setText(intent.getStringExtra("nameofShop").toString())
            et_description_trader.setText(intent.getStringExtra("description"))
            tv_address.text = intent.getStringExtra("address")
            cityName = intent.getStringExtra("city")!!
            cityLatitude = intent.getStringExtra("lati")!!
            cityLongitude = intent.getStringExtra("longi")!!
            et_trader_phone.setText(intent.getStringExtra("phoneNumber"))

            et_trader_email.setText(intent.getStringExtra("email"))
            et_web_address.setText(intent.getStringExtra("website"))

            countryCodee = intent.getStringExtra("countryCode").toString()
            countycode.setCountryForPhoneCode(countryCodee.toInt())

            postID = intent.getStringExtra("adID").toString()

            traderTypeId = intent.getStringExtra("traderTypeId").toString()

            hitTypeTradeActivity_Api()

            dayTimeModelArrayList =
                intent.getSerializableExtra("daytimeList") as ArrayList<TraderDaysTimingMyAds>

            productDetailDataModelArrayList =
                intent.getSerializableExtra("traderProductList") as ArrayList<TraderProductMyAds>

            Log.d(
                EditTraderActivity::class.java.name,
                "EditTraderActivity_productlist   " + productDetailDataModelArrayList.size
            )

            Log.d(
                EditTraderActivity::class.java.name,
                "EditTraderActivity_daylist   " + dayTimeModelArrayList.size
            )

            /*if (productDetailDataModelArrayList.isEmpty()) productDetailDataModelArrayList.add(
                TraderProductMyAds("", 0, "", "", "", 0)
            )*/

            if (productDetailDataModelArrayList.isNullOrEmpty() || productDetailDataModelArrayList.size == 0) {
                rv_product_details.visibility = View.GONE
                txt_productdetails.visibility = View.GONE
                tvAddTraderProduct.visibility = View.VISIBLE
            }

            if (dayTimeModelArrayList.isNullOrEmpty() || dayTimeModelArrayList.size == 0) {
                rvDayTime!!.visibility = View.GONE
                tvAddTraderDay.visibility = View.VISIBLE
            }

            productDetailRepeatAdapter =
                EditProductDetailRepeatAdapter(this, productDetailDataModelArrayList, this)
            rv_product_details!!.layoutManager = LinearLayoutManager(this)
            rv_product_details!!.adapter = productDetailRepeatAdapter

            dayTimeList = ArrayList()
            if (dayTimeModelArrayList.isEmpty()) {
                dayTimeList.add(DayTimeModel("", "", "", "", ""))
            } else {
                for (i in 0 until dayTimeModelArrayList.size) {
                    dayTimeList.add(
                        DayTimeModel(
                            dayTimeModelArrayList[i].day,
                            dayTimeModelArrayList[i].startTime,
                            dayTimeModelArrayList[i].endTime,
                            dayTimeModelArrayList[i].secondStartTime,
                            dayTimeModelArrayList[i].secondEndTime
                        )
                    )
                }
            }

            dayTimeAdapter = DayTimeRepeatAdapter(this, dayTimeList, this)
            rvDayTime!!.layoutManager = LinearLayoutManager(this)
            rvDayTime!!.adapter = dayTimeAdapter


            traderimageList =
                intent.getSerializableExtra("traderImageList") as ArrayList<TradersimageMyAds>

            if (!traderimageList.isNullOrEmpty()) {
                when (traderimageList.size) {
                    1 -> {
                        image1 = traderimageList[0].images
                        Glide.with(this).load(image_base_URl + traderimageList[0].images)
                            .error(R.mipmap.no_image_placeholder).into(ivImg1)

                    }
                    2 -> {
                        image1 = traderimageList[0].images
                        image2 = traderimageList[1].images
                        Glide.with(this).load(image_base_URl + traderimageList[0].images)
                            .error(R.mipmap.no_image_placeholder).into(ivImg1)
                        Glide.with(this).load(image_base_URl + traderimageList[1].images)
                            .error(R.mipmap.no_image_placeholder).into(ivImg2)

                    }
                    3 -> {
                        image1 = traderimageList[0].images
                        image2 = traderimageList[1].images
                        image3 = traderimageList[2].images
                        Glide.with(this).load(image_base_URl + traderimageList[0].images)
                            .error(R.mipmap.no_image_placeholder).into(ivImg1)
                        Glide.with(this).load(image_base_URl + traderimageList[1].images)
                            .error(R.mipmap.no_image_placeholder).into(ivImg2)
                        Glide.with(this).load(image_base_URl + traderimageList[2].images)
                            .error(R.mipmap.no_image_placeholder).into(ivImg)

                    }
                }
            }
        }
        launch(Dispatchers.Main.immediate) {
            authKey =
                dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
        }
    }

    private fun showPlacePicker() {
        Places.initialize(applicationContext, googleMapKey)
        val fields: List<Place.Field> =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent: Intent =
            Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    Log.i(
                        "editttttt",
                        "Place: " + place.name + "," + place.id + "," + place.address + "," + place.latLng
                    )

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

                }
            }
        }
    }

    private fun hitTypeTradeActivity_Api() {
        if (checkIfHasNetwork(this@EditTraderActivity)) {
            val authkey = AllSharedPref.restoreString(this, "auth_key")
            appViewModel.sendTraderTypeTraderData(security_key, authkey)
        } else {
            showSnackBar(this@EditTraderActivity, getString(R.string.no_internet_error))
        }
    }

    private fun hitFinalTraderPostApi() {


        if (checkIfHasNetwork(this@EditTraderActivity)) {
            val authkey = AllSharedPref.restoreString(this, "auth_key")

            var typeEmpty = ""
            Log.e("chheckkk","--="+selectDayGroup.toString()+"--=="+productDetailsGroup.toString())
                if(tvAddTraderDay.visibility==View.VISIBLE&&tvAddTraderProduct.visibility==View.VISIBLE) // day is empty
                {
                    typeEmpty = "2"
                }
                else if(tvAddTraderDay.visibility!=View.VISIBLE&&tvAddTraderProduct.visibility!=View.VISIBLE) //
                {
                    typeEmpty = "0"
                }
                else if(tvAddTraderDay.visibility==View.VISIBLE) // day is emptyy
                {
                    typeEmpty = "1"
                }
                else if(tvAddTraderProduct.visibility==View.VISIBLE) // prduck is emptyyy
                {
                    typeEmpty = "3"
                }
            Log.e("chheckkk","^^^&^&*%^&^&^&"+typeEmpty+"--==")

/*
            when {
                dayTime && product -> { // editTraderPost_Api (both yes)
                    typeEmpty = "0"
                }
                !product && dayTime -> { // product empty day true
                    typeEmpty = "3"
                }
                product && !dayTime -> { // editTraderPost_ApiwithoutDay
                    typeEmpty = "1"
                }
                !product && !dayTime -> { // editTraderPost_ApiWitoutProductandDay
                    typeEmpty = "2"
                }
            }
*/

            appViewModel.send_editPostTraderData(
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
                image1,
                image2,
                image3,
                postID,
                typeEmpty
            )
        }

        //}
        else {
            showSnackBar(this@EditTraderActivity, getString(R.string.no_internet_error))
        }
    }

    private val typeList: ArrayList<String> = ArrayList()
    private val typeListId: ArrayList<String> = ArrayList()
    private var selectedPosition = 0

    private fun traderTypeResponse() {
        appViewModel.observeActivityTypeTraderResponse()!!
            .observe(this, androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {

                    if (response.body() != null) {
                        val jsonObject = JSONObject(response.body().toString())
                        val mSizeOfData = jsonObject.getJSONArray("data").length()


                        typeList.clear()
                        typeListId.clear()
                        typeList.add("")
                        typeListId.add("")
                        for (i in 0 until mSizeOfData) {
                            val name = jsonObject.getJSONArray("data").getJSONObject(i).get("name")
                                .toString()
                            val id = jsonObject.getJSONArray("data").getJSONObject(i).get("id")
                                .toString()
                            typeList.add(name)
                            typeListId.add(id)
                            if (id == traderTypeId) {
                                selectedPosition = i
                            }
                        }
                        val arrayAdapte1 = ArrayAdapter(this, R.layout.customspinner, typeList)
                        trader_type.adapter = arrayAdapte1
                        trader_type.setSelection(selectedPosition + 1)

                        trader_type.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    traderTypeId = typeListId[position]
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                }
                            }

                    }
                } else {
                    ErrorBodyResponse(response, this, null)
                }
            })

        appViewModel.observeEditPostTraderResponse()!!
            .observe(this, androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    Log.d("addPostTraderResopnse", "-----------" + Gson().toJson(response.body()))
                    if (response.body() != null) {
                        response.body()
                        progressDialog.hidedialog()
                        finishAffinity()
                        myCustomToast(getString(R.string.post_updated))
                        OpenActivity(HomeActivity::class.java)

                    }else{
                        Log.d("addPostTraderResopnse", "-----------_else " + Gson().toJson(response.body()))
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
                                    image1 = image
                                    Glide.with(this).asBitmap().load(image_base_URl + image)
                                        .into(ivImg1!!)
                                }
                                "2" -> {
                                    image2 = image
                                    Glide.with(this).asBitmap().load(image_base_URl + image)
                                        .into(ivImg2!!)
                                }
                                "3" -> {
                                    image3 = image
                                    Glide.with(this).asBitmap().load(image_base_URl + image)
                                        .into(ivImg!!)
                                }
                                "4" -> {
                                    productDetailDataModelArrayList[productPhotoPosition].image =
                                        image
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
            R.id.ivImg1 -> {

                imageSelectedType = "1"
                checkPermission(this)
            }
            R.id.ivImg2 -> {

                imageSelectedType = "2"
                checkPermission(this)
            }
            R.id.ivImg -> {

                imageSelectedType = "3"
                checkPermission(this)
            }
            R.id.tvAddTraderDay -> {

                Log.e("TraderActivity", "--Activity--" + dayTimeList.size)
                if (dayTimeList.size == 0) {
                    dayTimeList.add(
                        DayTimeModel(
                            "",
                            "", "", "", ""
                        )
                    )
                    rvDayTime!!.visibility = View.VISIBLE
                    tvAddTraderDay.visibility = View.GONE
                    dayTimeAdapter.notifyDataSetChanged()

                } else {
                    rvDayTime!!.visibility = View.VISIBLE
                    tvAddTraderDay.visibility = View.GONE
                }

            }

            R.id.tvAddTraderProduct -> {

                Log.e("TraderActivity", "--Activity--" + productDetailDataModelArrayList.size)
                if (productDetailDataModelArrayList.size == 0) {
                    productDetailDataModelArrayList.add(
                        TraderProductMyAds(
                            "", 0, "", "", "", 0
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
            R.id.tv_address -> {
                showPlacePicker()
            }

            R.id.btn_trader_submit -> {

                if (traderTypeId == "") {
                    myCustomToast(getString(R.string.trader_type_missing_error))
                } else {
                    if (et_trader_shop_name.text.toString().isEmpty()) {
                        myCustomToast(getString(R.string.shop_name_missing_error))
                    } else {
                        if (et_description_trader.text.toString().isEmpty()) {
                            myCustomToast(getString(R.string.description_missing))
                        } else {
                            if (tv_address.text.toString().isEmpty()) {
                                myCustomToast(getString(R.string.address_missing_error))
                            } else {
                                var dayErrorString = ""
                                dayErrornumber = 0
                                var productErrorString = ""
                                productErrorNumber = 0

                                if (clickPosition.equals("0")) {
                                    Log.d(AddActivity::class.java.name, "AddActivity_agegroup_if  ")
                                }
                                else {
                                    /*   dayErrorString = getDayError(
                                          dayTimeList[dayTimeList.size - 1].selectedDay,
                                          dayErrorString,
                                          getString(R.string.select_day_previous),
                                          1
                                      )
                                     dayErrorString = getDayError(
                                          dayTimeList[dayTimeList.size - 1].firstStarttime,
                                          dayErrorString,
                                          getString(R.string.select_morning_time_previous),
                                          2
                                      )
                                      dayErrorString = getDayError(
                                          dayTimeList[dayTimeList.size - 1].firstEndtime,
                                          dayErrorString,
                                          getString(R.string.select_morning_time_previous),
                                          3
                                      )
                                      dayErrorString = getDayError(
                                          dayTimeList[dayTimeList.size - 1].secondStarttime,
                                          dayErrorString,
                                          getString(R.string.select_evening_time_previous),
                                          4
                                      )
                                      dayErrorString = getDayError(
                                          dayTimeList[dayTimeList.size - 1].secondEndtime,
                                          dayErrorString,
                                          getString(R.string.select_evening_time_previous),
                                          5
                                      )*/

                                    if (dayTimeList[dayTimeList.size - 1].selectedDay!!.isEmpty()) {
                                        dayErrornumber=11
                                        dayErrorString=getString(R.string.select_day)
                                    }
                                   else if (productDetailDataModelArrayList.size == 0) {

                                    } else {
                                        productErrorString = getProductError(
                                            productDetailDataModelArrayList[productDetailDataModelArrayList.size - 1].image,
                                            productErrorString,
                                            getString(R.string.select_image_in_previous),
                                            1
                                        )
                                        productErrorString = getProductError(
                                            productDetailDataModelArrayList[productDetailDataModelArrayList.size - 1].title,
                                            productErrorString,
                                            getString(R.string.fill_title_previous),
                                            2
                                        )
                                        productErrorString = getProductError(
                                            productDetailDataModelArrayList[productDetailDataModelArrayList.size - 1].price,
                                            productErrorString,
                                            getString(R.string.fill_price_previous),
                                            3
                                        )
                                        productErrorString = getProductError(
                                            productDetailDataModelArrayList[productDetailDataModelArrayList.size - 1].description,
                                            productErrorString,
                                            getString(R.string.fill_description_in_previous),
                                            4
                                        )

                                    }
                                }


                                if (dayErrornumber != 0 && dayErrorString.isNotEmpty()) {
                                    myCustomToast(dayErrorString)

                                }
                                else {
                                    if (productErrorNumber != 0 && productErrorString.isNotEmpty()) {
                                        myCustomToast(productErrorString)
                                    }
                                    else {
// day validations
                                        if (tvAddTraderDay.visibility==View.GONE) {
                                            selectDayGroup = JSONArray()
                                            for (i in 0 until dayTimeList.size) {
                                                val json = JSONObject()
                                                json.put("day_name", dayTimeList[i].selectedDay)
                                                json.put("time_from", dayTimeList[i].firstStarttime)
                                                json.put("time_to", dayTimeList[i].firstEndtime)
                                                json.put("secondStartTime", dayTimeList[i].secondStarttime)
                                                json.put(
                                                    "secondEndTime",
                                                    dayTimeList[i].secondEndtime
                                                )
                                                selectDayGroup.put(json)
                                            }
                                        }

                                        //  else if(tvAddTraderDay.visibility==View.VISIBLE) // day is emptyy
                                        //                {
                                        //                    typeEmpty = "1"
                                        //                }
                                        //                else if(tvAddTraderProduct.visibility==View.VISIBLE) // prduck is emptyyy
                                        //                {
                                        //                    typeEmpty = "3"
                                        //                }
                                     //   if (product) {
                                        if (tvAddTraderProduct.visibility==View.GONE) {
                                            productDetailsGroup = JSONArray()
                                            for (i in 0 until productDetailDataModelArrayList.size) {
                                                val json = JSONObject()
                                                json.put(
                                                    "image",
                                                    productDetailDataModelArrayList[i].image
                                                )
                                                json.put(
                                                    "title",
                                                    productDetailDataModelArrayList[i].title
                                                )
                                                json.put(
                                                    "price",
                                                    productDetailDataModelArrayList[i].price
                                                )
                                                json.put(
                                                    "description",
                                                    productDetailDataModelArrayList[i].description
                                                )
                                                productDetailsGroup.put(json)
                                            }
                                        }
                                        hitFinalTraderPostApi()

                                    }
                                }
                            }

                        }
                    }
                }
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

    private fun getDayError(dayFrom: String?, dayErrorString: String, s: String, i: Int): String {

        return when {
            dayFrom.isNullOrBlank() -> when {
                dayErrorString.isBlank() -> {
                    s
                }
                else -> dayErrorString
            }
            else -> {
                dayTime = true
                dayErrornumber = i
                dayErrorString
            }
        }
    }


    override fun getRealImagePath(imgPath: String?) {

        Log.d("selectedImagePath", "-------$imgPath")
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


    override fun ontraderItemClick(list: ArrayList<TraderProductMyAds>, pos: Int) {
        when {
            productDetailDataModelArrayList[list.size - 1].image.isEmpty() -> {
                myCustomToast(getString(R.string.select_image_in_previous))
            }
            productDetailDataModelArrayList[list.size - 1].title.isEmpty() -> {
                myCustomToast(getString(R.string.fill_title_previous))
            }
            productDetailDataModelArrayList[list.size - 1].price.isEmpty() -> {
                myCustomToast(getString(R.string.fill_price_previous))
            }
            productDetailDataModelArrayList[list.size - 1].description.isEmpty() -> {
                myCustomToast(getString(R.string.fill_description_in_previous))
            }
            else -> {
                productDetailDataModelArrayList.add(TraderProductMyAds("", 0, "", "", "", 0))
                productDetailRepeatAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onRemoveTraderProductItem(position: Int, list: Int) {
        txt_productdetails.visibility = View.GONE

        Log.d(EditTraderActivity::class.java.name, "TraderActivity_Remove_Position  " + position)

        Log.d(
            AddActivity::class.java.name,
            "OnRemoveClick   " + position + "    clickPosition   " + clickPosition + "   ListSize  " + list
        )

        clickPosition = position.toString()
        productDetailRepeatAdapter.notifyItemChanged(position)
        /*if (list == 0) {

//            dayTime = true
            product = false
            rv_product_details.visibility = View.GONE
            rv_product_details.visibility = View.GONE
            tvAddTraderProduct.visibility = View.VISIBLE
        } else {

        }*/
       // tvAddTraderProduct.visibility = View.VISIBLE
        if (productDetailDataModelArrayList.size == 0) {

            rv_product_details.visibility = View.GONE
            tvAddTraderProduct.visibility = View.VISIBLE
            product = false
        } else {
            product = true
        }

        Log.d(
            EditTraderActivity::class.java.name,
            "EditTraderActivity_productlist   " + productDetailDataModelArrayList.size
        )
    }

    override fun addCameraGalleryImage(list: java.util.ArrayList<TraderProductMyAds>, pos: Int) {
        productPhotoPosition = pos
        imageSelectedType = "4"
        checkPermission(this)
    }

    override fun dayTimeAdd(list: java.util.ArrayList<DayTimeModel>, position: Int) {
        when {
            dayTimeList[list.size - 1].selectedDay!!.isEmpty() -> {
                myCustomToast(getString(R.string.select_day_previous))
            }
            /* dayTimeList[list.size - 1].firstStarttime!!.isEmpty() -> {
                 myCustomToast(getString(R.string.select_morning_time_previous))
             }
             dayTimeList[list.size - 1].firstEndtime!!.isEmpty() -> {
                 myCustomToast(getString(R.string.select_morning_time_previous))
             }
             dayTimeList[list.size - 1].secondStarttime!!.isEmpty() -> {
                 myCustomToast(getString(R.string.select_evening_time_previous))
             }
             dayTimeList[list.size - 1].secondEndtime!!.isEmpty() -> {
                 myCustomToast(getString(R.string.select_evening_time_previous))
             }*/
            else -> {
                dayTimeList.add(DayTimeModel("", "", "", "", ""))
                dayTimeAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onSingleTraderDay(list: java.util.ArrayList<DayTimeModel>, position: Int) {

    }

    override fun onRemoveTraderDay(position: Int, list: Int) {
        Log.d(
            EditTraderActivity::class.java.name,
            "OnRemoveClick_Age   " + position + "    clickPosition   " + clickPosition + "   ListSize  " + list
        )


        clickPosition = position.toString()
        dayTimeAdapter.notifyItemChanged(position)

        if (list ==0) {

            rvDayTime!!.visibility = View.GONE
            tvAddTraderDay.visibility = View.VISIBLE
            dayTime = false
        } else {
            dayTime = true
        }
    }
}
