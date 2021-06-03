package com.nelyanlive.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.datastore.preferences.core.preferencesKey
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.nelyanlive.AppUtils
import com.nelyanlive.R
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.ui.*
import com.nelyanlive.utils.OpenActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DrawerFragment : Fragment(), CoroutineScope {

    lateinit  var mContext: Context
    var tvHome: TextView? = null
    var tvLogin: TextView? = null
    var tvAdd: TextView? = null
    var tvFavorite: TextView? = null
    var tvProfile: TextView? = null
    var ivProfile: ImageView? = null
    var tvContact: TextView? = null
    var tvNoti: TextView? = null
    var tvSettings: TextView? = null
    var tvLog: TextView? = null
    var dialog: Dialog? = null
    lateinit var v: View
    private val job by lazy {
        Job()
    }

    private val dataStoragePreference by lazy {
        DataStoragePreference(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_drawer, container, false)
        mContext = requireActivity()
        tvHome = v.findViewById(R.id.tvHome)
        tvHome!!.setOnClickListener(View.OnClickListener {
            val i = Intent(activity, HomeActivity::class.java)
            startActivity(i)
        })
        tvLogin = v.findViewById(R.id.tvLogin)
/*
        tvLogin!!.setOnClickListener(View.OnClickListener {
            val i = Intent(activity, LoginActivity::class.java)
            startActivity(i)
        })
*/
        tvAdd = v.findViewById(R.id.tv_myAdd)
        tvAdd!!.setOnClickListener(View.OnClickListener { AppUtils.gotoFragment(mContext, MyAddFragment(), R.id.frame_container, false) })
        tvFavorite = v.findViewById(R.id.tvFavorite)

        tvFavorite!!.setOnClickListener(View.OnClickListener {
            requireActivity().OpenActivity(FavouriteActivity::class.java)
          //  AppUtils.gotoFragment(mContext, com.nelyan_live.fragments.FavoriteFragment(), R.id.frame_container, false)

        })

        tvProfile = v.findViewById(R.id.tvProfile)
        ivProfile = v.findViewById(R.id.iv_user_pic)
        tvContact = v.findViewById(R.id.tvContact)

        launch(Dispatchers.Main.immediate) {
            var userImage = dataStoragePreference.emitStoredValue(preferencesKey<String>("imageLogin")).first()
            var userName = dataStoragePreference.emitStoredValue(preferencesKey<String>("nameLogin")).first()
            if (userImage != null)
                Glide.with(requireContext()).load(userImage).error(R.drawable.user_img).into(ivProfile!!)
            if (userName != null)
                tvLogin!!.text = userName
        }

        tvProfile!!.setOnClickListener(View.OnClickListener {
            val i = Intent(activity, ProfileActivity::class.java)
            startActivity(i)
        })


        tvContact!!.setOnClickListener(View.OnClickListener {
            val i = Intent(activity, ContactUsActivity::class.java)
            startActivity(i)
        })

        tvNoti = v.findViewById(R.id.tvNoti)

        tvNoti!!.setOnClickListener(View.OnClickListener {
            val i = Intent(activity, NotificationActivity::class.java)
            startActivity(i)
        })

        tvSettings = v.findViewById(R.id.tvSettings)

        tvSettings!!.setOnClickListener(View.OnClickListener {
            val i = Intent(activity, SettingsActivity::class.java)
            startActivity(i)
        })

        tvLog = v.findViewById(R.id.tvLog)
        tvLog!!.setOnClickListener(View.OnClickListener { showLog() })
        return v

    }

    private fun showLog() {
        dialog = Dialog(mContext)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_logout)
        dialog!!.setCancelable(true)

        val rlYes: RelativeLayout
        val rlNo: RelativeLayout

        rlYes = dialog!!.findViewById(R.id.rlYes)
        rlNo = dialog!!.findViewById(R.id.rlNo)

        rlYes.setOnClickListener {
            val i = Intent(mContext, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
            //   mContext.startActivity(new Intent(mContext, LoginActivity.class));
            dialog!!.dismiss()
        }

        rlNo.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()
    }

    override val coroutineContext: CoroutineContext
        get() = TODO("Not yet implemented")
}