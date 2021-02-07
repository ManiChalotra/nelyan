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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nelyan.R;
import com.nelyan.adapter.MessageAdapter;
import com.nelyan.adapter.NotificationAdapter;
import com.nelyan.ui.Chat1Activity;
import com.nelyan.ui.ChatActivity;
import com.nelyan.ui.HomeActivity;

public class MessageFragment extends Fragment {
View v;
Context mContext;
ImageView ivDel1,ivDel2,ivBack;
RelativeLayout rl_1,rl_2,rl_3;
Dialog dialog;
RecyclerView recyclerview;
    MessageAdapter messageAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_message, container, false);
        mContext=getActivity();
        ivBack=v.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment f = fm.findFragmentById(R.id.frame_container);

                fm.popBackStack();
               // getActivity().onBackPressed();

            }
        });
       /* rl_1=v.findViewById(R.id.rl_1);
        rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), Chat1Activity.class);
                startActivity(i);
            }
        });*/
        recyclerview=v.findViewById(R.id.recyclerview);
        messageAdapter = new MessageAdapter(mContext);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setAdapter(messageAdapter) ;
      /*  ivDel1=v.findViewById(R.id.ivDel1);
        ivDel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogDelete();
            }
        });
 ivDel2=v.findViewById(R.id.ivDel2);
        ivDel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogDelete();
            }
        });*/


        return v;
    }
    public void dailogDelete(){

        dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.alert_chat_delete);
        dialog.setCancelable(true);

        RelativeLayout rl_1;
        rl_1= dialog.findViewById(R.id.rl_1);
        rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}