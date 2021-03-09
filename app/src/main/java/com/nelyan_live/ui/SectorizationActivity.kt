package com.nelyan_live.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.nelyan_live.R

class SectorizationActivity : AppCompatActivity() {
    var mContext: Context? = null
    var ivBack: ImageView? = null
    var ivLogo: ImageView? = null
    var ivOn: ImageView? = null
    var ivOf: ImageView? = null
    var btnOk: Button? = null
    var ll_1: LinearLayout? = null
    var ll_2: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sectorization)
        mContext = this
        ivOn = findViewById(R.id.ivOn)
        ivOf = findViewById(R.id.ivOf)
        ll_1 = findViewById(R.id.ll_1)
        ll_1!!.setOnClickListener(View.OnClickListener {
            ivOn!!.setImageResource(R.drawable.checked)
            ivOf!!.setImageResource(R.drawable.checkbox)
        })
        ll_2 = findViewById(R.id.ll_2)
        ll_2!!.setOnClickListener(View.OnClickListener {
            ivOn!!.setImageResource(R.drawable.checkbox)
            ivOf!!.setImageResource(R.drawable.checked)
        })
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
        ivLogo = findViewById(R.id.ivLogo)
        ivLogo!!.setOnClickListener(View.OnClickListener { finish() })
        btnOk = findViewById(R.id.btnOk)
        btnOk!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@SectorizationActivity, HomeActivity::class.java)
            i.putExtra("activity", "seclist")
            startActivity(i)
        })
    }
}