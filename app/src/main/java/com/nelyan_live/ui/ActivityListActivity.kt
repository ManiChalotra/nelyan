package com.nelyan_live.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.AppUtils
import com.nelyan_live.R
import com.nelyan_live.adapter.ActivityListAdapter
import com.nelyan_live.utils.OpenActivity
import kotlinx.android.synthetic.main.fragment_activity_list.*

class ActivityListActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, ActivityListAdapter.OnMyEventRecyclerViewItemClickListner {

    var iv_map: ImageView? = null
    var ivBack: ImageView? = null
    var orderby: Spinner? = null
    var dialog: Dialog? = null
    var activityListAdapter: ActivityListAdapter? = null
    var recyclerview: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity_list)
        ivBack = findViewById(R.id.ivBack)
        orderby = findViewById(R.id.orderby)
        iv_map = findViewById(R.id.iv_map)
        recyclerview = findViewById(R.id.recyclerview)




        ivBack!!.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })


        tvFilter.setOnClickListener(View.OnClickListener {

            OpenActivity(ActivityFragmentActivity::class.java)

           /* val frag: Fragment  = ActivityFragment()
            supportFragmentManager?.beginTransaction().replace(R.id.frame_container, frag)?.addToBackStack(null)?.commit()
           // AppUtils.gotoFragment(mContext, ActivityFragment(), R.id.frame_container, false)*/
        })


        iv_map!!.setOnClickListener(View.OnClickListener { // navigationbar.setVisibility(View.GONE);
            //  AppUtils.gotoFragment(mContext, new MapActivityFragment(), R.id.fullframe_container, false);
            val i = Intent(this, Activity2Activity::class.java)
            startActivity(i)
        })


        activityListAdapter = ActivityListAdapter(this, this@ActivityListActivity)
       recyclerview!!.setLayoutManager(LinearLayoutManager(this))
      recyclerview!!.setAdapter(activityListAdapter)
        val genderlist = arrayOf<String?>(
                "", "Events in City",
                "Date Added",
                "Distance")
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
             this, R.layout.customspinner, genderlist)

// Setting Adapter to the Spinner
      orderby!!.setAdapter(adapter)

// Setting OnItemClickListener to the Spinner
      orderby!!.setOnItemSelectedListener(this@ActivityListActivity)

    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}
    override fun onMyEventItemClickListner() {
OpenActivity(ActivityDetailsActivity::class.java)
        //AppUtils.gotoFragment(this, ActivityDetailsFragment(), R.id.frame_container, false)

    }
}