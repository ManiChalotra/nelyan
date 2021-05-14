package com.nelyan_live.ui

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
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.nelyan_live.R
import com.nelyan_live.adapter.DayTimeRepeatAdapter
import com.nelyan_live.adapter.ProductDetailRepeatAdapter
import com.nelyan_live.data.network.responsemodels.trader_type.TraderTypeResponse
import com.nelyan_live.db.DataStoragePreference
import com.nelyan_live.modals.DayTimeModel
import com.nelyan_live.modals.ProductDetailDataModel
import com.nelyan_live.utils.*

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
import java.util.*
import kotlin.collections.ArrayList
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


    /*
var mContext: Context? = null
var ivBack: ImageView? = null
var ivImg: ImageView? = null
var ivCam: ImageView? = null
var btnSubmit: Button? = null
var tvAdd: TextView? = null
var tvClock: TextView? = null
var edClo: TextView? = null
var edClo1: TextView? = null
var edClo2: TextView? = null
var edClo3: TextView? = null
var ivplus: ImageView? = null
var ivImg1: ImageView? = null
var ivImg2: ImageView? = null
var ivImg3: ImageView? = null
var ll_1: LinearLayout? = null
var ll_2: LinearLayout? = null
var ll_3: LinearLayout? = null
var orderby: Spinner? = null
var orderby1: Spinner? = null
var Recycler_scroll: RecyclerView? = null
var indicator: ScrollingPagerIndicator? = null
var rlAddImg: RelativeLayout? = null
var rlImg: RelativeLayout? = null


var dayTimeRepeatAdapter: DayTimeRepeatAdapter? = null
var returnItemView = 1
var image = HashMap<String, Bitmap>()
*/


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
        rvProductDetails!!.setLayoutManager(LinearLayoutManager(this))
        rvProductDetails!!.setAdapter(productDetailRepeatAdapter)


        hitTypeTradeActivity_Api()
        traderTypeResponse()

    }

    private fun initalizeClicks() {

        // initalize the lists
        if (this::dayTimeModelArrayList.isInitialized) {
            dayTimeModelArrayList.clear()

        } else {
            dayTimeModelArrayList = ArrayList()
            dayTimeModelArrayList.add(DayTimeModel("", "", "", "", ""))
        }

        // initalize the lists
        if (this::productDetailDataModelArrayList.isInitialized) {
            productDetailDataModelArrayList.clear()

        } else {
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
        for (i in 0..productDetailDataModelArrayList.size - 1) {
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
        if (checkIfHasNetwork(this@TraderActivity)) {
            val authkey = AllSharedPref.restoreString(this, "auth_key")
            appViewModel.sendTraderTypeTraderData(security_key, authkey)
        } else {
            showSnackBar(this@TraderActivity, getString(R.string.no_internet_error))
        }
    }

    private fun hitFinalTraderPostApi() {
        if (traderType.equals("Doctor")) {
            traderTypeId = "15"
        } else if (traderType.equals("Food Court")) {
            traderTypeId = "16"
        } else if (traderType.equals("Plant Nursury")) {
            traderTypeId = "17"
        } else if (traderType.equals("Tutor Mathematics")) {
            traderTypeId = "18"
        } else if (traderType.equals("Gym Trainer")) {
            traderTypeId = "19"
        } else if (traderType.equals("Yoga Trainer")) {
            traderTypeId = "20"
        } else if (traderType.equals("Gadget Repair")) {
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

                var mfile: File? = null
                mfile = File(media)
                val imageFile: RequestBody? = mfile.asRequestBody("image/*".toMediaTypeOrNull())
                var photo: MultipartBody.Part? = null
                photo = MultipartBody.Part.createFormData("image", mfile?.name, imageFile!!)
                imagePathList.add(photo)
                var type: RequestBody
                type = "image".toRequestBody("text/plain".toMediaTypeOrNull())

/*
                if (media.contains(".mp4")) {
                    type = "video".toRequestBody("text/plain".toMediaTypeOrNull())
                } else {
                    type = "image".toRequestBody("text/plain".toMediaTypeOrNull())
                }
*/
                val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
                appViewModel.sendUploadImageData(type, users, imagePathList)
                progressDialog.setProgressDialog()
            } else {

                imageVideoListPosition = imageVideoListPosition + 1

                if (imageVideoListPosition <= selectedUrlListing.size) {
                    hitApiForBannerImages(imageVideoListPosition)
                }
            }

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
                    /*  val jsonObject = JSONObject(response.body().toString())
                      var jsonArray = jsonObject.getJSONArray("data")
                      val trader: ArrayList<String?> = ArrayList()
                      trader.add("")
                      for (i in 0 until jsonArray.length()) {
                          val name = jsonArray.getJSONObject(i).get("name").toString()
                          trader.add(name)
                      }*/
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
                    var jsonObject = JSONObject(mResponse)
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
                                imageVideoListPosition = imageVideoListPosition + 1
                                hitApiForBannerImages(imageVideoListPosition)
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
                            makeJsonArray()

                            // for make json product details
                            makeProductJsonArray()

                            hitFinalTraderPostApi()

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

/*

        mContext = this
        orderby = findViewById(R.id.orderby)
        val country: MutableList<String?> = ArrayList()
        country.add("")
        country.add("USA")
        country.add("Japan")
        country.add("India")
        val arrayAdapte1: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.customspinner, country as List<Any?>)




        orderby!!.setAdapter(arrayAdapte1)
        ivBack = findViewById(R.id.ivBack)
        btnSubmit = findViewById(R.id.btnSubmit)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
        btnSubmit = findViewById(R.id.btnSubmit)
        btnSubmit!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@TraderActivity, com.nelyan_live.ui.TraderPreviewActivity::class.java)
            //  i.putExtra("nurActivity","nurFrag");
            startActivity(i)
        })



        ll_1 = findViewById(R.id.ll_1)
        ll_2 = findViewById(R.id.ll_2)
        ll_3 = findViewById(R.id.ll_3)
        ivImg = findViewById(R.id.ivImg)
        ivImg1 = findViewById(R.id.ivImg1)
        ivImg2 = findViewById(R.id.ivImg2)
        ivImg3 = findViewById(R.id.ivImg3)
        rlImg = findViewById(R.id.rlImg)
        rlImg!!.setOnClickListener(View.OnClickListener {
            imgtype = "0"
            image("all")
        })
        ivplus = findViewById(R.id.ivplus)
        rlAddImg = findViewById(R.id.rlAddImg)
        rlAddImg!!.setOnClickListener(View.OnClickListener {
            imgtype = "1"
            image("all")
        })
        ll_1!!.setOnClickListener(View.OnClickListener {
            imgtype = "2"
            image("all")
        })
        ll_2!!.setOnClickListener(View.OnClickListener {
            imgtype = "3"
            image("all")
        })
        ll_3!!.setOnClickListener(View.OnClickListener {
            imgtype = "4"
            image("all")
        })
        val arrayList: ArrayList<TimeModel?> = ArrayList()

        arrayList.add(TimeModel("", ""))
        val dayTimeModel = DayTimeModel(arrayList)
        val dayTimeModelArrayList: ArrayList<DayTimeModel> = ArrayList<DayTimeModel>()
        dayTimeModelArrayList.add(dayTimeModel)
        dayTimeRepeatAdapter = DayTimeRepeatAdapter(this, dayTimeModelArrayList,
                object : DayTimeRepeatListener {
                    override fun dayTimeAdd(pos: Int) {
                        val arrayList1: ArrayList<TimeModel?> = ArrayList()

                        arrayList1.add(TimeModel("", ""))
                        val dayTimeModel1 = DayTimeModel(arrayList1)
                        dayTimeModelArrayList.add(dayTimeModel1)
                        dayTimeRepeatAdapter!!.notifyDataSetChanged()
                    }

                    override fun timeAdd(pos: Int) {
                        val dayTimeModel1: DayTimeModel = dayTimeModelArrayList[pos]
                        dayTimeModel1.selectTime.add(TimeModel("", ""))
                        dayTimeRepeatAdapter!!.notifyDataSetChanged()
                    }
                })
        rvDayTime!!.setAdapter(dayTimeRepeatAdapter)
        rvDayTime!!.setLayoutManager(LinearLayoutManager(this))
        val adapter = DescriptionRepeatAdapter(this, image, this@TraderActivity, returnItemView)
        rvDesc!!.setLayoutManager(LinearLayoutManager(this))
        rvDesc!!.setAdapter(adapter)*/


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
                                                val media = imageVideoUrlListing[i]
                                                if (!media.isEmpty()) {
                                                    selectedUrlListing.add(media)
                                                }
                                            }

                                            // hitting api for upper 5 images
                                            hitApiForBannerImages(0)

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

        when (IMAGE_SELECTED_TYPE) {

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
/*
            "4" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video44)

            }
            "5" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video55)
            }
*/
        }

        Glide.with(this).asBitmap().load(imgPATH).into(imageview!!)
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

    override fun onITEEMClick(productDetailDataModellist: java.util.ArrayList<ProductDetailDataModel>, pos: Int) {
        productDetailDataModellist.add(ProductDetailDataModel("", "", "", ""))
        productDetailRepeatAdapter.notifyDataSetChanged()

    }

    override fun addCameraGelleryImage(list: ArrayList<ProductDetailDataModel>, position: Int) {
        productPhotoPosition = position
        checkPermission(this)
    }


}