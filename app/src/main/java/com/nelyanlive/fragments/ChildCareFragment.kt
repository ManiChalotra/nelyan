package com.nelyanlive.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import com.nelyanlive.R
import com.nelyanlive.R.layout
import com.nelyanlive.adapter.ActivityListAdapter.OnHomeActivitiesRecyclerViewItemClickListner

class ChildCareFragment : Fragment(), OnItemSelectedListener, OnHomeActivitiesRecyclerViewItemClickListner {
    lateinit  var v: View
    var mContext: Context? = null
    var ivBack: ImageView? = null
    var iv_1: ImageView? = null
    var iv_2: ImageView? = null
    var iv_3: ImageView? = null
    var ll_1: LinearLayout? = null
    var ll_2: LinearLayout? = null
    var ll_3: LinearLayout? = null
    var ll_0: LinearLayout? = null
    var btnSearch: Button? = null
    var dialog: Dialog? = null
    var orderby1: Spinner? = null
    var spin1: Spinner? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        v = inflater.inflate(layout.fragment_child_care, container, false)
        mContext = activity
        ivBack = v.findViewById(R.id.ivBack)
        ll_0 = v.findViewById(R.id.ll_0)
        ll_0!!.setOnClickListener { dailogLocation() }
        ivBack!!.setOnClickListener {
            try {
                val bundle = arguments
                if (bundle!!.getString("child") == "childcare") {
                    requireActivity().onBackPressed()
                } else {
                    val fm = requireActivity().supportFragmentManager
                    val f = fm.findFragmentById(R.id.frame_container)
                    fm.popBackStack()
                }
            } catch (e: Exception) {
                val fm = requireActivity().supportFragmentManager
                val f = fm.findFragmentById(R.id.frame_container)
                fm.popBackStack()
            }
        }
        spin1 = v.findViewById(R.id.childType)
        btnSearch = v.findViewById(R.id.btnSearch)
        btnSearch!!.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val f = fm.findFragmentById(R.id.frame_container)
            fm.popBackStack()
        }
        orderby1 = v.findViewById(R.id.spinner_dayss)
        val km = arrayOf<String?>(
                "", "OKM", "5KM", "10KM", "15KM", "20KM", "25KM", "30KM"
        )
        val adapter1: ArrayAdapter<*> = ArrayAdapter<Any?>(
            requireActivity(), layout.size_customspinner, km)
        orderby1!!.adapter = adapter1
        orderby1!!.onItemSelectedListener = this@ChildCareFragment
        spin1 = v.findViewById(R.id.childType)
        val spi = arrayOf<String?>(
                "", "crèche",
                "maternal assistant ", "babysitter")
        val adapter0: ArrayAdapter<*> = ArrayAdapter<Any?>(
            requireActivity(), layout.customspinner, spi)
        spin1!!.adapter = adapter0
        spin1!!.onItemSelectedListener = this@ChildCareFragment
        return v
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    fun dailogLocation() {
        dialog = Dialog(mContext!!)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(layout.alert_location)
        dialog!!.setCancelable(true)
        val rlNo: RelativeLayout = dialog!!.findViewById(R.id.rlNo)
        rlNo.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            dialog!!.dismiss()
        }
        val rlYes: RelativeLayout = dialog!!.findViewById(R.id.rlYes)
        rlYes.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()
    }

    override fun onAddFavoriteClick(toString: String, ivFavourite: ImageView) {
        TODO("Not yet implemented")
    }

    override fun onHomeActivitiesItemClickListner(activityId: String, categoryId: String, postLatitude: String, postLongitude: String) {
        TODO("Not yet implemented")
    }


}