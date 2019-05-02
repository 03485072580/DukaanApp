package com.example.fasih.dukaanapp.order.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.models.StripeCustomCharge;
import com.example.fasih.dukaanapp.order.fragments.CheckoutFragment;
import com.example.fasih.dukaanapp.order.interfaces.PaymentNotifier;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderPageActivity extends AppCompatActivity {

    private FrameLayout orderPageFragmentContainer;
    private Button placeOrder;
    private CheckoutFragment checkoutFragment;
    private String paymentMethod;


    //Firebase Stuff
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseMethods firebaseMethods;
    private String currentUserID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);
        setupActivityWidgets();
        setupFirebase();
    }

    private void setupActivityWidgets() {

        orderPageFragmentContainer = findViewById(R.id.orderPageFragmentContainer);
        placeOrder = findViewById(R.id.placeOrder);

        addFragment(getIntent());

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkoutFragment.getPaymentMethodCorrect()){
                    //place an order on the DB
                    if (paymentMethod.equals("payByHand"))
                    firebaseMethods.placeNewOrder(currentUserID
                            , checkoutFragment.getCurrentProduct()
                            , "Pending");

                    if(paymentMethod.equals("payByStripe")){
                        Intent intent = new Intent(OrderPageActivity.this
                                ,PaymentGatewayActivity.class);
                        intent.putExtra(getString(R.string.uploadProduct),checkoutFragment.getCurrentProduct());
                        startActivity(intent);
                    }

                }
            }
        });
    }

    private void addFragment(Intent intent) {

        checkoutFragment = new CheckoutFragment();
        checkoutFragment.setActivityNotifierForChosenPaymentMethod(new PaymentNotifier() {
            @Override
            public void callbackPaymentMethodSelected(String paymentMethod) {
                OrderPageActivity.this.paymentMethod = paymentMethod;
            }

            @Override
            public void callbackActivityMakePostChargeRequest(StripeCustomCharge stripeCustomCharge) {
                //Not Interested
            }
        });
        if (intent != null) {

            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.currentSelectedProduct)
                    , intent.getParcelableExtra(getString(R.string.currentSelectedProduct)));
            checkoutFragment.setArguments(bundle);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.orderPageFragmentContainer
                        , checkoutFragment
                        , getString(R.string.checkoutFragment))

                .commitAllowingStateLoss();
    }



    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseMethods = new FirebaseMethods(this, getString(R.string.activity_order_page));

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
}
