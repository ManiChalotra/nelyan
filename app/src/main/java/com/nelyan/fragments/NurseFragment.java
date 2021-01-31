package com.nelyan.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nelyan.AppUtils;
import com.nelyan.R;
import com.nelyan.adapter.DetailsImageAdapter;
import com.nelyan.modals.DetailsImageModal;
import com.nelyan.ui.Chat1Activity;
import com.nelyan.ui.HomeActivity;

import java.util.ArrayList;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class NurseFragment extends Fragment implements OnMapReadyCallback {

    Context mContext;
    View v;
    RecyclerView rc, rc_detailsimg;
    ImageView iv_msg,ivBack,ivShare;
    Button btnModify,btnPublish;
    Dialog dialog;
    GoogleMap mMap;
    TextView tvTitle;
    ScrollingPagerIndicator indicator;

    ArrayList<DetailsImageModal> datalist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v= inflater.inflate(R.layout.fragment_nurse, container, false);
        mContext=getActivity();
      /*  Bundle bundle = getArguments();
        String message = bundle.getString("message");
        tvTitle.setText(message);*/
        SupportMapFragment mapFragment =
                (SupportMapFragment)
                        getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ivBack=v.findViewById(R.id.ivBack);
        tvTitle=v.findViewById(R.id.tvTitle);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        btnModify = v.findViewById(R.id.btnModify);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        btnPublish = v.findViewById(R.id.btnPublish);
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUtils.gotoFragment(mContext, new NurserieFragment(), R.id.frame_container, false);
            }
        });
        ivShare=v.findViewById(R.id.ivShare);
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogDelete();
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