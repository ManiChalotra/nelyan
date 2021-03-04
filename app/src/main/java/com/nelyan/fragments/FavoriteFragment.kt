package com.nelyan.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelyan.R
import com.nelyan.adapter.favoriteAdapter

class FavoriteFragment : Fragment(), OnItemSelectedListener {
    lateinit  var mContext: Context
    var fav: favoriteAdapter? = null
    var recyclerview: RecyclerView? = null
    var ivBack: ImageView? = null
   lateinit var v: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_favorite, container, false)
        mContext = requireActivity()
        ivBack = v.findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener {
            val fm = requireActivity()!!.supportFragmentManager
            val f = fm.findFragmentById(R.id.frame_container)
            fm.popBackStack()
        })
        recyclerview = v.findViewById(R.id.recyclerview)
        fav = favoriteAdapter(requireActivity())
        recyclerview?.setLayoutManager(LinearLayoutManager(mContext))
        recyclerview?.setAdapter(fav)
        return v
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}
}