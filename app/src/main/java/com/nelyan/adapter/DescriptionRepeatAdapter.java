package com.nelyan.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nelyan.R;
import com.nelyan.ui.TraderActivity;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class DescriptionRepeatAdapter extends RecyclerView.Adapter<DescriptionRepeatAdapter.DescriptionRepeatViewHolder> {

    Context context;
    int returnItemView = 1;
    TraderActivity descriptionRepeatListener;
    String selectedImage ="";
    File file;
    HashMap<String, Bitmap> image;
    ArrayList<AlbumFile> mAlbumFiles = new ArrayList<AlbumFile>();

    public DescriptionRepeatAdapter(Context context,HashMap<String, Bitmap>image,TraderActivity descriptionRepeatListener,int returnItemView) {
        this.context = context;
        this.image = image;
        this.returnItemView = returnItemView;
        this.descriptionRepeatListener = descriptionRepeatListener;
    }

    @NonNull
    @Override
    public DescriptionRepeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_description_repeat, parent, false);
        return new DescriptionRepeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DescriptionRepeatViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return returnItemView;
    }


    class DescriptionRepeatViewHolder extends RecyclerView.ViewHolder {

        TextView tvAdd;
        EditText  edtDesc,edtProductTitle,edtProductPrice;

        ImageView ivEvent,ivCam;
        public DescriptionRepeatViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAdd = itemView.findViewById(R.id.tvAdd);
            edtDesc = itemView.findViewById(R.id.edtDesc);
            edtProductPrice = itemView.findViewById(R.id.edtProductPrice);
            edtProductTitle = itemView.findViewById(R.id.edtProductTitle);
            ivEvent = itemView.findViewById(R.id.ivProductImage);
            ivCam = itemView.findViewById(R.id.ivCam);
        }

        void bind(final int pos) {

            if (pos == returnItemView - 1) {
                tvAdd.setVisibility(View.VISIBLE);
            } else {
                tvAdd.setVisibility(View.GONE);
            }
            try {
                ivEvent.setImageBitmap(image.get(String.valueOf(pos)));
                // ivCam.setVisibility(View.GONE);
                Log.e("kmdkmdkedcmk","ss c--"+pos+"   "+image.get("0"));
            }
            catch (Exception e)
            {
            }
            ivEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  //  descriptionRepeatListener.productImageClick(pos);
                    descriptionRepeatListener.imageClick( pos,returnItemView);
                  //  descriptionRepeatListener.imageClick( pos,returnItemView);
                }
            });

            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    returnItemView = returnItemView + 1;
                    notifyDataSetChanged();
                }
            });

        }
    }

    public interface DescriptionRepeatListener{
        void productImageClick(int pos,int returnItemView);
    }
}
