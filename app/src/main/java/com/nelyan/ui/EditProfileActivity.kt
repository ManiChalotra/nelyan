package com.nelyan.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
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
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.meherr.mehar.db.DataStoragePreference
import com.nelyan.R
import com.nelyan.utils.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.ivBack
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
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java)
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
    private var authkey = ""
    private  var imggPathh= ""

    // Initialize Places variables
    private val googleMapKey = "AIzaSyDQWqIXO-sNuMWupJ7cNNItMhR4WOkzXDE"
    private val PLACE_PICKER_REQUEST = 1


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun getRealImagePath(imgPath: String?) {
        imggPathh = imgPath.toString()
        Glide.with(this@EditProfileActivity).asBitmap().circleCrop().load(imggPathh).into(iv_profileEdit)
        Log.d("getEditPhotoPath", "----------" + imgPath)
    }

    override fun onResume() {
        super.onResume()
        if (imagePathCreated.isNullOrEmpty()) {
            imagePathCreated = intent?.extras?.getString("userImage").toString()
            name = intent?.extras?.getString("userName").toString()
            email = intent?.extras?.getString("userEmail").toString()
            city = intent?.extras?.getString("userCity").toString()
            utilization = intent?.extras?.getString("userUtilization").toString()
            authkey = intent?.extras?.getString("authorization").toString()

            Log.d("getPatttthhhh", "-------" + imagePathCreated)

            Glide.with(this@EditProfileActivity).asBitmap().circleCrop().load(image_base_URl + imagePathCreated).into(iv_profileEdit)
            et_nameEditProfile.setText(name); setFocusEditText(et_nameEditProfile)
            tv_emailEditProfile.text = email
            et_cityEditProfile.setText(city); setFocusEditText(et_cityEditProfile)
            et_utilizationEditProfile.setText(utilization); setFocusEditText(et_utilizationEditProfile)

        }


        launch(Dispatchers.Main.immediate) {
            latitude = dataStoragePreference.emitStoredValue(preferencesKey<String>("latitudeLogin")).first()
            longitude = dataStoragePreference.emitStoredValue(preferencesKey<String>("longitudeLogin")).first()
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        initalize()
        checkMvvmResponse()
    }

    private fun initalize() {
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
                val utilization = et_utilizationEditProfile.text.toString()
                val city = et_cityEditProfile.text.toString()
                hitEditProfileApi(name, email, city, utilization)

            }
            R.id.et_cityEditProfile -> {
                showPlacePicker()
            }

        }
    }

    private fun hitEditProfileApi(name: String, email: String, city: String, utilization: String) {
        val mName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val mEmail = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val mCity = city.toRequestBody("text/plain".toMediaTypeOrNull())
        val mUtilization = utilization.toRequestBody("text/plain".toMediaTypeOrNull())
        val mLatitude = latitude.toRequestBody("text/plain".toMediaTypeOrNull())
        val mLongitude = longitude.toRequestBody("text/plain".toMediaTypeOrNull())


        if (imggPathh.isNullOrEmpty()) {
            // without image
            appViewModel.sendEditProfileApiWithoutImageData(security_key, authkey, mName, mCity, mLatitude, mLongitude, mUtilization)
        } else {
            // with image
            var mfile: File? = null
            mfile = File(imggPathh)
            val imageFile: RequestBody? = mfile.asRequestBody("image/*".toMediaTypeOrNull())
            var photo: MultipartBody.Part? = null
            photo = MultipartBody.Part.createFormData("image", mfile?.name, imageFile!!)
            appViewModel.sendEditProfileApiWithImageData(security_key, authkey, mName, mCity, mLatitude, mLongitude, mUtilization, photo)
        }

        editProfileProgressBar?.showProgressBar()

    }

    private fun checkMvvmResponse() {
        appViewModel.observeEditProfileResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    editProfileProgressBar?.hideProgressBar()
                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    val message = jsonObject.get("msg").toString()
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
        // Initialize Places.
        Places.initialize(
                applicationContext,
                googleMapKey
        )
        // Create a new Places client instance.
        val placesClient: PlacesClient = Places.createClient(this@EditProfileActivity)
        // Set the fields to specify which types of place data to return.
        val fields: List<Place.Field> = Arrays.asList(
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
        if (requestCode === PLACE_PICKER_REQUEST) {
            if (resultCode === Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)

                city = place.name.toString()
                et_cityEditProfile.setText(city)

                // cityID = place.id.toString()
                latitude = place.latLng?.latitude.toString()
                longitude = place.latLng?.longitude.toString()

                Log.i(
                        "dddddd",
                        "Place: " + place.name + ", " + place.id + "," + place.address + "," + place.latLng
                )
            } else if (resultCode === AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("dddddd", status.getStatusMessage().toString())
            } else if (resultCode === Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i("dddddd", "-------Operation is cancelled ")
            }
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()

    }

    private fun setFocusEditText(editText: EditText) {
        val pos: Int = editText.getText().length
        editText.setSelection(pos)
    }

    /* override fun selectedImage(var1: Bitmap, var2: String) {
         ivImg!!.setImageBitmap(var1)
     }*/

}