package com.nelyanlive.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.adapter.EditProductDetailRepeatAdapter
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.myAd.*
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_trader.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

class EditTraderActivity : OpenCameraGallery(), View.OnClickListener, CoroutineScope,
    EditProductDetailRepeatAdapter.ProductRepeatListener {

    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(
        AppViewModel::class.java) }

    private var imageSelectedType = ""

    private val job by lazy { kotlinx.coroutines.Job() }
    private val dataStoragePreference by lazy { DataStoragePreference(this) }
    private lateinit var productDetailRepeatAdapter: EditProductDetailRepeatAdapter
    private var countryCodee = "91"
    private var productPhotoPosition = 0
    private var selectDayGroup: JSONArray = JSONArray()
    private var productDetailsGroup: JSONArray = JSONArray()

    var productErrorNumber = 0
    private var product = false

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
    private lateinit var dayTimeModelArrayList: ArrayList<TraderDaysTimingMyAds>
    private lateinit var productDetailDataModelArrayList: ArrayList<TraderProductMyAds>
    var traderTypeId: String = ""

    private var imagePathList = ArrayList<MultipartBody.Part>()


    var image1: String = ""
    var image2: String = ""
    var image3: String = ""

    private var postID = ""
    private var authKey = ""
    private lateinit var traderimageList: ArrayList<TradersimageMyAds>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trader)
        traderimageList = ArrayList()
        productDetailDataModelArrayList = ArrayList()

        initalizeClicks()

        countycode.setOnCountryChangeListener {
            countryCodee = countycode.selectedCountryCode.toString()
        }

        traderTypeResponse()

    }

    private fun initalizeClicks() {
        ivImg.setOnClickListener(this)
        ivImg1.setOnClickListener(this)
        ivImg2.setOnClickListener(this)
        ivImg3.setOnClickListener(this)
        ivplus.setOnClickListener(this)
        tv_address.setOnClickListener(this)
        btn_trader_submit.setOnClickListener(this)
        ivBack!!.setOnClickListener(this)

        if (intent.extras != null) {
            et_trader_shop_name.setText(intent.getStringExtra("nameofShop").toString())
            et_description_trader.setText(intent.getStringExtra("description"))
            tv_address.text = intent.getStringExtra("address")
            cityName = intent.getStringExtra("city")!!
            cityLatitude = intent.getStringExtra("lati")!!
            cityLongitude = intent.getStringExtra("longi")!!
            et_trader_phone.setText(intent.getStringExtra("phoneNumber"))

            et_trader_email.setText(intent.getStringExtra("email"))
            et_web_address.setText(intent.getStringExtra("website"))

            countryCodee = intent.getStringExtra("countryCode").toString()
            countycode.setCountryForPhoneCode(countryCodee.toInt())

            postID = intent.getStringExtra("adID").toString()

            traderTypeId = intent.getStringExtra("traderTypeId").toString()

            hitTypeTradeActivity_Api()

            dayTimeModelArrayList = intent.getSerializableExtra("daytimeList") as ArrayList<TraderDaysTimingMyAds>
            productDetailDataModelArrayList = intent.getSerializableExtra("traderProductList") as ArrayList<TraderProductMyAds>
            productDetailDataModelArrayList = intent.getSerializableExtra("traderProductList") as ArrayList<TraderProductMyAds>
            makeJsonArray()
            if(productDetailDataModelArrayList.isEmpty()) productDetailDataModelArrayList.add(TraderProductMyAds("",0,"","","",0))

            productDetailRepeatAdapter = EditProductDetailRepeatAdapter(this, productDetailDataModelArrayList, this)
            rv_product_details!!.layoutManager = LinearLayoutManager(this)
            rv_product_details!!.adapter = productDetailRepeatAdapter


            traderimageList = intent.getSerializableExtra("traderImageList")as ArrayList<TradersimageMyAds>

            if (!traderimageList.isNullOrEmpty()) {
                when (traderimageList.size) {
                    1 -> {
                        image1 = traderimageList[0].images
                        Glide.with(this).load(image_base_URl + traderimageList[0].images).error(R.mipmap.no_image_placeholder).into(ivImg1)

                    }
                    2 -> {
                        image1 = traderimageList[0].images
                        image2 = traderimageList[1].images
                        Glide.with(this).load(image_base_URl + traderimageList[0].images).error(R.mipmap.no_image_placeholder).into(ivImg1)
                        Glide.with(this).load(image_base_URl + traderimageList[1].images).error(R.mipmap.no_image_placeholder).into(ivImg2)

                    }
                    3 -> {
                        image1 = traderimageList[0].images
                        image2 = traderimageList[1].images
                        image3 = traderimageList[2].images
                        Glide.with(this).load(image_base_URl + traderimageList[0].images).error(R.mipmap.no_image_placeholder).into(ivImg1)
                        Glide.with(this).load(image_base_URl + traderimageList[1].images).error(R.mipmap.no_image_placeholder).into(ivImg2)
                        Glide.with(this).load(image_base_URl + traderimageList[2].images).error(R.mipmap.no_image_placeholder).into(ivImg)

                    }
                }
            }
        }
        launch(Dispatchers.Main.immediate) {
            authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
        }
    }

    private fun showPlacePicker() {
        Places.initialize(applicationContext, googleMapKey)
        val fields: List<Place.Field> = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)

                    cityName = place.name.toString()
                    tv_address.text = cityName

                    cityLatitude = place.latLng?.latitude.toString()
                    cityLongitude = place.latLng?.longitude.toString()
                }
            } 
        }
    }

    private fun makeJsonArray() {
        for (i in 0 until dayTimeModelArrayList.size) {
            val json = JSONObject()
            json.put("day_name", dayTimeModelArrayList[i].day)
            json.put("time_from", dayTimeModelArrayList[i].startTime)
            json.put("time_to", dayTimeModelArrayList[i].endTime)
            json.put("secondStartTime", dayTimeModelArrayList[i].secondStartTime)
            json.put("secondEndTime", dayTimeModelArrayList[i].secondEndTime)
            selectDayGroup.put(json)
        }

    }

    private fun makeProductJsonArray() {
        for (i in 0 until productDetailDataModelArrayList.size) {
            val json = JSONObject()
            json.put("image", productDetailDataModelArrayList[i].image)
            json.put("title", productDetailDataModelArrayList[i].title)
            json.put("price", productDetailDataModelArrayList[i].price)
            json.put("description", productDetailDataModelArrayList[i].description)
            productDetailsGroup.put(json)
        }

    }


    private fun hitTypeTradeActivity_Api() {
        if (checkIfHasNetwork(this@EditTraderActivity)) {
            val authkey = AllSharedPref.restoreString(this, "auth_key")
            appViewModel.sendTraderTypeTraderData(security_key, authkey)
        } else {
            showSnackBar(this@EditTraderActivity, getString(R.string.no_internet_error))
        }
    }

    private fun hitFinalTraderPostApi() {


        if (checkIfHasNetwork(this@EditTraderActivity)) {
            val authkey = AllSharedPref.restoreString(this, "auth_key")

            if(product)
            {appViewModel.send_editPostTraderData(security_key, authkey, "3", traderTypeId, et_trader_shop_name.text.toString().trim(),
                et_description_trader.text.toString().trim(), countryCodee, et_trader_phone.text.toString().trim(), tv_address.text.toString().trim(),
                cityName, cityLatitude, cityLongitude, et_trader_email.text.toString(), et_web_address.text.toString(), selectDayGroup.toString(),
                productDetailsGroup.toString(), image1, image2, image3, postID,"1")}
            else
            {appViewModel.send_editPostTraderData(security_key, authkey, "3", traderTypeId, et_trader_shop_name.text.toString().trim(),
                et_description_trader.text.toString().trim(), countryCodee, et_trader_phone.text.toString().trim(), tv_address.text.toString().trim(),
                cityName, cityLatitude, cityLongitude, et_trader_email.text.toString(), et_web_address.text.toString(), selectDayGroup.toString(),
                productDetailsGroup.toString(), image1, image2, image3, postID,"2")}


        } else {
            showSnackBar(this@EditTraderActivity, getString(R.string.no_internet_error))

        }
    }

    private val typeList: ArrayList<String> = ArrayList()
    private val typeListId: ArrayList<String> = ArrayList()
    private var selectedPosition = 0

    private fun traderTypeResponse() {
        appViewModel.observeActivityTypeTraderResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {

                if (response.body() != null) {
                    val jsonObject = JSONObject(response.body().toString())
                    val mSizeOfData = jsonObject.getJSONArray("data").length()


                    typeList.clear()
                    typeListId.clear()
                    typeList.add("")
                    typeListId.add("")
                    for (i in 0 until mSizeOfData) {
                        val name = jsonObject.getJSONArray("data").getJSONObject(i).get("name").toString()
                        val id = jsonObject.getJSONArray("data").getJSONObject(i).get("id").toString()
                        typeList.add(name)
                        typeListId.add(id)
                        if(id==traderTypeId)
                        {
                            selectedPosition = i
                        }
                    }
                    val arrayAdapte1 = ArrayAdapter(this, R.layout.customspinner, typeList)
                    trader_type.adapter = arrayAdapte1
                    trader_type.setSelection(selectedPosition+1)

                    trader_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            traderTypeId = typeListId[position]
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }

                       }
            } else {
                ErrorBodyResponse(response, this, null)
            }
        })

        appViewModel.observeEditPostTraderResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("addPostTraderResopnse", "-----------" + Gson().toJson(response.body()))
                if (response.body() != null) {
                    response.body()
                    progressDialog.hidedialog()
                    finishAffinity()
                    myCustomToast("Post Updated Successfully")
                    OpenActivity(HomeActivity::class.java)

                }
            } else {
                progressDialog.hidedialog()
                ErrorBodyResponse(response, this, null)
            }
        })

        appViewModel.observeUploadImageResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
           if (response!!.isSuccessful && response.code() == 200) {
                            progressDialog.hidedialog()

                            Log.d("urlDataLoading", "------------" + Gson().toJson(response.body()))
                            if (response.body() != null) {
                                if (response.body()!!.data != null) {
                                    val image = response.body()!!.data!![0].image.toString()

                                    when (imageSelectedType) {

                                        "1" -> {
                                            image1 = image
                                            Glide.with(this).asBitmap().load(image_base_URl + image)
                                                .into(ivImg1!!)

                                        }
                                        "2" -> {
                                           image2 = image
                                            Glide.with(this).asBitmap().load(image_base_URl + image)
                                               .into(ivImg2!!)
                                       }
                                        "3" -> {
                                            image3 = image

                                          Glide.with(this).asBitmap().load(image_base_URl + image)
                                               .into(ivImg!!)
                                       }
                                       "4" -> {
                                           productDetailDataModelArrayList[productPhotoPosition].image = image
                                           productDetailRepeatAdapter.notifyItemChanged(productPhotoPosition)
                                        }
                                    }
                                    imageSelectedType = ""

                                }

                            }
                       }
                            else {

                               ErrorBodyResponse(response, this, null)
                            }
        })

        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
        })

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.ivImg1 -> {

                imageSelectedType = "1"
                checkPermission(this)
            }
            R.id.ivImg2 -> {

                imageSelectedType = "2"
                checkPermission(this)
            }
            R.id.ivImg -> {

                imageSelectedType = "3"
                checkPermission(this)
            }
            R.id.tv_address -> {
                showPlacePicker()
            }
            R.id.btn_trader_submit -> {

                    if (traderTypeId == "") {
                        myCustomToast(getString(R.string.trader_type_missing_error))
                    }
                    else {
                        if (et_trader_shop_name.text.toString().isEmpty()) {
                            myCustomToast(getString(R.string.shop_name_missing_error))
                        } else {
                            if (et_description_trader.text.toString().isEmpty()) {
                                myCustomToast(getString(R.string.description_missing))
                            } else {
                                if (tv_address.text.toString().isEmpty()) {
                                    myCustomToast(getString(R.string.address_missing_error))
                                } else {
                                    if (et_trader_phone.text.toString().isEmpty()) {
                                        myCustomToast(getString(R.string.phone_number_missing))
                                    } else {
                                        if (et_trader_email.text.toString().isEmpty()) {
                                            myCustomToast(getString(R.string.email_address_missing))
                                        } else {

                                            var productErrorString = ""
                                            productErrorNumber = 0
                                            productErrorString =  getProductError(productDetailDataModelArrayList[productDetailDataModelArrayList.size-1].image,productErrorString,"please select image in previous data",1)
                                            productErrorString =  getProductError(productDetailDataModelArrayList[productDetailDataModelArrayList.size-1].title,productErrorString,"please fill title in previous data",2)
                                            productErrorString =  getProductError(productDetailDataModelArrayList[productDetailDataModelArrayList.size-1].price,productErrorString,"please fill price in previous data",3)
                                            productErrorString =  getProductError(productDetailDataModelArrayList[productDetailDataModelArrayList.size-1].description,productErrorString,"please fill description in previous data",4)

                                            if(productErrorNumber!=0 && productErrorString.isNotEmpty())
                                            {
                                                myCustomToast(productErrorString)
                                            }
                                            else
                                            {
                                                if(product) {
                                                    productDetailsGroup = JSONArray()
                                                    for (i in 0 until productDetailDataModelArrayList.size) {
                                                        val json = JSONObject()
                                                        json.put("image", productDetailDataModelArrayList[i].image)
                                                        json.put("title", productDetailDataModelArrayList[i].title)
                                                        json.put("price", productDetailDataModelArrayList[i].price)
                                                        json.put("description", productDetailDataModelArrayList[i].description)
                                                        productDetailsGroup.put(json)
                                                    }
                                                }
                                                hitFinalTraderPostApi()
                                            }


                                        }
                                    }
                                }

                            }
                        }
                    }

            }
        }
    }

    private fun getProductError(productFrom: String?, productErrorString: String, s: String,i:Int) : String {

        return when {
            productFrom.isNullOrBlank() -> when {
                productErrorString.isBlank() -> {
                    s
                }
                else -> productErrorString
            }
            else -> {
                product = true
                productErrorNumber =i
                productErrorString
            }
        }
    }


    override fun getRealImagePath(imgPath: String?) {

        Log.d("selectedImagePath", "-------$imgPath")
        uploadImageServer(imgPath)
    }

    private fun uploadImageServer(imgPath: String?) {

        val mFile: File?
        mFile = File(imgPath!!)
        val imageFile: RequestBody = mFile.asRequestBody("image/*".toMediaTypeOrNull())
        val photo: MultipartBody.Part?
        photo = MultipartBody.Part.createFormData("image", mFile.name, imageFile)
        imagePathList.add(0,photo)
        val type: RequestBody = "image".toRequestBody("text/plain".toMediaTypeOrNull())

        val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
        if (checkIfHasNetwork(this)) {
            appViewModel.sendUploadImageData(type, users, imagePathList)
            progressDialog.setProgressDialog()
        }
        else {
            showSnackBar(this, getString(R.string.no_internet_error))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }



    override fun onITEEMClick(list: ArrayList<TraderProductMyAds>, pos: Int) {
        when {
            productDetailDataModelArrayList[list.size-1].image.isEmpty() -> { myCustomToast("please select image in previous data") }
            productDetailDataModelArrayList[list.size-1].title.isEmpty() -> { myCustomToast("please fill Title in previous data")}
            productDetailDataModelArrayList[list.size-1].price.isEmpty() -> { myCustomToast("please fill price in previous data")}
            productDetailDataModelArrayList[list.size-1].description.isEmpty() -> { myCustomToast("please fill description in previous data")}
            else -> {  productDetailDataModelArrayList.add(TraderProductMyAds("", 0, "", "", "", 0))
                productDetailRepeatAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun addCameraGalleryImage(list: java.util.ArrayList<TraderProductMyAds>, pos: Int) {
        productPhotoPosition = pos
        imageSelectedType = "4"
        checkPermission(this)
    }


}
