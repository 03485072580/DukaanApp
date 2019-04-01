package com.example.fasih.dukaanapp.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.activities.SellerHomePageActivity;
import com.example.fasih.dukaanapp.home.activities.UserHomePageActivity;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.ProgressDialogFragment;
import com.example.fasih.dukaanapp.home.fragments.sellerPageResources.SearchUsernameFragment;
import com.example.fasih.dukaanapp.login.activity.LoginActivity;
import com.example.fasih.dukaanapp.models.Products;
import com.example.fasih.dukaanapp.models.ShopProfileSettings;
import com.example.fasih.dukaanapp.models.UserAccountSettings;
import com.example.fasih.dukaanapp.models.Users;
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

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by Fasih on 01/04/19.
 */

public class FirebaseMethods {

    //Facebook Stuff
    AccessToken accessToken;
    //Google Stuff
    GoogleSignInAccount account;
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

    public FirebaseMethods(Context context, String activityName) {
        this.mContext = context;
        this.activityName = activityName;
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        myRef = firebaseDatabase.getReference();
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
        Log.d("TAG1234", "addNewUser: " + mallUniqueID);

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
            , final String timeStamp) {
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
                Log.d("TAG1234", "run: " + imageLoadingUrl);
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

                        Products product = new Products(productName, selectedCategory, downloadUri.toString()
                                , productDescription, productPrice, productWarranty
                                , availableStock, timeStamp, productID, -1);
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
                                            Log.d("TAG1234", "onComplete: Successfully Created");
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
                    Log.d("TAG1234", "onComplete: " + downloadUrl);
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
                                Log.d("TAG1234", "onComplete: " + shopSettings.toString());
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
        Log.d("TAG1234", "searchQueryTextByUsername: " + query);
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
}
