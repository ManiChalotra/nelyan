package com.nelyanlive.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
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

    private var ageErrorNumber = 0
    private var eventErrorNumber = 0
    private var event = false
    private var age = false

    private var eventPhotoPosition = 0


    private var activityTypeId = ""

    private var shopName = ""
    private var website = ""
    private var activityName = ""
    private var descp = ""
    private var phonee = ""
    private var countryCodee = "+33"
    private var lastPos = 0

    private lateinit var updateEventList: ArrayList<ModelPOJO.AddEventDataModel>
    private var pos = -1

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

   private var selectedImages = mutableListOf("", "", "")

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

            listAge = ArrayList()
            listAge.add(ModelPOJO.AgeGroupDataModel("", "", "", "", ""))


            listEvent = ArrayList()
            listEvent.add(ModelPOJO.AddEventDataModel("", "", "", "", "",
                    "", "", "", "", "", ""))


        // clicks for images
        ivImg1.setOnClickListener(this)
        ivImg2.setOnClickListener(this)
        ivImg3.setOnClickListener(this)

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
                website = etWebsite.text.toString()
                 activityName = et_activityName.text.toString()
                 descp = et_description.text.toString()
                 phonee = et_phone.text.toString()

                if (!isImageSelected) {
                    myCustomToast(getString(R.string.media_missing_error))
                } else {
                    if (activityTypeId == "") {
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
                                                    media = JSONArray()
                                                    for (i in 0 until selectedImages.size) {
                                                        val json = JSONObject()
                                                        json.put("image", selectedImages[i])
                                                        media.put(json)
                                                    }

                                                    if (event) {
                                                        addEvent = JSONArray()
                                                        for (i in 0 until listEvent.size) {
                                                            val json = JSONObject()
                                                            json.put("image", listEvent[i].image.toString())
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
                                                        ageGroup = JSONArray()
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
                event = true
                eventErrorNumber =i
                eventErrorString
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

        uploadImageServer(imgPath)

        Log.d("selectedImagePath", "-------$imgPath")


    }


    private fun uploadImageServer(imgPath: String?) {

        val mFile: File?
        mFile = File(imgPath!!)
        val imageFile: RequestBody = mFile.asRequestBody("image/*".toMediaTypeOrNull())
        val photo: MultipartBody.Part?
        photo = MultipartBody.Part.createFormData("image", mFile.name, imageFile)
        imagePathList.add(0,photo)
        val type: RequestBody = "image".toRequestBody("text/plain".toMediaTypeOrNull())

        val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
        if (checkIfHasNetwork(this)) {
            appViewModel.sendUploadImageData(type, users, imagePathList)
            progressDialog.setProgressDialog()
        }
        else {
            showSnackBar(this, getString(R.string.no_internet_error))
        }
    }

    override fun addAgeGroupItem(list: ArrayList<ModelPOJO.AgeGroupDataModel>, position: Int) {
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

    override fun eventDataSet(type: String, value: String, position: Int) {
        when(type)
        {
            "name"->{ listEvent[position].name = value }
            "dateFrom"->{ listEvent[position].dateFrom = value }
            "dateTo"->{ listEvent[position].dateTo = value }
            "timeFrom"->{ listEvent[position].timeFrom = value }
            "timeTo"->{ listEvent[position].timeTo = value }
            "desc"->{ listEvent[position].description = value }
            "price"->{ listEvent[position].price = value }
        }
        addEventRepeatAdapter.notifyItemChanged(position)

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
            else -> {
                list.add(ModelPOJO.AddEventDataModel("", "", "", "", "", "",
                "", "", "", "", ""))
                addEventRepeatAdapter.notifyDataSetChanged()
            } }
    }

    override fun addCameraGalleryImage(list: ArrayList<ModelPOJO.AddEventDataModel>, position: Int) {
        imageSelectedType = "4"
        eventPhotoPosition = position
        checkPermission(this)
    }

    private fun hitTypeActivity() {
        launch(Dispatchers.Main.immediate) {
            authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
            appViewModel.sendActivityTypeData(security_key, authKey)
        }
    }


    val type: ArrayList<String> = ArrayList()
    val typeId: ArrayList<String> = ArrayList()

    private fun checkMvvmResponse() {
        appViewModel.observeActivityTypeResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
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
                    val arrayAdapter1 = ArrayAdapter(this, R.layout.customspinner, type as List<String>)
                    trader_type.adapter = arrayAdapter1
                    trader_type.onItemSelectedListener = object :OnItemSelectedListener{
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            activityTypeId = typeId[position]
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }
                    }
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
                                                .into(ivImg3!!)
                                        }
                                        "4" -> {
                                            listEvent[eventPhotoPosition].image = image
                                            addEventRepeatAdapter.notifyItemChanged(eventPhotoPosition)
                                        }
                                    }
                                    imageSelectedType = ""

                                }

                            }
                        }
                            else {

                                ErrorBodyResponse(response, this, null)
                            }
        })

        appViewModel.getException()!!.observe(this, androidx.lifecycle.Observer {
            myCustomToast(it)
            progressDialog.hidedialog()
        })
    }

    private fun hitFinallyActivityAddPostApi() {

        Log.d("datahtgfhtyhty=====", "-----00000-----${ageGroup.length()}------${addEvent.length()}---")
        Log.d("datahtgfhtyhty=====", "-----1111-----${ageGroup}-------")
        Log.d("datahtgfhtyhty=====", "-----22222-----${addEvent}-------")
        var typeEmpty = ""
        Log.e("datahtgfhtyhty","======$event====$age======")
        when {
            event && age -> { typeEmpty = "0"   }
            !age && event -> { typeEmpty = "3" }
            age && !event -> { typeEmpty = "1" }
            !age && !event -> { typeEmpty = "2" }
        }

            appViewModel.send_addPostActivity_Data(security_key, authKey, "1", activityTypeId, shopName, activityName,
                    descp, phonee, cityAddress, cityName,website, addressLatitude, addressLongitude, ageGroup.toString(),
                    addEvent.toString(), media.toString(), countryCodee,typeEmpty)

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


}