package com.nelyan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.nelyan.AppUtils;
import com.nelyan.R;
import com.nelyan.fragments.ActivityDetailsFragment;
import com.nelyan.fragments.ActivityFragment;
import com.nelyan.fragments.ActivityListFragment;
import com.nelyan.fragments.FavoriteFragment;

public class ActivityListAdapter  extends RecyclerView.Adapter<ActivityListAdapter.RecyclerViewHolder> {
    Context context;
    Activity a;
    RelativeLayout rl_1;
    ImageView image,iv_fev;
    OnMyEventRecyclerViewItemClickListner listner;
//    public ActivityListAdapter( Context context) {
//        this.context = context;
//        inflater = LayoutInflater.from(context);
//    }

    public ActivityListAdapter(FragmentActivity activity,OnMyEventRecyclerViewItemClickListner listing) {
        this.a=activity;
        this.listner=listing;

    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public RecyclerViewHolder(View view) {
            super(view);

        }
    }


    @Override
    public ActivityListAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(a).inflate(R.layout.list_activitylist,parent,false);
   //     View v = inflater.inflate(R.layout.list_activitylist, parent, false);
        rl_1=v.findViewById(R.id.rl_1);
        rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AppUtils.gotoFragment(a, new ActivityDetailsFragment(), R.id.frame_container, false);
            }
        });
        iv_fev=v.findViewById(R.id.iv_fev);
        iv_fev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AppUtils.gotoFragment(a, new FavoriteFragment(), R.id.frame_container, false);
            }
        });
        ActivityListAdapter.RecyclerViewHolder viewHolder = new ActivityListAdapter.RecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityListAdapter.RecyclerViewHolder holder, int position) {

    }
    @Override
    public int getItemCount() {
        return 3;
    }

     public interface  OnMyEventRecyclerViewItemClickListner{
    void onMyEventItemClickListner();
}

}



