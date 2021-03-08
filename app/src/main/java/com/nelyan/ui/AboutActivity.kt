package com.nelyan.ui

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.nelyan.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {
    var mContext: Context? = null
    var ivBack: ImageView? = null

    private  var data = ""

    override fun onResume() {
        super.onResume()
        data = intent?.extras?.getString("cmsData").toString()
        data = Html.fromHtml(data).toString()
        Log.d("datassss__about", "-------"+ data)
        tv_aboutTExt.text = data


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        mContext = this
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
    }
}