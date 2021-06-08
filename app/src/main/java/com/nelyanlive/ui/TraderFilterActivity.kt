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
import com.nelyanlive.data.network.responsemodels.trader_type.TraderTypeResponse
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.hometraderpostlist.HomeTraderListData
import com.nelyanlive.modals.hometraderpostlist.HomeTraderPostListResponse
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_trader.*
import kotlinx.android.synthetic.main.fragment_child_care.*
import kotlinx.android.synthetic.main.fragment_trade_filter.*
import kotlinx.android.synthetic.main.fragment_trade_filter.et_location
import kotlinx.android.synthetic.main.fragment_trade_filter.et_name
import kotlinx.android.synthetic.main.fragment_trade_filter.ivBack
import kotlinx.android.synthetic.main.fragment_trade_filter.ll_public
import kotlinx.android.synthetic.main.fragment_trade_filter.trader_type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.coroutines.CoroutineContext

class TraderFilterActivity : AppCompatActivity(), View.OnClickListener,AdapterView.OnItemSelectedListener, CoroutineScope
{

    private var latitudee = ""
    private var longitudee = ""
    private var typeId = ""
    private val job by lazy {
        Job()
    }
    private val traderDatalist by lazy { ArrayList<HomeTraderListData>() }

    private val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java)
    }
    private var authKey = ""
    var distance: String= ""

    private val dataStoragePreference by lazy { DataStoragePreference(this@TraderFilterActivity) }


    private val googleMapKey = "AIzaSyDQWqIXO-sNuMWupJ7cNNItMhR4WOkzXDE"
    private val PLACE_PICKER_REQUEST = 1

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_trade_filter)
        initalizeClicks()

        val genderlist = arrayOf<String?>("", "Sport", "Cultural", "Languages")
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, R.layout.customspinner, genderlist)
        trader_type!!.adapter = adapter
        trader_type!!.onItemSelectedListener = this@TraderFilterActivity
        val km = arrayOf<String?>(
                "", "0KM", "5KM", "10KM", "15KM", "20KM",
                "25KM", "30KM")
        val adapter1: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, R.layout.size_customspinner, km)
        spinner_trader_distance!!.adapter = adapter1
        spinner_trader_distance!!.onItemSelectedListener = this@TraderFilterActivity
        checkMvvmResponse()

        if (checkIfHasNetwork(this)) {
            val authkey = AllSharedPref.restoreString(this, "auth_key")
            appViewModel.sendTraderTypeTraderData(security_key, authkey)
        }
        else
        {
            showSnackBar(this, getString(R.string.no_internet_error))
        }

    }
    private  fun initalizeClicks(){
        ivBack.setOnClickListener(this)
        ll_public.setOnClickListener(this)
        btnFilter.setOnClickListener(this)
        et_location.setOnClickListener(this)

    }


    val traderType: MutableList<String?> = mutableListOf()
    val traderID: MutableList<String?> = mutableListOf()

    private fun checkMvvmResponse() {

        appViewModel.observeActivityTypeTraderResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    val jsonArray = jsonObject.getJSONArray("data")
                    traderType.add("")
                    for (i in 0 until jsonArray.length()) {
                        val name = jsonArray.getJSONObject(i).get("name").toString()
                        val id = jsonArray.getJSONObject(i).get("id").toString()
                        traderType.add(name)
                        traderID.add(id)
                    }

                    val arrayAdapter1 = ArrayAdapter(this, R.layout.customspinner, traderType)
                    trader_type.adapter = arrayAdapter1

                    trader_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            typeId = if(position!=0) {
                                traderID[position]!!
                            } else {
                                ""
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            //TODO("Not yet implemented")
                        }
                    }

                } }
            else
            {
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

                Log.i("dddddd", "Place: " + place.name + ", " + place.id + "," + place.address + "," + place.latLng)
            }

            else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
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
        // Create a new Places client instance.
        val placesClient: PlacesClient = Places.createClient(this@TraderFilterActivity)
        // Set the fields to specify which types of place data to return.
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
        when(v!!.id){
            R.id.ivBack->{
                onBackPressed()
            }
            R.id.ll_public->{
                launch(Dispatchers.Main.immediate) {
                    et_location.text = dataStoragePreference.emitStoredValue(preferencesKey<String>("cityLogin")).first()
                    latitudee = dataStoragePreference.emitStoredValue(preferencesKey<String>("latitudeLogin")).first()
                    longitudee = dataStoragePreference.emitStoredValue(preferencesKey<String>("longitudeLogin")).first()
                }
            }
            R.id.btnFilter->{
                if (et_location.text.isNullOrEmpty()){
                    myCustomToast(getString(R.string.location_missing_error))
                }
                else {

                    val intent = Intent()
                        .putExtra("name",et_name.text.toString().trim())
                        .putExtra("latitude",latitudee)
                        .putExtra("longitude",longitudee)
                        .putExtra("distance",distance)
                        .putExtra("typeId",typeId)
                        .putExtra("location",et_location.text.toString().trim())

                    setResult(1215,intent)
                    onBackPressed()

                   // hitFilterTrader_Api()
                }
            }
            R.id.et_location->{
                showPlacePicker()

            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

}
