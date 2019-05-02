package com.example.fasih.dukaanapp.models;

public class StripeBillingDetails {

    private StripeRecipientAddress address;
    private String email;
    private String name;
    private String phone;

    public StripeBillingDetails() {
    }

    public StripeBillingDetails(StripeRecipientAddress address
            , String email
            , String name
            , String phone) {
        this.address = address;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public StripeRecipientAddress getAddress() {
        return address;
    }

    public void setAddress(StripeRecipientAddress address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "StripeBillingDetails{" +
                "address=" + address +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
