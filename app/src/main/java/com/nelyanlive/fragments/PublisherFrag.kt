package com.nelyanlive.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.nelyanlive.R
import com.nelyanlive.ui.AddActivity
import com.nelyanlive.ui.BabySitterActivity
import com.nelyanlive.ui.CommunicationListner
import com.nelyanlive.ui.TraderActivity
import com.nelyanlive.utils.OpenActivity
import com.nelyanlive.utils.myCustomToast
import kotlinx.android.synthetic.main.activity_pubiler.*

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

        //hideKeyboard
        val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        initalizeClicks()

        radioGroup.setOnCheckedChangeListener { _, optionId ->
            run {

                when (optionId) {
                    R.id.btn_activity -> {
                        type = "1"
                    }
                    R.id.btn_child_care -> {
                        type = "2"
                    }
                    R.id.btn_traderArtisans -> {
                        type = "3"
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
                        requireActivity().OpenActivity(AddActivity::class.java)
                    }
                    "2"->{
                        requireActivity().OpenActivity(BabySitterActivity::class.java)
                    }
                    "3"->{
                        requireActivity().OpenActivity(TraderActivity::class.java)
                    }

                    else->{
                        requireActivity().myCustomToast(getString(R.string.publisher_unselected_error))
                    }
                }
            }
        }
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
}