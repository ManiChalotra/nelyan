package com.nelyan.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.nelyan.R

class SettingsActivity : AppCompatActivity() {
    var mContext: Context? = null
    var dialog: Dialog? = null
    var ivBack: ImageView? = null
    var toggle: ImageView? = null
    var toggle_off: ImageView? = null
    var On: ImageView? = null
    var Of: ImageView? = null
    var llAbout: LinearLayout? = null
    var llContact: LinearLayout? = null
    var llChange: LinearLayout? = null
    var llTerms: LinearLayout? = null
    var llPrivacy: LinearLayout? = null
    var llLogout: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        mContext = this
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
        toggle = findViewById(R.id.toggle)
        toggle_off = findViewById(R.id.toggle_off)
        toggle!!.setOnClickListener(View.OnClickListener {
            toggle_off!!.setVisibility(View.VISIBLE)
            toggle!!.setVisibility(View.GONE)
        })
        toggle_off!!.setOnClickListener(View.OnClickListener {
            toggle_off!!.setVisibility(View.GONE)
            toggle!!.setVisibility(View.VISIBLE)
        })
        On = findViewById(R.id.On)
        Of = findViewById(R.id.Of)
        On!!.setOnClickListener(View.OnClickListener {
            Of!!.setVisibility(View.VISIBLE)
            On!!.setVisibility(View.GONE)
        })
        Of!!.setOnClickListener(View.OnClickListener {
            Of!!.setVisibility(View.GONE)
            On!!.setVisibility(View.VISIBLE)
        })
        llChange = findViewById(R.id.llChange)
        llChange!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@SettingsActivity, ChangePasswordActivity::class.java)
            startActivity(i)
        })
        llContact = findViewById(R.id.llContact)
        llContact!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@SettingsActivity, ContactUsActivity::class.java)
            startActivity(i)
        })
        llAbout = findViewById(R.id.llAbout)
        llAbout!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@SettingsActivity, AboutActivity::class.java)
            startActivity(i)
        })
        llPrivacy = findViewById(R.id.llPrivacy)
        llPrivacy!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@SettingsActivity, PrivacyActivity::class.java)
            startActivity(i)
        })
        llTerms = findViewById(R.id.llTerms)
        llTerms!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@SettingsActivity, com.nelyan.ui.TermsActivity::class.java)
            startActivity(i)
        })
        llLogout = findViewById(R.id.llLogout)
        llLogout!!.setOnClickListener(View.OnClickListener { showDailog() })
    }

    fun showDailog() {
        dialog = Dialog(mContext!!)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_logout)
        dialog!!.setCancelable(true)
        val rlYes: RelativeLayout
        val rlNo: RelativeLayout
        rlYes = dialog!!.findViewById(R.id.rlYes)
        rlYes.setOnClickListener {
            val i = Intent(mContext, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
            dialog!!.dismiss()
        }
        rlNo = dialog!!.findViewById(R.id.rlNo)
        rlNo.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()
    }
}