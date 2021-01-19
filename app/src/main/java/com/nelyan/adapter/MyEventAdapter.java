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
import com.nelyan.fragments.ActivityFragment;
import com.nelyan.modals.EventModel;

import java.util.ArrayList;

public class MyEventAdapter extends RecyclerView.Adapter<MyEventAdapter.Vh>{
    Activity a;
    Context context;
    RelativeLayout rl_1;
    ArrayList<EventModel> datalist;
    public MyEventAdapter(FragmentActivity activity, ArrayList<EventModel> datalist) {
        this.a = activity;
        this.datalist=datalist;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(a).inflate(R.layout.row_myevent,parent,false);
        return  new Vh(v);
    }
    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        holder.eimg.setImageResource(datalist.get(position).getImg());
        holder.ename.setText(datalist.get(position).getEventName());
        holder.eloc.setText(datalist.get(position).getEventLocation());
        holder.edate.setText(datalist.get(position).getEventDate());
        holder.etime.setText(datalist.get(position).getEventTime());
        holder.etimetwo.setText(datalist.get(position).getEventTimeSecond());
        holder.eprice.setText(String.valueOf(datalist.get(position).getEventPrice()));
        holder.edesc.setText(datalist.get(position).getEventDesc());


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
        TextView ename,eloc,edate,etime,etimetwo,eprice,edesc;
        ImageView eimg;
        public Vh(@NonNull View itemView) {
            super(itemView);

            ename = itemView.findViewById(R.id.tv_eventname);
            eloc = itemView.findViewById(R.id.tv_eventloc);
            edate = itemView.findViewById(R.id.tv_eventdate);
            etime = itemView.findViewById(R.id.tv_eventtime);
            etimetwo = itemView.findViewById(R.id.tv_eventtimetwo);
            eprice = itemView.findViewById(R.id.tv_eventprice);
            edesc = itemView.findViewById(R.id.tv_eventdesc);
            eimg = itemView.findViewById(R.id.iv_eventimg);
        }
    }
}
