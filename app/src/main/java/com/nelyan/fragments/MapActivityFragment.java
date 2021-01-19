package com.nelyan.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nelyan.AppUtils;
import com.nelyan.R;
import com.nelyan.ui.Activity2Activity;

public class MapActivityFragment extends Fragment {
    Context mContext;
    ImageView ivBack,ivFilter;
    RelativeLayout rl_1;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_map_activity, container, false);
        mContext =getActivity();

        ivBack=v.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment f = fm.findFragmentById(R.id.frame_container);

                fm.popBackStack();
            }
        });
        ivFilter=v.findViewById(R.id.ivFilter);
        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    AppUtils.gotoFragment(mContext, new ActivityFragment(), R.id.frame_container, false);

            }
        });
        rl_1=v.findViewById(R.id.rl_1);
        rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.gotoFragment(mContext, new ActivityListFragment(), R.id.frame_container, false);
            }
        });
        return v;
    }
}