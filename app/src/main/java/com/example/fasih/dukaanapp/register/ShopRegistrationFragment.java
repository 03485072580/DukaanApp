package com.example.fasih.dukaanapp.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.utils.Constants;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.example.fasih.dukaanapp.utils.StringManipulations;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Fasih on 01/01/19.
 */

public class ShopRegistrationFragment extends Fragment {

    private EditText firstName, lastName, shopName, shopAddress, email, password;
    private LinearLayout registerButton;
    private ProgressBar registerProgress;
    private String scope = Constants.scope = "shop";
    private Boolean approvedByAdmin = false;

    //Firebase Stuff
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseMethods firebaseMethods;
    private String currentUserID = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration_shop, container, false);
        setupFragmentWidgets(view);
        setupFirebase();
        return view;
    }

    public void setupFragmentWidgets(View view) {
        firstName = view.findViewById(R.id.first_name);
        lastName = view.findViewById(R.id.last_name);
        shopName = view.findViewById(R.id.username);
        shopAddress = view.findViewById(R.id.address);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        registerButton = view.findViewById(R.id.signUp);
        registerProgress = view.findViewById(R.id.registerProgress);

        //Listeners
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(firstName.getText().toString()) && !TextUtils.isEmpty(lastName.getText().toString()) &&
                        !TextUtils.isEmpty(shopName.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) &&
                        !TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(shopAddress.getText().toString()))
                    setupDatabase();
                else
                    Toast.makeText(getActivity(), getString(R.string.warning_required_fields), Toast.LENGTH_SHORT).show();

            }
        });


    }

    /**
     * Firebase Setup Initialization
     */

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseMethods = new FirebaseMethods(getActivity(), getString(R.string.activity_register));

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

    private void checkIfShopNameAlreadyExists(final String shopName) {

        registerProgress.setVisibility(View.VISIBLE);

        Query query = myRef
                .child(getString(R.string.db_users_node))
                .orderByChild(getString(R.string.db_field_user_name))
                .equalTo(shopName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                registerProgress.setVisibility(View.GONE);

                if (!dataSnapshot.exists()) {
                    firebaseMethods.updateProgress(registerProgress);
                    firebaseMethods.addNewUser(firstName.getText().toString(), lastName.getText().toString()
                            , shopName, StringManipulations.toLowerCase(email.getText().toString()), password.getText().toString()
                            , "", "", scope, "", shopAddress.getText().toString(), approvedByAdmin);

                }
                //it will execute only if a match found
                if (dataSnapshot.exists()) {
                    //Found a match
                    Toast.makeText(getActivity(), getString(R.string.shop_already_existent), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                registerProgress.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getString(R.string.something_went_wrong) + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setupDatabase() {
        String shopName = this.shopName.getText().toString();
        checkIfShopNameAlreadyExists(StringManipulations.toLowerCaseUsername(shopName));

    }
}
