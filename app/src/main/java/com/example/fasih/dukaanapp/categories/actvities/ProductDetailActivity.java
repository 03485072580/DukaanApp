package com.example.fasih.dukaanapp.categories.actvities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.ZoomImageViewFragment;
import com.example.fasih.dukaanapp.home.interfaces.KeepHandleFragments;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.example.fasih.dukaanapp.utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.willy.ratingbar.ScaleRatingBar;

public class ProductDetailActivity extends AppCompatActivity
        implements KeepHandleFragments {

    private ImageView backArrow, adToCart, productImage;
    private Button addToCart;
    private TextView productTitle, productDesc, productPrice, productViews, productWarranty, timeStamp;
    private ScaleRatingBar simpleRatingBar;
    private Products product;
    private String currentlyAttachedFragment;
    //Firebase Stuff
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseMethods firebaseMethods;
    private String currentUserID = null;

    @Override
    public void onCustomAttachFragment(String previousFragment, String currentAttachedFragment) {

        this.currentlyAttachedFragment = currentAttachedFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setupUniversalImageLoader(UniversalImageLoader.getConfiguration(this));
        setupActivityWidgets();
        setupWidgetsInitialization(getCurrentProduct(getIntent()));
        setupFirebase();
    }

    private void setupUniversalImageLoader(ImageLoaderConfiguration config) {
        ImageLoader.getInstance().init(config);
    }

    private Products getCurrentProduct(Intent intent) {

        if (intent != null) {
            if (intent.hasExtra(getString(R.string.currentSelectedProduct))) {

                Products product = intent.getParcelableExtra(getString(R.string.currentSelectedProduct));
                this.product = product;
                return product;
            }

        }

        return null;
    }

    private void setupActivityWidgets() {

        backArrow = findViewById(R.id.backArrow);
        adToCart = findViewById(R.id.adToCart);
        productImage = findViewById(R.id.productImage);
        addToCart = findViewById(R.id.addToCart);
        productTitle = findViewById(R.id.productTitle);
        productDesc = findViewById(R.id.productDesc);
        productPrice = findViewById(R.id.productPrice);
        productViews = findViewById(R.id.productViews);
        productWarranty = findViewById(R.id.productWarranty);
        timeStamp = findViewById(R.id.timeStamp);
        simpleRatingBar = findViewById(R.id.simpleRatingBar);

        //Listeners

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logic for handling the back
                finish();
            }
        });

        adToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logic for adding the product to the User's Cart
                setupUserWishlistProducts();
            }
        });

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logic for displaying Product in Full Size + Zoom Feature
                setupImageZoomView();

            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logic for adding the product to the User's Cart
                setupUserWishlistProducts();
            }
        });
    }

    private void setupImageZoomView() {
        ZoomImageViewFragment zoomImageView = new ZoomImageViewFragment();
        zoomImageView.setZoomImageViewUri(product.getProduct_image_url());
        zoomImageView.setCurrentContext(ProductDetailActivity.this);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.parentLayout
                        , zoomImageView
                        , getString(R.string.zoomImageViewFragment))
                .commitAllowingStateLoss();
    }

    private void setupUserWishlistProducts() {


        myRef
                .child(getString(R.string.db_cart_node))
                .child(currentUserID)
                .orderByChild(getString(R.string.db_field_product_id))
                .equalTo(product.getProduct_id())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if (!dataSnapshot.exists()) {

                            firebaseMethods.initializeAddToCart(currentUserID, product);
                        } else {
                            Toast.makeText(ProductDetailActivity.this, getString(R.string.Already_Added_to_the_Cart), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public void setupWidgetsInitialization(Products product) {
        if (product != null) {

            productTitle.setText("Title :" + product.getProduct_name());
            productDesc.setText("Description :" + product.getProduct_description());
            productPrice.setText("Price :" + product.getProduct_price());
            productWarranty.setText("Warranty :" + product.getProduct_warranty());
            timeStamp.setText("Timestamp :" + product.getTimeStamp());
            simpleRatingBar.setRating(product.getProduct_rating());

            ImageLoader.getInstance().displayImage(product.getProduct_image_url(), productImage);
        }

    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseMethods = new FirebaseMethods(this, getString(R.string.activity_product_detail));

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    currentUserID = user.getUid();
                    setupProductViews(product);

                    myRef
                            .child(getString(R.string.db_views_node))
                            .child(product.getProduct_id())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {

                                        productViews.setText("Views :" + String.valueOf(dataSnapshot.getChildrenCount()));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

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

    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(currentlyAttachedFragment)) {
            if (currentlyAttachedFragment.equals(getString(R.string.zoomImageViewFragment))) {
                Fragment fragment = getSupportFragmentManager()
                        .findFragmentByTag(getString(R.string.zoomImageViewFragment));
                if (fragment instanceof ZoomImageViewFragment) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(fragment)
                            .commit();
                    currentlyAttachedFragment = null;
                }
                return;
            }
        }
        super.onBackPressed();
    }


    public void setupProductViews(final Products product) {

        myRef
                .child(getString(R.string.db_views_node))
                .child(product.getProduct_id())
                .orderByChild(getString(R.string.db_field_user_id))
                .equalTo(currentUserID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {

                            firebaseMethods.initializeProductViews(currentUserID, product);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }
}
