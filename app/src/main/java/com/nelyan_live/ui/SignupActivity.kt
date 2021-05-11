package com.nelyan_live.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
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
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.nelyan_live.db.DataStoragePreference
import com.nelyan_live.R
import com.nelyan_live.modals.ModelPOJO
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.activity_signup.*
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
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class SignupActivity : OpenCameraGallery(), OnItemSelectedListener, CoroutineScope, View.OnClickListener {

    val category by lazy { ArrayList<String>() }
    val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    val dataStoragePreference by lazy { DataStoragePreference(this@SignupActivity) }
    private var imgPath = ""
    private var cityName = ""
    private var cityLatitude = ""
    private var cityLongitude = ""

    private var termsConditipon_data = ""
    private var privacyPolicy_data = ""

    // Initialize Places variables
    private val googleMapKey = "AIzaSyDQWqIXO-sNuMWupJ7cNNItMhR4WOkzXDE"
    private val PLACE_PICKER_REQUEST = 1
    private var deviceToken: String?= null

    // dialog
    private var job = Job()
    private var socialSignup = ""
    private var socialName = ""
    private var socialEmail = ""
    private var socialImage = ""
    private var socialID = ""

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    // dialog
    private val progressDialog = ProgressDialog(this)


    //  String[] signup = {"Consultant", "Professional"};
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        initalize()
        settingSpinnerForTypeSelection()

    }

    /*  override fun selectedImage(var1: Bitmap, var2: String) {
          Log.d("imagePath","-------"+ var2)
          iv_uploader!!.setImageBitmap(var1)
      }*/


    private fun initalize() {
        ivBack.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
        tvPrivacy.setOnClickListener(this)
        tvTerms.setOnClickListener(this)
        iv_uploader.setOnClickListener(this)
        tv_city.setOnClickListener(this)

    }

    override fun onResume() {
        super.onResume()

        launch (Dispatchers.Main.immediate){
            deviceToken = dataStoragePreference?.emitStoredValue(preferencesKey<String>("fcmToken"))?.first()
        }

        launch(Dispatchers.Main.immediate) {

            // for terms condition
            appViewModel.sendGetContentApiData(security_key, "", "2")

        }
        launch(Dispatchers.Main.immediate) {

            // for privacy policy
            appViewModel.sendGetContentApiData(security_key, "", "3")
        }

        if (intent.extras != null) {

            socialSignup = intent?.extras?.getString("socialLogin").toString()
            imgPath = socialImage

            if (socialSignup.equals("SOCIAL_LOGIN")) {
                tv_passwordSignup.visibility = View.GONE
                password.visibility = View.GONE
                view1.visibility = View.GONE
                tv_confirmPasswordSignup.visibility = View.GONE
                password1.visibility = View.GONE
                view2.visibility = View.GONE

                socialEmail = intent?.extras?.getString("socialEmail").toString()
                socialImage = intent?.extras?.getString("socialImage").toString()
                socialName = intent?.extras?.getString("socialName").toString()
                socialID = intent?.extras?.getString("socialId").toString()

                if (socialEmail.isEmpty()) {
                    tv_userEmail.isFocusable = true
                    tv_userEmail.isFocusableInTouchMode = true
                } else {
                    tv_userEmail.setText(socialEmail)
                    tv_userEmail.isFocusable = false
                    tv_userEmail.isFocusableInTouchMode = false
                }

                // setting the social credential
                tv_username.setText(socialName)
                if (!socialImage.isEmpty()) {
                    Glide.with(this).asBitmap().centerCrop().load(socialImage).into(iv_uploader)
                } else {
                    iv_uploader.setImageResource(R.drawable.img_uploader)
                }


            } else {
                tv_passwordSignup.visibility = View.VISIBLE
                password.visibility = View.VISIBLE
                view1.visibility = View.VISIBLE
                tv_confirmPasswordSignup.visibility = View.VISIBLE
                password1.visibility = View.VISIBLE
                view2.visibility = View.VISIBLE

            }
        }
        checkMvvmResponse()

    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.btnRegister -> {
                val type = typeSelected.selectedItemPosition
                if (type != 0) {
                    if (Validation.checkName(tv_username.text.toString().trim(), this)) {
                        if (Validation.checkEmail(tv_userEmail.text.toString(), this)) {
                            if (socialSignup.equals("") || socialSignup.isEmpty() || socialSignup == null) {

                                if (Validation.checkPassword(tv_password.text.toString(), this)) {
                                    if (tv_confirmPassword.text.toString().isNullOrEmpty()) {
                                        myCustomToast(getString(R.string.confirm_password_missing_error))
                                    } else {
                                        if (tv_password.text.toString().equals(tv_confirmPassword.text.toString())) {
                                            if (tv_city.text.isNullOrEmpty()) {
                                                myCustomToast(getString(R.string.city_missing_error))
                                            } else {
                                                val name = tv_username.text.toString().trim()
                                                val email = tv_userEmail.text.toString()
                                                val password = tv_password.text.toString()
                                                hitSignUpapi(name, email, password, type.toString())
                                            }

                                        } else {
                                            myCustomToast(getString(R.string.password_mismatch_error))
                                        }
                                    }
                                }

                            } else {
                                // here control comes when social login
                                if (tv_city.text.isNullOrEmpty()) {
                                    myCustomToast(getString(R.string.select_city_error))
                                } else {
                                    val name = tv_username.text.toString().trim()
                                    val email = tv_userEmail.text.toString()
                                    hitSocialCompleteProfileApi(name, email, type.toString())

                                }
                            }
                        }
                    }

                } else {
                    myCustomToast(getString(R.string.signup_type_error))
                }

            }

            R.id.tvPrivacy -> {
                if (!privacyPolicy_data.isEmpty() && !privacyPolicy_data.equals("") && privacyPolicy_data != null) {
                    OpenActivity(PrivacyActivity::class.java) {
                        putString("cmsData", privacyPolicy_data)
                    }
                }
            }
            R.id.tvTerms -> {
                if (!termsConditipon_data.isEmpty() && !termsConditipon_data.equals("") && termsConditipon_data != null) {
                    OpenActivity(TermsActivity::class.java) {
                        putString("cmsData", termsConditipon_data)
                    }
                }
            }

            R.id.iv_uploader -> {
                checkPermission(this)                //image("all")
            }

            R.id.tv_city -> {
                showPlacePicker()
            }
        }
    }

    private fun hitSocialCompleteProfileApi(name: String, email: String, type: String) {
        // imageType 1-> file , 2-> url
        val tsLong = System.currentTimeMillis() / 1000
        val currentTS = tsLong.toString()
        Log.e("current", currentTS.toString())


        if (imgPath.isNullOrEmpty()) {
            appViewModel.sendcompleteSocialLogin_withoutImage_Data(security_key, socialID, email, name, type, cityLatitude, cityLongitude,
                    currentTS, cityName, "1")
            progressDialog.setProgressDialog()// signupProgressBar?.showProgressBar()
        } else {
            // here image is of type URl
            appViewModel.sendcompleteSocialLogin_withImage_Data(security_key, socialID, email, name, type, cityLatitude, cityLongitude,
                    currentTS, cityName, "2", imgPath)
            progressDialog.setProgressDialog()
        }

    }

    private fun showPlacePicker() {
        // Initialize Places.
        Places.initialize(
                applicationContext,
                googleMapKey
        )
        // Create a new Places client instance.
        val placesClient: PlacesClient = Places.createClient(this@SignupActivity)
        // Set the fields to specify which types of place data to return.
        val fields: List<Place.Field> = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
        )
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this@SignupActivity)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

    }


    private fun hitSignUpapi(name: String, email: String, password: String, type: String) {

        val tsLong = System.currentTimeMillis() / 1000
        val currentTS = tsLong.toString()
        Log.e("current", currentTS.toString())

        val mName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val mEmail = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val mPassword = password.toRequestBody("text/plain".toMediaTypeOrNull())
        val mType = type.toRequestBody("text/plain".toMediaTypeOrNull())
        val mSecond = currentTS.toRequestBody("text/plain".toMediaTypeOrNull())
        val city = cityName.toRequestBody("text/plain".toMediaTypeOrNull())
        val lat = cityLatitude.toRequestBody("text/plain".toMediaTypeOrNull())
        val longi = cityLongitude.toRequestBody("text/plain".toMediaTypeOrNull())


        if (imgPath.isNullOrEmpty()) {
            if (deviceToken.equals("") || deviceToken == null){
                appViewModel.Send_SIGNUP_withoutIMAGE_Data(security_key, device_Type, "1212313", mName, mEmail, mPassword, mType, mSecond, city, lat, longi)
            }else{
                appViewModel.Send_SIGNUP_withoutIMAGE_Data(security_key, device_Type, deviceToken, mName, mEmail, mPassword, mType, mSecond, city, lat, longi)

            }
            //signupProgressBar?.showProgressBar()
            progressDialog.setProgressDialog()

        } else {
            // hit with updating the image
            var mfile: File? = null
            mfile = File(imgPath)
            val imageFile: RequestBody? = mfile.asRequestBody("image/*".toMediaTypeOrNull())
            var photo: MultipartBody.Part? = null
            photo = MultipartBody.Part.createFormData("image", mfile?.name, imageFile!!)
            if (deviceToken.equals("") || deviceToken == null) {
                appViewModel.Send_SIGNUP_withIMAGE_Data(security_key, device_Type, "121321231", mName, mEmail,
                        mPassword, mType, mSecond, city, lat, longi, photo)
            }else {
                appViewModel.Send_SIGNUP_withIMAGE_Data(security_key, device_Type, deviceToken, mName,
                        mEmail, mPassword, mType, mSecond, city, lat, longi, photo)
            }
            progressDialog.setProgressDialog()
            //signupProgressBar?.showProgressBar()
        }

    }

    private fun checkMvvmResponse() {
        appViewModel!!.observeSignupResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    Log.d("signupResponse", "-----" + Gson().toJson(response.body()))
                    progressDialog.hidedialog()
                    // signupProgressBar?.hideProgressBar()
                    // here we save credentail for signup

                    val mResponse: String = response.body().toString()
                    val jsonObject = JSONObject(mResponse)

                    val message = jsonObject.get("msg").toString()
                    myCustomToast(message)

                    val email = jsonObject.getJSONObject("data").get("email").toString()
                    val name = jsonObject.getJSONObject("data").get("name").toString()
                    val password = jsonObject.getJSONObject("data").get("password").toString()
                    val type = jsonObject.getJSONObject("data").get("type").toString()
                    val notificationStatus = jsonObject.getJSONObject("data").get("notificationStatus").toString()
                    val image = jsonObject.getJSONObject("data").get("image").toString()
                    val phone = jsonObject.getJSONObject("data").get("phone").toString()
                    val authKey = jsonObject.getJSONObject("data").get("authKey").toString()
                    val cityOrZipcode = jsonObject.getJSONObject("data").get("cityOrZipcode").toString()
                    val latitude = jsonObject.getJSONObject("data").get("lat").toString()
                    val longitude = jsonObject.getJSONObject("data").get("lng").toString()

                    AllSharedPref.save(this, "auth_key", authKey)

                    launch(Dispatchers.IO) {
                        dataStoragePreference.save(email, preferencesKey<String>("emailLogin"))
                        dataStoragePreference.save(name, preferencesKey<String>("nameLogin"))
                        dataStoragePreference.save(password, preferencesKey<String>("passwordLogin"))
                        dataStoragePreference.save(type, preferencesKey<String>("typeLogin"))
                        dataStoragePreference.save(notificationStatus, preferencesKey<String>("notificationStatusLogin"))
                        dataStoragePreference.save(image, preferencesKey<String>("imageLogin"))
                        dataStoragePreference.save(phone, preferencesKey<String>("phoneLogin"))
                        dataStoragePreference.save(authKey, preferencesKey<String>("auth_key"))
                        dataStoragePreference.save(cityOrZipcode, preferencesKey<String>("cityLogin"))
                        dataStoragePreference.save(latitude, preferencesKey<String>("latitudeLogin"))
                        dataStoragePreference.save(longitude, preferencesKey<String>("longitudeLogin"))

                    }

                    progressDialog.hidedialog()
                    OpenActivity(HomeActivity::class.java) {
                        putString("authorization", authKey)
                    }
                }
            } else {
                ErrorBodyResponse(response, this, null)
                progressDialog.hidedialog()

            }
        })

        // for terms condition
        appViewModel!!.observeTermsConditionResponse()!!.observe(this, androidx.lifecycle.Observer { response ->

            //  appViewModel.observeTermsConditionResponse()!!.observe(this, Observer { response->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    Log.d("getContent_terms", "---------" + Gson().toJson(response.body()))

                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    termsConditipon_data = jsonObject.getJSONObject("data").get("content").toString()
                }
            } else {
                ErrorBodyResponse(response, this, null)
            }
        })

        // for  privacy policy

        appViewModel!!.observePrivacyPolicyResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            //   appViewModel.observePrivacyPolicyResponse()!!.observe(this, Observer { response->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    Log.d("getContent_privacy", "---------" + Gson().toJson(response.body()))

                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    privacyPolicy_data = jsonObject.getJSONObject("data").get("content").toString()
                }
            } else {
                ErrorBodyResponse(response, this, null)
            }
        })


        // complete profile socialLogin api
        appViewModel.observeCompleteSociaLogin_Api_Response()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    Log.d("completeSocialResponse", "---------" + Gson().toJson(response.body()))
                    val mResponse: String = response.body().toString()
                    val jsonObject = JSONObject(mResponse)

                    val message = jsonObject.get("msg").toString()
                    myCustomToast(message)
                    progressDialog.hidedialog()


                    val email = jsonObject.getJSONObject("data").get("email").toString()
                    val password = jsonObject.getJSONObject("data").get("password").toString()
                    val type = jsonObject.getJSONObject("data").get("type").toString()
                    val notificationStatus = jsonObject.getJSONObject("data").get("notificationStatus").toString()
                    val image = jsonObject.getJSONObject("data").get("image").toString()
                    val phone = jsonObject.getJSONObject("data").get("phone").toString()
                    val authKey = jsonObject.getJSONObject("data").get("authKey").toString()
                    val cityOrZipcode = jsonObject.getJSONObject("data").get("cityOrZipcode").toString()
                    val latitude = jsonObject.getJSONObject("data").get("lat").toString()
                    val longitude = jsonObject.getJSONObject("data").get("lng").toString()

                    AllSharedPref.save(this, "auth_key", authKey)

                    launch(Dispatchers.IO) {
                        dataStoragePreference.save(email, preferencesKey<String>("emailLogin"))
                        dataStoragePreference.save(password, preferencesKey<String>("passwordLogin"))
                        dataStoragePreference.save(type, preferencesKey<String>("typeLogin"))
                        dataStoragePreference.save(image, preferencesKey<String>("imageLogin"))
                        dataStoragePreference.save(phone, preferencesKey<String>("phoneLogin"))
                        dataStoragePreference.save(authKey, preferencesKey<String>("auth_key"))
                        dataStoragePreference.save(notificationStatus, preferencesKey<String>("notificationStatusLogin"))
                        dataStoragePreference.save(cityOrZipcode, preferencesKey<String>("cityLogin"))
                        dataStoragePreference.save(latitude, preferencesKey<String>("latitudeLogin"))
                        dataStoragePreference.save(longitude, preferencesKey<String>("longitudeLogin"))

                    }

                    progressDialog.hidedialog()

                    OpenActivity(HomeActivity::class.java) {
                        putString("authorization", authKey)
                    }


                }

            } else {
                ErrorBodyResponse(response, this, null)
                progressDialog.hidedialog()

            }
        })

        appViewModel!!.getException()!!.observe(this, androidx.lifecycle.Observer {
            myCustomToast(it)
            progressDialog.hidedialog()// signupProgressBar?.hideProgressBar()
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === PLACE_PICKER_REQUEST) {
            if (resultCode === Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)

                cityName = place.name.toString()
                tv_city.setText(cityName.toString())

                // cityID = place.id.toString()
                cityLatitude = place.latLng?.latitude.toString()
                cityLongitude = place.latLng?.longitude.toString()

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


    private fun settingSpinnerForTypeSelection() {
        category.add("")
        category.add("Consultant")
        category.add("Professional")
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter(this, R.layout.customspinner, category as List<String>)
        typeSelected!!.setAdapter(arrayAdapter)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun getRealImagePath(imagePath: String?) {
        Log.d("getImageRealPath", "---------------" + imagePath)
        imgPath = imagePath.toString()
        Glide.with(this).asBitmap().load(imgPath).circleCrop().into(iv_uploader)
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}