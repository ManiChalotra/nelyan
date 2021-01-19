package com.nelyan.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nelyan.AppUtils;
import com.nelyan.R;
import com.nelyan.adapter.ActivityListAdapter;
import com.nelyan.ui.Activity2Activity;

import static com.nelyan.R.layout.customspinner;

public class ActivityListFragment extends Fragment implements AdapterView.OnItemSelectedListener, ActivityListAdapter.OnMyEventRecyclerViewItemClickListner {
Context mContext;
TextView tvFilter;
ImageView iv_share,iv_map,ivBack;
    Spinner orderby;
View v;
Dialog dialog;

    ActivityListAdapter activityListAdapter;
RecyclerView recyclerview;
    private ActivityListFragment ActivityListFragment;
    BottomNavigationView navigationbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_activity_list, container, false);
        mContext=getActivity();
        ivBack=v.findViewById(R.id.ivBack);
        navigationbar=v.findViewById(R.id.navigationbar);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment f = fm.findFragmentById(R.id.frame_container);
                fm.popBackStack();
            }
        });

        orderby = v.findViewById(R.id.orderby);
        tvFilter=v.findViewById(R.id.tvFilter);
        tvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.gotoFragment(mContext, new ActivityFragment(), R.id.frame_container, false);

            }
        });
        iv_map=v.findViewById(R.id.iv_map);
        iv_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // navigationbar.setVisibility(View.GONE);
              //  AppUtils.gotoFragment(mContext, new MapActivityFragment(), R.id.fullframe_container, false);
                Intent i = new Intent(getActivity(),Activity2Activity.class);
                startActivity(i);
            }
        });
        recyclerview=v.findViewById(R.id.recyclerview);
        activityListAdapter = new ActivityListAdapter(getActivity(),ActivityListFragment);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setAdapter(activityListAdapter) ;
        final String[] genderlist = new String[]{
                "","Events in City",
                "Date Added",
                "Distance",
        };
        ArrayAdapter adapter = new ArrayAdapter(
                getActivity(), customspinner, genderlist);

// Setting Adapter to the Spinner
        orderby.setAdapter(adapter);

// Setting OnItemClickListener to the Spinner
        orderby.setOnItemSelectedListener(ActivityListFragment.this);
        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onMyEventItemClickListner() {
        Fragment fragment = new ActivityDetailsFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack("poofer");
        fragmentTransaction.commit();
    }

}