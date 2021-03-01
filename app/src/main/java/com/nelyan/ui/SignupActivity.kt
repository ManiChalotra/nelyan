package com.nelyan.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.nelyan.HELPER.image
import com.nelyan.R
import java.util.*

class SignupActivity : image(), OnItemSelectedListener {
    var mContext: Context? = null
    var ivBack: ImageView? = null
    var iv_uploader: ImageView? = null
    var btnRegister: Button? = null
    var tvTerms: TextView? = null
    var tvPrivacy: TextView? = null
    var orderby1: Spinner? = null

    //  String[] signup = {"Consultant", "Professional"};
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        mContext = this
        orderby1 = findViewById(R.id.orderby1)
        val category: MutableList<String?> = ArrayList()
        category.add("")
        category.add("Consultant")
        category.add("Professional")
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.customspinner, category as List<Any?>)
        orderby1!!.setAdapter(arrayAdapter)


        /* orderby1.setOnItemSelectedListener(this);
        ArrayAdapter sig = new ArrayAdapter(this,android.R.layout.simple_list_item_1,signup);
        sig.setDropDownViewResource(android.R.layout.simple_list_item_1);
        orderby1.setAdapter(sig);*/ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(i)
        })
        tvTerms = findViewById(R.id.tvTerms)
        tvTerms!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@SignupActivity, com.nelyan.ui.TermsActivity::class.java)
            startActivity(i)
        })
        tvPrivacy = findViewById(R.id.tvPrivacy)
        tvPrivacy!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@SignupActivity, PrivacyActivity::class.java)
            startActivity(i)
        })
        btnRegister = findViewById(R.id.btnRegister)
        btnRegister!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@SignupActivity, HomeActivity::class.java)
            startActivity(i)
        })
        iv_uploader = findViewById(R.id.iv_uploader)
        iv_uploader!!.setOnClickListener(View.OnClickListener { image("all") })
    }

    override fun selectedImage(var1: Bitmap, var2: String) {
        iv_uploader!!.setImageBitmap(var1)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}
}