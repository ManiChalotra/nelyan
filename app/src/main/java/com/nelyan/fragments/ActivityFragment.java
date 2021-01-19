package com.nelyan.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nelyan.AppUtils;
import com.nelyan.R;
import com.nelyan.adapter.ActivityListAdapter;
import com.nelyan.ui.Activity2Activity;
import com.nelyan.ui.HomeActivity;

import java.lang.reflect.Field;
import java.util.Calendar;

import static com.nelyan.R.layout.customspinner;
import static com.nelyan.R.layout.size_customspinner;

public class ActivityFragment extends Fragment  implements AdapterView.OnItemSelectedListener, ActivityListAdapter.OnMyEventRecyclerViewItemClickListner {
View v;
Context mContext;
Button btnFilter;
TextView tvCal;
Dialog dialog;
ImageView ivBack;
LinearLayout ll_1;
    Spinner orderby,orderby1;
    String[] country = {"Type of activity", "My activities", };
    private int mYear,mMonth,mDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_activity, container, false);
        mContext=getActivity();
        ivBack=v.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             // getActivity().onBackPressed();

try {
    Bundle bundle=getArguments();
    if(bundle.getString("unamea").equals("Homeactivity"))
    {
        getActivity().onBackPressed();

    }else
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.frame_container);
        fm.popBackStack();
    }
}catch (Exception e)
{
    FragmentManager fm = getActivity().getSupportFragmentManager();
    Fragment f = fm.findFragmentById(R.id.frame_container);
    fm.popBackStack();
}

            }
        });
        final Calendar c= Calendar.getInstance();
        mYear=c.get(Calendar.YEAR);
        mMonth=c.get(Calendar.MONTH);
        mDay=c.get(Calendar.DAY_OF_MONTH);
        tvCal=v.findViewById(R.id.tvCal);
        tvCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog();
            }
        });
        ll_1=v.findViewById(R.id.ll_1);
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
            /*    FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment f = fm.findFragmentById(R.id.frame_container);
                fm.popBackStack();*/
            }
        });
        orderby=v.findViewById(R.id.orderby);
        final String[] genderlist = new String[]{
                "",
                "Sport","Cultural","Languages",

        };
        ArrayAdapter adapter = new ArrayAdapter(
                getActivity(), customspinner, genderlist);
        orderby.setAdapter(adapter);
        orderby.setOnItemSelectedListener(ActivityFragment.this);
        orderby1=v.findViewById(R.id.orderby1);


        final String[] km = new String[]{
               "", "OKM",
                "1KM","2KM","3KM","4KM","5KM","6KM","7KM","8KM","9KM","10KM", "11KM",
                "12KM","13KM","14KM","15KM","16KM","17KM","18KM","19KM","20KM","21KM",
                "22KM","23KM","24KM","25KM","26KM","27KM","28KM","29KM","30KM"
        };
        ArrayAdapter adapter1 = new ArrayAdapter(
                getActivity(), size_customspinner, km);

        orderby1.setAdapter(adapter1);
        /*try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            ListPopupWindow popupWindow = (ListPopupWindow) popup.get(orderby1);
            popupWindow.setHeight(50);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            Log.e("sjhj","jsbhj"+e);
        }*/

        orderby1.setOnItemSelectedListener(ActivityFragment.this);
        return v;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onMyEventItemClickListner() { }
    private void dateDialog(){
        DatePickerDialog.OnDateSetListener listener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvCal.setText(dayOfMonth + "/" + (monthOfYear+1)+"/"+year);
            }};
        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(),R.style.datepicker,listener,mYear,mMonth,mDay);
        datePickerDialog.show();
        }
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
