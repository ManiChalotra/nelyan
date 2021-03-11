package com.nelyan_live.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.meherr.mehar.db.DataStoragePreference
import com.nelyan_live.HELPER.image
import com.nelyan_live.R
import com.nelyan_live.adapter.AgeGroupRepeatAdapter
import com.nelyan_live.adapter.EventRepeatAdapter
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.activity_activity3.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext


class ActivityFormActivity : OpenCameraGallery(), OnItemSelectedListener, View.OnClickListener , CoroutineScope{

    private  val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java)
    }
    private var IMAGE_SELECTED_TYPE = ""
    private  val job by lazy { kotlinx.coroutines.Job() }

    private  val dataStoragePreference by lazy { DataStoragePreference(this@ActivityFormActivity) }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onResume() {
        super.onResume()
        hitTypeActivity_Api()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity3)
        initalizeclicks()
        checkMvvmResponse()




        /*val ageGroupRepeatAdapter = AgeGroupRepeatAdapter(this)
        rvAgeGroup!!.layoutManager = LinearLayoutManager(this)
        rvAgeGroup!!.adapter = ageGroupRepeatAdapter
         val adapter = EventRepeatAdapter(this, image, this@ActivityFormActivity, 1)
        rvEvent!!.layoutManager = LinearLayoutManager(this)
        rvEvent!!.adapter = adapter
*/

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

   /* private fun dateDialog() {
       val  date = OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            val date = updateDateLabel()
            tvCal!!.text = date
        }
        val datePickerDialog = DatePickerDialog(this, R.style.datepicker, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH], myCalendar[Calendar.DAY_OF_MONTH])
        datePickerDialog.show()
    }
    private fun calDialog() {

        date = OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            val date = updateDateLabel()
            tvCal1!!.text = date
        }

        val datePickerDialog = DatePickerDialog(this, R.style.datepicker, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH], myCalendar[Calendar.DAY_OF_MONTH])
        datePickerDialog.show()
    }
*/


    private fun initalizeclicks() {

        // clicks for images
        rlImg.setOnClickListener(this)
        ivImg1.setOnClickListener(this)
        ivImg2.setOnClickListener(this)
        ivImg3.setOnClickListener(this)
        ivplus.setOnClickListener(this)

        // clicks for buttons
        btnSubmit.setOnClickListener(this)
        ivBack.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rlImg -> {
                IMAGE_SELECTED_TYPE = "1"
                checkPermission(this)
            }
            R.id.ivImg1 -> {
                IMAGE_SELECTED_TYPE = "2"

                checkPermission(this)
            }
            R.id.ivImg2 -> {
                IMAGE_SELECTED_TYPE = "3"

                checkPermission(this)
            }
            R.id.ivImg3 -> {
                IMAGE_SELECTED_TYPE = "4"

                checkPermission(this)
            }
            R.id.ivplus -> {
                IMAGE_SELECTED_TYPE = "5"
                checkPermission(this)
            }
            R.id.btnSubmit->{
                OpenActivity(DetailActivity::class.java)
            }
            R.id.ivBack->{
                onBackPressed()
            }
        }
    }

    /* override fun selectedImage(var1: Bitmap, var2: String?) {

         Log.d("imagesleectedPath","-----------"+ var2)
          if (imgtype == "0") {
              ivImg!!.setImageBitmap(var1)
          } else if (imgtype == "1") {
              ivplus!!.setImageBitmap(var1)
          } else if (imgtype == "2") {
              ivImg1!!.setImageBitmap(var1)
          } else if (imgtype == "3") {
              ivImg2!!.setImageBitmap(var1)
          } else if (imgtype == "4") {
              ivImg3!!.setImageBitmap(var1)
          } else {
              image[imasgezpos.toString()] = var1
              Log.e("kmdkmdkedcmk", "Activity-$imasgezpos $var1")
              Log.e("kmdkmdkedcmk", "PPPctivity-" + image[imasgezpos])
              val adapter = EventRepeatAdapter(this, image, this@ActivityFormActivity, returnItemView)
              rvEvent!!.layoutManager = LinearLayoutManager(this)
              rvEvent!!.adapter = adapter
          }
      }
  */
   /* private fun updateDateLabel(): String {
        val dateFormat = "dd/MM/yy"
        val sdf = SimpleDateFormat(dateFormat, Locale.US)
        return sdf.format(myCalendar.time)
    }*/

    override fun getRealImagePath(imgPath: String?) {
        Log.d("selectedImagePath", "-------" + imgPath)

        when (IMAGE_SELECTED_TYPE) {
            "1" -> {
                setImageOnTab(imgPath, ivImg)
            }
            "2" -> {
                setImageOnTab(imgPath, ivImg1)
            }
            "3" -> {
                setImageOnTab(imgPath, ivImg2)
            }
            "4" -> {
                setImageOnTab(imgPath, ivImg3)
            }
            "5" -> {
                setImageOnTab(imgPath, ivplus)
            }
        }
    }

    private fun setImageOnTab(imgPATH: String?, imageview: ImageView?) {
        Log.d("getimage","---------"+ imgPATH.toString())
        Glide.with(this).asBitmap().load( imgPATH).into(imageview!!)
    }

    /* fun imageClick(pos: Int, size: Int) {
         imgtype = "5"
         returnItemView = size
         Log.e("wdwdwdd", pos.toString())
         imasgezpos = pos.toString()
         image("all")
     }*/

    /*companion object {
        var imgtype: String? = null
        var imasgezpos: String? = null
    }*/


    private  fun hitTypeActivity_Api(){
        launch (Dispatchers.Main.immediate){
            val authKey = dataStoragePreference?.emitStoredValue(preferencesKey<String>("auth_key"))?.first()
            appViewModel.sendActivityTypeData(security_key, authKey)
        }
    }

    private  fun checkMvvmResponse(){
        appViewModel.observeActivityTypeResponse()!!.observe(this, androidx.lifecycle.Observer { response->
            if(response!!.isSuccessful && response.code()==200){
                if(response.body()!= null){
                    val jsonObject  = JSONObject(response.body().toString())
                    var jsonArray = jsonObject.getJSONArray("data")
                    val country: ArrayList<String?> = ArrayList()
                    country.add("")
                    for(i in 0 until jsonArray.length()){
                        val name = jsonArray.getJSONObject(i).get("name").toString()
                        country.add(name)
                    }
                    val arrayAdapte1 = ArrayAdapter<Any?>(this, R.layout.customspinner, country as List<Any?>)
                    orderby.adapter = arrayAdapte1
                }
            }else{
                ErrorBodyResponse(response, this, null)
            }
        })

        appViewModel.getException()!!.observe(this, androidx.lifecycle.Observer {
            myCustomToast(it)
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


}