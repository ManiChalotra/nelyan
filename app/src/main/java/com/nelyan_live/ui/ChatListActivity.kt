package com.nelyan_live.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.AppUtils
import com.nelyan_live.R
import com.nelyan_live.adapter.ChatListAdapter

class ChatListActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit  var mContext: Context
    var tvFilter: TextView? = null
    var iv_map: ImageView? = null
    var ivBack: ImageView? = null
    var orderby: Spinner? = null
    var dialog: Dialog? = null
    var chatListAdapter: ChatListAdapter? = null
    var recyclerview: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chat_list)


        mContext = this
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener {
            val fm = supportFragmentManager
            val f = fm.findFragmentById(R.id.frame_container)
            fm.popBackStack()
        })
        val ll_1 = findViewById<LinearLayout>(R.id.ll_1)
        ll_1.setOnClickListener { dailogLocation() }
        orderby = findViewById(R.id.orderby)
        tvFilter = findViewById(R.id.tvFilter)
        tvFilter!!.setOnClickListener(View.OnClickListener {
            AppUtils.gotoFragment(mContext, com.nelyan_live.fragments.ChildCareFragment(), R.id.frame_container, false)
            //   AppUtils.gotoFragment(mContext, new HomeFragment(), R.id.container, false);
        })
        iv_map = findViewById(R.id.iv_map)
        iv_map!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this, ChildCareActivity::class.java)
            startActivity(i)
        })
        recyclerview = findViewById(R.id.recyclerview)
        chatListAdapter = ChatListAdapter(this)
        recyclerview!!.setLayoutManager(LinearLayoutManager(mContext))
        recyclerview!!.setAdapter(chatListAdapter)
        val genderlist = arrayOf<String?>(
                "",
                "Date Added",
                "Avilable Place",
                "Distance")
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, R.layout.customspinner, genderlist)

            // Setting Adapter to the Spinner
        orderby!!.setAdapter(adapter)

            // Setting OnItemClickListener to the Spinner
        orderby!!.setOnItemSelectedListener(this@ChatListActivity)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    fun dailogLocation() {
        dialog = Dialog(mContext!!)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_location)
        dialog!!.setCancelable(true)
        val rlYes: RelativeLayout
        val rlNo: RelativeLayout
        rlNo = dialog!!.findViewById(R.id.rlNo)
        rlNo.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            dialog!!.dismiss()
        }
        rlYes = dialog!!.findViewById(R.id.rlYes)
        rlYes.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()
    }
}