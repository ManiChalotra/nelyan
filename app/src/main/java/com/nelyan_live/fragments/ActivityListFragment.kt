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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nelyan_live.AppUtils
import com.nelyan_live.R
import com.nelyan_live.R.layout
import com.nelyan_live.adapter.ActivityListAdapter
import com.nelyan_live.adapter.ActivityListAdapter.OnMyEventRecyclerViewItemClickListner
import com.nelyan_live.ui.Activity2Activity
import kotlinx.android.synthetic.main.fragment_activity_list.view.*

class ActivityListFragment : Fragment(), OnItemSelectedListener, OnMyEventRecyclerViewItemClickListner {
    lateinit var mContext: Context
    var tvFilter: TextView? = null
    var iv_share: ImageView? = null
    var iv_map: ImageView? = null
    var ivBack: ImageView? = null
    var orderby: Spinner? = null
    var v: View? = null
    var dialog: Dialog? = null
    var activityListAdapter: ActivityListAdapter? = null
    var recyclerview: RecyclerView? = null
    private val ActivityListFragment: ActivityListFragment? = null
    var navigationbar: BottomNavigationView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v= inflater.inflate(layout.fragment_activity_list, container, false)


        mContext = requireActivity()
        ivBack = v!!.findViewById(R.id.ivBack)
        navigationbar = v!!.findViewById(R.id.navigationbar)
        v!!.ivBack!!.setOnClickListener(View.OnClickListener {
            val fm = requireActivity()!!.supportFragmentManager
            val f = fm.findFragmentById(R.id.frame_container)
            fm.popBackStack()
        })

        orderby = v!!.findViewById(R.id.orderby)
        tvFilter = v!!.findViewById(R.id.tvFilter)
        tvFilter!!.setOnClickListener(View.OnClickListener { AppUtils.gotoFragment(mContext, ActivityFragment(), R.id.frame_container, false) })
        iv_map = v!!.findViewById(R.id.iv_map)
        v!!.iv_map!!.setOnClickListener(View.OnClickListener { // navigationbar.setVisibility(View.GONE);
            //  AppUtils.gotoFragment(mContext, new MapActivityFragment(), R.id.fullframe_container, false);
            val i = Intent(activity, Activity2Activity::class.java)
            startActivity(i)
        })
        recyclerview = v!!.findViewById(R.id.recyclerview)
        activityListAdapter = ActivityListAdapter(requireActivity(), this@ActivityListFragment)
        v!!.recyclerview!!.setLayoutManager(LinearLayoutManager(mContext))
        v!!.recyclerview!!.setAdapter(activityListAdapter)
        val genderlist = arrayOf<String?>(
                "", "Events in City",
                "Date Added",
                "Distance")
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                requireActivity()!!, layout.customspinner, genderlist)

// Setting Adapter to the Spinner
        v!!.orderby!!.setAdapter(adapter)

// Setting OnItemClickListener to the Spinner
        v!!.orderby!!.setOnItemSelectedListener(this@ActivityListFragment)

        return v

    }



    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}
    override fun onMyEventItemClickListner() {

       /* val fragment: Fragment = ActivityDetailsFragment()
        val fragmentManager = requireActivity()!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_container, fragment)
      //  fragmentTransaction.addToBackStack("poofer")
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()*/

        AppUtils.gotoFragment(requireActivity(), ActivityDetailsFragment(), R.id.frame_container, false)

    }
}