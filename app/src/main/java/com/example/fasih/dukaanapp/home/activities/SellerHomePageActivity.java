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

import com.example.fasih.dukaanapp.R;
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
        setContentView(R.layout.activity_seller_home_page);
        setUpTransluscentStatusBar();
        setupActivityWidgets();
        setupFirebase();

    }

    private void setupActivityWidgets() {
        mall = findViewById(R.id.mall);
        shop = findViewById(R.id.shop);


        //setting up Listners
        mall.setOnClickListener(this);
        shop.setOnClickListener(this);
    }

    private void setUpTransluscentStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    @Override
    public void onClick(final View view) {
        try {
            Query query = myRef
                    .child(getString(R.string.db_shop_profile_settings_node))
                    .orderByChild(getString(R.string.db_field_user_id))
                    .equalTo(currentUserID);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    HashMap<String, Object> shopProfileSettingsMap = (HashMap<String, Object>) dataSnapshot.child(currentUserID).getValue();
                    if (TextUtils.isEmpty((String) shopProfileSettingsMap.get(getString(R.string.db_field_shop_category)))) {
                        showCategoryScreen(view);
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

    private void showCategoryScreen(View view) {
        if (view.getId() == R.id.mall) {
            categorySelected(getString(R.string.mall));

        }
        if (view.getId() == R.id.shop) {
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
                                Log.d("TAG1234", "onSuccess: Successfully Updated");
                            }
                        });
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
