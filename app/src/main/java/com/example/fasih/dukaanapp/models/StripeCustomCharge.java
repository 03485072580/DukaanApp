package com.example.fasih.dukaanapp.models;

public class StripeCustomCharge {

    private String stripe_token;
    private String currency;
    private long amount_millis;
    private StripeShipping shipping;

    public StripeCustomCharge() {
    }

    public StripeCustomCharge(String stripe_token
            , String currency
            , long amount_millis
            , StripeShipping shipping) {
        this.stripe_token = stripe_token;
        this.currency = currency;
        this.amount_millis = amount_millis;
        this.shipping = shipping;
    }

    public String getStripe_token() {
        return stripe_token;
    }

    public void setStripe_token(String stripe_token) {
        this.stripe_token = stripe_token;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getAmount_millis() {
        return amount_millis;
    }

    public void setAmount_millis(long amount_millis) {
        this.amount_millis = amount_millis;
    }

    public StripeShipping getShipping() {
        return shipping;
    }

    public void setShipping(StripeShipping shipping) {
        this.shipping = shipping;
    }

    @Override
    public String toString() {
        return "StripeCustomCharge{" +
                "stripe_token='" + stripe_token + '\'' +
                ", currency='" + currency + '\'' +
                ", amount_millis=" + amount_millis +
                ", shipping=" + shipping +
                '}';
    }
}
