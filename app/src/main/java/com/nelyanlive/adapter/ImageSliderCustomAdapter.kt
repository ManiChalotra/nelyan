package com.nelyanlive.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.nelyanlive.R
import java.util.*

class ImageSliderCustomAdapter(private val context: Context, var list: ArrayList<Int>, var listtext: ArrayList<Int>, var text: ArrayList<Int>) : PagerAdapter() {
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val myImageLayout = LayoutInflater.from(view.context).inflate(R.layout.row_image_slider, view, false)
        val myImage = myImageLayout.findViewById<View>(R.id.imagslideid) as ImageView
        val myImaonege = myImageLayout.findViewById<View>(R.id.tv_walk) as TextView
        val myImag = myImageLayout.findViewById<View>(R.id.tv_walkdesc) as TextView
        myImage.setImageResource(list[position])
        myImaonege.setText(listtext[position])
        myImag.setText(text[position])
        view.addView(myImageLayout, 0)
        return myImageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

}