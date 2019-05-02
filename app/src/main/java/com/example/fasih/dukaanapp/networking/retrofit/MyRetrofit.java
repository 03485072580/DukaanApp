package com.example.fasih.dukaanapp.networking.retrofit;

import android.content.Context;
import android.util.Log;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.models.StripeCharge;
import com.example.fasih.dukaanapp.models.StripeCustomCharge;
import com.example.fasih.dukaanapp.networking.interfaces.GitHubService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofit {

    private Retrofit retrofit = null;

    public Retrofit getRetrofitSingletonInstance(Context context) {

        if (retrofit != null) {
            return retrofit;
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.TestStripeBaseURL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public GitHubService getGitHubServiceInstance() {
        if (retrofit != null)
            return retrofit.create(GitHubService.class);
        else
            return null;
    }

    public void makePostChargeAPICall(GitHubService service
            , StripeCustomCharge charge) {

        service.postStripeCustomCharge(charge).enqueue(new Callback<StripeCharge>() {
            @Override
            public void onResponse(Call<StripeCharge> call, Response<StripeCharge> response) {

                if (response.body() != null) {

                }
            }

            @Override
            public void onFailure(Call<StripeCharge> call, Throwable t) {
                Log.d("TAG1234", "onFailure: " + t);
            }
        });
    }
}
