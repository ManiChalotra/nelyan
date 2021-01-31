package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.nelyan.fragments.ActivityDetailsFragment;
import com.nelyan.modals.DetailsImageModal;
import com.nelyan.modals.DetailsTimeModal;

import java.util.ArrayList;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    Context mContext;
    Button btnModify,btnPublish;
    ImageView iv_msg,iv_back,iv_share;
    GoogleMap mMap;
    RecyclerView rc,rc_detailstime,rc_upcomingevents;
    ArrayList<DetailsImageModal> datalist = new ArrayList<>();
    ArrayList <DetailsTimeModal> datalisttime = new ArrayList<>();
    Dialog dialog;
    ScrollingPagerIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mContext=this;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        iv_back=findViewById(R.id.iv_back); btnModify=findViewById(R.id.btnModify); btnPublish=findViewById(R.id.btnPublish);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }); btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(mContext, HomeActivity.class);
                 i.putExtra("activity","acti");
                startActivity(i);
            }
        });
        iv_share=findViewById(R.id.iv_share);
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogDelete();
            }
        });

        iv_msg=findViewById(R.id.iv_msg);
        iv_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, Chat1Activity.class);
                startActivity(intent);
            }
        });
        rc = findViewById(R.id.rc_detailsimg);
        rc_detailstime =findViewById(R.id.rc_detailstime);
        rc_upcomingevents = findViewById(R.id.rc_upcomingevents);
        LinearLayoutManager lm = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager lm2 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager lm3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rc.setLayoutManager(lm);
        rc_detailstime.setLayoutManager(lm2);
        rc_upcomingevents.setLayoutManager(lm3);
        indicator=findViewById(R.id.indicator);
        datalist.add(new DetailsImageModal(R.drawable.img_1));
        datalist.add(new DetailsImageModal(R.drawable.img_4));
        datalist.add(new DetailsImageModal(R.drawable.img_1));
        datalist.add(new DetailsImageModal(R.drawable.img_4));
        datalist.add(new DetailsImageModal(R.drawable.img_1));
        datalisttime.add(new DetailsTimeModal("11:00AM ","3 Years","01:00PM","5 Years"));
        datalisttime.add(new DetailsTimeModal("11:00AM ","3 Years","01:00PM","5 Years"));
        datalisttime.add(new DetailsTimeModal("11:00AM ","3 Years","01:00PM","5 Years"));
        DetailsImageAdapter ad = new DetailsImageAdapter(this,datalist);
        DetailsTimeAdapter adt = new DetailsTimeAdapter(this,datalisttime);
        DetailsUpcomingAdapter adu = new DetailsUpcomingAdapter(this);
        rc.setAdapter(ad);
        indicator.attachToRecyclerView(rc);
        rc_detailstime.setAdapter(adt);
        rc_upcomingevents.setAdapter(adu);
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