package com.nelyan_live.ui

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.nelyan_live.R
import kotlinx.android.synthetic.main.activity_terms.*

class TermsActivity : AppCompatActivity() {
    var mContext: Context? = null
    var ivBack: ImageView? = null
    private  var data = ""

    override fun onResume() {
        super.onResume()
        data = intent?.extras?.getString("cmsData").toString()
        data = Html.fromHtml(data).toString()
        Log.d("datassss__terms", "-------$data")
        tv_termsTEXT.text = data


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        mContext = this
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
    }
}