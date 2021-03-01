package com.nelyan.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nelyan.R

class LoginActivity : AppCompatActivity() {
    var mContext: Context? = null
    var tvForgotPass: TextView? = null
    var tvSignup: TextView? = null
    var btnLogin: Button? = null
    var ivblank: ImageView? = null
    var ivOn: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mContext = this
        ivblank = findViewById(R.id.ivblank)
        ivOn = findViewById(R.id.ivOn)
        ivblank!!.setOnClickListener(View.OnClickListener {
            ivblank!!.setVisibility(View.GONE)
            ivOn!!.setVisibility(View.VISIBLE)
        })
        ivOn!!.setOnClickListener(View.OnClickListener {
            ivOn!!.setVisibility(View.GONE)
            ivblank!!.setVisibility(View.VISIBLE)
        })
        tvForgotPass = findViewById(R.id.tvForgotPass)
        tvForgotPass!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(i)
        })
        btnLogin = findViewById(R.id.btnLogin)
        btnLogin!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(i)
            finishAffinity()
        })
        tvSignup = findViewById(R.id.tvSignup)
        tvSignup!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(i)
        })
    }
}