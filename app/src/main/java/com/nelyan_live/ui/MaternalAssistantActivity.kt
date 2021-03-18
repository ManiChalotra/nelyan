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
import com.nelyan_live.utils.OpenCameraGallery
import com.nelyan_live.utils.ProgressDialog
import kotlinx.android.synthetic.main.activity_maternal_assistant.*
import kotlinx.android.synthetic.main.activity_maternal_assistant.ivImg
import kotlinx.android.synthetic.main.activity_maternal_assistant.ivImg1
import kotlinx.android.synthetic.main.activity_maternal_assistant.ivImg2
import kotlinx.android.synthetic.main.activity_maternal_assistant.ivImg3
import kotlinx.android.synthetic.main.activity_maternal_assistant.ivplus
import kotlinx.android.synthetic.main.activity_nurserie.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.util.*
import kotlin.coroutines.CoroutineContext

class MaternalAssistantActivity : OpenCameraGallery(), View.OnClickListener, CoroutineScope {


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maternal_assistant)
        initalizeClicks()


        // setting the spinner
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
        noOfPlacesMaterialSpinner.setAdapter(arrayAdapte1)

        /*
          btnSubmit = findViewById(R.id.btnSubmit)
          btnSubmit!!.setOnClickListener(View.OnClickListener {
              val i = Intent(this@MaternalAssistantActivity, HomeActivity::class.java)
              i.putExtra("activity", "mater")
              startActivity(i)
          })*/


    }

    private fun initalizeClicks() {

        et_addressMaterial.isFocusable = false
        et_addressMaterial.isFocusableInTouchMode = false
        et_addressMaterial.setOnClickListener(this)

        ivImg.setOnClickListener(this)
        ivImg1.setOnClickListener(this)
        ivImg2.setOnClickListener(this)
        ivImg3.setOnClickListener(this)
        ivplus.setOnClickListener(this)

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
                checkVideoButtonVisibility(imgPATH.toString(), iv_video01)
            }
            "2" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video02)

            }
            "3" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video03)

            }
            "4" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video04)

            }
            "5" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video05)
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

    override fun onClick(v: View?) {
        when (v!!.id) {
          

            R.id.et_addressMaterial -> {
                showPlacePicker()
            }
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
                et_addressMaterial.setText(cityAddress.toString())
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

}