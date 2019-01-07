package com.example.fasih.dukaanapp.login;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.register.RegisterActivity;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.example.fasih.dukaanapp.utils.StringManipulations;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Fasih on 11/07/18.
 */

public class UserFragment extends Fragment {

    private TextView signUp, forgotPassword;
    private EditText email, password;
    private LinearLayout login;
    private ProgressBar loginProgress;

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
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        setupFragmentWidgets(view);

        setupFirebase();
        return view;
    }

    private void setupFragmentWidgets(View view) {
        signUp = view.findViewById(R.id.signUp);
        login = view.findViewById(R.id.login);
        forgotPassword = view.findViewById(R.id.forgotPassword);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        loginProgress = view.findViewById(R.id.loginProgress);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra(getString(R.string.userFragment), getString(R.string.userFragment));
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString();
                String pass = password.getText().toString();

                if (!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pass)) {
                    setupLogin();
                } else {
                    Toast.makeText(getActivity(), R.string.warning_required_fields, Toast.LENGTH_SHORT).show();
                }
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

    private void setupLogin() {

        String username = this.email.getText().toString();
        String password = this.password.getText().toString();

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            if (StringManipulations.contains(username, "@") != null) {

                Boolean isEmail = StringManipulations.contains(username, "@");
                if (isEmail) {
                    firebaseMethods.updateProgress(loginProgress);
                    firebaseMethods.authenticateUser(null, username, password);
                } else {
                    firebaseMethods.updateProgress(loginProgress);
                    firebaseMethods.authenticateUser(StringManipulations.toLowerCaseUsername(username), null, password);
                }

            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.warning_required_fields), Toast.LENGTH_SHORT).show();
        }
    }
}
