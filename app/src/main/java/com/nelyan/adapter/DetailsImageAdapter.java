package com.nelyan.adapter;

import com.nelyan.R;
import com.nelyan.modals.DetailsImageModal;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class DetailsImageAdapter extends RecyclerView.Adapter<DetailsImageAdapter.Vh> {
    Activity a;
    ArrayList<DetailsImageModal> datalist;

    public DetailsImageAdapter(FragmentActivity activityDetailsActivity, ArrayList<DetailsImageModal> datalist) {
        this.a=activityDetailsActivity;
        this.datalist=datalist;

    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(a).inflate(R.layout.row_detailsimg,parent,false);
        return new Vh(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {

        holder.img.setImageResource(datalist.get(position).getImg());
        if(position%2==1)
        {
            holder.videoPic.setVisibility(View.VISIBLE);
        }
        else{
            holder.videoPic.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class  Vh extends RecyclerView.ViewHolder {
        ImageView img,videoPic;
        public Vh(@NonNull View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.detailsimg);
            videoPic= itemView.findViewById(R.id.iv_videoicon);
        }
    }
}
