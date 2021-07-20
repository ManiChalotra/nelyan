package com.nelyanlive.ui


import android.app.Activity
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
import com.nelyanlive.R
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.fragment_child_care.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.coroutines.CoroutineContext

class ChildCareFilterActivity : AppCompatActivity(),  CoroutineScope, View.OnClickListener {

    var btnSearch: Button? = null
    var dialog: Dialog? = null
    var ivBack: ImageView? = null
    var ll_1: LinearLayout? = null
    var ll_0: LinearLayout? = null
    var activityTypes: Spinner? = null
    var orderby1: Spinner? = null
    var childCareTypes: Spinner? = null
    var distance: String? = null
    var childcareTypesId: String= ""
    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var latitudee = ""
    private var longitudee = ""
    private val job by lazy {
        Job()
    }

    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }

    private val dataStoragePreference by lazy { DataStoragePreference(this@ChildCareFilterActivity) }

    private val googleMapKey = "AIzaSyDQWqIXO-sNuMWupJ7cNNItMhR4WOkzXDE"
    private val PLACE_PICKER_REQUEST = 1

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_child_care)

        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener {
            onBackPressed()
        }

        if (checkIfHasNetwork(this)) {
            launch(Dispatchers.Main.immediate) {
              val authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
                appViewModel.sendChildCareTypeData(security_key, authKey)
            }
        } else {
            showSnackBar(this, getString(R.string.no_internet_error))

        }

        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]

        launch(Dispatchers.Main.immediate) {
            et_location.text = dataStoragePreference.emitStoredValue(preferencesKey<String>("cityLogin")).first()
            latitudee = dataStoragePreference.emitStoredValue(preferencesKey<String>("latitudeLogin")).first()
            longitudee = dataStoragePreference.emitStoredValue(preferencesKey<String>("longitudeLogin")).first()
        }



        btnSearch = findViewById(R.id.btnSearch)
        childCareTypes = findViewById(R.id.childType)

        et_location.setOnClickListener(this)
        btnSearch!!.setOnClickListener(this)

        orderby1 = findViewById(R.id.spinner_dayss)
        val km = arrayOf<String?>("", "0KM", "5KM", "10KM", "15KM", "20KM", "25KM", "30KM", "35KM", "40KM", "45KM", "50KM")
        val kmValue = arrayOf<String?>("", "0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50")

        val adapter1: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.size_customspinner, km)
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
        checkMvvmResponse()
    }


    private fun showPlacePicker() {
        Places.initialize(applicationContext, googleMapKey)
        val fields: List<Place.Field> = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
         .build(this@ChildCareFilterActivity)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

    }




    val childCareType: ArrayList<String?> = ArrayList()
    val childCareTypeId: ArrayList<String?> = ArrayList()

    private fun checkMvvmResponse() {
        appViewModel.observeChildCareTypeResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    val jsonArray = jsonObject.getJSONArray("data")
                    childCareType.clear()
                    childCareTypeId.clear()
                    childCareType.add("")
                    childCareTypeId.add("")
                    for (i in 0 until jsonArray.length()) {
                        val name = jsonArray.getJSONObject(i).get("categoryName").toString()
                        val id = jsonArray.getJSONObject(i).get("id").toString()
                        childCareType.add(name)
                        childCareTypeId.add(id)
                    }
                    val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<String>(this, R.layout.customspinner, childCareType )
                    childCareTypes!!.adapter = arrayAdapter
                    childCareTypes!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            childcareTypesId = if(position!=0) {
                                childCareTypeId[position]!!
                            } else {
                                ""
                            }
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
                et_location.text = place.name.toString()
                latitudee = place.latLng?.latitude.toString()
                longitudee = place.latLng?.longitude.toString()
                Log.i("dddddd", "Place: " + place.name + ", " + place.id + "," + place.address + "," + place.latLng)
            }

            else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("dddddd", status.statusMessage.toString())
            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.et_location ->{
                showPlacePicker()
            }
            R.id.btnSearch ->{
                if (et_location.text.isNullOrEmpty()){
                  myCustomToast(getString(R.string.location_missing_error))
                }
                else {

                    val intent = Intent().putExtra("name",et_name.text.toString().trim())
                        .putExtra("latitude",latitudee)
                        .putExtra("longitude",longitudee)
                        .putExtra("distance",distance)
                        .putExtra("location",et_location.text.toString().trim())
                        .putExtra("childCareType",childcareTypesId)

                    setResult(1214,intent)
                    onBackPressed()

                }
            }
        }
    }
}