package com.nelyan.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelyan.R
import com.nelyan.adapter.MyAddAdapter
import com.nelyan.fragments.MyAddFragment

class AddActivity : AppCompatActivity() {
   lateinit  var mContext: Context
    var ivAdd: ImageView? = null
    var ivBack: ImageView? = null
    var orderby: Spinner? = null
    var myAddAdapter: MyAddAdapter? = null
    var recyclerview: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        mContext = this
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener { onBackPressed() })
        ivAdd = findViewById(R.id.ivAdd)
        ivAdd!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@AddActivity, com.nelyan.ui.PubilerActivity::class.java)
            startActivity(i)
        })
        recyclerview = findViewById(R.id.recyclerview)
        myAddAdapter = MyAddAdapter(mContext)
        recyclerview!!.setLayoutManager(LinearLayoutManager(mContext))
        recyclerview!!.setAdapter(myAddAdapter)
    }
}