package com.example.fasih.dukaanapp.models;

public class Orders {

    private String product_name;
    private String order_price;
    private String timeStamp;
    private String order_status;
    private String user_id;
    private String order_id;
    private String product_id;
    private String shop_id;

    public Orders() {
    }

    public Orders(String product_name
            , String order_price
            , String timeStamp
            , String order_status
            , String user_id
            , String order_id
            , String product_id
            , String shop_id) {

        this.product_name = product_name;
        this.order_price = order_price;
        this.timeStamp = timeStamp;
        this.order_status = order_status;
        this.user_id = user_id;
        this.order_id = order_id;
        this.product_id = product_id;
        this.shop_id = shop_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "product_name='" + product_name + '\'' +
                ", order_price='" + order_price + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", order_status='" + order_status + '\'' +
                ", user_id='" + user_id + '\'' +
                ", order_id='" + order_id + '\'' +
                ", product_id='" + product_id + '\'' +
                ", shop_id='" + shop_id + '\'' +
                '}';
    }
}
