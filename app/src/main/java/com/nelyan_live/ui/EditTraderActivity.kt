package com.nelyan_live.ui

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.datastore.preferences.core.preferencesKey
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
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.nelyan_live.R
import com.nelyan_live.adapter.DayTimeRepeatAdapter
import com.nelyan_live.adapter.EditProductDetailRepeatAdapter
import com.nelyan_live.adapter.ProductDetailRepeatAdapter
import com.nelyan_live.data.network.responsemodels.trader_type.TraderTypeResponse
import com.nelyan_live.db.DataStoragePreference
import com.nelyan_live.modals.DayTimeModel
import com.nelyan_live.modals.ProductDetailDataModel
import com.nelyan_live.modals.myAd.*
import com.nelyan_live.utils.*
import com.nelyan_live.adapter.VehicleTypeDialogboxAdapter

import kotlinx.android.synthetic.main.activity_trader.*
import kotlinx.android.synthetic.main.dialog_vehicle_type.*
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


    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
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
    private var cityAddress = ""
    private lateinit var dayTimeModelArrayList: ArrayList<TraderDaysTimingMyAds>
    private lateinit var productDetailDataModelArrayList: ArrayList<TraderProductMyAds>
    var traderType: String = ""
    var traderTypeId: String = ""

    private var urlListingFromResponse: ArrayList<String> = ArrayList()
    private var addEventUrlListingResponse: ArrayList<String> = ArrayList()
    private var imagePathList = ArrayList<MultipartBody.Part>()
    private var imagePathList2 = ArrayList<MultipartBody.Part>()
    private var isImageUploaded = ""
    private var imageVideoUrlListing = arrayListOf("", "", "")

    private var imageVideoListPosition = -1
    private var selectedUrlListing: ArrayList<String> = ArrayList()
    private var savedaddEventImagesData = false
    private var traderProductsJsonArray: JSONArray = JSONArray()
    private var traderDaytimeJsonArray: JSONArray = JSONArray()
    private val traderdayTimeList by lazy { ArrayList<TraderDaysTimingMyAds>() }
    private val traderProductList by lazy { ArrayList<TraderProductMyAds>() }

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

        hitTypeTradeActivity_Api()
        traderTypeResponse()



    }

    private fun getModelTypeDialog() {
        try {
            val d = Dialog(this@EditTraderActivity)
            d.setCancelable(true)
            d.window?.setGravity(Gravity.CENTER)
            d.requestWindowFeature(Window.FEATURE_NO_TITLE)
            d.setContentView(R.layout.dialog_vehicle_type)
            d.textsuccess.setText("Select type")
            d.setCancelable(true)
            d.btnok.setOnClickListener{
                d.dismiss()
            }
            val layoutManager = LinearLayoutManager(this@EditTraderActivity, LinearLayoutManager.VERTICAL, false)
            d.rv_state_dialog.layoutManager = layoutManager
            d.rv_state_dialog.adapter = VehicleTypeDialogboxAdapter(this@EditTraderActivity, modelTypes, object : VehicleTypeDialogboxAdapter.onClickListener{
                override fun subcatClick(subcatName: String?,id: String) {
                    tv_trader_type.setText(subcatName)
                    traderTypeId = id
                    d.dismiss()
                }

            })
            d.setCanceledOnTouchOutside(true)
            d.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

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
            tv_address.setText(intent.getStringExtra("address"))

            countryCodee = intent.getStringExtra("countryCode").toString()
            countycode.setCountryForPhoneCode(countryCodee.toInt())

            postID = intent.getStringExtra("adID").toString()



            traderTypeId = intent.getStringExtra("traderTypeId").toString()

            if (traderTypeId.equals("15")) {
                traderType = "Doctor"
            } else if (traderTypeId.equals("16")) {
                traderType = "Food Court"
            } else if (traderTypeId.equals("17")) {
                traderType = "Plant Nursury"
            } else if (traderTypeId.equals("18")) {
                traderType = "Tutor Mathematics"
            } else if (traderTypeId.equals("19")) {
                traderType = "Gym Trainer"
            } else if (traderTypeId.equals("20")) {
                traderType = "Yoga Trainer"
            } else if (traderTypeId.equals("21")) {
                traderType = "Gadget Repair"
            }

            tv_trader_type.text = traderType

            val country: ArrayList<String?> = ArrayList()
            country.add(traderType)

            val arrayAdapte1 = ArrayAdapter<Any?>(this, R.layout.customspinner, country as List<String>)
            trader_type.adapter = arrayAdapte1
            if (!traderType.isNullOrEmpty()) {
                val spinnerPosition = arrayAdapte1.getPosition(traderType)
                trader_type.setSelection(spinnerPosition)
            }

            if (traderTypeId.isNullOrEmpty()) {
                hitTypeTradeActivity_Api()
            }


            tv_trader_type.setOnClickListener {
              getModelTypeDialog()
            }



            dayTimeModelArrayList = intent.getSerializableExtra("daytimeList") as ArrayList<TraderDaysTimingMyAds>
            productDetailDataModelArrayList = intent.getSerializableExtra("traderProductList") as ArrayList<TraderProductMyAds>
            makeJsonArray()

          /*  dayTimeRepeatAdapter = DayTimeRepeatAdapter(this, dayTimeModelArrayList, this)
            rvDayTime!!.adapter = dayTimeRepeatAdapter
*/

            makeProductJsonArray()
            productDetailRepeatAdapter = EditProductDetailRepeatAdapter(this, productDetailDataModelArrayList, this)
            rv_product_details!!.setLayoutManager(LinearLayoutManager(this))
            rv_product_details!!.setAdapter(productDetailRepeatAdapter)


            traderimageList = intent.getSerializableExtra("traderImageList")as ArrayList<TradersimageMyAds>

            if (!traderimageList.isNullOrEmpty()) {
                if (traderimageList.size == 1) {
                    image1 = traderimageList.get(0).images
                    image2 = ""
                    image3 = ""
                    Glide.with(this).load(image_base_URl + traderimageList.get(0).images).error(R.mipmap.no_image_placeholder).into(ivImg)

                } else if (traderimageList.size == 2) {
                    image1 = traderimageList.get(0).images
                    image2 = traderimageList.get(1).images
                    image3 = ""
                    Glide.with(this).load(image_base_URl + traderimageList.get(0).images).error(R.mipmap.no_image_placeholder).into(ivImg)
                    Glide.with(this).load(image_base_URl + traderimageList.get(1).images).error(R.mipmap.no_image_placeholder).into(ivImg1)

                } else if (traderimageList.size == 3) {
                    image1 = traderimageList.get(0).images
                    image2 = traderimageList.get(1).images
                    image3 = traderimageList.get(2).images
                    Glide.with(this).load(image_base_URl + traderimageList.get(0).images).error(R.mipmap.no_image_placeholder).into(ivImg)
                    Glide.with(this).load(image_base_URl + traderimageList.get(1).images).error(R.mipmap.no_image_placeholder).into(ivImg1)
                    Glide.with(this).load(image_base_URl + traderimageList.get(2).images).error(R.mipmap.no_image_placeholder).into(ivImg2)

                }
            }
        }
        launch(Dispatchers.Main.immediate) {
            authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
        }
    }

    private fun showPlacePicker() {
        // Initialize Places.
        Places.initialize(applicationContext, googleMapKey)
        // Create a new Places client instance.
        val placesClient: PlacesClient = Places.createClient(this)
        // Set the fields to specify which types of place data to return.
        val fields: List<Place.Field> = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === PLACE_PICKER_REQUEST) {
            if (resultCode === Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)

                cityName = place.name.toString()
                tv_address.setText(cityName.toString())

                // cityID = place.id.toString()
                cityLatitude = place.latLng?.latitude.toString()
                cityLongitude = place.latLng?.longitude.toString()

                Log.i(
                        "dddddd",
                        "Place: " + place.name + ", " + place.id + "," + place.address + "," + place.latLng
                )
            } else if (resultCode === AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("dddddd", status.getStatusMessage().toString())
            } else if (resultCode === Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i("dddddd", "-------Operation is cancelled ")
            }
        }


    }


    private fun makeJsonArray() {
        // for age group listing cards
        for (i in 0..dayTimeModelArrayList.size - 1) {
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
        // for age group listing cards
        for (i in 0..productDetailDataModelArrayList.size - 1) {
            val json = JSONObject()
            json.put("image", productDetailDataModelArrayList[i].image)
            json.put("title", productDetailDataModelArrayList[i].title)
            json.put("price", productDetailDataModelArrayList[i].price)
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
                photo = MultipartBody.Part.createFormData("image", mfile?.name, imageFile!!)
                imagePathList2.add(photo)
            }
        }

        Log.d("imagePathLsiting", "-------------" + imagePathList2!!.size)

        val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
        val type = "image".toRequestBody("text/plain".toMediaTypeOrNull())
        appViewModel.sendUploadImageData(type, users, imagePathList2)
        progressDialog.setProgressDialog()
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

    private fun hitApiForBannerImages(position: Int) {

        imageVideoListPosition = position

        if (imageVideoListPosition <= selectedUrlListing.size - 1) {

            val media = selectedUrlListing[imageVideoListPosition]

            // if (!media.isNullOrEmpty()) {

//                var mfile: File? = null
//
//                mfile = File(selectedUrlListing)
//                val imageFile: RequestBody? = mfile.asRequestBody("image/*".toMediaTypeOrNull())
//                var photo: MultipartBody.Part? = null
//                photo = MultipartBody.Part.createFormData("image", mfile?.name, imageFile!!)
//                imagePathList.add(photo)

            if (imagePathList != null) {
                imagePathList!!.clear()
            } else {
                imagePathList = java.util.ArrayList()

            }


            var mfile: File? = null
            for (i in 0..selectedUrlListing!!.size - 1) {
                mfile = File(selectedUrlListing!![i])
                val imageFile: RequestBody? = RequestBody.create("image/*".toMediaTypeOrNull(), mfile)
                var photo: MultipartBody.Part? = null
                photo = MultipartBody.Part.createFormData("image", mfile?.name, imageFile!!)
                imagePathList!!.add(photo)
            }


            var type: RequestBody = "image".toRequestBody("text/plain".toMediaTypeOrNull())
            /*
            if (media.contains(".mp4")) {
                type = "video".toRequestBody("text/plain".toMediaTypeOrNull())
            } else {
                type = "image".toRequestBody("text/plain".toMediaTypeOrNull())
            }

            Log.e("imageimage", type.toString())
            */
            val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
            appViewModel.sendUploadImageData(type, users, imagePathList)
            progressDialog.setProgressDialog()
            //}

            /*
             else {


                imageVideoListPosition = imageVideoListPosition + 1

                if (imageVideoListPosition <= selectedUrlListing.size) {
                    hitApiForBannerImages(imageVideoListPosition)
                }
            }
            */

        } else {

            savedaddEventImagesData = true
            gettingURLOfEventImages()

            /* urlListingFromResponse.clear()
             addEventUrlListingResponse.clear()
             selectedUrlListing.clear()
             imagePathList.clear()
             savedaddEventImagesData = true
             gettingURLOfEventImages()
             */

        }
    }


    private fun traderTypeResponse() {
        appViewModel.observeActivityTypeTraderResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {

                if (response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    val mSizeOfData = jsonObject.getJSONArray("data").length()

                    traderTypeJsonArray = jsonObject.getJSONArray("data")


                    for (i in 0 until mSizeOfData) {
                        val id = traderTypeJsonArray.getJSONObject(i).get("id").toString().toInt()
                        val name = traderTypeJsonArray.getJSONObject(i).get("name").toString()

                        modelTypes!!.add(TraderTypeResponse.Data( id,  name))

                    }

                    Log.d("tttt", modelTypes.toString())
                        //  modelTypes = jsonArray as ArrayList<TraderTypeResponse.Data>
                    /*  val jsonObject = JSONObject(response.body().toString())
                      var jsonArray = jsonObject.getJSONArray("data")
                      val trader: ArrayList<String?> = ArrayList()
                      trader.add("")
                      for (i in 0 until jsonArray.length()) {
                          val name = jsonArray.getJSONObject(i).get("name").toString()
                          trader.add(name)
                      }*/
                    /*val res = Gson().fromJson(response.body().toString(), TraderTypeResponse::class.java)
                    val arrayAdapter1 = ArrayAdapter<Any?>(this, R.layout.customspinner, res.data)
                    trader_type.adapter = arrayAdapter1*/
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
                    val mResponse = response.body().toString()
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
                                addEventUrlListingResponse!!.clear()
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
            R.id.ivImg -> {
                clickImg1 = true
                clickImg2 = false
                clickImg3 = false
                IMAGE_SELECTED_TYPE = "1"
                checkPermission(this)
            }
            R.id.ivImg1 -> {
                clickImg1 = false
                clickImg2 = true
                clickImg3 = false
                IMAGE_SELECTED_TYPE = "2"
                checkPermission(this)
            }
            R.id.ivImg2 -> {
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

                if (IS_IMAGE_SELECTED.equals("")) {
                    myCustomToast(getString(R.string.media_missing_error))
                } else {
                    if (traderType.equals("") || traderType.equals(getString(R.string.select))) {
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
                                            // adding values

                                            // checking the list
                                            if (selectedUrlListing.size.equals(urlListingFromResponse.size)) {
                                                selectedUrlListing.clear()
                                                urlListingFromResponse.clear()
                                            }

                                            // for check upper images url from response
                                            Log.d("imageVideoListSize", "-----------" + imageVideoUrlListing)

                                            // rotating loop
                                            for (i in 0..imageVideoUrlListing.size - 1) {
                                                val media = imageVideoUrlListing.get(i)
                                                if (!media.isEmpty()) {
                                                    selectedUrlListing.add(media)
                                                }
                                            }
                                            Log.d("selectedUpperimages", "-------------------" + selectedUrlListing.toString())

                                           /* if (isImageUploaded.equals("") && isImageUploaded != null) {
                                                hitApiForBannerImages(0)
                                            } else {*/
                                                hitFinalTraderPostApi()
                                           /* }*/

                                            /*
                                             if (checkedEvent) {
                                                 if (selectedUrlListing.size.equals(urlListingFromResponse.size)) {
                                                     // taking image url from uploadimage api response
                                                     imagePathList!!.clear()

                                                     checkAddEventImages = true
                                                     gettingURLOfEventImages()
                                                     if (imagePathList.size.equals(addEventUrlListingResponse.size)) {
                                                         // for age group listing cards
                                                         for (i in 0..listAgeGroupDataModel.size - 1) {
                                                             val json = JSONObject()
                                                             json.put("age_from", listAgeGroupDataModel[i].ageFrom)
                                                             json.put("age_to", listAgeGroupDataModel[i].ageTo)
                                                             json.put("days", listAgeGroupDataModel[i].days)
                                                             json.put("time_from", listAgeGroupDataModel[i].timeFrom)
                                                             json.put("time_to", listAgeGroupDataModel[i].timeTo)
                                                             ageGroup.put(json)
                                                         }

                                                         hitApi(activityType, shopName, activityName, description, message, phone, cityAddress, cityLatitude, cityLongitude, cityName)
                                                     }
                                                 }
                                             }*/

                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }

/*
            R.id.ivImg3 -> {
                IMAGE_SELECTED_TYPE = "4"
                checkPermission(this)
            }
            R.id.ivplus -> {
                IMAGE_SELECTED_TYPE = "5"
                checkPermission(this)
            }
*/
        }
    }

    override fun getRealImagePath(imgPath: String?) {
        when (IMAGE_SELECTED_TYPE) {

            "1" -> {
                setImageOnTab(imgPath, ivImg)
                imageVideoUrlListing.set(0, imgPath.toString())
                // IMAGE_SELECTED_TYPE = ""
                IS_IMAGE_SELECTED = "1"
            }

            "2" -> {
                setImageOnTab(imgPath, ivImg1)
                imageVideoUrlListing.set(1, imgPath.toString())
                // IMAGE_SELECTED_TYPE = ""
                IS_IMAGE_SELECTED = "1"
            }
            "3" -> {
                setImageOnTab(imgPath, ivImg2)
                imageVideoUrlListing.set(2, imgPath.toString())
                //  IMAGE_SELECTED_TYPE = ""
                IS_IMAGE_SELECTED = "1"
            }
            "4" -> {
                productDetailDataModelArrayList[productPhotoPosition].image = imgPath.toString()
                Log.d("lisufjdhf", "-----------" + productDetailDataModelArrayList.toString())
                productDetailRepeatAdapter.notifyDataSetChanged()
            }

/*
            "4" -> {
                setImageOnTab(imgPath, ivImg3)
            }

            "5" -> {
                setImageOnTab(imgPath, ivplus)
            }
*/

        }
    }


    private fun setImageOnTab(imgPATH: String?, imageview: ImageView?) {
        Log.d("getimage", "---------" + imgPATH.toString())

       /* when (IMAGE_SELECTED_TYPE) {

            "1" -> {
                imageview?.setScaleType(ImageView.ScaleType.FIT_XY)
                checkVideoButtonVisibility(imgPATH.toString(), iv_video11)
            }
            "2" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video22)

            }
            "3" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video33)

            }
*//*
            "4" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video44)

            }
            "5" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video55)
            }
*//*
        }*/

        Glide.with(this).asBitmap().load(imgPATH).into(imageview!!)
        hitImageUploadApi(imgPATH)
    }

    //manu code..........................................

    fun hitImageUploadApi(imgPATH: String?) {
        var mfile: File? = null
        mfile = File(imgPATH)
        val imageFile: RequestBody? = mfile.asRequestBody("image/*".toMediaTypeOrNull())
        var photo: MultipartBody.Part? = null
        photo = MultipartBody.Part.createFormData("image", mfile?.name, imageFile!!)
        imagePathList.add(photo)
        var type: RequestBody
        type = "image".toRequestBody("text/plain".toMediaTypeOrNull())

        val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
        appViewModel.sendUploadImageData(type, users, imagePathList)
        progressDialog.setProgressDialog()
    }



    private fun checkVideoButtonVisibility(imgpath: String, videoButton: ImageView) {

        if (imgpath?.contains(".mp4")!!) {
            videoButton.visibility = View.VISIBLE
        } else {
            videoButton.visibility = View.GONE
        }
    }


/*
    companion object {
        var imgtype: String? = null
        var imasgezpos: String? = null
    }*/


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

    override fun addCameraGelleryImage(list: java.util.ArrayList<TraderProductMyAds>, pos: Int) {
        productPhotoPosition = pos
        IMAGE_SELECTED_TYPE = "4"
        checkPermission(this)
    }


}