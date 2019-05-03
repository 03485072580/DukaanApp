package com.example.fasih.dukaanapp.order.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.models.StripeCustomCharge;
import com.example.fasih.dukaanapp.networking.fragments.AlertPaymentDialogFragment;
import com.example.fasih.dukaanapp.networking.interfaces.GitHubService;
import com.example.fasih.dukaanapp.networking.retrofit.MyRetrofit;
import com.example.fasih.dukaanapp.order.interfaces.DialogClickNotify;
import com.example.fasih.dukaanapp.order.interfaces.PaymentNotifier;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputListener;
import com.stripe.android.view.CardMultilineWidget;

import retrofit2.Retrofit;

public class PaymentGatewayActivity extends AppCompatActivity {


    private Products product;
    private MyRetrofit myRetrofit;
    private ProgressBar confirmPaymentProgress;
    private AlertPaymentDialogFragment paymentDialog;

    private CardMultilineWidget card_multiline_widget;
    private Button confirmPayment;
    private Boolean isCardComplete, isExpirationComplete, isCvcComplete, isPostalCodeComplete = false;

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
        setContentView(R.layout.activity_payment_gateway);
        setupActivityWidgets();
        setupFirebase();
    }

    private void setupActivityWidgets() {

        card_multiline_widget = findViewById(R.id.card_multiline_widget);
        confirmPayment = findViewById(R.id.confirmPayment);
        confirmPaymentProgress = findViewById(R.id.confirmPaymentProgress);
        paymentDialog = new AlertPaymentDialogFragment();


        paymentDialog.setupOnButtonClickListener(new DialogClickNotify() {
            @Override
            public void setupOnClickListener(View view) {

                if (view.findViewById(R.id.confirm) != null)
                    if (view.getId() == view.findViewById(R.id.confirm).getId()) {
                        //init Payment Procedure
                        paymentDialog.dismiss();
                        confirmPaymentProgress.setVisibility(View.VISIBLE);
                        setupPaymentInitialization(product);
                        return;
                    }
                if (view.findViewById(R.id.cancel) != null)
                    if (view.getId() == view.findViewById(R.id.cancel).getId()) {
                        //cancel payment
                        paymentDialog.dismiss();
                    }

            }
        });

        product = getProduct(getIntent());
        confirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isCardComplete && isExpirationComplete && isCvcComplete && isPostalCodeComplete) {
                    //setupPayment
                    if (product != null) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(getString(R.string.uploadProduct), product);
                        paymentDialog.setArguments(bundle);
                        paymentDialog.show(getSupportFragmentManager(), getString(R.string.alertPaymentDialogFragment));
                    }
                } else {
                    //show message
                    Toast.makeText(PaymentGatewayActivity.this, "Please Again Fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        card_multiline_widget.setCardInputListener(new CardInputListener() {
            @Override
            public void onFocusChange(String focusField) {

                if (focusField.equals("focus_card")) {
                    isCardComplete = false;
                }
                if (focusField.equals("focus_expiry")) {
                    isExpirationComplete = false;
                }
                if (focusField.equals("focus_cvc")) {
                    isCvcComplete = false;
                }
                if (focusField.equals("focus_postal")) {
                    isPostalCodeComplete = false;
                }
            }

            @Override
            public void onCardComplete() {
                isCardComplete = true;
            }

            @Override
            public void onExpirationComplete() {
                isExpirationComplete = true;
            }

            @Override
            public void onCvcComplete() {
                isCvcComplete = true;
            }

            @Override
            public void onPostalCodeComplete() {
                isPostalCodeComplete = true;
            }
        });
    }

    private void setupPaymentInitialization(Products product) {

        Card cardToSave = card_multiline_widget.getCard();
        if (cardToSave == null) {
            Toast.makeText(this, "Invalid Card Data", Toast.LENGTH_SHORT).show();
            confirmPaymentProgress.setVisibility(View.GONE);
            return;
        }
//        createPaymentIntent(cardToSave);
        createPaymentToken(cardToSave, product);
    }

    /**
     * Charges API Test Code
     * Successfully generating the token
     *
     * @param cardToSave
     */
    private void createPaymentToken(Card cardToSave, Products product) {
        Stripe stripe = new Stripe(PaymentGatewayActivity.this
                , getString(R.string.stripe_testApiKey));//API KEY
        //replace it with your original api key
        //before publishing it to the market
        stripe.createToken(
                cardToSave,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        // Send token to your server
                        firebaseMethods.sendStripeTokenToFirebase(token.getId());
                        firebaseMethods.retrieveShopShippingInformation(product, token);
                    }

                    public void onError(Exception error) {
                        // Show localized error message
                        confirmPaymentProgress.setVisibility(View.GONE);
                        Toast.makeText(PaymentGatewayActivity.this,
                                "Error while generating token",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    private Products getProduct(Intent intent) {
        Products product;
        if (intent != null) {
            product = intent.getParcelableExtra(getString(R.string.uploadProduct));
            return product;
        }
        return null;
    }

    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        firebaseMethods = new FirebaseMethods(this, getString(R.string.activity_payment_gateway));
        myRetrofit = new MyRetrofit(mAuth, myRef, firebaseMethods, this, getString(R.string.activity_payment_gateway));

        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                currentUserID = user.getUid();
            }

        };

        //callbacks
        firebaseMethods.setStripeCustomCharge(new PaymentNotifier() {
            @Override
            public void callbackPaymentMethodSelected(String paymentMethod) {
                //Not Interested in this method here
            }

            @Override
            public void callbackActivityMakePostChargeRequest(StripeCustomCharge stripeCustomCharge) {
                //Here you can initialize retrofit Instance and Make a Post Request
                Retrofit retrofit = myRetrofit.getRetrofitSingletonInstance(PaymentGatewayActivity.this);
                GitHubService service = myRetrofit.getGitHubServiceInstance();
                if (service != null && stripeCustomCharge != null) {
                    myRetrofit.makePostChargeAPICall(service
                            , stripeCustomCharge
                            , confirmPaymentProgress
                            , product);
                } else {
                    confirmPaymentProgress.setVisibility(View.GONE);
                }
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
    }
}
