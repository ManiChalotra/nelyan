package com.nelyan_live.ui

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
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
import com.nelyan_live.HELPER.image
import com.nelyan_live.R
import com.nelyan_live.adapter.AgeGroupRepeatAdapter
import com.nelyan_live.adapter.EventRepeatAdapter
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.text.SimpleDateFormat
import java.util.*

class ActivityFormActivity : image(), OnItemSelectedListener {
    var mContext: Context? = null
    var ivBack: ImageView? = null
    var ivImg: ImageView? = null
    var ivCam: ImageView? = null
    var tvCal: TextView? = null
    var tvCal1: TextView? = null
    var btnSubmit: Button? = null
    var rl_1: RelativeLayout? = null
    var rl_2: RelativeLayout? = null
    var ll_1: LinearLayout? = null
    var ll_2: LinearLayout? = null
    var ll_3: LinearLayout? = null
    var Recycler_scroll: RecyclerView? = null
    var indicator: ScrollingPagerIndicator? = null
    var ivplus: ImageView? = null
    var ivImg1: ImageView? = null
    var ivImg2: ImageView? = null
    var ivImg3: ImageView? = null
    var orderby: Spinner? = null
    var returnItemView = 1
    var image = HashMap<String, Bitmap>()
    var rlImg: RelativeLayout? = null
    var rlAddImg: RelativeLayout? = null
    var myCalendar = Calendar.getInstance()
    var date: OnDateSetListener? = null
    var rvAgeGroup: RecyclerView? = null
    var rvEvent: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity3)
        mContext = this
        rvAgeGroup = findViewById(R.id.rvAgeGroup)
        rvEvent = findViewById(R.id.rvEvent)
        rl_1 = findViewById(R.id.rl_1)
       // rl_2 = findViewById(R.id.rl_2)
        ll_1 = findViewById(R.id.ll_1)
        ll_2 = findViewById(R.id.ll_2)
        ll_3 = findViewById(R.id.ll_3)
        ivImg = findViewById(R.id.ivImg)
        ivImg1 = findViewById(R.id.ivImg1)
        ivImg2 = findViewById(R.id.ivImg2)
        ivImg3 = findViewById(R.id.ivImg3)
        rlImg = findViewById(R.id.rlImg)
        rlImg!!.setOnClickListener {
            imgtype = "0"
            image("all")
        }
        ivplus = findViewById(R.id.ivplus)
        rlAddImg = findViewById(R.id.rlAddImg)
        rlAddImg!!.setOnClickListener {
            imgtype = "1"
            image("all")
        }
        ll_1!!.setOnClickListener {
            imgtype = "2"
            image("all")
        }
        ll_2!!.setOnClickListener {
            imgtype = "3"
            image("all")
        }
        ll_3!!.setOnClickListener {
            imgtype = "4"
            image("all")
        }

// tvClock=findViewById(R.id.tvClock);
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener { finish() }
        btnSubmit = findViewById(R.id.btnSubmit)
        btnSubmit!!.setOnClickListener {
            val i = Intent(mContext, DetailActivity::class.java)
            // i.putExtra("activity3","event");
            startActivity(i)
            // finish();
        }
        orderby = findViewById(R.id.orderby)
        val country: MutableList<String?> = ArrayList()
        country.add("")
        country.add("USA")
        country.add("Japan")
        country.add("India")
        val arrayAdapte1: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.customspinner, country as List<Any?>)
        orderby!!.adapter = arrayAdapte1
        val ageGroupRepeatAdapter = AgeGroupRepeatAdapter(this)
        rvAgeGroup!!.layoutManager = LinearLayoutManager(this)
        rvAgeGroup!!.adapter = ageGroupRepeatAdapter
        val adapter = EventRepeatAdapter(this, image, this@ActivityFormActivity, returnItemView)
        rvEvent!!.layoutManager = LinearLayoutManager(this)
        rvEvent!!.adapter = adapter
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}
    private fun dateDialog() {
        date = OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
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

   override fun selectedImage(var1: Bitmap, var2: String?) {
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

    private fun updateDateLabel(): String {
        val dateFormat = "dd/MM/yy"
        val sdf = SimpleDateFormat(dateFormat, Locale.US)
        return sdf.format(myCalendar.time)
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