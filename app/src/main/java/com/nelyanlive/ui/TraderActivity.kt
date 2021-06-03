package com.nelyanlive.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.adapter.DayTimeRepeatAdapter
import com.nelyanlive.adapter.ProductDetailRepeatAdapter
import com.nelyanlive.data.network.responsemodels.trader_type.TraderTypeResponse
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.DayTimeModel
import com.nelyanlive.modals.ProductDetailDataModel
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_trader.*
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
import kotlin.coroutines.CoroutineContext

class TraderActivity : OpenCameraGallery(), View.OnClickListener, CoroutineScope, DayTimeRepeatAdapter.OnDayTimeRecyclerViewItemClickListner,
        ProductDetailRepeatAdapter.ProductRepeatListener {


    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    private var IMAGE_SELECTED_TYPE = ""
    private var IS_IMAGE_SELECTED = ""

    private val job by lazy { kotlinx.coroutines.Job() }
    private val dataStoragePreference by lazy { DataStoragePreference(this) }
    private lateinit var dayTimeRepeatAdapter: DayTimeRepeatAdapter
    private lateinit var productDetailRepeatAdapter: ProductDetailRepeatAdapter
    private var countryCodee = "+33"
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
    private var cityAddress = ""

    var rvDayTime: RecyclerView? = null
    var rvProductDetails: RecyclerView? = null
    private lateinit var dayTimeModelArrayList: ArrayList<DayTimeModel>
    private lateinit var productDetailDataModelArrayList: ArrayList<ProductDetailDataModel>
    var traderType: String = ""
    var traderTypeId: String = "15"

    private var urlListingFromResponse: ArrayList<String> = ArrayList()
    private var addEventUrlListingResponse: ArrayList<String> = ArrayList()
    private var imagePathList = ArrayList<MultipartBody.Part>()
    private var imagePathList2 = ArrayList<MultipartBody.Part>()


    var imageVideoUrlListing = arrayListOf("", "", "", "", "")

    private var imageVideoListPosition = -1
    private var selectedUrlListing: ArrayList<String> = ArrayList()
    private var savedaddEventImagesData = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trader)
        initalizeClicks()
        rvDayTime = findViewById(R.id.rvDayTime)
        rvProductDetails = findViewById(R.id.rv_product_details)


        countycode.setOnCountryChangeListener {
            countryCodee = countycode.selectedCountryCode.toString()
        }

        dayTimeRepeatAdapter = DayTimeRepeatAdapter(this, dayTimeModelArrayList, this)
        rvDayTime!!.adapter = dayTimeRepeatAdapter


        productDetailRepeatAdapter = ProductDetailRepeatAdapter(this, productDetailDataModelArrayList, this@TraderActivity )
        rvProductDetails!!.layoutManager = LinearLayoutManager(this)
        rvProductDetails!!.adapter = productDetailRepeatAdapter


        hitTypeTradeActivity_Api()
        traderTypeResponse()

    }

    private fun initalizeClicks() {

        if (this::dayTimeModelArrayList.isInitialized) {
            dayTimeModelArrayList.clear()
        }
        else {
            dayTimeModelArrayList = ArrayList()
            dayTimeModelArrayList.add(DayTimeModel("", "", "", "", ""))
        }

        if (this::productDetailDataModelArrayList.isInitialized) {
            productDetailDataModelArrayList.clear()
        }
        else {
            productDetailDataModelArrayList = ArrayList()
            productDetailDataModelArrayList.add(ProductDetailDataModel("", "", "", "" ))
        }

        ivImg.setOnClickListener(this)
        ivImg1.setOnClickListener(this)
        ivImg2.setOnClickListener(this)
        ivImg3.setOnClickListener(this)
        ivplus.setOnClickListener(this)
        tv_address.setOnClickListener(this)
        btn_trader_submit.setOnClickListener(this)
        ivBack!!.setOnClickListener(this)

    }

    private fun showPlacePicker() {
        // Initialize Places.
        Places.initialize(applicationContext, googleMapKey)
        // Create a new Places client instance.
        val placesClient: PlacesClient = Places.createClient(this)
        // Set the fields to specify which types of place data to return.
        val fields: List<Place.Field> = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === PLACE_PICKER_REQUEST) {
            if (resultCode === Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)

                cityName = place.name.toString()
                tv_address.text = cityName.toString()

                // cityID = place.id.toString()
                cityLatitude = place.latLng?.latitude.toString()
                cityLongitude = place.latLng?.longitude.toString()

                Log.i("dddddd", "Place: " + place.name + ", " + place.id + "," + place.address + "," + place.latLng)
            }
            else if (resultCode === AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("dddddd", status.statusMessage.toString())
            }
            else if (resultCode === Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i("dddddd", "-------Operation is cancelled ")
            }
        }


    }


    private fun makeJsonArray() {
        // for age group listing cards
        for (i in 0 until dayTimeModelArrayList.size) {
            val json = JSONObject()
            json.put("day_name", dayTimeModelArrayList[i].selectedDay)
            json.put("time_from", dayTimeModelArrayList[i].firstStarttime)
            json.put("time_to", dayTimeModelArrayList[i].firstEndtime)
            json.put("secondStartTime", dayTimeModelArrayList[i].secondStarttime)
            json.put("secondEndTime", dayTimeModelArrayList[i].secondEndtime)
            selectDayGroup.put(json)
        }

    }

    private fun makeProductJsonArray() {
        // for age group listing cards
        for (i in 0 until productDetailDataModelArrayList.size) {
            val json = JSONObject()
            json.put("image", productDetailDataModelArrayList[i].image)
            json.put("title", productDetailDataModelArrayList[i].productTitle)
            json.put("price", productDetailDataModelArrayList[i].productPrice)
            json.put("description", productDetailDataModelArrayList[i].description)
            productDetailsGroup.put(json)
        }

    }

    private fun gettingURLOfEventImages() {
        if (imagePathList2 != null) {
            imagePathList2.clear()
        }

        for (i in 0 until productDetailDataModelArrayList.size) {
            val media = productDetailDataModelArrayList[i].image
            if (!media.isNullOrEmpty()) {
                var mfile: File? = null
                mfile = File(media)
                val imageFile: RequestBody? = mfile.asRequestBody("image/*".toMediaTypeOrNull())
                var photo: MultipartBody.Part? = null
                photo = MultipartBody.Part.createFormData("image", mfile.name, imageFile!!)
                imagePathList2.clear()
                imagePathList2.add(photo)
            }
        }

        Log.d("imagePathLsiting", "-------------" + imagePathList2.size)

        val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
        val type = "image".toRequestBody("text/plain".toMediaTypeOrNull())
        appViewModel.sendUploadImageData(type, users, imagePathList2)
        progressDialog.setProgressDialog()
    }


    private fun hitTypeTradeActivity_Api() {
        if (checkIfHasNetwork(this@TraderActivity)) {
            val authkey = AllSharedPref.restoreString(this, "auth_key")
            appViewModel.sendTraderTypeTraderData(security_key, authkey)
        } else {
            showSnackBar(this@TraderActivity, getString(R.string.no_internet_error))
        }
    }

    private fun hitFinalTraderPostApi() {
        if (traderType == "Doctor") {
            traderTypeId = "15"
        } else if (traderType == "Food Court") {
            traderTypeId = "16"
        } else if (traderType == "Plant Nursury") {
            traderTypeId = "17"
        } else if (traderType == "Tutor Mathematics") {
            traderTypeId = "18"
        } else if (traderType == "Gym Trainer") {
            traderTypeId = "19"
        } else if (traderType == "Yoga Trainer") {
            traderTypeId = "20"
        } else if (traderType == "Gadget Repair") {
            traderTypeId = "21"
        }

        if (checkIfHasNetwork(this@TraderActivity)) {
            val authkey = AllSharedPref.restoreString(this, "auth_key")
            appViewModel.send_addPostTraderData(security_key, authkey, "3", traderTypeId, et_trader_shop_name.text.toString().trim(),
                    et_description_trader.text.toString().trim(), countryCodee, et_trader_phone.text.toString().trim(), tv_address.text.toString().trim(),
                    cityName, cityLatitude, cityLongitude, et_trader_email.text.toString(), et_web_address.text.toString(), selectDayGroup.toString(),
                    productDetailsGroup.toString(), media.toString())
        } else {
            showSnackBar(this@TraderActivity, getString(R.string.no_internet_error))

        }
    }

    private fun hitApiForBannerImages(position: Int) {

        imageVideoListPosition = position

        if (imageVideoListPosition <= selectedUrlListing.size - 1) {

            val media = selectedUrlListing[imageVideoListPosition]

            if (!media.isNullOrEmpty()) {

                savedaddEventImagesData = false

                val mfile: File?
                mfile = File(media)
                val imageFile: RequestBody = mfile.asRequestBody("image/*".toMediaTypeOrNull())
                var photo: MultipartBody.Part? = null
                photo = MultipartBody.Part.createFormData("image", mfile.name, imageFile)
                imagePathList.clear()
                imagePathList.add(photo)

                val type: RequestBody = "image".toRequestBody("text/plain".toMediaTypeOrNull())

                val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
                appViewModel.sendUploadImageData(type, users, imagePathList)
                progressDialog.setProgressDialog()
            } else {

                imageVideoListPosition += 1

                if (imageVideoListPosition <= selectedUrlListing.size) {
                    hitApiForBannerImages(imageVideoListPosition)
                }
            }

        } else {

            savedaddEventImagesData = true
            gettingURLOfEventImages()



        }
    }


    private fun traderTypeResponse() {

        appViewModel.observeActivityTypeTraderResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {

                if (response.body() != null) {

                    val res = Gson().fromJson(response.body().toString(), TraderTypeResponse::class.java)
                    val arrayAdapter1 = ArrayAdapter<Any?>(this, R.layout.customspinner, res.data)
                    trader_type.adapter = arrayAdapter1
                }
            } else {
                ErrorBodyResponse(response, this, null)
            }
        })

        appViewModel.observeAddPostTraderResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("addPostTraderResopnse", "-----------" + Gson().toJson(response.body()))
                if (response.body() != null) {
                    response.body()
                    progressDialog.hidedialog()
                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    val postid = jsonObject.getJSONObject("data").get("postId").toString()
                    val categoryId = jsonObject.getJSONObject("data").get("categoryId").toString()
                    finishAffinity()

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
                        if (!savedaddEventImagesData) {
                            urlListingFromResponse.add(response.body()!!.data!![0].image.toString())
                            if (imageVideoListPosition <= 4) {
                                imageVideoListPosition += 1
                                hitApiForBannerImages(imageVideoListPosition)
                            }

                        } else {
                            // response for addevent images data
                            if (addEventUrlListingResponse != null) {
                                addEventUrlListingResponse.clear()
                            }

                            for (element in response.body()!!.data!!) {
                                addEventUrlListingResponse.add(element.image.toString())
                            }

                            // now making json format for upper images media
                            for (i in 0 until urlListingFromResponse.size) {
                                val json = JSONObject()
                                json.put("image", urlListingFromResponse[i])
                                media.put(json)
                            }

                            // json format for select day
                            makeJsonArray()

                            // for make json product details
                            makeProductJsonArray()

                            hitFinalTraderPostApi()

                        }

                        Log.d("urlListt", "-------------$urlListingFromResponse")
                        Log.d("addEventUrlListing", "-------------$addEventUrlListingResponse")

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



    private fun isValid(): Boolean {
        traderType = trader_type.selectedItem.toString()
        var check = false
        if (checkStringNull(IS_IMAGE_SELECTED))
            myCustomToast(getString(R.string.media_missing_error))
        else if (checkStringNull(traderType))
            myCustomToast(getString(R.string.trader_type_missing_error))
        else if (checkStringNull(et_trader_shop_name.text.toString()))
            myCustomToast(getString(R.string.shop_name_missing_error))
        else if (checkStringNull(et_description_trader.text.toString()))
            myCustomToast(getString(R.string.description_missing))
        else if (checkStringNull(et_trader_phone.text.toString()))
            myCustomToast(getString(R.string.phone_number_missing))
        else if (checkStringNull(et_trader_email.text.toString()))
            myCustomToast(getString(R.string.email_address_missing))
        else if (checkStringNull(et_trader_email.text.toString()))
            myCustomToast(getString(R.string.email_address_missing))
        else if (!Validation.checkEmail(et_trader_email.text.toString(), this))
            myCustomToast(getString(R.string.invalid_email_address))
        else
            check = true
        return check
    }

    fun checkStringNull(string: String?): Boolean {
        return string == null || string == "null" || string.isEmpty()
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.ivImg -> {
                IMAGE_SELECTED_TYPE = "1"
                checkPermission(this)
            }
            R.id.ivImg1 -> {
                IMAGE_SELECTED_TYPE = "2"
                checkPermission(this)
            }
            R.id.ivImg2 -> {
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
                    if (traderType == "" || traderType == getString(R.string.select)) {
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

                                            when {
                                                dayTimeModelArrayList[dayTimeModelArrayList.size-1].selectedDay.isNullOrEmpty() -> {
                                                    myCustomToast("Please select day")
                                                }
                                                dayTimeModelArrayList[dayTimeModelArrayList.size-1].firstStarttime.isNullOrEmpty() -> {
                                                    myCustomToast("Please select morning time")

                                                }
                                                dayTimeModelArrayList[dayTimeModelArrayList.size-1].firstEndtime.isNullOrEmpty() -> {
                                                    myCustomToast("Please select morning time")

                                                }
                                                dayTimeModelArrayList[dayTimeModelArrayList.size-1].secondStarttime.isNullOrEmpty() -> {
                                                    myCustomToast("Please select evening time")

                                                }
                                                dayTimeModelArrayList[dayTimeModelArrayList.size-1].secondEndtime.isNullOrEmpty() -> {
                                                    myCustomToast("Please select evening time")

                                                }
                                                else -> {

                                                    when {
                                                        productDetailDataModelArrayList[productDetailDataModelArrayList.size-1].image.isNullOrEmpty() -> {
                                                            myCustomToast("Please select image")
                                                        }
                                                        productDetailDataModelArrayList[productDetailDataModelArrayList.size-1].productTitle.isNullOrEmpty() -> {
                                                            myCustomToast("Please enter product title")
                                                        }
                                                        productDetailDataModelArrayList[productDetailDataModelArrayList.size-1].productPrice.isNullOrEmpty() -> {
                                                            myCustomToast("Please enter product price")
                                                        }
                                                        productDetailDataModelArrayList[productDetailDataModelArrayList.size-1].description.isNullOrEmpty() -> {
                                                            myCustomToast("Please enter description")
                                                        }

                                                        else -> {

                                                            if (selectedUrlListing.size == urlListingFromResponse.size) {
                                                                selectedUrlListing.clear()
                                                                urlListingFromResponse.clear()
                                                            }

                                                            // for check upper images url from response
                                                            Log.d("imageVideoListSize", "-----------$imageVideoUrlListing")


                                                            // rotating loop
                                                            for (i in 0 until imageVideoUrlListing.size) {
                                                                val media = imageVideoUrlListing[i]
                                                                if (media.isNotEmpty()) {
                                                                    selectedUrlListing.add(media)
                                                                }
                                                            }

                                                            hitApiForBannerImages(0)
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
                }
            }

        }
    }

    override fun getRealImagePath(imgPath: String?) {
        when (IMAGE_SELECTED_TYPE) {

            "1" -> {
                setImageOnTab(imgPath, ivImg)
                imageVideoUrlListing.set(0, imgPath.toString())
                IMAGE_SELECTED_TYPE = ""
                IS_IMAGE_SELECTED = "1"
            }

            "2" -> {
                setImageOnTab(imgPath, ivImg1)
                imageVideoUrlListing.set(1, imgPath.toString())
                IMAGE_SELECTED_TYPE = ""
                IS_IMAGE_SELECTED = "1"
            }
            "3" -> {
                setImageOnTab(imgPath, ivImg2)
                imageVideoUrlListing.set(2, imgPath.toString())
                IMAGE_SELECTED_TYPE = ""
                IS_IMAGE_SELECTED = "1"
            }
            else -> {
                productDetailDataModelArrayList[productPhotoPosition].image = imgPath.toString()
                Log.d("lisufjdhf", "-----------$productDetailDataModelArrayList")
                productDetailRepeatAdapter.notifyDataSetChanged()
            }

        }
    }

    private fun setImageOnTab(imgPATH: String?, imageview: ImageView?) {
        Log.d("getimage", "---------" + imgPATH.toString())

        when (IMAGE_SELECTED_TYPE) {

            "1" -> {
                imageview?.scaleType = ImageView.ScaleType.FIT_XY
                checkVideoButtonVisibility(imgPATH.toString(), iv_video11)
            }
            "2" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video22)

            }
            "3" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video33)

            }
        }

        Glide.with(this).asBitmap().load(imgPATH).into(imageview!!)
    }


    private fun checkVideoButtonVisibility(imgpath: String, videoButton: ImageView) {

        if (imgpath.contains(".mp4")) {
            videoButton.visibility = View.VISIBLE
        } else {
            videoButton.visibility = View.GONE
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun dayTimeAdd(list: java.util.ArrayList<DayTimeModel>, position: Int) {


        when {
            dayTimeModelArrayList[list.size-1].selectedDay.isNullOrEmpty() -> {
                myCustomToast("please select day")
            }
            dayTimeModelArrayList[list.size-1].firstStarttime.isNullOrEmpty() -> {
                myCustomToast("please select Morning time")

            }
            dayTimeModelArrayList[list.size-1].firstEndtime.isNullOrEmpty() -> {
                myCustomToast("please select Morning time")

            }
            dayTimeModelArrayList[list.size-1].secondStarttime.isNullOrEmpty() -> {
                myCustomToast("please select Evening time")

            }
            dayTimeModelArrayList[list.size-1].secondEndtime.isNullOrEmpty() -> {
                myCustomToast("please select Evening time")

            }
            else -> {
                list.add(DayTimeModel("", "", "", "", ""))
                dayTimeRepeatAdapter.notifyDataSetChanged()
            }
        }



    }

    override fun onITEEMClick(productDetailDataModellist: java.util.ArrayList<ProductDetailDataModel>, pos: Int) {


        productDetailDataModelArrayList.add(ProductDetailDataModel("", "", "", ""))
                productDetailRepeatAdapter.notifyDataSetChanged()
    }

    override fun addCameraGelleryImage(list: ArrayList<ProductDetailDataModel>, position: Int) {
        productPhotoPosition = position
        checkPermission(this)
    }


}