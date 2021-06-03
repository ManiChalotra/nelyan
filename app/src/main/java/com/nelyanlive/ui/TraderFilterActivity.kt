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
import com.nelyanlive.modals.hometraderpostlist.HomeTraderListData
import com.nelyanlive.modals.hometraderpostlist.HomeTraderPostListResponse
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

class TraderFilterActivity : AppCompatActivity(), View.OnClickListener,AdapterView.OnItemSelectedListener, CoroutineScope
{

    private var latitudee = ""
    private var longitudee = ""
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
                "25KM", "30KM"
        )
        val adapter1: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, R.layout.size_customspinner, km)
        spinner_trader_distance!!.adapter = adapter1
        spinner_trader_distance!!.onItemSelectedListener = this@TraderFilterActivity

        checkMvvmResponse()

    }
    private  fun initalizeClicks(){
        ivBack.setOnClickListener(this)
        ll_public.setOnClickListener(this)
        btnFilter.setOnClickListener(this)
        et_location.setOnClickListener(this)

    }

    private fun hitFilterTrader_Api() {
        if (checkIfHasNetwork(this@TraderFilterActivity)) {
            launch(Dispatchers.Main.immediate) {
                authKey =
                    dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
                appViewModel.sendFilterTraderListData(security_key, authKey, latitudee, longitudee,
                    distance, et_name.text.toString().trim(),
                        et_location.text.toString().trim() )
                trader_filter_progressbar?.showProgressBar()

            }
        } else {
            showSnackBar(this@TraderFilterActivity, getString(R.string.no_internet_error))
        }

    }


    private fun checkMvvmResponse() {
        appViewModel.observeFilterTraderListResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    trader_filter_progressbar?.hideProgressBar()

                    val jsonObject = JSONObject(response.body().toString())
                    var jsonArray = jsonObject.getJSONArray("data")
                    val homeTraderListResponse = Gson().fromJson<HomeTraderPostListResponse>(response.body().toString(), HomeTraderPostListResponse::class.java)

                    if (traderDatalist != null) {
                        traderDatalist.clear()
                        traderDatalist.addAll(homeTraderListResponse.data)
                    }
                    val returnIntent = Intent()
                    returnIntent.putExtra("filteredTraderDatalist", traderDatalist)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            } else {
                ErrorBodyResponse(response, this, null)
                trader_filter_progressbar?.hideProgressBar()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === PLACE_PICKER_REQUEST) {
            if (resultCode === Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                //  city = place.name.toString()
                et_location.text = place.name.toString()

                // cityID = place.id.toString()
                latitudee = place.latLng?.latitude.toString()
                longitudee = place.latLng?.longitude.toString()

                Log.i("dddddd", "Place: " + place.name + ", " + place.id + "," + place.address + "," + place.latLng)
            }

            else if (resultCode === AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("dddddd", status.statusMessage.toString())
            }

            else if (resultCode === Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i("dddddd", "-------Operation is cancelled ")
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
              //  dailogLocation()
            }
            R.id.btnFilter->{
                if (et_location.text.isNullOrEmpty()){
                    myCustomToast(getString(R.string.location_missing_error))
                }else {
                    hitFilterTrader_Api()
                }
            }
            R.id.et_location->{
                showPlacePicker()

            }
        }
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    fun dailogLocation() {
       val  dialog = Dialog(this)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.alert_location)
        dialog.setCancelable(true)
        val rlYes: RelativeLayout
        val rlNo: RelativeLayout
        rlNo = dialog.findViewById(R.id.rlNo)
        rlNo.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            dialog.dismiss()
        }
        rlYes = dialog.findViewById(R.id.rlYes)
        rlYes.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }



}
