package com.nelyan_live.ui

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
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.nelyan_live.R
import com.nelyan_live.db.DataStoragePreference
import com.nelyan_live.modals.homeactivitylist.HomeAcitivityResponseData
import com.nelyan_live.modals.homeactivitylist.HomeActivityResponse
import com.nelyan_live.modals.homechildcare.HomeChiildCareREsponse
import com.nelyan_live.modals.homechildcare.HomeChildCareeData
import com.nelyan_live.utils.*
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
    var tvCal: TextView? = null
    var dialog: Dialog? = null
    var ivBack: ImageView? = null
    var ll_1: LinearLayout? = null
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

    private val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java)
    }
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

        ivBack!!.setOnClickListener(View.OnClickListener { // getActivity().onBackPressed();
            onBackPressed()
        })

        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
       /* tvCal = findViewById(R.id.tv_cal)
        tvCal!!.setOnClickListener(this)
       */ ll_1 = findViewById(R.id.ll_public)
        ll_1!!.setOnClickListener(View.OnClickListener { dailogLocation() })
        btnSearch = findViewById(R.id.btnSearch)
        childCareTypes = findViewById(R.id.spinner_childcare_type)

        et_location.setOnClickListener(this)
        btnSearch!!.setOnClickListener(this)


        /*val genderlist = arrayOf<String?>(
                "",
                "Sport", "Cultural", "Languages")
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, R.layout.customspinner, genderlist)
        activityTypes!!.setAdapter(adapter)
        activityTypes!!.setOnItemSelectedListener(this@FilterActivity)
      */
        orderby1 = findViewById(R.id.spinner_dayss)
        val km = arrayOf<String?>(
                "", "0KM",
                "5KM", "10KM", "15KM", "20KM", "25KM", "30KM"
        )
        val adapter1: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, R.layout.size_customspinner, km)
        orderby1!!.setAdapter(adapter1)
         distance= orderby1!!.selectedItem.toString()

        val childCareTypesList = arrayOf<String?>(
                "", getString(R.string.nursery),
                getString((R.string.maternal_assistant)), getString(R.string.baby_sitter))

        val adapter0: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, R.layout.customspinner, childCareTypesList)
        childCareTypes!!.setAdapter(adapter0)

        if (childCareTypes!!.selectedItem.toString().equals(getString(R.string.nursery))){
            childcareTypesId= "1"
        } else if (childCareTypes!!.selectedItem.toString().equals(getString(R.string.maternal_assistant))){
            childcareTypesId= "2"
        } else if (childCareTypes!!.selectedItem.toString().equals(getString(R.string.baby_sitter))){
            childcareTypesId= "3"
        }



        /*try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            ListPopupWindow popupWindow = (ListPopupWindow) popup.get(orderby1);
            popupWindow.setHeight(50);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            Log.e("sjhj","jsbhj"+e);
        }*/orderby1!!.setOnItemSelectedListener(this@ChildCareFilterActivity)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onResume() {
        super.onResume()
        checkMvvmResponse()
    }


    private fun hitFilterActivity_Api() {
        if (checkIfHasNetwork(this@ChildCareFilterActivity)) {
            launch(Dispatchers.Main.immediate) {
                authKey = dataStoragePreference?.emitStoredValue(preferencesKey<String>("auth_key"))?.first()
                appViewModel.sendChildCareFilterData(security_key, authKey, latitudee, longitudee, distance!!, et_name.text.toString().trim(),
                        et_location.text.toString().trim(), childcareTypesId!! )
                childcare_filter_progressbar?.showProgressBar()  }
        } else {
            showSnackBar(this@ChildCareFilterActivity, getString(R.string.no_internet_error))
        }

    }


/*
    private fun dateDialog() {
        val listener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> tvCal!!.text = dayOfMonth.toString() +
                "/" + (monthOfYear + 1) + "/" + year }
        val datePickerDialog = DatePickerDialog(this, R.style.datepicker, listener, mYear, mMonth, mDay)
        datePickerDialog.show()
    }
*/

    private fun showPlacePicker() {
        // Initialize Places.
        Places.initialize(
                applicationContext,
                googleMapKey
        )
        // Create a new Places client instance.
        val placesClient: PlacesClient = Places.createClient(this@ChildCareFilterActivity)
        // Set the fields to specify which types of place data to return.
        val fields: List<Place.Field> = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
        )
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this@ChildCareFilterActivity)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

    }


    fun dailogLocation() {
        dialog = Dialog(this)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_location)
        dialog!!.setCancelable(true)
        val rlYes: RelativeLayout
        val rlNo: RelativeLayout
        rlNo = dialog!!.findViewById(R.id.rlNo)
        rlNo.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            dialog!!.dismiss()
        }
        rlYes = dialog!!.findViewById(R.id.rlYes)
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
                        childCareeDatalist!!.clear()
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

/*
    override fun onPermissionGranted() {

    }

    override fun onLocationGet(latitude: String?, longitude: String?) {
        latitudee=latitude!!
        longitudee=longitude!!
    }
*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === PLACE_PICKER_REQUEST) {
            if (resultCode === Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
              //  city = place.name.toString()
                et_location.setText(place.name.toString())

                // cityID = place.id.toString()
                latitudee = place.latLng?.latitude.toString()
                longitudee = place.latLng?.longitude.toString()

                Log.i("dddddd", "Place: " + place.name + ", " + place.id + "," + place.address + "," + place.latLng)
            }

            else if (resultCode === AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
                Log.i("dddddd", status.getStatusMessage().toString())
            }

            else if (resultCode === Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i("dddddd", "-------Operation is cancelled ")
            }
        }
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.et_location ->{
                showPlacePicker()
            }
/*
            R.id.tv_cal ->{
                dateDialog()
            }
*/
            R.id.btnSearch ->{
                if (et_location.text.isNullOrEmpty()){
                  myCustomToast(getString(R.string.location_missing_error))
                }else {
                    hitFilterActivity_Api()
                }
                /*  val intent = Intent()
                  intent.putExtra("name", et_name.text.toString().trim())
                  intent.putExtra("distance", orderby1!!.selectedItem.toString().trim())
                  intent.putExtra("location", et_location.text.toString().trim())
                  intent.putExtra("date", tv_cal.text.toString().trim())
                  intent.putExtra("lat", latitude)
                  intent.putExtra("lng", longitude)
                  setResult(Activity.RESULT_OK, intent)
                  finish()
  */
            }
        }
    }
}