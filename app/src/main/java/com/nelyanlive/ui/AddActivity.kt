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
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.adapter.AgeGroupRepeatAdapter
import com.nelyanlive.adapter.EventRepeatAdapter
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.ModelPOJO
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
import kotlin.coroutines.CoroutineContext

class AddActivity : OpenCameraGallery(), OnItemSelectedListener, View.OnClickListener,
        CoroutineScope, AgeGroupRepeatAdapter.OnAGeGroupRecyclerViewItemClickListner,
        EventRepeatAdapter.OnEventRecyclerViewItemClickListner {

    private val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java)
    }

    private var IMAGE_SELECTED_TYPE = ""
    private var IS_IMAGE_SELECTED = ""

    private val job by lazy { kotlinx.coroutines.Job() }

    private val dataStoragePreference by lazy { DataStoragePreference(this@AddActivity) }

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

    // dialo for progress
    private var progressDialog = ProgressDialog(this)

    // initalize the variables
    private lateinit var listAgeGroupDataModel: ArrayList<ModelPOJO.AgeGroupDataModel>
    private lateinit var listAddEventDataModel: ArrayList<ModelPOJO.AddEventDataModel>
    private lateinit var ageGroupRepeatAdapter: AgeGroupRepeatAdapter
    private lateinit var addEventRepeatAdapter: EventRepeatAdapter

    private var cityName = ""
    private var cityLatitude = ""
    private var cityLongitude = ""
    private var cityAddress = ""
    private var authKey = ""
    private var addressLatitude = ""
    private var addressLongitude = ""

    private var eventPhotoPosition = 0
    private var urlListingFromResponse: ArrayList<String> = ArrayList()
    private var addEventUrlListingResponse: ArrayList<String> = ArrayList()

    var imageVideoUrlListing = arrayListOf("", "", "", "", "")

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
    private var countryCodee = "+33"
    private var event = false
    private var lastPos = 0

    private lateinit var updateEventList: ArrayList<ModelPOJO.AddEventDataModel>
    private var pos = -1

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

   var selectedImags = arrayListOf("", "", "", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addactivity)
        initalizeclicks()
        checkMvvmResponse()

        countycode.setOnCountryChangeListener {
            countryCodee = countycode.selectedCountryCode.toString()
        }

        ageGroupRepeatAdapter = AgeGroupRepeatAdapter(this, listAgeGroupDataModel, this)
        rvAgeGroup!!.adapter = ageGroupRepeatAdapter

        addEventRepeatAdapter = EventRepeatAdapter(this, listAddEventDataModel, this)
        rvEvent!!.adapter = addEventRepeatAdapter

        hitTypeActivity_Api()

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun initalizeclicks() {

        // initalize the lists
        if (this::listAgeGroupDataModel.isInitialized) {
            listAgeGroupDataModel.clear()

        } else {
            listAgeGroupDataModel = ArrayList()
            listAgeGroupDataModel.add(ModelPOJO.AgeGroupDataModel("", "", "", "", ""))
        }

        if (this::listAddEventDataModel.isInitialized) {
            listAddEventDataModel.clear()

        } else {
            listAddEventDataModel = ArrayList()
            listAddEventDataModel.add(ModelPOJO.AddEventDataModel("", "", "", "", "",
                    "", "", "", "", "", ""))
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

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlImg -> {

                IMAGE_SELECTED_TYPE = "3"
                checkPermission(this)
            }
            R.id.ivImg1 -> {
                IMAGE_SELECTED_TYPE = "1"
                checkPermission(this)
            }
            R.id.ivImg2 -> {
                IMAGE_SELECTED_TYPE = "2"
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

                if (IS_IMAGE_SELECTED == "") {
                    myCustomToast(getString(R.string.media_missing_error))
                } else {
                    val activityType = trader_type.selectedItem.toString()
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
                                            // adding values
                                            activity_type = trader_type.selectedItem.toString()

                                            when (activity_type) {
                                                getString(R.string.sports) -> {
                                                    activity_typeId = "5"
                                                }
                                                getString(R.string.dance) -> {
                                                    activity_typeId = "9"
                                                }
                                                getString(R.string.drawing) -> {
                                                    activity_typeId = "10"
                                                }
                                                getString(R.string.zumba) -> {
                                                    activity_typeId = "11"
                                                }
                                                getString(R.string.tutor_mother_subject) -> {
                                                    activity_typeId = "13"
                                                }



                                            }

                                            shop_name = shopName
                                            activity_name = activityName
                                            descp = description
                                            messagee = message
                                            phonee = phone




                                            // for check upper images url from response
                                            Log.d("selectedImages", "-----------$selectedImags")
                                            Log.d("selectedImages", "-----------$listAgeGroupDataModel")


                                            for(i in 0 until selectedImags.size)
                                            {
                                                if(selectedImags[i].isNotEmpty())
                                                {
                                                    lastPos += 1
                                                }
                                            }


                                            when {
                                                listAgeGroupDataModel[listAgeGroupDataModel.size-1].ageFrom.isNullOrEmpty() -> { myCustomToast("please fill age Group form in previous data") }
                                                listAgeGroupDataModel[listAgeGroupDataModel.size-1].ageTo.isNullOrEmpty() -> { myCustomToast("please fill age Group to in previous data")}
                                                listAgeGroupDataModel[listAgeGroupDataModel.size-1].days.isNullOrEmpty() -> { myCustomToast("please select days in previous data")}
                                                listAgeGroupDataModel[listAgeGroupDataModel.size-1].timeFrom.isNullOrEmpty() -> { myCustomToast("please select From Time in previous data")}
                                                listAgeGroupDataModel[listAgeGroupDataModel.size-1].timeTo.isNullOrEmpty() -> { myCustomToast("please select To Time in previous data")}
                                                else -> {
                                                    hitApiForBannerImages(0)
                                                }
                                            }
                                             //hitApiForBannerImages(0)

                                        }
                                    }
                                }

                            }
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



            val media = selectedImags[imageVideoListPosition]

            if (!media.isNullOrEmpty()) {


                val mfile: File?
                mfile = File(media)
                val imageFile: RequestBody? = mfile.asRequestBody("image/*".toMediaTypeOrNull())
                val photo: MultipartBody.Part?
                photo = MultipartBody.Part.createFormData("image", mfile.name, imageFile!!)
                imagePathList.clear()
                imagePathList.add(photo)
                val type: RequestBody = "image".toRequestBody("text/plain".toMediaTypeOrNull())

                val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
                appViewModel.sendUploadImageData(type, users, imagePathList)
                progressDialog.setProgressDialog()
            }

            else {

                imageVideoListPosition += 1

                if (imageVideoListPosition < 4) {
                    hitApiForBannerImages(imageVideoListPosition)
                }
            }

      //  }

    }


    private fun gettingURLOfEventImages() {
        imagePathList2.clear()

        for (i in 0 until listAddEventDataModel.size) {
            val media = listAddEventDataModel[i].image
            if (!media.isNullOrEmpty()) {
                var mfile: File? = null
                mfile = File(media)
                val imageFile: RequestBody? = mfile.asRequestBody("image/*".toMediaTypeOrNull())
                var photo: MultipartBody.Part? = null
                photo = MultipartBody.Part.createFormData("image", mfile.name, imageFile!!)
                imagePathList2.add(photo)
            }
        }

        Log.d("imagePathLsiting", "-------------" + imagePathList2.size)

        val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
        val type = "image".toRequestBody("text/plain".toMediaTypeOrNull())
        appViewModel.sendUploadImageData(type, users, imagePathList2)

    }



    private fun showPlacePicker() {
        Places.initialize(applicationContext, googleMapKey)
        // Create a new Places client instance.
        val placesClient: PlacesClient = Places.createClient(this)
        // Set the fields to specify which types of place data to return.
        val fields: List<Place.Field> = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

    }

    private fun showPlacePickerForAddEvent() {
        // Initialize Places.
        Places.initialize(applicationContext, googleMapKey)
        // Create a new Places client instance.
        val placesClient: PlacesClient = Places.createClient(this)
        // Set the fields to specify which types of place data to return.
        val fields: List<Place.Field> = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, addEventRequestcode)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === PLACE_PICKER_REQUEST) {
            when {
                resultCode === Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    cityAddress = place.address.toString()
                    et_addressActivity.text = cityAddress.toString()
                    cityName = place.name.toString()
                    // cityID = place.id.toString()
                    addressLatitude = place.latLng?.latitude.toString()
                    addressLongitude = place.latLng?.longitude.toString()

                    Log.i("dddddd", "Place: " + place.name + ", " + place.id + "," + place.address + "," + place.latLng)
                }
                resultCode === AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    val status: Status = Autocomplete.getStatusFromIntent(data!!)
                    Log.i("dddddd", status.statusMessage.toString())
                }
                resultCode === Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                    Log.i("dddddd", "-------Operation is cancelled ")
                }
            }
        } else if (requestCode == addEventRequestcode) {

            val place = Autocomplete.getPlaceFromIntent(data!!)
         //   cityAddress = place.address.toString()
            cityName = place.name.toString()
            // cityID = place.id.toString()
            cityLatitude = place.latLng?.latitude.toString()
            cityLongitude = place.latLng?.longitude.toString()

            updateEventList[pos].lati = cityLatitude
            updateEventList[pos].city = cityName
            updateEventList[pos].longi = cityLongitude

            addEventRepeatAdapter.notifyDataSetChanged()

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
                selectedImags[0] =imgPath.toString()
            }
            "2" -> {
                setImageOnTab(imgPath, ivImg2)
                imageVideoUrlListing[1] = imgPath.toString()
                IMAGE_SELECTED_TYPE = ""
                IS_IMAGE_SELECTED = "1"
                selectedImags[1] =imgPath.toString()

            }
            "3" -> {
                setImageOnTab(imgPath, ivImg)
                imageVideoUrlListing[2] = imgPath.toString()
                IMAGE_SELECTED_TYPE = ""
                IS_IMAGE_SELECTED = "1"
                selectedImags[2] =imgPath.toString()

            }
            else -> {
                // here we getting the add event image photo path
                event = true
                selectedImags[3] =imgPath.toString()

                listAddEventDataModel[eventPhotoPosition].image = imgPath.toString()
                Log.d("lisufjdhf", "-----------$listAddEventDataModel")
                addEventRepeatAdapter.notifyDataSetChanged()
            }

        }
        Log.d("imageVideoListSize", "-----------$imageVideoUrlListing")
    }

    private fun setImageOnTab(imgPATH: String?, imageview: ImageView?) {
        Log.d("getimage", "---------" + imgPATH.toString())

        when (IMAGE_SELECTED_TYPE) {
            "1" -> {
                //    imageview?.setScaleType(ImageView.ScaleType.FIT_XY)
                checkVideoButtonVisibility(imgPATH.toString(), iv_video1)
            }
            "2" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video2)

            }
            "3" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video3)

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

    override fun addAgeGroupItem(list: ArrayList<ModelPOJO.AgeGroupDataModel>, position: Int) {

        Log.d("listtAgrGroup", "--------$list")
        //

        when {
            listAgeGroupDataModel[list.size-1].ageFrom.isNullOrEmpty() -> { myCustomToast("please fill age Group form in previous data") }
            listAgeGroupDataModel[list.size-1].ageTo.isNullOrEmpty() -> { myCustomToast("please fill age Group to in previous data")}
            listAgeGroupDataModel[list.size-1].days.isNullOrEmpty() -> { myCustomToast("please select days in previous data")}
            listAgeGroupDataModel[list.size-1].timeFrom.isNullOrEmpty() -> { myCustomToast("please select From Time in previous data")}
            listAgeGroupDataModel[list.size-1].timeTo.isNullOrEmpty() -> { myCustomToast("please select To Time in previous data")}
            else -> {  list.add(ModelPOJO.AgeGroupDataModel("", "", "", "", ""))
                // imageList.add(ModelPOJO.EventImage(""))
                ageGroupRepeatAdapter.notifyDataSetChanged()
            }
        }





    }

    override fun cityinAddEvent(list: ArrayList<ModelPOJO.AddEventDataModel>, position: Int, city: TextView) {
        updateEventList = list
        pos = position
        showPlacePickerForAddEvent()

    }

    override fun onAddEventItem(list: ArrayList<ModelPOJO.AddEventDataModel>, position: Int) {
        listAddEventDataModel.add(ModelPOJO.AddEventDataModel("", "", "", "", "", "",
                "", "", "", "", ""))
        addEventRepeatAdapter.notifyDataSetChanged()
    }

    override fun addCameraGelleryImage(list: ArrayList<ModelPOJO.AddEventDataModel>, position: Int) {
        // for adding event image on card
        eventPhotoPosition = position
        checkPermission(this)
    }

    private fun hitTypeActivity_Api() {
        launch(Dispatchers.Main.immediate) {
            authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
            appViewModel.sendActivityTypeData(security_key, authKey)
        }
    }

    private fun checkMvvmResponse() {
        appViewModel.observeActivityTypeResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    val jsonArray = jsonObject.getJSONArray("data")
                    val country: ArrayList<String?> = ArrayList()
                    country.add("")
                    for (i in 0 until jsonArray.length()) {
                        val name = jsonArray.getJSONObject(i).get("name").toString()
                        country.add(name)
                    }
                    val arrayAdapte1 = ArrayAdapter<Any?>(this, R.layout.customspinner, country as List<Any?>)
                    trader_type.adapter = arrayAdapte1
                }
            } else {
                ErrorBodyResponse(response, this, null)
            }
        })

        // add post for activity
        appViewModel.observe_addPostActivity_Response()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("addPostActivityResopnse", "-----------" + Gson().toJson(response.body()))
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

        // for imageUpload api
        appViewModel.observeUploadImageResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                //progressDialog.hidedialog()
                Log.d("urlDataLoading", "------------" + Gson().toJson(response.body()))
                if (response.body() != null) {
                    if (response.body()!!.data != null) {

                        if(imageVideoListPosition==3)
                        {
                            // response for addevent images data
                            addEventUrlListingResponse.clear()
                            if (!event) {
                                urlListingFromResponse.add(response.body()!!.data!![0].image.toString())
                            }


                            // now making json format for upper images media
                            for (i in 0 until urlListingFromResponse.size) {
                                val json = JSONObject()
                                json.put("image", urlListingFromResponse[i])
                                media.put(json)
                            }

                            // json format for addEvent
                            if (event) {
                                for (element in response.body()!!.data!!) {
                                    addEventUrlListingResponse.add(element.image.toString())
                                }


                                for (i in 0 until listAddEventDataModel.size) {
                                    val json = JSONObject()
                                    json.put("image", addEventUrlListingResponse[i])
                                    json.put("name", listAddEventDataModel[i].name.toString())
                                    json.put("file_type", "image")
                                    // for(j in 0 .. i){
                                    json.put("date_from", listAddEventDataModel[i].dateFrom.toString())
                                    json.put("date_to", listAddEventDataModel[i].dateTo.toString())
                                    json.put("time_from", listAddEventDataModel[i].timeFrom.toString())
                                    json.put("time_to", listAddEventDataModel[i].timeTo.toString())
                                    json.put("description", listAddEventDataModel[i].description.toString())
                                    json.put("price", listAddEventDataModel[i].price.toString())
                                    json.put("city", listAddEventDataModel[i].city.toString())
                                    json.put("lat", listAddEventDataModel[i].lati.toString())
                                    json.put("lng", listAddEventDataModel[i].longi.toString())
                                    //}
                                    addEvent.put(json)
                                }
                            }

                            // for age group listing cards
                            for (i in 0 until listAgeGroupDataModel.size) {
                                val json = JSONObject()
                                json.put("age_from", listAgeGroupDataModel[i].ageFrom)
                                json.put("age_to", listAgeGroupDataModel[i].ageTo)
                                json.put("days", listAgeGroupDataModel[i].days)
                                json.put("time_from", listAgeGroupDataModel[i].timeFrom)
                                json.put("time_to", listAgeGroupDataModel[i].timeTo)
                                json.put("time_to", listAgeGroupDataModel[i].timeTo)
                                json.put("time_to", listAgeGroupDataModel[i].timeTo)
                                ageGroup.put(json)
                            }

                            hitFinallyActivityAddPostApi()
                        }
                        else {
                            if (imageVideoListPosition != lastPos - 1) {
                                urlListingFromResponse.add(response.body()!!.data!![0].image.toString())
                                // if (imageVideoListPosition <= 4) {
                                imageVideoListPosition += 1
                                hitApiForBannerImages(imageVideoListPosition)
                                //}

                            } else {
                                // response for addevent images data
                                addEventUrlListingResponse.clear()
                                if (!event) {
                                    urlListingFromResponse.add(response.body()!!.data!![0].image.toString())
                                }


                                // now making json format for upper images media
                                for (i in 0 until urlListingFromResponse.size) {
                                    val json = JSONObject()
                                    json.put("image", urlListingFromResponse[i])
                                    media.put(json)
                                }

                                // json format for addEvent
                                if (event) {
                                    for (element in response.body()!!.data!!) {
                                        addEventUrlListingResponse.add(element.image.toString())
                                    }


                                    for (i in 0 until listAddEventDataModel.size) {
                                        val json = JSONObject()
                                        json.put("image", addEventUrlListingResponse[i])
                                        json.put("name", listAddEventDataModel[i].name.toString())
                                        json.put("file_type", "image")
                                        // for(j in 0 .. i){
                                        json.put("date_from", listAddEventDataModel[i].dateFrom.toString())
                                        json.put("date_to", listAddEventDataModel[i].dateTo.toString())
                                        json.put("time_from", listAddEventDataModel[i].timeFrom.toString())
                                        json.put("time_to", listAddEventDataModel[i].timeTo.toString())
                                        json.put("description", listAddEventDataModel[i].description.toString())
                                        json.put("price", listAddEventDataModel[i].price.toString())
                                        json.put("city", listAddEventDataModel[i].city.toString())
                                        json.put("lat", listAddEventDataModel[i].lati.toString())
                                        json.put("lng", listAddEventDataModel[i].longi.toString())
                                        //}
                                        addEvent.put(json)
                                    }
                                }

                                // for age group listing cards
                                for (i in 0 until listAgeGroupDataModel.size) {
                                    val json = JSONObject()
                                    json.put("age_from", listAgeGroupDataModel[i].ageFrom)
                                    json.put("age_to", listAgeGroupDataModel[i].ageTo)
                                    json.put("days", listAgeGroupDataModel[i].days)
                                    json.put("time_from", listAgeGroupDataModel[i].timeFrom)
                                    json.put("time_to", listAgeGroupDataModel[i].timeTo)
                                    json.put("time_to", listAgeGroupDataModel[i].timeTo)
                                    json.put("time_to", listAgeGroupDataModel[i].timeTo)
                                    ageGroup.put(json)
                                }

                                hitFinallyActivityAddPostApi()
                            }
                        }

                        Log.e("urlListt", "-------------$urlListingFromResponse")
                        Log.d("addEventUrlListing", "-------------$addEventUrlListingResponse")

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

        Log.d("datahtgfhtyhty=====", "-----1111-----${ageGroup.length()}------${addEvent.length()}---")


        //Toast.makeText(this,"====--${ageGroup.length()}------${addEvent.length()}--====",Toast.LENGTH_SHORT).show()

        if(ageGroup.length()>0 && addEvent.length()>0)
        {

            Log.d("data =====", "-----2222--------")

            appViewModel.send_addPostActivity_Data(security_key, authKey, "1", activity_typeId, shop_name, activity_name,
                    descp, phonee, cityAddress, cityName, addressLatitude, addressLongitude, ageGroup.toString(),
                    addEvent.toString(), media.toString(), countryCodee,"0")
        }
        else
        {
            if(!event && ageGroup.length() ==0)
            {

                Log.d("data =====", "-----3333--------")

                appViewModel.send_addPostActivity_Data(security_key, authKey, "1", activity_typeId, shop_name, activity_name,
                        descp, phonee, cityAddress, cityName, addressLatitude, addressLongitude, ageGroup.toString(),
                        addEvent.toString(), media.toString(), countryCodee,"2")
            }
            else
            {
                if(!event && ageGroup.length() !=0)
                {
                    appViewModel.send_addPostActivity_Data(security_key, authKey, "1", activity_typeId, shop_name, activity_name,
                            descp, phonee, cityAddress, cityName, addressLatitude, addressLongitude, ageGroup.toString(),
                            addEvent.toString(), media.toString(), countryCodee,"1")
                }
            }

        }


        //   progressDialog.setProgressDialog()
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


}