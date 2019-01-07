package com.example.fasih.dukaanapp.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.fasih.dukaanapp.R;
import com.example.fasih.dukaanapp.home.activities.HomePageActivity;
import com.example.fasih.dukaanapp.login.LoginActivity;
import com.example.fasih.dukaanapp.models.UserAccountSettings;
import com.example.fasih.dukaanapp.models.Users;
import com.example.fasih.dukaanapp.register.RegisterActivity;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

/**
 * Created by Fasih on 01/04/19.
 */

public class FirebaseMethods {

    //Facebook Stuff
    AccessToken accessToken;
    //Google Stuff
    GoogleSignInAccount account;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private ProgressBar updateProgress;
    private String activityName;
    private Context mContext;

    public FirebaseMethods(Context context, String activityName) {
        this.mContext = context;
        this.activityName = activityName;
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
    }

    public void addNewUser(final String firstName, final String lastName, final String userName, final String email
            , final String password, final String phoneNumber, final String country) {

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
                                StringManipulations.toLowerCaseUsername(userName), email);

                        UserAccountSettings settings = new UserAccountSettings(mAuth.getCurrentUser().getUid(),
                                firstName, lastName
                                , StringManipulations.toLowerCaseUsername(userName)
                                , email, "");

                        myRef
                                .child(mContext.getString(R.string.db_users_node))
                                .child(task1.getResult().getUser().getUid())
                                .setValue(users);

                        myRef
                                .child(mContext.getString(R.string.db_user_profile_settings_node))
                                .child(task1.getResult().getUser().getUid())
                                .setValue(settings);

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

    public void authenticateUser(String username, final String email, final String password) {
        if (TextUtils.isEmpty(username)) {
            //Mean Email is provided by the User
            if (updateProgress != null) {
                updateProgress.setVisibility(View.VISIBLE);
                mAuth
                        .signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                if (authResult.getUser().isEmailVerified()) {
                                    updateProgress.setVisibility(View.GONE);
                                    if (activityName.equals(mContext.getString(R.string.activity_login))) {
                                        mContext.startActivity(new Intent(mContext, HomePageActivity.class));
                                        ((LoginActivity) mContext).finishAffinity();
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
        if (TextUtils.isEmpty(email)) {
            //Mean Username is provided by the User
            if (updateProgress != null) {
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
                                                    mContext.startActivity(new Intent(mContext, HomePageActivity.class));
                                                    ((LoginActivity) mContext).finishAffinity();
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
            }

        }
    }

    public void updateProgress(ProgressBar registerProgress) {

        if (registerProgress != null) {
            this.updateProgress = registerProgress;
        }
    }

    public void setupFacebookLoginWithAccessToken(final AccessToken accessToken) {
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
                                                , task.getResult().getUser().getEmail());

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
                                    mContext.startActivity(new Intent(mContext, HomePageActivity.class));
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

    public void setupGoogleLoginWithAccount(final GoogleSignInAccount account) {
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
                                                , task.getResult().getUser().getEmail());

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

                                    mContext.startActivity(new Intent(mContext, HomePageActivity.class));

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
}
