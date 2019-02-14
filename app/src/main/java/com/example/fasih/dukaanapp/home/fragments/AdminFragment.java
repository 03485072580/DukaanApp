package com.example.fasih.dukaanapp.home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.activities.UserHomePageActivity;
import com.example.fasih.dukaanapp.register.RegisterActivity;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by Fasih on 11/07/18.
 */

public class AdminFragment extends Fragment {

    private TextView signUp;
    private Button signIn;

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
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        setupFragmentWidgets(view);
        return view;
    }

    private void setupFragmentWidgets(View view) {
        signUp = view.findViewById(R.id.textView3);
        signIn = view.findViewById(R.id.button2);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra(getString(R.string.adminFragment), getString(R.string.adminFragment));
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserHomePageActivity.class);
                startActivity(intent);
            }
        });
    }
}
