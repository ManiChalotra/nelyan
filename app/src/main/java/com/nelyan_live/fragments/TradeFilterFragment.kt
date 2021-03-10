/*
package com.nelyan_live.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import com.nelyan_live.R
import com.nelyan_live.R.layout
import com.nelyan_live.adapter.ActivityListAdapter.OnMyEventRecyclerViewItemClickListner

class TradeFilterFragment : Fragment(), OnItemSelectedListener, OnMyEventRecyclerViewItemClickListner {
   lateinit  var v: View
    var mContext: Context? = null
    private val mYear = 0
    private val mMonth = 0
    private val mDay = 0
    var ivBack: ImageView? = null
    var tvCal: TextView? = null
    var orderby: Spinner? = null
    var orderby1: Spinner? = null
    var dialog: Dialog? = null
    var btnFilter: Button? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(layout.fragment_trade_filter, container, false)
        mContext = activity
        ivBack = v.findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener {
            val fm = requireActivity()!!.supportFragmentManager
            val f = fm.findFragmentById(R.id.frame_container)
            fm.popBackStack()
        })
        val ll_1 = v.findViewById<LinearLayout>(R.id.ll_1)
        ll_1.setOnClickListener { dailogLocation() }
        btnFilter = v.findViewById(R.id.btnFilter)
        btnFilter!!.setOnClickListener(View.OnClickListener { requireActivity()!!.onBackPressed() })
        */
/*   tvCal=v.findViewById(R.id.tvCal);
        tvCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog();
            }
        });*//*
orderby = v.findViewById(R.id.orderby)
        val genderlist = arrayOf<String?>(
                "",
                "Sport", "Cultural", "Languages")
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                requireActivity()!!, layout.customspinner, genderlist)
        orderby!!.setAdapter(adapter)
        orderby!!.setOnItemSelectedListener(this@TradeFilterFragment)
        orderby1 = v.findViewById(R.id.orderby1)
        val km = arrayOf<String?>(
                "", "OKM", "5KM", "10KM", "15KM", "20KM",
                "25KM", "30KM"
        )
        val adapter1: ArrayAdapter<*> = ArrayAdapter<Any?>(
                requireActivity()!!, layout.size_customspinner, km)
        orderby1!!.setAdapter(adapter1)
        orderby1!!.setOnItemSelectedListener(this@TradeFilterFragment)
        return v
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}
    override fun onMyEventItemClickListner() {}

    */
/* private void dateDialog(){
        DatePickerDialog.OnDateSetListener listener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvCal.setText(dayOfMonth + "/" + (monthOfYear+1)+"/"+year);
            }};
        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(),R.style.datepicker,listener,mYear,mMonth,mDay);
        datePickerDialog.show();
    }*//*

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
