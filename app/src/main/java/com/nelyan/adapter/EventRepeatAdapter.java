package com.nelyan.adapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nelyan.R;
import com.nelyan.ui.Activity3Activity;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class EventRepeatAdapter extends RecyclerView.Adapter<EventRepeatAdapter.EventRepeatViewHolder> {
    String selectedImage ="";
    ArrayList<AlbumFile> mAlbumFiles = new ArrayList<AlbumFile>();
    File file;
    Context context;
    RelativeLayout rl_Add ;
    int returnItemView = 1;
    Activity3Activity eventRepeatListen;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    HashMap<String, Bitmap>image;
    public EventRepeatAdapter(Context context, HashMap<String, Bitmap>image, Activity3Activity eventRepeatListen,int returnItemView) {
        this.context = context;
        this.image = image;
        this.returnItemView = returnItemView;
        this.eventRepeatListen = eventRepeatListen;
    }

    @NonNull
    @Override
    public EventRepeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event_add_more, parent, false);
        return new EventRepeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventRepeatViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return returnItemView;
    }


    class EventRepeatViewHolder extends RecyclerView.ViewHolder {

        EditText edtEventName, edtDesc, edtPrice;
        TextView tvCal, tvCal1, edClo2, edClo3, tvAddMore;
        ImageView ivEvent,ivCam;

        public EventRepeatViewHolder(@NonNull View itemView) {
            super(itemView);
            edtEventName = itemView.findViewById(R.id.edtEventName);
            tvCal = itemView.findViewById(R.id.tvCal);
            tvCal1 = itemView.findViewById(R.id.tvCal1);
            edClo2 = itemView.findViewById(R.id.edClo2);
            ivEvent = itemView.findViewById(R.id.ivEventimage);
            edClo3 = itemView.findViewById(R.id.edClo3);
            edtDesc = itemView.findViewById(R.id.edtDesc);
            edtPrice = itemView.findViewById(R.id.edtPrice);
            tvAddMore = itemView.findViewById(R.id.tvAddMore);
            ivCam = itemView.findViewById(R.id.ivCam);

        }

        void bind(final int pos) {
            Log.e("kmdkmdkedcmk","sszzzzeeeeee    "+image.size());
            if (pos == returnItemView - 1) {
                tvAddMore.setVisibility(View.VISIBLE);
            } else {
                tvAddMore.setVisibility(View.GONE);
            }
            try {
                ivEvent.setImageBitmap(image.get(String.valueOf(pos)));
                // ivCam.setVisibility(View.GONE);
                Log.e("kmdkmdkedcmk","ss c--"+pos+"   "+image.get("0"));
            }
            catch (Exception e)
            {
            }
            tvAddMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    returnItemView = returnItemView + 1;
                    notifyDataSetChanged();
                }
            });

            ivEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventRepeatListen.imageClick( pos,returnItemView);

                }
            });

            tvCal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    date = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            String dateInString = updateDateLabel();
                            tvCal.setText(dateInString);
                        }
                    };
                    dateDialog();
                }
            });

            tvCal1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    date = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            String dateInString = updateDateLabel();
                            tvCal1.setText(dateInString);
                        }
                    };
                    dateDialog();
                }
            });

            edClo2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            edClo2.setText(selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            });

            edClo3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            edClo3.setText(selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            });
        }

        private void dateDialog() {
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.datepicker, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();

        }

        private String updateDateLabel() {
            String dateFormat = "dd/MM/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
            return sdf.format(myCalendar.getTime());
        }
    }

    public interface EventRepeatListen {
        void imageClick(int pos, int returnItemView);
    }

}

