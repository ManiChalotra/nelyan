package com.nelyan.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
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
import com.meherr.mehar.db.DataStoragePreference
import com.nelyan.HELPER.image
import com.nelyan.R
import com.nelyan.utils.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class SignupActivity : OpenCameraGallery(), OnItemSelectedListener, CoroutineScope, View.OnClickListener {

    val  category by lazy { ArrayList<String>() }
    val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    val dataStoragePreference by lazy { DataStoragePreference(this@SignupActivity) }
    private  var imgPath = ""
    private  var cityName = ""
    private  var cityLatitude = ""
   private  var cityLongitude = ""

    // Initialize Places variables
    private val googleMapKey = "AIzaSyDQWqIXO-sNuMWupJ7cNNItMhR4WOkzXDE"
    private val PLACE_PICKER_REQUEST = 1





    private  var job = Job()


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    //  String[] signup = {"Consultant", "Professional"};
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        initalize()
        settingSpinnerForTypeSelection()
        checkMvvmResponse()
    }

  /*  override fun selectedImage(var1: Bitmap, var2: String) {
        Log.d("imagePath","-------"+ var2)
        iv_uploader!!.setImageBitmap(var1)
    }*/

    private   fun  initalize(){



        ivBack.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
        tvPrivacy.setOnClickListener(this)
        tvTerms.setOnClickListener(this)
        iv_uploader.setOnClickListener(this)
        tv_city.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ivBack->{
                onBackPressed()
            }
            R.id.btnRegister->{
                val type = typeSelected.selectedItemPosition
                if(type!=0){
                    if(Validation.checkName(tv_username.text.toString().trim(), this)){
                        if(Validation.checkEmail(tv_userEmail.text.toString(), this)){
                            if(Validation.checkPassword(tv_password.text.toString(), this)){
                                if(tv_confirmPassword.text.toString().isNullOrEmpty()){
                                    myCustomToast("please enter confirm password")
                                }else{
                                    if(tv_password.text.toString().equals(tv_confirmPassword.text.toString())){
                                        if(tv_city.text.isNullOrEmpty()){
                                            myCustomToast("Please select your city")
                                        }else{
                                            val name = tv_username.text.toString().trim()
                                            val email = tv_userEmail.text.toString()
                                            val password = tv_password.text.toString()
                                            hitSignUpapi(name, email, password, type.toString())
                                        }

                                    }else{
                                        myCustomToast("Password and confirm password do not matches")
                                    }
                                }
                            }
                        }
                    }


                }else{
                    myCustomToast("Please select the required signup type")
                }

                //OpenActivity(HomeActivity::class.java)

            }
            R.id.tvPrivacy->{
                OpenActivity(PrivacyActivity::class.java)
            }
            R.id.tvTerms->{
                OpenActivity(TermsActivity::class.java)
            }
            R.id.iv_uploader->{

                checkPermission(this)
                //image("all")
            }
            R.id.tv_city->{
                showPlacePicker()
            }

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


        val mName = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val mEmail = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val mPassword = password.toRequestBody("text/plain".toMediaTypeOrNull())
        val mType = type.toRequestBody("text/plain".toMediaTypeOrNull())
        val mSecond = "12".toRequestBody("text/plain".toMediaTypeOrNull())
        val city = cityName.toRequestBody("text/plain".toMediaTypeOrNull())
        val lat = cityLatitude.toRequestBody("text/plain".toMediaTypeOrNull())
        val longi = cityLongitude.toRequestBody("text/plain".toMediaTypeOrNull())


        if(imgPath.isNullOrEmpty()){
            appViewModel.Send_SIGNUP_withoutIMAGE_Data(security_key, device_Type, "112",mName,mEmail,mPassword,mType,mSecond,city,lat,longi,)
            signupProgressBar?.showProgressBar()
        }else{
            // hit with updating the image
            var mfile: File? = null
            mfile = File(imgPath)
            val imageFile: RequestBody? = mfile.asRequestBody("image/*".toMediaTypeOrNull())
            var photo: MultipartBody.Part? = null
            photo = MultipartBody.Part.createFormData("image", mfile?.name, imageFile!!)
            appViewModel.Send_SIGNUP_withIMAGE_Data(security_key, device_Type, "112",mName,mEmail,mPassword,mType,mSecond,city,lat,longi,photo)
            signupProgressBar?.showProgressBar()
        }



    }

    private  fun checkMvvmResponse(){
        appViewModel!!.observeSignupResponse()!!.observe(this, androidx.lifecycle.Observer { response->
            if(response!!.isSuccessful && response.code() ==200){
                if(response.body()!= null){
                    Log.d("signupResponse","-----"+ Gson().toJson(response.body()))
                    signupProgressBar?.hideProgressBar()
                }
            }else{
                ErrorBodyResponse(response, this, signupProgressBar)
            }
        })
        appViewModel!!.getException()!!.observe(this, androidx.lifecycle.Observer {
            myCustomToast(it)
            signupProgressBar?.hideProgressBar()
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


    private  fun settingSpinnerForTypeSelection(){
        category.add("")
        category.add("Consultant")
        category.add("Professional")
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter(this, R.layout.customspinner, category as List<String>)
        typeSelected!!.setAdapter(arrayAdapter)
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun getRealImagePath(imagePath: String?) {
        Log.d("getImageRealPath","---------------"+ imagePath)
        imgPath = imagePath.toString()
        Glide.with(this).asBitmap().load(imgPath).into(iv_uploader)
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}