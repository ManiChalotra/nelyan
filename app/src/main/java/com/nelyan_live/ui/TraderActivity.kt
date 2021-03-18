package com.nelyan_live.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.meherr.mehar.db.DataStoragePreference
import com.nelyan_live.HELPER.image
import com.nelyan_live.R
import com.nelyan_live.adapter.DayTimeRepeatAdapter
import com.nelyan_live.adapter.DescriptionRepeatAdapter
import com.nelyan_live.modals.DayTimeModel
import com.nelyan_live.modals.TimeModel
import com.nelyan_live.utils.OpenCameraGallery
import com.nelyan_live.utils.ProgressDialog
import kotlinx.android.synthetic.main.activity_baby_sitter.*
import kotlinx.android.synthetic.main.activity_trader.*
import kotlinx.android.synthetic.main.activity_trader.ivImg
import kotlinx.android.synthetic.main.activity_trader.ivImg1
import kotlinx.android.synthetic.main.activity_trader.ivImg2
import kotlinx.android.synthetic.main.activity_trader.ivImg3
import kotlinx.android.synthetic.main.activity_trader.ivplus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class TraderActivity : OpenCameraGallery(), View.OnClickListener, CoroutineScope {


    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    private var IMAGE_SELECTED_TYPE = ""
    private val job by lazy { kotlinx.coroutines.Job() }
    private val dataStoragePreference by lazy { DataStoragePreference(this) }

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
    private var cityAddress = ""


    private  var dayTimeModelArrayList:ArrayList<DayTimeModel> = ArrayList()

    /*
var mContext: Context? = null
var ivBack: ImageView? = null
var ivImg: ImageView? = null
var ivCam: ImageView? = null
var btnSubmit: Button? = null
var tvAdd: TextView? = null
var tvClock: TextView? = null
var edClo: TextView? = null
var edClo1: TextView? = null
var edClo2: TextView? = null
var edClo3: TextView? = null
var ivplus: ImageView? = null
var ivImg1: ImageView? = null
var ivImg2: ImageView? = null
var ivImg3: ImageView? = null
var ll_1: LinearLayout? = null
var ll_2: LinearLayout? = null
var ll_3: LinearLayout? = null
var orderby: Spinner? = null
var orderby1: Spinner? = null
var Recycler_scroll: RecyclerView? = null
var indicator: ScrollingPagerIndicator? = null
var rlAddImg: RelativeLayout? = null
var rlImg: RelativeLayout? = null
var rvDayTime: RecyclerView? = null
var rvDesc: RecyclerView? = null
var dayTimeRepeatAdapter: DayTimeRepeatAdapter? = null
var returnItemView = 1
var image = HashMap<String, Bitmap>()
*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trader)
        initalizeClicks()

        rvDayTime.adapter = DayTimeRepeatAdapter(this, dayTimeModelArrayList)

    }



    private fun initalizeClicks() {
        ivImg.setOnClickListener(this)
        ivImg1.setOnClickListener(this)
        ivImg2.setOnClickListener(this)
        ivImg3.setOnClickListener(this)
        ivplus.setOnClickListener(this)

    }

/*

        mContext = this
        rvDesc = findViewById(R.id.rvDesc)
        rvDayTime = findViewById(R.id.rvDayTime)
        orderby = findViewById(R.id.orderby)
        val country: MutableList<String?> = ArrayList()
        country.add("")
        country.add("USA")
        country.add("Japan")
        country.add("India")
        val arrayAdapte1: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.customspinner, country as List<Any?>)




        orderby!!.setAdapter(arrayAdapte1)
        ivBack = findViewById(R.id.ivBack)
        btnSubmit = findViewById(R.id.btnSubmit)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
        btnSubmit = findViewById(R.id.btnSubmit)
        btnSubmit!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@TraderActivity, com.nelyan_live.ui.TraderPreviewActivity::class.java)
            //  i.putExtra("nurActivity","nurFrag");
            startActivity(i)
        })



        ll_1 = findViewById(R.id.ll_1)
        ll_2 = findViewById(R.id.ll_2)
        ll_3 = findViewById(R.id.ll_3)
        ivImg = findViewById(R.id.ivImg)
        ivImg1 = findViewById(R.id.ivImg1)
        ivImg2 = findViewById(R.id.ivImg2)
        ivImg3 = findViewById(R.id.ivImg3)
        rlImg = findViewById(R.id.rlImg)
        rlImg!!.setOnClickListener(View.OnClickListener {
            imgtype = "0"
            image("all")
        })
        ivplus = findViewById(R.id.ivplus)
        rlAddImg = findViewById(R.id.rlAddImg)
        rlAddImg!!.setOnClickListener(View.OnClickListener {
            imgtype = "1"
            image("all")
        })
        ll_1!!.setOnClickListener(View.OnClickListener {
            imgtype = "2"
            image("all")
        })
        ll_2!!.setOnClickListener(View.OnClickListener {
            imgtype = "3"
            image("all")
        })
        ll_3!!.setOnClickListener(View.OnClickListener {
            imgtype = "4"
            image("all")
        })
        val arrayList: ArrayList<TimeModel?> = ArrayList()

        arrayList.add(TimeModel("", ""))
        val dayTimeModel = DayTimeModel(arrayList)
        val dayTimeModelArrayList: ArrayList<DayTimeModel> = ArrayList<DayTimeModel>()
        dayTimeModelArrayList.add(dayTimeModel)
        dayTimeRepeatAdapter = DayTimeRepeatAdapter(this, dayTimeModelArrayList,
                object : DayTimeRepeatListener {
                    override fun dayTimeAdd(pos: Int) {
                        val arrayList1: ArrayList<TimeModel?> = ArrayList()

                        arrayList1.add(TimeModel("", ""))
                        val dayTimeModel1 = DayTimeModel(arrayList1)
                        dayTimeModelArrayList.add(dayTimeModel1)
                        dayTimeRepeatAdapter!!.notifyDataSetChanged()
                    }

                    override fun timeAdd(pos: Int) {
                        val dayTimeModel1: DayTimeModel = dayTimeModelArrayList[pos]
                        dayTimeModel1.selectTime.add(TimeModel("", ""))
                        dayTimeRepeatAdapter!!.notifyDataSetChanged()
                    }
                })
        rvDayTime!!.setAdapter(dayTimeRepeatAdapter)
        rvDayTime!!.setLayoutManager(LinearLayoutManager(this))
        val adapter = DescriptionRepeatAdapter(this, image, this@TraderActivity, returnItemView)
        rvDesc!!.setLayoutManager(LinearLayoutManager(this))
        rvDesc!!.setAdapter(adapter)*/


    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.ivImg -> {
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
        }
    }

    override fun getRealImagePath(imgPath: String?) {
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
        Log.d("getimage", "---------" + imgPATH.toString())

        when (IMAGE_SELECTED_TYPE) {

            "1" -> {
                imageview?.setScaleType(ImageView.ScaleType.FIT_XY)
                checkVideoButtonVisibility(imgPATH.toString(), iv_video11)
            }
            "2" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video22)

            }
            "3" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video33)

            }
            "4" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video44)

            }
            "5" -> {
                checkVideoButtonVisibility(imgPATH.toString(), iv_video55)
            }
        }

        Glide.with(this).asBitmap().load(imgPATH).into(imageview!!)
    }


    private fun checkVideoButtonVisibility(imgpath: String, videoButton: ImageView) {

        if (imgpath?.contains(".mp4")!!) {
            videoButton.visibility = View.VISIBLE
        } else {
            videoButton.visibility = View.GONE
        }
    }


/*
    companion object {
        var imgtype: String? = null
        var imasgezpos: String? = null
    }*/


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}