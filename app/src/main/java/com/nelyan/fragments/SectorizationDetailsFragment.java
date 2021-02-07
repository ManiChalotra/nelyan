package com.nelyan.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nelyan.R;
import com.nelyan.adapter.DetailsImageAdapter;
import com.nelyan.modals.DetailsImageModal;

import java.util.ArrayList;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class SectorizationDetailsFragment extends Fragment {

    Context mContext;
    View v;
    ImageView ivBack;
RecyclerView  rc,rc_detailsimg;
    ArrayList<DetailsImageModal> datalist = new ArrayList<>();
    ScrollingPagerIndicator indicator;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_sectorization_details, container, false);
        mContext = getActivity();
        ivBack=v.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment f = fm.findFragmentById(R.id.frame_container);
                fm.popBackStack();
               // getActivity().onBackPressed();
            }
        });
        rc = v.findViewById(R.id.rc_detailsimg);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rc.setLayoutManager(lm);
        indicator=v.findViewById(R.id.indicator);
        datalist.add(new DetailsImageModal(R.drawable.img_1));
        datalist.add(new DetailsImageModal(R.drawable.img_4));
        datalist.add(new DetailsImageModal(R.drawable.img_1));
        datalist.add(new DetailsImageModal(R.drawable.img_4));
        datalist.add(new DetailsImageModal(R.drawable.img_1));
        DetailsImageAdapter ad = new DetailsImageAdapter(getActivity(),datalist);
        rc.setAdapter(ad);
        indicator.attachToRecyclerView(rc);
        return v;
    }
}