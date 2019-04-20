package com.example.fasih.dukaanapp.home.fragments.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressIntentService extends IntentService {

    String country,city;


    public FetchAddressIntentService() {
        super(null); // That was the lacking constructor
    }

    public FetchAddressIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            List<Address> fromLocationLatLng = null;
            try {
                fromLocationLatLng = geocoder.getFromLocation(intent.
                                getDoubleExtra(getString(R.string.currentLat), 0)
                        , intent.
                                getDoubleExtra(getString(R.string.currentLng), 0)

                        , 1);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException exc) {
                exc.printStackTrace();
            }

            if (fromLocationLatLng == null || fromLocationLatLng.size() == 0) {

                deliverResultToReceiver(Constants.FAILURE_RESULT, getString(R.string.something_went_wrong));
            } else {
                Address address = fromLocationLatLng.get(0);
                ArrayList<String> addressFragments = new ArrayList<String>();

                // Fetch the address lines using getAddressLine,
                // join them, and send them to the thread.
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    country = address.getCountryName();
                    city = address.getLocality();
                    addressFragments.add(address.getAddressLine(i));
                }
                deliverResultToReceiver(Constants.SUCCESS_RESULT,
                        TextUtils.join(System.getProperty("line.separator"),
                                addressFragments));
            }

        }
    }

    private void deliverResultToReceiver(int resultCode, String message) {
//        Bundle bundle = new Bundle();
//        bundle.putString(Constants.RESULT_DATA_KEY, message);
        Intent myBroadcastHelperIntent = new Intent(Constants.RECEIVER);
        myBroadcastHelperIntent.putExtra(Constants.RESULT_DATA_KEY, message);
        myBroadcastHelperIntent.putExtra(Constants.RESULT_CODE, resultCode);
        myBroadcastHelperIntent.putExtra(getString(R.string.city),city);
        myBroadcastHelperIntent.putExtra(getString(R.string.country),country);
        myBroadcastHelperIntent.setPackage("com.example.fasih.dukaanapp");
        LocalBroadcastManager.getInstance(this).sendBroadcast(myBroadcastHelperIntent);
    }

}
