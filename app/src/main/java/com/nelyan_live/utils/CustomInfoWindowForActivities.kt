package com.nelyan_live.utils

import android.app.Activity
import android.content.Context
import android.view.View
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker

import com.nelyan_live.R
import com.nelyan_live.modals.homeactivitylist.HomeAcitivityResponseData
import kotlinx.android.synthetic.main.map_info_window.view.*


class CustomInfoWindowForActivities(val context: Context, val list: ArrayList<HomeAcitivityResponseData>):
    InfoWindowAdapter {
    override fun getInfoContents(marker: Marker?): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker?): View {
        val view = (context as Activity).layoutInflater.inflate(R.layout.map_info_window, null)

        view.iv_marker_user.setImageBitmap(list[marker!!.snippet.toInt()].activityimages[0].bitmap)
        view.tv_activity_name.text = list[marker!!.snippet.toInt()].activityname
        view.tv_location.text = list[marker!!.snippet.toInt()].address

        return view
    }
}