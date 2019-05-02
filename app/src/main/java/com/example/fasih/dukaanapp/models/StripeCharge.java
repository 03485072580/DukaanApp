package com.example.fasih.dukaanapp.models;

import java.util.Date;

public class StripeCharge {

    private String id;
    private long amount;
    private long created;
    private String currency;
    private Boolean livemode;
    private Boolean paid;
    private StripeShipping shipping;
    private String status;

    public StripeCharge() {
    }

    public StripeCharge(String id
            , long amount
            , long created
            , String currency
            , Boolean livemode
            , Boolean paid
            , StripeShipping shipping
            , String status) {
        this.id = id;
        this.amount = amount;
        this.created = created;
        this.currency = currency;
        this.livemode = livemode;
        this.paid = paid;
        this.shipping = shipping;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getLivemode() {
        return livemode;
    }

    public void setLivemode(Boolean livemode) {
        this.livemode = livemode;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public StripeShipping getShipping() {
        return shipping;
    }

    public void setShipping(StripeShipping shipping) {
        this.shipping = shipping;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StripeCharge{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", created=" + created +
                ", currency='" + currency + '\'' +
                ", livemode=" + livemode +
                ", paid=" + paid +
                ", shipping=" + shipping +
                ", status='" + status + '\'' +
                '}';
    }
}
