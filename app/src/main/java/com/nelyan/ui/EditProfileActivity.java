package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nelyan.HELPER.image;
import com.nelyan.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends image {
    Context mContext;
    ImageView ivBack,iv_uploader,ivImg;
    Button btnSave;
    CircleImageView iv_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mContext=this;
        ivBack=findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });btnSave=findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        ivImg=findViewById(R.id.iv_profile);
        iv_profile=findViewById(R.id.iv_profile);
        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image("all");
            }
        });
    }

    @Override
    public void selectedImage(Bitmap var1, String var2) {
        ivImg.setImageBitmap(var1);
    }
}