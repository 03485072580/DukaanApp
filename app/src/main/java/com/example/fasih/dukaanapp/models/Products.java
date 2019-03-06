package com.example.fasih.dukaanapp.models;

/**
 * Created by Fasih on 03/05/19.
 */

public class Products {
    private String product_name;
    private String product_category;
    private String product_image_url;
    private String product_description;
    private String product_price;
    private String product_warranty;
    private String product_stock;
    private String timeStamp;
    private String product_id;
    private long product_rating;

    public Products() {
    }

    public Products(String product_name, String product_category, String product_image_url, String product_description, String product_price
            , String product_warranty, String product_stock
            , String timeStamp, String product_id
            , long product_rating) {
        this.product_name = product_name;
        this.product_category = product_category;
        this.product_image_url = product_image_url;
        this.product_description = product_description;
        this.product_price = product_price;
        this.product_warranty = product_warranty;
        this.product_stock = product_stock;
        this.timeStamp = timeStamp;
        this.product_id = product_id;
        this.product_rating = product_rating;
    }

    public long getProduct_rating() {
        return product_rating;
    }

    public void setProduct_rating(long product_rating) {
        this.product_rating = product_rating;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getProduct_image_url() {
        return product_image_url;
    }

    public void setProduct_image_url(String product_image_url) {
        this.product_image_url = product_image_url;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_warranty() {
        return product_warranty;
    }

    public void setProduct_warranty(String product_warranty) {
        this.product_warranty = product_warranty;
    }

    public String getProduct_stock() {
        return product_stock;
    }

    public void setProduct_stock(String product_stock) {
        this.product_stock = product_stock;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    @Override
    public String toString() {
        return "Products{" +
                "product_name='" + product_name + '\'' +
                ", product_category='" + product_category + '\'' +
                ", product_image_url='" + product_image_url + '\'' +
                ", product_description='" + product_description + '\'' +
                ", product_price='" + product_price + '\'' +
                ", product_warranty='" + product_warranty + '\'' +
                ", product_stock='" + product_stock + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", product_id='" + product_id + '\'' +
                ", product_rating=" + product_rating +
                '}';
    }
}
