package com.example.fasih.dukaanapp.home.fragments.sellerPageResources;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.interfaces.OnContentUriCapturedListner;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Fasih on 02/25/19.
 */

public class CameraFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CODE = 123;
    private String currentPhotoPath;
    private Boolean isActivityResultLoadedSuccessfully = false;
    private Uri photoURI;
    private OnContentUriCapturedListner onContentUriCapturedListner;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        checkPermissionsGrant(getActivity());
        return view;
    }

    private void checkPermissionsGrant(FragmentActivity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] Permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (hasPermissions(Permissions, activity)) {
                //do your work here
                dispatchTakePictureIntent();
            } else {
                ActivityCompat.requestPermissions(activity, Permissions, REQUEST_CODE);
            }
        }

    }

    private boolean hasPermissions(String[] permissions, FragmentActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null && activity != null) {
            for (int i = 0; i < permissions.length; i++) {
                if (ActivityCompat.checkSelfPermission(activity, permissions[i]) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("TAG1234", "onRequestPermissionsResult: requestCode" + requestCode + "\n permissions" + permissions + "\n grantResults" + grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getActivity(), "This app requires Storage Permission to continue", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = createImageFile();

                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(getActivity(),
                            "com.example.android.fileprovider",
                            photoFile);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }

            // Continue only if the File was successfully created

        } catch (NullPointerException exc) {
            exc.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        mediaScanIntent.setData(Uri.fromFile(f));
        getActivity().sendBroadcast(mediaScanIntent);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            try {
                galleryAddPic();

                onContentUriCapturedListner.onImageContentUriCaptured(photoURI);

                getActivity()
                        .getSupportFragmentManager()
                        .popBackStackImmediate();

            } catch (NullPointerException exc) {
                exc.printStackTrace();
            }
        }
    }


    public void setCurrentInstance(CategoryShopFragment currentInstance) {
        onContentUriCapturedListner = currentInstance;
    }


    private void setPic(ImageView cameraPhoto) {
        // Get the dimensions of the View
        int targetW = cameraPhoto.getWidth();
        int targetH = cameraPhoto.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        cameraPhoto.setImageBitmap(bitmap);
    }
}
