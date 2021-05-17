package com.nelyan_live.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.nelyan_live.R

class SectorizationActivity : AppCompatActivity(), View.OnClickListener {
    var mContext: Context? = null
    var ivBack: ImageView? = null
    var ivLogo: ImageView? = null
    var ivOn: ImageView? = null
    var ivOf: ImageView? = null
    var btnOk: Button? = null
    var ll_1: LinearLayout? = null
    var ll_2: LinearLayout? = null


    private var listType: String? = null
    private var sectorizationType =""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sectorization)
        mContext = this

        ivOn = findViewById(R.id.ivOn)
        ivOf = findViewById(R.id.ivOf)
        ll_1 = findViewById(R.id.ll_public)
        ll_2 = findViewById(R.id.ll_privates)
        ivBack = findViewById(R.id.ivBack)
        ivLogo = findViewById(R.id.ivLogo)
        btnOk = findViewById(R.id.btn_sectorization_ok)

        if (intent.extras != null) {
            listType = intent.getStringExtra("type").toString()
            Log.e("qwe", intent.getStringExtra("type").toString())
        }

        ll_1!!.setOnClickListener(this)
        ll_2!!.setOnClickListener(this)
        ivBack!!.setOnClickListener(this)
        ivLogo!!.setOnClickListener(this)
        btnOk!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ll_privates -> {
                ivOn!!.setImageResource(R.drawable.checkbox)
                ivOf!!.setImageResource(R.drawable.checked)
                sectorizationType= "private"
            }
            R.id.ll_public -> {
                ivOn!!.setImageResource(R.drawable.checked)
                ivOf!!.setImageResource(R.drawable.checkbox)
                sectorizationType= "public"
            }
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.ivLogo -> {
                finish()
            }
            R.id.btn_sectorization_ok -> {
                val i = Intent(this@SectorizationActivity, HomeSectorizationActivity::class.java)
                i.putExtra("sectorizationType", sectorizationType)
                i.putExtra("type", listType)
                startActivity(i)
            }
        }
    }
}