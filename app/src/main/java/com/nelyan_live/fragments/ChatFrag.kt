package com.nelyan_live.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.nelyan_live.R
import com.nelyan_live.chat.ChatData
import com.nelyan_live.chat.GroupChatVM
import com.nelyan_live.data.viewmodel.AppViewModel
import com.nelyan_live.databinding.ActivityChatBinding
import com.nelyan_live.ui.CommunicationListner
import com.nelyan_live.ui.HomeActivity
import com.nelyan_live.ui.RegulationActivity
import com.nelyan_live.utils.AllSharedPref
import com.nelyan_live.utils.checkIfHasNetwork
import com.nelyan_live.utils.security_key
import com.nelyan_live.utils.showSnackBar
import org.json.JSONObject

class ChatFrag(var userlocation: String,var userlat: String,var userlong: String) : Fragment() {

    private  var listner: CommunicationListner?= null


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
    val groupChatVM : GroupChatVM by viewModels()


    lateinit var activityChatBinding: ActivityChatBinding


    override fun onResume() {
        super.onResume()
        if(listner!= null){
            listner!!.onFargmentActive(4)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        activityChatBinding = DataBindingUtil.inflate(LayoutInflater.from(container!!.context),R.layout.activity_chat, container, false)
        activityChatBinding.groupChatVM = groupChatVM

        mContext = container.context

        activityChatBinding.btnRegulation!!.setOnClickListener {
            val i = Intent(mContext, RegulationActivity::class.java)
            startActivity(i)
        }

        return activityChatBinding.root
    }

    fun showDailog() {
        dialog = Dialog(mContext)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_chat_flag)
        dialog!!.setCancelable(true)
        val btnSubmit: RelativeLayout = dialog!!.findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            dialog!!.dismiss()
        }
        dialog!!.show()
    }

    fun dailogDelete() {
        dialog = Dialog(mContext)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_chat_delete)
        dialog!!.setCancelable(true)
        val rl_1: RelativeLayout = dialog!!.findViewById(R.id.rl_1)
        rl_1.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            dialog!!.dismiss()
        }
        dialog!!.show()
    }




    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is CommunicationListner){
            listner = context
        }else{

            throw RuntimeException("Home Fragment not Attched")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listner = null
    }

    val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance((mContext as Activity).application).create(AppViewModel::class.java)
    }


    private var authorization = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        groupChatVM.groupName = userlocation
        groupChatVM.userId = (view.context as HomeActivity).userId
        groupChatVM.rvChat = activityChatBinding.rvChat
        groupChatVM.connectSocket(view.context)
        if (checkIfHasNetwork((mContext as Activity))) {
            authorization = AllSharedPref.restoreString(mContext, "auth_key")
            appViewModel.groupMessageApiData(security_key, authorization,userlocation,"0","20")
           // myfav_progressBar?.showProgressBar()
        }
        else {
            showSnackBar((mContext as Activity), getString(R.string.no_internet_error))
        }

        appViewModel.observeGroupMessageApiResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {

                    Log.e("myFavResponse", "-------------" + Gson().toJson(response.body()))
                    val jsonMain = JSONObject(response.body().toString())
                    Log.e("socket===", jsonMain.toString())
                    val listData : ArrayList<ChatData> = ArrayList()

                    val jsonArray = jsonMain.getJSONArray("data")
                    for(i in 0 until jsonArray.length()) {

                        val json = jsonArray.getJSONObject(i)

                        listData.add(ChatData(
                                json.getString("id"),
                                json.getString("senderId"),
                                json.getString("receiverId"),
                                "",
                                "",
                                json.getString("message"),
                                json.getString("readStatus"),
                                json.getString("messageType"),
                                json.getString("deletedId"),
                                json.getString("created"),
                                json.getString("updated"),
                                "",
                                json.getString("recieverImage"),
                                json.getString("senderName"),
                                json.getString("senderImage"),
                                groupChatVM.userId,"1"

                        ))
                    }


                    if(listData.isNotEmpty()) {
                        groupChatVM.listChat.addAll(listData)

                        groupChatVM.listChat.reverse()
                        groupChatVM.groupChatAdapter.addItems(groupChatVM.listChat)
                        activityChatBinding.rvChat.smoothScrollToPosition(groupChatVM.listChat.size - 1)
                        groupChatVM.noDataMessage.set("")
                    }
                    else{
                        groupChatVM.noDataMessage.set("No chat found")
                        groupChatVM.updateLocation(userlat,userlong,userlocation)

                    }

                }
            } else {

                Toast.makeText(mContext,"something went wrong",Toast.LENGTH_SHORT).show()

               // ErrorBodyResponse(response, this, myfav_progressBar)
            }
        })


    }

    override fun onDestroyView() {

        super.onDestroyView()
        Log.e("fasfasfa","======onDestroyView")

        groupChatVM.disconnectSocket()
    }

}