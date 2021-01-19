package com.nelyan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.nelyan.AppUtils;
import com.nelyan.R;
import com.nelyan.fragments.ActivityDetailsFragment;
import com.nelyan.fragments.NurserieFragment;

public class favoriteAdapter extends RecyclerView.Adapter<favoriteAdapter.RecyclerViewHolder> {
    Context context;
    Activity a;
    LayoutInflater inflater;
    RelativeLayout rl_1;
    ImageView iv_fev;

    public favoriteAdapter(FragmentActivity activity) {
        this.a = activity;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_fev,iv_unfev;
        public RecyclerViewHolder(View view) {

            super(view);
            iv_fev=view.findViewById(R.id.iv_fev);
            iv_unfev=view.findViewById(R.id.iv_unfev);

        }

    }

    @Override
    public favoriteAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(a).inflate(R.layout.list_favorite, parent, false);
        //     View v = inflater.inflate(R.layout.list_activitylist, parent, false);
        rl_1 = v.findViewById(R.id.rl_1);
        rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.gotoFragment(a, new NurserieFragment(), R.id.frame_container, false);
            }
        });
        favoriteAdapter.RecyclerViewHolder viewHolder = new favoriteAdapter.RecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final favoriteAdapter.RecyclerViewHolder holder, int position) {
        holder.iv_fev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder. iv_fev.setVisibility(View.GONE);
                holder. iv_unfev.setVisibility(View.VISIBLE);
            }
        }); holder.iv_unfev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder. iv_fev.setVisibility(View.VISIBLE);
                holder. iv_unfev.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }


}



