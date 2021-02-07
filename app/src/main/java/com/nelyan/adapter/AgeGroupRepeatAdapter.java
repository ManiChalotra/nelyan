package com.nelyan.adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nelyan.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AgeGroupRepeatAdapter extends RecyclerView.Adapter<AgeGroupRepeatAdapter.AgeGroupRepeatViewHolder> {

    Context context;
    int returnItemView = 1;
    List<String> days;
    HashMap<String, String> Selectedmonth=new HashMap<>();
    HashMap<String, String> SelectededtAgeFrom=new HashMap<>();
    HashMap<String, String> SelectededtAgeTo=new HashMap<>();
    HashMap<String, String> SelectedTIMEedClo=new HashMap<>();
    HashMap<String, String> SelectedTIMEedClo1=new HashMap<>();

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

    class AgeGroupRepeatViewHolder extends RecyclerView.ViewHolder
    {
        EditText edtAgeFrom, edtAgeTo;
        Spinner orderby1;
        TextView edClo, edClo1, tvAddMore;
        public AgeGroupRepeatViewHolder(@NonNull View itemView)
        {
            super(itemView);
            edtAgeFrom = itemView.findViewById(R.id.edtAgeFrom);
            edtAgeTo = itemView.findViewById(R.id.edtAgeTo);
            orderby1 = itemView.findViewById(R.id.orderby1);
            edClo = itemView.findViewById(R.id.edClo);
            edClo1 = itemView.findViewById(R.id.edClo1);
            tvAddMore = itemView.findViewById(R.id.tvAddMore);
        }

        void bind(final int pos) {
            if (pos == returnItemView - 1)
            {
                tvAddMore.setVisibility(View.VISIBLE);
            }
            else
            {
                tvAddMore.setVisibility(View.GONE);
            }


            try {
                edtAgeFrom.setText(SelectededtAgeFrom.get(String.valueOf(pos)));
                edtAgeTo.setText(SelectededtAgeTo.get(String.valueOf(pos)));
                edClo.setText(SelectedTIMEedClo.get(String.valueOf(pos)));
                edClo1.setText(SelectedTIMEedClo1.get(String.valueOf(pos)));
            }
            catch (Exception e)
            {

            }


            tvAddMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideKeyboard(context,view);
                    returnItemView = returnItemView + 1;
                    SelectededtAgeFrom.put(String.valueOf(pos),edtAgeFrom.getText().toString());
                    SelectededtAgeTo.put(String.valueOf(pos),edtAgeTo.getText().toString());
                    SelectedTIMEedClo.put(String.valueOf(pos),edClo.getText().toString());
                    SelectedTIMEedClo1.put(String.valueOf(pos),edClo1.getText().toString());
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

            days = new ArrayList<>();
            days.add("");
            days.add("Monday");
            days.add("Tuesday");
            days.add("Wednesday");
            days.add("Thursday");
            days.add("Friday");
            days.add("Saturday");
            days.add("Sunday");
            final ArrayAdapter<String> modeAdaptercity = new ArrayAdapter<String>(context, R.layout.customspinner, days);
            orderby1.setAdapter(modeAdaptercity);
            try
            {
                orderby1.setSelection(Integer.parseInt(Selectedmonth.get(String.valueOf(pos))));
            }
            catch (Exception e)
            {
            }


            orderby1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int jj, long l) {
                    hideKeyboard(context,view);
                    Selectedmonth.put(String.valueOf(pos),String.valueOf(jj));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }
    public static void hideKeyboard(Context context,View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
