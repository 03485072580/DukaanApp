package com.example.fasih.dukaanapp.home.fragments.sellerPageResources;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.adapter.MyShopsListAdapter;
import com.example.fasih.dukaanapp.home.activities.MallActivity;
import com.example.fasih.dukaanapp.home.interfaces.KeepHandleFragments;
import com.example.fasih.dukaanapp.models.ShopProfileSettings;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Fasih on 03/16/19.
 */

public class SearchUsernameFragment extends Fragment {

    private RecyclerView shopsContainer;
    private ArrayList<ShopProfileSettings> dataSet;
    private KeepHandleFragments keepHandleFragments;

    //Firebase Stuff
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseMethods firebaseMethods;
    private String currentUserID = null;
    private ValueEventListener listener;
    private Query query;

    public SearchUsernameFragment() {
        this.dataSet = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_username, container, false);
        if (savedInstanceState == null) {

            setupFragmentWidgets(view);
            setupFirebase();
            setupRecyclerView(dataSet);
        } else {
            setupFragmentWidgets(view);
            setupFirebase();

            dataSet = savedInstanceState.getParcelableArrayList("dataSet");
            setupRecyclerView(dataSet);

        }

        return view;
    }

    public void setupFragmentWidgets(View view) {
        shopsContainer = view.findViewById(R.id.shopsContainer);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Ensures that the Activity onCreate() Successfully Executed
        setupActivityListener(getActivity());
    }

    public void setupActivityListener(FragmentActivity activity) {
        keepHandleFragments = (MallActivity) activity;
        keepHandleFragments
                .onCustomAttachFragment(getString(R.string.shopsListFragment)
                        , getString(R.string.searchUsernameFragment));
    }

    private void setupRecyclerView(ArrayList<ShopProfileSettings> shopDataList) {
        try {
            shopsContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
            MyShopsListAdapter shopsListAdapter = new MyShopsListAdapter(getActivity());
            shopsListAdapter.setDataSet(shopDataList);
            shopsContainer.setAdapter(shopsListAdapter);
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("dataSet", dataSet);
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
        if (query != null) {
            query.removeEventListener(listener);
        }
    }

    public void setDataSet(ShopProfileSettings dataSet) {
        Log.d("TAG1234", "setDataSet: ");
        this.dataSet.add(dataSet);
    }
}
