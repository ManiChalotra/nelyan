package com.nelyan_live.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ImageView
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
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.meherr.mehar.db.DataStoragePreference
import com.nelyan_live.R
import com.nelyan_live.adapter.AgeGroupRepeatAdapter
import com.nelyan_live.adapter.EventRepeatAdapter
import com.nelyan_live.modals.ModelPOJO
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.activity_activity3.*
import kotlinx.android.synthetic.main.activity_activity3.ivBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.coroutines.CoroutineContext

class ActivityFormActivity : OpenCameraGallery(), OnItemSelectedListener, View.OnClickListener, CoroutineScope, AgeGroupRepeatAdapter.OnAGeGroupRecyclerViewItemClickListner, EventRepeatAdapter.OnEventRecyclerViewItemClickListner{

    private val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java)
    }
    private var IMAGE_SELECTED_TYPE = ""
    private val job by lazy { kotlinx.coroutines.Job() }

    private val dataStoragePreference by lazy { DataStoragePreference(this@ActivityFormActivity) }

    // Initialize Places variables
    private val googleMapKey = "AIzaSyDQWqIXO-sNuMWupJ7cNNItMhR4WOkzXDE"
    private val PLACE_PICKER_REQUEST = 1


    // json Array
    private lateinit var ageGroup:JSONArray
    private  lateinit var addEvent:JSONArray
    private  lateinit var media:JSONArray

    // dialo for progress
    private  var progressDialog = ProgressDialog(this)

    // initalize the variables
    private  lateinit  var listAgeGroupDataModel :ArrayList<ModelPOJO.AgeGroupDataModel>
    private  lateinit var  listAddEventDataModel:ArrayList<ModelPOJO.AddEventDataModel>
    private  lateinit var  ageGroupRepeatAdapter:AgeGroupRepeatAdapter
    private  lateinit var  addEventRepeatAdapter:EventRepeatAdapter

    private var cityName = ""
    private var cityLatitude = ""
    private var cityLongitude = ""
    private var cityAddress = ""
    private var authKey = ""

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onResume() {
        super.onResume()
        hitTypeActivity_Api()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity3)
        initalizeclicks()
        checkMvvmResponse()

        ageGroupRepeatAdapter = AgeGroupRepeatAdapter(this,listAgeGroupDataModel, this)
        rvAgeGroup!!.adapter = ageGroupRepeatAdapter

        addEventRepeatAdapter = EventRepeatAdapter(this, listAddEventDataModel, this)
        rvEvent!!.adapter = addEventRepeatAdapter

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun initalizeclicks() {

        // initalize the lists
        if(this::listAgeGroupDataModel.isInitialized){
            listAgeGroupDataModel.clear()

        }else{
            listAgeGroupDataModel = ArrayList()
            listAgeGroupDataModel.add(ModelPOJO.AgeGroupDataModel("","","","",""))

        }

        if(this::listAddEventDataModel.isInitialized){
            listAddEventDataModel.clear()

        }else{
            listAddEventDataModel = ArrayList()
            listAddEventDataModel.add(ModelPOJO.AddEventDataModel("","","","","",""))
        }


        // clicks for images
        rlImg.setOnClickListener(this)
        ivImg1.setOnClickListener(this)
        ivImg2.setOnClickListener(this)
        ivImg3.setOnClickListener(this)
        ivplus.setOnClickListener(this)

        // clicks for buttons
        btnSubmit.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        et_addressActivity.isFocusable = false
        et_addressActivity.isFocusableInTouchMode = false
        et_addressActivity.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlImg -> {
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
            R.id.btnSubmit -> {

                Log.d("myagelistttttt","------"+ listAgeGroupDataModel)

                val shopName = et_shopName.text.toString()
                val activityName = et_activityName.text.toString()
                val description = et_description.text.toString()
                val phone = et_phone.text.toString()
                val address = et_addressActivity.text.toString()
                val message = et_message.text.toString()

                if (IMAGE_SELECTED_TYPE.equals("")) {
                    myCustomToast("Please select atleast one media file image or video .")
                } else {
                    val activityType = orderby.selectedItem.toString()
                    if (activityType.equals("") || activityType.equals("Select")) {
                        myCustomToast("please select your activity Type ")
                    } else {
                        if (shopName.isEmpty()) {
                            myCustomToast("Please enter your shop name ")
                        } else {
                            if (activityName.isEmpty()) {
                                myCustomToast("Please  enter your activity name")
                            } else {

                                if (message.isEmpty()) {
                                    myCustomToast("Please enter your message")
                                } else {
                                    if (description.isEmpty()) {
                                        myCustomToast("Please enter your description ")
                                    } else {
                                        if (phone.isEmpty()) {
                                            myCustomToast("Please  enter your phone ")

                                        } else {
                                            if (address.isEmpty()) {
                                                myCustomToast("Please  select your address")
                                            } else {
                                                hitApi(activityType, shopName, activityName, description,message, phone, cityAddress, cityLatitude, cityLongitude, cityName)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.et_addressActivity -> {
                showPlacePicker()
            }
        }
    }

    private fun hitApi(activityType: String, shopName: String, activityName: String, description: String, message: String, phone: String, cityName: String, cityLatitude: String, cityLongitude: String, cityaddress: String?) {
        appViewModel.send_addPostActivity_Data(security_key, authKey, "1", activityType, shopName, activityName, description, message, phone, cityAddress, cityName, cityLatitude, cityLongitude, ageGroup.toString(), addEvent.toString(), media.toString())
    progressDialog.setProgressDialog()
    }

    private fun showPlacePicker() {
        // Initialize Places.
        Places.initialize(
                applicationContext,
                googleMapKey
        )
        // Create a new Places client instance.
        val placesClient: PlacesClient = Places.createClient(this)
        // Set the fields to specify which types of place data to return.
        val fields: List<Place.Field> = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
        )
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === PLACE_PICKER_REQUEST) {
            if (resultCode === Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)

                cityAddress = place.address.toString()
                et_addressActivity.setText(cityAddress.toString())
                cityName = place.name.toString()

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


    override fun getRealImagePath(imgPath: String?) {
        Log.d("selectedImagePath", "-------" + imgPath)

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
                checkVideoButtonVisibility(imgPATH.toString(), iv_video1)
            }
            "2" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video2)

            }
            "3" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video3)

            }
            "4" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video4)

            }
            "5" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video5)
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

    override fun addAgeGroupItem(list: ArrayList<ModelPOJO.AgeGroupDataModel>, position: Int) {

        Log.d("listtAgrGroup","--------"+ list)
        listAgeGroupDataModel.add(ModelPOJO.AgeGroupDataModel("","","","",""))
       ageGroupRepeatAdapter.notifyDataSetChanged()

    }

    override fun onAddEventItem(list: ArrayList<ModelPOJO.AddEventDataModel>, position: Int) {
            listAddEventDataModel.add(ModelPOJO.AddEventDataModel("","","","","",""))
        addEventRepeatAdapter.notifyDataSetChanged()
    }

    private fun hitTypeActivity_Api() {
        launch(Dispatchers.Main.immediate) {
            authKey = dataStoragePreference?.emitStoredValue(preferencesKey<String>("auth_key"))?.first()
            appViewModel.sendActivityTypeData(security_key, authKey)
        }
    }

    private fun checkMvvmResponse() {
        appViewModel.observeActivityTypeResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    var jsonArray = jsonObject.getJSONArray("data")
                    val country: ArrayList<String?> = ArrayList()
                    country.add("")
                    for (i in 0 until jsonArray.length()) {
                        val name = jsonArray.getJSONObject(i).get("name").toString()
                        country.add(name)
                    }
                    val arrayAdapte1 = ArrayAdapter<Any?>(this, R.layout.customspinner, country as List<Any?>)
                    orderby.adapter = arrayAdapte1
                }
            } else {
                ErrorBodyResponse(response, this, null)
            }
        })


        // add post for activity
        appViewModel.observe_addPostActivity_Response()!!.observe(this, androidx.lifecycle.Observer { response->
            if(response!!.isSuccessful && response.code()==200){
                if(response.body()!= null){
                    progressDialog.hidedialog()
                    OpenActivity(DetailActivity::class.java)
                }
            }else{
                ErrorBodyResponse(response, this, null)
            }
        })

        appViewModel.getException()!!.observe(this, androidx.lifecycle.Observer {
            myCustomToast(it)
            progressDialog.hidedialog()

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


}