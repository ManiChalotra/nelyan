package com.nelyan.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nelyan.R

class AllFragment : Fragment() {
    var v: View? = null
    var mContext: Context? = null
    var title: TextView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_all, container, false)
        mContext = activity
        title = v!!.findViewById(R.id.title)
        /* Intent i= Intent.g;
        String text= i.getStringExtra("yes")
        TextView  title=v.findViewById(R.id.title);
title.setText(text);*/return v
    }
}