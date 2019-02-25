package com.example.fasih.dukaanapp.home.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.CategoryMallFragment;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.CategoryShopFragment;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerHomePageActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView mall, shop;
    private RelativeLayout categoryScreenContainer, fragmentFrameHolder;
    //Firebase Stuff
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private String currentUserID = null;
    private ShopProfileSettings shopSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFirebase();

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
                        setContentView(R.layout.activity_seller_home_page);
                        setUpTransluscentStatusBar();
                        setupActivityWidgets();
                    } else {
                        setContentView(R.layout.activity_seller_home_page);
                        setUpTransluscentStatusBar();
                        setupActivityWidgets();
                        categoryScreenContainer.setVisibility(View.GONE);
                        //todo there exists either shop or mall
                        if (shopProfileSettingsMap.get(getString(R.string.db_field_shop_category))
                                .equals(getString(R.string.mall))) {

                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragmentContainer, new CategoryMallFragment()
                                            , getString(R.string.categoryMallFragment))
                                    .commit();
                        } else {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragmentContainer, new CategoryShopFragment()
                                            , getString(R.string.categoryShopFragment))
                                    .commitAllowingStateLoss();
                        }


                        fragmentFrameHolder.setVisibility(View.VISIBLE);

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
                                Log.d("TAG1234", "categorySelected: shopSettings onSuccess");
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragmentContainer
                                                , new CategoryMallFragment()
                                                , getString(R.string.categoryMallFragment))
                                        .commit();
                                categoryScreenContainer.setVisibility(View.GONE);
                                fragmentFrameHolder.setVisibility(View.VISIBLE);
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
                                        .replace(R.id.fragmentContainer
                                                , new CategoryShopFragment()
                                                , getString(R.string.categoryShopFragment))
                                        .commit();
                                categoryScreenContainer.setVisibility(View.GONE);
                                fragmentFrameHolder.setVisibility(View.VISIBLE);
                            }
                        });
        }

    }


    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    currentUserID = user.getUid();
                    setupView();

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
}
