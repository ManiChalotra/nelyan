package com.nelyan.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nelyan.R;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {
    Context context;

    private LayoutInflater inflater;

    public ItemsAdapter(Context applicationContext) {
        this.context = applicationContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.res, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

    }

    // Return the type of your itemsData (invoked by the layout manager)

    @Override
    public int getItemCount() {

        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
