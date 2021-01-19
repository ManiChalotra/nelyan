package com.nelyan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.nelyan.AppUtils;
import com.nelyan.R;
import com.nelyan.fragments.ActivityFragment;
import com.nelyan.fragments.ActivityListFragment;
import com.nelyan.fragments.ChatListFragment;
import com.nelyan.fragments.TraderListingFragment;
import com.nelyan.modals.HomeModal;
import com.nelyan.ui.SectorizationActivity;

import java.util.ArrayList;

public class MyHomeAdapter extends  RecyclerView.Adapter<MyHomeAdapter.Vh>{
    Activity a;
    Context context;
    LinearLayout ll1;
    ArrayList<HomeModal> datalist;
    public MyHomeAdapter(FragmentActivity activity, ArrayList<HomeModal> datalist) {

        this.a=activity;
        this.datalist=datalist;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(a).inflate(R.layout.row_home,parent,false);
        return  new Vh(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, final int position) {

        holder.img.setImageResource(datalist.get(position).getImg());
        holder.text.setText(datalist.get(position).getTask());

        holder.ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0)
                {
                    AppUtils.gotoFragment(a, new ActivityListFragment(), R.id.frame_container, false);
                } else if (position==1){
                    AppUtils.gotoFragment(a, new ChatListFragment(), R.id.frame_container, false);
                }else if (position==2){
                    Intent i=new Intent(a, SectorizationActivity.class);
                    a.startActivity(i);

                }else if (position==3){
                  AppUtils.gotoFragment(a, new TraderListingFragment(), R.id.frame_container, false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class  Vh extends RecyclerView.ViewHolder {
        ImageView img;
        LinearLayout ll1;
        TextView text;
        public Vh(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.iv_homeimg);
            ll1 = itemView.findViewById(R.id.ll1);
            text = itemView.findViewById(R.id.tv_hometxt);
        }
    }

}

