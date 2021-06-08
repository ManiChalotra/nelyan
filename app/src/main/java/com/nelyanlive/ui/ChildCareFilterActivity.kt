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
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.homechildcare.HomeChiildCareREsponse
import com.nelyanlive.modals.homechildcare.HomeChildCareeData
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.filter_activity.*
import kotlinx.android.synthetic.main.fragment_child_care.*
import kotlinx.android.synthetic.main.fragment_child_care.et_location
import kotlinx.android.synthetic.main.fragment_child_care.et_name
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.coroutines.CoroutineContext

class ChildCareFilterActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, CoroutineScope, View.OnClickListener {

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
    private val childCareeDatalist by lazy { ArrayList<HomeChildCareeData>() }

    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }

    private var authKey = ""
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

        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        ll_1 = findViewById(R.id.ll_public)
        ll_0 = findViewById(R.id.ll_0)
        ll_1!!.setOnClickListener { dailogLocation() }
        ll_0!!.setOnClickListener {

            launch(Dispatchers.Main.immediate) {
                et_location.text = dataStoragePreference.emitStoredValue(preferencesKey<String>("cityLogin")).first()
                latitudee = dataStoragePreference.emitStoredValue(preferencesKey<String>("latitudeLogin")).first()
                longitudee = dataStoragePreference.emitStoredValue(preferencesKey<String>("longitudeLogin")).first()
            }
        }
        btnSearch = findViewById(R.id.btnSearch)
        childCareTypes = findViewById(R.id.spinner_childcare_type)

        et_location.setOnClickListener(this)
        btnSearch!!.setOnClickListener(this)

        orderby1 = findViewById(R.id.spinner_dayss)
        val km = arrayOf<String?>("", "0KM", "5KM", "10KM", "15KM", "20KM", "25KM", "30KM")
        val adapter1: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.size_customspinner, km)
        orderby1!!.adapter = adapter1
         distance= orderby1!!.selectedItem.toString()

        val childCareTypesList = arrayOf<String?>("", getString(R.string.nursery), getString((R.string.maternal_assistant)), getString(R.string.baby_sitter))
        val adapter0: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.customspinner, childCareTypesList)
        childCareTypes!!.adapter = adapter0

        when {
            childCareTypes!!.selectedItem.toString() == getString(R.string.nursery) -> {
                childcareTypesId= "1"
            }
            childCareTypes!!.selectedItem.toString() == getString(R.string.maternal_assistant) -> {
                childcareTypesId= "2"
            }
            childCareTypes!!.selectedItem.toString() == getString(R.string.baby_sitter) -> {
                childcareTypesId= "3"
            }
        }
        orderby1!!.onItemSelectedListener = this@ChildCareFilterActivity
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onResume() {
        super.onResume()
        checkMvvmResponse() }

    private fun hitFilterActivity_Api() {
        if (checkIfHasNetwork(this@ChildCareFilterActivity)) {
            launch(Dispatchers.Main.immediate) {
                authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
                appViewModel.sendChildCareFilterData(security_key, authKey, latitudee, longitudee, distance!!, et_name.text.toString().trim(),et_location.text.toString().trim(), childcareTypesId)
                childcare_filter_progressbar?.showProgressBar()}
        }
        else {
            showSnackBar(this@ChildCareFilterActivity, getString(R.string.no_internet_error))
        }
    }

    private fun showPlacePicker() {
        // Initialize Places.
        Places.initialize(applicationContext, googleMapKey)
        // Create a new Places client instance.
        val placesClient: PlacesClient = Places.createClient(this@ChildCareFilterActivity)
        // Set the fields to specify which types of place data to return.
        val fields: List<Place.Field> = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
         .build(this@ChildCareFilterActivity)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

    }

    fun dailogLocation() {
        dialog = Dialog(this)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_location)
        dialog!!.setCancelable(true)
        val rlNo: RelativeLayout = dialog!!.findViewById(R.id.rlNo)
        rlNo.setOnClickListener {
            dialog!!.dismiss() }
        val rlYes: RelativeLayout = dialog!!.findViewById(R.id.rlYes)
        rlYes.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()
    }

    private fun checkMvvmResponse() {
        appViewModel.observeFilterChildCareListResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    childcare_filter_progressbar?.hideProgressBar()
                    val jsonObject = JSONObject(response.body().toString())
                    var jsonArray = jsonObject.getJSONArray("data")
                    val homeChildCareResponse = Gson().fromJson<HomeChiildCareREsponse>(response.body().toString(), HomeChiildCareREsponse::class.java)

                    if (childCareeDatalist != null) {
                        childCareeDatalist.clear()
                        childCareeDatalist.addAll(homeChildCareResponse.data)
                        Log.e("chiiii", childCareeDatalist.size.toString())
                    }

                    val returnIntent = Intent()
                    returnIntent.putExtra("filteredChildCareDatalist", childCareeDatalist)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            } else {
                ErrorBodyResponse(response, this, null)
                childcare_filter_progressbar?.hideProgressBar()
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

                   // hitFilterActivity_Api()
                }
            }
        }
    }
}