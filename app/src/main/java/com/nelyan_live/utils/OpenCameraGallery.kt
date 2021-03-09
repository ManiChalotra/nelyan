package com.nelyan_live.utils

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.nelyan_live.R
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws

abstract class OpenCameraGallery : AppCompatActivity() {

    val CAMERA_REQUEST = 1
    private val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var imgPath = ""
    private var requestCodeGallary = 159
    private var requestCodeCamera = 1231
    lateinit var mActivity: Activity


     fun checkPermission(activity: Activity) {
        mActivity = activity
        if (ActivityCompat.checkSelfPermission(
                        this!!,
                        Manifest.permission.CAMERA
                ) + ActivityCompat.checkSelfPermission(
                        this!!,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) + ActivityCompat.checkSelfPermission(
                        this!!,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            uploadImage()
        } else {
            requestPermission()
        }

    }


    private fun uploadImage() {
        val uploadImage = Dialog(this!!, R.style.Theme_Dialog)
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
            if (cameraIntent.resolveActivity(this!!.packageManager) != null) {
                // Create the File where the photo should go
                var photoFile: File? = null
                try {
                    val myDirectory =
                            File(Environment.getExternalStorageDirectory(), "Pictures")
                    if (!myDirectory.exists()) {
                        myDirectory.mkdirs()
                    }
                    photoFile = createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    //Log.i(TAG, "IOException");
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                    startActivityForResult(cameraIntent, requestCodeCamera)
                }
            }
        }


        tv_cancel.setOnClickListener { uploadImage.dismiss() }
        tvGallery.setOnClickListener {
            uploadImage.dismiss()
          //  selectImage(ivProfile, "1")

            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCodeGallary)


        }
        uploadImage.show()
    }


    private fun requestPermission() {

        let { ActivityCompat.requestPermissions(it, permissions, CAMERA_REQUEST) }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        var image: File? = null

        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",  // suffix
                storageDir // directory
        )
        // Save a file: path for use with ACTION_VIEW intents
        imgPath = "file:" + image.absolutePath
        return image

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        ////////////////////////////////////////
        // for handling image and gallery
        if (requestCode == requestCodeCamera && resultCode == Activity.RESULT_OK) {
            try {
                if (Uri.parse(imgPath) != null) {
                    CropImage.activity(Uri.parse(imgPath))
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(mActivity)
                }

                // imgPath = CommonUtil.getPath(requireContext(), Uri.parse(imgPath))
                // civProfile.setImageBitmap(BitmapFactory.decodeFile(imgPath))

            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (requestCode == requestCodeGallary && resultCode == Activity.RESULT_OK) {

            try {
                val uri = data!!.data
                if (uri != null) {
                    if (uri != null) {
                        CropImage.activity(uri)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(this)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else if (CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE === requestCode && resultCode == Activity.RESULT_OK) {
            try {
                val result: CropImage.ActivityResult = CropImage.getActivityResult(data)
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    val uri: Uri = result.getUri()
                    if (uri != null) {
                        imgPath = getPath(mActivity, uri).toString()
                        getRealImagePath(imgPath)

                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            uploadImage()
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            startActivityForResult(intent, PICK_IMAGE_FROM_CAMERA)

        } else if (!shouldShowRequestPermissionRationale(permissions[0])) {
            let {
                showDialog(
                        it,
                        getString(R.string.alert),
                        getString(R.string.go_to_settings_to_give_permissions)
                )
            }
        } else {
            let {
                showDialog(
                        it,
                        getString(R.string.alert),
                        getString(R.string.permissions_is_mandatory)
                )
            }
        }
    }

    fun showDialog(context: Context, msgHeading: String, message: String?) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.open_setting_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogButton = dialog.findViewById<Button>(R.id.btn_dialog)
        val text = dialog.findViewById<TextView>(R.id.text_dialog)
        val textHeading = dialog.findViewById<TextView>(R.id.tvHeading)
        textHeading.text = msgHeading
        text.text = message
        if (message == context.getString(R.string.go_to_settings_to_give_permissions)) {
            dialogButton.text = context.getString(R.string.settings)
        } else {
            dialogButton.text = context.getString(R.string.ok)
        }
        dialogButton.setOnClickListener {
            if (message == context.getString(R.string.go_to_settings_to_give_permissions)) {
                dialogButton.text = context.getString(R.string.settings)
                val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            dialog.dismiss()
        }
        dialog.show()
    }


    open fun getPath(context: Context?, uri: Uri): String? {
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
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
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
            }
        } finally {
            cursor?.close()
        }
        return null
    }


    abstract fun getRealImagePath(imgPath:String?)

}