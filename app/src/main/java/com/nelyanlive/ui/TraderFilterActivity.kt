package com.nelyanlive.ui

import android.app.Activity
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
import com.nelyanlive.R
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.fragment_trade_filter.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.coroutines.CoroutineContext

class TraderFilterActivity : AppCompatActivity(), View.OnClickListener, CoroutineScope {

    private var latitudee = ""
    private var longitudee = ""
    var SelectValue = ""
    var SelectName = ""
    private var typeId = ""
    var ReturnDistance = ""
    var ReturnName = ""
    var ReturnLocation = ""
    var ActivityType = ""
    var ActivityName = ""
    var poz: Int = 0
    private val job by lazy {
        Job()
    }

    private val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(AppViewModel::class.java)
    }
    var distance: String = ""

    private val dataStoragePreference by lazy { DataStoragePreference(this@TraderFilterActivity) }

    private val googleMapKey = "AIzaSyDQWqIXO-sNuMWupJ7cNNItMhR4WOkzXDE"
    private val PLACE_PICKER_REQUEST = 1

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_trade_filter)
        initalizeClicks()
        getFilterValues()

        Log.d(
            "TraderFilterActivity ",
            "OnCreateTraderFilter   "
        )
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

        val adapter1: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.size_customspinner, km)
        spinner_trader_distance!!.adapter = adapter1

        val arr: MutableList<Array<String?>> = Arrays.asList(kmValue)

        for (i in 0 until arr.get(0).size) {
            Log.e("checkdata", "categoty" + "----" + arr.get(0).get(i).toString())
            if (ReturnDistance.equals(arr.get(0).get(i).toString())) {
                Log.e("checkdata", "categoty_if" + i + arr.get(0).get(i).toString())
                spinner_trader_distance.setSelection(i)

//                if (ReturnName.equals("") || ReturnName == null) {
//
//                } else {
//                    spinner_trader_distance.setSelection(i)
//                }
            }
        }

        spinner_trader_distance!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    distance = if (kmValue[position].isNullOrBlank()|| km[position]=="0KM") {
                        ""
                    } else {
                        kmValue[position]!!
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        checkMvvmResponse()

        if (checkIfHasNetwork(this)) {
            val authkey = AllSharedPref.restoreString(this, "auth_key")
            appViewModel.sendTraderTypeTraderData(security_key, authkey)
        } else {
            showSnackBar(this, getString(R.string.no_internet_error))
        }
    }

    private fun initalizeClicks() {
        ivBack.setOnClickListener(this)
        //ll_public.setOnClickListener(this)
        //set city name
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

        btnFilter.setOnClickListener(this)
        tvFilterclear.setOnClickListener(this)
        et_location.setOnClickListener(this)

    }

    val traderType: MutableList<String?> = mutableListOf()
    val traderID: MutableList<String?> = mutableListOf()

    private fun checkMvvmResponse() {

        appViewModel.observeActivityTypeTraderResponse()!!
            .observe(this, androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    if (response.body() != null) {
                        val jsonObject = JSONObject(response.body().toString())
                        val jsonArray = jsonObject.getJSONArray("data")
                        traderType.add("")
                        traderID.add("")
                        for (i in 0 until jsonArray.length()) {
                            val name = jsonArray.getJSONObject(i).get("name").toString()
                            val id = jsonArray.getJSONObject(i).get("id").toString()
                            Log.e("checkmyactivity", "-ActivityType---" + ActivityType)
                            Log.e("checkmyactivity", "-CCCC_i_169  ---" + i)
                            Log.e("checkmyactivity", "-CCCC_i_id  ---" + id)
                            if (id.equals(ActivityType)) {
                                poz = i + 1
                                Log.e("checkmyactivity", "-CCCCC---" + poz)
                                Log.e("checkmyactivity", "-CCCC_i  ---" + i)
                                Log.e("checkmyactivity", "-CCCC_i_id_175  ---" + i)

                            }

                            traderType.add(name)
                            traderID.add(id)
                        }

                        val arrayAdapter1 = ArrayAdapter(this, R.layout.customspinner, traderType)
                        trader_type.adapter = arrayAdapter1
//                        trader_type.setSelection(poz)
                        if (!ActivityName.equals("")) {
                            trader_type.setSelection(poz)
                        }
                        trader_type.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    SelectName = trader_type.getSelectedItem()
                                        .toString()
                                    typeId = if (position != 0) {
                                        traderID[position]!!

                                    } else {
                                        ""
                                    }

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

    private fun showPlacePicker() {
        // Initialize Places.
        Places.initialize(
            applicationContext,
            googleMapKey
        )
        val fields: List<Place.Field> = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
        )
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(this@TraderFilterActivity)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivBack -> {
                onBackPressed()
            }

            R.id.btnFilter -> {
                if (et_location.text.isNullOrEmpty()) {
                    myCustomToast(getString(R.string.location_missing_error))
                } else {

                    val intent = Intent()
                        .putExtra("name", et_name.text.toString().trim())
                        .putExtra("latitude", latitudee)
                        .putExtra("longitude", longitudee)
                        .putExtra("distance", distance)
                        .putExtra("typeId", typeId)
                        .putExtra("location", et_location.text.toString().trim())
                        .putExtra("SelectValuetrade", SelectValue)
                        .putExtra("SelectNametrade", SelectName)
                        .putExtra("screentrade", "screentrade")
                    setResult(1215, intent)
                    onBackPressed()
                }
            }
            R.id.tvFilterclear -> {
//                val i = Intent(this, TraderListingActivity::class.java)
//                i.putExtra("screentrade", "screentrade")
//                ClearPreference()
////                startActivity(i)
//                finish()

                /**
                 * @author Pardeep Sharma
                 *  to clear all data on clear button
                 */
              ClearPreference()
                val intent = Intent()
                setResult(1, intent)
                onBackPressed()

            }
            R.id.et_location -> {
                showPlacePicker()

            }
        }
    }

    fun getFilterValues() {
        ReturnName = AllSharedPref.restoreString(this, "returnnametrade")
        ReturnLocation = AllSharedPref.restoreString(this, "returnlocationtrade")
        ReturnDistance = AllSharedPref.restoreString(this, "returndistancetrade")
        ActivityType = AllSharedPref.restoreString(this, "SelectValuetrade")
        ActivityName= AllSharedPref.restoreString(this, "SelectNametrade")

        Log.d(
            "TraderFilterActivity ",
            "returnValues_Traderif   " + ReturnName + "   " + ReturnDistance + "  " + ActivityType
        )
        et_name.setText(ReturnName, TextView.BufferType.EDITABLE);

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

    fun ClearPreference() {
        AllSharedPref.clearFilterValue(this, "returnnametrade")
        AllSharedPref.clearFilterValue(this, "returnlocationtrade")
        AllSharedPref.clearFilterValue(this, "returndistancetrade")
        AllSharedPref.clearFilterValue(this, "SelectValuetrade")
        AllSharedPref.clearFilterValue(this, "SelectNametrade")

//        AllSharedPref.save(
//            this,
//            "cleartrade",
//            "cleartrade"
//        )

    }
}
