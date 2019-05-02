package com.example.fasih.dukaanapp.networking.interfaces;

import com.example.fasih.dukaanapp.models.StripeCharge;
import com.example.fasih.dukaanapp.models.StripeCustomCharge;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GitHubService {

    @Headers("Content-Type:application/json")
    @POST("api/v1/charge")
    Call<StripeCharge> postStripeCustomCharge(@Body StripeCustomCharge charge);
}
