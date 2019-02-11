package com.example.fasih.dukaanapp.login.fragment;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Fasih on 01/12/19.
 */

public class ForgotPasswordFragment extends Fragment {

    private EditText email;
    private LinearLayout recoverPassword;
    private TextView reSendEmail;
    private String emailResetRequest;
    private ProgressBar loginProgress;
    //Firebase Stuff
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_forgot, container, false);
        setupFragmentWidgets(view);
        setupFirebase();
        return view;
    }


    public void setupFragmentWidgets(View view) {
        email = view.findViewById(R.id.email);
        recoverPassword = view.findViewById(R.id.recoverPassword);
        reSendEmail = view.findViewById(R.id.reSendEmail);
        loginProgress = view.findViewById(R.id.registerProgress);


        recoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgress.setVisibility(View.VISIBLE);
                emailResetRequest = email.getText().toString();
                if (!TextUtils.isEmpty(emailResetRequest)) {
                    if (emailResetRequest.contains("@")) {
                        mAuth.sendPasswordResetEmail(emailResetRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loginProgress.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), getString(R.string.recover_password), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.bad_email_format), Toast.LENGTH_SHORT).show();
                        loginProgress.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.missing_one_field), Toast.LENGTH_SHORT).show();
                    loginProgress.setVisibility(View.GONE);
                }
            }
        });
        reSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailResetRequest = email.getText().toString();
                if (!TextUtils.isEmpty(emailResetRequest)) {

                    mAuth.sendPasswordResetEmail(emailResetRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), getString(R.string.email_verification_required), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
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
