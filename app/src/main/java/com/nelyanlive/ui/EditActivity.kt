package com.nelyanlive.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.adapter.AgeGroupEditAdapter
import com.nelyanlive.adapter.EventEditAdapter
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.myAd.ActivityimageMyAds
import com.nelyanlive.modals.myAd.AgeGroupMyAds
import com.nelyanlive.modals.myAd.EventMyAds
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_addactivity.*
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

    private var IMAGE_SELECTED_TYPE: String? = null
    private var IS_IMAGE_SELECTED = ""

    private val job by lazy { kotlinx.coroutines.Job() }

    private val dataStoragePreference by lazy { DataStoragePreference(this@EditActivity) }

    private val googleMapKey = "AIzaSyDQWqIXO-sNuMWupJ7cNNItMhR4WOkzXDE"
    private val PLACE_PICKER_REQUEST = 1
    private val addEventRequestcode = 2
    private var imagePathList = ArrayList<MultipartBody.Part>()
    private var imagePathList2 = ArrayList<MultipartBody.Part>()

    // json Array
    private var ageGroup: JSONArray = JSONArray()
    private var addEvent: JSONArray = JSONArray()
    private var media: JSONArray = JSONArray()

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


    var imageVideoUrlListing = arrayListOf("", "", "" )

    private var imageVideoListPosition = -1
    private var selectedUrlListing: ArrayList<String> = ArrayList()
    private var savedaddEventImagesData = false

    //saving values variables
    private var activity_type = ""
    private var activityTypeId = ""
    private var shop_name = ""
    private var activity_name = ""
    private var descp = ""
    private var phonee = ""
    private var countryCodee = ""

    private lateinit var updateEventList: ArrayList<EventMyAds>
    private var pos = -1

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onResume() {
        super.onResume()
        hitTypeActivityApi()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addactivity)
        initializeClicks()
        checkMvvmResponse()

        countycode.setOnCountryChangeListener {
            countryCodee = countycode.selectedCountryCode.toString()
        }

        trader_type.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun initializeClicks() {

        if (intent.extras != null) {
            et_shopName.setText(intent.getStringExtra("nameofShop").toString())
            et_activityName.setText(intent.getStringExtra("nameofActivity"))
            et_description.setText(intent.getStringExtra("description"))
            et_phone.setText(intent.getStringExtra("phoneNumber"))
            et_addressActivity.text = intent.getStringExtra("address")

            countryCodee = intent.getStringExtra("countryCode").toString()
            countycode.setCountryForPhoneCode(countryCodee.toInt())

            postID = intent.getStringExtra("adID").toString()
            activityTypeId = intent.getStringExtra("activityTypeId").toString()
        }

            activityimageList = ArrayList()
            activityimageList = intent.getParcelableArrayListExtra<ActivityimageMyAds>("activityimagesList")!!

            if (!activityimageList.isNullOrEmpty()) {
                when (activityimageList.size) {
                    1 -> {
                        Glide.with(this).load(image_base_URl + activityimageList[0].images).error(R.mipmap.no_image_placeholder).into(ivImg1)

                    }
                    2 -> {
                        Glide.with(this).load(image_base_URl + activityimageList[0].images).error(R.mipmap.no_image_placeholder).into(ivImg1)
                        Glide.with(this).load(image_base_URl + activityimageList[1].images).error(R.mipmap.no_image_placeholder).into(ivImg2)

                    }
                    3 -> {
                        Glide.with(this).load(image_base_URl + activityimageList[0].images).error(R.mipmap.no_image_placeholder).into(ivImg1)
                        Glide.with(this).load(image_base_URl + activityimageList[1].images).error(R.mipmap.no_image_placeholder).into(ivImg2)
                        Glide.with(this).load(image_base_URl + activityimageList[2].images).error(R.mipmap.no_image_placeholder).into(ivImg3)

                    }
                }
            }

            listAgeGroupDataModel = ArrayList()
            listAgeGroupDataModel = intent.getParcelableArrayListExtra<AgeGroupMyAds>("ageGroupList")!!

            if(listAgeGroupDataModel.isEmpty()) listAgeGroupDataModel.add(AgeGroupMyAds(0,"","","","",0,0,"","",""))
            ageGroupEditAdapter = AgeGroupEditAdapter(this, listAgeGroupDataModel, this)
            rvAgeGroup!!.adapter = ageGroupEditAdapter


            listAddEventDataModel = ArrayList()


            listAddEventDataModel = intent.getParcelableArrayListExtra<EventMyAds>("eventMyAdsList")!!

        if(listAddEventDataModel.isEmpty()) listAddEventDataModel.add(EventMyAds(0,"","","","","","","",0,"","","","","",0,"",0))

        eventEditAdapter = EventEditAdapter(this, listAddEventDataModel, this)
            rvEvent!!.adapter = eventEditAdapter

        ivImg1.setOnClickListener(this)
        ivImg2.setOnClickListener(this)
        ivImg3.setOnClickListener(this)

        btnSubmit.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        et_addressActivity.setOnClickListener(this)
    }

    override fun getCurrentData(list: ArrayList<AgeGroupMyAds>, position: Int) {
        listAgeGroupDataModel = list
        for (i in 0 until listAgeGroupDataModel.size) {
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

        for (i in 0 until listAddEventDataModel.size) {
            val json = JSONObject()
            json.put("image", listAddEventDataModel[i].image)
            json.put("name", listAddEventDataModel[i].name)
            json.put("file_type", "image")
            json.put("date_from", listAddEventDataModel[i].dateFrom)
            json.put("date_to", listAddEventDataModel[i].dateTo)
            json.put("time_from", listAddEventDataModel[i].startTime)
            json.put("time_to", listAddEventDataModel[i].endTime)
            json.put("description", listAddEventDataModel[i].description)
            json.put("price", listAddEventDataModel[i].price)
            json.put("city", listAddEventDataModel[i].city)
            json.put("lat", listAddEventDataModel[i].latitude)
            json.put("lng", listAddEventDataModel[i].longitude)

            addEvent.put(json)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.ivImg1 -> {
                IMAGE_SELECTED_TYPE = "1"
                checkPermission(this)
            }
            R.id.ivImg2 -> {
                IMAGE_SELECTED_TYPE = "2"
                checkPermission(this)
            }
            R.id.ivImg3 -> {
                IMAGE_SELECTED_TYPE = "3"
                checkPermission(this)
            }

            R.id.btnSubmit -> {
                val shopName = et_shopName.text.toString()
                val activityName = et_activityName.text.toString()
                val description = et_description.text.toString()
                val phone = et_phone.text.toString()
                val address = et_addressActivity.text.toString()


                val activityType = trader_type?.selectedItem?.toString()?:""
                if (activityType == "" || activityType == getString(R.string.select)) {
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
                                        activity_type = trader_type.selectedItem.toString()
                                        shop_name = shopName
                                        activity_name = activityName
                                        descp = description
                                        phonee = phone

                                        when (activity_type) {
                                            getString(R.string.sports) -> {
                                                activityTypeId = "5"
                                            }
                                            getString(R.string.dance) -> {
                                                activityTypeId = "9"
                                            }
                                            getString(R.string.drawing) -> {
                                                activityTypeId = "10"
                                            }
                                            getString(R.string.zumba) -> {
                                                activityTypeId = "11"
                                            }
                                            getString(R.string.tutor_mother_subject) -> {
                                                activityTypeId = "13"
                                            }
                                        }
                                        if (activityimageList.isNullOrEmpty()) {

                                            if (selectedUrlListing.size == urlListingFromResponse.size) {
                                                selectedUrlListing.clear()
                                                urlListingFromResponse.clear()
                                            }

                                            Log.d("imageVideoListSize", "-----------$imageVideoUrlListing")

                                            if (IMAGE_SELECTED_TYPE == null) {
                                                hitFinallyActivityAddPostApi()
                                            } else {
                                                for (i in 0 until imageVideoUrlListing.size) {
                                                    val media = imageVideoUrlListing[i]
                                                    if (media.isNotEmpty()) {
                                                        selectedUrlListing.add(media)
                                                    }
                                                }

                                                hitApiForBannerImages(0)
                                            }


                                        } else if(!activityimageList.isNullOrEmpty()  &&  IMAGE_SELECTED_TYPE!= null){

                                            if (urlListingFromResponse != null) {
                                                urlListingFromResponse.clear()
                                            } else {
                                                urlListingFromResponse = ArrayList()

                                            }
                                            for (i in 0 until activityimageList.size) {
                                                urlListingFromResponse.add(activityimageList[i].images)
                                            }
                                            media = JSONArray()

                                            for (i in 0 until urlListingFromResponse.size) {
                                                val json = JSONObject()
                                                json.put("image", urlListingFromResponse[i])
                                                media.put(json)

                                            }

                                            for (i in 0 until imageVideoUrlListing.size) {
                                                val media = imageVideoUrlListing[i]
                                                if (media.isNotEmpty()) {
                                                    selectedUrlListing.add(media)
                                                }
                                            }

                                            hitApiForBannerImages(0)



                                        }else
                                        {
                                            if (urlListingFromResponse != null) {
                                                urlListingFromResponse.clear()
                                            } else {
                                                urlListingFromResponse = ArrayList()

                                            }
                                            for (i in 0 until activityimageList.size) {
                                                urlListingFromResponse.add(activityimageList.get(i).images.toString())
                                            }
                                            media = JSONArray()

                                            for (i in 0 until urlListingFromResponse.size) {
                                                val json = JSONObject()
                                                json.put("image", urlListingFromResponse[i])
                                                media.put(json)

                                            }
                                            hitFinallyActivityAddPostApi()
                                        }
                                    } } }
                        }
                    }
                }
            }

            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.et_addressActivity -> {
                showPlacePicker()
            }
        }
    }

    private fun hitApiForBannerImages(position: Int) {

        imageVideoListPosition = position

        if (imageVideoListPosition <= selectedUrlListing.size - 1) {

            val media = selectedUrlListing[imageVideoListPosition]

            if (!media.isNullOrEmpty()) {

                savedaddEventImagesData = false

                val mFile: File?
                mFile = File(media)
                val imageFile: RequestBody = mFile.asRequestBody("image/*".toMediaTypeOrNull())
                val photo: MultipartBody.Part?
                photo = MultipartBody.Part.createFormData("image", mFile.name, imageFile)
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


    private fun gettingURLOfEventImages() {
        if (imagePathList2 != null) {
            imagePathList2.clear()
        }else{
            imagePathList2 = ArrayList()

        }

        for (i in 0 until listAddEventDataModel.size) {
            val media = listAddEventDataModel[i].image
            if (!media.isNullOrEmpty()) {
                var mFile: File?
                mFile = File(media)
                val imageFile: RequestBody = mFile.asRequestBody("image/*".toMediaTypeOrNull())
                var photo: MultipartBody.Part?
                photo = MultipartBody.Part.createFormData("image", mFile.name, imageFile)
                imagePathList2.add(photo)
            }
        }

        Log.d("imagePathLsiting", "-------------" + imagePathList2.size)


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
        Places.initialize(applicationContext, googleMapKey)
        val fields: List<Place.Field> = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

    }

    private fun showPlacePickerForAddEvent() {
        Places.initialize(applicationContext, googleMapKey)
        val fields: List<Place.Field> = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, addEventRequestcode)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    cityAddress = place.address.toString()
                    et_addressActivity.text = cityAddress
                    cityName = place.name.toString()
                    addressLatitude = place.latLng?.latitude.toString()
                    addressLatitude = place.latLng?.longitude.toString()

                    Log.i("dddddd", "Place: " + place.name + ", " + place.id + "," + place.address + "," + place.latLng)
                }
            }
        } else if (requestCode == addEventRequestcode) {

            val place = Autocomplete.getPlaceFromIntent(data!!)
            cityName = place.name.toString()
            cityLatitude = place.latLng?.latitude.toString()
            cityLongitude = place.latLng?.longitude.toString()

            updateEventList[pos].latitude = cityLatitude
            updateEventList[pos].city = cityName
            updateEventList[pos].longitude = cityLongitude

            eventEditAdapter.notifyDataSetChanged()

        }
    }


    override fun getRealImagePath(imgPath: String?) {

        Log.d("selectedImagePath", "-------$imgPath")
        when (IMAGE_SELECTED_TYPE) {

            "1" -> {
                setImageOnTab(imgPath, ivImg1)
                imageVideoUrlListing[0] = imgPath.toString()
                IMAGE_SELECTED_TYPE = ""
                IS_IMAGE_SELECTED = "1"
            }
            "2" -> {
                setImageOnTab(imgPath, ivImg2)
                imageVideoUrlListing[1] = imgPath.toString()
                IMAGE_SELECTED_TYPE = ""
                IS_IMAGE_SELECTED = "1"
            }
            "3" -> {
                setImageOnTab(imgPath, ivImg3)
                imageVideoUrlListing[2] = imgPath.toString()
                IMAGE_SELECTED_TYPE = ""
                IS_IMAGE_SELECTED = "1"
            }
        }
        Log.d("imageVideoListSize", "-----------$imageVideoUrlListing")
    }

    private fun setImageOnTab(imgPATH: String?, imageview: ImageView?) {
        Log.d("getimage", "---------" + imgPATH.toString())

        Glide.with(this).asBitmap().load(imgPATH).into(imageview!!)
    }

    override fun addAgeGroupItem(list: ArrayList<AgeGroupMyAds>, position: Int) {
    }

    override fun cityinAddEvent(list: ArrayList<EventMyAds>, position: Int, city: TextView) {
        updateEventList = list
        pos = position
        showPlacePickerForAddEvent()
    }

    override fun onAddEventItem(list: ArrayList<EventMyAds>, position: Int) {
    }

    override fun addCameraGelleryImage(list: ArrayList<EventMyAds>, position: Int) {
        eventPhotoPosition = position
        checkPermission(this)
    }

    private fun hitTypeActivityApi() {
        launch(Dispatchers.Main.immediate) {
            authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                .first()
            appViewModel.sendActivityTypeData(security_key, authKey)
        }
    }

    private val typeList: ArrayList<String> = ArrayList()
    private val typeListId: ArrayList<String> = ArrayList()
    private var selectedPosition = 0


    private fun checkMvvmResponse() {
        appViewModel.observeActivityTypeResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    val jsonArray = jsonObject.getJSONArray("data")

                    typeList.clear()
                    typeListId.clear()
                    typeList.add("")
                    typeListId.add("")
                    for (i in 0 until jsonArray.length()) {
                        val name = jsonArray.getJSONObject(i).get("name").toString()
                        val id = jsonArray.getJSONObject(i).get("id").toString()
                        typeList.add(name)
                        typeListId.add(id)
                        if(id==activityTypeId)
                        {
                            selectedPosition = i
                        }
                    }
                    val arrayAdapte1 = ArrayAdapter(this, R.layout.customspinner, typeList)
                    trader_type.adapter = arrayAdapte1
                    trader_type.setSelection(selectedPosition+1)



                }
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
                    val jsonObject = JSONObject(mResponse)
                    val message = jsonObject.get("msg").toString()
                    myCustomToast(message)

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
                        if (!savedaddEventImagesData) {
                            urlListingFromResponse.add(response.body()!!.data!![0].image.toString())
                            if (imageVideoListPosition <= 4) {
                                imageVideoListPosition += 1
                                hitApiForBannerImages(imageVideoListPosition)
                            }

                        } else {

                            if (addEventUrlListingResponse != null) {
                                addEventUrlListingResponse!!.clear()
                            }

                            for (element in response.body()!!.data!!) {
                                addEventUrlListingResponse!!.add(element.image.toString())
                            }

                            for (i in 0 until urlListingFromResponse.size) {
                                val json = JSONObject()
                                json.put("image", urlListingFromResponse[i])
                                media.put(json)
                            }


                            for (i in 0 until addEventUrlListingResponse!!.size) {
                                val json = JSONObject()
                                json.put("image", addEventUrlListingResponse!![i])
                                json.put("name", listAddEventDataModel[i].name)
                                json.put("file_type", "image")
                                json.put("date_from", listAddEventDataModel[i].dateFrom)
                                json.put("date_to", listAddEventDataModel[i].dateTo)
                                json.put("time_from", listAddEventDataModel[i].startTime)
                                json.put("time_to", listAddEventDataModel[i].endTime)
                                json.put("description", listAddEventDataModel[i].description)
                                json.put("price", listAddEventDataModel[i].price)
                                json.put("city", listAddEventDataModel[i].city)
                                json.put("lat", listAddEventDataModel[i].latitude)
                                json.put("lng", listAddEventDataModel[i].longitude)
                                addEvent.put(json)
                            }


                            // for age group listing cards
                            for (i in 0 until listAgeGroupDataModel.size) {
                                val json = JSONObject()
                                json.put("age_from", listAgeGroupDataModel[i].ageFrom)
                                json.put("age_to", listAgeGroupDataModel[i].ageTo)
                                json.put("days", listAgeGroupDataModel[i].days)
                                json.put("time_from", listAgeGroupDataModel[i].timeFrom)
                                json.put("time_to", listAgeGroupDataModel[i].timeTo)
                                ageGroup.put(json)
                            }

                            hitFinallyActivityAddPostApi()
                        }

                        Log.d("urlListt", "-------------$urlListingFromResponse")
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

    private fun hitFinallyActivityAddPostApi() {

        appViewModel.send_editActivity_Data(security_key, authKey, postID, "1", activityTypeId, shop_name, activity_name,
                descp, phonee, cityAddress, cityName, addressLatitude, addressLongitude, ageGroup.toString(),
                addEvent.toString(), media.toString(), countryCodee)
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


}