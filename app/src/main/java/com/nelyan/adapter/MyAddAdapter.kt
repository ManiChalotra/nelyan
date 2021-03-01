package com.nelyan.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nelyan.R
import com.nelyan.ui.ActivityFormActivity

class MyAddAdapter(var context: Context) : RecyclerView.Adapter<MyAddAdapter.RecyclerViewHolder>() {
    var inflater: LayoutInflater
    var dialog: Dialog? = null
    var dialog1: Dialog? = null

    inner class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv_dot: ImageView
        var iv_cncl: ImageView? = null
        var ll_1: LinearLayout
        var tvEdit: TextView
        var tvDelete: TextView

        init {
            iv_dot = view.findViewById(R.id.iv_dot)
            ll_1 = view.findViewById(R.id.ll_1)
            tvEdit = view.findViewById(R.id.tvEdit)
            tvDelete = view.findViewById(R.id.tvDelete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val v = inflater.inflate(R.layout.list_my_add, parent, false)
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.iv_dot.setOnClickListener {
            if (holder.ll_1.visibility == View.VISIBLE) {
// Its visible
                holder.ll_1.visibility = View.GONE
            } else {
// Either gone or invisible
                holder.ll_1.visibility = View.VISIBLE
            }
        }
        holder.tvEdit.setOnClickListener { //  holder.ll_1.setVisibility(View.GONE);
            context.startActivity(Intent(context, ActivityFormActivity::class.java))
        }
        holder.tvDelete.setOnClickListener { //  holder.ll_1.setVisibility(View.GONE);
            dailogDelete()
        }
    }

    override fun getItemCount(): Int {
        return 4
    }

    fun dailogDelete() {
        dialog1 = Dialog(context)
        dialog1!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1!!.setContentView(R.layout.alert_chat_delete)
        dialog1!!.setCancelable(true)
        val rl_1: RelativeLayout
        rl_1 = dialog1!!.findViewById(R.id.rl_1)
        val tvMessage = dialog1!!.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = "Are you sure want to\ndelete my add?"
        rl_1.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            dialog1!!.dismiss()
        }
        dialog1!!.show()
    }

    fun dailogDot() {
        var iv_dot: ImageView
        dialog = Dialog(context)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_dot)
        dialog!!.setCancelable(true)
        val window = dialog!!.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.RIGHT or Gravity.TOP
        wlp.x = -10 //x position
        wlp.y = 0
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp
        var tvEdit = dialog!!.findViewById<TextView>(R.id.tvEdit)
        val tvDelet = dialog!!.findViewById<TextView>(R.id.tvDelete)
        tvEdit = dialog!!.findViewById(R.id.tvEdit)
        tvEdit.setOnClickListener {
            context.startActivity(Intent(context, ActivityFormActivity::class.java))
            dialog!!.dismiss()
        }
        tvDelet.setOnClickListener {
            dailogDelete()
            dialog!!.dismiss()
        }
        dialog!!.show()
    }

    init {
        inflater = LayoutInflater.from(context)
    }
}