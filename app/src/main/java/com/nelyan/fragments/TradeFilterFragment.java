package com.nelyan.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nelyan.R;
import com.nelyan.adapter.ActivityListAdapter;

import static com.nelyan.R.layout.customspinner;
import static com.nelyan.R.layout.size_customspinner;
import static com.nelyan.R.layout.trader_customspinner;

public class TradeFilterFragment extends Fragment implements AdapterView.OnItemSelectedListener, ActivityListAdapter.OnMyEventRecyclerViewItemClickListner {
    View v;
    Context mContext;
    private int mYear,mMonth,mDay;
    ImageView ivBack;
    TextView tvCal;
    Spinner orderby,orderby1;
    Dialog dialog;
Button btnFilter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_trade_filter, container, false); mContext = getActivity();
        ivBack=v.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment f = fm.findFragmentById(R.id.frame_container);

                fm.popBackStack();
            }
        });
        LinearLayout ll_1=v.findViewById(R.id.ll_1);
        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogLocation();
            }
        });

        btnFilter=v.findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
     /*   tvCal=v.findViewById(R.id.tvCal);
        tvCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog();
            }
        });*/
        orderby=v.findViewById(R.id.orderby);
        final String[] genderlist = new String[]{
                "",
                "Sport","Cultural","Languages",

        };
        ArrayAdapter adapter = new ArrayAdapter(
                getActivity(), customspinner, genderlist);
        orderby.setAdapter(adapter);
        orderby.setOnItemSelectedListener(TradeFilterFragment.this);
        orderby1=v.findViewById(R.id.orderby1);
        final String[] km = new String[]{
              "",  "OKM","5KM","10KM","15KM","20KM",
                "25KM","30KM"

        };
        ArrayAdapter adapter1 = new ArrayAdapter(
                getActivity(), size_customspinner, km);
        orderby1.setAdapter(adapter1);
        orderby1.setOnItemSelectedListener(TradeFilterFragment.this);

        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onMyEventItemClickListner() {

    }
   /* private void dateDialog(){
        DatePickerDialog.OnDateSetListener listener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvCal.setText(dayOfMonth + "/" + (monthOfYear+1)+"/"+year);
            }};
        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(),R.style.datepicker,listener,mYear,mMonth,mDay);
        datePickerDialog.show();
    }*/
   public void dailogLocation(){

       dialog = new Dialog(mContext);
       dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
       dialog.setContentView(R.layout.alert_location);
       dialog.setCancelable(true);

       RelativeLayout rlYes,rlNo;
       rlNo= dialog.findViewById(R.id.rlNo);
       rlNo.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
               dialog.dismiss();
           }
       }); rlYes= dialog.findViewById(R.id.rlYes);
       rlYes.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
           }
       });
       dialog.show();
   }
}