package com.nelyan_live.ui

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.nelyan_live.R
import com.nelyan_live.adapter.ActivityListAdapter
import kotlinx.android.synthetic.main.fragment_trade_filter.*

class TraderFilterActivity : AppCompatActivity(), View.OnClickListener,AdapterView.OnItemSelectedListener, ActivityListAdapter.OnHomeActivitiesRecyclerViewItemClickListner {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_trade_filter)
        initalizeClicks()

        val genderlist = arrayOf<String?>("", "Sport", "Cultural", "Languages")
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, R.layout.customspinner, genderlist)
        orderby!!.setAdapter(adapter)
        orderby!!.setOnItemSelectedListener(this@TraderFilterActivity)
        val km = arrayOf<String?>(
                "", "OKM", "5KM", "10KM", "15KM", "20KM",
                "25KM", "30KM"
        )
        val adapter1: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, R.layout.size_customspinner, km)
        orderby1!!.setAdapter(adapter1)
        orderby1!!.setOnItemSelectedListener(this@TraderFilterActivity)

    }
    private  fun initalizeClicks(){
        ivBack.setOnClickListener(this)
        ll_1.setOnClickListener(this)
        btnFilter.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ivBack->{
                onBackPressed()
            }
            R.id.ll_1->{
                dailogLocation()
            }
            R.id.btnFilter->{
                onBackPressed()
            }
        }
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    fun dailogLocation() {
       val  dialog = Dialog(this)
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

    override fun onAddFavoriteClick() {
        TODO("Not yet implemented")
    }

    override fun onHomeActivitiesItemClickListner() {
        TODO("Not yet implemented")
    }

}
