package com.example.fasih.dukaanapp.order.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fasih.dukaanapp.R;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputListener;
import com.stripe.android.view.CardMultilineWidget;

public class PaymentGatewayActivity extends AppCompatActivity {


    private CardMultilineWidget card_multiline_widget;
    private Button confirmPayment;
    private Boolean isCardComplete, isExpirationComplete, isCvcComplete, isPostalCodeComplete=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        setupActivityWidgets();
//        setupPaymentInitialization();

    }

    private void setupActivityWidgets() {

        card_multiline_widget = findViewById(R.id.card_multiline_widget);
        confirmPayment = findViewById(R.id.confirmPayment);

        confirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isCardComplete && isExpirationComplete && isCvcComplete && isPostalCodeComplete)
                {
                    //setupPayment
                    setupPaymentInitialization();
                }
                else
                {
                    //show message
                    Toast.makeText(PaymentGatewayActivity.this, "Please Again Fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        card_multiline_widget.setCardInputListener(new CardInputListener() {
            @Override
            public void onFocusChange(String focusField) {

                Log.d("TAG1234", "onCardComplete: "+focusField);

                if(focusField.equals("focus_card")){
                    isCardComplete = false;
                }
                if(focusField.equals("focus_expiry")){
                    isExpirationComplete = false;
                }
                if(focusField.equals("focus_cvc")){
                    isCvcComplete = false;
                }
                if(focusField.equals("focus_postal")){
                    isPostalCodeComplete = false;
                }
            }

            @Override
            public void onCardComplete() {
                Log.d("TAG1234", "onCardComplete: ");
                isCardComplete =true;
            }

            @Override
            public void onExpirationComplete() {
                Log.d("TAG1234", "onExpirationComplete: ");
                isExpirationComplete = true;
            }

            @Override
            public void onCvcComplete() {
                Log.d("TAG1234", "onCvcComplete: ");
                isCvcComplete = true;
            }

            @Override
            public void onPostalCodeComplete() {
                Log.d("TAG1234", "onPostalCodeComplete: ");
                isPostalCodeComplete = true;
            }
        });
    }

    private void setupPaymentInitialization() {

        Card cardToSave = card_multiline_widget.getCard();
        if (cardToSave == null) {
            Toast.makeText(this, "Invalid Card Data", Toast.LENGTH_SHORT).show();
            return;
        }
//        createPaymentIntent(cardToSave);
//        createPaymentToken(cardToSave);
    }

//    private void createPaymentIntent(Card cardToSave) {
//        Stripe stripe = new Stripe(PaymentGatewayActivity.this, "pk_test_Cy2QcAs1XUqNFOimYEpNRR9a006i6TEI1C");
//
//        PaymentMethodCreateParams paymentMethodCreateParams =
//                PaymentMethodCreateParams.create(cardToSave,null);
//        PaymentIntentParams paymentIntentParams =
//                PaymentIntentParams.createConfirmPaymentIntentWithPaymentMethodCreateParams(
//                        paymentMethodCreateParams, clientSecret,
//                        "yourapp://post-authentication-return-url");
//
//        try {
//            PaymentIntent paymentIntent = stripe.confirmPaymentIntentSynchronous(
//                    paymentIntentParams,
//                    "pk_test_Cy2QcAs1XUqNFOimYEpNRR9a006i6TEI1C"
//            );
//        } catch (AuthenticationException e) {
//            e.printStackTrace();
//        } catch (InvalidRequestException e) {
//            e.printStackTrace();
//        } catch (APIConnectionException e) {
//            e.printStackTrace();
//        } catch (APIException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     *
     * Charges API Test Code
     * Successfully generating the token
     * @param cardToSave
     */
    private void createPaymentToken(Card cardToSave) {
        Stripe stripe = new Stripe(PaymentGatewayActivity.this
                , "pk_test_Cy2QcAs1XUqNFOimYEpNRR9a006i6TEI1C");//API KEY
        //replace it with your original api key
        //before publishing it to the market
        stripe.createToken(
                cardToSave,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        // Send token to your server
                        Log.d("TAG1234", "onSuccess: "+token);
                    }
                    public void onError(Exception error) {
                        // Show localized error message
                        Toast.makeText(PaymentGatewayActivity.this,
                                "Error while generating token",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }
}
