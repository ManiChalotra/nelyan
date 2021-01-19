package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.nelyan.HELPER.image;
import com.nelyan.R;

import java.util.ArrayList;
import java.util.List;

public class BabySitterActivity extends image implements
        AdapterView.OnItemSelectedListener {
Context mContext;
ImageView ivBack,ivImg;
Button btnSubmit;
    Spinner orderby;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_sitter);
        mContext=this;
        orderby=findViewById(R.id.orderby);
        List<String> count = new ArrayList<>();
        count.add("");
        count.add("0");
        count.add("1"); count.add("2");
        count.add("3"); count.add("4"); count.add("5");   count.add("6");
        count.add("7");   count.add("8");   count.add("9");   count.add("10");
        ArrayAdapter arrayAdapte1= new ArrayAdapter( this,R.layout.customspinner,count);
        orderby.setAdapter(arrayAdapte1);
        ivBack=findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivImg=findViewById(R.id.ivImg);
        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image("all");
            }
        });

        btnSubmit=findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(BabySitterActivity.this,HomeActivity.class);
                i.putExtra("activity","babyfrag");
                startActivity(i);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void selectedImage(Bitmap var1, String var2) {
        ivImg.setImageBitmap(var1);
    }
}