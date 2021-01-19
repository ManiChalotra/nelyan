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
import android.widget.TextView;

import com.nelyan.AppUtils;
import com.nelyan.R;
import com.nelyan.ui.ChildCareActivity;
import com.nelyan.ui.HomeActivity;

public class TraderListingFragment extends Fragment {

    Context mContext;
    ImageView ivHeart, ivBack,ivMap;
    TextView title,tvFilter;
    RelativeLayout rl_1;
    View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_trader_listing, container, false);
        mContext=getActivity();
        ivBack=v.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment f = fm.findFragmentById(R.id.frame_container);

                fm.popBackStack();
            }
        });  ivMap=v.findViewById(R.id.ivMap);
        ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(), ChildCareActivity.class);
                startActivity(i);
            }
        }); tvFilter=v.findViewById(R.id.tvFilter);
        tvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.gotoFragment(mContext, new TradeFilterFragment(), R.id.frame_container, false);

            }
        });  ivHeart=v.findViewById(R.id.ivHeart);
        ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.gotoFragment(mContext, new FavoriteFragment(), R.id.frame_container, false);

            }
        }); rl_1=v.findViewById(R.id.rl_1);
        rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.gotoFragment(mContext, new TraderPublishFragment(), R.id.frame_container, false);
            }
        });

        return v;
    }
}