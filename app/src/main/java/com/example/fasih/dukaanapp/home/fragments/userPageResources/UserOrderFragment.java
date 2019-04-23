package com.example.fasih.dukaanapp.home.fragments.userPageResources;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.adapter.OrderProductsAdapter;
import com.example.fasih.dukaanapp.home.interfaces.OnRecyclerImageSelectedListener;
import com.example.fasih.dukaanapp.maps.userMaps.activities.UserMapsActivity;
import com.example.fasih.dukaanapp.models.Orders;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserOrderFragment extends Fragment
        implements OnRecyclerImageSelectedListener {

    @Override
    public void onClickGridImage(int position, View view, Products currentSelectedProduct) {

    }

    @Override
    public void onClickGridImage(int position, View view, String Url) {

        Intent intent = new Intent(getActivity(), UserMapsActivity.class);
        startActivity(intent);

    }


    private RecyclerView currentOrdersContainer;
    private OrderProductsAdapter adapter;
    private ArrayList<Orders> userViewOrdersList;

    //Firebase Stuff
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseMethods firebaseMethods;
    private String currentUserID = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_order, container, false);
        setupFragmentWidgets(view);
        setupFirebase();
        return view;
    }

    private void setupFragmentWidgets(View view) {

        currentOrdersContainer = view.findViewById(R.id.currentOrdersContainer);
    }

    public void setupRecyclerView(ArrayList<Orders> userViewOrdersList) {

        if (userViewOrdersList != null)
            if (!userViewOrdersList.isEmpty()) {

                adapter = new OrderProductsAdapter(getContext(), userViewOrdersList);
                adapter.setupOnItemClickListener(this);
                currentOrdersContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
                currentOrdersContainer.setAdapter(adapter);

            }
    }


    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseMethods = new FirebaseMethods(getActivity(), getString(R.string.userOrderFragment));

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    currentUserID = user.getUid();
                    queryDBCartProducts(currentUserID);
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




    private void queryDBCartProducts(String currentUserID) {

        userViewOrdersList = new ArrayList<>();
        myRef
                .child(getString(R.string.db_orders_node))
                .child(currentUserID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            userViewOrdersList.clear();
                            Map<String, Object> userMap = (HashMap<String, Object>) dataSnapshot.getValue();
                            Collection<Object> shopsCollection = userMap.values();
                            for (Object shop : shopsCollection) {
                                HashMap<String, Object> orderMap = (HashMap<String, Object>) shop;
                                Collection<Object> orderCollection = orderMap.values();
                                for(Object order: orderCollection)
                                {
                                    HashMap<String, Object> singleOrder = (HashMap<String, Object>) order;
                                    Orders orders = new Orders();
                                    orders.setProduct_name((String) singleOrder.get(getString(R.string.db_field_product_name)));
                                    orders.setOrder_price((String) singleOrder.get(getString(R.string.db_field_order_price)));
                                    orders.setTimeStamp((String) singleOrder.get(getString(R.string.db_field_timeStamp)));
                                    orders.setOrder_status((String) singleOrder.get(getString(R.string.db_field_order_status)));
                                    orders.setUser_id((String) singleOrder.get(getString(R.string.db_field_user_id)));
                                    orders.setOrder_id((String) singleOrder.get(getString(R.string.db_field_order_id)));
                                    orders.setProduct_id((String) singleOrder.get(getString(R.string.db_field_product_id)));
                                    orders.setShop_id((String) singleOrder.get(getString(R.string.db_field_shop_id)));

                                    userViewOrdersList.add(orders);
                                }

                            }
                            setupRecyclerView(userViewOrdersList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
