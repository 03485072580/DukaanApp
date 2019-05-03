package com.example.fasih.dukaanapp.models;

public class FCMTokens {

    private String user_id;
    private String fcm_token;

    public FCMTokens() {
    }

    public FCMTokens(String user_id, String fcm_token) {
        this.user_id = user_id;
        this.fcm_token = fcm_token;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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
                ", fcm_token='" + fcm_token + '\'' +
                '}';
    }
}
