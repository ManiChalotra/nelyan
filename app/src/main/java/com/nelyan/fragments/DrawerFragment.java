package com.nelyan.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.nelyan.AppUtils;
import com.nelyan.R;
import com.nelyan.ui.ContactUsActivity;
import com.nelyan.ui.HomeActivity;
import com.nelyan.ui.LoginActivity;
import com.nelyan.ui.MyAlertActivity;
import com.nelyan.ui.NotificationActivity;
import com.nelyan.ui.PrivacyActivity;
import com.nelyan.ui.ProfileActivity;
import com.nelyan.ui.SettingsActivity;


public class DrawerFragment extends Fragment {
    Context mContext;
    TextView tvHome,tvLogin,
            tvAdd,tvAlert,tvFavorite,tvProfile,tvContact,tvNoti,tvSettings,tvLog;
    Dialog dialog;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_drawer, container, false);
        mContext=getActivity();
        tvHome=v.findViewById(R.id.tvHome);
        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
            }
        });
        tvLogin=v.findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
            }
        });


        tvAdd=v.findViewById(R.id.tvAdd);
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.gotoFragment(mContext, new MyAddFragment(), R.id.frame_container, false);
            }
        });
       /* tvAlert=v.findViewById(R.id.tvAlert);
        tvAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyAlertActivity.class);
                startActivity(i);
            }
        });*/
        tvFavorite=v.findViewById(R.id.tvFavorite);
        tvFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.gotoFragment(mContext, new FavoriteFragment(), R.id.frame_container, false);
            }
        }); tvProfile=v.findViewById(R.id.tvProfile);
        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ProfileActivity.class);
                startActivity(i);
            }
        });tvContact=v.findViewById(R.id.tvContact);
        tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(i);
            }
        });tvNoti=v.findViewById(R.id.tvNoti);
        tvNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NotificationActivity.class);
                startActivity(i);
            }
        });
        tvSettings=v.findViewById(R.id.tvSettings);
        tvSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SettingsActivity.class);
                startActivity(i);
            }
        });
        tvLog=v.findViewById(R.id.tvLog);
        tvLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLog();
            }
        });
        return v;
    }
    public void showLog(){

        dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.alert_logout);
        dialog.setCancelable(true);
        RelativeLayout rlYes,rlNo;

        rlYes= dialog.findViewById(R.id.rlYes);
        rlYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(mContext, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                //   mContext.startActivity(new Intent(mContext, LoginActivity.class));

                dialog.dismiss();
            }
        });rlNo= dialog.findViewById(R.id.rlNo);
        rlNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}