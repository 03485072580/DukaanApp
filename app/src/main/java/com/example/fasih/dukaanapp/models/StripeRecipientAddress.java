package com.example.fasih.dukaanapp.models;

public class StripeRecipientAddress {

    private String line1;//required
    private String city;
    private String country;
    private String line2;
    private String postal_code;
    private String state;

    public StripeRecipientAddress() {
    }

    public StripeRecipientAddress(String line1
            , String city
            , String country
            , String line2
            , String postal_code
            , String state) {
        this.line1 = line1;
        this.city = city;
        this.country = country;
        this.line2 = line2;
        this.postal_code = postal_code;
        this.state = state;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "StripeRecipientAddress{" +
                "line1='" + line1 + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", line2='" + line2 + '\'' +
                ", postal_code='" + postal_code + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
