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

public class AgeGroupRepeatAdapter extends RecyclerView.Adapter<AgeGroupRepeatAdapter.AgeGroupRepeatViewHolder> {

    Context context;
    int returnItemView = 1;

    public AgeGroupRepeatAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public AgeGroupRepeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_time_add_more, parent, false);
        return new AgeGroupRepeatViewHolder(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull AgeGroupRepeatViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return returnItemView;
    }


    class AgeGroupRepeatViewHolder extends RecyclerView.ViewHolder {

        EditText edtAgeFrom, edtAgeTo;
        Spinner orderby1;
        TextView edClo, edClo1, tvAddMore;

        public AgeGroupRepeatViewHolder(@NonNull View itemView) {
            super(itemView);
            edtAgeFrom = itemView.findViewById(R.id.edtAgeFrom);
            edtAgeTo = itemView.findViewById(R.id.edtAgeTo);
            orderby1 = itemView.findViewById(R.id.orderby1);
            edClo = itemView.findViewById(R.id.edClo);
            edClo1 = itemView.findViewById(R.id.edClo1);
            tvAddMore = itemView.findViewById(R.id.tvAddMore);

        }

        void bind(int pos) {

            if (pos == returnItemView - 1) {
                tvAddMore.setVisibility(View.VISIBLE);
            } else {
                tvAddMore.setVisibility(View.GONE);
            }

            tvAddMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    returnItemView = returnItemView + 1;
                    notifyDataSetChanged();
                }
            });

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

            List<String> days = new ArrayList<>();
            days.add("");
            days.add("Monday");
            days.add("Tuesday");
            days.add("Wednesday");
            days.add("Thursday");
            days.add("Friday");
            days.add("Saturday");
            days.add("Sunday");
            ArrayAdapter arrayAdapter = new ArrayAdapter(context, R.layout.customspinner, days);
            orderby1.setAdapter(arrayAdapter);
        }
    }
}

