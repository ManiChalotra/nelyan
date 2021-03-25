package com.nelyan_live.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
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
import com.nelyan_live.R
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.activity_nurserie.*
import kotlinx.android.synthetic.main.activity_nurserie.ivImg
import kotlinx.android.synthetic.main.activity_nurserie.ivImg1
import kotlinx.android.synthetic.main.activity_nurserie.ivImg2
import kotlinx.android.synthetic.main.activity_nurserie.ivImg3
import kotlinx.android.synthetic.main.activity_nurserie.ivplus
import kotlinx.android.synthetic.main.customspinner.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

class NurserieActivity : OpenCameraGallery(), View.OnClickListener, CoroutineScope {

    private var media: JSONArray = JSONArray()
    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    private var IMAGE_SELECTED_TYPE = ""
    private val job by lazy { kotlinx.coroutines.Job() }
    private val dataStoragePreference by lazy { DataStoragePreference(this) }

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
    private var urlListingFromResponse: ArrayList<String> = ArrayList()

    private var imageVideoUrlListing = arrayListOf("", "", "", "", "")

    private var imageVideoListPosition = -1
    private var selectedUrlListing: ArrayList<String> = ArrayList()
    private var imagePathList = ArrayList<MultipartBody.Part>()
    private var imagePathList2 = ArrayList<MultipartBody.Part>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nurserie)
        initalizeClicks()

        // add info list
        val info: MutableList<String?> = ArrayList()
        info.add("")
        info.add("Public")
        info.add("Private")
        val arrayAdapte1: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.customspinner, info as List<Any?>)
        addInfoSpinner.setAdapter(arrayAdapte1)


        // no of places avilable
        val count: MutableList<String?> = ArrayList()
        count.add("")
        count.add("0")
        count.add("1")
        count.add("2")
        count.add("3")
        count.add("4")
        count.add("5")
        count.add("6")
        count.add("7")
        count.add("8")
        count.add("9")
        count.add("10")
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.customspinner, count as List<Any?>)
        noOfPlacesSpinner.setAdapter(arrayAdapter)

        /*
         btnSubmit!!.setOnClickListener(View.OnClickListener {
             val i = Intent(this@NurserieActivity, HomeActivity::class.java)
             i.putExtra("activity", "nurFrag")
             startActivity(i)
         })
         */

    }

    private fun initalizeClicks() {
        ivImg.setOnClickListener(this)
        ivImg1.setOnClickListener(this)
        ivImg2.setOnClickListener(this)
        ivImg3.setOnClickListener(this)
        ivplus.setOnClickListener(this)
        et_addressNursery.isFocusable = false
        et_addressNursery.isFocusableInTouchMode = false
        et_addressNursery.setOnClickListener(this)
        btn_submit.setOnClickListener(this)

        checkMvvmResponse()
    }

    override fun getRealImagePath(imgPath: String?) {

        when (IMAGE_SELECTED_TYPE) {

            "1" -> {
                setImageOnTab(imgPath, ivImg)
            }

            "2" -> {
                setImageOnTab(imgPath, ivImg1)
            }

            "3" -> {
                setImageOnTab(imgPath, ivImg2)
            }

            "4" -> {
                setImageOnTab(imgPath, ivImg3)
            }

            "5" -> {
                setImageOnTab(imgPath, ivplus)
            }

        }

    }

    private fun setImageOnTab(imgPATH: String?, imageview: ImageView?) {
        Log.d("getimage", "---------" + imgPATH.toString())

        when (IMAGE_SELECTED_TYPE) {

            "1" -> {
                imageview?.setScaleType(ImageView.ScaleType.FIT_XY)
                checkVideoButtonVisibility(imgPATH.toString(), iv_video11)
            }
            "2" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video22)

            }
            "3" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video33)

            }
            "4" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video44)

            }
            "5" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video55)
            }
        }
        Glide.with(this).asBitmap().load(imgPATH).into(imageview!!)
    }

    private fun checkVideoButtonVisibility(imgpath: String, videoButton: ImageView) {

        if (imgpath?.contains(".mp4")!!) {
            videoButton.visibility = View.VISIBLE
        } else {
            videoButton.visibility = View.GONE
        }
    }


    private fun hitApiForBannerImages(position: Int) {

        imageVideoListPosition = position

        if (imageVideoListPosition <= selectedUrlListing.size - 1) {

            val media = selectedUrlListing[imageVideoListPosition]

            if (!media.isNullOrEmpty()) {


                var mfile: File? = null
                mfile = File(media)
                val imageFile: RequestBody? = mfile.asRequestBody("image/*".toMediaTypeOrNull())
                var photo: MultipartBody.Part? = null
                photo = MultipartBody.Part.createFormData("image", mfile?.name, imageFile!!)
                imagePathList.add(photo)
                var type: RequestBody
                if (media.contains(".mp4")) {
                    type = "video".toRequestBody("text/plain".toMediaTypeOrNull())
                } else {
                    type = "image".toRequestBody("text/plain".toMediaTypeOrNull())
                }

                Log.e("imageimage", type.toString())
                val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
                appViewModel.sendUploadImageData(type, users, imagePathList)
            } else {

                imageVideoListPosition = imageVideoListPosition + 1

                if (imageVideoListPosition <= selectedUrlListing.size) {
                    hitApiForBannerImages(imageVideoListPosition)
                }
            }

        }
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
            R.id.ivImg3 -> {

                IMAGE_SELECTED_TYPE = "4"
                checkPermission(this)
            }
            R.id.ivplus -> {

                IMAGE_SELECTED_TYPE = "5"
                checkPermission(this)
            }

            R.id.et_addressNursery -> {
                showPlacePicker()
            }
            R.id.btn_submit -> {
                val nurseryName = et_nursery_name.text.toString()
                val addInfoSpinner = addInfoSpinner.selectedItem.toString()
                val noOfPlacesSpinner = noOfPlacesSpinner.selectedItem.toString()
                val etPhoneNumber = et_phoneNumber.text.toString()
                val etAddressNursery = et_addressNursery.text.toString()
                val description = et_description.text.toString()

                if (IMAGE_SELECTED_TYPE.equals("")) {
                    myCustomToast("Please select at-least one media file image or video .")
                } else {
                    if (nurseryName.isEmpty()) {
                            myCustomToast("Please enter your nursery name ")
                        } else {
                            if (addInfoSpinner.isEmpty()) {
                                myCustomToast("Please Add nursery information ")
                            } else {
                                if (noOfPlacesSpinner.isEmpty()) {
                                    myCustomToast("Please enter number of places")
                                } else {
                                    if (etPhoneNumber.isEmpty()) {
                                        myCustomToast("Please enter phone number ")
                                    } else {
                                        if (etAddressNursery.isEmpty()) {
                                            myCustomToast(" Please select your address ")

                                        } else {
                                            if (description.isEmpty()) {
                                                myCustomToast("Please enter your description")
                                            } else {
                                                // adding values
                                            //    activity_type =  orderby.selectedItem.toString()
                                               /* shop_name = shopName
                                                activity_name = activityName
                                                descp =  description
                                                messagee = message
                                                phonee = phone*/



                                                // checking the list
                                                if (selectedUrlListing.size.equals(urlListingFromResponse.size)) {
                                                    selectedUrlListing.clear()
                                                    urlListingFromResponse.clear()
                                                }

                                                // for check upper images url from response
                                                Log.d("imageVideoListSize", "-----------" + imageVideoUrlListing)


                                                // rotating loop
                                                for (i in 0..imageVideoUrlListing.size - 1) {
                                                    val media = imageVideoUrlListing[i]
                                                    if (!media.isEmpty()) {
                                                        selectedUrlListing.add(media)
                                                    }
                                                }

                                                // hitting api for upper 5 images
                                                hitApiForBannerImages(0)

                                            }
                                     //   }
                                    }
                                }
                            }
                        }
                    }
                }


            }
        }
    }


    private fun checkMvvmResponse() {

        // add post for activity
        appViewModel.observe_addPostActivity_Response()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("addPostActivityResopnse", "-----------" + Gson().toJson(response.body()))
                if (response.body() != null) {
                    progressDialog.hidedialog()
                    OpenActivity(DetailActivity::class.java)
                }
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

                            // now making json format for upper images media
                            for (i in 0..urlListingFromResponse.size - 1) {
                                val json = JSONObject()
                                json.put("image", urlListingFromResponse[i])
                                media.put(json)
                            }


                        //    hitFinallyActivityAddPostApi()


                        Log.d("urlListt", "-------------" + urlListingFromResponse.toString())

                    }
                } else {
                    ErrorBodyResponse(response, this, null)
                }
            }
        })


        appViewModel.getException()!!.observe(this, androidx.lifecycle.Observer {
            myCustomToast(it)
            progressDialog.hidedialog()

        })

    }


    private fun showPlacePicker() {
        // Initialize Places.
        Places.initialize(applicationContext, googleMapKey)
        // Create a new Places client instance.
        val placesClient: PlacesClient = Places.createClient(this)
        // Set the fields to specify which types of place data to return.
        val fields: List<Place.Field> = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === PLACE_PICKER_REQUEST) {
            if (resultCode === Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)

                cityAddress = place.address.toString()
                et_addressNursery.setText(cityAddress.toString())
                cityName = place.name.toString()

                // cityID = place.id.toString()
                cityLatitude = place.latLng?.latitude.toString()
                cityLongitude = place.latLng?.longitude.toString()

                Log.i("dddddd", "Place: " + place.name + ", " + place.id + "," + place.address + "," + place.latLng)
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


}