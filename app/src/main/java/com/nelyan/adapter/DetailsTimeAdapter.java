package com.nelyan.adapter;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.nelyan.R;
import com.nelyan.modals.DetailsTimeModal;

import java.util.ArrayList;

public class DetailsTimeAdapter extends RecyclerView.Adapter<DetailsTimeAdapter.Vh> {
    Activity a;
    ArrayList<DetailsTimeModal> datalisttime;
    public DetailsTimeAdapter(FragmentActivity activityDetailsActivity, ArrayList<DetailsTimeModal> datalisttime) {
        this.a=activityDetailsActivity;
        this.datalisttime=datalisttime;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(a).inflate(R.layout.row_activtydetail_time,parent,false);
        return new Vh(v);

    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        holder.t1.setText(datalisttime.get(position).getTime());
        holder.t2.setText(datalisttime.get(position).getYear());
        holder.t3.setText(datalisttime.get(position).getTimeone());
        holder.t4.setText(datalisttime.get(position).getYearone());
    }

    @Override
    public int getItemCount() {
        return datalisttime.size();
    }


    class  Vh extends RecyclerView.ViewHolder {
        TextView t1,t2,t3,t4;
        public Vh(@NonNull View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.text1);
            t2=itemView.findViewById(R.id.text2);
            t3=itemView.findViewById(R.id.text3);
            t4=itemView.findViewById(R.id.text4);
        }
    }
}