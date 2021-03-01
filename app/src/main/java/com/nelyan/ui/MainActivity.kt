package com.nelyan.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.nelyan.R
import java.util.*

class MainActivity : AppCompatActivity() {
    var mContext: Context? = null
    var ivLogo: ImageView? = null
    var timer: Timer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = this
        ivLogo = findViewById(R.id.ivLogo)
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                val i = Intent(this@MainActivity, WalkthroughActivity::class.java)
                startActivity(i)
                finish()
            }
        }, 3000)
    }
}