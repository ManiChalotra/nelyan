package com.nelyan.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nelyan.AppUtils;
import com.nelyan.R;
import com.nelyan.fragments.ActivityFragment;
import com.nelyan.fragments.TraderPublishFragment;

public class TraderPreviewActivity extends AppCompatActivity implements OnMapReadyCallback {
    Context mContext;
    ImageView ivBack;
    Button btnModify,btnPublish;
    Dialog dialog;
    GoogleMap mMap;
    ImageView ivShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trader_preview);
        mContext=this;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ivBack=findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }); ivShare=findViewById(R.id.ivShare);
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogShare();
            }
        }); btnModify=findViewById(R.id.btnModify);
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });btnPublish=findViewById(R.id.btnPublish);
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  AppUtils.gotoFragment(mContext, new TraderPublishFragment(), R.id.frame_container, false);
                Intent i = new Intent(TraderPreviewActivity.this,HomeActivity.class);
               i.putExtra("activity","tarfrag");
                startActivity(i);
            }
        });
    }
    public void dailogShare(){

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