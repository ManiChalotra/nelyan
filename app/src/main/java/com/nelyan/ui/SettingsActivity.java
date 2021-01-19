package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nelyan.R;

public class SettingsActivity extends AppCompatActivity {
Context mContext;

Dialog dialog;
ImageView ivBack,toggle,toggle_off,On,Of;
LinearLayout llAbout,llContact,llChange,llTerms,llPrivacy,llLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext=this;
        ivBack=findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toggle=findViewById(R.id.toggle);
        toggle_off=findViewById(R.id.toggle_off);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggle_off.setVisibility(View.VISIBLE);
                toggle.setVisibility(View.GONE);
            }
        });
        toggle_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toggle_off.setVisibility(View.GONE);
                toggle.setVisibility(View.VISIBLE);
            }
        });
        On=findViewById(R.id.On);
        Of=findViewById(R.id.Of);
        On.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Of.setVisibility(View.VISIBLE);
                On.setVisibility(View.GONE);
            }
        });
        Of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Of.setVisibility(View.GONE);
                On.setVisibility(View.VISIBLE);
            }
        });

        llChange=findViewById(R.id.llChange);
        llChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SettingsActivity.this,ChangePasswordActivity.class);
                startActivity(i);
            }
        }); llContact=findViewById(R.id.llContact);
        llContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SettingsActivity.this,ContactUsActivity.class);
                startActivity(i);
            }
        }); llAbout=findViewById(R.id.llAbout);
        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SettingsActivity.this,AboutActivity.class);
                startActivity(i);
            }
        }); llPrivacy=findViewById(R.id.llPrivacy);
        llPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SettingsActivity.this,PrivacyActivity.class);
                startActivity(i);
            }
        }); llTerms=findViewById(R.id.llTerms);
        llTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SettingsActivity.this,TermsActivity.class);
                startActivity(i);
            }
        }); llLogout=findViewById(R.id.llLogout);
        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDailog();
            }
        });

    }
    public void showDailog(){

        dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.alert_logout);
        dialog.setCancelable(true);
        RelativeLayout rlYes,rlNo;

        rlYes= dialog.findViewById(R.id.rlYes);
        rlYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(mContext,LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
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