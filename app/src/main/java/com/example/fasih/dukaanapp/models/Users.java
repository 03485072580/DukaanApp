package com.example.fasih.dukaanapp.models;

/**
 * Created by Fasih on 01/04/19.
 * Todo if scope is shop then username
 * Todo simply reflects to the shop name
 */

public class Users {

    private String user_id;
    private String first_name;
    private String last_name;
    private String user_name;
    private String email;
    private String scope;

    public Users(String user_id, String first_name, String last_name
            , String user_name, String email, String scope) {

        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_name = user_name;
        this.email = email;
        this.scope = scope;
    }

    public Users() {
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Users{" +
                "user_id='" + user_id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", user_name='" + user_name + '\'' +
                ", email='" + email + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
