package com.nelyan.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nelyan.R;
import com.nelyan.adapter.NotificationAdapter;
import com.nelyan.adapter.SectorizationAdapter;
import com.nelyan.ui.Activity2Activity;
import com.nelyan.ui.SectorizationActivity;
import com.nelyan.ui.SectorizationMapActivity;

public class SectorizationListFragment extends Fragment {
View v;
Context mContext;
ImageView ivMap,ivMap1,ivBack;
RecyclerView recyclerview,recyclerview1;
    SectorizationAdapter sectorizationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_sectorization_list, container, false);
        mContext=getActivity();
        ivBack=v.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment f = fm.findFragmentById(R.id.frame_container);
                fm.popBackStack();
            }
        });
        ivMap=v.findViewById(R.id.ivMap);
        ivMap1=v.findViewById(R.id.ivMap1);
        ivMap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SectorizationMapActivity.class);
                startActivity(i);
            }
        });
        ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),SectorizationMapActivity.class);
                startActivity(i);
            }
        });

        recyclerview=v.findViewById(R.id.recyclerview);
        sectorizationAdapter = new SectorizationAdapter(getActivity());
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setAdapter(sectorizationAdapter) ;

        recyclerview1=v.findViewById(R.id.recyclerview1);
        sectorizationAdapter = new SectorizationAdapter(getActivity());
        recyclerview1.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview1.setAdapter(sectorizationAdapter) ;

        return v;
    }
}
