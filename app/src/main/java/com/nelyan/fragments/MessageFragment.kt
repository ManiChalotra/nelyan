package com.nelyan.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelyan.R
import com.nelyan.adapter.MessageAdapter
import com.nelyan.adapter.NotificationAdapter

class MessageFragment : Fragment() {
    var v: View? = null
   lateinit var mContext: Context
    var ivDel1: ImageView? = null
    var ivDel2: ImageView? = null
    var ivBack: ImageView? = null
    var rl_1: RelativeLayout? = null
    var rl_2: RelativeLayout? = null
    var rl_3: RelativeLayout? = null
    var dialog: Dialog? = null
    var recyclerview: RecyclerView? = null
    var messageAdapter: MessageAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_message, container, false)
        mContext = requireActivity()
        ivBack = v!!.findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener {
            val fm = requireActivity()!!.supportFragmentManager
            val f = fm.findFragmentById(R.id.frame_container)
            fm.popBackStack()
            // getActivity().onBackPressed();
        })
        /* rl_1=v.findViewById(R.id.rl_1);
        rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), Chat1Activity.class);
                startActivity(i);
            }
        });*/recyclerview = v!!.findViewById(R.id.recyclerview)
        messageAdapter = MessageAdapter(mContext)
        recyclerview!!.setLayoutManager(LinearLayoutManager(mContext))
        recyclerview!!.setAdapter(messageAdapter)
        /*  ivDel1=v.findViewById(R.id.ivDel1);
        ivDel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogDelete();
            }
        });
 ivDel2=v.findViewById(R.id.ivDel2);
        ivDel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailogDelete();
            }
        });*/return v
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
    }
}