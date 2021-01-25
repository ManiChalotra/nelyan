package com.nelyan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nelyan.R;
import com.nelyan.modals.DayTimeModel;

import java.util.ArrayList;
import java.util.List;

public class DayTimeRepeatAdapter extends RecyclerView.Adapter<DayTimeRepeatAdapter.DayTimeRepeatViewHolder> {

    Context context;
    ArrayList<DayTimeModel> dayTimeModelArrayList;
    DayTimeRepeatListener dayTimeRepeatListener;

    public DayTimeRepeatAdapter(Context context,ArrayList<DayTimeModel> dayTimeModelArrayList,DayTimeRepeatListener dayTimeRepeatListener) {
        this.context = context;
        this.dayTimeModelArrayList = dayTimeModelArrayList;
        this.dayTimeRepeatListener = dayTimeRepeatListener;
    }

    @NonNull
    @Override
    public DayTimeRepeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_day_time_add_more, parent, false);
        return new DayTimeRepeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayTimeRepeatViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return dayTimeModelArrayList.size();
    }

    class DayTimeRepeatViewHolder extends RecyclerView.ViewHolder {

        Spinner daysSpinner;
        RecyclerView rvTime;
        TextView tvAddDay,tvAddTime;

        public DayTimeRepeatViewHolder(@NonNull View itemView) {
            super(itemView);
            daysSpinner = itemView.findViewById(R.id.orderby1);
            rvTime = itemView.findViewById(R.id.rvTime);
            tvAddDay = itemView.findViewById(R.id.tvAddDay);
            tvAddTime = itemView.findViewById(R.id.tvAddTime);
        }

        void bind(final int pos) {
            if (pos == dayTimeModelArrayList.size() - 1) {
                tvAddDay.setVisibility(View.VISIBLE);
            } else {
                tvAddDay.setVisibility(View.GONE);
            }

            tvAddDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dayTimeRepeatListener.dayTimeAdd(pos);
                    /*notifyDataSetChanged();*/
                }
            });

            tvAddTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dayTimeRepeatListener.timeAdd(pos);
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
            daysSpinner.setAdapter(arrayAdapter);

            TimeRepeatAdapter timeRepeatAdapter = new TimeRepeatAdapter(context, dayTimeModelArrayList.get(pos).getSelectTime());
            rvTime.setAdapter(timeRepeatAdapter);
            rvTime.setLayoutManager(new LinearLayoutManager(context));
        }
    }

    public interface DayTimeRepeatListener{
        void dayTimeAdd(int pos);
        void timeAdd(int pos);
    }
}
