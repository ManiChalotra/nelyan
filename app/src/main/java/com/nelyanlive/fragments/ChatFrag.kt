package com.nelyanlive.fragments

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.chat.ChatData
import com.nelyanlive.chat.GroupChatVM
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.databinding.ActivityChatBinding
import com.nelyanlive.ui.CommunicationListner
import com.nelyanlive.ui.HomeActivity
import com.nelyanlive.utils.*
import com.nelyanlive.utils.fcm.MyFirebaseMessagingService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.java_websocket.util.Base64
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatFrag(var userlocation: String, var userlat: String, var userlong: String,var ivBell: ImageView) : Fragment() {

    private var listner: CommunicationListner? = null
    lateinit var mContext: Context
     val groupChatVM: GroupChatVM by viewModels()
    lateinit var activityChatBinding: ActivityChatBinding

    override fun onResume() {
        super.onResume()
        MyFirebaseMessagingService.myChatVisible = false

        if (listner != null) {
            listner!!.onFargmentActive(4)
        }
    }

    override fun onPause() {
        super.onPause()
        MyFirebaseMessagingService.myChatVisible = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        activityChatBinding = DataBindingUtil.inflate(LayoutInflater.from(container!!.context), R.layout.activity_chat, container, false)
        activityChatBinding.groupChatVM = groupChatVM
        mContext = container.context
        groupChatVM.noDataMessage.set(mContext.getString(R.string.loading_chat))
        activityChatBinding.btnRegulation.setOnClickListener {
           /* val i = Intent(mContext, RegulationActivity::class.java)
            startActivity(i)*/
        }

        activityChatBinding.ivAttachment.setOnClickListener {
            checkPermission()
        }

        ivBell.visibility = View.GONE
        return activityChatBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e("fasfasfa","=chatFrag=====onAttach")

        if (context is CommunicationListner) {
            listner = context
        } else {
            throw RuntimeException("Home Fragment not Attached")
        }
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("fasfasfa","==chatFrag=====onDetach")

        listner = null
    }

    val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance((mContext as Activity).application).create(AppViewModel::class.java)
    }

    private var authorization = ""
    var list = listOf<Address>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        val geocoder = Geocoder(view.context, Locale.getDefault())
        list = geocoder.getFromLocation(userlat.toDouble(), userlong.toDouble(), 1)

        if(!list[0].locality.isNullOrBlank())

        groupChatVM.groupName =if(!list[0].locality.isNullOrBlank()) {list[0].locality} else{userlocation}
        groupChatVM.userId = (view.context as HomeActivity).userId
        groupChatVM.rvChat = activityChatBinding.rvChat
        groupChatVM.connectSocket(view.context)


        if (checkIfHasNetwork((mContext as Activity))) {

            authorization = AllSharedPref.restoreString(mContext, "auth_key")
            appViewModel.groupMessageApiData(security_key, authorization, if(!list[0].locality.isNullOrBlank()) {list[0].locality} else{userlocation}, "0", "20")

        }
        else {
            showSnackBar((mContext as Activity), getString(R.string.no_internet_error))
        }

        appViewModel.observeGroupMessageApiResponse()!!.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {

                    Log.e("observeGroupMessageApi", "-------------" + Gson().toJson(response.body()))
                    val jsonMain = JSONObject(response.body().toString())
                    Log.e("socket===", jsonMain.toString())
                    val listData: ArrayList<ChatData> = ArrayList()

                   groupChatVM.groupId = ""

                    val jsonArray = jsonMain.getJSONArray("data")

                    Log.e("socket=======", "===="+jsonArray.length())

                    for (i in jsonArray.length()-1 downTo  0) {

                        val json = jsonArray.getJSONObject(i)
                        groupChatVM.groupId = json.getString("groupId")

                        //decoding
                        var val3 = ""
                        try{
                            val lineSep = System.getProperty("line.separator")
                            val ps2: String = json.getString("message")
                            val tmp2: ByteArray = Base64.decode(ps2)
                            val val2 = String(tmp2, StandardCharsets.UTF_8)
                             val3 = val2.replace("<br />".toRegex(), lineSep!!)
                        }
                        catch (e: Exception)
                        {e.printStackTrace()}


                        listData.add(ChatData(
                                json.getString("id"),
                                json.getString("senderId"),
                                json.getString("receiverId"),
                                "",
                            json.getString("groupId"),
                            val3,
                                json.getString("readStatus"),
                                json.getString("messageType"),
                                json.getString("deletedId"),
                                json.getString("created"),
                                json.getString("updated"),
                                "",
                                json.getString("recieverImage"),
                                json.getString("senderName"),
                                json.getString("senderImage"),
                                groupChatVM.userId, "1",if(listData.size==0){true}
                        else{checkDateCompare(json.getString("created"),listData[listData.size-1].created)}

                        ))
                    }

                    if(groupChatVM.groupId.isNotEmpty())  setNotification(groupChatVM.groupId)

                    if (listData.isNotEmpty()) {
                        groupChatVM.listChat.addAll(listData)
                        groupChatVM.groupChatAdapter.addItems(groupChatVM.listChat)
                        activityChatBinding.rvChat.smoothScrollToPosition(groupChatVM.listChat.size - 1)
                        groupChatVM.noDataMessage.set("")
                    } else {
                        groupChatVM.noDataMessage.set("No chat found")
                        groupChatVM.updateLocation(userlat, userlong, if(!list[0].locality.isNullOrBlank()) {list[0].locality} else{userlocation})

                    } }
            } else
            {

                Toast.makeText(mContext, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()

            }
        })

        activityChatBinding.rvChat.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if(bottom<oldBottom) {
                v!!.post { if(groupChatVM.listChat.isNotEmpty()) {
                    activityChatBinding.rvChat.smoothScrollToPosition(groupChatVM.listChat.size - 1)
                } }
            }
        }

        ivBell.setOnClickListener {
            dailogNotification()
        }

    }

    private fun checkDateCompare(created: String, created1: String): Boolean {


        val date1 = SimpleDateFormat("dd").format(Date(created.toLong() * 1000))
        val date2 = SimpleDateFormat("dd").format(Date(created1.toLong() * 1000))

        return date1!=date2
    }

    override fun onDestroyView() {
        Log.e("fasfasfa","==chatFrag=====onDestroyView")
        super.onDestroyView()

    }

    private fun setNotification(groupID: String) {
        if (checkIfHasNetwork((mContext as Activity))) {
            authorization = AllSharedPref.restoreString(mContext, "auth_key")
            appViewModel.groupNotifyApiData(security_key, authorization, groupID)
        }
        else {
            showSnackBar((mContext as Activity), getString(R.string.no_internet_error))
        }

        appViewModel.observeGroupNotifyApiResponse()!!.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {

                    Log.e("observeGroupNotifyApi", "-------------" + Gson().toJson(response.body()))
                    val jsonMain = JSONObject(response.body().toString())
                    Log.e("socket===", jsonMain.toString())
                    if(!jsonMain.isNull("data")) {
                        val jsonData = jsonMain.getJSONObject("data")

                        val notificationStatus = jsonData.getString("notification")
                        groupChatVM.notifyStatus = if (notificationStatus == "1") {
                            "0"
                        } else {
                            "1"
                        }

                    }
                    ivBell.setImageResource(if(groupChatVM.notifyStatus=="0"){R.drawable.unmute}else{R.drawable.mute})
                    ivBell.visibility = View.VISIBLE
                    ivBell.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext,R.color.colorAccent))
                }

            } else {

                Toast.makeText(mContext, mContext.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()

            }
        })

    }


    lateinit var dialog: Dialog

    fun dailogNotification() {
        dialog = Dialog(mContext)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.alert_notification_change)
        dialog.setCancelable(true)
        val tvYes: TextView = dialog.findViewById(R.id.tvYes)
        val tvNo: TextView = dialog.findViewById(R.id.tvNo)
        val tvMessage: TextView = dialog.findViewById(R.id.tvMessage)
        tvMessage.text = if(groupChatVM.notifyStatus=="1"){
            getString(R.string.are_you_sure_to_un_mute)}
        else{getString(R.string.are_you_sure_to_mute)}
        tvYes.setOnClickListener {
            dialog.dismiss()
           setMuteNotifications()
        }
        tvNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setMuteNotifications() {
        if (checkIfHasNetwork((mContext as Activity))) {
            authorization = AllSharedPref.restoreString(mContext, "auth_key")
            appViewModel.changeNotifyApiData(security_key, authorization, groupChatVM.groupId,groupChatVM.notifyStatus)
        }
        else {
            showSnackBar((mContext as Activity), getString(R.string.no_internet_error))
        }

        appViewModel.observeChangeNotifyApiResponse()!!.observe(viewLifecycleOwner, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {

                    Log.e("observeGroupNotifyApiResponse", "-------------" + Gson().toJson(response.body()))
                    val jsonMain = JSONObject(response.body().toString())
                    Log.e("socket===", jsonMain.toString())
                    val jsonData = jsonMain.getJSONObject("data")

                    val notificationStatus = jsonData.getString("notification")
                    groupChatVM.notifyStatus = if(notificationStatus=="1"){"0"}else{"1"}
                    ivBell.setImageResource(if(notificationStatus=="1"){R.drawable.unmute}else{R.drawable.mute})
                    ivBell.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext,R.color.colorAccent))

                }

            } else {

                Toast.makeText(mContext, mContext.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()

            }
        })

    }

    fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.CAMERA
                ) + ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) + ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            uploadImage()
        }
        else {
            requestPermission()
        }

    }

    val CAMERA_REQUEST = 1
    private val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private fun requestPermission() {
        let { ActivityCompat.requestPermissions((mContext as Activity), permissions, CAMERA_REQUEST) }
    }

    private var requestCodeGallary = 159
    private var requestCodeCamera = 1231

    private fun uploadImage() {
        val uploadImage = Dialog(mContext, R.style.Theme_Dialog)
        uploadImage.requestWindowFeature(Window.FEATURE_NO_TITLE)
        uploadImage.setContentView(R.layout.camera_gallery_popup)
        uploadImage.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        uploadImage.setCancelable(true)
        uploadImage.setCanceledOnTouchOutside(true)
        uploadImage.window!!.setGravity(Gravity.BOTTOM)
        uploadImage.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val tvCamera = uploadImage.findViewById<TextView>(R.id.tvCamera)
        val tvGallery = uploadImage.findViewById<TextView>(R.id.tvGallery)
        val tv_cancel = uploadImage.findViewById<TextView>(R.id.tv_cancel)
        tvCamera.setOnClickListener {
            uploadImage.dismiss()
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity((mContext as Activity).packageManager) != null) {
                var photoFile: File? = null
                try {
                    val myDirectory =
                            File(Environment.getExternalStorageDirectory(), "Pictures")
                    if (!myDirectory.exists()) {
                        myDirectory.mkdirs()
                    }
                    photoFile = createImageFile()
                } catch (ex: IOException) {
                }
                if (photoFile != null) {
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                    startActivityForResult(cameraIntent, requestCodeCamera)
                }
            }
        }


        tv_cancel.setOnClickListener { uploadImage.dismiss() }
        tvGallery.setOnClickListener {
            uploadImage.dismiss()
            val intent = Intent()
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*"))
            intent.action = Intent.ACTION_GET_CONTENT
            this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCodeGallary)


        }
        uploadImage.show()
    }


    private var imgPath = ""

    private fun createImageFile(): File? {
        val image: File?

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",  // suffix
                storageDir // directory
        )
        imgPath = "file:" + image.absolutePath
        return image

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == requestCodeCamera && resultCode == Activity.RESULT_OK) {
            try {
                if (Uri.parse(imgPath) != null) {
                    imgPath = getPath(mContext, Uri.parse(imgPath)).toString()

                    setImage(imgPath)
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        else if (requestCode == requestCodeGallary && resultCode == Activity.RESULT_OK) {

            try {
                val uri = data!!.data
                if (uri != null) {
                    imgPath = getPath(mContext, uri).toString()
                   // getRealImagePath(imgPath)
                    setImage(imgPath)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    private fun getPath(context: Context?, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                return context?.let { getDataColumn(it, contentUri, null, null) }
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                when (type) {
                    "image" -> {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    "video" -> {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    "audio" -> {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                        split[1]
                )
                return context?.let { getDataColumn(it, contentUri, selection, selectionArgs) }
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else context?.let { getDataColumn(it, uri, null, null) }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            } }
        finally { cursor?.close() }
        return null
    }

    private var imagePathList = ArrayList<MultipartBody.Part>()

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
        setImage = imgPath.replaceBeforeLast("/","")
        setImageTrue = true
        val type: RequestBody = "image".toRequestBody("text/plain".toMediaTypeOrNull())
        val users = "users".toRequestBody("text/plain".toMediaTypeOrNull())

        appViewModel.sendUploadImageData(type, users, imagePathList)

        appViewModel.observeUploadImageResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("urlDataLoading", "------------" + Gson().toJson(response.body()))
                if (response.body() != null) {
                    if (response.body()!!.data != null) {

                        val fileName  = response.body()!!.data!![0].fileName.toString()
                        var str  = response.body()!!.data!![0].image.toString()
                        str =     str.replaceBeforeLast("/","")

                        if(setImage.substring(1) ==fileName && setImageTrue) {
                            setImageTrue = false
                            groupChatVM.sendChatImageMessage(str)
                        }
                    }
                }
                else {
                    ErrorBodyResponse(response, mContext, null)
                }
            }
            else {
                Toast.makeText(mContext,mContext.getString(R.string.something_went_wrong) , Toast.LENGTH_SHORT).show()
            }
        })



    }


}