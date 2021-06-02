package com.nelyan_live.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nelyan_live.R
import com.nelyan_live.chat.MessagesVM
import com.nelyan_live.databinding.FragmentMessageBinding
import com.nelyan_live.ui.CommunicationListner
import com.nelyan_live.ui.HomeActivity


class MessageFragment : Fragment() {

    private  var listner: CommunicationListner?= null
     val messagesVM: MessagesVM by viewModels()

    override fun onResume() {
        super.onResume()
        if(listner!= null){
            listner!!.onFargmentActive(2)
        }
    }

    lateinit var fragmentMessageBinding: FragmentMessageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        fragmentMessageBinding = DataBindingUtil.inflate(LayoutInflater.from(container!!.context),R.layout.fragment_message, container, false)
        fragmentMessageBinding.messageVM =messagesVM

        messagesVM.userId = (container.context as HomeActivity).userId
        Log.e("fasfasfa","==MessageFragment====onCreateView")

        return fragmentMessageBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("fasfasfa","==MessageFragment====onViewCreated")
        //hideKeyboard
        val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        messagesVM.connectSocket(view.context)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("fasfasfa","==MessageFragment====onAttach")

        if(context is CommunicationListner)
        {
            listner = context
        }
        else{

            throw RuntimeException("Home Fragment not Attched")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listner = null
    }


}