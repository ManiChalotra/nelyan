package com.nelyan.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelyan.R
import com.nelyan.adapter.MyHomeAdapter
import com.nelyan.modals.HomeModal
import java.util.*

class HomeFragment : Fragment() {
    var mContext: Context? = null
    var iv_bell: ImageView? = null
    var datalist = ArrayList<HomeModal>()
    var recyclerView: RecyclerView? = null
    var iv_back: ImageView? = null
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        mContext = activity
        recyclerView = root.findViewById(R.id.rc_home)
        val lm = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.setLayoutManager(lm)
        datalist.clear()
        datalist.add(HomeModal(R.drawable.banner_img, "Les activités dans ma ville"))
        datalist.add(HomeModal(R.drawable.banner_img_2, "La garde d'enfants"))
        datalist.add(HomeModal(R.drawable.banner_img_3, "Les écoles de mon enfant"))
        datalist.add(HomeModal(R.drawable.service_4, "Les commerçants dans ma ville"))
        val ad = MyHomeAdapter(requireActivity(), datalist)
        recyclerView!!.setAdapter(ad)
        iv_back = root.findViewById(R.id.iv_back)
        iv_back!!.setImageResource(R.drawable.menu)
        iv_back!!.setVisibility(View.GONE)
        return root
    }
}