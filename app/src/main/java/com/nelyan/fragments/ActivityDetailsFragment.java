package com.nelyan.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nelyan.R;
import com.nelyan.adapter.DetailsImageAdapter;
import com.nelyan.adapter.DetailsTimeAdapter;
import com.nelyan.adapter.DetailsUpcomingAdapter;
import com.nelyan.modals.DetailsImageModal;
import com.nelyan.modals.DetailsTimeModal;
import com.nelyan.ui.Chat1Activity;
import com.nelyan.ui.HomeActivity;

import java.util.ArrayList;

public class ActivityDetailsFragment extends Fragment  implements OnMapReadyCallback {
View v;
Context mContext;
TextView tvMon,tvTue,tvWed,tvThur,tvFri,tvSat,tvSun;
ImageView iv_msg,iv_back,iv_share;
    RecyclerView rc,rc_detailstime,rc_upcomingevents;
    ArrayList <DetailsImageModal> datalist = new ArrayList<>();
    ArrayList <DetailsTimeModal> datalisttime = new ArrayList<>();
    GoogleMap mMap;
Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_activity_details, container, false);
        mContext=getActivity();
        SupportMapFragment mapFragment =
                (SupportMapFragment)
                        getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    /*    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);*/
        mapFragment.getMapAsync(this);
        tvMon=v.findViewById(R.id.tvMon);
        tvTue=v.findViewById(R.id.tvTue);
        tvWed=v.findViewById(R.id.tvWed);
        tvThur=v.findViewById(R.id.tvThur);
        tvFri=v.findViewById(R.id.tvFri);
        tvSat=v.findViewById(R.id.tvSat);
        tvSun=v.findViewById(R.id.tvSun);
        tvMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMon.setBackground(getResources().getDrawable(R.drawable.selected_outlinbg));
                tvTue.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvWed.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvThur.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvFri.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSat.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSun.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
            }
        });
        tvTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMon.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvTue.setBackground(getResources().getDrawable(R.drawable.selected_outlinbg));
                tvWed.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvThur.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvFri.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSat.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSun.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
            }
        }); tvWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMon.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvTue.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvWed.setBackground(getResources().getDrawable(R.drawable.selected_outlinbg));
                tvThur.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvFri.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSat.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSun.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
            }
        });tvThur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMon.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvTue.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvWed.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvThur.setBackground(getResources().getDrawable(R.drawable.selected_outlinbg));
                tvFri.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSat.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSun.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
            }
        });tvFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMon.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvTue.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvWed.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvThur.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvFri.setBackground(getResources().getDrawable(R.drawable.selected_outlinbg));
                tvSat.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSun.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
            }
        });tvSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMon.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvTue.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvWed.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvThur.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvFri.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSat.setBackground(getResources().getDrawable(R.drawable.selected_outlinbg));
                tvSun.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
            }
        });tvSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMon.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvTue.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvWed.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvThur.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvFri.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSat.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSun.setBackground(getResources().getDrawable(R.drawable.selected_outlinbg));
            }
        });

        iv_back=v.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bundle bundle=getArguments();
                    if(bundle.getString("activity").equals("Home"))
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
        iv_share=v.findViewById(R.id.iv_share);
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogDelete();
            }
        });

        iv_msg=v.findViewById(R.id.iv_msg);
        iv_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Chat1Activity.class);
                startActivity(intent);
            }
        });
        rc = v.findViewById(R.id.rc_detailsimg);
        rc_detailstime = v.findViewById(R.id.rc_detailstime);
        rc_upcomingevents = v.findViewById(R.id.rc_upcomingevents);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager lm2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager lm3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        rc.setLayoutManager(lm);
        rc_detailstime.setLayoutManager(lm2);
        rc_upcomingevents.setLayoutManager(lm3);

        datalist.add(new DetailsImageModal(R.drawable.image1));
        datalist.add(new DetailsImageModal(R.drawable.image2));
        datalist.add(new DetailsImageModal(R.drawable.image3));
        datalist.add(new DetailsImageModal(R.drawable.image1));
        datalist.add(new DetailsImageModal(R.drawable.image2));
        datalist.add(new DetailsImageModal(R.drawable.image3));
        datalisttime.add(new DetailsTimeModal("11:00AM ","3 Years","01:00PM","5 Years"));
        datalisttime.add(new DetailsTimeModal("11:00AM ","3 Years","01:00PM","5 Years"));
        datalisttime.add(new DetailsTimeModal("11:00AM ","3 Years","01:00PM","5 Years"));
        DetailsImageAdapter ad = new DetailsImageAdapter(getActivity(),datalist);
        DetailsTimeAdapter adt = new DetailsTimeAdapter(getActivity(),datalisttime);
        DetailsUpcomingAdapter adu = new DetailsUpcomingAdapter(getActivity());
        rc.setAdapter(ad);
        rc_detailstime.setAdapter(adt);
        rc_upcomingevents.setAdapter(adu);
        return v;
    }
    public void dailogDelete(){

        dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.alert_share);
        dialog.setCancelable(true);

        LinearLayout ll_1;
        ll_1= dialog.findViewById(R.id.ll_1);
        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mContext.startActivity(new Intent(mContext, HomeActivity.class));
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng india = new LatLng(48.946697,2.153927);
        mMap.addMarker(new MarkerOptions()
                .position(india)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(india));
    }
}