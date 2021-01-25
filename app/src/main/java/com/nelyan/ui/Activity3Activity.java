package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nelyan.HELPER.image;
import com.nelyan.R;
import com.nelyan.adapter.AgeGroupRepeatAdapter;
import com.nelyan.adapter.EventRepeatAdapter;
import com.nelyan.fragments.ActivityListFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

import static com.nelyan.R.layout.customspinner;

public class Activity3Activity extends image implements AdapterView.OnItemSelectedListener {
    Context mContext;
    ImageView ivBack, ivImg, ivCam;
    TextView tvCal, tvCal1;
    Button btnSubmit;
    static String imgtype,imasgezpos;
    RelativeLayout rl_1, rl_2;
    LinearLayout ll_1, ll_2;
    RecyclerView Recycler_scroll;
    ScrollingPagerIndicator indicator;
    ImageView ivplus;
    Spinner orderby;

    int returnItemView=1;
    HashMap<String, Bitmap>image=new HashMap<>();
    RelativeLayout rlImg,rlAddImg;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    RecyclerView rvAgeGroup, rvEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity3);
        mContext = this;

        indicator=findViewById(R.id.indicator);
        Recycler_scroll=findViewById(R.id.Recycler_scroll);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        Recycler_scroll.setLayoutManager(linearLayoutManager);
        ItemsAdapter  adapterItems = new ItemsAdapter(mContext);
        Recycler_scroll.setAdapter(adapterItems);
        indicator.attachToRecyclerView(Recycler_scroll);
        rvAgeGroup = findViewById(R.id.rvAgeGroup);
        rvEvent = findViewById(R.id.rvEvent);
        rl_1 = findViewById(R.id.rl_1);
        rl_2 = findViewById(R.id.rl_2);

        ll_1 = findViewById(R.id.ll_1);
        ll_2 = findViewById(R.id.ll_2);

        ivImg = findViewById(R.id.ivImg); rlImg = findViewById(R.id.rlImg);
        rlImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgtype="0";
                image("all");
            }
        });/*ivplus = findViewById(R.id.ivplus); rlAddImg = findViewById(R.id.rlAddImg);
        rlAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgtype="1";
                image("all");
            }
        });*/

        //  tvClock=findViewById(R.id.tvClock);
        ivBack = findViewById(R.id.ivBack);
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
                Intent i = new Intent(mContext, DetailActivity.class);
                //  i.putExtra("activity3","event");
                startActivity(i);
                // finish();
            }
        });
        orderby = findViewById(R.id.orderby);
        List<String> country = new ArrayList<>();
        country.add("");
        country.add("USA");
        country.add("Japan");
        country.add("India");
        ;
        ArrayAdapter arrayAdapte1 = new ArrayAdapter(this, R.layout.customspinner, country);
        orderby.setAdapter(arrayAdapte1);
        AgeGroupRepeatAdapter ageGroupRepeatAdapter = new AgeGroupRepeatAdapter(this);
        rvAgeGroup.setLayoutManager(new LinearLayoutManager(this));
        rvAgeGroup.setAdapter(ageGroupRepeatAdapter);

        EventRepeatAdapter  adapter = new EventRepeatAdapter(this,image,Activity3Activity.this,returnItemView);
        rvEvent.setLayoutManager(new LinearLayoutManager(this));
        rvEvent.setAdapter(adapter) ;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void dateDialog() {

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String date = updateDateLabel();
                tvCal.setText(date);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.datepicker, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();


    }

    private void calDialog() {
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String date = updateDateLabel();
                tvCal1.setText(date);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.datepicker, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void selectedImage(Bitmap var1, String var2) {
        if (   imgtype.equals("0"))
        {
            ivImg.setImageBitmap(var1);
        }
        else if (imgtype.equals("1"))
        {
            ivplus.setImageBitmap(var1);
        }
        else
        {
            image.put(imasgezpos.toString(),var1);
            Log.e("kmdkmdkedcmk","Activity-"+imasgezpos+"   "+var1);
            Log.e("kmdkmdkedcmk","PPPctivity-"+image.get(imasgezpos));
            EventRepeatAdapter  adapter = new EventRepeatAdapter(this,image,Activity3Activity.this,returnItemView);
            rvEvent.setLayoutManager(new LinearLayoutManager(this));
            rvEvent.setAdapter(adapter) ;
        }
    }

    private String updateDateLabel() {
        String dateFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        return sdf.format(myCalendar.getTime());
    }
    public void   imageClick(int pos,int size) {
        imgtype="2";
        returnItemView=size;
        Log.e("wdwdwdd",String.valueOf(pos));
        imasgezpos= String.valueOf(pos);
        image("all");
    }

}