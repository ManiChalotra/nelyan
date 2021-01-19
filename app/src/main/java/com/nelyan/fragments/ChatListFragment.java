package com.nelyan.fragments;

import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nelyan.AppUtils;
import com.nelyan.R;
import com.nelyan.adapter.ActivityListAdapter;
import com.nelyan.adapter.ChatListAdapter;
import com.nelyan.ui.Activity2Activity;
import com.nelyan.ui.ChildCareActivity;

import static com.nelyan.R.layout.customspinner;

public class ChatListFragment extends Fragment  implements AdapterView.OnItemSelectedListener{
    Context mContext;
    TextView tvFilter;
    ImageView ivButton,iv_map,ivBack;
    Spinner orderby;
    View v;
    Dialog dialog;
    ChatListAdapter chatListAdapter;
       RecyclerView recyclerview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_chat_list, container, false);
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
     LinearLayout ll_1=v.findViewById(R.id.ll_1);
        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogLocation();
            }
        });
        orderby = v.findViewById(R.id.orderby);
        tvFilter=v.findViewById(R.id.tvFilter);
        tvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.gotoFragment(mContext, new ChildCareFragment(), R.id.frame_container, false);
             //   AppUtils.gotoFragment(mContext, new HomeFragment(), R.id.container, false);
            }
        });
        iv_map=v.findViewById(R.id.iv_map);
        iv_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(), ChildCareActivity.class);
                startActivity(i);
            }
        });
        recyclerview=v.findViewById(R.id.recyclerview);
        chatListAdapter = new ChatListAdapter(getActivity());
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setAdapter(chatListAdapter) ;
        final String[] genderlist = new String[]{
                "",
                "Date Added",
                "Avilable Place",
                "Distance",
        };
        ArrayAdapter adapter = new ArrayAdapter(
                getActivity(), customspinner, genderlist);

// Setting Adapter to the Spinner
        orderby.setAdapter(adapter);

// Setting OnItemClickListener to the Spinner
        orderby.setOnItemSelectedListener(ChatListFragment.this);
        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void dailogLocation(){

        dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.alert_location);
        dialog.setCancelable(true);

        RelativeLayout rlYes,rlNo;
        rlNo= dialog.findViewById(R.id.rlNo);
        rlNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
                dialog.dismiss();
            }
        }); rlYes= dialog.findViewById(R.id.rlYes);
        rlYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}