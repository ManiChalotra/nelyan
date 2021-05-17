package com.nelyan_live.ui

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.nelyan_live.adapter.ActivityTypeDialogboxAdapter
import com.nelyan_live.adapter.AgeGroupEditAdapter
import com.nelyan_live.adapter.EventEditAdapter
import com.nelyan_live.data.network.responsemodels.trader_type.TraderTypeResponse
import com.nelyan_live.db.DataStoragePreference
import com.nelyan_live.modals.activity_type.ActivityTypeResponse
import com.nelyan_live.modals.myAd.ActivityimageMyAds
import com.nelyan_live.modals.myAd.AgeGroupMyAds
import com.nelyan_live.modals.myAd.EventMyAds
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.activity_addactivity.*
import kotlinx.android.synthetic.main.activity_addactivity.countycode
import kotlinx.android.synthetic.main.activity_addactivity.ivBack
import kotlinx.android.synthetic.main.activity_addactivity.ivImg
import kotlinx.android.synthetic.main.activity_addactivity.ivImg1
import kotlinx.android.synthetic.main.activity_addactivity.ivImg2
import kotlinx.android.synthetic.main.activity_addactivity.ivImg3
import kotlinx.android.synthetic.main.activity_addactivity.ivplus
import kotlinx.android.synthetic.main.activity_addactivity.rlImg
import kotlinx.android.synthetic.main.activity_addactivity.trader_type
import kotlinx.android.synthetic.main.activity_myadd.*
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

class EditActivity : OpenCameraGallery(), OnItemSelectedListener, View.OnClickListener,
        CoroutineScope, AgeGroupEditAdapter.OnAGeGroupRecyclerViewItemClickListner,
        EventEditAdapter.OnEventRecyclerViewItemClickListner {

    private val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java)
    }
    var modelTypes:MutableList<ActivityTypeResponse.Data>? =null

    private var IMAGE_SELECTED_TYPE: String? = null
    private var IS_IMAGE_SELECTED = ""

    private val job by lazy { kotlinx.coroutines.Job() }

    private val dataStoragePreference by lazy { DataStoragePreference(this@EditActivity) }

    // Initialize Places variables
    private val googleMapKey = "AIzaSyDQWqIXO-sNuMWupJ7cNNItMhR4WOkzXDE"
    private val PLACE_PICKER_REQUEST = 1
    private val addEventRequestcode = 2
    private var imagePathList = ArrayList<MultipartBody.Part>()
    private var imagePathList2 = ArrayList<MultipartBody.Part>()

    // json Array
    private var ageGroup: JSONArray = JSONArray()
    private var addEvent: JSONArray = JSONArray()
    private var media: JSONArray = JSONArray()

    private var activityTypeJsonArray: JSONArray = JSONArray()


    // dialo for progress
    private var progressDialog = ProgressDialog(this)

    // initalize the variables
    private lateinit var listAgeGroupDataModel: ArrayList<AgeGroupMyAds>
    private lateinit var listAddEventDataModel: ArrayList<EventMyAds>
    private lateinit var activityimageList: ArrayList<ActivityimageMyAds>

    private lateinit var ageGroupEditAdapter: AgeGroupEditAdapter
    private lateinit var eventEditAdapter: EventEditAdapter

    private var cityName = ""
    private var cityLatitude = ""
    private var cityLongitude = ""
    private var addressLatitude = ""
    private var addressLongitude = ""
    private var cityAddress = ""
    private var authKey = ""
    private var postID = ""

    private var eventPhotoPosition = 0
    private var urlListingFromResponse: ArrayList<String> = ArrayList()
    private var addEventUrlListingResponse: ArrayList<String>? = null

    var imageVideoUrlListing = arrayListOf("", "", "", )

    private var imageVideoListPosition = -1
    private var selectedUrlListing: ArrayList<String> = ArrayList()
    private var savedaddEventImagesData = false

    //saving values variables
    private var activity_type = ""
    private var activity_typeId = ""
    private var shop_name = ""
    private var activity_name = ""
    private var descp = ""
    private var messagee = ""
    private var phonee = ""
    private var countryCodee = ""

    private lateinit var updateEventList: ArrayList<EventMyAds>
    private var pos = -1

    var image1: String=""
    var image2: String=""
    var image3: String=""

    var clickImg1: Boolean= false
    var clickImg2: Boolean= false
    var clickImg3: Boolean= false

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addactivity)
        modelTypes = ArrayList()

        initalizeclicks()

        checkMvvmResponse()

        countycode.setOnCountryChangeListener {
            countryCodee = countycode.selectedCountryCode.toString()
        }

        tv_acticity_type.visibility = View.VISIBLE
        trader_type.visibility = View.GONE

        tv_acticity_type.setOnClickListener {
            getModelTypeDialog()
        }




/*
        trader_type.setOnItemClickListener { parent, view, position, id ->
            hitTypeActivity_Api()
        }*/
    }

   /* private fun hitDetailApi() {
        if (checkIfHasNetwork(this)) {
            val authorization = AllSharedPref.restoreString(this, "auth_key")
            appViewModel.getEditDetailData(security_key, authorization, "1", postID)
          //  myads_progressBar?.showProgressBar()
            //  checkMvvmResponse()
        } else {
            showSnackBar(this, getString(R.string.no_internet_error))
        }
    }*/

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun initalizeclicks() {

        if (intent.extras != null) {
            et_shopName.setText(intent.getStringExtra("nameofShop").toString())
            et_activityName.setText(intent.getStringExtra("nameofActivity"))
            et_description.setText(intent.getStringExtra("description"))
            et_phone.setText(intent.getStringExtra("phoneNumber"))
            et_addressActivity.setText(intent.getStringExtra("address"))

            countryCodee = intent.getStringExtra("countryCode").toString()
            countycode.setCountryForPhoneCode(countryCodee.toInt())
            addressLatitude= intent.getStringExtra("latitude").toString()
            addressLongitude= intent.getStringExtra("longitude").toString()
            
            postID = intent.getStringExtra("adID").toString()

            activity_typeId = intent.getStringExtra("activityTypeId").toString()

            if (activity_typeId.equals("5")) {
                activity_type = getString(R.string.sports)

            } else if (activity_typeId.equals("9")) {
                activity_type = getString(R.string.dance)

            } else if (activity_typeId.equals("10")) {
                activity_type = getString(R.string.drawing)

            } else if (activity_typeId.equals("11")) {
                activity_type = getString(R.string.zumba)

            } else if (activity_typeId.equals("13")) {
                activity_type = getString(R.string.tutor_mother_subject)

            }

            tv_acticity_type.setText(activity_type)

            val country: ArrayList<String?> = ArrayList()
            country.add(activity_type)

            val arrayAdapte1 = ArrayAdapter<Any?>(this, R.layout.customspinner, country as List<String>)
            trader_type.adapter = arrayAdapte1
            if (!activity_type.isNullOrEmpty() ) {
                val spinnerPosition = arrayAdapte1.getPosition(activity_type)
                trader_type.setSelection(spinnerPosition)
            }

            launch(Dispatchers.Main.immediate) {
                authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
            }

            hitTypeActivity_Api()



//            hitDetailApi()
        }

        // initalize the lists
        if (this::activityimageList.isInitialized) {
            activityimageList.clear()

        } else {
            activityimageList = ArrayList()
            activityimageList = intent.getParcelableArrayListExtra<ActivityimageMyAds>("activityimagesList")!!

            if (!activityimageList.isNullOrEmpty()) {
                if (activityimageList.size == 1) {
                    image1 = activityimageList.get(0).images
                    image2 = ""
                    image3 = ""
                    Glide.with(this).load(image_base_URl + activityimageList.get(0).images).error(R.mipmap.no_image_placeholder).into(ivImg)

                } else if (activityimageList.size == 2) {
                    image1 = activityimageList.get(0).images
                    image2 = activityimageList.get(1).images
                    image3 = ""
                    Glide.with(this).load(image_base_URl + activityimageList.get(0).images).error(R.mipmap.no_image_placeholder).into(ivImg)
                    Glide.with(this).load(image_base_URl + activityimageList.get(1).images).error(R.mipmap.no_image_placeholder).into(ivImg1)

                } else if (activityimageList.size == 3) {
                    image1 = activityimageList.get(0).images
                    image2 = activityimageList.get(1).images
                    image3 = activityimageList.get(2).images
                    Glide.with(this).load(image_base_URl + activityimageList.get(0).images).error(R.mipmap.no_image_placeholder).into(ivImg)
                    Glide.with(this).load(image_base_URl + activityimageList.get(1).images).error(R.mipmap.no_image_placeholder).into(ivImg1)
                    Glide.with(this).load(image_base_URl + activityimageList.get(2).images).error(R.mipmap.no_image_placeholder).into(ivImg2)

                }
            }


            /* ageGroupEditAdapter = AgeGroupEditAdapter(this, activityimageList, this)
             rvAgeGroup!!.adapter = ageGroupEditAdapter*/
        }

        if (this::listAgeGroupDataModel.isInitialized) {
            listAgeGroupDataModel.clear()

        } else {
            listAgeGroupDataModel = ArrayList()
            listAgeGroupDataModel = intent.getParcelableArrayListExtra<AgeGroupMyAds>("ageGroupList")!!

            ageGroupEditAdapter = AgeGroupEditAdapter(this, listAgeGroupDataModel, this)
            rvAgeGroup!!.adapter = ageGroupEditAdapter
        }


        if (this::listAddEventDataModel.isInitialized) {
            listAddEventDataModel.clear()

        } else {
            listAddEventDataModel = ArrayList()
            listAddEventDataModel = intent.getParcelableArrayListExtra<EventMyAds>("eventMyAdsList")!!
            eventEditAdapter = EventEditAdapter(this, listAddEventDataModel, this)
            rvEvent!!.adapter = eventEditAdapter
        }


        // clicks for images
        rlImg.setOnClickListener(this)
        ivImg1.setOnClickListener(this)
        ivImg2.setOnClickListener(this)
        ivImg3.setOnClickListener(this)
        ivplus.setOnClickListener(this)

        // clicks for buttons
        btnSubmit.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        //   et_addressActivity.isFocusable = false
        // et_addressActivity.isFocusableInTouchMode = false
        et_addressActivity.setOnClickListener(this)

    }

    private fun checkMvvmResponse() {
        /*//manu code..................
        appViewModel.observerEditDetailResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    var jsonArray = jsonObject.getJSONArray("data")

                }
            } else {
                ErrorBodyResponse(response, this, null)
            }
        })*/

        appViewModel.observeActivityTypeResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    val mSizeOfData = jsonObject.getJSONArray("data").length()

                    activityTypeJsonArray = jsonObject.getJSONArray("data")


                    for (i in 0 until mSizeOfData) {
                        val id = activityTypeJsonArray.getJSONObject(i).get("id").toString().toInt()
                        val name = activityTypeJsonArray.getJSONObject(i).get("name").toString()

                        modelTypes!!.add(ActivityTypeResponse.Data( id,  name))

                    }


                    /*  var jsonArray = jsonObject.getJSONArray("data")
                      val country: ArrayList<String?> = ArrayList()
                      country.add("")
                      for (i in 0 until jsonArray.length()) {
                          val name = jsonArray.getJSONObject(i).get("name").toString()
                          country.add(name)
                      }
                      val arrayAdapte1 = ArrayAdapter<Any?>(this, R.layout.customspinner, country as List<Any?>)
                      trader_type.adapter = arrayAdapte1
                  */}
            } else {
                ErrorBodyResponse(response, this, null)
            }
        })

        // add post for activity
        appViewModel.observe_editActivity_Response()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("editActivityResponse", "-----------" + Gson().toJson(response.body()))
                if (response.body() != null) {
                    response.body()
                    progressDialog.hidedialog()
                    val mResponse = response.body().toString()
                    var jsonObject = JSONObject(mResponse)
                    val message = jsonObject.get("msg").toString()
                    myCustomToast(message)
                    myCustomToast("Post updated successfully")

                    finishAffinity()
                    OpenActivity(HomeActivity::class.java)

                }
            } else {
                progressDialog.hidedialog()
                ErrorBodyResponse(response, this, null)
            }
        })

        // for imageUpload api
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
                            /*if (imageVideoListPosition <= 4) {
                                imageVideoListPosition = imageVideoListPosition + 1
                                hitApiForBannerImages(imageVideoListPosition)
                            }*/


                            /* // now making json format for upper images media
                             for (i in 0..urlListingFromResponse.size - 1) {
                                 val json = JSONObject()
                                 json.put("image", urlListingFromResponse[i])
                                 media.put(json)
                             }*/





                        } else {

                            // response for addevent images data
                            if (addEventUrlListingResponse != null) {
                                addEventUrlListingResponse!!.clear()
                            }

                            for (i in 0 until response.body()!!.data!!.size) {
                                addEventUrlListingResponse!!.add(response.body()!!.data!![i].image.toString())
                            }

                            // now making json format for upper images media
                            for (i in 0..urlListingFromResponse.size - 1) {
                                val json = JSONObject()
                                json.put("image", urlListingFromResponse[i])
                                media.put(json)
                            }


                            // json format for addEvent
                            for (i in 0..addEventUrlListingResponse!!.size - 1) {
                                val json = JSONObject()
                                json.put("image", addEventUrlListingResponse!![i])
                                json.put("name", listAddEventDataModel[i].name.toString())
                                json.put("file_type", "image")
                                // for(j in 0 .. i){
                                json.put("date_from", listAddEventDataModel[i].dateFrom.toString())
                                json.put("date_to", listAddEventDataModel[i].dateTo.toString())
                                json.put("time_from", listAddEventDataModel[i].startTime.toString())
                                json.put("time_to", listAddEventDataModel[i].endTime.toString())
                                json.put("description", listAddEventDataModel[i].description.toString())
                                json.put("price", listAddEventDataModel[i].price.toString())
                                json.put("city", listAddEventDataModel[i].city.toString())
                                json.put("lat", listAddEventDataModel[i].latitude.toString())
                                json.put("lng", listAddEventDataModel[i].longitude.toString())
                                //}
                                addEvent.put(json)
                            }


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

             //               hitFinallyActivityAddPostApi()
                        }

                        Log.d("urlListt", "-------------" + urlListingFromResponse.toString())
                        Log.d("addEventUrlListing", "-------------" + addEventUrlListingResponse.toString())

                    }
                } else {
                    ErrorBodyResponse(response, this, null)
                }
            }
        })

        appViewModel.getException()!!.observe(this, androidx.lifecycle.Observer {
            myCustomToast(it)
            progressDialog.hidedialog()
        })

    }


    private fun getModelTypeDialog() {
        try {
            val d = Dialog(this@EditActivity)
            d.setCancelable(true)
            d.window?.setGravity(Gravity.CENTER)
            d.requestWindowFeature(Window.FEATURE_NO_TITLE)
            d.setContentView(R.layout.dialog_vehicle_type)
            d.textsuccess.setText("Select type")
            d.setCancelable(true)
            d.btnok.setOnClickListener{
                d.dismiss()
            }
            val layoutManager = LinearLayoutManager(this@EditActivity, LinearLayoutManager.VERTICAL, false)
            d.rv_state_dialog.layoutManager = layoutManager
            d.rv_state_dialog.adapter = ActivityTypeDialogboxAdapter(this@EditActivity, modelTypes, object : ActivityTypeDialogboxAdapter.onClickListener{
                override fun subcatClick(subcatName: String?,id: String) {
                    tv_acticity_type.setText(subcatName)
                    activity_typeId = id
                    d.dismiss()
                }

            })
            d.setCanceledOnTouchOutside(true)
            d.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun getCurrentData(list: ArrayList<AgeGroupMyAds>, position: Int) {
        listAgeGroupDataModel = list
        for (i in 0..listAgeGroupDataModel.size - 1) {
            val json = JSONObject()
            json.put("age_from", listAgeGroupDataModel[i].ageFrom)
            json.put("age_to", listAgeGroupDataModel[i].ageTo)
            json.put("days", listAgeGroupDataModel[i].days)
            json.put("time_from", listAgeGroupDataModel[i].timeFrom)
            json.put("time_to", listAgeGroupDataModel[i].timeTo)
            ageGroup.put(json)
        }
    }

    override fun currentEventDataList(list: java.util.ArrayList<EventMyAds>, position: Int) {
        listAddEventDataModel = list

        for (i in 0..listAddEventDataModel!!.size - 1) {
            val json = JSONObject()
            json.put("image", listAddEventDataModel!![i].image)
            json.put("name", listAddEventDataModel[i].name.toString())
            json.put("file_type", "image")
            // for(j in 0 .. i){
            json.put("date_from", listAddEventDataModel[i].dateFrom.toString())
            json.put("date_to", listAddEventDataModel[i].dateTo.toString())
            json.put("time_from", listAddEventDataModel[i].startTime.toString())
            json.put("time_to", listAddEventDataModel[i].endTime.toString())
            json.put("description", listAddEventDataModel[i].description.toString())
            json.put("price", listAddEventDataModel[i].price.toString())
            json.put("city", listAddEventDataModel[i].city.toString())
            json.put("lat", listAddEventDataModel[i].latitude.toString())
            json.put("lng", listAddEventDataModel[i].longitude.toString())
            //}
            addEvent.put(json)
        }


    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlImg -> {
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
            R.id.ivImg3 -> {
                IMAGE_SELECTED_TYPE = "4"
                checkPermission(this)
            }
            R.id.ivplus -> {
                IMAGE_SELECTED_TYPE = "5"
                checkPermission(this)
            }
            R.id.btnSubmit -> {
                val shopName = et_shopName.text.toString()
                val activityName = et_activityName.text.toString()
                val description = et_description.text.toString()
                val phone = et_phone.text.toString()
                val address = et_addressActivity.text.toString()
                val message = et_message.text.toString()

//                if (IS_IMAGE_SELECTED.equals("")) {
//                    myCustomToast(getString(R.string.media_missing_error))
//                } else {
 //               val activityType = trader_type?.selectedItem?.toString()?:""
                if (activity_typeId.isNullOrEmpty()) {
                    myCustomToast(getString(R.string.activity_type_missing_error))
                } else {
                    if (shopName.isEmpty()) {
                        myCustomToast(getString(R.string.shop_name_missing_error))
                    } else {
                        if (activityName.isEmpty()) {
                            myCustomToast(getString(R.string.activity_name_missing_error))
                        } else {
                            if (description.isEmpty()) {
                                myCustomToast(getString(R.string.description_missing))
                            } else {
                                if (phone.isEmpty()) {
                                    myCustomToast(getString(R.string.phone_number_missing))
                                } else {
                                    if (address.isEmpty()) {
                                        myCustomToast(getString(R.string.address_missing_error))
                                    } else {
                                        // adding values
                                        activity_type = trader_type.selectedItem.toString()
                                        shop_name = shopName
                                        activity_name = activityName
                                        descp = description
                                        messagee = message
                                        phonee = phone

/*
                                        if (activity_type.equals(getString(R.string.sports))) {
                                            activity_typeId = "5"
                                        } else if (activity_type.equals(getString(R.string.dance))) {
                                            activity_typeId = "9"
                                        } else if (activity_type.equals(getString(R.string.drawing))) {
                                            activity_typeId = "10"
                                        } else if (activity_type.equals(getString(R.string.zumba))) {
                                            activity_typeId = "11"
                                        } else if (activity_type.equals(getString(R.string.tutor_mother_subject))) {
                                            activity_typeId = "13"
                                        }
*/


                                        if (activityimageList.isNullOrEmpty()) {

                                            // checking the list
                                            if (selectedUrlListing.size.equals(urlListingFromResponse.size)) {
                                                selectedUrlListing.clear()
                                                urlListingFromResponse.clear()
                                            }

                                            // for check upper images url from response
                                            Log.d("imageVideoListSize", "-----------" + imageVideoUrlListing)

                                            /*if (IMAGE_SELECTED_TYPE == null) {*/
                                                hitFinallyActivityAddPostApi()
                                            /*} else {
                                                // rotating loop
                                                for (i in 0..imageVideoUrlListing.size - 1) {
                                                    val media = imageVideoUrlListing[i]
                                                    if (!media.isEmpty()) {
                                                        selectedUrlListing.add(media)
                                                    }
                                                }

                                                // hitting api for upper 5 images
                                            //    hitApiForBannerImages(0)
                                            }*/


                                        } else if(!activityimageList.isNullOrEmpty()  &&  IMAGE_SELECTED_TYPE!= null){

                                            if (urlListingFromResponse != null) {
                                                urlListingFromResponse!!.clear()
                                            } else {
                                                urlListingFromResponse = ArrayList()

                                            }
                                            for (i in 0 until activityimageList.size) {
                                                urlListingFromResponse.add(activityimageList.get(i).images.toString())
                                            }
                                            media = JSONArray()

                                            for (i in 0..urlListingFromResponse.size - 1) {
                                                val json = JSONObject()
                                                json.put("image", urlListingFromResponse[i])
                                                media.put(json)

                                            }

                                            // rotating loop
                                            for (i in 0..imageVideoUrlListing.size - 1) {
                                                val media = imageVideoUrlListing[i]
                                                if (!media.isEmpty()) {
                                                    selectedUrlListing.add(media)
                                                }
                                            }

                                                    //  hitApiForBannerImages(0)


                                            hitFinallyActivityAddPostApi()
                                        }else
                                        {
                                            if (urlListingFromResponse != null) {
                                                urlListingFromResponse!!.clear()
                                            } else {
                                                urlListingFromResponse = ArrayList()

                                            }
                                            for (i in 0 until activityimageList.size) {
                                                urlListingFromResponse.add(activityimageList.get(i).images.toString())
                                            }
                                            media = JSONArray()

                                            for (i in 0..urlListingFromResponse.size - 1) {
                                                val json = JSONObject()
                                                json.put("image", urlListingFromResponse[i])
                                                media.put(json)

                                            }


                                            hitFinallyActivityAddPostApi()


                                        }
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
                /*}*/
            }

            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.et_addressActivity -> {
                showPlacePicker()
            }
        }
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

                val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
                appViewModel.sendUploadImageData(type, users, imagePathList)
                progressDialog.setProgressDialog()
            } else {

                imageVideoListPosition = imageVideoListPosition + 1

                if (imageVideoListPosition <= selectedUrlListing.size) {
                 //   hitApiForBannerImages(imageVideoListPosition)
                }
            }

        } else {

            savedaddEventImagesData = true
            gettingURLOfEventImages()


        }
    }


    private fun gettingURLOfEventImages() {
        if (imagePathList2 != null) {
            imagePathList2.clear()
        }else{
            imagePathList2 = ArrayList()

        }

        for (i in 0 until listAddEventDataModel.size) {
            val media = listAddEventDataModel[i].image
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

       // savedaddEventImagesData = true

        val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
        val type = "image".toRequestBody("text/plain".toMediaTypeOrNull())
        if(imagePathList2.isNullOrEmpty() && IMAGE_SELECTED_TYPE==null ){
            hitFinallyActivityAddPostApi()
        }else if(imagePathList2.isNullOrEmpty() && IMAGE_SELECTED_TYPE!= null){
            appViewModel.sendUploadImageData(type, users, imagePathList2)

        }else{
        appViewModel.sendUploadImageData(type, users, imagePathList2)
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

    private fun showPlacePickerForAddEvent() {
        // Initialize Places.
        Places.initialize(applicationContext, googleMapKey)
        // Create a new Places client instance.
        val placesClient: PlacesClient = Places.createClient(this)
        // Set the fields to specify which types of place data to return.
        val fields: List<Place.Field> = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, addEventRequestcode)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === PLACE_PICKER_REQUEST) {
            if (resultCode === Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                cityAddress = place.address.toString()
                et_addressActivity.setText(cityAddress.toString())
                cityName = place.name.toString()
                // cityID = place.id.toString()
                addressLatitude = place.latLng?.latitude.toString()
                addressLongitude = place.latLng?.longitude.toString()

                Log.i("dddddd", "Place: " + place.name + ", " + place.id + "," + place.address + "," + place.latLng)
            } else if (resultCode === AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("dddddd", status.getStatusMessage().toString())
            } else if (resultCode === Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i("dddddd", "-------Operation is cancelled ")
            }
        } else if (requestCode == addEventRequestcode) {

            val place = Autocomplete.getPlaceFromIntent(data!!)
         //   cityAddress = place.address.toString()
            cityName = place.name.toString()
            // cityID = place.id.toString()
            cityLatitude = place.latLng?.latitude.toString()
            cityLongitude = place.latLng?.longitude.toString()

            updateEventList.get(pos).latitude = cityLatitude
            updateEventList.get(pos).city = cityName
            updateEventList.get(pos).longitude = cityLongitude

            eventEditAdapter.notifyDataSetChanged()

        }
    }


    override fun getRealImagePath(imgPath: String?) {

        Log.d("selectedImagePath", "-------" + imgPath)
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
                // here we getting the add event image photo path
                /*  listAddEventDataModel[eventPhotoPosition].image = imgPath.toString()
                  Log.d("lisufjdhf", "-----------" + listAddEventDataModel.toString())
                  eventEditAdapter.notifyDataSetChanged()
              */
            }

        }
        Log.d("imageVideoListSize", "-----------" + imageVideoUrlListing)
    }

    private fun setImageOnTab(imgPATH: String?, imageview: ImageView?) {
        Log.d("getimage", "---------" + imgPATH.toString())

        /*when (IMAGE_SELECTED_TYPE) {
            "1" -> {
                //    imageview?.setScaleType(ImageView.ScaleType.FIT_XY)
               // checkVideoButtonVisibility(imgPATH.toString(), iv_video1)
            }
            "2" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video2)

            }
            "3" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video3)

            }


        }*/
        Glide.with(this).asBitmap().load(imgPATH).into(imageview!!)
        hitImageUploadApi(imgPATH)
    }

    private fun checkVideoButtonVisibility(imgpath: String, videoButton: ImageView) {

        if (imgpath?.contains(".mp4")!!) {
            videoButton.visibility = View.VISIBLE
        } else {
            videoButton.visibility = View.GONE
        }
    }

    override fun addAgeGroupItem(list: ArrayList<AgeGroupMyAds>, position: Int) {

        Log.d("listtAgrGroup", "--------" + list)
        /* list.add(AgeGroupMyAds("", "", "", "", "", "", ""))
         // imageList.add(ModelPOJO.EventImage(""))
         ageGroupEditAdapter.notifyDataSetChanged()
 */
    }

    override fun cityinAddEvent(list: ArrayList<EventMyAds>, position: Int, city: TextView) {
        updateEventList = list
        pos = position
        showPlacePickerForAddEvent()
        /* list.get(position).city = cityName
         list.get(position).lati = cityLatitude
         list.get(position).longi = cityLongitude*/
        //  city.setText(list.get(position).city)

        // eventEditAdapter.notifyDataSetChanged()

    }

    override fun onAddEventItem(list: ArrayList<EventMyAds>, position: Int) {
//        listAddEventDataModel.add(ModelPOJO.AddEventDataModel("", "", "", "", "", "",
//                "", "", "", "", ""))
//        eventEditAdapter.notifyDataSetChanged()
    }

    override fun addCameraGelleryImage(list: ArrayList<EventMyAds>, position: Int) {
        // for adding event image on card
        eventPhotoPosition = position
        checkPermission(this)
    }

    private fun hitTypeActivity_Api() {
        launch(Dispatchers.Main.immediate) {
            authKey = dataStoragePreference?.emitStoredValue(preferencesKey<String>("auth_key"))?.first()
            appViewModel.sendActivityTypeData(security_key, authKey)
        }
    }


    private fun hitFinallyActivityAddPostApi() {

        appViewModel.send_editActivity_Data(security_key, authKey, postID, "1", activity_typeId, shop_name, activity_name,
                descp, phonee, et_addressActivity.text.toString(), cityName, addressLatitude, addressLongitude, ageGroup.toString(),
                addEvent.toString()/*, media.toString()*/, countryCodee, image1, image2, image3)

           progressDialog.setProgressDialog()
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


}