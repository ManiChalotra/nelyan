package com.nelyan.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelyan.HELPER.image
import com.nelyan.R
import com.nelyan.adapter.DayTimeRepeatAdapter
import com.nelyan.adapter.DayTimeRepeatAdapter.DayTimeRepeatListener
import com.nelyan.adapter.DescriptionRepeatAdapter
import com.nelyan.modals.DayTimeModel
import com.nelyan.modals.TimeModel
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.util.*

class TraderActivity : image(), OnItemSelectedListener {
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trader)
        mContext = this
        // indicator=findViewById(R.id.indicator);
        /* Recycler_scroll=findViewById(R.id.Recycler_scroll);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        Recycler_scroll.setLayoutManager(linearLayoutManager);
        ItemsAdapter  adapterItems = new ItemsAdapter(mContext);
        Recycler_scroll.setAdapter(adapterItems);
        indicator.attachToRecyclerView(Recycler_scroll);*/rvDesc = findViewById(R.id.rvDesc)
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
            val i = Intent(this@TraderActivity, com.nelyan.ui.TraderPreviewActivity::class.java)
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

        arrayList.add(TimeModel("",""))
        val dayTimeModel = DayTimeModel(arrayList)
        val dayTimeModelArrayList: ArrayList<DayTimeModel> = ArrayList<DayTimeModel>()
        dayTimeModelArrayList.add(dayTimeModel)
        dayTimeRepeatAdapter = DayTimeRepeatAdapter(this, dayTimeModelArrayList,
                object : DayTimeRepeatListener {
                    override fun dayTimeAdd(pos: Int) {
                        val arrayList1: ArrayList<TimeModel?> = ArrayList()

                        arrayList1.add(TimeModel("",""))
                        val dayTimeModel1 = DayTimeModel(arrayList1)
                        dayTimeModelArrayList.add(dayTimeModel1)
                        dayTimeRepeatAdapter!!.notifyDataSetChanged()
                    }

                    override fun timeAdd(pos: Int) {
                        val dayTimeModel1: DayTimeModel = dayTimeModelArrayList[pos]
                        dayTimeModel1.selectTime.add(TimeModel("",""))
                        dayTimeRepeatAdapter!!.notifyDataSetChanged()
                    }
                })
        rvDayTime!!.setAdapter(dayTimeRepeatAdapter)
        rvDayTime!!.setLayoutManager(LinearLayoutManager(this))
        val adapter = DescriptionRepeatAdapter(this, image, this@TraderActivity, returnItemView)
        rvDesc!!.setLayoutManager(LinearLayoutManager(this))
        rvDesc!!.setAdapter(adapter)
    }

    /*  private String getCurrentTime() {
        String currentTime="Current Time: "+timepicker.getCurrentHour()+":"+timepicker.getCurrentMinute();
        return currentTime;
    }*/
    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}
    override fun selectedImage(var1: Bitmap, var2: String) {

        // ivImg.setImageBitmap(var1);
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
            Log.e("kmdkmdkedcmk", "Activity-$imasgezpos   $var1")
            Log.e("kmdkmdkedcmk", "PPPctivity-" + image[imasgezpos])
            val adapter = DescriptionRepeatAdapter(this, image, this@TraderActivity, returnItemView)
            rvDesc!!.layoutManager = LinearLayoutManager(this)
            rvDesc!!.adapter = adapter
        }
    }

    fun imageClick(pos: Int, size: Int) {
        imgtype = "5"
        returnItemView = size
        Log.e("wdwdwdd", pos.toString())
        imasgezpos = pos.toString()
        image("all")
    }

    companion object {
        var imgtype: String? = null
        var imasgezpos: String? = null
    }
}