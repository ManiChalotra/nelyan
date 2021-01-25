package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nelyan.HELPER.image;
import com.nelyan.R;
import com.nelyan.adapter.DayTimeRepeatAdapter;
import com.nelyan.adapter.DescriptionRepeatAdapter;
import com.nelyan.adapter.EventRepeatAdapter;
import com.nelyan.adapter.TimeRepeatAdapter;
import com.nelyan.modals.DayTimeModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class TraderActivity extends image implements
        AdapterView.OnItemSelectedListener {
    Context mContext;
    ImageView ivBack, ivImg, ivCam;
    Button btnSubmit;
    TextView tvAdd, tvClock;
    TextView edClo, edClo1, edClo2, edClo3;
    ImageView ivClock, ivClock1;
    Spinner orderby, orderby1;    RecyclerView Recycler_scroll;
    ScrollingPagerIndicator indicator;

    RecyclerView rvDayTime,rvDesc;
    DayTimeRepeatAdapter dayTimeRepeatAdapter;
    static String imgtype,imasgezpos;
    int returnItemView=1;
    HashMap<String, Bitmap> image=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trader);
        mContext = this;
        indicator=findViewById(R.id.indicator);
        Recycler_scroll=findViewById(R.id.Recycler_scroll);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        Recycler_scroll.setLayoutManager(linearLayoutManager);
        ItemsAdapter  adapterItems = new ItemsAdapter(mContext);
        Recycler_scroll.setAdapter(adapterItems);
        indicator.attachToRecyclerView(Recycler_scroll);
        rvDesc = findViewById(R.id.rvDesc);
        rvDayTime = findViewById(R.id.rvDayTime);

        orderby = findViewById(R.id.orderby);
        List<String> country = new ArrayList<>();
        country.add("");
        country.add("USA");
        country.add("Japan");
        country.add("India");
        ;
        ArrayAdapter arrayAdapte1 = new ArrayAdapter(this, R.layout.customspinner, country);
        orderby.setAdapter(arrayAdapte1);

        ivBack = findViewById(R.id.ivBack);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(TraderActivity.this, TraderPreviewActivity.class);
                //  i.putExtra("nurActivity","nurFrag");
                startActivity(i);
            }
        });
        ivImg = findViewById(R.id.ivImg);
        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image("all");
            }
        });

        final ArrayList<Integer> arrayList = new ArrayList();
        arrayList.add(1);

        final DayTimeModel dayTimeModel = new DayTimeModel(arrayList);

        final ArrayList<DayTimeModel> dayTimeModelArrayList = new ArrayList<>();
        dayTimeModelArrayList.add(dayTimeModel);

        dayTimeRepeatAdapter = new DayTimeRepeatAdapter(this, dayTimeModelArrayList,
                new DayTimeRepeatAdapter.DayTimeRepeatListener() {
                    @Override
                    public void dayTimeAdd(int pos) {
                        final ArrayList<Integer> arrayList1 = new ArrayList();
                        arrayList1.add(1);
                        DayTimeModel dayTimeModel1 = new DayTimeModel(arrayList1);
                        dayTimeModelArrayList.add(dayTimeModel1);
                        dayTimeRepeatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void timeAdd(int pos) {

                        DayTimeModel dayTimeModel1 = dayTimeModelArrayList.get(pos);
                        dayTimeModel1.getSelectTime().add(1);
                        dayTimeRepeatAdapter.notifyDataSetChanged();
                    }
                });
        rvDayTime.setAdapter(dayTimeRepeatAdapter);
        rvDayTime.setLayoutManager(new LinearLayoutManager(this));


        DescriptionRepeatAdapter  adapter = new DescriptionRepeatAdapter(this,image,TraderActivity.this,returnItemView);

        rvDesc.setLayoutManager(new LinearLayoutManager(this));
        rvDesc.setAdapter(adapter);
    }

  /*  private String getCurrentTime() {
        String currentTime="Current Time: "+timepicker.getCurrentHour()+":"+timepicker.getCurrentMinute();
        return currentTime;
    }*/

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void selectedImage(Bitmap var1, String var2) {

       // ivImg.setImageBitmap(var1);
        if (   imgtype.equals("0"))
        {
            ivImg.setImageBitmap(var1);
        }
        else if (imgtype.equals("1"))
        {
         //   ivplus.setImageBitmap(var1);
        }
        else
        {
            image.put(imasgezpos.toString(),var1);
            Log.e("kmdkmdkedcmk","Activity-"+imasgezpos+"   "+var1);
            Log.e("kmdkmdkedcmk","PPPctivity-"+image.get(imasgezpos));
            DescriptionRepeatAdapter adapter = new DescriptionRepeatAdapter(this,image,TraderActivity.this,returnItemView);
            rvDesc.setLayoutManager(new LinearLayoutManager(this));
            rvDesc.setAdapter(adapter) ;
        }
    }
    public void   imageClick(int pos,int size) {
        imgtype="2";
        returnItemView=size;
        Log.e("wdwdwdd",String.valueOf(pos));
        imasgezpos= String.valueOf(pos);
        image("all");
    }
}