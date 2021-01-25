package com.nelyan.fragments;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.nelyan.R;
import com.nelyan.adapter.ActivityListAdapter;

import static com.nelyan.R.layout.customspinner;
import static com.nelyan.R.layout.row_spinner_custom;
import static com.nelyan.R.layout.size_customspinner;


public class ChildCareFragment extends Fragment  implements AdapterView.OnItemSelectedListener, ActivityListAdapter.OnMyEventRecyclerViewItemClickListner {
View v;
Context mContext;
ImageView ivBack,iv_1,iv_2,iv_3;
LinearLayout ll_1,ll_2,ll_3,ll_0;
Button btnSearch;
Dialog dialog;
Spinner orderby1,spin1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_child_care, container, false);
       mContext=getActivity();
        ivBack=v.findViewById(R.id.ivBack); ll_0=v.findViewById(R.id.ll_0);
        ll_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogLocation();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Bundle bundle=getArguments();
                    if(bundle.getString("child").equals("childcare"))
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
        spin1=v.findViewById(R.id.spin1);
       btnSearch=v.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               FragmentManager fm = getActivity().getSupportFragmentManager();
               Fragment f = fm.findFragmentById(R.id.frame_container);
               fm.popBackStack();
           }
       });


        orderby1=v.findViewById(R.id.orderby1);
        final String[] km = new String[]{
            "",    "OKM","5KM","10KM","15KM","20KM", "25KM","30KM"

        };
        ArrayAdapter adapter1 = new ArrayAdapter(
                getActivity(), size_customspinner, km);
        orderby1.setAdapter(adapter1);
        orderby1.setOnItemSelectedListener(ChildCareFragment.this);


        spin1=v.findViewById(R.id.spin1);
        final String[] spi = new String[]{
                "",  "crèche",
                "maternal assistant ","babysitter",};
        ArrayAdapter adapter0 = new ArrayAdapter(
                getActivity(), customspinner, spi);
        spin1.setAdapter(adapter0);
        spin1.setOnItemSelectedListener(ChildCareFragment.this);
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