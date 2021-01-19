package com.nelyan.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nelyan.R;
import com.nelyan.adapter.ActivityListAdapter;
import com.nelyan.adapter.MyAddAdapter;
import com.nelyan.ui.Activity3Activity;
import com.nelyan.ui.HomeActivity;
import com.nelyan.ui.PubilerActivity;

import static com.nelyan.R.layout.customspinner;

public class MyAddFragment extends Fragment  implements AdapterView.OnItemSelectedListener{
    Context mContext;
    ImageView ivAdd,ivBack;
    Spinner orderby;
    View v;

    MyAddAdapter myAddAdapter;
    RecyclerView recyclerview;
    private MyAddFragment MyAddFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_my_add, container, false);
        mContext=getActivity();
        ivBack=v.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment f = fm.findFragmentById(R.id.frame_container);

                fm.popBackStack();
            }
        }); ivAdd=v.findViewById(R.id.ivAdd);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(getActivity(), PubilerActivity.class);
            startActivity(i);
            }
        });
       // orderby = v.findViewById(R.id.orderby);
        recyclerview=v.findViewById(R.id.recyclerview);
        myAddAdapter = new MyAddAdapter(mContext);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setAdapter(myAddAdapter) ;

      /*  final String[] genderlist = new String[]{
                "",
                "Date Added",
                "Date Added",
                "Even in City",
        };
        ArrayAdapter adapter = new ArrayAdapter(
                getActivity(), customspinner, genderlist);

// Setting Adapter to the Spinner
        orderby.setAdapter(adapter);*/

// Setting OnItemClickListener to the Spinner
//        orderby.setOnItemSelectedListener(MyAddFragment.this);


        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}