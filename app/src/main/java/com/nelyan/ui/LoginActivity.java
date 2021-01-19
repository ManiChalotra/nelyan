package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nelyan.R;

public class LoginActivity extends AppCompatActivity {
    Context mContext;
    TextView tvForgotPass,tvSignup;
    Button btnLogin;
    ImageView ivblank,ivOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=this;
        ivblank=findViewById(R.id.ivblank);
        ivOn=findViewById(R.id.ivOn);
        ivblank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivblank.setVisibility(View.GONE);
                ivOn.setVisibility(View.VISIBLE);
            }
        }); ivOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivOn.setVisibility(View.GONE);
                ivblank.setVisibility(View.VISIBLE);
            }
        });

        tvForgotPass=findViewById(R.id.tvForgotPass);
        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(i);
            }
        });
        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(LoginActivity.this,HomeActivity.class);

                startActivity(i);
                finishAffinity();
            }
        });


        tvSignup=findViewById(R.id.tvSignup);
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(i);
            }
        });

    }
}