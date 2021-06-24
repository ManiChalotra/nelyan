package com.nelyanlive.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
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
import com.nelyanlive.data.network.responsemodels.trader_type.TraderTypeResponse
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.DayTimeModel
import com.nelyanlive.modals.myAd.*
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_trader.*
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

class EditTraderActivity : OpenCameraGallery(), View.OnClickListener, CoroutineScope, DayTimeRepeatAdapter.OnDayTimeRecyclerViewItemClickListner,
    EditProductDetailRepeatAdapter.ProductRepeatListener {


    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(
        AppViewModel::class.java) }
    private var IMAGE_SELECTED_TYPE = ""
    private var IS_IMAGE_SELECTED = ""

    private val job by lazy { kotlinx.coroutines.Job() }
    private val dataStoragePreference by lazy { DataStoragePreference(this) }
    private lateinit var dayTimeRepeatAdapter: DayTimeRepeatAdapter
    private lateinit var productDetailRepeatAdapter: EditProductDetailRepeatAdapter
    private var countryCodee = "91"
    private var productPhotoPosition = 0
    private var selectDayGroup: JSONArray = JSONArray()
    private var productDetailsGroup: JSONArray = JSONArray()
    private var media: JSONArray = JSONArray()


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
    private lateinit var productDetailDataModelArrayList: ArrayList<TraderProductMyAds>
    var traderType: String = ""
    var traderTypeId: String = ""

    private var urlListingFromResponse: ArrayList<String> = ArrayList()
    private var addEventUrlListingResponse: ArrayList<String> = ArrayList()
    private var imagePathList = ArrayList<MultipartBody.Part>()
    private var imageVideoUrlListing = arrayListOf("", "", "")

    private var imageVideoListPosition = -1
    private var selectedUrlListing: ArrayList<String> = ArrayList()
    private var savedaddEventImagesData = false

    var image1: String = ""
    var image2: String = ""
    var image3: String = ""

    var clickImg1: Boolean = false
    var clickImg2: Boolean = false
    var clickImg3: Boolean = false
    private var postID = ""
    private var authKey = ""
    private lateinit var traderimageList: ArrayList<TradersimageMyAds>

    var modelTypes:MutableList<TraderTypeResponse.Data>?=null
    private var traderTypeJsonArray: JSONArray = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trader)
        traderimageList = ArrayList()
        modelTypes = ArrayList()
        traderimageList = ArrayList()
        dayTimeModelArrayList = ArrayList()
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
        ivplus.setOnClickListener(this)
        tv_address.setOnClickListener(this)
        btn_trader_submit.setOnClickListener(this)
        ivBack!!.setOnClickListener(this)

        if (intent.extras != null) {
            et_trader_shop_name.setText(intent.getStringExtra("nameofShop").toString())
            et_trader_email.setText(intent.getStringExtra("email"))
            et_web_address.setText(intent.getStringExtra("website"))
            et_description_trader.setText(intent.getStringExtra("description"))
            et_trader_phone.setText(intent.getStringExtra("phoneNumber"))
            tv_address.text = intent.getStringExtra("address")

            countryCodee = intent.getStringExtra("countryCode").toString()
            countycode.setCountryForPhoneCode(countryCodee.toInt())

            postID = intent.getStringExtra("adID").toString()



            traderTypeId = intent.getStringExtra("traderTypeId").toString()


            hitTypeTradeActivity_Api()

            dayTimeModelArrayList = intent.getSerializableExtra("daytimeList") as ArrayList<TraderDaysTimingMyAds>
            productDetailDataModelArrayList = intent.getSerializableExtra("traderProductList") as ArrayList<TraderProductMyAds>
            makeJsonArray()

            makeProductJsonArray()
            productDetailRepeatAdapter = EditProductDetailRepeatAdapter(this, productDetailDataModelArrayList, this)
            rv_product_details!!.layoutManager = LinearLayoutManager(this)
            rv_product_details!!.adapter = productDetailRepeatAdapter


            traderimageList = intent.getSerializableExtra("traderImageList")as ArrayList<TradersimageMyAds>

            if (!traderimageList.isNullOrEmpty()) {
                when (traderimageList.size) {
                    1 -> {
                        image1 = traderimageList[0].images
                        image2 = ""
                        image3 = ""
                        Glide.with(this).load(image_base_URl + traderimageList[0].images).error(R.mipmap.no_image_placeholder).into(ivImg1)

                    }
                    2 -> {
                        image1 = traderimageList[0].images
                        image2 = traderimageList[1].images
                        image3 = ""
                        Glide.with(this).load(image_base_URl + traderimageList[0].images).error(R.mipmap.no_image_placeholder).into(ivImg1)
                        Glide.with(this).load(image_base_URl + traderimageList[1].images).error(R.mipmap.no_image_placeholder).into(ivImg2)

                    }
                    3 -> {
                        image1 = traderimageList[0].images
                        image2 = traderimageList[1].images
                        image3 = traderimageList[2].images
                        Glide.with(this).load(image_base_URl + traderimageList[0].images).error(R.mipmap.no_image_placeholder).into(ivImg1)
                        Glide.with(this).load(image_base_URl + traderimageList[1].images).error(R.mipmap.no_image_placeholder).into(ivImg2)
                        Glide.with(this).load(image_base_URl + traderimageList[2].images).error(R.mipmap.no_image_placeholder).into(ivImg)

                    }
                }
            }
        }
        launch(Dispatchers.Main.immediate) {
            authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
        }
    }

    private fun showPlacePicker() {
        Places.initialize(applicationContext, googleMapKey)
        val fields: List<Place.Field> = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)

                    cityName = place.name.toString()
                    tv_address.text = cityName

                    cityLatitude = place.latLng?.latitude.toString()
                    cityLongitude = place.latLng?.longitude.toString()

                    Log.i("dddddd", "Place: " + place.name + ", " + place.id + "," + place.address + "," + place.latLng)
                }
            } 
        }


    }


    private fun makeJsonArray() {
        for (i in 0 until dayTimeModelArrayList.size) {
            val json = JSONObject()
            json.put("day_name", dayTimeModelArrayList[i].day)
            json.put("time_from", dayTimeModelArrayList[i].startTime)
            json.put("time_to", dayTimeModelArrayList[i].endTime)
            json.put("secondStartTime", dayTimeModelArrayList[i].secondStartTime)
            json.put("secondEndTime", dayTimeModelArrayList[i].secondEndTime)
            selectDayGroup.put(json)
        }

    }

    private fun makeProductJsonArray() {
        for (i in 0 until productDetailDataModelArrayList.size) {
            val json = JSONObject()
            json.put("image", productDetailDataModelArrayList[i].image)
            json.put("title", productDetailDataModelArrayList[i].title)
            json.put("price", productDetailDataModelArrayList[i].price)
            json.put("description", productDetailDataModelArrayList[i].description)
            productDetailsGroup.put(json)
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
            appViewModel.send_editPostTraderData(security_key, authkey, "3", traderTypeId, et_trader_shop_name.text.toString().trim(),
                et_description_trader.text.toString().trim(), countryCodee, et_trader_phone.text.toString().trim(), tv_address.text.toString().trim(),
                cityName, cityLatitude, cityLongitude, et_trader_email.text.toString(), et_web_address.text.toString(), selectDayGroup.toString(),
                productDetailsGroup.toString(), image1, image2, image3, postID)
        } else {
            showSnackBar(this@EditTraderActivity, getString(R.string.no_internet_error))

        }
    }


    private val typeList: ArrayList<String> = ArrayList()
    private val typeListId: ArrayList<String> = ArrayList()
    private var selectedPosition = 0

    private fun traderTypeResponse() {
        appViewModel.observeActivityTypeTraderResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {

                if (response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    val mSizeOfData = jsonObject.getJSONArray("data").length()

                    traderTypeJsonArray = jsonObject.getJSONArray("data")

                    typeList.clear()
                    typeListId.clear()
                    typeList.add("")
                    typeListId.add("")
                    for (i in 0 until mSizeOfData) {
                        val name = traderTypeJsonArray.getJSONObject(i).get("name").toString()
                        val id = traderTypeJsonArray.getJSONObject(i).get("id").toString()
                        typeList.add(name)
                        typeListId.add(id)
                        if(id==traderTypeId)
                        {
                            selectedPosition = i
                        }
                    }
                    val arrayAdapte1 = ArrayAdapter(this, R.layout.customspinner, typeList)
                    trader_type.adapter = arrayAdapte1
                    trader_type.setSelection(selectedPosition+1)

                    trader_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        appViewModel.observeEditPostTraderResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("addPostTraderResopnse", "-----------" + Gson().toJson(response.body()))
                if (response.body() != null) {
                    response.body()
                    progressDialog.hidedialog()
                    finishAffinity()
                    myCustomToast("Post Updated Successfully")
                    OpenActivity(HomeActivity::class.java)

                }
            } else {
                progressDialog.hidedialog()
                ErrorBodyResponse(response, this, null)
            }
        })

        appViewModel.observeUploadImageResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("urlDataLoading", "------------" + Gson().toJson(response.body()))
                if (response.body() != null) {
                    if (response.body()!!.data != null) {

                        if(clickImg1==true){
                            image1 = response.body()!!.data!![0].image.toString()

                        }else if(clickImg2==true){
                            image2 = response.body()!!.data!![0].image.toString()

                        }else if(clickImg3==true){
                            image3 = response.body()!!.data!![0].image.toString()
                        }

                        progressDialog.hidedialog()

                        if (!savedaddEventImagesData) {
                            urlListingFromResponse.add(response.body()!!.data!![0].image.toString())
                            if (imageVideoListPosition <= 4) {
                                imageVideoListPosition = imageVideoListPosition + 1
                                //  hitApiForBannerImages(imageVideoListPosition)
                            }

                        } else {
                            // response for addevent images data
                            if (addEventUrlListingResponse != null) {
                                addEventUrlListingResponse.clear()
                            }

                            for (i in 0 until response.body()!!.data!!.size) {
                                addEventUrlListingResponse.add(response.body()!!.data!![i].image.toString())
                            }

                            // now making json format for upper images media
                            for (i in 0..urlListingFromResponse.size - 1) {
                                val json = JSONObject()
                                json.put("image", urlListingFromResponse[i])
                                media.put(json)
                            }


                            // json format for select day
                            // makeJsonArray()

                            // for make json product details
                            // makeProductJsonArray()

                            //  hitFinalTraderPostApi()

                        }

                        Log.d("urlListt", "-------------" + urlListingFromResponse.toString())
                        Log.d("addEventUrlListing", "-------------" + addEventUrlListingResponse.toString())

                    }
                } else {
                    ErrorBodyResponse(response, this, null)
                }
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
                clickImg1 = true
                clickImg2 = false
                clickImg3 = false
                IMAGE_SELECTED_TYPE = "1"
                checkPermission(this)
            }
            R.id.ivImg2 -> {
                clickImg1 = false
                clickImg2 = true
                clickImg3 = false
                IMAGE_SELECTED_TYPE = "2"
                checkPermission(this)
            }
            R.id.ivImg -> {
                clickImg1 = false
                clickImg2 = false
                clickImg3 = true
                IMAGE_SELECTED_TYPE = "3"
                checkPermission(this)
            }
            R.id.tv_address -> {
                showPlacePicker()
            }
            R.id.btn_trader_submit -> {
                val shopName = et_trader_shop_name.text.toString()
                traderType = trader_type.selectedItem.toString()
                val description = et_description_trader.text.toString()
                val phone = et_trader_phone.text.toString()
                val address = tv_address.text.toString()
                val email = et_trader_email.text.toString()

                if (IS_IMAGE_SELECTED == "") {
                    myCustomToast(getString(R.string.media_missing_error))
                } else {
                    if (traderTypeId == "") {
                        myCustomToast(getString(R.string.trader_type_missing_error))
                    } else {
                        if (shopName.isEmpty()) {
                            myCustomToast(getString(R.string.shop_name_missing_error))
                        } else {
                            if (description.isEmpty()) {
                                myCustomToast(getString(R.string.description_missing))
                            } else {
                                if (address.isEmpty()) {
                                    myCustomToast(getString(R.string.address_missing_error))
                                } else {
                                    if (phone.isEmpty()) {
                                        myCustomToast(getString(R.string.phone_number_missing))
                                    } else {
                                        if (email.isEmpty()) {
                                            myCustomToast(getString(R.string.email_address_missing))
                                        } else {

                                            // checking the list
                                            if (selectedUrlListing.size == urlListingFromResponse.size) {
                                                selectedUrlListing.clear()
                                                urlListingFromResponse.clear()
                                            }

                                            Log.d("imageVideoListSize", "-----------$imageVideoUrlListing")

                                            // rotating loop
                                            for (i in 0 until imageVideoUrlListing.size) {
                                                val media = imageVideoUrlListing[i]
                                                if (media.isNotEmpty()) {
                                                    selectedUrlListing.add(media)
                                                }
                                            }
                                            Log.d("selectedUpperimages",
                                                "-------------------$selectedUrlListing"
                                            )

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
    }

    override fun getRealImagePath(imgPath: String?) {
        when (IMAGE_SELECTED_TYPE) {

            "1" -> {
                setImageOnTab(imgPath, ivImg1)
                imageVideoUrlListing[0] = imgPath.toString()
                IS_IMAGE_SELECTED = "1"
            }

            "2" -> {
                setImageOnTab(imgPath, ivImg2)
                imageVideoUrlListing[1] = imgPath.toString()
                IS_IMAGE_SELECTED = "1"
            }
            "3" -> {
                setImageOnTab(imgPath, ivImg)
                imageVideoUrlListing[2] = imgPath.toString()
                IS_IMAGE_SELECTED = "1"
            }
            "4" -> {
                productDetailDataModelArrayList[productPhotoPosition].image = imgPath.toString()
                Log.d("lisufjdhf", "-----------$productDetailDataModelArrayList")
                productDetailRepeatAdapter.notifyDataSetChanged()
            }


        }
    }


    private fun setImageOnTab(imgPATH: String?, imageview: ImageView?) {
        Log.d("getimage", "---------" + imgPATH.toString())



        Glide.with(this).asBitmap().load(imgPATH).into(imageview!!)
        hitImageUploadApi(imgPATH)
    }


    fun hitImageUploadApi(imgPATH: String?) {
        val mFile: File?
        mFile = File(imgPATH!!)
        val imageFile: RequestBody = mFile.asRequestBody("image/*".toMediaTypeOrNull())
        val photo: MultipartBody.Part?
        photo = MultipartBody.Part.createFormData("image", mFile.name, imageFile)
        imagePathList.add(photo)
        val type: RequestBody = "image".toRequestBody("text/plain".toMediaTypeOrNull())

        val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
        appViewModel.sendUploadImageData(type, users, imagePathList)
        progressDialog.setProgressDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun dayTimeAdd(list: java.util.ArrayList<DayTimeModel>, position: Int) {
        list.add(DayTimeModel("", "", "", "", ""))
        dayTimeRepeatAdapter.notifyDataSetChanged()
    }

    override fun onITEEMClick(list: ArrayList<TraderProductMyAds>, pos: Int) {
        productDetailDataModelArrayList.add(TraderProductMyAds("", 0, "", "", "", 0))
        productDetailRepeatAdapter.notifyDataSetChanged()
    }

    override fun addCameraGalleryImage(list: java.util.ArrayList<TraderProductMyAds>, pos: Int) {
        productPhotoPosition = pos
        IMAGE_SELECTED_TYPE = "4"
        checkPermission(this)
    }


}
