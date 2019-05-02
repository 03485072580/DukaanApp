package com.example.fasih.dukaanapp.order.interfaces;

import com.example.fasih.dukaanapp.models.StripeCustomCharge;

public interface PaymentNotifier {

    void callbackPaymentMethodSelected(String paymentMethod);
    void callbackActivityMakePostChargeRequest(StripeCustomCharge stripeCustomCharge);
}
