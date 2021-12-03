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
import kotlinx.android.synthetic.main.filter_activity.*
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
    var activityTypes: Spinner? = null
    var orderby1: Spinner? = null
    var distance: String? = null
    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var latitudee = ""
    private var longitudee = ""
    private var typeId = ""
    var SelectValue = ""
    var ReturnDistance = ""
    var ActivityType = ""
//    var int poz = 0
    var poz: Int = 0

    private val job by lazy { Job() }
    private val activitisDatalist by lazy { ArrayList<HomeAcitivityResponseData>() }

    private val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(AppViewModel::class.java)
    }
    private var authKey = ""
    private val dataStoragePreference by lazy { DataStoragePreference(this@ActivitiesFilterActivity) }

    private val googleMapKey = "AIzaSyDQWqIXO-sNuMWupJ7cNNItMhR4WOkzXDE"
    private val PLACE_PICKER_REQUEST = 1

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_activity)

        if (intent.hasExtra("name")) {
            tvName.text = intent.getStringExtra("name")

        }
        Log.d(
            "ActivityFilterActivity ",
            "OnCreateCalled   "
        )
        getFilterValues()
        ivBack = findViewById(R.id.ivBack)

        ivBack!!.setOnClickListener { onBackPressed() }
        checkMvvmResponse()
        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        tvCal = findViewById(R.id.tv_cal)
        tvCal!!.setOnClickListener(this)

        launch(Dispatchers.Main.immediate) {
//            et_location.text =
//                dataStoragePreference.emitStoredValue(preferencesKey<String>("cityLogin")).first()
            latitudee =
                dataStoragePreference.emitStoredValue(preferencesKey<String>("latitudeLogin"))
                    .first()
            longitudee =
                dataStoragePreference.emitStoredValue(preferencesKey<String>("longitudeLogin"))
                    .first()
        }

        btnFilter = findViewById(R.id.btnFilter)
        activityTypes = findViewById(R.id.trader_type)

        et_location.setOnClickListener(this)
        btnFilter!!.setOnClickListener(this)
        tvFilterclear!!.setOnClickListener(this)

        orderby1 = findViewById(R.id.spinner_dayss)
        val km = arrayOf<String?>(
            "Distance",
            "0KM",
            "5KM",
            "10KM",
            "15KM",
            "20KM",
            "25KM",
            "30KM",
            "35KM",
            "40KM",
            "45KM",
            "50KM"
        )
        val kmValue =
            arrayOf<String?>("", "0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50")

        val adapter1: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this, R.layout.size_customspinner, km
        )
        orderby1!!.adapter = adapter1
        val arr: MutableList<Array<String?>> = Arrays.asList(kmValue)

        for (i in 0 until arr.get(0).size) {
            Log.e("checkdata", "categoty" + "----" + arr.get(0).get(i).toString())
            if (ReturnDistance.equals(arr.get(0).get(i).toString())) {
                Log.e("checkdata", "categoty_if" + i + arr.get(0).get(i).toString())
                spinner_dayss.setSelection(i)
            }
        }

        orderby1!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                distance = if (kmValue[position].isNullOrBlank()) {
                    ""
                } else {
                    kmValue[position]!!
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // TODO("Not yet implemented")
            }
        }
        if (tvName.text != getString(R.string.event)) {
            viewType.visibility = View.VISIBLE
            tvType.visibility = View.VISIBLE
            llType.visibility = View.VISIBLE

            launch(Dispatchers.Main.immediate) {
                authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                    .first()
                appViewModel.sendActivityTypeData(security_key, authKey)
            }
            Log.d(ActivitiesFilterActivity::class.java.name, "ActivitiesFilter_if   ")
        } else {
            tvType.text = getString(R.string.event_type)
            launch(Dispatchers.Main.immediate) {
                authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                    .first()
                appViewModel.sendActivityTypeData(security_key, authKey)
            }

            Log.d(ActivitiesFilterActivity::class.java.name, "ActivitiesFilter_else   ")
            et_name.hint = getString(R.string.enter_event_name)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(
            "ActivityFilterActivity ",
            "onResumeCalled   "
        )
    }

    private fun dateDialog() {
        val listener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            tvCal!!.text = dayOfMonth.toString() +
                    "/" + (monthOfYear + 1) + "/" + year
        }
        val datePickerDialog =
            DatePickerDialog(this, R.style.datepicker, listener, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    private fun showPlacePicker() {
        // Initialize Places.
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
            .build(this@ActivitiesFilterActivity)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

    }

    val category: ArrayList<String?> = ArrayList()
    val categoryId: ArrayList<String?> = ArrayList()

    private fun checkMvvmResponse() {

        appViewModel.observeActivityTypeResponse()!!
            .observe(this, androidx.lifecycle.Observer { response ->
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
                            var id = jsonArray.getJSONObject(i).get("id").toString()
                            Log.e("checkmyactivity", "id   " + id + "---" + ActivityType)
                            if (id.equals(ActivityType)) {
                                poz = i
                                Log.e("checkmyactivity", "-CCCCC---" + poz)

                            }
                            category.add(name)
                            categoryId.add(id)
                        }
                        val arrayAdapte1 =
                            ArrayAdapter(this, R.layout.customspinner, category as List<Any?>)
                        traderType.adapter = arrayAdapte1
//                        traderType.setSelection(poz)
                        // for check the value wit api

                        traderType.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    Log.d(
                                        "ActivityFilterActivity   ",
                                        "Selected_Item    " + categoryId[position]!!
                                    )
                                    Log.d(
                                        "ActivityFilterActivity   ",
                                        "Selected_traderType    " + traderType.getSelectedItem()
                                            .toString()
                                    )
//                                    SelectValue = traderType.getSelectedItem()
//                                        .toString()
//                                    AllSharedPref.save(
//                                        this,
//                                        "selecttype",
//                                        "HelloIndia"
//                                    )
                                    typeId = if (position != 0) {
                                        categoryId[position]!!
                                    } else {
                                        ""
                                    }
                                    Log.d(
                                        "ActivityFilterActivity ",
                                        "returnValues_typeId   " + typeId
                                    )
                                    SelectValue = typeId
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

        appViewModel.observeFilterActivityListResponse()!!
            .observe(this, androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    if (response.body() != null) {
                        activity_filter_progressbar?.hideProgressBar()

                        val jsonObject = JSONObject(response.body().toString())
                        var jsonArray = jsonObject.getJSONArray("data")
                        val homeAcitivitiesResponse = Gson().fromJson(
                            response.body().toString(),
                            HomeActivityResponse::class.java
                        )

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

                Log.i(
                    "dddddd",
                    "Place: " + place.name + ", " + place.id + "," + place.address + "," + place.latLng
                )
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("dddddd", status.statusMessage.toString())
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.et_location -> {
                showPlacePicker()
            }
            R.id.tv_cal -> {
                dateDialog()
            }
            R.id.tvFilterclear -> {
                val i = Intent(this, ActivitiesListActivity::class.java)
                startActivity(i)
                finish()
            }
            R.id.btnFilter -> {
                if (et_location.text.isNullOrEmpty()) {
                    myCustomToast(getString(R.string.location_missing_error))
                } else {
                    when (tvName.text) {
                        getString(R.string.event) -> {
                            val intent = Intent().putExtra("name", et_name.text.toString())
                                .putExtra("latitude", latitudee)
                                .putExtra("longitude", longitudee)
                                .putExtra("distance", distance)
                                .putExtra("location", et_location.text.toString())
                                .putExtra("minage", edtAgeFrom.text.toString())
                                .putExtra("maxage", edtAgeTo.text.toString())
                                .putExtra("age", edtAge.text.toString())
                                .putExtra("SelectValueactivity", SelectValue)

                            setResult(1212, intent)
                            onBackPressed()
                        }
                        getString(R.string.activity) -> {
                            val intent = Intent().putExtra("name", et_name.text.toString())
                                .putExtra("latitude", latitudee)
                                .putExtra("longitude", longitudee)
                                .putExtra("distance", distance)
                                .putExtra("typeId", typeId)
                                .putExtra("location", et_location.text.toString())
                                .putExtra("minage", edtAgeFrom.text.toString())
                                .putExtra("maxage", edtAgeTo.text.toString())
                                .putExtra("SelectValueactivity", SelectValue)
                                .putExtra("age", edtAge.text.toString())

                            setResult(1213, intent)
                            onBackPressed()
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }

    fun getFilterValues() {
//        if (AllSharedPref != null && !AllSharedPref.equals("")) {
        var ReturnName = AllSharedPref.restoreString(this, "returnName")
        var ReturnLocation = AllSharedPref.restoreString(this, "returnLocation")
        ReturnDistance = AllSharedPref.restoreString(this, "returnDistance")
        var ReturnMinAge = AllSharedPref.restoreString(this, "minage")
        var ReturnMaxAge = AllSharedPref.restoreString(this, "maxage")
        ActivityType = AllSharedPref.restoreString(this, "SelectValueactivity")
        var Age = AllSharedPref.restoreString(this, "Age")

        Log.d(
            "ActivityFilterActivity ",
            "returnValues_if   " + ReturnName + "   " + ReturnDistance + "  " + ReturnMinAge + "  " +
                    ReturnMaxAge + "   " + ActivityType + "  " + ReturnLocation
        )
        et_name.setText(ReturnName, TextView.BufferType.EDITABLE);
        edtAge.setText(Age, TextView.BufferType.EDITABLE);

        if (ReturnLocation != null && !ReturnLocation.equals("")) {
            et_location.setText(ReturnLocation, TextView.BufferType.EDITABLE);
        } else {
            launch(Dispatchers.Main.immediate) {
                et_location.text =
                    dataStoragePreference.emitStoredValue(preferencesKey<String>("cityLogin"))
                        .first()
                latitudee =
                    dataStoragePreference.emitStoredValue(preferencesKey<String>("latitudeLogin"))
                        .first()
                longitudee =
                    dataStoragePreference.emitStoredValue(preferencesKey<String>("longitudeLogin"))
                        .first()
            }
        }
    }
}