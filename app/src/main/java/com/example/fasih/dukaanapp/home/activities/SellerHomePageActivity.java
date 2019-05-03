package com.example.fasih.dukaanapp.home.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.CategoryShopFragment;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.ShareFragment;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.ZoomImageViewFragment;
import com.example.fasih.dukaanapp.home.interfaces.OnBackButtonPressedListener;
import com.example.fasih.dukaanapp.models.FCMTokens;
import com.example.fasih.dukaanapp.models.ShopProfileSettings;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mynameismidori.currencypicker.CurrencyPicker;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerHomePageActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView mall, shop;
    private RelativeLayout categoryScreenContainer, fragmentFrameHolder, shareFragmentFrameHolder;
    private Fragment fragment;
    private OnBackButtonPressedListener onBackButtonPressedListener;
    //Firebase Stuff
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private String currentUserID = null;
    private ShopProfileSettings shopSettings;
    private Boolean isActivityRestarted = false;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFirebase(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isActivityRestarted = true;
        setupFirebase(savedInstanceState);
    }

    private void setupView() {
        try {
            Query query = myRef
                    .child(getString(R.string.db_shop_profile_settings_node))
                    .orderByChild(getString(R.string.db_field_user_id))
                    .equalTo(currentUserID);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    HashMap<String, Object> shopProfileSettingsMap =
                            (HashMap<String, Object>) dataSnapshot
                                    .child(currentUserID)
                                    .getValue();
                    if (TextUtils.isEmpty((String) shopProfileSettingsMap.get(getString(R.string.db_field_shop_category)))) {
                        if (!isActivityRestarted) {
                            setContentView(R.layout.activity_seller_home_page);
                            setUpTransluscentStatusBar();
                            setupActivityWidgets();
                        }

                    } else {
                        if (!isActivityRestarted) {
                            setContentView(R.layout.activity_seller_home_page);
                            setUpTransluscentStatusBar();
                            setupActivityWidgets();
                        }

                        categoryScreenContainer.setVisibility(View.GONE);
                        fragmentFrameHolder.setVisibility(View.GONE);
                        //todo there exists either shop or mall
                        if (shopProfileSettingsMap.get(getString(R.string.db_field_shop_category))
                                .equals(getString(R.string.mall))) {


                            Intent intent = new Intent(SellerHomePageActivity.this, MallActivity.class);
                            intent.putExtra(getString(R.string.db_field_user_id), (String) shopProfileSettingsMap.get(getString(R.string.db_field_user_id)));
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                            finish();
                        } else {

                            CategoryShopFragment categoryShopFragment = new CategoryShopFragment();
                            categoryShopFragment.setXmlResources(fragmentFrameHolder, shareFragmentFrameHolder);
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragmentContainer
                                            , categoryShopFragment
                                            , getString(R.string.categoryShopFragment))
                                    .commitAllowingStateLoss();

                            fragmentFrameHolder.setVisibility(View.VISIBLE);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }
    }

    private void setupActivityWidgets() {
        mall = findViewById(R.id.mall);
        shop = findViewById(R.id.shop);
        categoryScreenContainer = findViewById(R.id.categoryScreenContainer);
        fragmentFrameHolder = findViewById(R.id.fragmentFrameHolder);
        shareFragmentFrameHolder = findViewById(R.id.shareFragmentFrameHolder);


        //setting up Listners
        mall.setOnClickListener(this);
        shop.setOnClickListener(this);
        mall.setTag(getString(R.string.mall));
        shop.setTag(getString(R.string.shop));
    }

    private void setUpTransluscentStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    @Override
    public void onClick(final View view) {
        showCategoryScreen(view);

    }

    private void showCategoryScreen(View view) {
        if (view.getTag().toString().equals(getString(R.string.mall))) {
            categorySelected(getString(R.string.mall));

        }
        if (view.getTag().toString().equals(getString(R.string.shop))) {
            categorySelected(getString(R.string.shop));
        }
    }

    private void categorySelected(String category) {
        if (category.equals(getString(R.string.mall))) {

            Query query = myRef
                    .child(getString(R.string.db_shop_profile_settings_node))
                    .orderByChild(getString(R.string.db_field_user_id))
                    .equalTo(currentUserID);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    shopSettings = new ShopProfileSettings();
                    for (DataSnapshot ds : dataSnapshot.child(currentUserID).getChildren()) {
                        if (ds.getKey().equals(getString(R.string.db_field_admin_approved))) {
                            shopSettings.setAdmin_approved((Boolean) ds.getValue());
                        }
                        if (ds.getKey().equals(getString(R.string.db_field_user_id))) {
                            shopSettings.setUser_id((String) ds.getValue());

                        }
                        if (ds.getKey().equals(getString(R.string.db_field_first_name))) {
                            shopSettings.setFirst_name((String) ds.getValue());

                        }
                        if (ds.getKey().equals(getString(R.string.db_field_last_name))) {
                            shopSettings.setLast_name((String) ds.getValue());
                        }
                        if (ds.getKey().equals(getString(R.string.db_field_user_name))) {
                            shopSettings.setUser_name((String) ds.getValue());
                        }
                        if (ds.getKey().equals(getString(R.string.db_field_email))) {
                            shopSettings.setEmail((String) ds.getValue());
                        }
                        if (ds.getKey().equals(getString(R.string.db_field_scope))) {
                            shopSettings.setScope((String) ds.getValue());
                        }
                        if (ds.getKey().equals(getString(R.string.db_field_shop_address))) {
                            shopSettings.setShop_address((String) ds.getValue());
                        }
                        if (ds.getKey().equals(getString(R.string.db_field_city))) {
                            shopSettings.setCity((String) ds.getValue());
                        }
                        if (ds.getKey().equals(getString(R.string.db_field_country))) {
                            shopSettings.setCountry((String) ds.getValue());
                        }
                        if (ds.getKey().equals(getString(R.string.db_field_shop_category))) {
                            shopSettings.setShop_category(getString(R.string.mall));
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("TAG1234", "onCancelled: " + databaseError.getMessage());
                }
            });
            if (shopSettings != null) {

                myRef
                        .child(getString(R.string.db_shop_profile_settings_node))
                        .child(currentUserID)
                        .setValue(shopSettings)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Navigate to the Desired Activity/Fragment

                                Intent intent = new Intent(SellerHomePageActivity.this, MallActivity.class);
                                intent.putExtra(getString(R.string.db_field_user_id), shopSettings.getUser_id());
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                                finish();
                            }
                        });
            }

        }
        if (category.equals(getString(R.string.shop))) {

            Query query = myRef
                    .child(getString(R.string.db_shop_profile_settings_node))
                    .orderByChild(getString(R.string.db_field_user_id))
                    .equalTo(currentUserID);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    shopSettings = new ShopProfileSettings();
                    for (DataSnapshot ds : dataSnapshot.child(currentUserID).getChildren()) {
                        if (ds.getKey().equals(getString(R.string.db_field_admin_approved))) {
                            shopSettings.setAdmin_approved((Boolean) ds.getValue());
                        }
                        if (ds.getKey().equals(getString(R.string.db_field_user_id))) {
                            shopSettings.setUser_id((String) ds.getValue());

                        }
                        if (ds.getKey().equals(getString(R.string.db_field_first_name))) {
                            shopSettings.setFirst_name((String) ds.getValue());

                        }
                        if (ds.getKey().equals(getString(R.string.db_field_last_name))) {
                            shopSettings.setLast_name((String) ds.getValue());
                        }
                        if (ds.getKey().equals(getString(R.string.db_field_user_name))) {
                            shopSettings.setUser_name((String) ds.getValue());
                        }
                        if (ds.getKey().equals(getString(R.string.db_field_email))) {
                            shopSettings.setEmail((String) ds.getValue());
                        }
                        if (ds.getKey().equals(getString(R.string.db_field_scope))) {
                            shopSettings.setScope((String) ds.getValue());
                        }
                        if (ds.getKey().equals(getString(R.string.db_field_shop_address))) {
                            shopSettings.setShop_address((String) ds.getValue());
                        }
                        if (ds.getKey().equals(getString(R.string.db_field_city))) {
                            shopSettings.setCity((String) ds.getValue());
                        }
                        if (ds.getKey().equals(getString(R.string.db_field_country))) {
                            shopSettings.setCountry((String) ds.getValue());
                        }
                        if (ds.getKey().equals(getString(R.string.db_field_shop_category))) {
                            shopSettings.setShop_category(getString(R.string.shop));
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("TAG1234", "onCancelled: " + databaseError.getMessage());
                }
            });

            if (shopSettings != null)
                myRef
                        .child(getString(R.string.db_shop_profile_settings_node))
                        .child(currentUserID)
                        .setValue(shopSettings)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Navigate to the Desired Activity/Fragment
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragmentContainer
                                                , new CategoryShopFragment()
                                                , getString(R.string.categoryShopFragment))
                                        .commitAllowingStateLoss();
                                categoryScreenContainer.setVisibility(View.GONE);
                                fragmentFrameHolder.setVisibility(View.VISIBLE);
                            }
                        });
        }

    }

    private void sendTokenToServer(String fcm_token, String currentUserID) {

        myRef
                .child(getString(R.string.db_FCMToken_node))
                .child(currentUserID)
                .setValue(new FCMTokens(currentUserID,fcm_token));
    }

    private void setupFirebase(final Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    currentUserID = user.getUid();
                    SharedPreferences preferences = getSharedPreferences(getString(R.string.myPreferences), MODE_PRIVATE);
                    String fcm_token = preferences.getString(getString(R.string.fcm_token), "");
                    if (!fcm_token.equals("")) {
                        sendTokenToServer(fcm_token,currentUserID);
                    }
                    if (savedInstanceState == null) {
                        setupView();
                    } else {
                        if (!isActivityRestarted) {
                            setContentView(R.layout.activity_seller_home_page);
                            setUpTransluscentStatusBar();
                            setupActivityWidgets();
                        }


                        CategoryShopFragment fragmentByTag = (CategoryShopFragment) getSupportFragmentManager()
                                .findFragmentByTag(getString(R.string.categoryShopFragment));
                        if (fragmentByTag != null) {
                            fragmentByTag.setXmlResources(fragmentFrameHolder, shareFragmentFrameHolder);
                            categoryScreenContainer.setVisibility(View.GONE);
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .show(fragmentByTag)
                                    .commitAllowingStateLoss();
                        }

                        fragmentFrameHolder.setVisibility(View.VISIBLE);
                    }
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

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        this.fragment = fragment;
    }

    @Override
    public void onBackPressed() {
        //todo HINT: keep the reference of the previous fragment for Smooth back navigation
        if (fragment instanceof ShareFragment) {

            onBackButtonPressedListener = (OnBackButtonPressedListener) fragment;
            onBackButtonPressedListener.onBackPressed();
            fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.categoryShopFragment));
        } else if (fragment instanceof ZoomImageViewFragment) {

            onBackButtonPressedListener = (OnBackButtonPressedListener) fragment;
            onBackButtonPressedListener.onBackPressed();
            fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.shareFragment));
        } else if (fragment instanceof CurrencyPicker) {
            //do nothing. Let it be handled by the backArrow Button

            if (getSupportFragmentManager().findFragmentByTag("CURRENCY_PICKER") != null) {
                getSupportFragmentManager().popBackStack();
            }

        } else if (fragment instanceof DialogFragment) {
            fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.shareFragment));
        } else {
            super.onBackPressed();
        }

    }
}
