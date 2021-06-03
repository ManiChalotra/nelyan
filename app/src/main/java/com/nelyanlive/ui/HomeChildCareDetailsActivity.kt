package com.nelyanlive.ui


import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.adapter.ChildCareDetailsImageAdapter
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.childcarePostdetails.ChildCareImage
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_home_child_care_details.*
import kotlinx.android.synthetic.main.alert_share.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.util.*
import kotlin.coroutines.CoroutineContext

class HomeChildCareDetailsActivity : AppCompatActivity(), View.OnClickListener, CoroutineScope, OnMapReadyCallback {

    var rc: RecyclerView? = null
    var rvChildcareImages: RecyclerView? = null
    var iv_msg: ImageView? = null
    var ivBack: ImageView? = null
    var ivShare: ImageView? = null
    var btnModify: Button? = null
    var btnPublish: Button? = null
    var dialog: Dialog? = null
    var mMap: GoogleMap? = null
    var tvTitle: TextView? = null
    var indicator: ScrollingPagerIndicator? = null
/*
    var datalist = ArrayList<DetailsImageModal>()
*/
    var categoryId=""
    var postId=""

    private val job by lazy {
        Job()
    }
    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    private val dataStoragePreference by lazy { DataStoragePreference(this@HomeChildCareDetailsActivity) }
    private var authkey: String? = null

    private var shareDialog: ShareDialog? = null


    var latitude = ""
    var longitude = ""
    private var listChildCareimage = ArrayList<ChildCareImage>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_child_care_details)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


        ivBack = findViewById(R.id.ivBack)
        tvTitle = findViewById(R.id.tvTitle)
        btnModify = findViewById(R.id.btnModify)
        btnPublish = findViewById(R.id.btnPublish)
        ivShare = findViewById(R.id.ivShare)
        rvChildcareImages = findViewById(R.id.rv_childcare_images)

        ivBack!!.setOnClickListener(this)
        btnModify!!.setOnClickListener(this)
        btnPublish!!.setOnClickListener(this)
        ivShare!!.setOnClickListener(this)
        indicator = findViewById(R.id.child_care_indicator)


        if (intent.extras != null) {
            categoryId = intent.getStringExtra("categoryId").toString()
            postId = intent.getStringExtra("activityId")!!
        }


        launch(Dispatchers.Main.immediate) {
            authkey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                .first()
            appViewModel.postDetailsApiData(security_key, authkey, "2", postId, "2")
            childcare_details_progressbar?.showProgressBar()

        }

        checkMvvmResponse()


        /* val lm = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
         rc!!.setLayoutManager(lm)
         datalist.add(DetailsImageModal(R.drawable.img_1))
         datalist.add(DetailsImageModal(R.drawable.img_4))
         datalist.add(DetailsImageModal(R.drawable.img_1))
         datalist.add(DetailsImageModal(R.drawable.img_4))
         datalist.add(DetailsImageModal(R.drawable.img_1))
 */


        /*val ad = DetailsImageAdapter(this, datalist)
        rc!!.setAdapter(ad)
        indicator!!.attachToRecyclerView(rc!!)
*/

    }


    fun dailogshare() {
        dialog = Dialog(this)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_share)
        dialog!!.setCancelable(true)
        val ll_1: LinearLayout
        ll_1 = dialog!!.findViewById(R.id.ll_public)

        dialog!!.ll_twitter_share.setOnClickListener {
             CommonMethodsKotlin.twitterShare(this)
            dialog!!.dismiss()
        }
        dialog!!.ll_instra_share.setOnClickListener {
            CommonMethodsKotlin.instaShare(this)
            dialog!!.dismiss()
        }
        dialog!!.ll_messanger_share.setOnClickListener {
            CommonMethodsKotlin.messageShare(this)
            dialog!!.dismiss()
        }
        dialog!!.ll_gmail_share.setOnClickListener {
            val email = Intent(Intent.ACTION_SEND)
            //  email.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>("riteshsaini22@hotmail.com"))
            email.putExtra(Intent.EXTRA_SUBJECT, "Nelyan App")
            email.putExtra(Intent.EXTRA_TEXT, "Nelyan.. social app. \n"+
                    "https://play.google.com/store/apps/details?id=com.nelyan")
            email.setPackage("com.google.android.gm")
            email.type = "message/rfc822"
            try {
                startActivity(email)
            } catch (ex: ActivityNotFoundException) {
                myCustomToast( getString(R.string.gmail_not_error))
            }

            dialog!!.dismiss()
        }
        dialog!!.ll_whatsapp_share.setOnClickListener {
        CommonMethodsKotlin.whatsAppShare(this)

            dialog!!.dismiss()
        }
        dialog!!.ll_fv_share.setOnClickListener {
        CommonMethodsKotlin.fbShare(this)

            var content =  ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.nelyan"))
                    .setShareHashtag( ShareHashtag.Builder().setHashtag("Nelyan App").build())
                    .build()

            try {
                shareDialog!!.show(content) // Show ShareDialog

            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, "Facebook have not been installed.", Toast.LENGTH_SHORT).show()
            }

/*
            var content =  ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.nelyan"))
                    .setShareHashtag( ShareHashtag.Builder().setHashtag("Nelyan.. social app.  ").build())
                    .build()

            try {
                shareDialog!!.show(content) // Show ShareDialog

            } catch (ex: ActivityNotFoundException) {
                myCustomToast( getString(R.string.facebook_not_error))
            }
*/
            dialog!!.dismiss()
        }
        dialog!!.show()
    }

    private fun checkMvvmResponse() {

        appViewModel.observePostDetailApiResponse()!!.observe(this, androidx.lifecycle.Observer { response ->

            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {

                    childcare_details_progressbar?.hideProgressBar()
                    Log.d("postDetailsResponse", "-------------" + Gson().toJson(response.body()))

                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)

                    val message = jsonObject.get("msg").toString()
                    myCustomToast(message)

                    tv_child_care_name.text = jsonObject.getJSONObject("data").get("name").toString()
                    tv_child_care_description.text = jsonObject.getJSONObject("data").get("description").toString()
                    tv_available_place.text = jsonObject.getJSONObject("data").get("availableplace").toString()
                    tv_childcare_phone.text = jsonObject.getJSONObject("data").get("countryCode").toString() + "-" +
                            jsonObject.getJSONObject("data").get("phone").toString()
                    tv_child_care_address.text = jsonObject.getJSONObject("data").get("city").toString()

                    var childcareTypeId= jsonObject.getJSONObject("data").get("ChildcareType").toString()


                    longitude = jsonObject.getJSONObject("data").get("longitude").toString()
                    latitude = jsonObject.getJSONObject("data").get("latitude").toString()

/*
                    if (childcareTypeId.equals("1")) {
                        tv_trader_type.text = this.getString(R.string.doctor)
                    } else if (childcareTypeId.equals("2")) {
                        tv_trader_type.text = this.getString(R.string.food_court)
                    } else if (childcareTypeId.equals("3")) {
                        tv_trader_type.text = this.getString(R.string.plant_nursury)
                    }
*/

                    val listArray: JSONArray = jsonObject.getJSONObject("data").getJSONArray("ChildCareImages")
                    val mSizeOfData: Int = listArray.length()


/*Set Trader Images List in adapter*/


                    if (listChildCareimage != null) {
                        listChildCareimage.clear()
                    } else {
                        listChildCareimage = ArrayList()
                    }
                    for (i in 0 until mSizeOfData) {
                        val model = listArray.getJSONObject(i)
                        val createdAt = model.get("createdAt")
                        val updatedAt = model.get("updatedAt")
                        val postId = model.get("postId")
                        val images = model.get("image")
                        val id = model.get("id")

                        listChildCareimage.add(ChildCareImage(createdAt.toString(), id.toString().toInt(), images.toString(),
                                postId.toString().toInt(), updatedAt.toString()))
                    }

                    if (mSizeOfData != 0) {
                        val childCareDetailsImageAdapter = ChildCareDetailsImageAdapter(this, listChildCareimage)
                        rvChildcareImages!!.adapter = childCareDetailsImageAdapter
                        indicator!!.attachToRecyclerView(rvChildcareImages!!)
                    }



                }
            } else {
                ErrorBodyResponse(response, this, childcare_details_progressbar)
                childcare_details_progressbar?.hideProgressBar()
            }
        })


        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
            childcare_details_progressbar?.hideProgressBar()
        })
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        val india = LatLng(48.946697, 2.153927)
        mMap!!.addMarker(MarkerOptions()
                .position(india)
                .title("Marker in Sydney"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(india))
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.ivBack -> {
              onBackPressed()
            }
            R.id.ivShare -> {
                dailogshare()
            }
            R.id.btnModify -> {

            }
            R.id.btnPublish -> {
                OpenActivity(NurserieActivityy::class.java )
                // AppUtils.gotoFragment(this, com.nelyan_live.fragments.NurserieFragment(), R.id.frame_container, false)

            }
        }
    }

}