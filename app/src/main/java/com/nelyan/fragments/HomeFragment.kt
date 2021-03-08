package com.nelyan.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.preferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.meherr.mehar.db.DataStoragePreference
import com.nelyan.R
import com.nelyan.adapter.MyHomeAdapter
import com.nelyan.modals.HomeModal
import com.nelyan.utils.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.tookbar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext


class HomeFragment : Fragment(), View.OnClickListener, CoroutineScope {

    val appViewModel: AppViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(AppViewModel::class.java)
    }

    private val job by lazy { Job() }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private val datalist by lazy { ArrayList<HomeModal>() }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(Dispatchers.Main.immediate) {
            val authkey = DataStoragePreference(requireActivity())!!.emitStoredValue(preferencesKey<String>("auth_key"))!!.first()
            Log.d("HomeFragAuthKey", "------------------" + authkey)
            appViewModel.sendHOmeCategoryData(security_key, authkey)
            homeFargProgressBar?.showProgressBar()
            checkMvvmResponse()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initalize()
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

                    rv_home?.adapter = MyHomeAdapter(requireActivity(), datalist)
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


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                iv_back!!.setImageResource(R.drawable.menu)
                iv_back!!.setVisibility(View.GONE)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}