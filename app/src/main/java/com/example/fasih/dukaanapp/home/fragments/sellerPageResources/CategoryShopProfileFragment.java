package com.example.fasih.dukaanapp.home.fragments.sellerPageResources;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.example.fasih.dukaanapp.models.ShopProfileSettings;
import com.example.fasih.dukaanapp.models.Users;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.example.fasih.dukaanapp.utils.StringManipulations;
import com.example.fasih.dukaanapp.utils.UniversalImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Fasih on 02/16/19.
 */

public class CategoryShopProfileFragment extends Fragment {


    private CircleImageView profileImage;
    private TextView displayName, brandName, shopAddress, username, email, tvMall;
    private ImageView approvedStatus;
    private EditText displayNameEditText, shopAddressEditText, city, country;
    private Button updateProfileBtn;
    private ShopProfileSettings shopSettings;
    private ImageView backArrow;
    private RelativeLayout mallLayout;

    //Firebase Stuff
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseMethods firebaseMethods;
    private ValueEventListener valueEventListener;
    private Query query;
    private String currentUserID = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_shop_profile, container, false);
        setupFragmentWidgets(view);
        setupUniversalImageLoader();
        setupFirebase();
        return view;
    }

    private void setupUniversalImageLoader() {
        try {
            ImageLoader.getInstance().init(UniversalImageLoader.getConfiguration(getActivity().getApplicationContext()));
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }

    }

    public void setupFragmentWidgets(View view) {
        profileImage = view.findViewById(R.id.profileImage);
        displayName = view.findViewById(R.id.displayName);
        brandName = view.findViewById(R.id.brandName);
        shopAddress = view.findViewById(R.id.shopAddress);
        approvedStatus = view.findViewById(R.id.approvedStatus);
        displayNameEditText = view.findViewById(R.id.displayNameEditText);
        shopAddressEditText = view.findViewById(R.id.shopAddressEditText);
        city = view.findViewById(R.id.city);
        country = view.findViewById(R.id.country);
        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        updateProfileBtn = view.findViewById(R.id.updateProfileBtn);
        backArrow = view.findViewById(R.id.backArrow);
        mallLayout = view.findViewById(R.id.mallLayout);
        tvMall = view.findViewById(R.id.tvMall);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getActivity().finish();

                } catch (NullPointerException exc) {
                    exc.printStackTrace();
                }
            }
        });

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //responsible for updating the seller's profile fields
                final ProgressDialogFragment dialogFragment = new ProgressDialogFragment();

                if (!TextUtils.isEmpty(displayNameEditText.getText().toString())
                        && !TextUtils.isEmpty(shopAddressEditText.getText().toString())) {
                    dialogFragment.show(getFragmentManager(), getString(R.string.dialogFragment));
                    dialogFragment.setCancelable(false);

                    String displayName = displayNameEditText.getText().toString();
                    String shopAddress = shopAddressEditText.getText().toString();
                    String city = CategoryShopProfileFragment.this.city.getText().toString();
                    String country = CategoryShopProfileFragment.this.country.getText().toString();
                    if (shopSettings != null) {

                        if (displayName.contains(" ")) {
                            shopSettings.setFirst_name(StringManipulations.getFirstName(displayName));
                            shopSettings.setLast_name(StringManipulations.getLastName(displayName));
                        } else {
                            shopSettings.setFirst_name(StringManipulations.getFirstName(displayName));
                            shopSettings.setLast_name("");
                        }

                        shopSettings.setShop_address(shopAddress);
                        shopSettings.setCity(city);
                        shopSettings.setCountry(country);

                        final Users users = new Users(shopSettings.getUser_id()
                                , shopSettings.getFirst_name()
                                , shopSettings.getLast_name()
                                , shopSettings.getUser_name()
                                , shopSettings.getEmail()
                                , shopSettings.getScope());

                        myRef
                                .child(getString(R.string.db_shop_profile_settings_node))
                                .child(shopSettings.getUser_id())
                                .setValue(shopSettings)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //succeeded... Now also Update Users Node and Mall Node
                                            myRef
                                                    .child(getString(R.string.db_users_node))
                                                    .child(users.getUser_id())
                                                    .setValue(users);

                                            myRef
                                                    .child(getString(R.string.db_mall_node))
                                                    .child(shopSettings.getMall_id())
                                                    .child(currentUserID)
                                                    .setValue(shopSettings);
                                        } else {
                                            //failed
                                        }
                                        dialogFragment.dismissAllowingStateLoss();
                                    }
                                });
                    }
                } else {
                    try {
                        Toast.makeText(getActivity(), getString(R.string.must_provide_name_address), Toast.LENGTH_SHORT).show();
                    } catch (NullPointerException exc) {
                        exc.printStackTrace();
                    }
                }

            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show dialog
                TakePhotoFragment fragment = new TakePhotoFragment();
                fragment.updateProfileImage(profileImage);
                fragment.show(getFragmentManager(), getString(R.string.takePhotoFragment));
            }
        });
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        firebaseMethods = new FirebaseMethods(getActivity(), getString(R.string.activity_login));

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    currentUserID = user.getUid();
                    setupFirebaseDatabase(currentUserID);
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
        if (query != null) {
            query.removeEventListener(valueEventListener);
        }
    }

    public void setupFirebaseDatabase(final String currentUserID) {

        query = myRef.child(getString(R.string.db_shop_profile_settings_node))
                .orderByChild(getString(R.string.db_field_user_id))
                .equalTo(currentUserID);

        valueEventListener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    HashMap<String, Object> shopMap = (HashMap<String, Object>) dataSnapshot.child(currentUserID).getValue();

                    shopSettings = new ShopProfileSettings(
                            (String) shopMap.get(getString(R.string.db_field_user_id))
                            , (String) shopMap.get(getString(R.string.db_field_first_name))
                            , (String) shopMap.get(getString(R.string.db_field_last_name))
                            , (String) shopMap.get(getString(R.string.db_field_user_name))
                            , (String) shopMap.get(getString(R.string.db_field_email))
                            , (String) shopMap.get(getString(R.string.db_field_scope))
                            , (String) shopMap.get(getString(R.string.db_field_shop_address))
                            , (String) shopMap.get(getString(R.string.db_field_city))
                            , (String) shopMap.get(getString(R.string.db_field_country))
                            , (Boolean) shopMap.get(getString(R.string.db_field_admin_approved))
                            , (String) shopMap.get(getString(R.string.db_field_shop_category))
                            , (String) shopMap.get(getString(R.string.db_field_profile_image_url))
                            , (String) shopMap.get(getString(R.string.db_field_mall_id))
                    );
                    updateProfileFields(shopSettings.getFirst_name() + shopSettings.getLast_name()
                            , shopSettings.getUser_name()
                            , shopSettings.getEmail()
                            , shopSettings.getShop_address()
                            , shopSettings.getCity()
                            , shopSettings.getCountry()
                            , shopSettings.getAdmin_approved()
                            , shopSettings.getProfile_image_url()
                            , shopSettings.getMall_id()
                    );


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateProfileFields(String displayName
            , String user_name
            , String email
            , String shop_address
            , String city
            , String country
            , Boolean admin_approved
            , String profile_image_url
            , String mall_id) {


        this.displayName.setText(displayName);
        this.displayNameEditText.setText(displayName);
        this.username.setText(user_name);
        this.brandName.setText(user_name);
        this.email.setText(email);
        this.shopAddress.setText(shop_address);
        this.shopAddressEditText.setText(shop_address);
        this.city.setText(city);
        this.country.setText(country);
        if (admin_approved) {
            approvedStatus.setImageResource(R.drawable.ic_user_verified);
        } else {
            approvedStatus.setImageResource(R.drawable.ic_user_not_verified);
        }

        if (!TextUtils.isEmpty(mall_id)) {
            //show the shop its relevant mall id
            mallLayout.setVisibility(View.VISIBLE);
            tvMall.setText(mall_id);
        } else {
            //hide mall id field
            mallLayout.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(profile_image_url)) {
            ImageLoader.getInstance().displayImage(profile_image_url, profileImage);
        }


    }
}
