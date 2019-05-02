package com.example.fasih.dukaanapp.models;

public class StripeToken {

    private String user_id;
    private String user_name;
    private String scope;
    private String stripe_token;

    public StripeToken() {
    }

    public StripeToken(String user_id
            , String user_name
            , String scope
            , String stripe_token) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.scope = scope;
        this.stripe_token = stripe_token;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getStripe_token() {
        return stripe_token;
    }

    public void setStripe_token(String stripe_token) {
        this.stripe_token = stripe_token;
    }

    @Override
    public String toString() {
        return "StripeToken{" +
                "user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", scope='" + scope + '\'' +
                ", stripe_token='" + stripe_token + '\'' +
                '}';
    }
}
