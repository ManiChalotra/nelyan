package com.nelyanlive.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.nelyanlive.R
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.util.*
import kotlin.coroutines.CoroutineContext

class EditProfileActivity : OpenCameraGallery(), View.OnClickListener, CoroutineScope {
    private val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(AppViewModel::class.java)
    }

    private val dataStoragePreference by lazy {
        DataStoragePreference(this@EditProfileActivity)
    }
    private val job by lazy {
        Job()
    }
    private var imagePathCreated = ""
    private var name = ""
    private var email = ""
    private var city = ""
    private var utilization = ""
    private var latitude = ""
    private var longitude = ""
    private var authKey = ""
    private var imgPath = ""

    // Initialize Places variables
    private val googleMapKey = "AIzaSyDQWqIXO-sNuMWupJ7cNNItMhR4WOkzXDE"
    private val PLACE_PICKER_REQUEST = 1


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun getRealImagePath(imgPath: String?) {
        this.imgPath = imgPath.toString()
        Glide.with(this).load(this.imgPath).into(iv_profileEdit)
        Log.d("getEditPhotoPath", "----------${this.imgPath}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        initialize()
        checkMvvmResponse()
        Glide.with(this@EditProfileActivity).asBitmap().circleCrop()
            .load(from_admin_image_base_URl + intent?.extras?.getString("userImage").toString())
            .error(R.mipmap.ic_user_place)
            .into(iv_profileEdit)

        if (imagePathCreated.isEmpty()) {
            imagePathCreated = intent?.extras?.getString("userImage").toString()
            name = intent?.extras?.getString("userName").toString()
            email = intent?.extras?.getString("userEmail").toString()
            city = intent?.extras?.getString("userCity").toString()
            authKey = intent?.extras?.getString("authorization").toString()

            Log.d("getPath", "---onResume----$imagePathCreated")

            et_nameEditProfile.setText(name); setFocusEditText(et_nameEditProfile)
            tv_emailEditProfile.text = email
            et_cityEditProfile.setText(city); setFocusEditText(et_cityEditProfile)

        }

        launch(Dispatchers.Main.immediate) {
            latitude =
                dataStoragePreference.emitStoredValue(preferencesKey<String>("latitudeLogin"))
                    .first()
            longitude =
                dataStoragePreference.emitStoredValue(preferencesKey<String>("longitudeLogin"))
                    .first()
        }


    }

    private fun initialize() {
        ivBack.setOnClickListener(this)
        ivPlus.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        et_cityEditProfile.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.ivPlus -> {
                checkPermission(this@EditProfileActivity)
            }
            R.id.btnSave -> {
                val name = et_nameEditProfile.text.toString()
                val email = tv_emailEditProfile.text.toString()
                val city = et_cityEditProfile.text.toString()

                if (Validation.checkName(name, this)) {
                    if (Validation.checkEmail(email, this)) {
                        if (city.isEmpty()) {
                            myCustomToast(getString(R.string.city_missing_error))
                        } else {
                                hitEditProfileApi(name, city, utilization)
                        } } }
            }
            R.id.et_cityEditProfile -> {
                showPlacePicker()
            }

        }
    }

    private fun hitEditProfileApi(name: String, city: String, utilization: String) {
        val mName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val mCity = city.toRequestBody("text/plain".toMediaTypeOrNull())
        val mUtilization = utilization.toRequestBody("text/plain".toMediaTypeOrNull())
        val mLatitude = latitude.toRequestBody("text/plain".toMediaTypeOrNull())
        val mLongitude = longitude.toRequestBody("text/plain".toMediaTypeOrNull())


        if (imgPath.isEmpty()) {
            // without image
            appViewModel.sendEditProfileApiWithoutImageData(
                security_key,
                authKey,
                mName,
                mCity,
                mLatitude,
                mLongitude,
                mUtilization
            )
        } else {
            // with image
            val mFile: File?
            mFile = File(imgPath)
            val imageFile: RequestBody = mFile.asRequestBody("image/*".toMediaTypeOrNull())
            val photo: MultipartBody.Part?
            photo = MultipartBody.Part.createFormData("image", mFile.name, imageFile)
            appViewModel.sendEditProfileApiWithImageData(
                security_key,
                authKey,
                mName,
                mCity,
                mLatitude,
                mLongitude,
                mUtilization,
                photo
            )
        }

        editProfileProgressBar?.showProgressBar()

    }

    private fun checkMvvmResponse() {
        appViewModel.observeEditProfileResponse()!!
            .observe(this, androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    if (response.body() != null) {
                        editProfileProgressBar?.hideProgressBar()
                        val mResponse = response.body().toString()
                        val jsonObject = JSONObject(mResponse)
                        val message = jsonObject.get("msg").toString()


                        Log.i("observeEditProfileResponse  ", "-------$jsonObject")

                        val name = jsonObject.getJSONObject("data").get("name").toString()
                        val image = jsonObject.getJSONObject("data").get("image").toString()
                        val cityOrZipcode =
                            jsonObject.getJSONObject("data").get("cityOrZipcode").toString()
                        val lat = jsonObject.getJSONObject("data").get("lat").toString()
                        val long = jsonObject.getJSONObject("data").get("lng").toString()

                        launch(Dispatchers.Main.immediate) {
                            dataStoragePreference.save(name, preferencesKey("nameLogin"))
                            dataStoragePreference.save(image, preferencesKey("imageLogin"))
                            dataStoragePreference.save(cityOrZipcode, preferencesKey("cityLogin"))
                            dataStoragePreference.save(lat, preferencesKey("latitudeLogin"))
                            dataStoragePreference.save(long, preferencesKey("longitudeLogin"))
                            Log.d(
                                "userLocation======",
                                dataStoragePreference.emitStoredValue(preferencesKey<String>("cityLogin"))
                                    .first()
                            )


                        }

                        myCustomToast(message)
                        onBackPressed()
                    }

                } else {
                    ErrorBodyResponse(response, this@EditProfileActivity, editProfileProgressBar)
                }
            })
        appViewModel.getException()!!.observe(this, androidx.lifecycle.Observer {
            myCustomToast(it)
            editProfileProgressBar?.hideProgressBar()
        })
    }

    private fun showPlacePicker() {
        Places.initialize(
            applicationContext,
            googleMapKey
        )
        val fields: List<Place.Field> = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
        )
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(this@EditProfileActivity)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)

                    city = place.name.toString()
                    et_cityEditProfile.setText(city)

                    latitude = place.latLng?.latitude.toString()
                    longitude = place.latLng?.longitude.toString()

                    Log.i(
                        "d",
                        "Place: " + place.name + ", " + place.id + "," + place.address + "," + place.latLng
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()

    }

    private fun setFocusEditText(editText: EditText) {
        val pos: Int = editText.text.length
        editText.setSelection(pos)
    }


}