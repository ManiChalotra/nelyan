package com.nelyan.adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nelyan.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeRepeatAdapter extends RecyclerView.Adapter<TimeRepeatAdapter.TimeRepeatViewHolder> {

    Context context;
    ArrayList<Integer> arrayList;
    public TimeRepeatAdapter(Context context,ArrayList<Integer> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public TimeRepeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_time_repart, parent, false);
        return new TimeRepeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeRepeatViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class TimeRepeatViewHolder extends RecyclerView.ViewHolder {

        TextView edClo, edClo1, tvAdd;

        public TimeRepeatViewHolder(@NonNull View itemView) {
            super(itemView);
            edClo = itemView.findViewById(R.id.edClo);
            edClo1 = itemView.findViewById(R.id.edClo1);
            tvAdd = itemView.findViewById(R.id.tvAdd);

        }

        void bind(final int pos) {

            edClo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            edClo.setText(selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            });


            edClo1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            edClo1.setText(selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            });


        }
    }

}
