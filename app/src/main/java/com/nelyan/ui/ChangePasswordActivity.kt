package com.nelyan.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.nelyan.R

class ChangePasswordActivity : AppCompatActivity() {
    var mContext: Context? = null
    var ivBack: ImageView? = null
    var btnSave: Button? = null
    var dialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        mContext = this
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
        btnSave = findViewById(R.id.btnSave)
        btnSave!!.setOnClickListener(View.OnClickListener { showDailog() })
    }

    fun showDailog() {
        dialog = Dialog(mContext!!)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_change_pass)
        dialog!!.setCancelable(true)
        val btnOk: Button
        btnOk = dialog!!.findViewById(R.id.btnOk)
        btnOk.setOnClickListener { //   mContext.startActivity(new Intent(mContext, SettingsActivity.class));
            finish()
            dialog!!.dismiss()
        }
        dialog!!.show()
    }
}