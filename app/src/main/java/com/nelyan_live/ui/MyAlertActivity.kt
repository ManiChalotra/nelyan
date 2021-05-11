package com.nelyan_live.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.nelyan_live.R

class MyAlertActivity : AppCompatActivity() {
    var mContext: Context? = null
    var ivBack: ImageView? = null
    var ivLogo: ImageView? = null
    var btnOk: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_alert)
        mContext = this
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
        ivLogo = findViewById(R.id.ivLogo)
        ivLogo!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@MyAlertActivity, HomeActivity::class.java)
            startActivity(i)
        })
        btnOk = findViewById(R.id.btn_sectorization_ok)
        btnOk!!.setOnClickListener(View.OnClickListener { finish() })
    }
}