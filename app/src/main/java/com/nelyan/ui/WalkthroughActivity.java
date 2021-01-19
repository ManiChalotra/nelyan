package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nelyan.R;
import com.nelyan.adapter.ImageSliderCustomAdapter;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class WalkthroughActivity extends AppCompatActivity {
    Context mContext;
    CircleIndicator indicator;
    ViewPager mPager;
    ArrayList<Integer> list;
    ArrayList<Integer> listtext;
    ArrayList<Integer> text;
    TextView tv,tv_walk,tv_walkdesc;
    LinearLayout login,signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);
        mContext=this;
        setView();
    }
    void setView(){
        mPager=findViewById(R.id.view_pager);
        login=findViewById(R.id.ll_login);
        signup=findViewById(R.id.ll_signup);

        indicator=findViewById(R.id.indicator_product);
        list=new ArrayList<>();
        list.add(R.drawable.walkthrough_1);
        list.add(R.drawable.walkthrough_2);
        list.add(R.drawable.walkthrough_3);
        list.add(R.drawable.walkthrough_5);
        list.add(R.drawable.walkthrough_4);

        text=new ArrayList<>();
        text.add(R.string.walkone);
        text.add(R.string.walktwo);
        text.add(R.string.walkthree);
        text.add(R.string.walkfive);
        text.add(R.string.walkfour);

        listtext=new ArrayList<Integer>();
        listtext.add(R.string.walkonedesc);
        listtext.add(R.string.walktwodesc);
        listtext.add(R.string.walkthreedesc);
        listtext.add(R.string.walkfivedesc);
        listtext.add(R.string.walkfourdesc);
        mPager.setAdapter(new ImageSliderCustomAdapter(this,list,text,listtext));
        indicator.setViewPager(mPager);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) { }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WalkthroughActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WalkthroughActivity.this, LoginActivity.class);
                startActivity(i);}
        });
    }
}