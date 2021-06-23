package com.nelyanlive.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.nelyanlive.modals.childcaretype.ChildCareTypeRespone
import com.nelyanlive.modals.myAd.ChildCareImageMyAds
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_baby_sitter.*
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

class EditBabySitterActivity : OpenCameraGallery(), View.OnClickListener, CoroutineScope {

    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(
        AppViewModel::class.java) }


    private var IMAGE_SELECTED_TYPE = ""
    private val job by lazy { kotlinx.coroutines.Job() }
    private val dataStoragePreference by lazy { DataStoragePreference(this) }
    private var media: JSONArray = JSONArray()
    private var countryCodee = "91"
    private var childCareId = ""

    private var authKey = ""
    private var maternalName = ""
    private var placeSpin = ""
    private var phoneNumber = ""
    private var address_baby_sitter = ""
    private var descp_baby_sitter = ""

    private var selectedUrlListing: ArrayList<String> = ArrayList()
    private var urlListingFromResponse: ArrayList<String> = ArrayList()
    var imageVideoUrlListing = arrayListOf("", "", "", "", "")



    private var imagePathList = ArrayList<MultipartBody.Part>()

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

    var image1: String=""
    var image2: String=""
    var image3: String=""
    var modelTypes:MutableList<ChildCareTypeRespone.Data>? =null

    var clickImg1: Boolean= false
    var clickImg2: Boolean= false
    var clickImg3: Boolean= false
    private var postID = ""
    private lateinit var childimageList: ArrayList<ChildCareImageMyAds>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baby_sitter)
        modelTypes = ArrayList()

        initalizeClicks()
        childimageList = ArrayList()

        sp_child_care_type.visibility = View.VISIBLE






        launch(Dispatchers.Main.immediate) {
            authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
        }

    }

    private fun hitChildCareType_Api() {
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


    private fun initalizeClicks() {

        if (intent.extras != null) {
            childimageList = intent.getSerializableExtra("childCareImageList") as ArrayList<ChildCareImageMyAds>
            childCareId = intent.getStringExtra("childType").toString()
            et_maternalName.setText(intent.getStringExtra("name").toString())
            et_number_of_places.setText(intent.getStringExtra("noofplaces"))
            et_descriptionBabySitter.setText(intent.getStringExtra("description"))
            et_phoneNumber.setText(intent.getStringExtra("phoneNumber"))
            et_addressBabySitter.setText(intent.getStringExtra("city"))

            countryCodee = intent.getStringExtra("countryCode").toString()
            countycode_baby_sitter.setCountryForPhoneCode(countryCodee.toInt())

            postID = intent.getStringExtra("adID").toString()
            cityLatitude = intent.getStringExtra("latitude").toString()
            cityLongitude = intent.getStringExtra("longitude").toString()
            cityAddress = intent.getStringExtra("city").toString()


            hitChildCareType_Api()






            childimageList = ArrayList()
            childimageList = intent.getSerializableExtra("childCareImageList") as ArrayList<ChildCareImageMyAds>

            if (!childimageList.isNullOrEmpty()) {
                when (childimageList.size) {
                    1 -> {
                        image1 = childimageList[0].image
                        image2 = ""
                        image3 = ""
                        Glide.with(this).load(image_base_URl + childimageList[0].image).error(R.mipmap.no_image_placeholder).into(ivImg1)

                    }
                    2 -> {
                        image1 = childimageList[0].image
                        image2 = childimageList[1].image
                        image3 = ""
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
        ivImg3.setOnClickListener(this)
        ivplus.setOnClickListener(this)
        ivBackBabaySitter.setOnClickListener(this)
        btnSubmitBabySitter.setOnClickListener(this)

        et_addressBabySitter.isFocusable = false
        et_addressBabySitter.isFocusableInTouchMode = false
        et_addressBabySitter.setOnClickListener(this)

        countycode_baby_sitter.setOnCountryChangeListener {
            countryCodee = countycode_baby_sitter.selectedCountryCode.toString()
        }
        checkMvvmResponse()

    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.ivImg1 -> {
                clickImg1 = true
                clickImg2 = false
                clickImg3 = false
                IMAGE_SELECTED_TYPE = "1"
                checkPermission(this)
            }
            R.id.ivImg2 -> {
                clickImg1 = false
                clickImg2 = true
                clickImg3 = false
                IMAGE_SELECTED_TYPE = "2"
                checkPermission(this)
            }
            R.id.ivImg -> {
                clickImg1 = false
                clickImg2 = false
                clickImg3 = true
                IMAGE_SELECTED_TYPE = "3"
                checkPermission(this)
            }
            R.id.ivBackBabaySitter -> {
                onBackPressed()
            }
            R.id.btnSubmitBabySitter -> {
                maternalName = et_maternalName.text.toString()
                placeSpin = et_number_of_places.text.toString()
                phoneNumber = et_phoneNumber.text.toString()
                address_baby_sitter = et_addressBabySitter.text.toString()
                descp_baby_sitter = et_descriptionBabySitter.text.toString()


                if (childCareId.isEmpty()) {
                    myCustomToast(getString(R.string.child_care_type_missing_error))
                } else {
                    if (maternalName.isEmpty()) {
                        myCustomToast(getString(R.string.maternal_missing_error))
                    } else {
                        if (placeSpin.isEmpty()) {
                            myCustomToast(getString(R.string.places_number_error))
                        } else {
                            if (phoneNumber.isEmpty()) {
                                myCustomToast(getString(R.string.phone_number_missing))
                            } else {
                                if (address_baby_sitter.isEmpty()) {
                                    myCustomToast(getString(R.string.address_missing_error))
                                } else {
                                    if (descp_baby_sitter.isEmpty()) {
                                        myCustomToast(getString(R.string.description_missing))
                                    } else {

                                        // checking the list
                                        if (selectedUrlListing.size == urlListingFromResponse.size) {
                                            selectedUrlListing.clear()
                                            urlListingFromResponse.clear()
                                        }

                                        Log.d("imageVideoListSize",
                                            "-----------$imageVideoUrlListing"
                                        )

                                        // rotating loop
                                        for (i in 0 until imageVideoUrlListing.size) {
                                            val media = imageVideoUrlListing[i]
                                            if (!media.isEmpty()) {
                                                selectedUrlListing.add(media)
                                            }
                                        }
                                        Log.d("selectedUpperimages",
                                            "-------------------$selectedUrlListing"
                                        )


                                        hitFinallyActivityAddPostApi()

                                    }
                                }
                            }
                        }

                    }

                }


            }


            R.id.et_addressBabySitter -> {
                showPlacePicker()
            }
        }
    }

    override fun getRealImagePath(imgPath: String?) {

        when (IMAGE_SELECTED_TYPE) {
            "1" -> {
                setImageOnTab(imgPath, ivImg1)
                imageVideoUrlListing[0] = imgPath.toString()
                iv_image1.visibility = View.GONE
            }

            "2" -> {
                setImageOnTab(imgPath, ivImg2)
                imageVideoUrlListing[1] = imgPath.toString()
            }

            "3" -> {
                setImageOnTab(imgPath, ivImg)
                imageVideoUrlListing[2] = imgPath.toString()
            }

        }

    }

    private fun setImageOnTab(imgPATH: String?, imageview: ImageView) {
        Log.d("getImage", "---------" + imgPATH.toString())

        Glide.with(this).asBitmap().load(imgPATH).into(imageview)
        hitImageUploadApi(imgPATH)
    }


    fun hitImageUploadApi(imgPATH: String?) {
        val mfile: File?
        mfile = File(imgPATH!!)
        val imageFile: RequestBody = mfile.asRequestBody("image/*".toMediaTypeOrNull())
        val photo: MultipartBody.Part?
        photo = MultipartBody.Part.createFormData("image", mfile.name, imageFile)
        imagePathList.add(photo)
        val type: RequestBody = "image".toRequestBody("text/plain".toMediaTypeOrNull())

        val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
        appViewModel.sendUploadImageData(type, users, imagePathList)
        progressDialog.setProgressDialog()
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
                                            {
                                                selectedPosition = i
                                            }
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
                        ) {
                            childCareId = typeListId[position]
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }


                }

            } else {
                ErrorBodyResponse(response, this, null)
            }
        })


        appViewModel.observeUploadImageResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("urlDataLoading", "------------" + Gson().toJson(response.body()))
                if (response.body() != null) {
                    if (response.body()!!.data != null) {

                        when {
                            clickImg1 -> {
                                image1 = response.body()!!.data!![0].image.toString()
                            }
                            clickImg2 -> {
                                image2 = response.body()!!.data!![0].image.toString()
                            }
                            clickImg3 -> {
                                image3 = response.body()!!.data!![0].image.toString()
                            }
                        }

                        progressDialog.hidedialog()


                        if (urlListingFromResponse != null) {
                            urlListingFromResponse.clear()
                        } else {
                            urlListingFromResponse = ArrayList()

                        }

                        var mlist = response.body()!!.data
                        if (mlist.isNullOrEmpty()) {
                            mlist = ArrayList()
                        } else {
                            makeImageJsonArray(mlist)

                        }
                    }
                } else {
                    ErrorBodyResponse(response, this, null)
                    progressDialog.hidedialog()
                }
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

    fun makeImageJsonArray(mlist: List<ImageUploadApiResponseModel.Datum>) {
        media = JSONArray()
        for (i in mlist.indices) {
            val image = mlist[i].image.toString()
            urlListingFromResponse.add(image)
            val json = JSONObject()
            json.put("image", image)
            json.put("file_type", "image")
            media.put(json)

        }
    }

    private fun hitFinallyActivityAddPostApi() {
        if (checkIfHasNetwork(this)) {
            appViewModel.editMaternalPost_Data(security_key, authKey, "2", childCareId, maternalName, "hah@gmail.com", "",
                placeSpin, countryCodee, phoneNumber, cityAddress, descp_baby_sitter, cityName, cityLatitude, cityLongitude,
                 image1, image2, image3, postID)
        } else {
            showSnackBar(this, getString(R.string.no_internet_error))
        }
    }


    private fun showPlacePicker() {
        Places.initialize(applicationContext, googleMapKey)
        val fields: List<Place.Field> = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)

                cityAddress = place.address.toString()
                et_addressBabySitter.setText(cityAddress)
                cityName = place.name.toString()

                cityLatitude = place.latLng?.latitude.toString()
                cityLongitude = place.latLng?.longitude.toString()

                Log.i("dddddd", "Place: " + place.name + ", " + place.id + "," + place.address + "," + place.latLng)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


}