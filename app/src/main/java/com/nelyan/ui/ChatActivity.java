package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nelyan.HELPER.image;
import com.nelyan.R;

public class ChatActivity extends image {
Context mContext;
ImageView ivBack,ivMan,ivOn,ivOf,ivAttachment;
Button btnRegulation;
    LinearLayout ll_1,ll_2;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mContext=this;
        ivOn=findViewById(R.id.ivOn);
        ivOf=findViewById(R.id.ivOf);
        ivOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ivOf.setVisibility(View.VISIBLE);
                ivOn.setVisibility(View.GONE);
            }
        });
        ivOf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ivOf.setVisibility(View.GONE);
                ivOn.setVisibility(View.VISIBLE);
            }
        });

        ivBack=findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }); ivAttachment=findViewById(R.id.ivAttachment);
        ivAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image("all");
            }
        });btnRegulation=findViewById(R.id.btnRegulation);
        btnRegulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ChatActivity.this,RegulationActivity.class);
                startActivity(i);
            }
        });ivMan=findViewById(R.id.ivMan);
        ivMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ChatActivity.this,Chat1Activity.class);
                startActivity(i);
            }
        });
        ll_1=findViewById(R.id.ll_1);
        ll_2=findViewById(R.id.ll_2);
        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDailog();

            }
        });  ll_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogDelete();
            }
        });
    }
    public void showDailog(){

        dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.alert_chat_flag);
        dialog.setCancelable(true);

        RelativeLayout btnSubmit;
        btnSubmit= dialog.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
                dialog.dismiss();
            }
        });
        dialog.show();
    } public void dailogDelete(){

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

    @Override
    public void selectedImage(Bitmap var1, String var2) {
        ivAttachment.setImageBitmap(var1);
    }
}