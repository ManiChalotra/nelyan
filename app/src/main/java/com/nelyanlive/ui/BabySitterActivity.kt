package com.nelyanlive.ui

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.data.network.responsemodels.ImageUploadApiResponseModel
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
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

class BabySitterActivity : OpenCameraGallery(), View.OnClickListener, CoroutineScope {

    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }


    private var IMAGE_SELECTED_TYPE = ""
    private val job by lazy { kotlinx.coroutines.Job() }
    private val dataStoragePreference by lazy { DataStoragePreference(this) }
    private var media: JSONArray = JSONArray()
    private var countryCodee = "+33"
    private var childCareType = ""
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

    private var imageVideoListPosition = -1

    private var imagePathList = ArrayList<MultipartBody.Part>()

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
    private var isImageUploaded=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baby_sitter)
        initalizeClicks()

        launch(Dispatchers.Main.immediate) {
            authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
        }

        hitChildCareType_Api()
    }

    private fun hitChildCareType_Api() {
        if (checkIfHasNetwork(this@BabySitterActivity)) {
            launch(Dispatchers.Main.immediate) {
                authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
                appViewModel.sendChildCareTypeData(security_key, authKey)
            }
        } else {
            showSnackBar(this@BabySitterActivity, getString(R.string.no_internet_error))
        } }


    private fun initalizeClicks() {

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
            R.id.ivBackBabaySitter -> {
                onBackPressed()
            }
            R.id.btnSubmitBabySitter -> {
                maternalName = et_maternalName.text.toString()
                childCareType = sp_child_care_type.selectedItem.toString()
                placeSpin = et_number_of_places.text.toString()
                phoneNumber = et_phoneNumber.text.toString()
                address_baby_sitter = et_addressBabySitter.text.toString()
                descp_baby_sitter = et_descriptionBabySitter.text.toString()

                var imageSelected = false
                for(i in 0 until imageVideoUrlListing.size)
                {
                    if(!imageSelected && imageVideoUrlListing[i].isNotEmpty()) imageSelected = true
                }

                if (!imageSelected) {
                    myCustomToast(getString(R.string.select_image_video_error))
                } else {
                    if (childCareType == "" || childCareType == getString(R.string.select)) {
                        myCustomToast(getString(R.string.child_care_type_missing_error))
                    } else {
                        if (maternalName.isEmpty()) {
                            myCustomToast(getString(R.string.please_enter_name))
                        } else {
                            if (placeSpin.isEmpty()) {
                                myCustomToast(getString(R.string.places_number_error))
                            } else {
                                    if (address_baby_sitter.isEmpty()) {
                                        myCustomToast(getString(R.string.address_missing_error))
                                    }
                                    else {
                                        if (descp_baby_sitter.isEmpty()) {
                                            myCustomToast(getString(R.string.description_missing))
                                        }
                                        else {
                                            when (childCareType) {
                                                "Nursery" -> {
                                                    childCareId = "1"
                                                }
                                                "Maternal Assistant" -> {
                                                    childCareId = "2"
                                                }
                                                "Baby Sitter" -> {
                                                    childCareId = "3"
                                                } }

                                            // checking the list
                                            if (selectedUrlListing.size == urlListingFromResponse.size) {
                                                selectedUrlListing.clear()
                                                urlListingFromResponse.clear()
                                            }

                                            // for check upper images url from response
                                            Log.d("imageVideoListSize", "-----------$imageVideoUrlListing")

                                            // rotating loop
                                            for (i in 0 until imageVideoUrlListing.size) {
                                                val media = imageVideoUrlListing[i]
                                                if (media.isNotEmpty()) {
                                                    selectedUrlListing.add(media)
                                                } }

                                            if (isImageUploaded == "" && isImageUploaded !=null) {
                                                hitApiForBannerImages(0)
                                            }
                                            else
                                            {
                                                hitFinallyActivityAddPostApi()
                                            } } }} } } } }

            R.id.et_addressBabySitter -> {
                showPlacePicker()
            } }
    }

    override fun getRealImagePath(imgPath: String?) {

        when (IMAGE_SELECTED_TYPE) {
            "1" -> {
                setImageOnTab(imgPath, ivImg)
                imageVideoUrlListing[0] = imgPath.toString()
                iv_image1.visibility = View.GONE
            }

            "2" -> {
                setImageOnTab(imgPath, ivImg1)
                imageVideoUrlListing[1] = imgPath.toString()
            }

            "3" -> {
                setImageOnTab(imgPath, ivImg2)
                imageVideoUrlListing[2] = imgPath.toString()
            } }
    }

    private fun setImageOnTab(imgPATH: String?, imageview: ImageView?) {

        when (IMAGE_SELECTED_TYPE) {

            "1" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video011)
            }
            "2" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video012)
            }
            "3" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video013)
            } }

        Glide.with(this).asBitmap().load(imgPATH).into(imageview!!)
    }

    private fun checkVideoButtonVisibility(imgpath: String, videoButton: ImageView) {

        if (imgpath.contains(".mp4")) {
            videoButton.visibility = View.VISIBLE
        } else {
            videoButton.visibility = View.GONE
        }
    }

    private fun hitApiForBannerImages(position: Int) {

        imageVideoListPosition = position

        if (imageVideoListPosition <= selectedUrlListing.size - 1) {

            if (imagePathList != null) {
                imagePathList.clear()
            }
            else {
                imagePathList = java.util.ArrayList()
            }

            var mfile: File?
            for (i in 0 until selectedUrlListing.size) {
                mfile = File(selectedUrlListing[i])
                val imageFile: RequestBody = mfile.asRequestBody("image/*".toMediaTypeOrNull())
                var photo: MultipartBody.Part? = null
                photo = MultipartBody.Part.createFormData("image", mfile.name, imageFile)
                imagePathList.add(photo)
            }

            val type: RequestBody = "image".toRequestBody("text/plain".toMediaTypeOrNull())

            val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
            appViewModel.sendUploadImageData(type, users, imagePathList)
            progressDialog.setProgressDialog()

        } }


    private fun checkMvvmResponse() {

        appViewModel.observeChildCareTypeResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    val jsonArray = jsonObject.getJSONArray("data")
                    val country: ArrayList<String?> = ArrayList()
                    country.add("")
                    for (i in 0 until jsonArray.length()) {
                        val name = jsonArray.getJSONObject(i).get("categoryName").toString()
                        country.add(name)
                    }
                    val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.customspinner, country as List<Any?>)
                    sp_child_care_type!!.adapter = arrayAdapter
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

                        if (urlListingFromResponse != null) {
                            urlListingFromResponse.clear()
                        }
                        else {
                            urlListingFromResponse = ArrayList()
                        }

                        var mlist = response.body()!!.data
                        if (mlist.isNullOrEmpty()) {
                            mlist = ArrayList()
                        }
                        else
                        {
                            makeImageJsonArray(mlist)
                        }

                        hitFinallyActivityAddPostApi()
                    }
                } else {
                    ErrorBodyResponse(response, this, null)
                    progressDialog.hidedialog()
                }
            }
        })

        appViewModel.observe_addMaternalPost_Response()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("addMaternalPostResopnse", "-----------" + Gson().toJson(response.body()))
                if (response.body() != null) {
                    progressDialog.hidedialog()
                    finishAffinity()
                    OpenActivity(HomeActivity::class.java)
                    myCustomToast(response.message())
                } }
            else {
                progressDialog.hidedialog()
                ErrorBodyResponse(response, this, null)
            } })

        appViewModel.getException()!!.observe(this, androidx.lifecycle.Observer {
            myCustomToast(it)
            progressDialog.hidedialog()
        })
    }

    private fun makeImageJsonArray(mlist: List<ImageUploadApiResponseModel.Datum>) {
        media = JSONArray()
        for (i in mlist.indices) {
            val image = mlist[i].image.toString()
            urlListingFromResponse.add(image)
            val json = JSONObject()
            json.put("image", image)
            json.put("file_type", "image")
            media.put(json)

        } }

    private fun hitFinallyActivityAddPostApi() {
        if (checkIfHasNetwork(this)) {
            appViewModel.sendMaternalPost_Data(security_key, authKey, "2", childCareId, maternalName, "hah@gmail.com",
                    placeSpin, countryCodee, phoneNumber, cityAddress, descp_baby_sitter, cityName, cityLatitude, cityLongitude,
                    media.toString())
        }
        else {
            showSnackBar(this, getString(R.string.no_internet_error))
        } }

    private fun showPlacePicker() {
        Places.initialize(applicationContext, googleMapKey)
        val fields: List<Place.Field> = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)

                    cityLatitude = place.latLng?.latitude.toString()
                    cityLongitude = place.latLng?.longitude.toString()
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val list = geocoder.getFromLocation(place.latLng?.latitude!!.toDouble(), place.latLng?.longitude!!.toDouble(), 1)
                    cityName = if(!list[0].locality.isNullOrBlank()) {list[0].locality} else{place.name.toString() }
                    cityAddress = place.address.toString()
                    et_addressBabySitter.setText(cityAddress)
                } } } }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}