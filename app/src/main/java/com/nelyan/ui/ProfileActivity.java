package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nelyan.R;

public class ProfileActivity extends AppCompatActivity {
Context mContext;
ImageView ivBack,iv_cncl;
Button btnEdit;
ImageView ivPlus;
TextView tvEdit,tvDelete;
LinearLayout ll_1;
Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mContext=this;
        ll_1=findViewById(R.id.ll_1);  tvEdit=findViewById(R.id.tvEdit);  tvDelete=findViewById(R.id.tvDelete);
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ProfileActivity.this,EditProfileActivity.class);
                startActivity(i);
            }
        }); tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delDialog();
            }
        });

        ivBack=findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });btnEdit=findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext,EditProfileActivity.class);
                startActivity(i);
            }
        });ivPlus=findViewById(R.id.ivPlus);
        ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( ll_1.getVisibility() == View.VISIBLE) {
// Its visible
                    ll_1.setVisibility(View.GONE);
                } else {
// Either gone or invisible
                   ll_1.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void delDialog(){

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