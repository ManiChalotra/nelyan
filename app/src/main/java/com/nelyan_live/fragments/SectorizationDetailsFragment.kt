package com.nelyan_live.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.R
import com.nelyan_live.adapter.DetailsImageAdapter
import com.nelyan_live.modals.DetailsImageModal
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.util.*

class SectorizationDetailsFragment : Fragment() {
    var mContext: Context? = null
    lateinit  var v: View
    var ivBack: ImageView? = null
    var rc: RecyclerView? = null
    var rc_detailsimg: RecyclerView? = null
    var datalist = ArrayList<DetailsImageModal>()
    var indicator: ScrollingPagerIndicator? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_sectorization_details, container, false)
        mContext = activity
        ivBack = v!!.findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener {
            val fm = requireActivity()!!.supportFragmentManager
            val f = fm.findFragmentById(R.id.frame_container)
            fm.popBackStack()
            // getActivity().onBackPressed();
        })
        rc = v.findViewById(R.id.rc_detailsimg)
        val lm = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rc!!.setLayoutManager(lm)
        indicator = v.findViewById(R.id.indicator)
        datalist.add(DetailsImageModal(R.drawable.img_1))
        datalist.add(DetailsImageModal(R.drawable.img_4))
        datalist.add(DetailsImageModal(R.drawable.img_1))
        datalist.add(DetailsImageModal(R.drawable.img_4))
        datalist.add(DetailsImageModal(R.drawable.img_1))
        val ad = DetailsImageAdapter(requireActivity()!!, datalist)
        rc!!.setAdapter(ad)
        indicator!!.attachToRecyclerView(rc!!)
        return v
    }
}