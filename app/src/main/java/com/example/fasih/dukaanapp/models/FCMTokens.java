package com.example.fasih.dukaanapp.models;

public class FCMTokens {

    private String user_id;
    private String user_name;
    private String scope;
    private String fcm_token;

    public FCMTokens() {
    }

    public FCMTokens(String user_id, String user_name, String scope, String fcm_token) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.scope = scope;
        this.fcm_token = fcm_token;
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

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    @Override
    public String toString() {
        return "FCMTokens{" +
                "user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", scope='" + scope + '\'' +
                ", fcm_token='" + fcm_token + '\'' +
                '}';
    }
}
