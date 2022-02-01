package com.nelyanlive.ui

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.chat.ChatVM
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.databinding.ActivityChat1Binding
import com.nelyanlive.utils.ErrorBodyResponse
import com.nelyanlive.utils.OpenActivity
import com.nelyanlive.utils.OpenCameraGallery
import com.nelyanlive.utils.ProgressDialog
import com.nelyanlive.utils.fcm.MyFirebaseMessagingService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class Chat1Activity :  OpenCameraGallery() {
    var ivBack: ImageView? = null
    var ivAttachment: ImageView? = null


    lateinit var activityChat1Binding: ActivityChat1Binding
     var userId =  ""

    val chatVM: ChatVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityChat1Binding = DataBindingUtil.setContentView(this,R.layout.activity_chat1)
        activityChat1Binding.chatVM = chatVM

        activityChat1Binding.ivBack.setOnClickListener { onBackPressed() }

        ivAttachment = findViewById(R.id.ivAttachment)
        ivAttachment!!.setOnClickListener {
            checkPermission(this)
        }
        chatVM.rvChat =activityChat1Binding.rvChat
        chatVM.noDataMessage.set(getString(R.string.loading_chat))

        Log.e("Chat1Activity","=====${intent.getStringExtra("senderID")}")

        if (intent.hasExtra("senderID")) {

            chatVM.senderID = intent.getStringExtra("senderID")!!
            chatVM.senderName.set(intent.getStringExtra("senderName")!!)
            chatVM.senderImage.set(intent.getStringExtra("senderImage")!!)
            chatVM.userId = intent.getStringExtra("userId")!!
            userId = intent.getStringExtra("userId")!!
        }

        chatVM.connectSocket(this)

        activityChat1Binding.rvChat.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if(bottom<oldBottom) {
                v!!.post { if(chatVM.listChat.isNotEmpty()) {
                    activityChat1Binding.rvChat.smoothScrollToPosition(chatVM.listChat.size - 1)
                } }
            }
        }
    }

    override fun onBackPressed() {
        chatVM.disconnectSocket()
        MyFirebaseMessagingService.chatNotification.value ="false"
        super.onBackPressed()
        OpenActivity(HomeActivity::class.java){
            putString("chat", userId)
        }
        finishAffinity()
    }

    override fun onResume() {
        super.onResume()
        MyFirebaseMessagingService.currentChatUser = intent.getStringExtra("senderID")!!
    }

    override fun onPause() {
        super.onPause()
        MyFirebaseMessagingService.currentChatUser = ""
    }

    override fun getRealImagePath(imgPath: String?) {
        setImage(imgPath)
    }

    private var imagePathList = ArrayList<MultipartBody.Part>()
    private val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java)
    }

    private var progressDialog = ProgressDialog(this)

    var setImage = ""
    var setImageTrue = false

    fun setImage(imgPath: String?) {
        val mfile: File?
        mfile = File(imgPath!!)
        val imageFile: RequestBody = mfile.asRequestBody("image/*".toMediaTypeOrNull())
        val photo: MultipartBody.Part?
        photo = MultipartBody.Part.createFormData("image", mfile.name, imageFile)
        imagePathList.clear()
        imagePathList.add(photo)
        val type: RequestBody = "image".toRequestBody("text/plain".toMediaTypeOrNull())
        val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())
        setImage = imgPath.replaceBeforeLast("/","")
        setImageTrue = true

        appViewModel.sendUploadImageData(type, users, imagePathList)
        progressDialog.setProgressDialog()

        appViewModel.observeUploadImageResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                progressDialog.hidedialog()
                Log.d("urlDataLoading", "------------" + Gson().toJson(response.body()))
                if (response.body() != null) {
                    if (response.body()!!.data != null) {

                        Log.e("urlListt", "--------${response.body()!!.data!![0].image.toString()}---")

                        var str  = response.body()!!.data!![0].image.toString()
                        val fileName  = response.body()!!.data!![0].fileName.toString()
                    str = str.replaceBeforeLast("/","")

                        Log.e("urlListt", "--------${fileName}---")
                        Log.e("urlListt", "--------${setImage.substring(1)}---")

                        if(setImage.substring(1) ==fileName && setImageTrue) {
                            setImageTrue = false
                            chatVM.sendChatImageMessage(str)
                        }
                    }
                } else {
                    ErrorBodyResponse(response, this, null)
                }
            }
            else {
                Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
                progressDialog.hidedialog()

            }
        })
    }
}