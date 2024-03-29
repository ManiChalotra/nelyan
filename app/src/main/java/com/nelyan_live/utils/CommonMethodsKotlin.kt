package com.nelyan_live.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import com.nelyan_live.R
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


public class CommonMethodsKotlin {


    companion object{
        fun   messageShare(mContext: Activity) {
            try {
                val mText = mContext.resources.getText(R.string.join_me_share)
                var textSend ="" + mText + " "+mContext.getString(R.string.tag_send)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
                {
                    var defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(mContext) // Need to change the build to API 19

                    var sendIntent = Intent(Intent.ACTION_SEND)
                    sendIntent.setType("text/plain")
                    sendIntent.putExtra(Intent.EXTRA_TEXT, textSend)
                    if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
                    // any app that support this intent.
                    {
                        sendIntent.setPackage(defaultSmsPackageName)
                    }
                    mContext.startActivity(sendIntent);

                } else // For early versions, do what worked for you before.
                {
                    var smsIntent = Intent(android.content.Intent.ACTION_VIEW)
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    //   smsIntent.putExtra("address", "phoneNumber")

                    smsIntent.putExtra("sms_body", textSend)
                    mContext.startActivity(smsIntent);
                }

            } catch (ex: Exception) {

            }

        }


        fun time_to_timestamp(str_date: String?, pattren: String?): Long {
            var time_stamp: Long = 0
            try {
                val formatter = SimpleDateFormat(pattren)
                //SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                // formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                val date = formatter.parse(str_date) as Date
                time_stamp = date.time
            } catch (ex: ParseException) {
                ex.printStackTrace()
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
            time_stamp = time_stamp / 1000
            return time_stamp
        }


        /*
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKatOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

// DocumentProvider
        if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {
// ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

// TODO handle non-primary volumes
            }
// DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
// MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video1".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
// MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
// File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
*/
        // save the given bitmap on external storage and returns the path of the file.
        /*
    public static String saveImageToExternalStorage(Bitmap bitmap) {

        // Calendar now = Calendar.getInstance();
        String ImageURi = null;
        String imageFileName = "image" + System.currentTimeMillis() + ".jpg";
        //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
        try {
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    AppController.getInstance().getString(R.string.app_name));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //  String mPath = sdf.format(now.getTime()) + ".jpg";
            File imageFile = new File(dir, imageFileName);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 60;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            ImageURi = imageFile.getAbsolutePath();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ImageURi;
    }
*/
        /*Calender date to string convert*/
        fun calender_date_to_timestamp(str_date: String?): Long {
            var time_stamp: Long = 0
            try {
                val formatter = SimpleDateFormat("dd-MM-yyyy")
                //SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                formatter.timeZone = TimeZone.getTimeZone("UTC")
                val date = formatter.parse(str_date) as Date
                time_stamp = date.time
            } catch (ex: ParseException) {
                ex.printStackTrace()
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
            time_stamp = time_stamp / 1000
            return time_stamp
        }


        fun fbShare(mContext: Activity) {
            try {

                val mText = mContext.resources.getText(R.string.join_me_share)
                var textSend ="" + mText + " "+mContext.getString(R.string.tag_send)

                var share = Intent(Intent.ACTION_SEND)
                share.setType("text/plain")
                  share.putExtra(Intent.EXTRA_TEXT, textSend)
                 share.setPackage("com.facebook.katana") //Facebook App package

                try {
                    mContext.startActivity(Intent.createChooser(share, "Share..."))
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(mContext, "Facebook have not been installed.", Toast.LENGTH_SHORT).show()
                }

               /* var shareDialog=ShareDialog(mContext)
                val content = ShareLinkContent.Builder()
                  //  .setContentTitle("Hello Facebook")
                   // .setContentDescription("The 'Hello Facebook' sample  showcases simple Facebook integration")
                    .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                    .build()
                shareDialog.show(content);*/


            } catch (ex: Exception) {
               // CommonMethods.MaterialToastError(mContext, "Facebook have not been installed.")

            }
        }


        fun fbShare2(mContext: Activity, uri:Uri) {
            try {

                val mText = mContext.resources.getText(R.string.join_me_share)
                var textSend ="" + mText + " "+mContext.getString(R.string.tag_send)

                var share = Intent(Intent.ACTION_SEND)
              /*  share.setType("text/plain")*/
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_TEXT, textSend)
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.setPackage("com.facebook.katana") //Facebook App package

                try {
                    mContext.startActivity(Intent.createChooser(share, "Share..."))
                } catch (ex: ActivityNotFoundException) {
                   Toast.makeText(mContext, "Facebook have not been installed.", Toast.LENGTH_SHORT).show()

                }

                /* var shareDialog=ShareDialog(mContext)
                 val content = ShareLinkContent.Builder()
                   //  .setContentTitle("Hello Facebook")
                    // .setContentDescription("The 'Hello Facebook' sample  showcases simple Facebook integration")
                     .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                     .build()
                 shareDialog.show(content);*/

            } catch (ex: Exception) {
                // CommonMethods.MaterialToastError(mContext, "Facebook have not been installed.")

            }
        }
        fun fbMessangerShare(mContext: Activity) {
            try {
                val mText = mContext.resources.getText(R.string.join_me_share)
                var textSend ="" + mText + " "+mContext.getString(R.string.tag_send)

                var share = Intent(Intent.ACTION_SEND)
                share.setType("text/plain")
                share.putExtra(Intent.EXTRA_TEXT, textSend)
                share.setPackage("com.facebook.orca") //Facebook App package

                try {
                    mContext.startActivity(share);
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(mContext, "Please Install Facebook Messenger.", Toast.LENGTH_SHORT).show()
                }



            } catch (ex: Exception) {

            }
        }


        fun fbMessangerShare2(mContext: Activity, uri:Uri) {
            try {
                val mText = mContext.resources.getText(R.string.join_me_share)
                var textSend ="" + mText + " "+mContext.getString(R.string.tag_send)

                var share = Intent(Intent.ACTION_SEND)
              //  share.setType("text/plain")
                 share.setType("image/*");
                share.putExtra(Intent.EXTRA_TEXT, textSend)
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.setPackage("com.facebook.orca") //Facebook App package

                try {
                    mContext.startActivity(share);
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(mContext, "Please Install Facebook Messenger.", Toast.LENGTH_SHORT).show()

                }



            } catch (ex: Exception) {

            }
        }


        fun whatsAppShare(mContext: Activity) {
            try {
                val mText = mContext.resources.getText(R.string.join_me_share)
                var textSend ="" + mText + " "+mContext.getString(R.string.tag_send)


                var shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.setType("text/plain")
                shareIntent.setPackage("com.whatsapp");

                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "LoveF2y")

                shareIntent.putExtra(Intent.EXTRA_TEXT, textSend)

                try {
                    mContext.startActivity(shareIntent)
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(mContext, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show()
                }

            } catch (ex: Exception) {

            }

        }


        fun whatsAppShare2(mContext: Activity, uri:Uri) {
            try {
                val mText = mContext.resources.getText(R.string.join_me_share)
                var textSend ="" + mText + " "+mContext.getString(R.string.tag_send)


                var shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.setType("text/plain")
                shareIntent.setPackage("com.whatsapp");

                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "LoveF2y")

                shareIntent.putExtra(Intent.EXTRA_TEXT, textSend)
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

                try {
                    mContext.startActivity(shareIntent)
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(mContext, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show()
                }

            } catch (ex: Exception) {

            }

        }



        fun instaShare(mContext:Context){
            try {

                val mText = mContext.resources.getText(R.string.join_me_share)
                var textSend ="" + mText + " "+mContext.getString(R.string.tag_send)

                val shareOnAppIntent = Intent()
                shareOnAppIntent.action = Intent.ACTION_SEND
                shareOnAppIntent.putExtra(Intent.EXTRA_TEXT,textSend)
                shareOnAppIntent.type = "text/plain"
                shareOnAppIntent.setPackage("com.instagram.android")
                mContext.startActivity(shareOnAppIntent)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                Toast.makeText(mContext, "Instagram have not been installed.", Toast.LENGTH_SHORT).show()

            }
        }


        fun instaShare2(mContext:Context, uri:Uri){
            try {

                val mText = mContext.resources.getText(R.string.join_me_share)
                var textSend ="" + mText + " "+mContext.getString(R.string.tag_send)

                val shareOnAppIntent = Intent()
                shareOnAppIntent.action = Intent.ACTION_SEND
                shareOnAppIntent.putExtra(Intent.EXTRA_TEXT,textSend)
                shareOnAppIntent.putExtra(Intent.EXTRA_STREAM, uri);
               // shareOnAppIntent.type = "text/plain"
                shareOnAppIntent.setType("image/*");
                 shareOnAppIntent.setPackage("com.instagram.android")
                mContext.startActivity(shareOnAppIntent)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                Toast.makeText(mContext, "Instagram have not been installed.", Toast.LENGTH_SHORT).show()
            }
        }



        fun twitterShare(mContext:Context){
            try {
                val mText = mContext.resources.getText(R.string.join_me_share)
                var textSend ="" + mText + " "+mContext.getString(R.string.tag_send)


              /*  try { // get the Twitter app if possible
                    var intent: Intent? = null

               //     mContext.getPackageManager().getPackageInfo("com.twitter.android", 0)
                  //  intent = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=USERID"))
                    intent!!.putExtra(Intent.EXTRA_TEXT,textSend)
                    intent!!.type = "text/plain"
                    intent.setPackage("com.twitter")
                    intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                } catch (e: java.lang.Exception) { // no Twitter app, revert to browser
                    CommonMethods.MaterialToastError(mContext, "Twitter have not been installed.")

                }*/

                val tweetIntent = Intent(Intent.ACTION_SEND)
                tweetIntent.putExtra(Intent.EXTRA_TEXT, textSend)
                tweetIntent.type = "text/plain"

                val packManager: PackageManager = mContext.getPackageManager()
                val resolvedInfoList =
                    packManager.queryIntentActivities(
                        tweetIntent,
                        PackageManager.MATCH_DEFAULT_ONLY
                    )

                var resolved = false
                for (resolveInfo in resolvedInfoList) {
                    if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                        tweetIntent.setClassName(
                            resolveInfo.activityInfo.packageName,
                            resolveInfo.activityInfo.name
                        )
                        resolved = true
                        break
                    }
                }
                if (resolved) {
                    mContext.startActivity(tweetIntent)
                } else {
                    val i = Intent()
                    i.putExtra(Intent.EXTRA_TEXT, textSend)
                    i.action = Intent.ACTION_VIEW
                    i.data = Uri.parse(
                        "https://twitter.com/intent/tweet?text=" + urlEncode(textSend)
                    )
                    mContext.  startActivity(i)
                   // Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show()
                    Toast.makeText(mContext, "Twitter have not been installed.", Toast.LENGTH_SHORT).show()

                }
             } catch (e: Exception) {
            }
        }
          fun twitterShare2(mContext:Context, uri:Uri){
            try {
                val mText = mContext.resources.getText(R.string.join_me_share)
                var textSend ="" + mText + " "+mContext.getString(R.string.tag_send)


              /*  try { // get the Twitter app if possible
                    var intent: Intent? = null

               //     mContext.getPackageManager().getPackageInfo("com.twitter.android", 0)
                  //  intent = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=USERID"))
                    intent!!.putExtra(Intent.EXTRA_TEXT,textSend)
                    intent!!.type = "text/plain"
                    intent.setPackage("com.twitter")
                    intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                } catch (e: java.lang.Exception) { // no Twitter app, revert to browser
                    CommonMethods.MaterialToastError(mContext, "Twitter have not been installed.")

                }*/

                val tweetIntent = Intent(Intent.ACTION_SEND)
                tweetIntent.putExtra(Intent.EXTRA_TEXT, textSend)

                tweetIntent.type = "text/plain"

                tweetIntent.putExtra(Intent.EXTRA_STREAM, uri);
                val packManager: PackageManager = mContext.getPackageManager()
                val resolvedInfoList =
                    packManager.queryIntentActivities(
                        tweetIntent,
                        PackageManager.MATCH_DEFAULT_ONLY
                    )

                var resolved = false
                for (resolveInfo in resolvedInfoList) {
                    if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                        tweetIntent.setClassName(
                            resolveInfo.activityInfo.packageName,
                            resolveInfo.activityInfo.name
                        )
                        resolved = true
                        break
                    }
                }
                if (resolved) {
                    mContext.startActivity(tweetIntent)
                } else {
                    val i = Intent()
                    i.putExtra(Intent.EXTRA_TEXT, textSend)
                    i.action = Intent.ACTION_VIEW
                    i.data = Uri.parse(
                        "https://twitter.com/intent/tweet?text=" + urlEncode(textSend)
                    )
                    mContext.  startActivity(i)
                   // Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show()
                    Toast.makeText(mContext, "Twitter have not been installed.", Toast.LENGTH_SHORT).show()

                }
             } catch (e: Exception) {
            }
        }



        fun urlEncode(s: String): String? {
            return try {
                URLEncoder.encode(s, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                Log.wtf("wtf", "UTF-8 should always be supported", e)
                throw RuntimeException("URLEncoder.encode() failed for $s")
            }
        }

        fun linkdinShare(mContext:Context){
            try {

                val mText = mContext.resources.getText(R.string.join_me_share)
                var textSend ="" + mText + " "+mContext.getString(R.string.tag_send)



                val linkedinIntent = Intent(Intent.ACTION_SEND)
                linkedinIntent.type = "text/plain"
                linkedinIntent.putExtra(Intent.EXTRA_TEXT, textSend)

                var linkedinAppFound = false
                val matches2: List<ResolveInfo> = mContext.getPackageManager().queryIntentActivities(linkedinIntent, 0)

                for (info in matches2) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.linkedin")) {
                        linkedinIntent.setPackage(info.activityInfo.packageName)
                        linkedinAppFound = true
                        break
                    }else if (info.activityInfo.packageName.toLowerCase().startsWith("com.linkedin.android.lite")) {
                        linkedinIntent.setPackage(info.activityInfo.packageName)
                        linkedinAppFound = true
                        break
                    }
                }

                if (linkedinAppFound) {
                    mContext.startActivity(linkedinIntent)
                } else {
                    Toast.makeText(mContext, "LinkedIn app have not been installed.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
            }
        }


        fun linkdinShare2(mContext:Context,uri: Uri){
            try {

                val mText = mContext.resources.getText(R.string.join_me_share)
                var textSend ="" + mText + " "+mContext.getString(R.string.tag_send)



                val linkedinIntent = Intent(Intent.ACTION_SEND)
               // linkedinIntent.type = "text/plain"
               // linkedinIntent.type = "text/plain"
                linkedinIntent.setType("image/*");
                linkedinIntent.putExtra(Intent.EXTRA_TEXT, textSend)
                linkedinIntent.putExtra(Intent.EXTRA_STREAM, uri);

                var linkedinAppFound = false
                val matches2: List<ResolveInfo> = mContext.getPackageManager().queryIntentActivities(linkedinIntent, 0)

                for (info in matches2) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.linkedin")) {
                        linkedinIntent.setPackage(info.activityInfo.packageName)
                        linkedinAppFound = true
                        break
                    }else if (info.activityInfo.packageName.toLowerCase().startsWith("com.linkedin.android.lite")) {
                        linkedinIntent.setPackage(info.activityInfo.packageName)
                        linkedinAppFound = true
                        break
                    }
                }

                if (linkedinAppFound) {
                    mContext.startActivity(linkedinIntent)
                } else {
                    Toast.makeText(mContext, "LinkedIn app have not been installed.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
            }
        }

    }






}