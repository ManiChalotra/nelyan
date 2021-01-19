package com.nelyan.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.nelyan.R;
import com.nelyan.ui.Activity3Activity;
import com.nelyan.ui.EditProfileActivity;


public class MyAddAdapter  extends RecyclerView.Adapter<MyAddAdapter.RecyclerViewHolder> {
    Context context;
    LayoutInflater inflater;
    Dialog dialog,dialog1;
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_dot,iv_cncl;
        LinearLayout ll_1;
        TextView tvEdit,tvDelete;
        public RecyclerViewHolder(View view) {
            super(view);
            iv_dot=view.findViewById(R.id.iv_dot);
            ll_1=view.findViewById(R.id.ll_1);
            tvEdit=view.findViewById(R.id.tvEdit);
            tvDelete=view.findViewById(R.id.tvDelete);
        }
    }

    public MyAddAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyAddAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.list_my_add, parent, false);
        MyAddAdapter.RecyclerViewHolder viewHolder = new MyAddAdapter.RecyclerViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final MyAddAdapter.RecyclerViewHolder holder, int position) {

holder.iv_dot.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if ( holder.ll_1.getVisibility() == View.VISIBLE) {
// Its visible
            holder.ll_1.setVisibility(View.GONE);
        } else {
// Either gone or invisible
            holder.ll_1.setVisibility(View.VISIBLE);
        }

    }
});holder.tvEdit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      //  holder.ll_1.setVisibility(View.GONE);
        context.startActivity(new Intent(context, Activity3Activity.class));
    }
});holder.tvDelete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      //  holder.ll_1.setVisibility(View.GONE);
        dailogDelete();
    }
});
    }
    @Override
    public int getItemCount() {
        return 4;
    }

    public void dailogDelete(){

        dialog1 = new Dialog(context);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog1.setContentView(R.layout.alert_chat_delete);
        dialog1.setCancelable(true);

        RelativeLayout rl_1;
        rl_1= dialog1.findViewById(R.id.rl_1);
        rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }
    public void dailogDot(){
        ImageView iv_dot;
        dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.alert_dot);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.RIGHT| Gravity.TOP;
        wlp.x = -10; //x position
        wlp.y = 0;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        TextView tvEdit =dialog.findViewById(R.id.tvEdit);
        TextView tvDelet=dialog.findViewById(R.id.tvDelete);
        tvEdit= dialog.findViewById(R.id.tvEdit);
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 context.startActivity(new Intent(context, Activity3Activity.class));
                dialog.dismiss();
            }
        });
        tvDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogDelete();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}

