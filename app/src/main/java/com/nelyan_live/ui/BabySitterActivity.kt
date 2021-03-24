package com.nelyan_live.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
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
import com.nelyan_live.HELPER.image
import com.nelyan_live.R
import com.nelyan_live.modals.ModelPOJO
import com.nelyan_live.utils.OpenCameraGallery
import com.nelyan_live.utils.ProgressDialog
import com.nelyan_live.utils.myCustomToast
import kotlinx.android.synthetic.main.activity_activity3.*
import kotlinx.android.synthetic.main.activity_baby_sitter.*
import kotlinx.android.synthetic.main.activity_baby_sitter.ivImg
import kotlinx.android.synthetic.main.activity_baby_sitter.ivImg1
import kotlinx.android.synthetic.main.activity_baby_sitter.ivImg2
import kotlinx.android.synthetic.main.activity_baby_sitter.ivImg3
import kotlinx.android.synthetic.main.activity_baby_sitter.ivplus
import kotlinx.android.synthetic.main.activity_maternal_assistant.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.io.File
import java.util.*
import kotlin.coroutines.CoroutineContext

class BabySitterActivity : OpenCameraGallery(), View.OnClickListener, CoroutineScope {


    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    private var IMAGE_SELECTED_TYPE = ""
    private val job by lazy { kotlinx.coroutines.Job() }
    private val dataStoragePreference by lazy { DataStoragePreference(this) }

    private  var activity_type = ""
    private  var maternal_name = ""
    private  var phone_number= ""
    private  var descp_baby_sitter = ""
    private  var address_baby_sitter = ""

    private var selectedUrlListing: ArrayList<String> = ArrayList()
    private var urlListingFromResponse: ArrayList<String> = ArrayList()
    var imageVideoUrlListing = arrayListOf("", "", "", "", "")

    private var imageVideoListPosition = -1

    private var savedaddEventImagesData = false

    private var imagePathList = ArrayList<MultipartBody.Part>()
    private var imagePathList2 = ArrayList<MultipartBody.Part>()

    private lateinit var listAddEventDataModel: ArrayList<ModelPOJO.AddEventDataModel>

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baby_sitter)
        initalizeClicks()


        // setting the  spinner
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
        val arrayAdapte1: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.customspinner, count as List<Any?>)
        noOfPlacesBabySpinner!!.setAdapter(arrayAdapte1)


    }

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
            R.id.ivBackBabaySitter -> {
                finish()
            }
            R.id.btnSubmitBabySitter -> {

                val maternalName = et_maternalName.text.toString()
                val phoneNumber = et_phoneNumber.text.toString()
                val addressBabySitter = et_addressBabySitter.text.toString()
                val descriptionBabySitter = et_descriptionBabySitter.text.toString()


                if (IMAGE_SELECTED_TYPE.equals("")) {
                    myCustomToast("Please select atleast one media file image or video .")
                } else {
                    val activityType = orderby.selectedItem.toString()
                    if (activityType.equals("") || activityType.equals("Select")) {
                        myCustomToast("please select your activity Type ")
                    } else {
                        if (maternalName.isEmpty()) {
                            myCustomToast("Please enter your maternal assistant name ")
                        } else {
                            if (phoneNumber.isEmpty()) {
                                myCustomToast("Please enter your phone number")
                            } else {

                                if (addressBabySitter.isEmpty()) {
                                    myCustomToast("Please enter your address")
                                } else {
                                    if (descriptionBabySitter.isEmpty()) {
                                        myCustomToast("Please enter your description ")
                                    } else {
                                        // adding values
                                        activity_type = orderby.selectedItem.toString()
                                        maternal_name = maternalName
                                        phone_number = phoneNumber
                                        address_baby_sitter = addressBabySitter
                                        descp_baby_sitter = descriptionBabySitter


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


//                val i = Intent(this@BabySitterActivity, HomeActivity::class.java)
//                i.putExtra("activity", "babyfrag")
//                startActivity(i)
                                    }
                                }
                            }
                        }
                    }
                }

            }



                                    R.id.et_addressBabySitter->{
                                        showPlacePicker()
                                    }
                                }
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


    private fun hitApiForBannerImages(position: Int) {

        imageVideoListPosition = position

        if (imageVideoListPosition <= selectedUrlListing.size - 1) {


            val media = selectedUrlListing[imageVideoListPosition]

            if (!media.isNullOrEmpty()) {

                savedaddEventImagesData = false

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
                val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
                appViewModel.sendUploadImageData(type, users, imagePathList)
            } else {

                imageVideoListPosition = imageVideoListPosition + 1

                if (imageVideoListPosition <= selectedUrlListing.size) {
                    hitApiForBannerImages(imageVideoListPosition)
                }


            }

        } else {

            savedaddEventImagesData = true
            gettingURLOfEventImages()

            /* urlListingFromResponse.clear()
             addEventUrlListingResponse.clear()
             selectedUrlListing.clear()
             imagePathList.clear()
             savedaddEventImagesData = true
             gettingURLOfEventImages()
             */

        }


    }

    private fun gettingURLOfEventImages() {
        if (imagePathList2 != null) {
            imagePathList2.clear()
        }

        for (i in 0 until listAddEventDataModel.size) {
            val media = listAddEventDataModel[i].image
            if (!media.isNullOrEmpty()) {
                var mfile: File? = null
                mfile = File(media)
                val imageFile: RequestBody? = mfile.asRequestBody("image/*".toMediaTypeOrNull())
                var photo: MultipartBody.Part? = null
                photo = MultipartBody.Part.createFormData("image", mfile?.name, imageFile!!)
                imagePathList2.add(photo)
            }
        }

        Log.d("imagePathLsiting", "-------------" + imagePathList2!!.size)

        val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
        val type = "image".toRequestBody("text/plain".toMediaTypeOrNull())
        appViewModel.sendUploadImageData(type, users, imagePathList2)

    }



                            private fun setImageOnTab(imgPATH: String?, imageview: ImageView?) {
                                Log.d("getimage", "---------" + imgPATH.toString())

                                when (IMAGE_SELECTED_TYPE) {

                                    "1" -> {
                                        imageview?.setScaleType(ImageView.ScaleType.FIT_XY)
                                        checkVideoButtonVisibility(imgPATH.toString(), iv_video011)
                                    }
                                    "2" -> {
                                        checkVideoButtonVisibility(imgPATH.toString(), iv_video012)

                                    }
                                    "3" -> {
                                        checkVideoButtonVisibility(imgPATH.toString(), iv_video013)

                                    }
                                    "4" -> {
                                        checkVideoButtonVisibility(imgPATH.toString(), iv_video014)

                                    }
                                    "5" -> {
                                        checkVideoButtonVisibility(imgPATH.toString(), iv_video015)
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
                                        et_addressBabySitter.setText(cityAddress.toString())
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