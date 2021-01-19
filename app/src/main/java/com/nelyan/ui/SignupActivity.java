package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;

import com.nelyan.HELPER.image;
import com.nelyan.R;

import java.util.ArrayList;
import java.util.List;

public class SignupActivity extends image implements
        AdapterView.OnItemSelectedListener {
    Context mContext;
    ImageView ivBack,iv_uploader;
    Button btnRegister;
    TextView tvTerms,tvPrivacy;
    Spinner orderby1;
  //  String[] signup = {"Consultant", "Professional"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mContext=this;
        orderby1=findViewById(R.id.orderby1);

        List <String> category = new ArrayList<>();
        category.add("");
        category.add("Consultant");
        category.add("Professional");
        ArrayAdapter arrayAdapter= new ArrayAdapter( this,R.layout.customspinner,category);
        orderby1.setAdapter(arrayAdapter);



       /* orderby1.setOnItemSelectedListener(this);
        ArrayAdapter sig = new ArrayAdapter(this,android.R.layout.simple_list_item_1,signup);
        sig.setDropDownViewResource(android.R.layout.simple_list_item_1);
        orderby1.setAdapter(sig);*/

        ivBack=findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
        tvTerms=findViewById(R.id.tvTerms);
        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(SignupActivity.this,TermsActivity.class);
                startActivity(i);
            }
        });
        tvPrivacy=findViewById(R.id.tvPrivacy);
        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(SignupActivity.this,PrivacyActivity.class);
                startActivity(i);
            }
        });
 btnRegister=findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(SignupActivity.this,HomeActivity.class);
                startActivity(i);
            }
        });

        iv_uploader=findViewById(R.id.iv_uploader);
        iv_uploader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
image("all");
            }
        });
    }

    @Override
    public void selectedImage(Bitmap var1, String var2) {
        iv_uploader.setImageBitmap(var1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}