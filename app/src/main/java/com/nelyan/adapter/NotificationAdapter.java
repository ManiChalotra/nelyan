package com.nelyan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nelyan.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.RecyclerViewHolder> {
    Context context;
    LayoutInflater inflater;
    RelativeLayout rl_1;
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public RecyclerViewHolder(View view) {
            super(view);

        }
    }

    public NotificationAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public NotificationAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.list_noti, parent, false);

        NotificationAdapter.RecyclerViewHolder viewHolder = new NotificationAdapter.RecyclerViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.RecyclerViewHolder holder, final int i) {

    }
    @Override
    public int getItemCount() {
        return 5;
    }


}


