package com.nelyan.fragments;

import android.app.Dialog;
import android.content.Context;
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
import com.nelyan.fragments.NurserieFragment;
import com.nelyan.modals.DetailsImageModal;

import java.util.ArrayList;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class MaternalFragmentFragment extends Fragment  implements OnMapReadyCallback {
    Context mContext;
    View v;
    ImageView ivBack;
    Button btnModify,btnPublish;
    TextView tvTitle;
    LinearLayout ll_btns;
    GoogleMap mMap;
    ScrollingPagerIndicator indicator;
    RecyclerView rc;
    ArrayList <DetailsImageModal> datalist = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v= inflater.inflate(R.layout.fragment_maternal_fragment, container, false);
        mContext=getActivity();
        SupportMapFragment mapFragment =
                (SupportMapFragment)
                        getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ivBack=v.findViewById(R.id.ivBack);
        ll_btns=v.findViewById(R.id.ll_btns);
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
                ll_btns.setVisibility(View.GONE);
              //  AppUtils.gotoFragment(mContext, new NurserieFragment(), R.id.frame_container, false);
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