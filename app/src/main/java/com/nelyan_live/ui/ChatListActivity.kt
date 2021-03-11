package com.nelyan_live.ui


import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.AppUtils
import com.nelyan_live.R
import com.nelyan_live.adapter.ChatListAdapter
import com.nelyan_live.utils.OpenActivity
import kotlinx.android.synthetic.main.fragment_chat_list.*

class ChatListActivity : AppCompatActivity(),View.OnClickListener, AdapterView.OnItemSelectedListener, ChatListAdapter.OnChatListItemClickListner {


    var chatListAdapter: ChatListAdapter? = null
    var recyclerview: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chat_list)
        initalizeClicks()


        recyclerview = findViewById(R.id.recyclerview)
        chatListAdapter = ChatListAdapter(this, this)
        recyclerview!!.setLayoutManager(LinearLayoutManager(this))
        recyclerview!!.setAdapter(chatListAdapter)


      // for setting order module listing
        val genderlist = arrayOf<String?>(
                "",
                "Date Added",
                "Avilable Place",
                "Distance")
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, R.layout.customspinner, genderlist)

            // Setting Adapter to the Spinner
        orderby!!.setAdapter(adapter)
            // Setting OnItemClickListener to the Spinner
        orderby!!.setOnItemSelectedListener(this@ChatListActivity)


    }

    private  fun initalizeClicks(){
        ivBack.setOnClickListener(this)
        ll_1.setOnClickListener(this)
        tvFilter.setOnClickListener(this)
        iv_map.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ivBack->{
                onBackPressed()
            }
            R.id.ll_1->{
                dailogLocation()
            }
            R.id.tvFilter->{
                AppUtils.gotoFragment(this, com.nelyan_live.fragments.ChildCareFragment(), R.id.frame_container, false)

            }
            R.id.iv_map->{
                OpenActivity(ChildCareActivity::class.java)
            }

        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    fun dailogLocation() {
       val  dialog = Dialog(this)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_location)
        dialog!!.setCancelable(true)
        val rlYes: RelativeLayout
        val rlNo: RelativeLayout
        rlNo = dialog!!.findViewById(R.id.rlNo)
        rlNo.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            dialog!!.dismiss()
        }
        rlYes = dialog!!.findViewById(R.id.rlYes)
        rlYes.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()
    }

    override fun onItemClickListner(position: Int) {
        OpenActivity(NurserieActivityy::class.java)
    }
}