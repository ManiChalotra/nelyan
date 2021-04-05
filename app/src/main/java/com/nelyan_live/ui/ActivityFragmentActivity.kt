package com.nelyan_live.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.nelyan_live.R
import com.nelyan_live.adapter.ActivityListAdapter
import java.util.*

class ActivityFragmentActivity : AppCompatActivity() , AdapterView.OnItemSelectedListener {

    var btnFilter: Button? = null
    var tvCal: TextView? = null
    var dialog: Dialog? = null
    var ivBack: ImageView? = null
    var ll_1: LinearLayout? = null
    var orderby: Spinner? = null
    var orderby1: Spinner? = null
    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity)

        ivBack = findViewById(R.id.ivBack)

        ivBack!!.setOnClickListener(View.OnClickListener { // getActivity().onBackPressed();
            onBackPressed()
        })

        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        tvCal =findViewById(R.id.tvCal)
        tvCal!!.setOnClickListener(View.OnClickListener { dateDialog() })
        ll_1 = findViewById(R.id.ll_1)
        ll_1!!.setOnClickListener(View.OnClickListener { dailogLocation() })
        btnFilter = findViewById(R.id.btnFilter)
        btnFilter!!.setOnClickListener(View.OnClickListener {
          onBackPressed()
        })
        orderby =findViewById(R.id.orderby)
        val genderlist = arrayOf<String?>(
                "",
                "Sport", "Cultural", "Languages")
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, R.layout.customspinner, genderlist)
        orderby!!.setAdapter(adapter)
        orderby!!.setOnItemSelectedListener(this@ActivityFragmentActivity)
        orderby1 = findViewById(R.id.orderby1)
        val km = arrayOf<String?>(
                "", "OKM",
                "5KM", "10KM", "15KM", "20KM", "25KM", "30KM"
        )
        val adapter1: ArrayAdapter<*> = ArrayAdapter<Any?>(
             this, R.layout.size_customspinner, km)
        orderby1!!.setAdapter(adapter1)
        /*try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            ListPopupWindow popupWindow = (ListPopupWindow) popup.get(orderby1);
            popupWindow.setHeight(50);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            Log.e("sjhj","jsbhj"+e);
        }*/orderby1!!.setOnItemSelectedListener(this@ActivityFragmentActivity)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun dateDialog() {
        val listener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> tvCal!!.text = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year }
        val datePickerDialog = DatePickerDialog(this, R.style.datepicker, listener, mYear, mMonth, mDay)
        datePickerDialog.show()
    }

    fun dailogLocation() {
        dialog = Dialog(this)
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