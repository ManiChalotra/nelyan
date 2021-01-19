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

public class SectorizationDetailsFragment extends Fragment {

    Context mContext;
    View v;
    ImageView ivBack;
RecyclerView  rc,rc_detailsimg;
    ArrayList<DetailsImageModal> datalist = new ArrayList<>();
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
            }
        });
        rc = v.findViewById(R.id.rc_detailsimg);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rc.setLayoutManager(lm);
        datalist.add(new DetailsImageModal(R.drawable.image1));
        datalist.add(new DetailsImageModal(R.drawable.image2));
        datalist.add(new DetailsImageModal(R.drawable.image3));
        datalist.add(new DetailsImageModal(R.drawable.image1));
        datalist.add(new DetailsImageModal(R.drawable.image2));
        datalist.add(new DetailsImageModal(R.drawable.image3));
        DetailsImageAdapter ad = new DetailsImageAdapter(getActivity(),datalist);
        rc.setAdapter(ad);
        return v;
    }
}