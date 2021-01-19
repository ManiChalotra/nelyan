package com.nelyan.HELPER;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.nelyan.BuildConfig;
import com.nelyan.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public abstract class image extends AppCompatActivity {

    Dialog dialog;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private SharedPreferences sharedPreferences;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    File imgFile;
    Bitmap bitmap;
    Bitmap imag;
    ImageView profileimage;
    private String encodedImage,pictureFilePath,kk,strIMg="";

    public void image(String type)
    {
        if(type.equals("camera"))
        {
            camera();

        }else
        {


            dialog = new Dialog(image.this);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.add_photo);
            dialog.setCancelable(true);
            dialog.show();

            LinearLayout take_photo = dialog.findViewById(R.id.take_photo);
            LinearLayout gallery = dialog.findViewById(R.id.gallery);
            LinearLayout cancel = dialog.findViewById(R.id.cancel);
            take_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    camera();
                }
            });
            gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFileChooser();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            dialog.dismiss();
        } catch (Exception e){

        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                strIMg = convertImageToString(bitmap);
                selectedImage(bitmap,"");
                Log.e("catch_bitmap", "value: " + strIMg);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if (requestCode == REQUEST_IMAGE_CAPTURE) {
            try {
                File imgFile = new File(pictureFilePath);
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), bmOptions);
                bitmap = Bitmap.createScaledBitmap(bitmap,700, 700, true);
            }catch (Exception e)
            {

            }

            ExifInterface ei = null;
            try {
                ei = new ExifInterface(pictureFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
            try {
                rotatedBitmap = Bitmap.createScaledBitmap(rotatedBitmap,700, 700, true);
                strIMg = convertimage(rotatedBitmap);
                selectedImage(rotatedBitmap,"");

            } catch (Exception e) {
                dialog.dismiss();
            }
        }


    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    //    File imgFile = new File(pictureFilePath);
    public String convertimage(Bitmap bit)
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 70, bout);
        byte[] imagearray = bout.toByteArray();

        encodedImage = android.util.Base64.encodeToString(imagearray, android.util.Base64.DEFAULT);
        String kk = encodedImage;
        return kk;
    }
    private File getPictureFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "BusinessEvents" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile,  ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();

        return image;
    }

    public String convertImageToString(Bitmap bit) {

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 70, bout);
        byte[] imagearray = bout.toByteArray();

        kk=new String(mImageUri.toString());
        kk = android.util.Base64.encodeToString(imagearray, android.util.Base64.DEFAULT);

        return kk;
    }
    public abstract void selectedImage(Bitmap var1, String var2);

    public  void camera()
    {
        PermissionListener permissionlistener = new PermissionListener() {

            public void onPermissionGranted() {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    File pictureFile = null;
                    try {
                        pictureFile = getPictureFile();
                    }

                    catch (IOException ex)

                    {
                        Toast.makeText(image.this,
                                "Photo file can't be created, please try again",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (pictureFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(image.this,
                                BuildConfig.APPLICATION_ID+".provider",
                                pictureFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions)
            {
                Toast.makeText(getApplicationContext(), "Permission Denied" , Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with((getApplicationContext()))
                .setPermissionListener(permissionlistener)
                .setRationaleConfirmText("Permission")
                .setRationaleTitle("Permission required.")
                .setRationaleMessage("We need this permission to access camera and device storage.")
                .setDeniedTitle("Permission denied")
                .setDeniedMessage("Without this permission,receiver couldn't hear you.\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setGotoSettingButtonText("Settings")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
}
