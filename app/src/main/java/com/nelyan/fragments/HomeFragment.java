package com.nelyan.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nelyan.R;
import com.nelyan.adapter.MyHomeAdapter;
import com.nelyan.modals.HomeModal;
import com.nelyan.ui.NotificationActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
Context mContext;
ImageView iv_bell;
    ArrayList<HomeModal> datalist = new ArrayList<>();
    RecyclerView recyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mContext=getActivity();
        iv_bell= root.findViewById(R.id.iv_bell);
        iv_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), NotificationActivity.class);
                startActivity(i);
            }
        });


        recyclerView = root.findViewById(R.id.rc_home);
        LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(lm);
        datalist.clear();

        datalist.add(new HomeModal(R.drawable.banner_img,"Les activités dans ma ville"));
        datalist.add(new HomeModal(R.drawable.banner_img_2,"La garde d'enfants"));
        datalist.add(new HomeModal(R.drawable.banner_img_3,"Les écoles de mon enfant"));
        datalist.add(new HomeModal(R.drawable.service_4,"Les commerçants dans ma ville"));

        MyHomeAdapter ad = new MyHomeAdapter(getActivity(),datalist);

        recyclerView.setAdapter(ad);

        return root;
    }
}