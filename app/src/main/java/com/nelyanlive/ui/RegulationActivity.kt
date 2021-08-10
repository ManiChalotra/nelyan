package com.nelyanlive.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.utils.AllSharedPref
import com.nelyanlive.utils.checkIfHasNetwork
import com.nelyanlive.utils.security_key
import com.nelyanlive.utils.showSnackBar
import kotlinx.android.synthetic.main.activity_regulation.*
import org.json.JSONObject

class RegulationActivity : AppCompatActivity() {
    var mContext: Context? = null
    var v: View? = null
    var ivBack: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regulation)
        mContext = this
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener { finish() }
        setData()
    }

    val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance((this).application).create(AppViewModel::class.java)
    }

    private var authorization = ""

    private fun setData() {
        if (checkIfHasNetwork((this))) {
            authorization = AllSharedPref.restoreString(this, "auth_key")
            appViewModel.getChatRegulations(security_key, authorization)
        }
        else {
            showSnackBar((this), getString(R.string.no_internet_error))
        }

        appViewModel.observeChatRegulationApiResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {

                    Log.e("observeChatRegulationApiResponse", "-------------" + Gson().toJson(response.body()))
                    val jsonMain = JSONObject(response.body().toString())
                    Log.e("socket===", jsonMain.toString())
                    val jsonData = jsonMain.getJSONObject("data")

                    tvText.text =   HtmlCompat.fromHtml(jsonData.getString("content"), HtmlCompat.FROM_HTML_MODE_LEGACY)

                }
            }

        })

    }


}