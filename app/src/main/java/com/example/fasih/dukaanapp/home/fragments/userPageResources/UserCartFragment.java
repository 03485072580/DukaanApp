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

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.adapter.CartProductsAdapter;
import com.example.fasih.dukaanapp.home.interfaces.OnRecyclerImageSelectedListener;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.order.activities.OrderPageActivity;
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

public class UserCartFragment extends Fragment
        implements OnRecyclerImageSelectedListener {

    @Override
    public void onClickGridImage(int position, View view, Products currentSelectedProduct) {


        //start the Order Activity Here

        Intent intent = new Intent(getActivity(), OrderPageActivity.class);
        intent.putExtra(getString(R.string.currentSelectedProduct), currentSelectedProduct);
        startActivity(intent);
    }

    @Override
    public void onClickGridImage(int position, View view, String Url) {

    }


    private RecyclerView cartProductsContainer;
    private CartProductsAdapter adapter;

    //Firebase Stuff
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseMethods firebaseMethods;
    private String currentUserID = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_cart, container, false);
        setupFragmentWidgets(view);
        setupFirebase();
        return view;
    }

    public void setupFragmentWidgets(View view) {
        cartProductsContainer = view.findViewById(R.id.cartProductsContainer);
    }


    public void setupRecyclerView(ArrayList<Products> userViewProductsList) {

        if (userViewProductsList != null)
            if (!userViewProductsList.isEmpty()) {

                adapter = new CartProductsAdapter(getContext(), userViewProductsList);
                adapter.setupOnItemClickListener(this);
                cartProductsContainer.setLayoutManager(new LinearLayoutManager(getActivity()));
                cartProductsContainer.setAdapter(adapter);

            }
    }


    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseMethods = new FirebaseMethods(getActivity(), getString(R.string.userCartFragment));

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


    private void queryDBCartProducts(final String currentUserID) {

        final ArrayList<Products> userViewProductsList = new ArrayList<>();

        myRef
                .child(getString(R.string.db_cart_node))
                .child(currentUserID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            Map<String, Object> allProductMap = (Map<String, Object>) dataSnapshot.getValue();
                            Collection<Object> cartProducts = allProductMap.values();
                            userViewProductsList.clear();
                            for (Object cartProduct : cartProducts) {
                                Products product = new Products();
                                if (getActivity() != null) {
                                    product.setProduct_name((String) ((HashMap<String, Object>) cartProduct).get(getString(R.string.db_field_product_name)));
                                    product.setProduct_category((String) ((HashMap<String, Object>) cartProduct).get(getString(R.string.db_field_product_category)));
                                    product.setProduct_image_url((String) ((HashMap<String, Object>) cartProduct).get(getString(R.string.db_field_product_image_url)));
                                    product.setProduct_description((String) ((HashMap<String, Object>) cartProduct).get(getString(R.string.db_field_product_description)));
                                    product.setProduct_price((String) ((HashMap<String, Object>) cartProduct).get(getString(R.string.db_field_product_price)));
                                    product.setProduct_warranty((String) ((HashMap<String, Object>) cartProduct).get(getString(R.string.db_field_product_warranty)));
                                    product.setProduct_stock((String) ((HashMap<String, Object>) cartProduct).get(getString(R.string.db_field_product_stock)));
                                    product.setTimeStamp((String) ((HashMap<String, Object>) cartProduct).get(getString(R.string.db_field_timeStamp)));
                                    product.setProduct_id((String) ((HashMap<String, Object>) cartProduct).get(getString(R.string.db_field_product_id)));
                                    product.setProduct_rating((Long) ((HashMap<String, Object>) cartProduct).get(getString(R.string.db_field_product_rating)));
                                    product.setShop_id((String) ((HashMap<String, Object>) cartProduct).get(getString(R.string.db_field_shop_id)));
                                    userViewProductsList.add(product);
                                }

                            }

                            setupRecyclerView(userViewProductsList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
