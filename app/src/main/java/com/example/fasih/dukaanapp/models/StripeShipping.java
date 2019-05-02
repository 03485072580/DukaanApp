package com.example.fasih.dukaanapp.models;

public class StripeShipping {

    private StripeRecipientAddress address;//required
    private String name;//required
    private String phone;

    public StripeShipping() {
    }

    public StripeShipping(StripeRecipientAddress address
            , String name
            , String phone) {
        this.address = address;
        this.name = name;
        this.phone = phone;
    }

    public StripeRecipientAddress getAddress() {
        return address;
    }

    public void setAddress(StripeRecipientAddress address) {
        this.address = address;
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
        return "StripeShipping{" +
                "address=" + address +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
