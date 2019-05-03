package com.example.fasih.dukaanapp.networking.retrofit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.models.StripeCharge;
import com.example.fasih.dukaanapp.models.StripeCustomCharge;
import com.example.fasih.dukaanapp.models.fcmMessage.Message;
import com.example.fasih.dukaanapp.networking.interfaces.GitHubService;
import com.example.fasih.dukaanapp.utils.FirebaseMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofit {

    private Retrofit retrofit = null;
    private Context mContext;
    private String resName;

    //Firebase Stuff
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;

    public MyRetrofit(FirebaseAuth mAuth
            , DatabaseReference myRef
            , FirebaseMethods firebaseMethods
            , Context context
            , String activityOrFragmentName) {

        this.mAuth = mAuth;
        this.myRef = myRef;
        this.firebaseMethods = firebaseMethods;
        this.mContext = context;
        this.resName = activityOrFragmentName;
    }

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
            , StripeCustomCharge charge
            , ProgressBar confirmPaymentProgress
            , Products product) {

        service.postStripeCustomCharge(charge).enqueue(new Callback<StripeCharge>() {
            @Override
            public void onResponse(Call<StripeCharge> call
                    , Response<StripeCharge> response) {

                if (response.body() != null) {
                    //do your work here
                    myRef
                            .child(mContext.getString(R.string.db_stripe_charge_node))
                            .child(mAuth.getCurrentUser().getUid())
                            .child(response.body().getId())//chargeID
                            .setValue(response.body()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            confirmPaymentProgress.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                firebaseMethods.placeNewOrder(mAuth.getCurrentUser().getUid()
                                        , product
                                        , "pending");
                                //start the order confirmation Fragment
                            }
                        }
                    });
                } else
                    confirmPaymentProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<StripeCharge> call, Throwable t) {
                Log.d("TAG1234", "onFailure: " + t);
                confirmPaymentProgress.setVisibility(View.GONE);
            }
        });
    }


    public void makePostFcmMessageAPICall(GitHubService service, Message message) {

        service.postFcmMessageToPlaceOrder(message)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                        Log.d("TAG1234", "onResponse: "+response);
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
    }
}
