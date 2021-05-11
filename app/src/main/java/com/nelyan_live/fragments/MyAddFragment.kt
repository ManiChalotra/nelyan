package com.nelyan_live.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.R
import com.nelyan_live.adapter.MyAddAdapter

import com.nelyan_live.ui.PubilerActivity

class MyAddFragment : Fragment(), OnItemSelectedListener {
   lateinit  var mContext: Context
    var ivAdd: ImageView? = null
    var ivBack: ImageView? = null
    var orderby: Spinner? = null
    var v: View? = null
    var myAddAdapter: MyAddAdapter? = null
    var recyclerview: RecyclerView? = null
    private val MyAddFragment: MyAddFragment? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_my_add, container, false)
        mContext = requireActivity()
        ivBack = v!!.findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener {
            val fm = requireActivity()!!.supportFragmentManager
            val f = fm.findFragmentById(R.id.frame_container)
            fm.popBackStack()
        })
        ivAdd = v!!.findViewById(R.id.ivAdd)
        ivAdd!!.setOnClickListener(View.OnClickListener {
            val i = Intent(activity, PubilerActivity::class.java)
            startActivity(i)
        })
        recyclerview = v!!.findViewById(R.id.recyclerview)
/*
        myAddAdapter = MyAddAdapter(mContext, datalist)
*/
        recyclerview!!.setLayoutManager(LinearLayoutManager(mContext))
        recyclerview!!.setAdapter(myAddAdapter)
        return v
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}
}