package com.nelyan.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.nelyan.R;
public class DetailsUpcomingAdapter extends RecyclerView.Adapter {
    Activity a;

    public DetailsUpcomingAdapter(FragmentActivity activityDetailsActivity) {
        this.a=activityDetailsActivity;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public RecyclerViewHolder(View view) {
            super(view);

        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(a).inflate(R.layout.row_upcoming_details,parent,false);
        DetailsUpcomingAdapter.RecyclerViewHolder viewHolder = new DetailsUpcomingAdapter.RecyclerViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}


