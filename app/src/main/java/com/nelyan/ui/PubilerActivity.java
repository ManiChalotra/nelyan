package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nelyan.R;

public class PubilerActivity extends AppCompatActivity {
Context mContext;
ImageView ivBack,ivLogo,ivRadio1,ivOn,ivRadio2,ivRadio3,ivRadio4;
LinearLayout ll_1,ll_2,ll_3,ll_4,ll_5;
Button btnSubmit;
int state =0;
String type="1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubiler);
        mContext = this;
        ivBack = findViewById(R.id.ivBack);
        ll_1 = findViewById(R.id.ll_1);
        ll_2 = findViewById(R.id.ll_2);
        ll_3 = findViewById(R.id.ll_3);
        ll_4 = findViewById(R.id.ll_4);
        ll_5 = findViewById(R.id.ll_5);
        ivOn = findViewById(R.id.ivOn);
        ivRadio1 = findViewById(R.id.ivRadio1);
        ivRadio2 = findViewById(R.id.ivRadio2);
        ivRadio3 = findViewById(R.id.ivRadio3);
        ivRadio4 = findViewById(R.id.ivRadio4);
        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "1";

                ivOn.setImageResource(R.drawable.radio_outline);
                ivRadio1.setImageResource(R.drawable.radio_fill);
                ivRadio2.setImageResource(R.drawable.radio_outline);
                ivRadio3.setImageResource(R.drawable.radio_outline);
                ivRadio4.setImageResource(R.drawable.radio_outline);
            }
        });
        ll_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "2";
                ivOn.setImageResource(R.drawable.radio_fill);
                ivRadio1.setImageResource(R.drawable.radio_outline);
                ivRadio2.setImageResource(R.drawable.radio_outline);
                ivRadio3.setImageResource(R.drawable.radio_outline);
                ivRadio4.setImageResource(R.drawable.radio_outline);
            }
        });
        ll_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "3";
                ivOn.setImageResource(R.drawable.radio_outline);
                ivRadio1.setImageResource(R.drawable.radio_outline);
                ivRadio2.setImageResource(R.drawable.radio_fill);
                ivRadio3.setImageResource(R.drawable.radio_outline);
                ivRadio4.setImageResource(R.drawable.radio_outline);
            }
        });
        ll_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "4";
                ivOn.setImageResource(R.drawable.radio_outline);
                ivRadio1.setImageResource(R.drawable.radio_outline);
                ivRadio2.setImageResource(R.drawable.radio_outline);
                ivRadio3.setImageResource(R.drawable.radio_fill);
                ivRadio4.setImageResource(R.drawable.radio_outline);
            }
        });
        ll_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "5";
                ivOn.setImageResource(R.drawable.radio_outline);
                ivRadio1.setImageResource(R.drawable.radio_outline);
                ivRadio2.setImageResource(R.drawable.radio_outline);
                ivRadio3.setImageResource(R.drawable.radio_outline);
                ivRadio4.setImageResource(R.drawable.radio_fill);
            }
        });


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivLogo = findViewById(R.id.ivLogo);
        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PubilerActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("1")) {
                    Intent i = new Intent(PubilerActivity.this, Activity3Activity.class);
                    startActivity(i);
                } else if (type.equals("2")) {
                    Intent i = new Intent(PubilerActivity.this, NurserieActivity.class);
                    startActivity(i);
                } else if (type.equals("3")) {
                    Intent i = new Intent(PubilerActivity.this, MaternalAssistantActivity.class);
                    startActivity(i);
                } else if (type.equals("4")) {
                    Intent i = new Intent(PubilerActivity.this, BabySitterActivity.class);
                    startActivity(i);
                } else if (type.equals("5")) {
                    Intent i = new Intent(PubilerActivity.this, TraderActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}