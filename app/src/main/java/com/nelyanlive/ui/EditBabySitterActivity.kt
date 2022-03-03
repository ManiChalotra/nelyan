package com.nelyanlive.ui

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.data.network.responsemodels.ImageUploadApiResponseModel
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.myAd.ChildCareImageMyAds
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_baby_sitter.*
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

class EditBabySitterActivity : OpenCameraGallery(), View.OnClickListener, CoroutineScope {

    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(
        AppViewModel::class.java) }


    private var imageSelectedType = ""
    private val job by lazy { kotlinx.coroutines.Job() }
    private val dataStoragePreference by lazy { DataStoragePreference(this) }
    private var countryCodee = "91"
    private var childCareId = ""

    private var authKey = ""

    private var imagePathList = ArrayList<MultipartBody.Part>()

    private var progressDialog = ProgressDialog(this)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    //private val googleMapKey = "AIzaSyDQWqIXO-sNuMWupJ7cNNItMhR4WOkzXDE"
    private val googleMapKey1 = "AIzaSyD"
    private val googleMapKey2 = "QWqIXO-sNuM"
    private val googleMapKey3 = "WupJ7cNNIt"
    private val googleMapKey4 = "MhR4WOkzXDE"
    private val placePickerRequest = 1

    private var cityName = ""
    private var cityLatitude = ""
    private var cityLongitude = ""

    var image1: String=""
    var image2: String=""
    var image3: String=""

    private var postID = ""
    private lateinit var childimageList: ArrayList<ChildCareImageMyAds>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baby_sitter)

        initializeClicks()
        childimageList = ArrayList()

        sp_child_care_type.visibility = View.VISIBLE

        launch(Dispatchers.Main.immediate) {
            authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
        }
    }

    private fun hitChildCareTypeApi() {
        if (checkIfHasNetwork(this@EditBabySitterActivity)) {
            launch(Dispatchers.Main.immediate) {
                authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                    .first()
                appViewModel.sendChildCareTypeData(security_key, authKey)
            }
        } else {
            showSnackBar(this@EditBabySitterActivity, getString(R.string.no_internet_error))

        }
    }


    private fun initializeClicks() {

        if (intent.extras != null) {
            childimageList = intent.getSerializableExtra("childCareImageList") as ArrayList<ChildCareImageMyAds>
            childCareId = intent.getStringExtra("childType").toString()
            et_maternalName.setText(intent.getStringExtra("name").toString())
            et_number_of_places.setText(intent.getStringExtra("noofplaces"))
            et_descriptionBabySitter.setText(intent.getStringExtra("description"))
            et_phoneNumber.setText(intent.getStringExtra("phoneNumber"))
            et_addressBabySitter.setText(intent.getStringExtra("address"))

            countryCodee = intent.getStringExtra("countryCode").toString()
            countycode_baby_sitter.setCountryForPhoneCode(countryCodee.toInt())

            postID = intent.getStringExtra("adID").toString()
            cityLatitude = intent.getStringExtra("latitude").toString()
            cityLongitude = intent.getStringExtra("longitude").toString()
            cityName = intent.getStringExtra("city").toString()

            hitChildCareTypeApi()

            childimageList = ArrayList()
            childimageList = intent.getSerializableExtra("childCareImageList") as ArrayList<ChildCareImageMyAds>

            if (!childimageList.isNullOrEmpty()) {
                when (childimageList.size) {
                    1 -> {
                        image1 = childimageList[0].image
                        Glide.with(this).load(image_base_URl + childimageList[0].image).error(R.mipmap.no_image_placeholder).into(ivImg1)

                    }
                    2 -> {
                        image1 = childimageList[0].image
                        image2 = childimageList[1].image
                        Glide.with(this).load(image_base_URl + childimageList[0].image).error(R.mipmap.no_image_placeholder).into(ivImg1)
                        Glide.with(this).load(image_base_URl + childimageList[1].image).error(R.mipmap.no_image_placeholder).into(ivImg2)

                    }
                    3 -> {
                        image1 = childimageList[0].image
                        image2 = childimageList[1].image
                        image3 = childimageList[2].image
                        Glide.with(this).load(image_base_URl + childimageList[0].image).error(R.mipmap.no_image_placeholder).into(ivImg1)
                        Glide.with(this).load(image_base_URl + childimageList[1].image).error(R.mipmap.no_image_placeholder).into(ivImg2)
                        Glide.with(this).load(image_base_URl + childimageList[2].image).error(R.mipmap.no_image_placeholder).into(ivImg)

                    }
                }

                launch(Dispatchers.Main.immediate) {
                    authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
                }
            }
        }

        ivImg.setOnClickListener(this)
        ivImg1.setOnClickListener(this)
        ivImg2.setOnClickListener(this)
        ivBackBabaySitter.setOnClickListener(this)
        btnSubmitBabySitter.setOnClickListener(this)

        et_addressBabySitter.isFocusable = false
        et_addressBabySitter.isFocusableInTouchMode = false
        et_addressBabySitter.setOnClickListener(this)

        countycode_baby_sitter.setOnCountryChangeListener {
            countryCodee = countycode_baby_sitter.selectedCountryCode.toString()
        }
        checkMvvmResponse()

        /**
         * @author Pardeep Sharma
         * to make the edittext scrollable
         */
        CommonMethodsKotlin().scrollableEditText(et_descriptionBabySitter,R.id.et_descriptionBabySitter)

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
            R.id.ivImg -> {
                imageSelectedType = "3"
                checkPermission(this)
            }
            R.id.ivBackBabaySitter -> {
                onBackPressed()
            }
            R.id.btnSubmitBabySitter -> {

                if (childCareId.isEmpty()) {
                    myCustomToast(getString(R.string.child_care_type_missing_error))
                }
                else {
                    if (et_maternalName.text.toString().isEmpty()) {
                        myCustomToast(getString(R.string.please_enter_name))
                    } else {
                        if (et_number_of_places.text.toString().isEmpty()) {
                            myCustomToast(getString(R.string.places_number_error))
                        } else {
                                if (et_addressBabySitter.text.toString().isEmpty()) {
                                    myCustomToast(getString(R.string.address_missing_error))
                                }
                                else {
                                    if (et_descriptionBabySitter.text.toString().isEmpty()) {
                                        myCustomToast(getString(R.string.description_missing))
                                    } else {

                                        hitFinallyActivityAddPostApi()
                                    }
                                } } } } }

            R.id.et_addressBabySitter -> {
                showPlacePicker()
            }
        } }

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

    private val typeList: ArrayList<String> = ArrayList()
    private val typeListId: ArrayList<String> = ArrayList()
    private var selectedPosition = 0

    private fun checkMvvmResponse() {

        appViewModel.observeChildCareTypeResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {

                    val jsonObject = JSONObject(response.body().toString())
                    val mSizeOfData = jsonObject.getJSONArray("data").length()

                    val childCareTypeJsonArray = jsonObject.getJSONArray("data")

                                        typeList.clear()
                                        typeListId.clear()
                                        typeList.add("")
                                        typeListId.add("")
                                        for (i in 0 until mSizeOfData) {
                                            val name = childCareTypeJsonArray.getJSONObject(i).get("categoryName").toString()
                                            val id = childCareTypeJsonArray.getJSONObject(i).get("id").toString()
                                            typeList.add(name)
                                            typeListId.add(id)
                                            if(id==childCareId)
                                            { selectedPosition = i }
                                        }
                                        val arrayAdapte1 = ArrayAdapter(this, R.layout.customspinner, typeList)
                                        sp_child_care_type.adapter = arrayAdapte1
                                        sp_child_care_type.setSelection(selectedPosition+1)

                    sp_child_care_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) { childCareId = typeListId[position] }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    } }

            } else {
                ErrorBodyResponse(response, this, null)
            }
        })


        appViewModel.observeUploadImageResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
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
                                    .into(ivImg!!)
                            }
                        }
                        imageSelectedType = ""
                    } } }
                else {

                    ErrorBodyResponse(response, this, null)
                }
        })

        appViewModel.observe_editMaternalPost_Response()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("EditMaternalPostResponse", "-----------" + Gson().toJson(response.body()))
                if (response.body() != null) {
                    progressDialog.hidedialog()
                    finishAffinity()
                    OpenActivity(HomeActivity::class.java)
                    myCustomToast(getString(R.string.post_updated_success))
                }
            } else {
                progressDialog.hidedialog()
                ErrorBodyResponse(response, this, null)
            }
        })


        appViewModel.getException()!!.observe(this, androidx.lifecycle.Observer {
            myCustomToast(it)
            progressDialog.hidedialog()
        })
    }


    private fun hitFinallyActivityAddPostApi() {
        if (checkIfHasNetwork(this)) {
            appViewModel.editMaternalPost_Data(security_key, authKey, "2", childCareId, et_maternalName.text.toString(), "hah@gmail.com", "",
                et_number_of_places.text.toString(), countryCodee, et_phoneNumber.text.toString(), et_addressBabySitter.text.toString(), et_descriptionBabySitter.text.toString(), cityName, cityLatitude, cityLongitude,
                 image1, image2, image3, postID)
        } else {
            showSnackBar(this, getString(R.string.no_internet_error))
        }
    }


    private fun showPlacePicker() {
        val placeKey= googleMapKey1+googleMapKey2+googleMapKey3+googleMapKey4
        // Initialize Places.
        Places.initialize(
            applicationContext,
            placeKey
        )
        val fields: List<Place.Field> = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, placePickerRequest)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == placePickerRequest) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)

                cityLatitude = place.latLng?.latitude.toString()
                cityLongitude = place.latLng?.longitude.toString()

                val geocoder = Geocoder(this, Locale.getDefault())
                val list = geocoder.getFromLocation(place.latLng?.latitude!!.toDouble(), place.latLng?.longitude!!.toDouble(), 1)
                cityName = if(!list[0].locality.isNullOrBlank()) {list[0].locality} else{place.name.toString() }
                et_addressBabySitter.setText(place.address.toString())

            } } }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


}