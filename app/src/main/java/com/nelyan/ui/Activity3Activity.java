package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.nelyan.fragments.ActivityListFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.nelyan.R.layout.customspinner;

public class Activity3Activity extends image implements
        AdapterView.OnItemSelectedListener {
    Context mContext;
ImageView ivBack,ivImg,ivCam;
TextView tvCal,tvCal1,tvClock1,tvClock,edClo4,edClo0;
Button btnSubmit;
TimePickerDialog timePickerDialog;
Calendar calendar;
int currentHour;
int currentMinute;
RelativeLayout rl_1, rl_2,rlAdd,rlAdd1;
LinearLayout ll_1,ll_2,llAdrs,ll_price;
    private int mYear,mMonth,mDay;
   // EditText edClo1,edClo2,edClo3;
    TextView  edClo,edClo1,edClo2,edClo3;
    ImageView ivClock,ivClock1,ivClock2,ivClock3;
    Spinner orderby,orderby1,orderby2,orderby3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity3);
        mContext=this;
        rl_1=findViewById(R.id.rl_1);
        rl_2=findViewById(R.id.rl_2);

        llAdrs=findViewById(R.id.llAdrs);
        rlAdd=findViewById(R.id.rlAdd);
        rlAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llAdrs.setVisibility(View.VISIBLE);
            }
        }); ll_price=findViewById(R.id.ll_price);
        rlAdd1=findViewById(R.id.rlAdd1);
        rlAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_price.setVisibility(View.VISIBLE);
            }
        });

        ll_1=findViewById(R.id.ll_1);
        ll_2=findViewById(R.id.ll_2);
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
        edClo=findViewById(R.id.edClo);
        edClo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Activity3Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edClo.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        edClo1=findViewById(R.id.edClo1);
        edClo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Activity3Activity.this, new TimePickerDialog.OnTimeSetListener() {
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
                mTimePicker = new TimePickerDialog(Activity3Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edClo2.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });edClo3=findViewById(R.id.edClo3);
        edClo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Activity3Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edClo3.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        edClo0=findViewById(R.id.edClo0);
        edClo0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Activity3Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edClo0.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });edClo4=findViewById(R.id.edClo4);
        edClo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Activity3Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edClo4.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        tvCal=findViewById(R.id.tvCal);
        tvCal1=findViewById(R.id.tvCal1);
        tvCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog();
            }
        });
        tvCal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calDialog();
            }
        });
      //  tvClock=findViewById(R.id.tvClock);
        ivBack=findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }); btnSubmit=findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(mContext,DetailActivity.class);
              //  i.putExtra("activity3","event");
                startActivity(i);
              // finish();
            }
        });
        orderby=findViewById(R.id.orderby);
        List<String> country = new ArrayList<>();
        country.add("");
        country.add("USA");
        country.add("Japan"); country.add("India"); ;
        ArrayAdapter arrayAdapte1= new ArrayAdapter( this,R.layout.customspinner,country);
        orderby.setAdapter(arrayAdapte1);

      /*  orderby2=findViewById(R.id.orderby2);
        List<String> from = new ArrayList<>();
        from.add("Age Group From");
        from.add("5");
        from.add("10"); from.add("15"); ;
        ArrayAdapter arrayAdapte2= new ArrayAdapter( this,R.layout.customspinner,from);
        orderby2.setAdapter(arrayAdapte2);

        orderby3=findViewById(R.id.orderby3);
        List<String> To = new ArrayList<>();
        To.add("Age Group To");
        To.add("25");
        To.add("30"); To.add("45"); ;
        ArrayAdapter arrayAdapte3= new ArrayAdapter( this,R.layout.customspinner,To);
        orderby3.setAdapter(arrayAdapte3);*/

        orderby1=findViewById(R.id.orderby1);
        List<String> days = new ArrayList<>();
        days.add("");
        days.add("Monday");
        days.add("Tuesday"); days.add("Wednesday"); days.add("Thursday"); days.add("Friday"); days.add("Saturday"); days.add("Sunday");
        ArrayAdapter arrayAdapter= new ArrayAdapter( this,R.layout.customspinner,days);
        orderby1.setAdapter(arrayAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void dateDialog(){
        DatePickerDialog.OnDateSetListener listener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvCal.setText(dayOfMonth + "/" + (monthOfYear+1)+"/"+year);
            }};
        DatePickerDialog datePickerDialog=new DatePickerDialog(this,R.style.datepicker,listener,mYear,mMonth,mDay);
        datePickerDialog.show();
    }
    private void calDialog(){
        DatePickerDialog.OnDateSetListener listener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvCal1.setText(dayOfMonth + "/" + (monthOfYear+1)+"/"+year);
            }};
        DatePickerDialog datePickerDialog=new DatePickerDialog(this,R.style.datepicker,listener,mYear,mMonth,mDay);
        datePickerDialog.show();
    } /*private void clockDialog(){
        TimePickerDialog.OnTimeSetListener listener= new TimePickerDialog().OnTimeSetListener() {
            @Override
            public void onDateSet(TimePicker view, int hour, int mintue, int dayOfMonth) {
                tvCal1.setText(dayOfMonth + "/" + (monthOfYear+1)+"/"+year);
            }};
        TimePickerDialog timePickerDialog=new TimePickerDialog(this,R.style.datepicker,listener,mYear,mMonth,mDay);
        timePickerDialog.show();
    }*/

    @Override
    public void selectedImage(Bitmap var1, String var2) {
        ivImg.setImageBitmap(var1);
        ivCam.setImageBitmap(var1);
    }
}