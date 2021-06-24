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
        CoroutineScope, AgeGroupRepeatAdapter.OnAgeGroupRecyclerViewItemClickListener,
        EventRepeatAdapter.OnEventRecyclerViewItemClickListener {

    private val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java)
    }

    private var imageSelectedType = ""
    private var isImageSelected = false

    private val job by lazy { kotlinx.coroutines.Job() }

    private val dataStoragePreference by lazy { DataStoragePreference(this@AddActivity) }

    // Initialize Places variables
    private val googleMapKey = "AIzaSyDQWqIXO-sNuMWupJ7cNNItMhR4WOkzXDE"
    private val placePickRequestCode = 1
    private val addEventRequestCode = 2
    private var imagePathList = ArrayList<MultipartBody.Part>()

    // json Array
    private var ageGroup: JSONArray = JSONArray()
    private var addEvent: JSONArray = JSONArray()
    private var media: JSONArray = JSONArray()

    // dialo for progress
    private var progressDialog = ProgressDialog(this)

    // initalize the variables
    private lateinit var listAge: ArrayList<ModelPOJO.AgeGroupDataModel>
    private lateinit var listEvent: ArrayList<ModelPOJO.AddEventDataModel>
    private lateinit var ageGroupRepeatAdapter: AgeGroupRepeatAdapter
    private lateinit var addEventRepeatAdapter: EventRepeatAdapter

    private var cityName = ""
    private var cityLatitude = ""
    private var cityLongitude = ""
    private var cityAddress = ""
    private var authKey = ""
    private var addressLatitude = ""
    private var addressLongitude = ""

    var ageErrorNumber = 0
    var eventErrorNumber = 0
    private var event = false
    private var age = false

    private var eventPhotoPosition = 0
    private var urlListingFromResponse: ArrayList<String> = ArrayList()
    private var addEventUrlListingResponse: ArrayList<String> = ArrayList()

    private var imageVideoListPosition = -1

    private var activityTypeId = ""

    private var shopName = ""
    private var activityName = ""
    private var descp = ""
    private var phonee = ""
    private var countryCodee = "+33"
    private var lastPos = 0

    private lateinit var updateEventList: ArrayList<ModelPOJO.AddEventDataModel>
    private var pos = -1

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

   private var selectedImages = mutableListOf("", "", "","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addactivity)
        initalizeClicks()
        checkMvvmResponse()
        

        countycode.setOnCountryChangeListener {
            countryCodee = countycode.selectedCountryCode.toString()
        }

        ageGroupRepeatAdapter = AgeGroupRepeatAdapter(this, listAge, this)
        rvAgeGroup!!.adapter = ageGroupRepeatAdapter

        addEventRepeatAdapter = EventRepeatAdapter(this, listEvent, this)
        rvEvent!!.adapter = addEventRepeatAdapter

        hitTypeActivity()

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun initalizeClicks() {

        // initalize the lists
        if (this::listAge.isInitialized) {
            listAge.clear()

        } else {
            listAge = ArrayList()
            listAge.add(ModelPOJO.AgeGroupDataModel("", "", "", "", ""))
        }

        if (this::listEvent.isInitialized) {
            listEvent.clear()

        } 
        else {
            listEvent = ArrayList()
            listEvent.add(ModelPOJO.AddEventDataModel("", "", "", "", "",
                    "", "", "", "", "", ""))
        }

        // clicks for images
        ivImg3.setOnClickListener(this)
        ivImg1.setOnClickListener(this)
        ivImg2.setOnClickListener(this)
        ivImg3.setOnClickListener(this)

        // clicks for buttons
        btnSubmit.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        et_addressActivity.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivImg3 -> {

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

            R.id.btnSubmit -> {
                 shopName = et_shopName.text.toString()
                 activityName = et_activityName.text.toString()
                 descp = et_description.text.toString()
                 phonee = et_phone.text.toString()
                val activityType = trader_type.selectedItem.toString()

                if (!isImageSelected) {
                    myCustomToast(getString(R.string.media_missing_error))
                } else {
                    if (activityType == "" || activityType == getString(R.string.select)) {
                        myCustomToast(getString(R.string.activity_type_missing_error))
                    } else {
                        if (shopName.isEmpty()) {
                            myCustomToast(getString(R.string.shop_name_missing_error))
                        } else {
                            if (activityName.isEmpty()) {
                                myCustomToast(getString(R.string.activity_name_missing_error))
                            } else {
                                if (descp.isEmpty()) {
                                    myCustomToast(getString(R.string.description_missing))
                                } else {
                                        if (et_addressActivity.text.toString().isEmpty()) {
                                            myCustomToast(getString(R.string.address_missing_error))
                                        }
                                        else {
                                            // adding values

                                            when (activityType) {
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


                                            // for check upper images url from response
                                            Log.d("selectedImages", "-----------$selectedImages")
                                            Log.d("selectedImages", "-----------$listAge")


                                            for(i in 0 until selectedImages.size)
                                            {
                                                if(selectedImages[i].isNotEmpty())
                                                {
                                                    lastPos += 1
                                                }
                                            }

                                            var ageErrorString = ""
                                            ageErrorNumber = 0
                                            ageErrorString =  getAgeError(listAge[listAge.size-1].ageFrom,ageErrorString,"please fill age Group form in previous data",1)
                                            ageErrorString =  getAgeError(listAge[listAge.size-1].ageTo,ageErrorString,"please fill age Group to in previous data",2)
                                            ageErrorString =  getAgeError(listAge[listAge.size-1].days,ageErrorString,"please select days in previous data",3)
                                            ageErrorString =  getAgeError(listAge[listAge.size-1].timeFrom,ageErrorString,"please select From Time in previous data",4)
                                            ageErrorString =  getAgeError(listAge[listAge.size-1].timeTo,ageErrorString,"please select To Time in previous data",5)

                                            var eventErrorString = ""
                                            eventErrorNumber = 0
                                            eventErrorString =  getEventError(listEvent[listEvent.size-1].image,eventErrorString,"please select image in previous data",1)
                                            eventErrorString =  getEventError(listEvent[listEvent.size-1].name,eventErrorString,"please fill Event name in previous data",2)
                                            eventErrorString =  getEventError(listEvent[listEvent.size-1].dateFrom,eventErrorString,"please select From Date in previous data",3)
                                            eventErrorString =  getEventError(listEvent[listEvent.size-1].dateTo,eventErrorString,"please select To Date in previous data",4)
                                            eventErrorString =  getEventError(listEvent[listEvent.size-1].timeFrom,eventErrorString,"please select From Time in previous data",5)
                                            eventErrorString =  getEventError(listEvent[listEvent.size-1].timeTo,eventErrorString,"please select To Time in previous data",6)
                                            eventErrorString =  getEventError(listEvent[listEvent.size-1].description,eventErrorString,"please fill description in previous data",7)
                                            eventErrorString =  getEventError(listEvent[listEvent.size-1].price,eventErrorString,"please fill price in previous data",8)
                                            eventErrorString =  getEventError(listEvent[listEvent.size-1].city,eventErrorString,"please fill city in previous data",9)

                                            Log.e("dsfasdfsadf","===============$ageErrorString=========$ageErrorNumber")
                                            

                                            if(ageErrorNumber!=0 && ageErrorString.isNotEmpty())
                                            {
                                                myCustomToast(ageErrorString)
                                            }
                                            else
                                            {
                                                if(eventErrorNumber!=0 && eventErrorString.isNotEmpty())
                                                {
                                                    myCustomToast(eventErrorString)
                                                }
                                                else
                                                {
                                                    hitApiForBannerImages(0)
                                                }
                                            }
                                        } } } } } } }

            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.et_addressActivity -> {
                showPlacePicker(placePickRequestCode)
            }
        } }

    private fun getAgeError(ageFrom: String?, ageErrorString: String, s: String,i:Int) : String {

        return when {
            ageFrom.isNullOrBlank() -> when {
                ageErrorString.isBlank() -> {
                    s
                }
                else -> ageErrorString
            }
            else -> {
                age = true
                ageErrorNumber =i
                ageErrorString
            }
        }
    }
    private fun getEventError(ageFrom: String?, eventErrorString: String, s: String,i:Int) : String {

        return when {
            ageFrom.isNullOrBlank() -> when {
                eventErrorString.isBlank() -> {
                    s
                }
                else -> eventErrorString
            }
            else -> {

                eventErrorNumber =i
                eventErrorString
            }
        }
    }
    private fun hitApiForBannerImages(position: Int) {

        imageVideoListPosition = position

            val media = selectedImages[imageVideoListPosition]

            if (!media.isNullOrEmpty()) {

                val mFile: File?
                mFile = File(media)
                val imageFile: RequestBody = mFile.asRequestBody("image/*".toMediaTypeOrNull())
                val photo: MultipartBody.Part?
                photo = MultipartBody.Part.createFormData("image", mFile.name, imageFile)
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
    }
    private fun showPlacePicker(requestCode: Int) {
        Places.initialize(applicationContext, googleMapKey)
        val fields: List<Place.Field> = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    placePickRequestCode -> {
                        val place = Autocomplete.getPlaceFromIntent(data!!)
                        cityAddress = place.address.toString()
                        et_addressActivity.text = cityAddress
                        cityName = place.name.toString()
                        addressLatitude = place.latLng?.latitude.toString()
                        addressLongitude = place.latLng?.longitude.toString()
                    }
                    addEventRequestCode -> {

                        val place = Autocomplete.getPlaceFromIntent(data!!)
                        cityName = place.name.toString()
                        cityLatitude = place.latLng?.latitude.toString()
                        cityLongitude = place.latLng?.longitude.toString()
                        updateEventList[pos].lati = cityLatitude
                        updateEventList[pos].city = cityName
                        updateEventList[pos].longi = cityLongitude
                        addEventRepeatAdapter.notifyDataSetChanged()

                    } } } }
    }

    override fun getRealImagePath(imgPath: String?) {

        Log.d("selectedImagePath", "-------$imgPath")
        when (imageSelectedType) {

            "1" -> {
                setImageOnTab(imgPath, ivImg1)
                imageSelectedType = ""
                isImageSelected = true
                selectedImages[0] =imgPath.toString()
            }
            "2" -> {
                setImageOnTab(imgPath, ivImg2)
                imageSelectedType = ""
                isImageSelected = true
                selectedImages[1] =imgPath.toString()

            }
            "3" -> {
                setImageOnTab(imgPath, ivImg3)
                imageSelectedType = ""
                isImageSelected = true
                selectedImages[2] =imgPath.toString()

            }
            else -> {
                // here we getting the add event image photo path
                event = true
                selectedImages[3] =imgPath.toString()

                listEvent[eventPhotoPosition].image = imgPath.toString()
                Log.e("lisufjdhf", "-----------$listEvent")
                addEventRepeatAdapter.notifyDataSetChanged()

            } } }

    private fun setImageOnTab(imgPATH: String?, imageview: ImageView?) {
        Log.e("getimage", "---------" + imgPATH.toString())
        Glide.with(this).asBitmap().load(imgPATH).into(imageview!!)
    }

    override fun addAgeGroupItem(list: ArrayList<ModelPOJO.AgeGroupDataModel>, position: Int) {

        Log.d("listtAgrGroup", "--------$list")
        when {
            listAge[list.size-1].ageFrom.isNullOrEmpty() -> { myCustomToast("please fill age Group form in previous data") }
            listAge[list.size-1].ageTo.isNullOrEmpty() -> { myCustomToast("please fill age Group to in previous data")}
            listAge[list.size-1].days.isNullOrEmpty() -> { myCustomToast("please select days in previous data")}
            listAge[list.size-1].timeFrom.isNullOrEmpty() -> { myCustomToast("please select From Time in previous data")}
            listAge[list.size-1].timeTo.isNullOrEmpty() -> { myCustomToast("please select To Time in previous data")}
            else -> {  list.add(ModelPOJO.AgeGroupDataModel("", "", "", "", ""))
                ageGroupRepeatAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun cityAddEvent(list: ArrayList<ModelPOJO.AddEventDataModel>, position: Int, city: TextView) {
        updateEventList = list
        pos = position
        showPlacePicker(addEventRequestCode)
    }

    override fun onAddEventItem(list: ArrayList<ModelPOJO.AddEventDataModel>, position: Int) {
        when {
            listEvent[list.size-1].image.isNullOrEmpty() -> { myCustomToast("please select image in previous data") }
            listEvent[list.size-1].name.isNullOrEmpty() -> { myCustomToast("please fill Event name in previous data")}
            listEvent[list.size-1].dateFrom.isNullOrEmpty() -> { myCustomToast("please select From Date in previous data")}
            listEvent[list.size-1].dateTo.isNullOrEmpty() -> { myCustomToast("please select To Date in previous data")}
            listEvent[list.size-1].timeFrom.isNullOrEmpty() -> { myCustomToast("please select From Time in previous data")}
            listEvent[list.size-1].timeTo.isEmpty() -> { myCustomToast("please select To Time in previous data")}
            listEvent[list.size-1].description.isNullOrEmpty() -> { myCustomToast("please fill description in previous data")}
            listEvent[list.size-1].price.isNullOrEmpty() -> { myCustomToast("please fill price in previous data")}
            listEvent[list.size-1].city.isNullOrEmpty() -> { myCustomToast("please fill city in previous data")}
            else -> {  list.add(ModelPOJO.AddEventDataModel("", "", "", "", "", "",
                "", "", "", "", ""))
                addEventRepeatAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun addCameraGalleryImage(list: ArrayList<ModelPOJO.AddEventDataModel>, position: Int) {
        eventPhotoPosition = position
        checkPermission(this)
    }

    private fun hitTypeActivity() {
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
                    val arrayAdapter1 = ArrayAdapter(this, R.layout.customspinner, country as List<Any?>)
                    trader_type.adapter = arrayAdapter1
                }
            } else {
                ErrorBodyResponse(response, this, null)
            }
        })

        // add post for activity
        appViewModel.observe_addPostActivity_Response()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("addPostActivityResopnse", "-----------" + Gson().toJson(response.body()))
                    progressDialog.hidedialog()
                    finishAffinity()
                    OpenActivity(HomeActivity::class.java)
               
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

                                for (i in 0 until listEvent.size) {
                                    val json = JSONObject()
                                    json.put("image", addEventUrlListingResponse[i])
                                    json.put("name", listEvent[i].name.toString())
                                    json.put("file_type", "image")
                                    json.put("date_from", listEvent[i].dateFrom.toString())
                                    json.put("date_to", listEvent[i].dateTo.toString())
                                    json.put("time_from", listEvent[i].timeFrom.toString())
                                    json.put("time_to", listEvent[i].timeTo)
                                    json.put("description", listEvent[i].description.toString())
                                    json.put("price", listEvent[i].price.toString())
                                    json.put("city", listEvent[i].city.toString())
                                    json.put("lat", listEvent[i].lati.toString())
                                    json.put("lng", listEvent[i].longi.toString())
                                    addEvent.put(json)
                                }
                            }

                            // for age group listing cards
                            if(age) {
                                for (i in 0 until listAge.size) {
                                    val json = JSONObject()
                                    json.put("age_from", listAge[i].ageFrom)
                                    json.put("age_to", listAge[i].ageTo)
                                    json.put("days", listAge[i].days)
                                    json.put("time_from", listAge[i].timeFrom)
                                    json.put("time_to", listAge[i].timeTo)
                                    json.put("time_to", listAge[i].timeTo)
                                    json.put("time_to", listAge[i].timeTo)
                                    ageGroup.put(json)
                                }
                            }

                            hitFinallyActivityAddPostApi()
                        }
                        else {
                            if (imageVideoListPosition != lastPos - 1) {
                                urlListingFromResponse.add(response.body()!!.data!![0].image.toString())
                                imageVideoListPosition += 1
                                hitApiForBannerImages(imageVideoListPosition)

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


                                    for (i in 0 until listEvent.size) {
                                        val json = JSONObject()
                                        json.put("image", addEventUrlListingResponse[i])
                                        json.put("name", listEvent[i].name.toString())
                                        json.put("file_type", "image")
                                        json.put("date_from", listEvent[i].dateFrom.toString())
                                        json.put("date_to", listEvent[i].dateTo.toString())
                                        json.put("time_from", listEvent[i].timeFrom.toString())
                                        json.put("time_to", listEvent[i].timeTo)
                                        json.put("description", listEvent[i].description.toString())
                                        json.put("price", listEvent[i].price.toString())
                                        json.put("city", listEvent[i].city.toString())
                                        json.put("lat", listEvent[i].lati.toString())
                                        json.put("lng", listEvent[i].longi.toString())
                                        addEvent.put(json)
                                    }
                                }

                                if(age) {
                                    for (i in 0 until listAge.size) {
                                        val json = JSONObject()
                                        json.put("age_from", listAge[i].ageFrom)
                                        json.put("age_to", listAge[i].ageTo)
                                        json.put("days", listAge[i].days)
                                        json.put("time_from", listAge[i].timeFrom)
                                        json.put("time_to", listAge[i].timeTo)
                                        json.put("time_to", listAge[i].timeTo)
                                        json.put("time_to", listAge[i].timeTo)
                                        ageGroup.put(json)
                                    }
                                }

                                hitFinallyActivityAddPostApi()
                            }
                        }
                    }
                }
                else {
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

        if(ageGroup.length()>0 && addEvent.length()>0)
        {

            Log.d("data =====", "-----2222--------")

            appViewModel.send_addPostActivity_Data(security_key, authKey, "1", activityTypeId, shopName, activityName,
                    descp, phonee, cityAddress, cityName, addressLatitude, addressLongitude, ageGroup.toString(),
                    addEvent.toString(), media.toString(), countryCodee,"0")
        }
        else
        {
            if(!event && ageGroup.length()==0)
            {


                appViewModel.send_addPostActivity_Data(security_key, authKey, "1", activityTypeId, shopName, activityName,
                        descp, phonee, cityAddress, cityName, addressLatitude, addressLongitude, ageGroup.toString(),
                        addEvent.toString(), media.toString(), countryCodee,"2")
            }
            else
            {
                if(!event && ageGroup.length() !=0)
                {
                    appViewModel.send_addPostActivity_Data(security_key, authKey, "1", activityTypeId, shopName, activityName,
                            descp, phonee, cityAddress, cityName, addressLatitude, addressLongitude, ageGroup.toString(),
                            addEvent.toString(), media.toString(), countryCodee,"1")
                }
                else if(event && ageGroup.length() ==0)
                {
                    appViewModel.send_addPostActivity_Data(security_key, authKey, "1", activityTypeId, shopName, activityName,
                        descp, phonee, cityAddress, cityName, addressLatitude, addressLongitude, ageGroup.toString(),
                        addEvent.toString(), media.toString(), countryCodee,"3")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }




    data class AgeData(var ageFrom : String ="", var ageTo : String ="",
                       var day : String ="", var timeFrom : String ="",
                       var timeTo : String ="")

    data class EventData(var eventImage : String ="", var eventName : String ="",
                       var dateFrom : String ="", var dateTo : String ="",
                       var timeFrom : String ="",var timeTo : String =""
                       ,var description : String ="",var price : String =""
                       ,var city : String ="")

    data class AddActivityData(var imageList: MutableList<String> = mutableListOf(),var activityType : String ="",
                               var shopName : String ="",var activityName : String ="",var description : String ="",
                               var phone : String ="",var address : String ="",var ageList: MutableList<AgeData> = mutableListOf()
                               ,var eventList: MutableList<EventData> = mutableListOf())


}