package com.nelyan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.nelyan.R;

import java.util.ArrayList;

public class ImageSliderCustomAdapter extends PagerAdapter {

    private Context context;
    ArrayList<Integer> list;
    ArrayList<Integer> listtext;
    ArrayList<Integer> text;


    public ImageSliderCustomAdapter(Context context, ArrayList<Integer> list, ArrayList<Integer> listtext, ArrayList<Integer> text) {
        this.context = context;
        this.list = list;
        this.listtext = listtext;
        this.text = text;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = LayoutInflater.from(view.getContext()).inflate(R.layout.row_image_slider, view, false);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.imagslideid);
        TextView myImaonege = (TextView) myImageLayout.findViewById(R.id.tv_walk);
        TextView myImag = (TextView) myImageLayout.findViewById(R.id.tv_walkdesc);

myImage.setImageResource(list.get(position));
myImaonege.setText(listtext.get(position));
myImag.setText(text.get(position));


        view.addView(myImageLayout,0);
        return myImageLayout;
    }





    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}

