package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nelyan.R;

public class SectorizationActivity extends AppCompatActivity {
    Context mContext;
    ImageView ivBack,ivLogo,ivOn,ivOf;
    Button btnOk;
    LinearLayout ll_1,ll_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sectorization);
        mContext=this;
        ivOn=findViewById(R.id.ivOn);
        ivOf=findViewById(R.id.ivOf);
        ll_1=findViewById(R.id.ll_1);
        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivOn.setImageResource(R.drawable.checked);
                ivOf.setImageResource(R.drawable.checkbox);
            }
        });
        ll_2=findViewById(R.id.ll_2);
        ll_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivOn.setImageResource(R.drawable.checkbox);
                ivOf.setImageResource(R.drawable.checked);
            }
        });


        ivBack=findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });  ivLogo=findViewById(R.id.ivLogo);
        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });btnOk=findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SectorizationActivity.this,HomeActivity.class);
                i.putExtra("activity","seclist");
                startActivity(i);
            }
        });
    }
}