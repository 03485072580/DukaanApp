package com.example.fasih.dukaanapp.home.fragments.sellerPageResources;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
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
import com.example.fasih.dukaanapp.utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;

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
    private Uri photoURI;


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
                            "com.example.fasih.dukaanapp.provider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
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

        MediaScannerConnection.scanFile(
                getActivity().getApplicationContext(),
                new String[]{currentPhotoPath},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.v("TAG1234",
                                "file " + path + " was scanned seccessfully: " + uri);
                    }
                });
//
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(currentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        getActivity().sendBroadcast(mediaScanIntent);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                galleryAddPic();
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setCapturedImageUri(photoURI);
                EventBus.getDefault().postSticky(messageEvent);

            } catch (NullPointerException exc) {
                exc.printStackTrace();
            }
            Log.d("TAG1234", "onActivityResult: ");
        }
    }

    /**
     * Managing multiple full-sized images can be tricky with limited memory.
     * If you find your application running out of memory after displaying just a few images,
     * you can dramatically reduce the amount of dynamic heap used by expanding the JPEG into
     * a memory array that's already scaled to match the size of the destination view.
     * The following example method demonstrates this technique.
     *
     * @param cameraPhoto
     */
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
