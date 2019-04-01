package com.example.fasih.dukaanapp.home.fragments.sellerPageResources;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Created by Fasih on 01/16/19.
 */

public class ShopsListFragment extends Fragment {

    private RecyclerView shopsContainer;
    private Toolbar toolbar;
    private ArrayList<ShopProfileSettings> shopsListArray;
    private SearchView search;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shops_list, container, false);
        setupFragmentWidgets(view);
        setupFirebase();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Ensures that the Activity onCreate() Successfully Executed
        setupActivityListener(getActivity());
    }

    private void setupFragmentWidgets(View view) {
        shopsContainer = view.findViewById(R.id.shopsContainer);
        toolbar = view.findViewById(R.id.topToolBar);
        search = view.findViewById(R.id.search);
        try {

            ((MallActivity) getActivity()).setSupportActionBar(toolbar);
        } catch (NullPointerException exc) {
            Log.d("TAG1234", "setupFragmentWidgets: NullPointerException" + exc.getMessage());
        }

        //Listener's Section
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseMethods.searchQueryTextByUsername(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


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

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseMethods = new FirebaseMethods(getActivity(), getString(R.string.shopsListFragment));

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    currentUserID = user.getUid();
                    setupShopsDataSet();
                }

            }
        };


    }

    private void setupShopsDataSet() {

        shopsListArray = new ArrayList<>();

        query = myRef
                .child(getString(R.string.db_mall_node))
                .child(currentUserID)
                .orderByChild(getString(R.string.db_field_mall_id))
                .equalTo(currentUserID);
        listener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HashMap<String, Object> shopsMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    if (shopsMap != null) {
                        Collection<Object> shopsList = shopsMap.values();
                        if (!shopsListArray.isEmpty()) {
                            shopsListArray.clear();
                        }

                        for (Iterator<Object> it = shopsList.iterator(); it.hasNext(); ) {
                            HashMap object = (HashMap) it.next();

                            shopsListArray.add(
                                    new ShopProfileSettings(
                                            (String) object.get(getString(R.string.db_field_user_id))
                                            , (String) object.get(getString(R.string.db_field_first_name))
                                            , (String) object.get(getString(R.string.db_field_last_name))
                                            , (String) object.get(getString(R.string.db_field_user_name))
                                            , (String) object.get(getString(R.string.db_field_email))
                                            , (String) object.get(getString(R.string.db_field_scope))
                                            , (String) object.get(getString(R.string.db_field_shop_address))
                                            , (String) object.get(getString(R.string.db_field_city))
                                            , (String) object.get(getString(R.string.db_field_country))
                                            , (Boolean) object.get(getString(R.string.db_field_admin_approved))
                                            , (String) object.get(getString(R.string.db_field_shop_category))
                                            , (String) object.get(getString(R.string.db_field_profile_image_url))
                                            , (String) object.get(getString(R.string.db_field_mall_id))
                                    )
                            );
                        }//end for
                        setupRecyclerView(shopsListArray);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    public void setupActivityListener(FragmentActivity activity) {
        keepHandleFragments = (MallActivity) activity;
        keepHandleFragments
                .onCustomAttachFragment(getString(R.string.activity_mall)
                        , getString(R.string.shopsListFragment));
    }
}
