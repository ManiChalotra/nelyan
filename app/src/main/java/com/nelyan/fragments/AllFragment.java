package com.nelyan.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nelyan.R;
public class AllFragment extends Fragment {
View v;
Context mContext;
TextView title;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_all, container, false);
        mContext=getActivity();
        title=v.findViewById(R.id.title);
       /* Intent i= Intent.g;
        String text= i.getStringExtra("yes")
        TextView  title=v.findViewById(R.id.title);
title.setText(text);*/
        return v;
    }
}