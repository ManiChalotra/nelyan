package com.nelyan_live.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nelyan_live.R
import com.nelyan_live.ui.*
import com.nelyan_live.utils.OpenActivity
import com.nelyan_live.utils.myCustomToast
import kotlinx.android.synthetic.main.activity_pubiler.*
import java.lang.RuntimeException

class PublisherFrag : Fragment(), View.OnClickListener {

    private var type = ""
    private  var listner: CommunicationListner?= null

    override fun onResume() {
        super.onResume()
        if(listner!= null){
            listner!!.onFargmentActive(3)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_pubiler, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initalizeClicks()

        radioGroup.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.btn_activity -> {
                        type = "1"
                    }
                    R.id.btn_Nursery -> {
                        type = "2"
                    }
                    R.id.btn_materialAssistant -> {
                        type = "3"
                    }
                    R.id.btn_babySitter -> {
                        type = "4"
                    }
                    R.id.btn_traderArtisans -> {
                        type = "5"
                    }
                }
            }
        }
    }


    private fun initalizeClicks() {
        btnSubmit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnSubmit -> {
                when(type){
                    "1"->{
                        var authkey = arguments?.getString("authorization")
                        requireActivity().OpenActivity(ActivityFormActivity::class.java){
                            putString("authorization", authkey)
                        }
                    }
                    "2"->{
                        requireActivity().OpenActivity(NurserieActivity::class.java)
                    }
                    "3"->{
                        requireActivity().OpenActivity(MaternalAssistantActivity::class.java)
                    }
                    "4"->{
                        requireActivity().OpenActivity(BabySitterActivity::class.java)
                    }
                    "5"->{
                        requireActivity().OpenActivity(TraderActivity::class.java)
                    }
                    else->{
                        requireActivity().myCustomToast("Please select your publisher type ")
                    }
                }

            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is CommunicationListner){
            listner = context as CommunicationListner
        }else{

            throw RuntimeException("Home Fragment not Attched")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listner = null
    }
}