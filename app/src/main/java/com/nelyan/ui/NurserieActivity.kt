package com.nelyan.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.recyclerview.widget.RecyclerView
import com.nelyan.HELPER.image
import com.nelyan.R
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.util.*

class NurserieActivity : image(), OnItemSelectedListener {
    var mContext: Context? = null
    var ivBack: ImageView? = null
    var ivImg: ImageView? = null
    var ivplus: ImageView? = null
    var btnSubmit: Button? = null
    var imgtype: String? = null
    var orderby: Spinner? = null
    var orderby1: Spinner? = null
    var Recycler_scroll: RecyclerView? = null
    var indicator: ScrollingPagerIndicator? = null
    var rlAddImg: RelativeLayout? = null
    var rlImg: RelativeLayout? = null
    var ivImg1: ImageView? = null
    var ivImg2: ImageView? = null
    var ivImg3: ImageView? = null
    var ll_1: LinearLayout? = null
    var ll_2: LinearLayout? = null
    var ll_3: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nurserie)
        mContext = this
        /* indicator=findViewById(R.id.indicator);
        Recycler_scroll=findViewById(R.id.Recycler_scroll);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        Recycler_scroll.setLayoutManager(linearLayoutManager);
        ItemsAdapter  adapterItems = new ItemsAdapter(mContext);
        Recycler_scroll.setAdapter(adapterItems);
        indicator.attachToRecyclerView(Recycler_scroll);*/ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
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
        orderby = findViewById(R.id.orderby)
        val info: MutableList<String?> = ArrayList()
        info.add("")
        info.add("Public")
        info.add("Private")
        val arrayAdapte1: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.customspinner, info as List<Any?>)
        orderby!!.setAdapter(arrayAdapte1)
        orderby1 = findViewById(R.id.orderby1)
        val count: MutableList<String?> = ArrayList()
        count.add("")
        count.add("0")
        count.add("1")
        count.add("2")
        count.add("3")
        count.add("4")
        count.add("5")
        count.add("6")
        count.add("7")
        count.add("8")
        count.add("9")
        count.add("10")
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.customspinner, count as List<Any?>)
        orderby1!!.setAdapter(arrayAdapter)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnSubmit!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@NurserieActivity, HomeActivity::class.java)
            i.putExtra("activity", "nurFrag")
            startActivity(i)
        })
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}
    override fun selectedImage(var1: Bitmap, var2: String) {
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
        }
    }
}