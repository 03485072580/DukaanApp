package com.example.fasih.dukaanapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Fasih on 01/11/19.
 * Todo Here username reflects the shop Name
 */

public class ShopProfileSettings implements Parcelable {

    public static final Creator<ShopProfileSettings> CREATOR = new Creator<ShopProfileSettings>() {
        @Override
        public ShopProfileSettings createFromParcel(Parcel in) {
            return new ShopProfileSettings(in);
        }

        @Override
        public ShopProfileSettings[] newArray(int size) {
            return new ShopProfileSettings[size];
        }
    };
    private String user_id;
    private String first_name;
    private String last_name;
    private String user_name;
    private String email;
    private String scope;
    private String shop_address;
    private String city;
    private String country;
    private Boolean admin_approved;
    private String shop_category;
    private String profile_image_url;
    private String mall_id;

    public ShopProfileSettings() {
    }

    public ShopProfileSettings(String user_id
            , String first_name
            , String last_name
            , String user_name
            , String email
            , String scope
            , String shop_address
            , String city
            , String country
            , Boolean admin_approved
            , String shop_category
            , String profile_image_url
            , String mall_id) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_name = user_name;
        this.email = email;
        this.scope = scope;
        this.shop_address = shop_address;
        this.city = city;
        this.country = country;
        this.admin_approved = admin_approved;
        this.shop_category = shop_category;
        this.profile_image_url = profile_image_url;
        this.mall_id = mall_id;
    }

    protected ShopProfileSettings(Parcel in) {
        user_id = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        user_name = in.readString();
        email = in.readString();
        scope = in.readString();
        shop_address = in.readString();
        city = in.readString();
        country = in.readString();
        byte tmpAdmin_approved = in.readByte();
        admin_approved = tmpAdmin_approved == 0 ? null : tmpAdmin_approved == 1;
        shop_category = in.readString();
        profile_image_url = in.readString();
        mall_id = in.readString();
    }

    public String getMall_id() {
        return mall_id;
    }

    public void setMall_id(String mall_id) {
        this.mall_id = mall_id;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getShop_category() {
        return shop_category;
    }

    public void setShop_category(String shop_category) {
        this.shop_category = shop_category;
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

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
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

    public Boolean getAdmin_approved() {
        return admin_approved;
    }

    public void setAdmin_approved(Boolean admin_approved) {
        this.admin_approved = admin_approved;
    }

    @Override
    public String toString() {
        return "ShopProfileSettings{" +
                "user_id='" + user_id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", user_name='" + user_name + '\'' +
                ", email='" + email + '\'' +
                ", scope='" + scope + '\'' +
                ", shop_address='" + shop_address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", admin_approved=" + admin_approved +
                ", shop_category='" + shop_category + '\'' +
                ", profile_image_url='" + profile_image_url + '\'' +
                ", mall_id='" + mall_id + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(user_id);
        parcel.writeString(first_name);
        parcel.writeString(last_name);
        parcel.writeString(user_name);
        parcel.writeString(email);
        parcel.writeString(scope);
        parcel.writeString(shop_address);
        parcel.writeString(city);
        parcel.writeString(country);
        parcel.writeByte((byte) (admin_approved == null ? 0 : admin_approved ? 1 : 2));
        parcel.writeString(shop_category);
        parcel.writeString(profile_image_url);
        parcel.writeString(mall_id);
    }
}
