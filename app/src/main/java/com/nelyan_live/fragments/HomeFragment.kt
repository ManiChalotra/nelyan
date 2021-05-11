package com.nelyan_live.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.nelyan_live.db.DataStoragePreference
import com.nelyan_live.R
import com.nelyan_live.adapter.MyHomeAdapter
import com.nelyan_live.modals.HomeModal
import com.nelyan_live.ui.*
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.tookbar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.json.JSONObject
import java.lang.RuntimeException
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

        val authkey = AllSharedPref.restoreString(requireContext(), "auth_key")
        Log.d("authorization","---------------"+ authkey)
        appViewModel.sendHOmeCategoryData(security_key, authkey)
        homeFargProgressBar?.showProgressBar()
        checkMvvmResponse()



        /*launch(Dispatchers.Main.immediate) {
            val authkey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
            val authkey1 = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
            Log.d("HomeFragAuthKey", "------------------" + authkey)
            Log.d("HomeFragAuthKey", "------------------" + authkey1)
            appViewModel.sendHOmeCategoryData(security_key, authkey)
            homeFargProgressBar?.showProgressBar()
            checkMvvmResponse()
        }*/

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
                        datalist!!.clear()
                    }

                    for (i in 0 until mSizeOfData) {
                        val name = jsonObject.getJSONArray("data").getJSONObject(i).get("name").toString()
                        val image = jsonObject.getJSONArray("data").getJSONObject(i).get("image").toString()
                        datalist!!.add(HomeModal(image, name))
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
              //AppUtils.gotoFragment(requireActivity(), ActivityListFragment(), R.id.frame_container, false)
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
                iv_back!!.setVisibility(View.GONE)
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

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}