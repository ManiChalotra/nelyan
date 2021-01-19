package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nelyan.HELPER.image;
import com.nelyan.R;
import com.nelyan.adapter.ChatAdapter;

public class Chat1Activity extends image {
    Context mContext;
    ImageView ivBack,ivAttachment;
    ChatAdapter chatAdapter;
    RecyclerView recyclerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat1);
        mContext=this;
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
        });
        recyclerview=findViewById(R.id.recyclerview);
        chatAdapter = new ChatAdapter(mContext);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setAdapter(chatAdapter) ;
    }

    @Override
    public void selectedImage(Bitmap var1, String var2) {
        ivAttachment.setImageBitmap(var1);
    }
}