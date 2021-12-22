package com.nelyanlive.ui

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
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

class EditActivity : OpenCameraGallery(), View.OnClickListener,
    CoroutineScope, AgeGroupEditAdapter.OnAgeGroupRecyclerViewItemClickListener,
    EventEditAdapter.OnEventRecyclerViewItemClickListener {

    private val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(AppViewModel::class.java)
    }
    private var imageSelectedType = ""

    var image1: String = ""
    var image2: String = ""
    var image3: String = ""

    private val job by lazy { kotlinx.coroutines.Job() }

    private val dataStoragePreference by lazy { DataStoragePreference(this@EditActivity) }

    private val googleMapKey = "AIzaSyDQWqIXO-sNuMWupJ7cNNItMhR4WOkzXDE"
    private val placePickerRequestCode = 1
    private val addEventRequestCode = 2
    private var imagePathList = ArrayList<MultipartBody.Part>()

    // json Array
    private var ageGroup: JSONArray = JSONArray()
    private var addEvent: JSONArray = JSONArray()

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
    private var authKey = ""
    private var postID = ""

    private var eventPhotoPosition = 0

    private var activityTypeId = ""
    private var countryCodee = ""

    private val typeList: ArrayList<String> = ArrayList()
    private val typeListId: ArrayList<String> = ArrayList()
    private var selectedPosition = 0

    var ageErrorNumber = 0
    var eventErrorNumber = 0
    private var event = false
    private var age = false

    private lateinit var updateEventList: ArrayList<EventMyAds>
    private var pos = -1

    var clickPosition: String = ""

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

        Log.d("onCreate_EditActivity   ", "onCreate_EditActiivty   ")
        countycode.setOnCountryChangeListener {
            countryCodee = countycode.selectedCountryCode.toString()
        }
    }

    private fun initializeClicks() {

        if (intent.extras != null) {

            et_shopName.setText(intent.getStringExtra("nameofShop"))
            et_activityName.setText(intent.getStringExtra("nameofActivity"))
            et_description.setText(intent.getStringExtra("description"))
            et_phone.setText(intent.getStringExtra("phoneNumber"))
            et_addressActivity.text = intent.getStringExtra("address")
            etWebsite.setText(intent.getStringExtra("website"))
            addressLatitude = intent.getStringExtra("latti")!!
            cityName = intent.getStringExtra("city")!!
            addressLongitude = intent.getStringExtra("longi")!!
            countryCodee = intent.getStringExtra("countryCode").toString()
            countycode.setCountryForPhoneCode(countryCodee.toInt())
            postID = intent.getStringExtra("adID").toString()
            activityTypeId = intent.getStringExtra("activityTypeId").toString()
            edtAgeFrom.setText(intent.getStringExtra("minage"))
            edtAgeTo.setText(intent.getStringExtra("maxage"))
            Log.d(
                EditActivity::class.java.name,
                "EditActivity_ShopName   " + intent.getStringExtra("nameofShop")
            )
        }

        activityimageList = ArrayList()
        activityimageList =
            intent.getParcelableArrayListExtra<ActivityimageMyAds>("activityimagesList")!!

        if (!activityimageList.isNullOrEmpty()) {
            when (activityimageList.size) {
                1 -> {
                    Glide.with(this).load(image_base_URl + activityimageList[0].images)
                        .error(R.mipmap.no_image_placeholder).into(ivImg1)
                    image1 = activityimageList[0].images


                }
                2 -> {
                    Glide.with(this).load(image_base_URl + activityimageList[0].images)
                        .error(R.mipmap.no_image_placeholder).into(ivImg1)
                    Glide.with(this).load(image_base_URl + activityimageList[1].images)
                        .error(R.mipmap.no_image_placeholder).into(ivImg2)
                    image1 = activityimageList[0].images
                    image2 = activityimageList[1].images
                }
                3 -> {
                    Glide.with(this).load(image_base_URl + activityimageList[0].images)
                        .error(R.mipmap.no_image_placeholder).into(ivImg1)
                    Glide.with(this).load(image_base_URl + activityimageList[1].images)
                        .error(R.mipmap.no_image_placeholder).into(ivImg2)
                    Glide.with(this).load(image_base_URl + activityimageList[2].images)
                        .error(R.mipmap.no_image_placeholder).into(ivImg3)
                    image1 = activityimageList[0].images
                    image2 = activityimageList[1].images
                    image3 = activityimageList[2].images
                }
            }
        }

        listAgeGroupDataModel = ArrayList()
        listAgeGroupDataModel = intent.getParcelableArrayListExtra<AgeGroupMyAds>("ageGroupList")!!

        if (listAgeGroupDataModel.isEmpty()) listAgeGroupDataModel.add(
            AgeGroupMyAds(
                0,
                "",
                "",
                "",
                "",
                0,
                0,
                "",
                "",
                ""
            )
        )

        ageGroupEditAdapter = AgeGroupEditAdapter(this, listAgeGroupDataModel, this)
        rvAgeGroup!!.adapter = ageGroupEditAdapter

        listAddEventDataModel = ArrayList()
        listAddEventDataModel = intent.getParcelableArrayListExtra<EventMyAds>("eventMyAdsList")!!

        Log.d(EditActivity::class.java.name, "EditActivity_eventlist   " + listAddEventDataModel)
        Log.d(EditActivity::class.java.name, "EditActivity_agelist   " + listAgeGroupDataModel)

        if (listAgeGroupDataModel.isNullOrEmpty()) {
            rvAgeGroup.visibility = View.GONE
            tvAddAgeGroup.visibility = View.VISIBLE
        }

        if (listAddEventDataModel.isNullOrEmpty()) {
            rvEvent.visibility = View.GONE
            tvAddEvent.visibility = View.VISIBLE
        }

        if (listAddEventDataModel.isEmpty()) listAddEventDataModel.add(
            EventMyAds(
                0,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                0,
                "",
                "",
                "",
                "",
                "",
                0,
                "",
                0
            )
        )

        eventEditAdapter = EventEditAdapter(this, listAddEventDataModel, this)
        rvEvent!!.adapter = eventEditAdapter

        ivImg1.setOnClickListener(this)
        ivImg2.setOnClickListener(this)
        ivImg3.setOnClickListener(this)

        btnSubmit.setOnClickListener(this)
        tvAddAgeGroup.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        et_addressActivity.setOnClickListener(this)
        tvAddEvent.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.ivImg1 -> {
                imageSelectedType = "1"
                checkPermission(this)
            }
            R.id.ivImg2 -> {
                imageSelectedType = "2"
                checkPermission(this)
            }
            R.id.ivImg3 -> {
                imageSelectedType = "3"
                checkPermission(this)
            }
            R.id.tvAddAgeGroup -> {
                Log.e("EventRepearAdapter_size", "--Activity_age--" + listAgeGroupDataModel.size)
                if (listAgeGroupDataModel.size == 0) {
                    listAgeGroupDataModel.add(
                        AgeGroupMyAds(
                            0,
                            "",
                            "",
                            "",
                            "",
                            0,
                            0,
                            "",
                            "",
                            ""
                        )
                    )
                    rvAgeGroup.visibility = View.VISIBLE
                    tvAddAgeGroup.visibility = View.GONE
                    ageGroupEditAdapter.notifyDataSetChanged()

                } else {
                    tvAddAgeGroup.visibility = View.GONE
                }
            }

            R.id.tvAddEvent -> {
                Log.e("EventRepearAdapter_size", "--Activity--" + listAddEventDataModel.size)
                if (listAddEventDataModel.size == 0) {
                    listAddEventDataModel.add(
                        EventMyAds(
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            0
                        )
                    )
                    rvEvent.visibility = View.VISIBLE
                    tvAddEvent.visibility = View.GONE
                    eventEditAdapter.notifyDataSetChanged()

                } else {
                    tvAddEvent.visibility = View.GONE
                }
            }

            R.id.btnSubmit -> {

                if (activityTypeId == "") {
                    myCustomToast(getString(R.string.activity_type_missing_error))
                } else {
                    if (et_shopName.text.toString().isEmpty()) {
                        myCustomToast(getString(R.string.shop_name_missing_error))
                    } else {
                        if (et_activityName.text.toString().isEmpty()) {
                            myCustomToast(getString(R.string.activity_name_missing_error))
                        } else {
                            if (et_description.text.toString().isEmpty()) {
                                myCustomToast(getString(R.string.description_missing))
                            } else {
                                if (et_addressActivity.text.toString().isEmpty()) {
                                    myCustomToast(getString(R.string.address_missing_error))
                                } else {
                                    if (edtAgeFrom.text.toString()
                                            .isEmpty() && edtAgeTo.text.toString().isEmpty()
                                    ) {
                                        myCustomToast(getString(R.string.select_age_text))
                                    } else {
                                        if (edtAgeFrom.text.toString().isEmpty()) {
                                            myCustomToast(getString(R.string.select_age_from))
                                        } else {
                                            if (edtAgeTo.text.toString().isEmpty()) {
                                                myCustomToast(getString(R.string.select_age_to))
                                            } /*else {
                                                if (edtAgeFrom.text.toString() > edtAgeTo.text.toString()) {
                                                    myCustomToast(getString(R.string.ageto_fromage_text))
                                                }*/ else {

                                                var ageErrorString = ""
                                                ageErrorNumber = 0
                                                var eventErrorString = ""
                                                eventErrorNumber = 0

                                                if (clickPosition.equals("0")) {
                                                    Log.d(
                                                        AddActivity::class.java.name,
                                                        "AddActivity_agegroup_if  "
                                                    )
                                                } else {

                                                    ageErrorString = getAgeError(
                                                        listAgeGroupDataModel[listAgeGroupDataModel.size - 1].ageFrom,
                                                        ageErrorString,
                                                        getString(R.string.fill_age_group_from_in_previous_data),
                                                        1
                                                    )
                                                    ageErrorString = getAgeError(
                                                        listAgeGroupDataModel[listAgeGroupDataModel.size - 1].ageTo,
                                                        ageErrorString,
                                                        getString(R.string.fill_age_group_to),
                                                        2
                                                    )
                                                    ageErrorString = getAgeError(
                                                        listAgeGroupDataModel[listAgeGroupDataModel.size - 1].days,
                                                        ageErrorString,
                                                        getString(R.string.select_days_in_previous),
                                                        3
                                                    )
                                                    ageErrorString = getAgeError(
                                                        listAgeGroupDataModel[listAgeGroupDataModel.size - 1].timeFrom,
                                                        ageErrorString,
                                                        getString(R.string.select_from_time),
                                                        4
                                                    )
                                                    ageErrorString = getAgeError(
                                                        listAgeGroupDataModel[listAgeGroupDataModel.size - 1].timeTo,
                                                        ageErrorString,
                                                        getString(R.string.select_to_time),
                                                        5
                                                    )
                                                }
                                                if (clickPosition.equals("0")) {

                                                } else {
                                                    eventErrorString = getEventError(
                                                        listAddEventDataModel[listAddEventDataModel.size - 1].image,
                                                        eventErrorString,
                                                        getString(R.string.select_image_in_previous),
                                                        1
                                                    )
                                                    eventErrorString = getEventError(
                                                        listAddEventDataModel[listAddEventDataModel.size - 1].name,
                                                        eventErrorString,
                                                        getString(R.string.fill_event_name_in_previous),
                                                        2
                                                    )
                                                    eventErrorString = getEventError(
                                                        listAddEventDataModel[listAddEventDataModel.size - 1].dateFrom,
                                                        eventErrorString,
                                                        getString(R.string.select_from_date_in_previous),
                                                        3
                                                    )
                                                    eventErrorString = getEventError(
                                                        listAddEventDataModel[listAddEventDataModel.size - 1].dateTo,
                                                        eventErrorString,
                                                        getString(R.string.select_to_date_in_previous),
                                                        4
                                                    )
                                                    eventErrorString = getEventError(
                                                        listAddEventDataModel[listAddEventDataModel.size - 1].startTime,
                                                        eventErrorString,
                                                        getString(R.string.select_from_time),
                                                        5
                                                    )
                                                    eventErrorString = getEventError(
                                                        listAddEventDataModel[listAddEventDataModel.size - 1].endTime,
                                                        eventErrorString,
                                                        getString(R.string.select_to_time),
                                                        6
                                                    )
                                                    eventErrorString = getEventError(
                                                        listAddEventDataModel[listAddEventDataModel.size - 1].description,
                                                        eventErrorString,
                                                        getString(R.string.fill_description_in_previous),
                                                        7
                                                    )
                                                    eventErrorString = getEventError(
                                                        listAddEventDataModel[listAddEventDataModel.size - 1].price,
                                                        eventErrorString,
                                                        getString(R.string.fill_price_previous),
                                                        8
                                                    )
                                                    eventErrorString = getEventError(
                                                        listAddEventDataModel[listAddEventDataModel.size - 1].city,
                                                        eventErrorString,
                                                        getString(R.string.fill_city_previous),
                                                        9
                                                    )

                                                }

//                                    var eventErrorString = ""
//                                    eventErrorNumber = 0
//                                    eventErrorString = getEventError(listAddEventDataModel[listAddEventDataModel.size - 1].image, eventErrorString, getString(R.string.select_image_in_previous), 1)
//                                    eventErrorString = getEventError(listAddEventDataModel[listAddEventDataModel.size - 1].name, eventErrorString, getString(R.string.fill_event_name_in_previous), 2)
//                                    eventErrorString = getEventError(listAddEventDataModel[listAddEventDataModel.size - 1].dateFrom, eventErrorString, getString(R.string.select_from_date_in_previous), 3)
//                                    eventErrorString = getEventError(listAddEventDataModel[listAddEventDataModel.size - 1].dateTo, eventErrorString, getString(R.string.select_to_date_in_previous), 4)
//                                    eventErrorString = getEventError(listAddEventDataModel[listAddEventDataModel.size - 1].startTime, eventErrorString, getString(R.string.select_from_time), 5)
//                                    eventErrorString = getEventError(listAddEventDataModel[listAddEventDataModel.size - 1].endTime, eventErrorString, getString(R.string.select_to_time), 6)
//                                    eventErrorString = getEventError(listAddEventDataModel[listAddEventDataModel.size - 1].description, eventErrorString, getString(R.string.fill_description_in_previous), 7)
//                                    eventErrorString = getEventError(listAddEventDataModel[listAddEventDataModel.size - 1].price, eventErrorString, getString(R.string.fill_price_previous), 8)
//                                    eventErrorString = getEventError(listAddEventDataModel[listAddEventDataModel.size - 1].city, eventErrorString, getString(R.string.fill_city_previous), 9)


                                                if (ageErrorNumber != 0 && ageErrorString.isNotEmpty()) {
                                                    myCustomToast(ageErrorString)
                                                } else {
                                                    if (eventErrorNumber != 0 && eventErrorString.isNotEmpty()) {
                                                        myCustomToast(eventErrorString)
                                                    } else {


                                                        if (age) {
                                                            ageGroup = JSONArray()
                                                            for (i in 0 until listAgeGroupDataModel.size) {
                                                                val json = JSONObject()
                                                                json.put(
                                                                    "age_from",
                                                                    listAgeGroupDataModel[i].ageFrom
                                                                )
                                                                json.put(
                                                                    "age_to",
                                                                    listAgeGroupDataModel[i].ageTo
                                                                )
                                                                json.put(
                                                                    "days",
                                                                    listAgeGroupDataModel[i].days
                                                                )
                                                                json.put(
                                                                    "time_from",
                                                                    listAgeGroupDataModel[i].timeFrom
                                                                )
                                                                json.put(
                                                                    "time_to",
                                                                    listAgeGroupDataModel[i].timeTo
                                                                )
                                                                ageGroup.put(json)
                                                            }
                                                        }
                                                        if (event) {
                                                            addEvent = JSONArray()
                                                            for (i in 0 until listAddEventDataModel.size) {
                                                                val json = JSONObject()
                                                                json.put(
                                                                    "image",
                                                                    listAddEventDataModel[i].image
                                                                )
                                                                json.put(
                                                                    "name",
                                                                    listAddEventDataModel[i].name
                                                                )
                                                                json.put("file_type", "image")
                                                                json.put(
                                                                    "date_from",
                                                                    listAddEventDataModel[i].dateFrom
                                                                )
                                                                json.put(
                                                                    "date_to",
                                                                    listAddEventDataModel[i].dateTo
                                                                )
                                                                json.put(
                                                                    "time_from",
                                                                    listAddEventDataModel[i].startTime
                                                                )
                                                                json.put(
                                                                    "time_to",
                                                                    listAddEventDataModel[i].endTime
                                                                )
                                                                json.put(
                                                                    "description",
                                                                    listAddEventDataModel[i].description
                                                                )
                                                                json.put(
                                                                    "price",
                                                                    listAddEventDataModel[i].price
                                                                )
                                                                json.put(
                                                                    "city",
                                                                    listAddEventDataModel[i].city
                                                                )
                                                                json.put(
                                                                    "lat",
                                                                    listAddEventDataModel[i].latitude
                                                                )
                                                                json.put(
                                                                    "lng",
                                                                    listAddEventDataModel[i].longitude
                                                                )
                                                                addEvent.put(json)
                                                            }
                                                        }
                                                        hitFinallyActivityAddPostApi()
                                                    }
                                                }
                                            }
//                                            }
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
                showPlacePicker(placePickerRequestCode)
            }
        }
    }


    private fun getAgeError(ageFrom: String?, ageErrorString: String, s: String, i: Int): String {

        return when {
            ageFrom.isNullOrBlank() -> when {
                ageErrorString.isBlank() -> {
                    s
                }
                else -> ageErrorString
            }
            else -> {
                age = true
                ageErrorNumber = i
                ageErrorString
            }
        }
    }

    private fun getEventError(
        ageFrom: String?,
        eventErrorString: String,
        s: String,
        i: Int
    ): String {

        return when {
            ageFrom.isNullOrBlank() -> when {
                eventErrorString.isBlank() -> {
                    s
                }
                else -> eventErrorString
            }
            else -> {
                event = true
                eventErrorNumber = i
                eventErrorString
            }
        }
    }

    private fun showPlacePicker(code: Int) {
        Places.initialize(applicationContext, googleMapKey)
        val fields: List<Place.Field> =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent: Intent =
            Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            placePickerRequestCode -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val place = Autocomplete.getPlaceFromIntent(data!!)
                        et_addressActivity.text = place.address.toString()
                        addressLatitude = place.latLng?.latitude.toString()
                        addressLongitude = place.latLng?.longitude.toString()
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

                        Log.i(
                            "dddddd",
                            "Place: " + place.name + ", " + place.id + "," + place.address + "," + place.latLng
                        )
                    }
                }
            }
            addEventRequestCode -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val place = Autocomplete.getPlaceFromIntent(data!!)
                        cityLatitude = place.latLng?.latitude.toString()
                        cityLongitude = place.latLng?.longitude.toString()
                        updateEventList[pos].latitude = cityLatitude
                        updateEventList[pos].longitude = cityLongitude
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val list = geocoder.getFromLocation(
                            place.latLng?.latitude!!.toDouble(),
                            place.latLng?.longitude!!.toDouble(),
                            1
                        )
                        updateEventList[pos].city = if (!list[0].locality.isNullOrBlank()) {
                            list[0].locality
                        } else {
                            place.name.toString()
                        }

                        eventEditAdapter.notifyDataSetChanged()
                    }
                }
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

    override fun onRemoveAgeItem(position: Int, list: Int) {
        Log.d(
            EditActivity::class.java.name,
            "OnRemoveClick_Age   " + position + "    clickPosition   " + clickPosition + "   ListSize  " + list
        )
        event = true
        clickPosition = position.toString()
        ageGroupEditAdapter.notifyItemChanged(position)
        if (list == 0) {
            rvAgeGroup.visibility = View.GONE
            tvAddAgeGroup.visibility = View.VISIBLE
        } else {

        }
    }

    override fun addAgeGroupItem(list: ArrayList<AgeGroupMyAds>, position: Int) {

        when {
            listAgeGroupDataModel[list.size - 1].ageFrom.isNullOrEmpty() -> {
                myCustomToast(getString(R.string.fill_age_group_from_in_previous_data))
            }
            listAgeGroupDataModel[list.size - 1].ageTo.isEmpty() -> {
                myCustomToast(getString(R.string.fill_age_group_to))
            }
            listAgeGroupDataModel[list.size - 1].days.isEmpty() -> {
                myCustomToast(getString(R.string.select_days_in_previous))
            }
            listAgeGroupDataModel[list.size - 1].timeFrom.isEmpty() -> {
                myCustomToast(getString(R.string.select_from_time))
            }
            listAgeGroupDataModel[list.size - 1].timeTo.isEmpty() -> {
                myCustomToast(getString(R.string.select_to_time))
            }
            else -> {
                listAgeGroupDataModel.add(AgeGroupMyAds(0, "", "", "", "", 0, 0, "", "", ""))
                ageGroupEditAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun cityAddEvent(list: ArrayList<EventMyAds>, position: Int, city: TextView) {
        updateEventList = list
        pos = position
        showPlacePicker(addEventRequestCode)
    }

    override fun onRemoveEventItem(position: Int, list: Int) {
        age = true
        Log.d(
            EditActivity::class.java.name,
            "OnRemoveClick   " + position + "clickPosition   " + clickPosition
        )
        clickPosition = position.toString()
        eventEditAdapter.notifyItemChanged(position)

        if (list == 0) {
            rvEvent.visibility = View.GONE
            tvAddEvent.visibility = View.VISIBLE
        } else {

        }

    }

    override fun onAddEventItem(list: ArrayList<EventMyAds>, position: Int) {

        when {
            listAddEventDataModel[list.size - 1].image.isEmpty() -> {
                myCustomToast(getString(R.string.select_image_in_previous))
            }
            listAddEventDataModel[list.size - 1].name.isEmpty() -> {
                myCustomToast(getString(R.string.fill_event_name_in_previous))
            }
            listAddEventDataModel[list.size - 1].dateFrom.isEmpty() -> {
                myCustomToast(getString(R.string.select_from_date_in_previous))
            }
            listAddEventDataModel[list.size - 1].dateTo.isEmpty() -> {
                myCustomToast(getString(R.string.select_to_date_in_previous))
            }
            listAddEventDataModel[list.size - 1].startTime.isEmpty() -> {
                myCustomToast(getString(R.string.select_from_time))
            }
            listAddEventDataModel[list.size - 1].endTime.isEmpty() -> {
                myCustomToast(getString(R.string.select_to_time))
            }
            listAddEventDataModel[list.size - 1].description.isEmpty() -> {
                myCustomToast(getString(R.string.fill_description_in_previous))
            }
            listAddEventDataModel[list.size - 1].price.isEmpty() -> {
                myCustomToast(getString(R.string.fill_price_previous))
            }
            listAddEventDataModel[list.size - 1].city.isEmpty() -> {
                myCustomToast(getString(R.string.fill_city_previous))
            }
            else -> {
                listAddEventDataModel.add(
                    EventMyAds(
                        0,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        0,
                        "",
                        "",
                        "",
                        "",
                        "",
                        0,
                        "",
                        0
                    )
                )
                eventEditAdapter.notifyDataSetChanged()
            }
        }

    }

    override fun addCameraGalleryImage(position: Int) {
        eventPhotoPosition = position
        imageSelectedType = "4"
        checkPermission(this)
    }

    private fun hitTypeActivityApi() {
        launch(Dispatchers.Main.immediate) {
            authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                .first()
            appViewModel.sendActivityTypeData(security_key, authKey)
        }
    }


    private fun checkMvvmResponse() {
        appViewModel.observeActivityTypeResponse()!!
            .observe(this, androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    if (response.body() != null) {
                        val jsonObject = JSONObject(response.body().toString())
                        val jsonArray = jsonObject.getJSONArray("data")

                        Log.d(EditActivity::class.java.name, "EditActivity_jsonArray  " + jsonArray)
                        typeList.clear()
                        typeListId.clear()
                        typeList.add("")
                        typeListId.add("")
                        for (i in 0 until jsonArray.length()) {
                            val name = jsonArray.getJSONObject(i).get("name").toString()
                            val id = jsonArray.getJSONObject(i).get("id").toString()
                            typeList.add(name)
                            typeListId.add(id)
                            if (id == activityTypeId) {
                                selectedPosition = i
                            }
                        }
                        val arrayAdapter = ArrayAdapter(this, R.layout.customspinner, typeList)
                        trader_type.adapter = arrayAdapter
                        trader_type.setSelection(selectedPosition + 1)

                        trader_type.onItemSelectedListener = object : OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View,
                                position: Int,
                                id: Long
                            ) {
                                activityTypeId = typeListId[position]
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                            }
                        }

                    }
                } else {
                    ErrorBodyResponse(response, this, null)
                }
            })

        appViewModel.observe_editActivity_Response()!!
            .observe(this, androidx.lifecycle.Observer { response ->

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
                                        .into(ivImg3!!)
                                }
                                "4" -> {
                                    listAddEventDataModel[eventPhotoPosition].image = image
                                    eventEditAdapter.notifyItemChanged(eventPhotoPosition)
                                }
                            }
                            imageSelectedType = ""

                        }

                    }
                } else {

                    ErrorBodyResponse(response, this, null)
                }

            })

        appViewModel.getException()!!.observe(this, androidx.lifecycle.Observer {
            myCustomToast(it)
            progressDialog.hidedialog()
        })

    }

    private fun hitFinallyActivityAddPostApi() {

        var typeEmpty = ""
        when {
            event && age -> {
                typeEmpty = "1"
            }
            !age && event -> {
                typeEmpty = "2"
            }
            age && !event -> {
                typeEmpty = "3"
            }
            !age && !event -> {
                typeEmpty = "4"
            }
        }

        if (clickPosition.equals("0")) {
            appViewModel.send_editActivity_Data(
                security_key,
                authKey,
                postID,
                "1",
                activityTypeId,
                et_shopName.text.toString(),
                et_activityName.text.toString(),
                et_description.text.toString(),
                et_phone.text.toString(),
                et_addressActivity.text.toString(),
                edtAgeFrom.text.toString(),
                edtAgeTo.text.toString(),
                etWebsite.text.toString(),
                cityName,
                addressLatitude,
                addressLongitude,
                ageGroup.toString(),
                "",
                countryCodee,
                image1,
                image2,
                image3,
                typeEmpty
            )
            Log.d(
                AppViewModel::class.java.name,
                "EditActivity_1020      " + typeEmpty + "  age_1020  " + age
            )
        } else {
            appViewModel.send_editActivity_Data(
                security_key,
                authKey,
                postID,
                "1",
                activityTypeId,
                et_shopName.text.toString(),
                et_activityName.text.toString(),
                et_description.text.toString(),
                et_phone.text.toString(),
                et_addressActivity.text.toString(),
                edtAgeFrom.text.toString(),
                edtAgeTo.text.toString(),
                etWebsite.text.toString(),
                cityName,
                addressLatitude,
                addressLongitude,
                ageGroup.toString(),
                addEvent.toString(),
                countryCodee,
                image1,
                image2,
                image3,
                typeEmpty
            )
            Log.d(
                AppViewModel::class.java.name,
                "EditActivity_1047      " + typeEmpty + " Age_1049  " + age
            )
        }
        appViewModel.send_editActivity_Data(
            security_key,
            authKey,
            postID,
            "1",
            activityTypeId,
            et_shopName.text.toString(),
            et_activityName.text.toString(),
            et_description.text.toString(),
            et_phone.text.toString(),
            et_addressActivity.text.toString(),
            edtAgeFrom.text.toString(),
            edtAgeTo.text.toString(),
            etWebsite.text.toString(),
            cityName,
            addressLatitude,
            addressLongitude,
            ageGroup.toString(),
            addEvent.toString(),
            countryCodee,
            image1,
            image2,
            image3,
            typeEmpty
        )
        Log.d(
            AppViewModel::class.java.name,
            "EditActivity_1074      " + typeEmpty + " age_1077  " + age
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


}