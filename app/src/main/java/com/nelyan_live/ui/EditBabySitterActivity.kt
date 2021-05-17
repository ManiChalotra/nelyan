package com.nelyan_live.ui

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.*
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.nelyan_live.adapter.ActivityTypeDialogboxAdapter
import com.nelyan_live.adapter.ChildCareTypeDialogboxAdapter
import com.nelyan_live.data.network.responsemodels.ImageUploadApiResponseModel
import com.nelyan_live.modals.ModelPOJO
import com.nelyan_live.modals.activity_type.ActivityTypeResponse
import com.nelyan_live.modals.childcaretype.ChildCareTypeRespone
import com.nelyan_live.modals.myAd.ActivityimageMyAds
import com.nelyan_live.modals.myAd.ChildCareImageMyAds
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.activity_addactivity.*
import kotlinx.android.synthetic.main.activity_baby_sitter.*
import kotlinx.android.synthetic.main.activity_baby_sitter.ivImg
import kotlinx.android.synthetic.main.activity_baby_sitter.ivImg1
import kotlinx.android.synthetic.main.activity_baby_sitter.ivImg2
import kotlinx.android.synthetic.main.activity_baby_sitter.ivImg3
import kotlinx.android.synthetic.main.activity_baby_sitter.ivplus
import kotlinx.android.synthetic.main.activity_home_child_care_details.*
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

    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    var mediaArrayBody: ArrayList<MultipartBody.Part> = ArrayList()


    private var IMAGE_SELECTED_TYPE = ""
    private val job by lazy { kotlinx.coroutines.Job() }
    private val dataStoragePreference by lazy { DataStoragePreference(this) }
    private var media: JSONArray = JSONArray()
    private var countryCodee = "91"
    private var childCareType = ""
    private var childCareId = ""

    /*
      private  var maternal_name = ""
      private  var phone_number= ""
      private  var descp_baby_sitter = ""
      private  var address_baby_sitter = ""*/
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
    private var isImageUploaded=""

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

        tv_child_care_type.visibility = View.VISIBLE
        sp_child_care_type.visibility = View.GONE



        tv_child_care_type.setOnClickListener {
            getModelTypeDialog()
        }


        /* // setting the  spinner
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
 */
        // Child care Type  spinner
        /*val childCareList: MutableList<String?> = ArrayList()
        childCareList.add("")
        childCareList.add(getString(R.string.nursery))
        childCareList.add(getString(R.string.maternal_asistant))
        childCareList.add(getString(R.string.baby_sitter))*/


        launch(Dispatchers.Main.immediate) {
            authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))?.first()
        }

     }

    private fun hitChildCareType_Api() {
        if (checkIfHasNetwork(this@EditBabySitterActivity)) {
            launch(Dispatchers.Main.immediate) {
                authKey = dataStoragePreference?.emitStoredValue(preferencesKey<String>("auth_key"))?.first()
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

             if (childCareId.equals("1")) {
                 childCareType = getString(R.string.nursery)
                 tv_child_care_type.text = getString(R.string.nursery)

             } else if (childCareId.equals("2")) {
                 childCareType = getString(R.string.maternal_assistant)
                 tv_child_care_type.text = getString(R.string.maternal_assistant)

             } else if (childCareId.equals("3")) {
                 getString(R.string.baby_sitter)
                 tv_child_care_type.text = getString(R.string.baby_sitter)
             }

             hitChildCareType_Api()

             val country: ArrayList<String?> = ArrayList()
             country.add(childCareType)

             val arrayAdapte1 = ArrayAdapter<Any?>(this, R.layout.customspinner, country as List<String>)
             sp_child_care_type.adapter = arrayAdapte1
             if (!childCareType.isNullOrEmpty()) {
                 val spinnerPosition = arrayAdapte1.getPosition(childCareType)
                 sp_child_care_type.setSelection(spinnerPosition)
             }


             // initalize the lists
             /*if (this::childimageList.isInitialized) {
                 childimageList.clear()

             } else {*/
                 childimageList = ArrayList()
                 childimageList = intent.getSerializableExtra("childCareImageList") as ArrayList<ChildCareImageMyAds>

                 if (!childimageList.isNullOrEmpty()) {
                     if (childimageList.size == 1) {
                         image1 = childimageList.get(0).image
                         image2 = ""
                         image3 = ""
                         Glide.with(this).load(image_base_URl + childimageList.get(0).image).error(R.mipmap.no_image_placeholder).into(ivImg)

                     } else if (childimageList.size == 2) {
                         image1 = childimageList.get(0).image
                         image2 = childimageList.get(1).image
                         image3 = ""
                         Glide.with(this).load(image_base_URl + childimageList.get(0).image).error(R.mipmap.no_image_placeholder).into(ivImg)
                         Glide.with(this).load(image_base_URl + childimageList.get(1).image).error(R.mipmap.no_image_placeholder).into(ivImg1)

                     } else if (childimageList.size == 3) {
                         image1 = childimageList.get(0).image
                         image2 = childimageList.get(1).image
                         image3 = childimageList.get(2).image
                         Glide.with(this).load(image_base_URl + childimageList.get(0).image).error(R.mipmap.no_image_placeholder).into(ivImg)
                         Glide.with(this).load(image_base_URl + childimageList.get(1).image).error(R.mipmap.no_image_placeholder).into(ivImg1)
                         Glide.with(this).load(image_base_URl + childimageList.get(2).image).error(R.mipmap.no_image_placeholder).into(ivImg2)

                     }
                /* }*/

                 launch(Dispatchers.Main.immediate) {
                     authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
                 }

//            hitDetailApi()
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

            R.id.ivImg -> {
                clickImg1 = true
                clickImg2 = false
                clickImg3 = false
                IMAGE_SELECTED_TYPE = "1"
                checkPermission(this)
            }
            R.id.ivImg1 -> {
                clickImg1 = false
                clickImg2 = true
                clickImg3 = false
                IMAGE_SELECTED_TYPE = "2"
                checkPermission(this)
            }
            R.id.ivImg2 -> {
                clickImg1 = false
                clickImg2 = false
                clickImg3 = true
                IMAGE_SELECTED_TYPE = "3"
                checkPermission(this)
            }
/*
            R.id.ivImg3 -> {
                IMAGE_SELECTED_TYPE = "4"
                checkPermission(this)
            }
            R.id.ivplus -> {
                IMAGE_SELECTED_TYPE = "5"
                checkPermission(this)
            }
*/
            R.id.ivBackBabaySitter -> {
                onBackPressed()
            }
            R.id.btnSubmitBabySitter -> {
                maternalName = et_maternalName.text.toString()
             //   childCareType = sp_child_care_type.selectedItem.toString()
                placeSpin = et_number_of_places.text.toString()
                phoneNumber = et_phoneNumber.text.toString()
                address_baby_sitter = et_addressBabySitter.text.toString()
                descp_baby_sitter = et_descriptionBabySitter.text.toString()

                /*if (IMAGE_SELECTED_TYPE.equals("")) {
                    myCustomToast(getString(R.string.select_image_video_error))
                } else {*/
                    if (childCareId.isNullOrEmpty()) {
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
/*
                                            if (childCareType.equals("Nursery")) {
                                                childCareId = "1"

                                            } else if (childCareType.equals("Maternal Assistant")) {
                                                childCareId = "2"

                                            } else if (childCareType.equals("Baby Sitter")) {
                                                childCareId = "3"
                                            }
*/

                                            // checking the list
                                            if (selectedUrlListing.size.equals(urlListingFromResponse.size)) {
                                                selectedUrlListing.clear()
                                                urlListingFromResponse.clear()
                                            }

                                            // for check upper images url from response
                                            Log.d("imageVideoListSize", "-----------" + imageVideoUrlListing)

                                            // rotating loop
                                            for (i in 0..imageVideoUrlListing.size - 1) {
                                                val media = imageVideoUrlListing.get(i)
                                                if (!media.isEmpty()) {
                                                    selectedUrlListing.add(media)
                                                }
                                            }
                                            Log.d("selectedUpperimages", "-------------------" + selectedUrlListing.toString())

                                            /*if (isImageUploaded.equals("") && isImageUploaded !=null) {
                                                hitApiForBannerImages(0)
                                            }else {*/
                                                hitFinallyActivityAddPostApi()
                                            /*}*/
                                        }
                                    }
            //                    }
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
                setImageOnTab(imgPath, ivImg)
                imageVideoUrlListing.set(0, imgPath.toString())
                iv_image1.visibility = View.GONE
            }

            "2" -> {
                setImageOnTab(imgPath, ivImg1)
                imageVideoUrlListing.set(1, imgPath.toString())
            }

            "3" -> {
                setImageOnTab(imgPath, ivImg2)
                imageVideoUrlListing.set(2, imgPath.toString())
            }

            /*  "4" -> {
                  setImageOnTab(imgPath, ivImg3)
                  imageVideoUrlListing.set(3, imgPath.toString())
              }

              "5" -> {
                  setImageOnTab(imgPath, ivplus)
                  imageVideoUrlListing.set(4, imgPath.toString())
              }
  */
        }

    }

    private fun setImageOnTab(imgPATH: String?, imageview: ImageView?) {
        Log.d("getimage", "---------" + imgPATH.toString())
/*
        when (IMAGE_SELECTED_TYPE) {

            "1" -> {
             //   imageview?.setScaleType(ImageView.ScaleType.FIT_XY)
                checkVideoButtonVisibility(imgPATH.toString(), iv_video011)
            }
            "2" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video012)
            }
            "3" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video013)
            }
            *//* "4" -> {
                 checkVideoButtonVisibility(imgPATH.toString(), iv_video014)

             }
             "5" -> {
                 checkVideoButtonVisibility(imgPATH.toString(), iv_video015)
             }*//*
        }*/

        Glide.with(this).asBitmap().load(imgPATH).into(imageview!!)
        hitImageUploadApi(imgPATH)
    }

    private fun checkVideoButtonVisibility(imgpath: String, videoButton: ImageView) {

        if (imgpath?.contains(".mp4")!!) {
            videoButton.visibility = View.VISIBLE
        } else {
            videoButton.visibility = View.GONE
        }
    }

    //manu code..........................................

    fun hitImageUploadApi(imgPATH: String?) {
        var mfile: File? = null
        mfile = File(imgPATH)
        val imageFile: RequestBody? = mfile.asRequestBody("image/*".toMediaTypeOrNull())
        var photo: MultipartBody.Part? = null
        photo = MultipartBody.Part.createFormData("image", mfile?.name, imageFile!!)
        imagePathList.add(photo)
        var type: RequestBody
        type = "image".toRequestBody("text/plain".toMediaTypeOrNull())

        val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
        appViewModel.sendUploadImageData(type, users, imagePathList)
        progressDialog.setProgressDialog()
    }

    private fun getModelTypeDialog() {
        try {
            val d = Dialog(this@EditBabySitterActivity)
            d.setCancelable(true)
            d.window?.setGravity(Gravity.CENTER)
            d.requestWindowFeature(Window.FEATURE_NO_TITLE)
            d.setContentView(R.layout.dialog_vehicle_type)
            d.textsuccess.setText("Select type")
            d.setCancelable(true)
            d.btnok.setOnClickListener{
                d.dismiss()
            }
            val layoutManager = LinearLayoutManager(this@EditBabySitterActivity, LinearLayoutManager.VERTICAL, false)
            d.rv_state_dialog.layoutManager = layoutManager
            d.rv_state_dialog.adapter = ChildCareTypeDialogboxAdapter(this@EditBabySitterActivity, modelTypes,
                    object : ChildCareTypeDialogboxAdapter.onClickListener{
                override fun subcatClick(subcatName: String?,id: String) {
                    tv_child_care_type.setText(subcatName)
                    childCareId = id
                    d.dismiss()
                }

            })
            d.setCanceledOnTouchOutside(true)
            d.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun hitApiForBannerImages(position: Int) {

        imageVideoListPosition = position

        if (imageVideoListPosition <= selectedUrlListing.size - 1) {

            val media = selectedUrlListing[imageVideoListPosition]

            // if (!media.isNullOrEmpty()) {

//                var mfile: File? = null
//
//                mfile = File(selectedUrlListing)
//                val imageFile: RequestBody? = mfile.asRequestBody("image/*".toMediaTypeOrNull())
//                var photo: MultipartBody.Part? = null
//                photo = MultipartBody.Part.createFormData("image", mfile?.name, imageFile!!)
//                imagePathList.add(photo)

            if (imagePathList != null) {
                imagePathList!!.clear()
            } else {
                imagePathList = java.util.ArrayList()

            }


            var mfile: File? = null
            for (i in 0..selectedUrlListing!!.size - 1) {
                mfile = File(selectedUrlListing!![i])
                val imageFile: RequestBody? = RequestBody.create("image/*".toMediaTypeOrNull(), mfile)
                var photo: MultipartBody.Part? = null
                photo = MultipartBody.Part.createFormData("image", mfile?.name, imageFile!!)
                imagePathList!!.add(photo)
            }


            var type: RequestBody = "image".toRequestBody("text/plain".toMediaTypeOrNull())
            /*
            if (media.contains(".mp4")) {
                type = "video".toRequestBody("text/plain".toMediaTypeOrNull())
            } else {
                type = "image".toRequestBody("text/plain".toMediaTypeOrNull())
            }

            Log.e("imageimage", type.toString())
            */
            val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
            appViewModel.sendUploadImageData(type, users, imagePathList)
            progressDialog.setProgressDialog()
            //}

            /*
             else {


                imageVideoListPosition = imageVideoListPosition + 1

                if (imageVideoListPosition <= selectedUrlListing.size) {
                    hitApiForBannerImages(imageVideoListPosition)
                }
            }


            */

        }
    }


    private fun checkMvvmResponse() {
        // add post for Maternal assistant
        Log.e("going", "messsaaaa")

        appViewModel.observeChildCareTypeResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {

                    val jsonObject = JSONObject(response.body().toString())
                    val mSizeOfData = jsonObject.getJSONArray("data").length()

                    var childCareTypeJsonArray = jsonObject.getJSONArray("data")


                    for (i in 0 until mSizeOfData) {
                        val id = childCareTypeJsonArray.getJSONObject(i).get("id").toString().toInt()
                        val name = childCareTypeJsonArray.getJSONObject(i).get("categoryName").toString()

                        modelTypes!!.add(ChildCareTypeRespone.Data( id,  name))

                    }

                }
                   /* val jsonObject = JSONObject(response.body().toString())
                    var jsonArray = jsonObject.getJSONArray("data")
                    val country: ArrayList<String?> = ArrayList()
                    country.add("")
                    for (i in 0 until jsonArray.length()) {
                        val name = jsonArray.getJSONObject(i).get("categoryName").toString()
                        country.add(name)
                    }
                    val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.customspinner, country as List<Any?>)
                    sp_child_care_type!!.setAdapter(arrayAdapter)
                }*/
            } else {
                ErrorBodyResponse(response, this, null)
            }
        })


        // for imageUpload api
        appViewModel.observeUploadImageResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("urlDataLoading", "------------" + Gson().toJson(response.body()))
                if (response.body() != null) {
                    if (response.body()!!.data != null) {

                            if (clickImg1 == true) {
                                image1 = response.body()!!.data!![0].image.toString()

                            } else if (clickImg2 == true) {
                                image2 = response.body()!!.data!![0].image.toString()

                            } else if (clickImg3 == true) {
                                image3 = response.body()!!.data!![0].image.toString()
                            }

                            progressDialog.hidedialog()


                        if (urlListingFromResponse != null) {
                            urlListingFromResponse!!.clear()
                        } else {
                            urlListingFromResponse = ArrayList()

                        }

                        var mlist = response.body()!!.data
                        if (mlist.isNullOrEmpty()) {
                            mlist = ArrayList()
                        } else {
                            makeImageJsonArray(mlist)

                        }


/*
                        urlListingFromResponse.add(response.body()!!.data!![0].image.toString())
                        if (imageVideoListPosition <= 4) {
                            imageVideoListPosition = imageVideoListPosition + 1
                            hitApiForBannerImages(imageVideoListPosition)
                        }
                        // now making json format for upper images media
                        for (i in 0..urlListingFromResponse.size - 1) {
                            val json = JSONObject()
                            json.put("image", urlListingFromResponse[i])
                            media.put(json)
                        }

                        hitFinallyActivityAddPostApi()

                        Log.d("urlListt", "-------------" + urlListingFromResponse.toString())

                    }
*/
                     //   hitFinallyActivityAddPostApi()
                      //  progressDialog.hidedialog()
                    }
                } else {
                    ErrorBodyResponse(response, this, null)
                    progressDialog.hidedialog()
                }
            }
        })

        appViewModel.observe_editMaternalPost_Response()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("EditMaternalPostResopnse", "-----------" + Gson().toJson(response.body()))
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
        for (i in 0..mlist!!.size - 1) {
            val image = mlist.get(i).image.toString()
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
                    /*media.toString(),*/ image1, image2, image3, postID)
        } else {
            showSnackBar(this, getString(R.string.no_internet_error))
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