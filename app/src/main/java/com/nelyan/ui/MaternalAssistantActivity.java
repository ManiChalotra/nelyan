package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.nelyan.HELPER.image;
import com.nelyan.R;

import java.util.ArrayList;
import java.util.List;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class MaternalAssistantActivity extends image implements
        AdapterView.OnItemSelectedListener {
    Context mContext;
    ImageView ivBack,ivImg;
    ImageView ivplus;
    Button btnSubmit;
    String imgtype;
    Spinner orderby;
    RecyclerView Recycler_scroll;
    ScrollingPagerIndicator indicator;
    RelativeLayout rlAddImg,rlImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maternal_assistant);
        mContext=this;

        indicator=findViewById(R.id.indicator);
        Recycler_scroll=findViewById(R.id.Recycler_scroll);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        Recycler_scroll.setLayoutManager(linearLayoutManager);
        ItemsAdapter  adapterItems = new ItemsAdapter(mContext);
        Recycler_scroll.setAdapter(adapterItems);
        indicator.attachToRecyclerView(Recycler_scroll);

        orderby=findViewById(R.id.orderby);
        List<String> count = new ArrayList<>();
        count.add("");  count.add("0");   count.add("1");
        count.add("2");
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
        });  ivImg=findViewById(R.id.ivImg); rlImg=findViewById(R.id.rlImg);
        rlImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imgtype="0";
                image("all");
            }
        });
       /* ivplus=findViewById(R.id.ivplus); rlAddImg=findViewById(R.id.rlAddImg);
        rlAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgtype="1";
                image("all");
            }
        });*/
        btnSubmit=findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MaternalAssistantActivity.this,HomeActivity.class);
                i.putExtra("activity","mater");
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
        if (   imgtype.equals("0")){
            ivImg.setImageBitmap(var1);
        } else if (imgtype.equals("1")){
            ivplus.setImageBitmap(var1);
        }
    }
}