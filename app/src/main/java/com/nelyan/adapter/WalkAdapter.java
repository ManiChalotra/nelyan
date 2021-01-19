package com.nelyan.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.nelyan.AppUtils;
import com.nelyan.R;
import com.nelyan.fragments.ActivityDetailsFragment;
import com.nelyan.modals.EventModel;
import com.nelyan.modals.WalkthroughModal;

import java.util.ArrayList;

public class WalkAdapter extends RecyclerView.Adapter<WalkAdapter.Vh>{
    Activity a;
    Context context;
    RelativeLayout rl_1;
    ArrayList<WalkthroughModal> datalist;
    public WalkAdapter(FragmentActivity activity, ArrayList<WalkthroughModal> datalist) {
        this.a = activity;
        this.datalist=datalist;
    }

    @NonNull
    @Override
    public WalkAdapter.Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(a).inflate(R.layout.row_myevent,parent,false);
        return  new WalkAdapter.Vh(v);
    }
    @Override
    public void onBindViewHolder(@NonNull WalkAdapter.Vh holder, int position) {
        holder.img.setImageResource(datalist.get(position).getImagslideid());
        holder.nic.setText(datalist.get(position).getTv_walk());
        holder.big.setText(datalist.get(position).getTv_walkdesc());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.gotoFragment(a, new ActivityDetailsFragment(), R.id.frame_container, false);


            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class Vh extends RecyclerView.ViewHolder {
        TextView nic,big;
        ImageView img;
        public Vh(@NonNull View itemView) {
            super(itemView);

            nic = itemView.findViewById(R.id.tv_walk);
            big = itemView.findViewById(R.id.tv_walkdesc);

            img = itemView.findViewById(R.id.iv_eventimg);
        }
    }
}

