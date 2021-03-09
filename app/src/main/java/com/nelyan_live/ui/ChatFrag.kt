package com.nelyan_live.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.nelyan_live.R

class ChatFrag : Fragment() {
   lateinit  var mContext: Context
    lateinit  var v: View
    var ivBack: ImageView? = null
    var ivMan: ImageView? = null
    var ivOn: ImageView? = null
    var ivOf: ImageView? = null
    var ivAttachment: ImageView? = null
    var btnRegulation: Button? = null
    var ll_1: LinearLayout? = null
    var ll_2: LinearLayout? = null
    var dialog: Dialog? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.activity_chat, container, false)
        mContext = requireActivity()
        ivOn = v.findViewById(R.id.ivOn)
        ivOf = v.findViewById(R.id.ivOf)
        ivOn!!.setOnClickListener(View.OnClickListener {
            ivOf!!.setVisibility(View.VISIBLE)
            ivOn!!.setVisibility(View.GONE)
        })
        ivOf!!.setOnClickListener(View.OnClickListener {
            ivOf!!.setVisibility(View.GONE)
            ivOn!!.setVisibility(View.VISIBLE)
        })
        ivAttachment = v.findViewById(R.id.ivAttachment)
        ivAttachment!!.setOnClickListener(View.OnClickListener {
            // image("all");
        })
        btnRegulation = v.findViewById(R.id.btnRegulation)
        btnRegulation!!.setOnClickListener(View.OnClickListener {
            val i = Intent(mContext, com.nelyan_live.ui.RegulationActivity::class.java)
            startActivity(i)
        })
        ivMan = v.findViewById(R.id.ivMan)
        ivMan!!.setOnClickListener(View.OnClickListener {
            val i = Intent(mContext, Chat1Activity::class.java)
            startActivity(i)
        })
        ll_1 = v.findViewById(R.id.ll_1)
        ll_2 = v.findViewById(R.id.ll_2)
        ll_1!!.setOnClickListener(View.OnClickListener { showDailog() })
        ll_2!!.setOnClickListener(View.OnClickListener { dailogDelete() })
        return v
    }

    fun showDailog() {
        dialog = Dialog(mContext!!)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_chat_flag)
        dialog!!.setCancelable(true)
        val btnSubmit: RelativeLayout
        btnSubmit = dialog!!.findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            dialog!!.dismiss()
        }
        dialog!!.show()
    }

    fun dailogDelete() {
        dialog = Dialog(mContext!!)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_chat_delete)
        dialog!!.setCancelable(true)
        val rl_1: RelativeLayout
        rl_1 = dialog!!.findViewById(R.id.rl_1)
        rl_1.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            dialog!!.dismiss()
        }
        dialog!!.show()
    } /* @Override
    public void selectedImage(Bitmap var1, String var2) {
        ivAttachment.setImageBitmap(var1);
    }*/
}