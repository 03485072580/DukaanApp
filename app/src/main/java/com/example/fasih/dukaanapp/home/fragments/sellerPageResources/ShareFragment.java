package com.example.fasih.dukaanapp.home.fragments.sellerPageResources;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.interfaces.OnBackButtonPressedListener;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.example.fasih.dukaanapp.utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Fasih on 02/25/19.
 */

public class ShareFragment extends Fragment implements OnBackButtonPressedListener {
    private RelativeLayout fragmentFrameHolder, shareFragmentFrameHolder;

    private ImageView backArrow, sharableImage;
    private TextView share;
    private String imageLoadingUrl;
    private Button currencyPicker;
    private EditText productName, productDescription, productPrice, productWarranty, availableStock;
    private MaterialSpinner selectedCategory;

    //Firebase Stuff
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseMethods firebaseMethods;
    private String currentUserID = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        setupFragmentWidgets(view);
        setupFirebase();
        setupUniversalImageLoader();
        setupSharableImage();
        return view;
    }

    private void setupSharableImage() {
        ImageLoader.getInstance().displayImage(imageLoadingUrl, sharableImage);
    }

    private void setupUniversalImageLoader() {
        try {
            ImageLoader.getInstance().init(UniversalImageLoader.getConfiguration(getActivity().getApplicationContext()));
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }
    }


    private void setupFragmentWidgets(View view) {
        share = view.findViewById(R.id.shareImage);
        backArrow = view.findViewById(R.id.backArrow);
        sharableImage = view.findViewById(R.id.sharableImage);
        currencyPicker = view.findViewById(R.id.currencyPicker);
        productName = view.findViewById(R.id.productName);
        productDescription = view.findViewById(R.id.productDescription);
        productPrice = view.findViewById(R.id.productPrice);
        productWarranty = view.findViewById(R.id.productWarranty);
        availableStock = view.findViewById(R.id.availableStock);

        selectedCategory = view.findViewById(R.id.selectedCategory);
        selectedCategory.setItems(getResources().getStringArray(R.array.spinner_items));
//        testImage.setImageURI(Uri.parse(capturedImage));

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    fragmentFrameHolder.setVisibility(View.VISIBLE);
                    shareFragmentFrameHolder.setVisibility(View.GONE);
                    getFragmentManager().popBackStack();

                } catch (NullPointerException exc) {
                    exc.printStackTrace();
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    shareProduct();
                } catch (NullPointerException exc) {
                    exc.printStackTrace();
                }
            }
        });

        sharableImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!TextUtils.isEmpty(imageLoadingUrl)) {

                        ZoomImageViewFragment zoomImageViewFragment = new ZoomImageViewFragment();
                        zoomImageViewFragment.setZoomImageViewUri(imageLoadingUrl);
                        getFragmentManager()
                                .beginTransaction()
                                .add(R.id.fragmentContainer3, zoomImageViewFragment, getString(R.string.zoomImageViewFragment))
                                .commitNowAllowingStateLoss();
                    }
                } catch (NullPointerException exc) {
                    exc.printStackTrace();
                }
            }
        });

        productPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!currencyPicker.isEnabled())
                    currencyPicker.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        currencyPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");
                if (!TextUtils.isEmpty(productPrice.getText().toString())) { // dialog title
                    picker.setListener(new CurrencyPickerListener() {
                        @Override
                        public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                            // Implement your code here
                            Editable editable = new SpannableStringBuilder(productPrice.getText().toString() + " " + code + " " + symbol);
                            productPrice.setText(editable.toString());
                            currencyPicker.setEnabled(false);
                            picker.dismissAllowingStateLoss();
                        }
                    });
                    picker.show(getFragmentManager(), "CURRENCY_PICKER");
                } else {
                    productPrice.requestFocus();
                    productPrice.setError("Fill this Field before selecting Currency");
                }
            }
        });

    }

    private void shareProduct() {
        if (!TextUtils.isEmpty(productName.getText().toString()) && !TextUtils.isEmpty(productDescription.getText().toString()) &&
                !TextUtils.isEmpty(productPrice.getText().toString()) && !TextUtils.isEmpty(productWarranty.getText().toString())
                && !TextUtils.isEmpty(availableStock.getText().toString()) && !TextUtils.isEmpty(imageLoadingUrl)) {
            if (selectedCategory.getSelectedIndex() != 0) {
                //All Correct. Firebase Server work Here
                firebaseMethods.uploadProduct(productName.getText().toString()
                        , selectedCategory.getText().toString()
                        , imageLoadingUrl
                        , productDescription.getText().toString()
                        , productPrice.getText().toString()
                        , productWarranty.getText().toString()
                        , availableStock.getText().toString()
                        , getTimeStamp());

            } else {
                try {
                    Toast.makeText(getActivity(), "Please Select an Option to continue Furhter", Toast.LENGTH_SHORT).show();

                } catch (NullPointerException exc) {
                    exc.printStackTrace();
                }
            }

        } else {
            try {
                Toast.makeText(getActivity(), "All Fields & Pictures Are Mandatory", Toast.LENGTH_SHORT).show();

            } catch (NullPointerException exc) {
                exc.printStackTrace();
            }
        }


    }

    public void setXmlResources(RelativeLayout fragmentFrameHolder, RelativeLayout shareFragmentFrameHolder) {
        this.fragmentFrameHolder = fragmentFrameHolder;
        this.shareFragmentFrameHolder = shareFragmentFrameHolder;
    }

    @Override
    public void onBackPressed() {
        try {

            Log.d("TAG1234", "onBackPressed:222 shareFragment");
            fragmentFrameHolder.setVisibility(View.VISIBLE);
            shareFragmentFrameHolder.setVisibility(View.GONE);
            getFragmentManager().popBackStackImmediate();
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }
    }

    public void setImageLoadingUrl(String imageLoadingUrl) {
        this.imageLoadingUrl = imageLoadingUrl;
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseMethods = new FirebaseMethods(getActivity(), getString(R.string.shareFragment));

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    currentUserID = user.getUid();
                }

            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    public String getTimeStamp() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy 'Time' HH:mm:ss aaa", Locale.US);
        String timeStamp = sdf.format(date);
        return timeStamp;
    }
}
