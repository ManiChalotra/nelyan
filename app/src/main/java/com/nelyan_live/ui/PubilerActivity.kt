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
import com.nelyan_live.utils.myCustomToast
import kotlinx.android.synthetic.main.activity_pubiler.*

class PubilerActivity : AppCompatActivity(), View.OnClickListener {
    var mContext: Context? = null
    var ivLogo: ImageView? = null
    var ivRadio1: ImageView? = null
    var ivOn: ImageView? = null
    var ivRadio2: ImageView? = null
    var ivRadio3: ImageView? = null
    var ivRadio4: ImageView? = null
    var ll_1: LinearLayout? = null
    var ll_2: LinearLayout? = null
    var ll_3: LinearLayout? = null
    var ll_4: LinearLayout? = null
    var ll_5: LinearLayout? = null
    var state = 0
    var type = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pubiler)

        header.visibility =View.VISIBLE
        btnSubmit!!.setOnClickListener(this)
        ivBack!!.setOnClickListener(this)



        radioGroup.setOnCheckedChangeListener { radioGroup, optionId ->
            run {

                when (optionId) {
                    R.id.btn_activity -> {
                        type = "1"
                    }
                    R.id.btn_child_care -> {
                        type = "2"
                    }
                    R.id.btn_traderArtisans -> {
                        type = "3"
                    }
                }
            }
        }


        /*  mContext = this
          ivBack = findViewById(R.id.ivBack)
          ll_1 = findViewById(R.id.ll_1)
          ll_2 = findViewById(R.id.ll_2)
          ll_3 = findViewById(R.id.ll_3)
          ll_4 = findViewById(R.id.ll_4)
          ll_5 = findViewById(R.id.ll_5)
          ivOn = findViewById(R.id.ivOn)
          ivRadio1 = findViewById(R.id.apivRadio1)
          ivRadio2 = findViewById(R.id.ivRadio2)
          ivRadio3 = findViewById(R.id.ivRadio3)
          ivRadio4 = findViewById(R.id.ivRadio4)


          ll_1!!.setOnClickListener(View.OnClickListener {
              type = "1"
              ivOn!!.setImageResource(R.drawable.radio_outline)
              ivRadio1!!.setImageResource(R.drawable.radio_fill)
              ivRadio2!!.setImageResource(R.drawable.radio_outline)
              ivRadio3!!.setImageResource(R.drawable.radio_outline)
              ivRadio4!!.setImageResource(R.drawable.radio_outline)
          })
          ll_2!!.setOnClickListener(View.OnClickListener {
              type = "2"
              ivOn!!.setImageResource(R.drawable.radio_fill)
              ivRadio1!!.setImageResource(R.drawable.radio_outline)
              ivRadio2!!.setImageResource(R.drawable.radio_outline)
              ivRadio3!!.setImageResource(R.drawable.radio_outline)
              ivRadio4!!.setImageResource(R.drawable.radio_outline)
          })
          ll_3!!.setOnClickListener(View.OnClickListener {
              type = "3"
              ivOn!!.setImageResource(R.drawable.radio_outline)
              ivRadio1!!.setImageResource(R.drawable.radio_outline)
              ivRadio2!!.setImageResource(R.drawable.radio_fill)
              ivRadio3!!.setImageResource(R.drawable.radio_outline)
              ivRadio4!!.setImageResource(R.drawable.radio_outline)
          })
          ll_4!!.setOnClickListener(View.OnClickListener {
              type = "4"
              ivOn!!.setImageResource(R.drawable.radio_outline)
              ivRadio1!!.setImageResource(R.drawable.radio_outline)
              ivRadio2!!.setImageResource(R.drawable.radio_outline)
              ivRadio3!!.setImageResource(R.drawable.radio_fill)
              ivRadio4!!.setImageResource(R.drawable.radio_outline)
          })
          ll_5!!.setOnClickListener(View.OnClickListener {
              type = "5"
              ivOn!!.setImageResource(R.drawable.radio_outline)
              ivRadio1!!.setImageResource(R.drawable.radio_outline)
              ivRadio2!!.setImageResource(R.drawable.radio_outline)
              ivRadio3!!.setImageResource(R.drawable.radio_outline)
              ivRadio4!!.setImageResource(R.drawable.radio_fill)
          })
          ivBack!!.setOnClickListener(View.OnClickListener { finish() })
          ivLogo = findViewById(R.id.ivLogo)
          ivLogo!!.setOnClickListener(View.OnClickListener {
              val i = Intent(this@PubilerActivity, HomeActivity::class.java)
              startActivity(i)
          })
          btnSubmit = findViewById(R.id.btnSubmit)
          btnSubmit!!.setOnClickListener(View.OnClickListener {

          })*/
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnSubmit ->{
                if (type == "1") {
                    val i = Intent(this@PubilerActivity, AddActivity::class.java)
                    startActivity(i)
                } else if (type == "2") {
                    val i = Intent(this@PubilerActivity, BabySitterActivity::class.java)
                    startActivity(i)
                } else if (type == "3") {
                    val i = Intent(this@PubilerActivity, TraderActivity::class.java)
                    startActivity(i)
                } else {
                    myCustomToast(getString(R.string.publisher_unselected_error))
                }
            }
            R.id.ivBack ->{
                onBackPressed()
            }

        }
    }
}