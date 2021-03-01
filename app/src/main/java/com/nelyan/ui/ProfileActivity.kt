package com.nelyan.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.nelyan.R

class ProfileActivity : AppCompatActivity() {
    var mContext: Context? = null
    var ivBack: ImageView? = null
    var iv_cncl: ImageView? = null
    var btnEdit: Button? = null
    var ivPlus: ImageView? = null
    var tvEdit: TextView? = null
    var tvDelete: TextView? = null
    var ll_1: LinearLayout? = null
    var dialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        mContext = this
        ll_1 = findViewById(R.id.ll_1)
        tvEdit = findViewById(R.id.tvEdit)
        tvDelete = findViewById(R.id.tvDelete)
        tvEdit!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@ProfileActivity, EditProfileActivity::class.java)
            startActivity(i)
        })
        tvDelete!!.setOnClickListener(View.OnClickListener { delDialog() })
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
        btnEdit = findViewById(R.id.btnEdit)
        btnEdit!!.setOnClickListener(View.OnClickListener {
            val i = Intent(mContext, EditProfileActivity::class.java)
            startActivity(i)
        })
        ivPlus = findViewById(R.id.ivPlus)
        ivPlus!!.setOnClickListener(View.OnClickListener {
            if (ll_1!!.getVisibility() == View.VISIBLE) {
// Its visible
                ll_1!!.setVisibility(View.GONE)
            } else {
// Either gone or invisible
                ll_1!!.setVisibility(View.VISIBLE)
            }
        })
    }

    fun delDialog() {
        dialog = Dialog(mContext!!)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_chat_delete)
        dialog!!.setCancelable(true)
        val rl_1: RelativeLayout
        rl_1 = dialog!!.findViewById(R.id.rl_1)
        rl_1.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            dialog!!.dismiss()
        }
        dialog!!.show()
    }
}