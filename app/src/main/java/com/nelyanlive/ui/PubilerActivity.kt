package com.nelyanlive.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.nelyanlive.R
import com.nelyanlive.utils.myCustomToast
import kotlinx.android.synthetic.main.activity_pubiler.*

class PubilerActivity : AppCompatActivity(), View.OnClickListener {
    var mContext: Context? = null
    var ll_1: LinearLayout? = null
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


    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnSubmit ->{
                when (type) {
                    "1" -> {
                        val i = Intent(this@PubilerActivity, AddActivity::class.java)
                        startActivity(i)
                    }
                    "2" -> {
                        val i = Intent(this@PubilerActivity, BabySitterActivity::class.java)
                        startActivity(i)
                    }
                    "3" -> {
                        val i = Intent(this@PubilerActivity, TraderActivity::class.java)
                        startActivity(i)
                    }
                    else -> {
                        myCustomToast(getString(R.string.publisher_unselected_error))
                    }
                }
            }
            R.id.ivBack ->{
                onBackPressed()
            }

        }
    }
}