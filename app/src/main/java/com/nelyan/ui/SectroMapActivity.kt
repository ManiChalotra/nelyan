package com.nelyan.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.nelyan.R

class SectroMapActivity : AppCompatActivity() {
    var mContext: Context? = null
    var ivBack: ImageView? = null
    var ivFilter: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sectro_map)
        mContext = this
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
        ivFilter = findViewById(R.id.ivFilter)
        ivFilter!!.setOnClickListener(View.OnClickListener { finish() })
    }
}