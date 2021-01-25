package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nelyan.R;

public class ContactUsActivity extends AppCompatActivity {
ImageView ivBack;
Context mContext;
Button btnSubmit;
Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        mContext=this;
        ivBack=findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSubmit=findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogSubmit();
            }
        });
    }
    public void dailogSubmit(){

        dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.alert_contact);
        dialog.setCancelable(true);

        Button btnYes;
        btnYes= dialog.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}