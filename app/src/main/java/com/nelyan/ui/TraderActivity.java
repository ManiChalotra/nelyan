package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TraderActivity extends image implements
        AdapterView.OnItemSelectedListener {
    Context mContext;
    ImageView ivBack,ivImg,ivCam;
    Button btnSubmit;
    TextView tvAdd,tvClock;
    TextView edClo,edClo1,edClo2,edClo3;
    ImageView ivClock,ivClock1;
    Spinner orderby,orderby1;
    LinearLayout llDes,ll_time;
RelativeLayout rlAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trader);
        mContext=this;
        ll_time=findViewById(R.id.ll_time);
        rlAdd=findViewById(R.id.rlAdd);
        rlAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_time.setVisibility(View.VISIBLE);
            }
        });

        llDes=findViewById(R.id.llDes);
        tvAdd=findViewById(R.id.tvAdd);
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llDes.setVisibility(View.VISIBLE);
            }
        });
        edClo=findViewById(R.id.edClo);

        edClo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TraderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edClo.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });  edClo1=findViewById(R.id.edClo1);

        edClo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TraderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edClo1.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        edClo2=findViewById(R.id.edClo2);
        edClo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TraderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edClo2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });  edClo3=findViewById(R.id.edClo3);

        edClo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TraderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edClo3.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });



        orderby=findViewById(R.id.orderby);
        List<String> country = new ArrayList<>();
        country.add("");
        country.add("USA");
        country.add("Japan"); country.add("India"); ;
        ArrayAdapter arrayAdapte1= new ArrayAdapter( this,R.layout.customspinner,country);
        orderby.setAdapter(arrayAdapte1);

        orderby1=findViewById(R.id.orderby1);
        List<String> days = new ArrayList<>();
        days.add("");
        days.add("Monday");
        days.add("Tuesday"); days.add("Wednesday"); days.add("Thursday"); days.add("Friday"); days.add("Saturday"); days.add("Sunday");
        ArrayAdapter arrayAdapter= new ArrayAdapter( this,R.layout.customspinner,days);
        orderby1.setAdapter(arrayAdapter);

        ivBack=findViewById(R.id.ivBack);
        btnSubmit=findViewById(R.id.btnSubmit);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSubmit=findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(TraderActivity.this,TraderPreviewActivity.class);
              //  i.putExtra("nurActivity","nurFrag");
                startActivity(i);
            }
        });
        ivImg=findViewById(R.id.ivImg);
        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image("all");
            }
        });ivCam=findViewById(R.id.ivCam);
        ivCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image("all");
            }
        });

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

        ivImg.setImageBitmap(var1);
        ivCam.setImageBitmap(var1);
    }
}