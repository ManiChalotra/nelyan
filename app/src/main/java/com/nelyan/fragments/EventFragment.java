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
import static com.nelyan.R.layout.customspinner;

import com.nelyan.AppUtils;
import com.nelyan.R;
import com.nelyan.adapter.MyEventAdapter;
import com.nelyan.ui.Activity2Activity;
import com.nelyan.modals.EventModel;

import java.util.ArrayList;

    public class EventFragment extends Fragment implements AdapterView.OnItemSelectedListener {
        Context mContext;
        ImageView ivLocation, ivBack;
        TextView title,tvFilter;
        View v;

        Spinner orderby;
        RecyclerView rc;
        ArrayList<EventModel> datalist = new ArrayList<>();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            v = inflater.inflate(R.layout.fragment_event, container, false);
            mContext = getActivity();
            ivBack=v.findViewById(R.id.ivBack);
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    Fragment f = fm.findFragmentById(R.id.frame_container);

                    fm.popBackStack();
                }
            });
            ivLocation = v.findViewById(R.id.iv_bell);

            orderby = v.findViewById(R.id.orderby);
            rc = v.findViewById(R.id.rc_event);
            tvFilter=v.findViewById(R.id.tvFilter);
            tvFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppUtils.gotoFragment(mContext, new ActivityFragment(), R.id.frame_container, false);

                }
            });
            ivBack.setVisibility(View.VISIBLE);
//            title.setVisibility(View.VISIBLE);
            ivLocation.setVisibility(View.VISIBLE);
            ivLocation.setImageResource(R.drawable.location_circle);

            LinearLayoutManager LM = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rc.setLayoutManager(LM);
            datalist.clear();
            datalist.add(new EventModel(R.drawable.img_1, "Danse", "Marechal 20KM", "10.12.2020 To 11.12.2020", "11:00 AM 3 Year", "01:00 PM 5 Year", 450.80, "During the summer,many cities show movies in the park. Bring a blanket, snacks, a light sweater, and enjoy the magic of cinema"));
            datalist.add(new EventModel(R.drawable.img_1, "Danse", "Marechal 20KM", "10.12.2020 To 11.12.2020", "11:00 AM 3 Year", "01:00 PM 5 Year", 450.80, "During the summer,many cities show movies in the park. Bring a blanket, snacks, a light sweater, and enjoy the magic of cinema"));


            MyEventAdapter ad = new MyEventAdapter(getActivity(), datalist);
            //rc.setAdapter(null);

            rc.setAdapter(ad);



            ivLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(getActivity(),Activity2Activity.class);
                    intent.putExtra("event","activity");
                    startActivity(intent);
                    /*Intent i= new Intent(getActivity(), Activity2Activity.class);
                    startActivity(i);*/
                }
            });

            final String[] genderlist = new String[]{
                    "","Events in City",
                    "Date Added",
                    "Distance",
            };
            ArrayAdapter adapter = new ArrayAdapter(
                    getActivity(), customspinner, genderlist);

// Setting Adapter to the Spinner
            orderby.setAdapter(adapter);

// Setting OnItemClickListener to the Spinner
            orderby.setOnItemSelectedListener(EventFragment.this);

            return v;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }