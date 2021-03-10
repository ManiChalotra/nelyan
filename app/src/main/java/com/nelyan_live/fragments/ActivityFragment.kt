/*
package com.nelyan_live.fragments

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
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
import java.util.*

class ActivityFragment : Fragment(), OnItemSelectedListener, OnMyEventRecyclerViewItemClickListner {
    var v: View? = null
    var mContext: Context? = null
    var btnFilter: Button? = null
    var tvCal: TextView? = null
    var dialog: Dialog? = null
    var ivBack: ImageView? = null
    var ll_1: LinearLayout? = null
    var orderby: Spinner? = null
    var orderby1: Spinner? = null
    var country = arrayOf("Type of activity", "My activities")
    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(layout.fragment_activity, container, false)
        mContext = activity
        ivBack = v!!.findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener { // getActivity().onBackPressed();
            try {
                val bundle = arguments
                if (bundle!!.getString("unamea") == "Homeactivity") {
                    requireActivity()!!.onBackPressed()
                } else {
                    val fm = requireActivity()!!.supportFragmentManager
                    val f = fm.findFragmentById(R.id.frame_container)
                    fm.popBackStack()
                }
            } catch (e: Exception) {
                val fm = requireActivity()!!.supportFragmentManager
                val f = fm.findFragmentById(R.id.frame_container)
                fm.popBackStack()
            }
        })
        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        tvCal = v!!.findViewById(R.id.tvCal)
        tvCal!!.setOnClickListener(View.OnClickListener { dateDialog() })
        ll_1 = v!!.findViewById(R.id.ll_1)
        ll_1!!.setOnClickListener(View.OnClickListener { dailogLocation() })
        btnFilter = v!!.findViewById(R.id.btnFilter)
        btnFilter!!.setOnClickListener(View.OnClickListener {
            requireActivity()!!.onBackPressed()
            */
/*    FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment f = fm.findFragmentById(R.id.frame_container);
                fm.popBackStack();*//*

        })
        orderby = v!!.findViewById(R.id.orderby)
        val genderlist = arrayOf<String?>(
                "",
                "Sport", "Cultural", "Languages")
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                requireActivity()!!, layout.customspinner, genderlist)
        orderby!!.setAdapter(adapter)
        orderby!!.setOnItemSelectedListener(this@ActivityFragment)
        orderby1 = v!!.findViewById(R.id.orderby1)
        val km = arrayOf<String?>(
                "", "OKM",
                "5KM", "10KM", "15KM", "20KM", "25KM", "30KM"
        )
        val adapter1: ArrayAdapter<*> = ArrayAdapter<Any?>(
                requireActivity()!!, layout.size_customspinner, km)
        orderby1!!.setAdapter(adapter1)
        */
/*try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            ListPopupWindow popupWindow = (ListPopupWindow) popup.get(orderby1);
            popupWindow.setHeight(50);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            Log.e("sjhj","jsbhj"+e);
        }*//*
orderby1!!.setOnItemSelectedListener(this@ActivityFragment)
        return v
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}
    override fun onMyEventItemClickListner() {}
    private fun dateDialog() {
        val listener = OnDateSetListener { view, year, monthOfYear, dayOfMonth -> tvCal!!.text = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year }
        val datePickerDialog = DatePickerDialog(requireActivity()!!, R.style.datepicker, listener, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

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
