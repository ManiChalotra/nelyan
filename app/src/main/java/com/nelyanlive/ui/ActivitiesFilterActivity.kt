package com.nelyanlive.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.homeactivitylist.HomeAcitivityResponseData
import com.nelyanlive.modals.homeactivitylist.HomeActivityResponse
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_addactivity.*
import kotlinx.android.synthetic.main.filter_activity.*
import kotlinx.android.synthetic.main.filter_activity.trader_type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.coroutines.CoroutineContext

class ActivitiesFilterActivity : AppCompatActivity(), CoroutineScope, View.OnClickListener {

    var btnFilter: Button? = null
    var tvCal: TextView? = null
    var dialog: Dialog? = null
    var ivBack: ImageView? = null
    var ll_1: LinearLayout? = null
    var activityTypes: Spinner? = null
    var orderby1: Spinner? = null
    var distance: String? = null
    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var latitudee = ""
    private var longitudee = ""
    private var typeId = ""
    private val job by lazy { Job() }
    private val activitisDatalist by lazy { ArrayList<HomeAcitivityResponseData>() }

    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    private var authKey = ""
    private val dataStoragePreference by lazy { DataStoragePreference(this@ActivitiesFilterActivity) }

    private val googleMapKey = "AIzaSyDQWqIXO-sNuMWupJ7cNNItMhR4WOkzXDE"
    private val PLACE_PICKER_REQUEST = 1

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_activity)

        if(intent.hasExtra("name"))
        { tvName.text = intent.getStringExtra("name")}

        ivBack = findViewById(R.id.ivBack)

        ivBack!!.setOnClickListener { onBackPressed() }
        checkMvvmResponse()
        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        tvCal = findViewById(R.id.tv_cal)
        tvCal!!.setOnClickListener(this)
            //ll_1 = findViewById(R.id.ll_public)
            //ll_1!!.setOnClickListener { dialogLocation() }
            //set city
        launch(Dispatchers.Main.immediate) {
            et_location.text = dataStoragePreference.emitStoredValue(preferencesKey<String>("cityLogin")).first()
            latitudee = dataStoragePreference.emitStoredValue(preferencesKey<String>("latitudeLogin")).first()
            longitudee = dataStoragePreference.emitStoredValue(preferencesKey<String>("longitudeLogin")).first()
        }

        btnFilter = findViewById(R.id.btnFilter)
        activityTypes = findViewById(R.id.trader_type)

        et_location.setOnClickListener(this)
        btnFilter!!.setOnClickListener(this)

        orderby1 = findViewById(R.id.spinner_dayss)
        val km = arrayOf<String?>("", "0KM", "5KM", "10KM", "15KM", "20KM", "25KM", "30KM")
        val kmValue = arrayOf<String?>("", "0", "5", "10", "15", "20", "25", "30")

        val adapter1: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, R.layout.size_customspinner, km)
        orderby1!!.adapter = adapter1

       orderby1!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
           override fun onItemSelected(
               parent: AdapterView<*>?,
               view: View?,
               position: Int,
               id: Long
           ) {
               distance = if(km[position].isNullOrBlank()){""}else{kmValue[position]!!}
           }

           override fun onNothingSelected(parent: AdapterView<*>?) {
               // TODO("Not yet implemented")
           }
       }

        if(tvName.text !="Event")
        {
            viewType.visibility = View.VISIBLE
            tvType.visibility = View.VISIBLE
            llType.visibility = View.VISIBLE
        launch(Dispatchers.Main.immediate) {
            authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
            appViewModel.sendActivityTypeData(security_key, authKey)
        }} }


    override fun onResume() {
        super.onResume()
         }

    private fun dateDialog() {
        val listener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> tvCal!!.text = dayOfMonth.toString() +
                "/" + (monthOfYear + 1) + "/" + year }
        val datePickerDialog = DatePickerDialog(this, R.style.datepicker, listener, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    private fun showPlacePicker() {
        // Initialize Places.
        Places.initialize(
                applicationContext,
                googleMapKey
        )
        // Create a new Places client instance.
        val placesClient: PlacesClient = Places.createClient(this@ActivitiesFilterActivity)
        // Set the fields to specify which types of place data to return.
        val fields: List<Place.Field> = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
        )
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this@ActivitiesFilterActivity)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

    }

    private fun dialogLocation() {
        launch(Dispatchers.Main.immediate) {
            et_location.text = dataStoragePreference.emitStoredValue(preferencesKey<String>("cityLogin")).first()
            latitudee = dataStoragePreference.emitStoredValue(preferencesKey<String>("latitudeLogin")).first()
            longitudee = dataStoragePreference.emitStoredValue(preferencesKey<String>("longitudeLogin")).first()
        }

    }
    val category: ArrayList<String?> = ArrayList()
    val categoryId: ArrayList<String?> = ArrayList()

    private fun checkMvvmResponse() {

        appViewModel.observeActivityTypeResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    val jsonArray = jsonObject.getJSONArray("data")
                    category.clear()
                    categoryId.clear()
                    category.add("")
                    categoryId.add("")
                    for (i in 0 until jsonArray.length()) {
                        val name = jsonArray.getJSONObject(i).get("name").toString()
                        val id = jsonArray.getJSONObject(i).get("id").toString()
                        category.add(name)
                        categoryId.add(id)
                    }
                    val arrayAdapte1 = ArrayAdapter(this, R.layout.customspinner, category as List<Any?>)
                    traderType.adapter = arrayAdapte1
                    traderType.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            typeId = if(position!=0) { categoryId[position]!! } else { "" }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            //TODO("Not yet implemented")
                        }
                    }
                }
            } else {
                ErrorBodyResponse(response, this, null)
            }
        })

        appViewModel.observeFilterActivityListResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    activity_filter_progressbar?.hideProgressBar()

                    val jsonObject = JSONObject(response.body().toString())
                    var jsonArray = jsonObject.getJSONArray("data")
                    val homeAcitivitiesResponse = Gson().fromJson(response.body().toString(), HomeActivityResponse::class.java)

                    if (activitisDatalist != null) {
                        activitisDatalist.clear()
                        activitisDatalist.addAll(homeAcitivitiesResponse.data)
                    }
                    val returnIntent = Intent()
                    returnIntent.putExtra("filteredActivitisDatalist", activitisDatalist)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            } else {
                ErrorBodyResponse(response, this, null)
                activity_filter_progressbar?.hideProgressBar()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
              //  city = place.name.toString()
                et_location.text = place.name.toString()

                // cityID = place.id.toString()
                latitudee = place.latLng?.latitude.toString()
                longitudee = place.latLng?.longitude.toString()

                Log.i("dddddd", "Place: " + place.name + ", " + place.id + "," + place.address + "," + place.latLng)
            }

            else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("dddddd", status.statusMessage.toString())
            } } }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.et_location ->{
                showPlacePicker()
            }
            R.id.tv_cal ->{
                dateDialog()
            }
            R.id.btnFilter ->{
                if (et_location.text.isNullOrEmpty()){
                  myCustomToast(getString(R.string.location_missing_error))
                }
                else {
                    when (tvName.text) {
                        "Event" -> {

                            val intent = Intent().putExtra("name",et_name.text.toString())
                                .putExtra("latitude",latitudee)
                                .putExtra("longitude",longitudee)
                                .putExtra("distance",distance)
                                .putExtra("location",et_location.text.toString())

                            setResult(1212,intent)
                            onBackPressed()
                        }
                        "Activity" -> {
                            val intent = Intent().putExtra("name",et_name.text.toString())
                                .putExtra("latitude",latitudee)
                                .putExtra("longitude",longitudee)
                                .putExtra("distance",distance)
                                .putExtra("typeId",typeId)
                                .putExtra("location",et_location.text.toString())

                            setResult(1213,intent)
                            onBackPressed()
                        }
                        else -> {
                           // hitFilterActivity_Api()
                        }
                    }
                }
            }
        }
    }
}