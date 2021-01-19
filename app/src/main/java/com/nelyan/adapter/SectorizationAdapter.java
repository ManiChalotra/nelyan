package com.nelyan.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.nelyan.AppUtils;
import com.nelyan.R;
import com.nelyan.fragments.NurserieFragment;
import com.nelyan.fragments.SectorizationDetailsFragment;

public class SectorizationAdapter  extends RecyclerView.Adapter<SectorizationAdapter.RecyclerViewHolder> {
    Context context;
    Activity a;
    LayoutInflater inflater;
    RelativeLayout rl_1;
    ImageView iv_fev;
    public SectorizationAdapter(FragmentActivity activity) {
        this.a=activity; }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public RecyclerViewHolder(View view) {
            super(view);
        }
    }
    @Override
    public SectorizationAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(a).inflate(R.layout.list_sectorization,parent,false);
        //     View v = inflater.inflate(R.layout.list_activitylist, parent, false);
        rl_1=v.findViewById(R.id.rl_1);
        rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.gotoFragment(a, new SectorizationDetailsFragment(), R.id.frame_container, false);
            }
        });
        SectorizationAdapter.RecyclerViewHolder viewHolder = new SectorizationAdapter.RecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SectorizationAdapter.RecyclerViewHolder holder, int position) {

    }
    @Override
    public int getItemCount() {
        return 2;
    }


}




