package com.nelyanlive.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.adapter.MyHomeAdapter
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.HomeModal
import com.nelyanlive.ui.ActivitiesListActivity
import com.nelyanlive.ui.CommunicationListner
import com.nelyanlive.ui.HomeChildCareListActivity
import com.nelyanlive.ui.TraderListingActivity
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.tookbar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext


class HomeFragment : Fragment(), View.OnClickListener, CoroutineScope , MyHomeAdapter.OnHomeRecyclerViewItemClickListner{

    val appViewModel: AppViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(AppViewModel::class.java)
    }

    private val job = Job()

    private  val dataStoragePreference by lazy { DataStoragePreference(requireContext()) }

    private  var listner:CommunicationListner?= null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private val datalist by lazy { ArrayList<HomeModal>() }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        if(listner!= null){
            listner!!.onFargmentActive(1)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initalize()
           //hideKeyboard
            val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)


        val authkey = AllSharedPref.restoreString(requireContext(), "auth_key")
        Log.d("authorization", "---------------$authkey")
        appViewModel.sendHOmeCategoryData(security_key, authkey)
        homeFargProgressBar?.showProgressBar()
        checkMvvmResponse()

    }

    private fun initalize() {
        iv_back.setOnClickListener(this)
    }

    private fun checkMvvmResponse() {
        appViewModel.observeHomeCategoryResponse()!!.observe(requireActivity(), androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    homeFargProgressBar?.hideProgressBar()
                    Log.d("homeCategoryResponse", "-------------" + Gson().toJson(response.body()))
                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    val listArray = jsonObject.getJSONArray("data")
                    val mSizeOfData = listArray.length()

                    if (datalist != null) {
                        datalist.clear()
                    }

                    for (i in 0 until mSizeOfData) {
                        val name = jsonObject.getJSONArray("data").getJSONObject(i).get("name").toString()
                        val image = jsonObject.getJSONArray("data").getJSONObject(i).get("image").toString()
                        datalist.add(HomeModal(image, name))
                    }

                    rv_home?.adapter = MyHomeAdapter(requireActivity(), datalist, this@HomeFragment)
                }
            } else {
                ErrorBodyResponse(response, requireActivity(), homeFargProgressBar)
            }
        })

        appViewModel.getException()!!.observe(requireActivity(), androidx.lifecycle.Observer {
            requireActivity().myCustomToast(it)
            homeFargProgressBar?.hideProgressBar()
        })
    }

    override fun onItemHomeClickListner(list: java.util.ArrayList<HomeModal>, position: Int) {
        when(position){

            0->{
                requireActivity().OpenActivity(ActivitiesListActivity::class.java)
                {putString("type","1")}
            }

            1->{
                requireActivity().OpenActivity(HomeChildCareListActivity::class.java)
                {putString("type","2")}
            }

            2->{
                val i = Intent(requireActivity(), TraderListingActivity::class.java)
                i.putExtra("type","3")
                requireActivity().startActivity(i)
            }

            3->{
                requireActivity().OpenActivity(TraderListingActivity::class.java){putString("type","4")}
              //  AppUtils.gotoFragment(requireActivity(), TraderListingFragment(), R.id.frame_container, false)
            }

        }
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                iv_back!!.setImageResource(R.drawable.menu)
                iv_back!!.visibility = View.GONE
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

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}