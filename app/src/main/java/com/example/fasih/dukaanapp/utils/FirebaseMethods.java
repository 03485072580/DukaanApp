package com.example.fasih.dukaanapp.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.adapter.CarsFragmentAdapter;
import com.example.fasih.dukaanapp.adapter.ClothingProductsAdapter;
import com.example.fasih.dukaanapp.adapter.CosmeticsProductsAdapter;
import com.example.fasih.dukaanapp.adapter.ElectronicsProductsAdapter;
import com.example.fasih.dukaanapp.adapter.MobileProductsAdapter;
import com.example.fasih.dukaanapp.categories.actvities.SubCategoryActivity;
import com.example.fasih.dukaanapp.categories.actvities.UniqueCategoryActivity;
import com.example.fasih.dukaanapp.categories.interfaces.KeepHandleRecyclerList;
import com.example.fasih.dukaanapp.home.activities.SellerHomePageActivity;
import com.example.fasih.dukaanapp.home.activities.UserHomePageActivity;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.ProgressDialogFragment;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.SearchUsernameFragment;
import com.example.fasih.dukaanapp.login.activity.LoginActivity;
import com.example.fasih.dukaanapp.models.Orders;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.models.ProductsNew;
import com.example.fasih.dukaanapp.models.ShopProfileSettings;
import com.example.fasih.dukaanapp.models.StripeCustomCharge;
import com.example.fasih.dukaanapp.models.StripeRecipientAddress;
import com.example.fasih.dukaanapp.models.StripeShipping;
import com.example.fasih.dukaanapp.models.StripeToken;
import com.example.fasih.dukaanapp.models.UserAccountSettings;
import com.example.fasih.dukaanapp.models.Users;
import com.example.fasih.dukaanapp.models.Views;
import com.example.fasih.dukaanapp.order.activities.OrderPageActivity;
import com.example.fasih.dukaanapp.order.activities.PaymentGatewayActivity;
import com.example.fasih.dukaanapp.order.interfaces.PaymentNotifier;
import com.example.fasih.dukaanapp.register.RegisterActivity;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.stripe.android.model.Token;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Fasih on 01/04/19.
 */

public class FirebaseMethods {

    //Facebook Stuff
    AccessToken accessToken;
    //Google Stuff
    GoogleSignInAccount account;

    private KeepHandleRecyclerList currentChildReference;
    private PaymentNotifier paymentNotifier;

    private ProgressDialogFragment dialogFragment;
    private Boolean isScopeCorrect = false;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference myRef;
    private ProgressBar updateProgress;
    private String activityName;
    private Context mContext;
    private ArrayList<Products> userViewProductsList = new ArrayList<>();

    public FirebaseMethods(Context context, String activityName) {
        this.mContext = context;
        this.activityName = activityName;
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        myRef = firebaseDatabase.getReference();

        setupUniversalImageLoader(UniversalImageLoader.getConfiguration(context));
    }

    private void setupUniversalImageLoader(ImageLoaderConfiguration config) {
        ImageLoader.getInstance().init(config);
    }

    //Username might be the name of the User or
    // may be the shop Name depending upon the situation

    public void addNewUser(final String firstName, final String lastName, final String userName, final String email
            , final String password, final String phoneNumber, final String country, final String scope, final String city
            , final String shop_address, final Boolean admin_approved) {

        //Check if email is not null, then execute the below code
        // else just update the DB

        if (updateProgress != null) {
            updateProgress.setVisibility(View.VISIBLE);

            Task<AuthResult> task = mAuth.createUserWithEmailAndPassword(email, password);

            task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task1) {
                    if (task1.isSuccessful()) {

                        Users users = new Users(mAuth.getCurrentUser().getUid(), firstName, lastName,
                                StringManipulations.toLowerCaseUsername(userName), email, scope);


                        myRef
                                .child(mContext.getString(R.string.db_users_node))
                                .child(task1.getResult().getUser().getUid())
                                .setValue(users);

                        if (scope.equals(mContext.getString(R.string.scope_user))) {

                            UserAccountSettings settings = new UserAccountSettings(mAuth.getCurrentUser().getUid(),
                                    firstName, lastName
                                    , StringManipulations.toLowerCaseUsername(userName)
                                    , email, "");


                            myRef
                                    .child(mContext.getString(R.string.db_user_profile_settings_node))
                                    .child(task1.getResult().getUser().getUid())
                                    .setValue(settings);
                        } else if (scope.equals(mContext.getString(R.string.scope_shop))) {
                            ShopProfileSettings settings = new ShopProfileSettings(mAuth.getCurrentUser().getUid(),
                                    firstName, lastName
                                    , StringManipulations.toLowerCaseUsername(userName)
                                    , email, scope, shop_address, city, country, admin_approved
                                    , "", "", "");


                            myRef
                                    .child(mContext.getString(R.string.db_shop_profile_settings_node))
                                    .child(task1.getResult().getUser().getUid())
                                    .setValue(settings);
                        }

                        task1.getResult().getUser()
                                .sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        updateProgress.setVisibility(View.GONE);
                                        if (activityName.equals(mContext.getString(R.string.activity_register))) {
                                            ((RegisterActivity) mContext).finish();
                                        }

                                        Toast.makeText(mContext, mContext.getString(R.string.email_verification_required), Toast.LENGTH_LONG).show();

                                    }
                                });

                    } else {
                        updateProgress.setVisibility(View.GONE);

                        if (task1.getException() instanceof FirebaseAuthWeakPasswordException) {
                            Toast.makeText(mContext, R.string.password_length_warning, Toast.LENGTH_SHORT).show();
                        }
                        if (task1.getException() instanceof FirebaseAuthEmailException) {
                            Toast.makeText(mContext, R.string.bad_email, Toast.LENGTH_SHORT).show();
                        }
                        if (task1.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(mContext, R.string.user_email_collsion, Toast.LENGTH_SHORT).show();
                        }
                        if (task1.getException() instanceof FirebaseNetworkException) {
                            Toast.makeText(mContext, mContext.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                        }
                        if (task1.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(mContext, mContext.getString(R.string.bad_email_format), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        }
    }

    public void addNewUser(final String firstName, final String lastName, final String userName, final String email
            , final String password, final String phoneNumber, final String country, final String scope, final String city
            , final String shop_address, final Boolean admin_approved, final String mallUniqueID) {

        //Check if email is not null, then execute the below code
        // else just update the DB

        //mall Id is responsible for the creation of the new node called as mall_shops

        if (updateProgress != null) {
            updateProgress.setVisibility(View.VISIBLE);

            final Task<AuthResult> task = mAuth.createUserWithEmailAndPassword(email, password);

            task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task1) {
                    if (task1.isSuccessful()) {

                        Users users = new Users(mAuth.getCurrentUser().getUid(), firstName, lastName,
                                StringManipulations.toLowerCaseUsername(userName), email, scope);


                        myRef
                                .child(mContext.getString(R.string.db_users_node))
                                .child(task1.getResult().getUser().getUid())
                                .setValue(users);

                        if (scope.equals(mContext.getString(R.string.scope_user))) {

                            UserAccountSettings settings = new UserAccountSettings(mAuth.getCurrentUser().getUid(),
                                    firstName, lastName
                                    , StringManipulations.toLowerCaseUsername(userName)
                                    , email, "");


                            myRef
                                    .child(mContext.getString(R.string.db_user_profile_settings_node))
                                    .child(task1.getResult().getUser().getUid())
                                    .setValue(settings);
                        } else if (scope.equals(mContext.getString(R.string.scope_shop))) {
                            ShopProfileSettings settings = new ShopProfileSettings(mAuth.getCurrentUser().getUid(),
                                    firstName, lastName
                                    , StringManipulations.toLowerCaseUsername(userName)
                                    , email, scope, shop_address, city, country
                                    , admin_approved, mContext.getString(R.string.shop), "", mallUniqueID);


                            myRef
                                    .child(mContext.getString(R.string.db_shop_profile_settings_node))
                                    .child(task1.getResult().getUser().getUid())
                                    .setValue(settings);

                            myRef
                                    .child(mContext.getString(R.string.db_mall_node))
                                    .child(mallUniqueID)
                                    .child(task1.getResult().getUser().getUid())
                                    .setValue(settings);
                        }

                        task1.getResult().getUser()
                                .sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        updateProgress.setVisibility(View.GONE);
                                        if (activityName.equals(mContext.getString(R.string.activity_register))) {

                                            ((RegisterActivity) mContext).finish();
                                        }

                                        Toast.makeText(mContext, mContext.getString(R.string.email_verification_required), Toast.LENGTH_LONG).show();

                                    }
                                });

                    } else {
                        updateProgress.setVisibility(View.GONE);

                        if (task1.getException() instanceof FirebaseAuthWeakPasswordException) {
                            Toast.makeText(mContext, R.string.password_length_warning, Toast.LENGTH_SHORT).show();
                        }
                        if (task1.getException() instanceof FirebaseAuthEmailException) {
                            Toast.makeText(mContext, R.string.bad_email, Toast.LENGTH_SHORT).show();
                        }
                        if (task1.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(mContext, R.string.user_email_collsion, Toast.LENGTH_SHORT).show();
                        }
                        if (task1.getException() instanceof FirebaseNetworkException) {
                            Toast.makeText(mContext, mContext.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                        }
                        if (task1.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(mContext, mContext.getString(R.string.bad_email_format), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        }
    }


    public void authenticateUser(final String username
            , final String email
            , final String password
            , final String fragmentName
            , final String scope) {


        if (TextUtils.isEmpty(username)) {
            //Mean Email is provided by the User

            Query query = myRef
                    .child(mContext.getString(R.string.db_users_node))
                    .orderByChild(mContext.getString(R.string.db_field_email))
                    .equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            HashMap<String, Object> hashMap = (HashMap<String, Object>) ds.getValue();
                            if (hashMap.containsKey("scope")) {
                                String dbPresentScope = (String) hashMap.get("scope");
                                isScopeCorrect = dbPresentScope.equals(scope);
                            }
                        }
                        if (updateProgress != null && isScopeCorrect) {
                            updateProgress.setVisibility(View.VISIBLE);
                            mAuth
                                    .signInWithEmailAndPassword(email, password)
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            if (authResult.getUser().isEmailVerified()) {
                                                updateProgress.setVisibility(View.GONE);
                                                if (activityName.equals(mContext.getString(R.string.activity_login))) {
                                                    if (fragmentName.equals(mContext.getString(R.string.userFragment))) {
                                                        mContext.startActivity(new Intent(mContext, UserHomePageActivity.class)
                                                                .putExtra(mContext.getString(R.string.userFragment), mContext.getString(R.string.userFragment)));
                                                        ((LoginActivity) mContext).finishAffinity();
                                                    }
                                                    if (fragmentName.equals(mContext.getString(R.string.shopFragment))) {
                                                        mContext.startActivity(new Intent(mContext, SellerHomePageActivity.class)
                                                                .putExtra(mContext.getString(R.string.shopFragment), mContext.getString(R.string.shopFragment)));
                                                        ((LoginActivity) mContext).finishAffinity();
                                                    }

                                                }

                                            } else {
                                                updateProgress.setVisibility(View.GONE);
                                                authResult.getUser().sendEmailVerification();
                                                Toast.makeText(mContext, mContext.getString(R.string.email_verification_required), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            updateProgress.setVisibility(View.GONE);

                                            Toast.makeText(mContext, mContext.getString(R.string.bad_username_password), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.bad_username_password), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.bad_username_password), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });


        }
        if (TextUtils.isEmpty(email)) {
            //Mean Username is provided by the User

            Query query_scope = myRef
                    .child(mContext.getString(R.string.db_users_node))
                    .orderByChild(mContext.getString(R.string.db_field_user_name))
                    .equalTo(username);
            query_scope.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            HashMap<String, Object> hashMap = (HashMap<String, Object>) ds.getValue();
                            if (hashMap.containsKey("scope")) {
                                String dbPresentScope = (String) hashMap.get("scope");
                                isScopeCorrect = dbPresentScope.equals(scope);

                            }
                            if (updateProgress != null && isScopeCorrect) {
                                updateProgress.setVisibility(View.VISIBLE);

                                Query query = myRef
                                        .child(mContext.getString(R.string.db_users_node))
                                        .orderByChild(mContext.getString(R.string.db_field_user_name))
                                        .equalTo(username);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (!dataSnapshot.exists()) {
                                            //Mean Provided username is not correct
                                            Toast.makeText(mContext, mContext.getString(R.string.bad_username_password), Toast.LENGTH_SHORT).show();
                                            updateProgress.setVisibility(View.GONE);
                                        }
                                        if (dataSnapshot.exists()) {
                                            //Mean Found a match
                                            // Now Login with the current users email
                                            String email = null;
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                Users users = ds.getValue(Users.class);
                                                email = users.getEmail();
                                            }

                                            mAuth
                                                    .signInWithEmailAndPassword(email, password)
                                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                        @Override
                                                        public void onSuccess(AuthResult authResult) {
                                                            if (authResult.getUser().isEmailVerified()) {
                                                                updateProgress.setVisibility(View.GONE);
                                                                if (activityName.equals(mContext.getString(R.string.activity_login))) {
                                                                    if (fragmentName.equals(mContext.getString(R.string.userFragment))) {
                                                                        mContext.startActivity(new Intent(mContext, UserHomePageActivity.class)
                                                                                .putExtra(mContext.getString(R.string.userFragment), mContext.getString(R.string.userFragment)));
                                                                        ((LoginActivity) mContext).finishAffinity();
                                                                    }
                                                                    if (fragmentName.equals(mContext.getString(R.string.shopFragment))) {
                                                                        mContext.startActivity(new Intent(mContext, SellerHomePageActivity.class)
                                                                                .putExtra(mContext.getString(R.string.shopFragment), mContext.getString(R.string.shopFragment)));
                                                                        ((LoginActivity) mContext).finishAffinity();
                                                                    }
                                                                }
                                                            } else {

                                                                updateProgress.setVisibility(View.GONE);
                                                                authResult.getUser().sendEmailVerification();
                                                                Toast.makeText(mContext, mContext.getString(R.string.email_verification_required), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            updateProgress.setVisibility(View.GONE);

                                                            Toast.makeText(mContext, mContext.getString(R.string.bad_username_password), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                Toast.makeText(mContext, mContext.getString(R.string.bad_username_password), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.bad_username_password), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

    }

    public void updateProgress(ProgressBar registerProgress) {

        if (registerProgress != null) {
            this.updateProgress = registerProgress;
        }
    }

    public void setupFacebookLoginWithAccessToken(final AccessToken accessToken, final String scope) {
        this.accessToken = accessToken;

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Update the DB
                            Query query = myRef
                                    .child(mContext.getString(R.string.db_users_node))
                                    .orderByChild(mContext.getString(R.string.db_field_user_id))
                                    .equalTo(task.getResult().getUser().getUid());

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        String lastName = null;
                                        String firstName = null;

                                        String displayName = task.getResult().getUser().getDisplayName();
                                        if (displayName != null) {
                                            if (displayName.contains(" ")) {

                                                firstName = displayName.substring(0, displayName.indexOf(" "));
                                                lastName = displayName.substring(displayName.indexOf(" ") + 1, displayName.length());

                                            }
                                        }

                                        Users users = new Users(task.getResult().getUser().getUid(), firstName, lastName,
                                                StringManipulations.toLowerCaseUsername("")
                                                , task.getResult().getUser().getEmail(), scope);

                                        UserAccountSettings settings = new UserAccountSettings(task.getResult().getUser().getUid(),
                                                firstName, lastName
                                                , StringManipulations.toLowerCaseUsername("")
                                                , task.getResult().getUser().getEmail(), "");

                                        myRef
                                                .child(mContext.getString(R.string.db_users_node))
                                                .child(task.getResult().getUser().getUid())
                                                .setValue(users);

                                        myRef
                                                .child(mContext.getString(R.string.db_user_profile_settings_node))
                                                .child(task.getResult().getUser().getUid())
                                                .setValue(settings);
                                    }
                                    mContext.startActivity(new Intent(mContext, UserHomePageActivity.class)
                                            .putExtra(mContext.getString(R.string.userFragment)
                                                    , mContext.getString(R.string.userFragment)));
                                    ((RegisterActivity) mContext).finishAffinity();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        } else {
                            Log.w("TAG1234", "signInWithCredential:failure", task.getException());

                        }

                        // ...
                    }
                });

    }

    public void setupGoogleLoginWithAccount(final GoogleSignInAccount account, final String scope) {
        this.account = account;

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //setup DB Work
                            Query query = myRef
                                    .child(mContext.getString(R.string.db_users_node))
                                    .orderByChild(mContext.getString(R.string.db_field_user_id))
                                    .equalTo(task.getResult().getUser().getUid());

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        String lastName = null;
                                        String firstName = null;

                                        String displayName = task.getResult().getUser().getDisplayName();
                                        if (displayName != null) {
                                            if (displayName.contains(" ")) {

                                                firstName = displayName.substring(0, displayName.indexOf(" "));
                                                lastName = displayName.substring(displayName.indexOf(" ") + 1, displayName.length());

                                            }
                                        }

                                        Users users = new Users(task.getResult().getUser().getUid(), firstName, lastName,
                                                StringManipulations.toLowerCaseUsername("")
                                                , task.getResult().getUser().getEmail(), scope);

                                        UserAccountSettings settings = new UserAccountSettings(task.getResult().getUser().getUid(),
                                                firstName, lastName
                                                , StringManipulations.toLowerCaseUsername("")
                                                , task.getResult().getUser().getEmail(), "");

                                        myRef
                                                .child(mContext.getString(R.string.db_users_node))
                                                .child(task.getResult().getUser().getUid())
                                                .setValue(users);

                                        myRef
                                                .child(mContext.getString(R.string.db_user_profile_settings_node))
                                                .child(task.getResult().getUser().getUid())
                                                .setValue(settings);
                                    }

                                    mContext.startActivity(new Intent(mContext, UserHomePageActivity.class)
                                            .putExtra(mContext.getString(R.string.userFragment)
                                                    , mContext.getString(R.string.userFragment)));
                                    ((RegisterActivity) mContext).finishAffinity();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            Log.w("TAG1234", "signInWithGoogleCredential:failure", task.getException());

                        }

                        // ...
                    }
                });
    }

    public void uploadProduct(final String productName
            , final String selectedCategory
            , String imageLoadingUrl
            , final String productDescription
            , final String productPrice
            , final String productWarranty
            , final String availableStock
            , final String timeStamp
            , final String shop_id
            , final String selectedType) {
        if (activityName.equals(mContext.getString(R.string.shareFragment))) {

            dialogFragment = new ProgressDialogFragment();
            dialogFragment.setCancelable(false);
            dialogFragment.show(((FragmentActivity) mContext)
                            .getSupportFragmentManager()
                    , mContext.getString(R.string.dialogFragment));
        }


        BufferedInputStream bufferedInputStream = null;
        storageReference = firebaseStorage.getReference(mContext.getPackageName()
                + "/images/" + mAuth.getCurrentUser().getUid() + "/");
        try {

            if (imageLoadingUrl.toLowerCase().startsWith("file://")) {
                imageLoadingUrl = imageLoadingUrl.toLowerCase().replace("file://", "");
                bufferedInputStream = new BufferedInputStream
                        (new FileInputStream(new File(imageLoadingUrl)));
            }
            if (imageLoadingUrl.toLowerCase().startsWith("content://")) {
                FileInputStream inputStream = (FileInputStream) mContext.getContentResolver().openInputStream(Uri.parse(imageLoadingUrl));
                bufferedInputStream = new BufferedInputStream(inputStream);
            }

            final StorageReference imageUploadReference = storageReference.child(Uri.parse(imageLoadingUrl).getLastPathSegment());
            ByteArrayBuffer baf = new ByteArrayBuffer(1024);
            int currentByteCount = 0;
            while ((currentByteCount = bufferedInputStream.read()) != -1) {
                baf.append(currentByteCount);
            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(baf.toByteArray(), 0, baf.toByteArray().length);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = imageUploadReference.putBytes(data);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imageUploadReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        String productID = myRef.push().getKey();
                        productID = productID.substring(3, 13);
                        Uri downloadUri = task.getResult();

                        ProductsNew product = new ProductsNew(productName, selectedCategory, downloadUri.toString()
                                , productDescription, productPrice, productWarranty
                                , availableStock, timeStamp, productID, -1, shop_id, selectedType);
                        myRef
                                .child(mContext.getString(R.string.db_products_node))
                                .child(mAuth.getCurrentUser().getUid())
                                .child(productID)
                                .setValue(product)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (activityName.equals(mContext.getString(R.string.shareFragment))) {
                                            dialogFragment.dismissAllowingStateLoss();
                                        }
                                        if (task.isSuccessful()) {
                                        } else {
                                            task.getException().printStackTrace();
                                        }
                                    }
                                });

                    }
                }
            });

        } catch (NullPointerException exc) {
            exc.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void putBytesToUploadImage(byte[] bytes) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        storageReference = firebaseStorage.getReference(mContext.getPackageName() +
                "/profile_images/" +
                FirebaseAuth.getInstance().getCurrentUser().getUid() +
                "/" + timeStamp + ".jpg");
        UploadTask uploadTask = storageReference.putBytes(bytes);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {

                    final Uri downloadUrl = task.getResult();
                    Query query = myRef.child(mContext.getString(R.string.db_shop_profile_settings_node))
                            .orderByChild(mContext.getString(R.string.db_field_user_id))
                            .equalTo(mAuth.getCurrentUser().getUid());

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                HashMap<String, Object> shopMap = (HashMap<String, Object>) dataSnapshot
                                        .child(mAuth.getCurrentUser().getUid())
                                        .getValue();
                                ShopProfileSettings shopSettings = new ShopProfileSettings(
                                        (String) shopMap.get(mContext.getString(R.string.db_field_user_id))
                                        , (String) shopMap.get(mContext.getString(R.string.db_field_first_name))
                                        , (String) shopMap.get(mContext.getString(R.string.db_field_last_name))
                                        , (String) shopMap.get(mContext.getString(R.string.db_field_user_name))
                                        , (String) shopMap.get(mContext.getString(R.string.db_field_email))
                                        , (String) shopMap.get(mContext.getString(R.string.db_field_scope))
                                        , (String) shopMap.get(mContext.getString(R.string.db_field_shop_address))
                                        , (String) shopMap.get(mContext.getString(R.string.db_field_city))
                                        , (String) shopMap.get(mContext.getString(R.string.db_field_country))
                                        , (Boolean) shopMap.get(mContext.getString(R.string.db_field_admin_approved))
                                        , (String) shopMap.get(mContext.getString(R.string.db_field_shop_category))
                                        , (String) shopMap.get(mContext.getString(R.string.db_field_profile_image_url))
                                        , (String) shopMap.get(mContext.getString(R.string.db_field_mall_id))
                                );
                                shopSettings.setProfile_image_url(downloadUrl.toString());
                                myRef.child(mContext.getString(R.string.db_shop_profile_settings_node))
                                        .child(mAuth.getCurrentUser().getUid())
                                        .setValue(shopSettings);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }
        });
    }


    public void searchQueryTextByUsername(String query) {
        Query queryDB = myRef
                .child(mContext.getString(R.string.db_mall_node))
                .child(mAuth.getCurrentUser().getUid())
                .orderByChild(mContext.getString(R.string.db_field_user_name))
                .equalTo(query);
        queryDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //there's a shop matching with particular shop name
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    Collection<Object> objectCollection = hashMap.values();

                    ShopProfileSettings shopProfileSettings = null;

                    for (Iterator<Object> it = objectCollection.iterator(); it.hasNext(); ) {
                        /**
                         *
                         * Username is always unique.
                         * So it's always going to return 1 result only
                         */
                        HashMap map = (HashMap) it.next();
                        shopProfileSettings = new ShopProfileSettings(
                                (String) map.get(mContext.getString(R.string.db_field_user_id))
                                , (String) map.get(mContext.getString(R.string.db_field_first_name))
                                , (String) map.get(mContext.getString(R.string.db_field_last_name))
                                , (String) map.get(mContext.getString(R.string.db_field_user_name))
                                , (String) map.get(mContext.getString(R.string.db_field_email))
                                , (String) map.get(mContext.getString(R.string.db_field_scope))
                                , (String) map.get(mContext.getString(R.string.db_field_shop_address))
                                , (String) map.get(mContext.getString(R.string.db_field_city))
                                , (String) map.get(mContext.getString(R.string.db_field_country))
                                , (Boolean) map.get(mContext.getString(R.string.db_field_admin_approved))
                                , (String) map.get(mContext.getString(R.string.db_field_shop_category))
                                , (String) map.get(mContext.getString(R.string.db_field_profile_image_url))
                                , (String) map.get(mContext.getString(R.string.db_field_mall_id))
                        );
                    }
                    SearchUsernameFragment searchFragment = new SearchUsernameFragment();
                    searchFragment.setDataSet(shopProfileSettings);

                    if (activityName.equals(mContext.getString(R.string.shopsListFragment))) {
                        ((FragmentActivity) mContext).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragmentContainer, searchFragment, mContext.getString(R.string.searchUsernameFragment))
                                .commitAllowingStateLoss();

                    }
                } else {
                    //No Results Found

                    SearchUsernameFragment searchFragment = new SearchUsernameFragment();
                    if (activityName.equals(mContext.getString(R.string.shopsListFragment))) {
                        ((FragmentActivity) mContext).getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragmentContainer, searchFragment, mContext.getString(R.string.searchUsernameFragment))
                                .commitAllowingStateLoss();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void queryProducts(final String queryString) {

        Query productsNodeQuery = myRef
                .child(mContext.getString(R.string.db_products_node));
        productsNodeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String, Object> shopUsersList = (HashMap<String, Object>) dataSnapshot.getValue();
                if (shopUsersList != null) {
                    Collection<Object> objectsList = shopUsersList.values();
                    //obj is representing the list of products placed by the current user in obj
                    for (Object obj : objectsList) {
                        HashMap<String, Object> productList = (HashMap<String, Object>) obj;
                        Collection<Object> particularUserProducts = productList.values();
                        //product is representing the individual product placed by current user
                        for (Object product : particularUserProducts) {
                            HashMap<String, String> productObj = (HashMap<String, String>) product;
                            Products userProduct = new Products();
                            userProduct.setProduct_category(productObj.get(mContext.getString(R.string.db_field_product_category)));
                            userProduct.setProduct_name(productObj.get(mContext.getString(R.string.db_field_product_name)));
                            userProduct.setProduct_image_url(productObj.get(mContext.getString(R.string.db_field_product_image_url)));
                            userProduct.setProduct_description(productObj.get(mContext.getString(R.string.db_field_product_description)));
                            userProduct.setProduct_price(productObj.get(mContext.getString(R.string.db_field_product_price)));
                            userProduct.setProduct_warranty(productObj.get(mContext.getString(R.string.db_field_product_warranty)));
                            userProduct.setProduct_stock(productObj.get(mContext.getString(R.string.db_field_product_stock)));
                            userProduct.setTimeStamp(productObj.get(mContext.getString(R.string.db_field_timeStamp)));
                            userProduct.setProduct_id(productObj.get(mContext.getString(R.string.db_field_product_id)));
                            userProduct.setShop_id(productObj.get(mContext.getString(R.string.db_field_shop_id)));
                            userProduct.setType("");
                            if (productObj.get(mContext.getString(R.string.db_field_type)) != null) {
                                userProduct.setType(productObj.get(mContext.getString(R.string.db_field_type)));
                            }
                            userProduct.setProduct_rating(Long.parseLong(String.valueOf(productObj.get(mContext.getString(R.string.db_field_product_rating)))));

                            if (userProduct.getProduct_category().equals(queryString)) {
                                //add to the list for displaying to the user
                                userViewProductsList.add(userProduct);
                                //now notify the Home Fragment that new Products are Ready to display to the use
                            }
                            if (userProduct.getType().equals(queryString)) {
                                //add to the list for displaying to the user
                                userViewProductsList.add(userProduct);
                                //now notify the Home Fragment that new Products are Ready to display to the use
                            }

                        }
                    }

                    if (activityName.equals(mContext.getString(R.string.activity_unique_category))) {
                        ((UniqueCategoryActivity) mContext).setupIntentResources(userViewProductsList);
                    }
                    if (activityName.equals(mContext.getString(R.string.activity_sub_category))) {
                        ((SubCategoryActivity) mContext).setupIntentResources(userViewProductsList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG1234", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    /**
     * userViewProductsList refers to the previous ArrayList (may contain some Data Already)
     *
     * @param queryString
     * @param userViewProductsList
     */
    public void queryProducts(final String queryString
            , final ArrayList<Products> userViewProductsList
            , final RecyclerView.Adapter adapter) {

        final ArrayList<Products> tempUserViewProductList = new ArrayList<>();
        tempUserViewProductList.clear();
        Query productsNodeQuery = myRef
                .child(mContext.getString(R.string.db_products_node));
        productsNodeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (updateProgress != null)
                    updateProgress.setVisibility(View.VISIBLE);

                HashMap<String, Object> shopUsersList = (HashMap<String, Object>) dataSnapshot.getValue();
                if (shopUsersList != null) {
                    Collection<Object> objectsList = shopUsersList.values();
                    //obj is representing the list of products placed by the current user in obj
                    for (Object obj : objectsList) {
                        HashMap<String, Object> productList = (HashMap<String, Object>) obj;
                        Collection<Object> particularUserProducts = productList.values();
                        //product is representing the individual product placed by current user
                        for (Object product : particularUserProducts) {
                            HashMap<String, String> productObj = (HashMap<String, String>) product;
                            Products userProduct = new Products();
                            userProduct.setProduct_category(productObj.get(mContext.getString(R.string.db_field_product_category)));
                            userProduct.setProduct_name(productObj.get(mContext.getString(R.string.db_field_product_name)));
                            userProduct.setProduct_image_url(productObj.get(mContext.getString(R.string.db_field_product_image_url)));
                            userProduct.setProduct_description(productObj.get(mContext.getString(R.string.db_field_product_description)));
                            userProduct.setProduct_price(productObj.get(mContext.getString(R.string.db_field_product_price)));
                            userProduct.setProduct_warranty(productObj.get(mContext.getString(R.string.db_field_product_warranty)));
                            userProduct.setProduct_stock(productObj.get(mContext.getString(R.string.db_field_product_stock)));
                            userProduct.setTimeStamp(productObj.get(mContext.getString(R.string.db_field_timeStamp)));
                            userProduct.setProduct_id(productObj.get(mContext.getString(R.string.db_field_product_id)));
                            userProduct.setShop_id(productObj.get(mContext.getString(R.string.db_field_shop_id)));
                            userProduct.setProduct_rating(Long.parseLong(String.valueOf(productObj.get(mContext.getString(R.string.db_field_product_rating)))));

                            if (userProduct.getProduct_category().equals(queryString)) {
                                //add to the list for displaying to the user
                                tempUserViewProductList.add(userProduct);
                                //now notify the Home Fragment that new Products are Ready to display to the use
                            }

                        }
                    }
                    //Here Always a request come from the Fragments
                    if (activityName.equals(mContext.getString(R.string.carsFragment))
                            || activityName.equals(mContext.getString(R.string.mobileFragment))
                            || activityName.equals(mContext.getString(R.string.clothingFragment))
                            || activityName.equals(mContext.getString(R.string.cosmeticsFragment))
                            || activityName.equals(mContext.getString(R.string.electronicsFragment))
                            || activityName.equals(mContext.getString(R.string.fragrancesFragment))
                            || activityName.equals(mContext.getString(R.string.jewellaryFragment))) {

                        int previousProductsListSize = userViewProductsList.size();
                        int currentProductsListSize = tempUserViewProductList.size();
                        if (currentProductsListSize == previousProductsListSize) {
                            if (adapter instanceof ClothingProductsAdapter) {
                                ((ClothingProductsAdapter) adapter).setLoading();
                            }
                            if (adapter instanceof CarsFragmentAdapter) {
                                ((CarsFragmentAdapter) adapter).setInitialLoadingProgress();
                            }
                            if (adapter instanceof MobileProductsAdapter) {
                                ((MobileProductsAdapter) adapter).setLoading();
                            }
                            if (updateProgress != null)
                                updateProgress.setVisibility(View.GONE);
                        }
                        if (currentProductsListSize > previousProductsListSize) {
                            for (int i = 0; i < previousProductsListSize; i++) {
                                for (int j = 0; j < currentProductsListSize; j++) {
                                    if (userViewProductsList.get(i).getProduct_id().equals(tempUserViewProductList.get(j).getProduct_id())) {
                                        tempUserViewProductList.remove(j);
                                        break;
                                    }
                                }
                            }
                            userViewProductsList.addAll(tempUserViewProductList);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (updateProgress != null)
                                        updateProgress.setVisibility(View.GONE);
                                    currentChildReference.onUpdateRecyclerList(userViewProductsList);
                                }
                            }, 2000);

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG1234", "onCancelled: " + databaseError.getMessage());
                updateProgress.setVisibility(View.GONE);
            }
        });
    }

    public void initializeProductViews(final String currentUserID, final Products product) {
        myRef
                .child(mContext.getString(R.string.db_users_node))
                .orderByChild(mContext.getString(R.string.db_field_user_id))
                .equalTo(currentUserID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            HashMap<String, Object> map = (HashMap<String, Object>) ds.getValue();
                            Users users = new Users();
                            users.setUser_id((String) map.get(mContext.getString(R.string.db_field_user_id)));
                            users.setUser_name((String) map.get(mContext.getString(R.string.db_field_user_name)));

                            Views views = new Views(users.getUser_id(), users.getUser_name());
                            myRef
                                    .child(mContext.getString(R.string.db_views_node))
                                    .child(product.getProduct_id())
                                    .child(currentUserID)
                                    .setValue(views);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void initializeAddToCart(String currentUserID, Products product) {

        myRef
                .child(mContext.getString(R.string.db_cart_node))
                .child(currentUserID)
                .child(myRef.push().getKey())
                .setValue(product)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, mContext.getString(R.string.addedCart), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void setListenerForUpdatingRecyclerView(KeepHandleRecyclerList currentChildReference) {
        this.currentChildReference = currentChildReference;

    }

    public void updateShopProfileSettingsNode(ShopProfileSettings shopProfileSettings) {

        myRef.child(mContext.getString(R.string.db_shop_profile_settings_node))
                .child(mAuth.getCurrentUser().getUid())
                .setValue(shopProfileSettings);
    }

    public void placeNewOrder(String currentUserID, Products product, String orderStatus) {

        //future notify seller as well through Notification
        String orderID = myRef.push().getKey().substring(3, 13);
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy 'Time' HH:mm:ss", Locale.US).format(new Date());
        Orders order = new Orders(product.getProduct_name()
                , product.getProduct_price()
                , timeStamp
                , orderStatus
                , currentUserID
                , orderID
                , product.getProduct_id()
                , product.getShop_id());


        myRef
                .child(mContext.getString(R.string.db_orders_node))
                .child(currentUserID)
                .child(product.getShop_id())
                .child(orderID)
                .setValue(order)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(mContext, mContext.getString(R.string.successfully_created_order), Toast.LENGTH_SHORT).show();
                            if (activityName.equals(mContext.getString(R.string.activity_order_page)))
                                ((OrderPageActivity) mContext).finish();
                            if (activityName.equals(mContext.getString(R.string.activity_payment_gateway)))
                                ((PaymentGatewayActivity) mContext).finish();

                            //plus needs to remove the cart Product as well in future
                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.failure_created_order), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    public void setupUserWishlistProducts(final Products product) {


        myRef
                .child(mContext.getString(R.string.db_cart_node))
                .child(mAuth.getCurrentUser().getUid())
                .orderByChild(mContext.getString(R.string.db_field_product_id))
                .equalTo(product.getProduct_id())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if (!dataSnapshot.exists()) {

                            FirebaseMethods.this.initializeAddToCart(mAuth.getCurrentUser().getUid(), product);
                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.Already_Added_to_the_Cart), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public void setSellerViews(final MobileProductsAdapter.MyViewHolder viewHolder
            , String currentShopID) {

        myRef
                .child(mContext.getString(R.string.db_shop_profile_settings_node))
                .orderByChild(mContext.getString(R.string.db_field_user_id))
                .equalTo(currentShopID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                HashMap<String, Object> shopProfileSettings = (HashMap<String, Object>) ds.getValue();
                                viewHolder.sellingBy.setText((String) shopProfileSettings.get(mContext.getString(R.string.db_field_user_name)));
                                ImageLoader.getInstance().displayImage((String) shopProfileSettings.get(mContext.getString(R.string.db_field_profile_image_url)), viewHolder.sellerImage);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public void setSellerViews(final ElectronicsProductsAdapter.MyViewHolder viewHolder
            , String currentShopID) {

        myRef
                .child(mContext.getString(R.string.db_shop_profile_settings_node))
                .orderByChild(mContext.getString(R.string.db_field_user_id))
                .equalTo(currentShopID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                HashMap<String, Object> shopProfileSettings = (HashMap<String, Object>) ds.getValue();
                                viewHolder.sellingBy.setText((String) shopProfileSettings.get(mContext.getString(R.string.db_field_user_name)));
                                ImageLoader.getInstance().displayImage((String) shopProfileSettings.get(mContext.getString(R.string.db_field_profile_image_url)), viewHolder.sellerImage);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public void setSellerViews(final CosmeticsProductsAdapter.MyViewHolder viewHolder
            , String currentShopID) {

        myRef
                .child(mContext.getString(R.string.db_shop_profile_settings_node))
                .orderByChild(mContext.getString(R.string.db_field_user_id))
                .equalTo(currentShopID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                HashMap<String, Object> shopProfileSettings = (HashMap<String, Object>) ds.getValue();
                                viewHolder.sellingBy.setText((String) shopProfileSettings.get(mContext.getString(R.string.db_field_user_name)));
                                ImageLoader.getInstance().displayImage((String) shopProfileSettings.get(mContext.getString(R.string.db_field_profile_image_url)), viewHolder.sellerImage);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public void setSellerViews(final CarsFragmentAdapter.ViewHolder viewHolder
            , String currentShopID) {

        myRef
                .child(mContext.getString(R.string.db_shop_profile_settings_node))
                .orderByChild(mContext.getString(R.string.db_field_user_id))
                .equalTo(currentShopID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                HashMap<String, Object> shopProfileSettings = (HashMap<String, Object>) ds.getValue();
                                viewHolder.sellingBy.setText((String) shopProfileSettings.get(mContext.getString(R.string.db_field_user_name)));
                                ImageLoader.getInstance().displayImage((String) shopProfileSettings.get(mContext.getString(R.string.db_field_profile_image_url)), viewHolder.sellerImage);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public void filterInterestedShopProducts(final ArrayList<Products> shopRestrictedList
            , final String queryShopTerms
            , final Products product
            , final ClothingProductsAdapter adapter) {


        myRef
                .child(mContext.getString(R.string.db_shop_profile_settings_node))
                .orderByChild(mContext.getString(R.string.db_field_user_id))
                .equalTo(product.getShop_id())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (DataSnapshot shop : dataSnapshot.getChildren()) {
                            HashMap<String, Object> shopNode = (HashMap<String, Object>) shop.getValue();

                            if (shopNode.containsKey(mContext.getString(R.string.db_field_user_name))) {
                                if (shopNode.get(mContext.getString(R.string.db_field_user_name))
                                        .toString().contains(queryShopTerms)) {

                                    Log.d("TAG1234", "onDataChange: ");
                                    shopRestrictedList.add(product);
                                    adapter.setFilteredList(shopRestrictedList);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void filterInterestedShopProducts(final ArrayList<Products> shopRestrictedList
            , final String queryShopTerms
            , final Products product
            , final CarsFragmentAdapter adapter) {


        myRef
                .child(mContext.getString(R.string.db_shop_profile_settings_node))
                .orderByChild(mContext.getString(R.string.db_field_user_id))
                .equalTo(product.getShop_id())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (DataSnapshot shop : dataSnapshot.getChildren()) {
                            HashMap<String, Object> shopNode = (HashMap<String, Object>) shop.getValue();

                            if (shopNode.containsKey(mContext.getString(R.string.db_field_user_name))) {
                                if (shopNode.get(mContext.getString(R.string.db_field_user_name))
                                        .toString().contains(queryShopTerms)) {

                                    Log.d("TAG1234", "onDataChange: ");
                                    shopRestrictedList.add(product);
                                    adapter.setFilteredList(shopRestrictedList);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void filterInterestedShopProducts(final ArrayList<Products> shopRestrictedList
            , final String queryShopTerms
            , final Products product
            , final MobileProductsAdapter adapter) {


        myRef
                .child(mContext.getString(R.string.db_shop_profile_settings_node))
                .orderByChild(mContext.getString(R.string.db_field_user_id))
                .equalTo(product.getShop_id())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (DataSnapshot shop : dataSnapshot.getChildren()) {
                            HashMap<String, Object> shopNode = (HashMap<String, Object>) shop.getValue();

                            if (shopNode.containsKey(mContext.getString(R.string.db_field_user_name))) {
                                if (shopNode.get(mContext.getString(R.string.db_field_user_name))
                                        .toString().contains(queryShopTerms)) {

                                    Log.d("TAG1234", "onDataChange: ");
                                    shopRestrictedList.add(product);
                                    adapter.setFilteredList(shopRestrictedList);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void filterInterestedShopProducts(final ArrayList<Products> shopRestrictedList
            , final String queryShopTerms
            , final Products product
            , final CosmeticsProductsAdapter adapter) {


        myRef
                .child(mContext.getString(R.string.db_shop_profile_settings_node))
                .orderByChild(mContext.getString(R.string.db_field_user_id))
                .equalTo(product.getShop_id())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (DataSnapshot shop : dataSnapshot.getChildren()) {
                            HashMap<String, Object> shopNode = (HashMap<String, Object>) shop.getValue();

                            if (shopNode.containsKey(mContext.getString(R.string.db_field_user_name))) {
                                if (shopNode.get(mContext.getString(R.string.db_field_user_name))
                                        .toString().contains(queryShopTerms)) {

                                    Log.d("TAG1234", "onDataChange: ");
                                    shopRestrictedList.add(product);
                                    adapter.setFilteredList(shopRestrictedList);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void filterInterestedShopProducts(final ArrayList<Products> shopRestrictedList
            , final String queryShopTerms
            , final Products product
            , final ElectronicsProductsAdapter adapter) {


        myRef
                .child(mContext.getString(R.string.db_shop_profile_settings_node))
                .orderByChild(mContext.getString(R.string.db_field_user_id))
                .equalTo(product.getShop_id())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (DataSnapshot shop : dataSnapshot.getChildren()) {
                            HashMap<String, Object> shopNode = (HashMap<String, Object>) shop.getValue();

                            if (shopNode.containsKey(mContext.getString(R.string.db_field_user_name))) {
                                if (shopNode.get(mContext.getString(R.string.db_field_user_name))
                                        .toString().contains(queryShopTerms)) {

                                    Log.d("TAG1234", "onDataChange: ");
                                    shopRestrictedList.add(product);
                                    adapter.setFilteredList(shopRestrictedList);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void sendStripeTokenToFirebase(String stripe_token) {


        myRef
                .child(mContext.getString(R.string.db_users_node))
                .orderByChild(mContext.getString(R.string.db_field_user_id))
                .equalTo(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot singleUser : dataSnapshot.getChildren()) {

                            HashMap<String, Object> mMap = (HashMap<String, Object>) singleUser.getValue();

                            StripeToken token = new StripeToken((String) mMap.get(mContext.getString(R.string.db_field_user_id))
                                    , (String) mMap.get(mContext.getString(R.string.db_field_user_name))
                                    , (String) mMap.get(mContext.getString(R.string.db_field_scope))
                                    , stripe_token);

                            myRef
                                    .child(mContext.getString(R.string.db_stripeToken_node))
                                    .child((String) mMap.get(mContext.getString(R.string.db_field_user_id)))
                                    .setValue(token);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public void retrieveShopShippingInformation(Products product
            , Token token) {

        long price = Long.parseLong(product.getProduct_price().split(" ")[0]) * 100;//millis
        String currency = product.getProduct_price().split(" ")[1].toLowerCase();

        myRef
                .child(mContext.getString(R.string.db_shop_profile_settings_node))
                .orderByChild(mContext.getString(R.string.db_field_user_id))
                .equalTo(product.getShop_id())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot singleShop : dataSnapshot.getChildren()) {
                            Map<String, Object> mMap = (HashMap<String, Object>) singleShop.getValue();
                            StripeShipping stripeShipping = new StripeShipping();
                            StripeRecipientAddress address = new StripeRecipientAddress();

                            address.setCity((String) mMap.get(mContext.getString(R.string.db_field_city)));
                            address.setCountry((String) mMap.get(mContext.getString(R.string.db_field_country)));
                            address.setLine1((String) mMap.get(mContext.getString(R.string.db_field_shop_address)));
                            stripeShipping.setName((String) mMap.get(mContext.getString(R.string.db_field_user_name)));
                            stripeShipping.setPhone(null);
                            stripeShipping.setAddress(address);

                            //notify the activity to make a Post Api call
                            StripeCustomCharge charge = new StripeCustomCharge(token.getId()
                                    , currency
                                    , price
                                    , stripeShipping);
                            paymentNotifier.callbackActivityMakePostChargeRequest(charge);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void setStripeCustomCharge(PaymentNotifier paymentNotifier) {
        this.paymentNotifier = paymentNotifier;
    }
}


//        if(currentChildReference instanceof CarsFragment){
//
//        }


//                            if (currentProductsListSize < previousProductsListSize)
//                                for (int i = 0; i < currentProductsListSize; i++) {
//                                    if (!userViewProductsList.get(i).equals(tempUserViewProductList.get(i))) {
//                                        userViewProductsList.add(tempUserViewProductList.get(i));
//                                    }
//                                }
//                            Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (updateProgress != null)
//                                        updateProgress.setVisibility(View.GONE);
//                                    currentChildReference.onUpdateRecyclerList(userViewProductsList);
//                                }
//                            }, 2000);