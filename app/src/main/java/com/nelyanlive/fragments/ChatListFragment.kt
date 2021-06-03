/*
package com.nelyan_live.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.AppUtils
import com.nelyan_live.R
import com.nelyan_live.R.layout
import com.nelyan_live.adapter.ChatListAdapter
import com.nelyan_live.ui.ChildCareActivity

class ChatListFragment : Fragment(), OnItemSelectedListener {
    lateinit  var mContext: Context
    var tvFilter: TextView? = null
    var ivButton: ImageView? = null
    var iv_map: ImageView? = null
    var ivBack: ImageView? = null
    var orderby: Spinner? = null
    lateinit  var v: View
    var dialog: Dialog? = null
    var chatListAdapter: ChatListAdapter? = null
    var recyclerview: RecyclerView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(layout.fragment_chat_list, container, false)
        mContext = requireActivity()
        ivBack = v.findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener {
            val fm = requireActivity()!!.supportFragmentManager
            val f = fm.findFragmentById(R.id.frame_container)
            fm.popBackStack()
        })
        val ll_1 = v.findViewById<LinearLayout>(R.id.ll_1)
        ll_1.setOnClickListener { dailogLocation() }
        orderby = v.findViewById(R.id.orderby)
        tvFilter = v.findViewById(R.id.tvFilter)
        tvFilter!!.setOnClickListener(View.OnClickListener {
            AppUtils.gotoFragment(mContext, com.nelyan_live.fragments.ChildCareFragment(), R.id.frame_container, false)
            //   AppUtils.gotoFragment(mContext, new HomeFragment(), R.id.container, false);
        })
        iv_map = v.findViewById(R.id.iv_map)
        iv_map!!.setOnClickListener(View.OnClickListener {
            val i = Intent(activity, ChildCareActivity::class.java)
            startActivity(i)
        })
        recyclerview = v.findViewById(R.id.recyclerview)
        chatListAdapter = ChatListAdapter(requireActivity())
        recyclerview!!.setLayoutManager(LinearLayoutManager(mContext))
        recyclerview!!.setAdapter(chatListAdapter)
        val genderlist = arrayOf<String?>(
                "",
                "Date Added",
                "Avilable Place",
                "Distance")
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                requireActivity()!!, layout.customspinner, genderlist)

// Setting Adapter to the Spinner
        orderby!!.setAdapter(adapter)

// Setting OnItemClickListener to the Spinner
        orderby!!.setOnItemSelectedListener(this@ChatListFragment)
        return v
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}
    fun dailogLocation() {
        dialog = Dialog(mContext!!)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(layout.alert_location)
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
}*/
